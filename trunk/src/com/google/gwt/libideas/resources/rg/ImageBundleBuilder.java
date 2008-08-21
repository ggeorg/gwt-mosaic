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
import com.google.gwt.libideas.resources.rebind.ResourceContext;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Accumulates state for the bundled image.
 */
class ImageBundleBuilder {
  public interface Arranger {
    Size arrangeImages(Collection<? extends HasRect> rects);

    void place(Size canvasSize, Graphics2D canvas, ImageRect imageRect);
  }

  /**
   * The rectangle at which the original image is placed into the composite
   * image.
   */
  public static class ImageRect implements HasRect {

    private final String name;
    private final int height, width;
    private final BufferedImage image;
    private int left, top;

    private boolean hasBeenPositioned;

    public ImageRect(String name, BufferedImage image) {
      this.name = name;
      this.image = image;
      this.width = image.getWidth();
      this.height = image.getHeight();
    }

    public int getHeight() {
      return height;
    }

    public int getLeft() {
      return left;
    }

    public String getName() {
      return name;
    }

    public int getTop() {
      return top;
    }

    public int getWidth() {
      return width;
    }

    public boolean hasBeenPositioned() {
      return hasBeenPositioned;
    }

    public void setPosition(int left, int top) {
      hasBeenPositioned = true;
      this.left = left;
      this.top = top;
    }
  }

  static class BestFitArranger implements Arranger {
    private static final Comparator<HasRect> decreasingHeightComparator = new Comparator<HasRect>() {
      public int compare(HasRect a, HasRect b) {
        final int c = b.getHeight() - a.getHeight();
        // If we encounter equal heights, use the name to keep things
        // deterministic.
        return (c != 0) ? c : b.getName().compareTo(a.getName());
      }
    };

    private static final Comparator<HasRect> decreasingWidthComparator = new Comparator<HasRect>() {
      public int compare(HasRect a, HasRect b) {
        final int c = b.getWidth() - a.getWidth();
        // If we encounter equal heights, use the name to keep things
        // deterministic.
        return (c != 0) ? c : b.getName().compareTo(a.getName());
      }
    };

    /**
     * Arranges the images to try to decrease the overall area of the resulting
     * bundle. This uses a strategy that is basically Next-Fit Decreasing Height
     * Decreasing Width (NFDHDW). The rectangles to be packed are sorted in
     * decreasing order by height. The tallest rectangle is placed at the far
     * left. We attempt to stack the remaining rectangles on top of one another
     * to construct as many columns as necessary. After finishing each column,
     * we also attempt to do some horizontal packing to fill up the space left
     * due to widths of rectangles differing in the column.
     */
    public Size arrangeImages(Collection<? extends HasRect> rects) {
      if (rects.size() == 0) {
        return new Size(0, 0);
      }

      // Create a list of ImageRects ordered by decreasing height used for
      // constructing columns.
      final ArrayList<HasRect> rectsOrderedByHeight = new ArrayList<HasRect>(
          rects);
      Collections.sort(rectsOrderedByHeight, decreasingHeightComparator);

      // Create a list of ImageRects ordered by decreasing width used for
      // packing
      // individual columns.
      final ArrayList<HasRect> rectsOrderedByWidth = new ArrayList<HasRect>(
          rects);
      Collections.sort(rectsOrderedByWidth, decreasingWidthComparator);

      // Place the first, tallest image as the first column.
      final HasRect first = rectsOrderedByHeight.get(0);
      first.setPosition(0, 0);

      // Setup state for laying things cumulatively.
      int curX = first.getWidth();
      final int colH = first.getHeight();

      for (int i = 1, n = rectsOrderedByHeight.size(); i < n; i++) {
        // If this ImageRect has been positioned already, move on.
        if (rectsOrderedByHeight.get(i).hasBeenPositioned()) {
          continue;
        }

        int colW = 0;
        int curY = 0;

        final ArrayList<HasRect> rectsInColumn = new ArrayList<HasRect>();
        for (int j = i; j < n; j++) {
          final HasRect current = rectsOrderedByHeight.get(j);
          // Look for rects that have not been positioned with a small enough
          // height to go in this column.
          if (!current.hasBeenPositioned()
              && (curY + current.getHeight()) <= colH) {

            // Set the horizontal position here, the top field will be set in
            // arrangeColumn after we've collected a full set of ImageRects.
            current.setPosition(curX, 0);
            colW = Math.max(colW, current.getWidth());
            curY += current.getHeight();

            // Keep the ImageRects in this column in decreasing order by width.
            final int pos = Collections.binarySearch(rectsInColumn, current,
                decreasingWidthComparator);
            assert pos < 0;
            rectsInColumn.add(-1 - pos, current);
          }
        }

        // Having selected a set of ImageRects that fill out this column
        // vertical,
        // now we'll scan the remaining ImageRects to try to fit some in the
        // horizontal gaps.
        if (!rectsInColumn.isEmpty()) {
          arrangeColumn(rectsInColumn, rectsOrderedByWidth);
        }

        // We're done with that column, so move the horizontal accumulator by
        // the
        // width of the column we just finished.
        curX += colW;
      }

      return new Size(curX, colH);
    }

