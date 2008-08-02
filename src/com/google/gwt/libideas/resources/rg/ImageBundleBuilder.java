/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.libideas.resources.rg;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.util.Util;
import com.google.gwt.libideas.resources.rebind.ResourceContext;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * Accumulates state for the bundled image. This is a slighly-modified version
 * of the ImageBundleBuilder class from the GWT UI package.
 */
class ImageBundleBuilder {

  /**
   * The rectangle at which the original image is placed into the composite
   * image.
   */
  public static class ImageRect {

    public final int height;
    public final BufferedImage image;
    public int left;
    public final int width;
    public final String strongName;

    public ImageRect(BufferedImage image, String strongName) {
      this.image = image;
      this.width = image.getWidth();
      this.height = image.getHeight();
      this.strongName = strongName;
    }

    public boolean equals(Object o) {
      return ((o instanceof ImageRect) && (((ImageRect) o).strongName.equals(strongName)));
    }

    public int hashCode() {
      return strongName.hashCode();
    }

    public String toString() {
      return strongName + " " + width + "x" + height;
    }
  }

  /*
   * Only PNG is supported right now. In the future, we may be able to infer the
   * best output type, and get rid of this constant.
   */
  private static final String BUNDLE_FILE_TYPE = "png";
  private static final String BUNDLE_MIME_TYPE = "image/png";

  private final Map<String, ImageRect> strongNameToRectMap = new HashMap<String, ImageRect>();
  private final Map<String, ImageRect> nameToRectMap = new HashMap<String, ImageRect>();

  /**
   * Assimilates the image associated with a particular image method into the
   * master composite. If the method names an image that has already been
   * assimilated, the existing image rectangle is reused.
   * 
   * @param logger a hierarchical logger which logs to the hosted console
   * @param imageName the name of an image that can be found on the classpath
   * @param resource The URL from which the contents of the image can be
   *          obtained
   * @throws UnableToCompleteException if the image with name
   *           <code>imageName</code> cannot be added to the master composite
   *           image
   */
  public void assimilate(TreeLogger logger, String imageName, URL resource)
      throws UnableToCompleteException, UnsuitableForStripException {

    /*
     * Decide whether or not we need to add to the composite image. Either way,
     * we associated it with the rectangle of the specified image as it exists
     * within the composite image. Note that the coordinates of the rectangle
     * aren't computed until the composite is written.
     */
    ImageRect rect = getMapping(imageName);
    if (rect == null) {
      // Assimilate the image into the composite.
      rect = readImage(logger, resource);

      checkSuitableForStrip(logger, imageName, resource, rect);

      // De-dup the component images
      if (strongNameToRectMap.containsKey(rect.strongName)) {
        rect = strongNameToRectMap.get(rect.strongName);
      } else {
        strongNameToRectMap.put(rect.strongName, rect);
      }

      // Map the URL to its image so that even if the same URL is used more than
      // once, we only include the referenced image once in the bundled image.
      putMapping(imageName, rect);
    }
  }

  public ImageRect getMapping(String imageName) {
    return nameToRectMap.get(imageName);
  }

  public String writeBundledImage(TreeLogger logger, ResourceContext context)
      throws UnableToCompleteException {

    logger = logger.branch(TreeLogger.DEBUG, "Writing bundle image", null);

    // Create the bundled image from all of the constituent images.
    BufferedImage bundledImage = drawBundledImage();

    // Write the bundled image into a byte array, so that we can compute
    // its strong name.
    byte[] imageBytes;

    try {
      ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bundledImage, BUNDLE_FILE_TYPE, byteOutputStream);
      imageBytes = byteOutputStream.toByteArray();
    } catch (IOException e) {
      logger.log(TreeLogger.ERROR,
          "Unable to generate file name for image bundle file", null);
      throw new UnableToCompleteException();
    }