    public void place(Size canvasSize, Graphics2D canvas, ImageRect imageRect) {
      canvas.drawImage(imageRect.image, imageRect.left, imageRect.top, null);
    }

    /**
     * Companion method to {@link #arrangeImages()}. This method does a best
     * effort horizontal packing of a column after it was packed vertically.
     * This is the Decreasing Width part of Next-Fit Decreasing Height
     * Decreasing Width. The basic strategy is to sort the remaining rectangles
     * by decreasing width and try to fit them to the left of each of the
     * rectangles we've already picked for this column.
     * 
     * @param rectsInColumn the ImageRects that were already selected for this
     *          column
     * @param remainingRectsOrderedByWidth the sub list of ImageRects that may
     *          not have been positioned yet
     */
    private void arrangeColumn(List<HasRect> rectsInColumn,
        List<HasRect> remainingRectsOrderedByWidth) {
      final HasRect first = rectsInColumn.get(0);

      final int columnWidth = first.getWidth();
      int curY = first.getHeight();

      // Skip this first ImageRect because it is guaranteed to consume the full
      // width of the column.
      for (int i = 1, m = rectsInColumn.size(); i < m; i++) {
        final HasRect r = rectsInColumn.get(i);
        // The ImageRect was previously positioned horizontally, now set the top
        // field.
        r.setPosition(r.getLeft(), curY);
        int curX = r.getWidth();

        // Search for ImageRects that are shorter than the left most ImageRect
        // and
        // narrow enough to fit in the column.
        for (int j = 0, n = remainingRectsOrderedByWidth.size(); j < n; j++) {
          final HasRect current = remainingRectsOrderedByWidth.get(j);
          if (!current.hasBeenPositioned()
              && (curX + current.getWidth()) <= columnWidth
              && (current.getHeight() <= r.getHeight())) {
            current.setPosition(r.getLeft() + curX, r.getTop());
            curX += current.getWidth();
          }
        }

        // Update the vertical accumulator so we'll know where to place the next
        // ImageRect.
        curY += r.getHeight();
      }
    }
  }

  /**
   * A mockable interface to test the image arrangement algorithms.
   */
  interface HasRect {

    int getHeight();

    int getLeft();

    String getName();

    int getTop();

    int getWidth();

    boolean hasBeenPositioned();

    void setPosition(int left, int top);
  }

  /**
   * Performs a simple horizontal arrangement of rectangles. Images will be
   * tiled vertically to fill to fill the full height of the image.
   */
  static class HorizontalArranger implements Arranger {
    public Size arrangeImages(Collection<? extends HasRect> rects) {
      int height = 1;
      int width = 0;

      for (HasRect rect : rects) {
        rect.setPosition(width, 0);
        width += rect.getWidth();
        height = lcm(height, rect.getHeight());
      }

      return new Size(width, height);
    }

    /**
     * Tiles the images vertically.
     */
    public void place(Size canvasSize, Graphics2D canvas, ImageRect imageRect) {
      int y = 0;
      while (y < canvasSize.height) {
        canvas.drawImage(imageRect.image, imageRect.left, y, null);
        y += imageRect.height;
      }
    }
  }

  /**
   * Used to return the size of the resulting image from the method
   * {@link ImageBundleBuilder#arrangeImages()}.
   */
  static class Size {
    private final int width, height;

    Size(int width, int height) {
      this.width = width;
      this.height = height;
    }
  }

  /**
   * Performs a simple vertical arrangement of rectangles. Images will be tiled
   * horizontally to fill the full width of the image.
   */
  static class VerticalArranger implements Arranger {
    public Size arrangeImages(Collection<? extends HasRect> rects) {
      int height = 0;
      int width = 1;

      for (HasRect rect : rects) {
        rect.setPosition(0, height);
        width = lcm(width, rect.getWidth());
        height += rect.getHeight();
      }

      return new Size(width, height);
    }

    /**
     * Tiles the images horizontally.
     */
    public void place(Size canvasSize, Graphics2D canvas, ImageRect imageRect) {
      int x = 0;
      while (x < canvasSize.width) {
        canvas.drawImage(imageRect.image, x, imageRect.top, null);
        x += imageRect.width;
      }
    }
  }

  /*
   * Only PNG is supported right now. In the future, we may be able to infer the
   * best output type, and get rid of this constant.
   */
  private static final String BUNDLE_FILE_TYPE = "png";
  private static final String BUNDLE_MIME_TYPE = "image/png";
  private static final int IMAGE_MAX_SIZE = 256;

  static int gcd(int a, int b) {
    while (b != 0) {
      int t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  static int lcm(int a, int b) {
    return b / gcd(a, b) * a;
  }

  private final Map<String, ImageRect> imageNameToImageRectMap = new HashMap<String, ImageRect>();

  /**
   * Assimilates the image associated with a particular image method into the
   * master composite. If the method names an image that has already been
   * assimilated, the existing image rectangle is reused.
   * 
   * @param logger a hierarchical logger which logs to the hosted console
   * @param imageName the name of an image that can be found on the classpath
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
      rect = addImage(logger, imageName, resource);

      // Map the URL to its image so that even if the same URL is used more than
      // once, we only include the referenced image once in the bundled image.
      putMapping(imageName, rect);
    }
  }

  public ImageRect getMapping(String imageName) {
    return imageNameToImageRectMap.get(imageName);
  }

  public ImageRect removeMapping(String imageName) {
    return imageNameToImageRectMap.remove(imageName);
  }

  public String writeBundledImage(TreeLogger logger, ResourceContext context,
      Arranger arranger) throws UnableToCompleteException {

    if (imageNameToImageRectMap.isEmpty()) {
      return null;
    }

    // Create the bundled image from all of the constituent images.
    BufferedImage bundledImage = drawBundledImage(arranger);

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

    String bundleFileName = context.addToOutput(
        context.getResourceBundleType().getQualifiedSourceName() + ".cache."
            + BUNDLE_FILE_TYPE, BUNDLE_MIME_TYPE, imageBytes, false);

    return bundleFileName;
  }

  private ImageRect addImage(TreeLogger logger, String imageName, URL imageUrl)
      throws UnableToCompleteException, UnsuitableForStripException {

    logger = logger.branch(TreeLogger.TRACE,
        "Adding image '" + imageName + "'", null);

    BufferedImage image;
    // Load the image
    try {
      image = ImageIO.read(imageUrl);
    } catch (IllegalArgumentException iex) {
      if (imageName.toLowerCase().endsWith("png")
          && iex.getMessage() != null
          && iex.getStackTrace()[0].getClassName().equals(
              "javax.imageio.ImageTypeSpecifier$Indexed")) {
        logger.log(TreeLogger.ERROR,
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
    } catch (IOException e) {
      logger.log(TreeLogger.ERROR, "Unable to read image resource", null);
      throw new UnableToCompleteException();
    }

    if (image == null) {
      logger.log(TreeLogger.ERROR, "Unrecognized image file format", null);
      throw new UnableToCompleteException();
    }

    ImageRect toReturn = new ImageRect(imageName, image);

    if (toReturn.height > IMAGE_MAX_SIZE || toReturn.width > IMAGE_MAX_SIZE) {
      throw new UnsuitableForStripException(toReturn);
    }

    return toReturn;
  }

  /**
   * This method creates the bundled image through the composition of the other
   * images.
   * 
   * In this particular implementation, we use NFDHDW (see
   * {@link #arrangeImages()}) to get an approximate optimal image packing.
   * 
   * The most important aspect of drawing the bundled image is that it be drawn
   * in a deterministic way. The drawing of the image should not rely on
   * implementation details of the Generator system which may be subject to
   * change.
   */
  private BufferedImage drawBundledImage(Arranger arranger) {

    // There is no need to impose any order here, because arrangeImages
    // will position the ImageRects in a deterministic fashion, even though
    // we might paint them in a non-deterministic order.
    Collection<ImageRect> imageRects = imageNameToImageRectMap.values();

    // Arrange images and determine the size of the resulting bundle.
    Size size = arranger.arrangeImages(imageRects);

    // Create the bundled image.
    BufferedImage bundledImage = new BufferedImage(size.width, size.height,
        BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g2d = bundledImage.createGraphics();

    for (ImageRect imageRect : imageRects) {
      arranger.place(size, g2d, imageRect);
    }
    g2d.dispose();

    return bundledImage;
  }

  private void putMapping(String imageName, ImageRect rect) {
    imageNameToImageRectMap.put(imageName, rect);
  }
}