    return context.addToOutput(context.getResourceBundleType().getName()
        + ".cache.png", BUNDLE_MIME_TYPE, imageBytes, false);
  }

  /**
   * Enforce heuristics about whether or not an image should be added to an
   * image strip, or left as a separate resource request.
   * 
   * @throws UnsuitableForStripException if the resource should not be added to
   *           an image strip
   */
  private void checkSuitableForStrip(TreeLogger logger, String imageName,
      URL resource, ImageRect rect) throws UnsuitableForStripException {

    // PNG images won't get any bigger, so we may as well include them in the
    // strip
    if (resource.getPath().endsWith("png")) {
      return;
    }

    if (rect.width * rect.height > 128 * 128) {
      logger.log(TreeLogger.DEBUG, "Not adding " + imageName
          + " to strip because it is large.", null);
      throw new UnsuitableForStripException(rect);
    }
  }

  /*
   * This method creates the bundled image through the composition of the other
   * images.
   * 
   * This method could be implemented in a variety of ways. For example, one
   * could use a knapsack algorithm to draw these images in an optimal amount of
   * space.
   * 
   * In this particular implementation, we iterate through the image rectangles
   * in ascending order of associated filename, and draw the rectangles from
   * left to right in a single row.
   * 
   * The most important aspect of drawing the bundled image is that it be drawn
   * in a deterministic way. The drawing of the image should not rely on
   * implementation details of the Generator system which may be subject to
   * change. For example, at the time of this writing, the image names are added
   * to nameToRectMap based on the alphabetical ordering of their associated
   * methods. This behavior is the result of the oracle returning the list of a
   * type's methods in alphabetical order. However, this behavior is
   * undocumented, and should not be relied on. If this behavior were to change,
   * it would inadvertently affect the generation of bundled images.
   */
  private BufferedImage drawBundledImage() {

    // Impose an ordering on the image rectangles, so that we construct
    // the bundled image in a deterministic way.
    SortedMap<String, ImageRect> sortedNameToRectMap = new TreeMap<String, ImageRect>();
    sortedNameToRectMap.putAll(strongNameToRectMap);
    Collection<ImageRect> orderedImageRects = sortedNameToRectMap.values();

    // Determine how big the composited image should be by taking the
    // sum of the widths and the max of the heights.
    int nextLeft = 0;
    int maxHeight = 0;
    for (ImageRect imageRect : orderedImageRects) {
      imageRect.left = nextLeft;
      nextLeft += imageRect.width;
      maxHeight = Math.max(maxHeight, imageRect.height);
    }

    // Create the bundled image.
    BufferedImage bundledImage = new BufferedImage(nextLeft, maxHeight,
        BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g2d = bundledImage.createGraphics();

    for (ImageRect imageRect : orderedImageRects) {

      // We do not need to pass in an ImageObserver, because we are working
      // with BufferedImages. ImageObservers only need to be used when
      // the image to be drawn is being loaded asynchronously. See
      // http://java.sun.com/docs/books/tutorial/2d/images/drawimage.html
      // for more information.
      g2d.drawImage(imageRect.image, imageRect.left, 0, null);
    }
    g2d.dispose();

    return bundledImage;
  }

  private void putMapping(String imageName, ImageRect rect) {
    nameToRectMap.put(imageName, rect);
  }

  private ImageRect readImage(TreeLogger logger, URL imageUrl)
      throws UnableToCompleteException {

    logger = logger.branch(TreeLogger.TRACE, "Adding image '"
        + imageUrl.toExternalForm() + "'", null);

    // Fetch the image.
    byte[] imageBytes = Util.readURLAsBytes(imageUrl);

    try {
      BufferedImage image;
      // Decode the image
      try {
        image = ImageIO.read(new ByteArrayInputStream(imageBytes));
      } catch (IllegalArgumentException iex) {
        if (imageUrl.getPath().toLowerCase().endsWith("png")
            && iex.getMessage() != null
            && iex.getStackTrace()[0].getClassName().equals(
                "javax.imageio.ImageTypeSpecifier$Indexed")) {
          logger.log(
              TreeLogger.ERROR,
              "Unable to read image. The image may not be in valid PNG format. "
                  + "This problem may also be due to a bug in versions of the "
                  + "JRE prior to 1.6. See "
                  + "http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5098176 "
                  + "for more information. If this bug is the cause of the "
                  + "error, try resaving the image using a different image "
                  + "program, or upgrade to a newer JRE.", null);
          throw new UnableToCompleteException();
        } else {
          throw iex;
        }
      }

      if (image == null) {
        logger.log(TreeLogger.ERROR, "Unrecognized image file format", null);
        throw new UnableToCompleteException();
      }

      return new ImageRect(image, Util.computeStrongName(imageBytes));

    } catch (IOException e) {
      logger.log(TreeLogger.ERROR, "Unable to read image resource", null);
      throw new UnableToCompleteException();
    }
  }
}
