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
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.libideas.resources.client.ImageResource.ImageOptions;
import com.google.gwt.libideas.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.libideas.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.libideas.resources.rebind.AbstractResourceGenerator;
import com.google.gwt.libideas.resources.rebind.FieldAccumulator;
import com.google.gwt.libideas.resources.rebind.ResourceContext;
import com.google.gwt.libideas.resources.rebind.ResourceGeneratorUtil;
import com.google.gwt.libideas.resources.rebind.StringSourceWriter;
import com.google.gwt.libideas.resources.rg.ImageBundleBuilder.Arranger;
import com.google.gwt.libideas.resources.rg.ImageBundleBuilder.ImageRect;
import com.google.gwt.user.rebind.SourceWriter;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Builds an image strip for all ImageResources defined within an
 * ImmutableResourceBundle.
 */
public final class ImageResourceGenerator extends AbstractResourceGenerator {
  private ResourceContext context;
  private Map<RepeatStyle, ImageBundleBuilder> builders;
  private Map<String, String> externalLocationExpressions;
  private Map<String, ImageRect> externalImageRects;
  private Map<RepeatStyle, String> imageResourceBundleUrlIdents;

  @Override
  public String createAssignment(TreeLogger logger, JMethod method)
      throws UnableToCompleteException {
    String name = method.getName();

    SourceWriter sw = new StringSourceWriter();
    sw.println("new " + ImageResourcePrototype.class.getName() + "(");
    sw.indent();
    sw.println('"' + name + "\",");

    ImageBundleBuilder.ImageRect rect;
    if (!externalImageRects.containsKey(name)) {
      // This is a reference to a field
      sw.println(imageResourceBundleUrlIdents.get(getRepeatStyle(method)) + ",");
      rect = getBuilder(method).getMapping(method.getName());
    } else {
      sw.println(externalLocationExpressions.get(name).toString() + ", ");
      rect = externalImageRects.get(name);
    }

    if (rect == null) {
      logger.log(TreeLogger.ERROR, "No ImageRect ever computed for " + name,
          null);
      throw new UnableToCompleteException();
    }

    sw.println(rect.getLeft() + ", " + rect.getTop() + ", " + rect.getWidth()
        + ", " + rect.getHeight());

    sw.outdent();
    sw.print(")");

    return sw.toString();
  }

  @Override
  public void createFields(TreeLogger logger, FieldAccumulator fields)
      throws UnableToCompleteException {
    for (Map.Entry<RepeatStyle, ImageBundleBuilder> entry : builders.entrySet()) {
      RepeatStyle repeatStyle = entry.getKey();
      ImageBundleBuilder builder = entry.getValue();
      Arranger arranger;

      switch (repeatStyle) {
        case None:
          arranger = new ImageBundleBuilder.BestFitArranger();
          break;
        case Horizontal:
          arranger = new ImageBundleBuilder.VerticalArranger();
          break;
        case Vertical:
          arranger = new ImageBundleBuilder.HorizontalArranger();
          break;
        case Both:
          // This is taken care of when writing the external images;
          continue;
        default:
          logger.log(TreeLogger.ERROR, "Unknown RepeatStyle" + repeatStyle);
          throw new UnableToCompleteException();
      }

      String bundleUrlExpression = builder.writeBundledImage(logger.branch(
          TreeLogger.DEBUG, "Writing image strip", null), context, arranger);

      if (bundleUrlExpression == null) {
        continue;
      }

      TypeOracle typeOracle = context.getGeneratorContext().getTypeOracle();
      JClassType stringType = typeOracle.findType(String.class.getName());
      assert stringType != null;

      imageResourceBundleUrlIdents.put(repeatStyle, fields.addField(stringType,
          "imageResourceBundleUrl" + repeatStyle, bundleUrlExpression, true,
          true));
    }
  }

  @Override
  public void init(TreeLogger logger, ResourceContext context) {
    this.context = context;
    builders = new HashMap<RepeatStyle, ImageBundleBuilder>();
    externalLocationExpressions = new HashMap<String, String>();
    externalImageRects = new HashMap<String, ImageRect>();
    imageResourceBundleUrlIdents = new HashMap<RepeatStyle, String>();
  }

  @Override
  public void prepare(TreeLogger logger, JMethod method)
      throws UnableToCompleteException {
    URL[] resources = ResourceGeneratorUtil.findResources(logger, context,
        method);

    if (resources.length != 1) {
      logger.log(TreeLogger.ERROR, "Exactly one image may be specified", null);
      throw new UnableToCompleteException();
    }

    ImageBundleBuilder builder = getBuilder(method);
    URL resource = resources[0];
    String name = method.getName();

    try {
      builder.assimilate(logger, name, resource);
      if (context.supportsDataUrls()
          || getRepeatStyle(method) == RepeatStyle.Both) {
        // Just use the calculated meta-data
        ImageRect r = builder.removeMapping(name);
        r.setPosition(0, 0);
        throw new UnsuitableForStripException(r);
      }
    } catch (UnsuitableForStripException e) {
      // Add the image to the output as a separate resource
      String urlExpression = context.addToOutput(resource, false);
      externalLocationExpressions.put(name, urlExpression);
      externalImageRects.put(name, e.getImageRect());
    }
  }

  private ImageBundleBuilder getBuilder(JMethod method) {
    RepeatStyle repeatStyle = getRepeatStyle(method);
    ImageBundleBuilder builder = builders.get(repeatStyle);
    if (builder == null) {
      builder = new ImageBundleBuilder();
      builders.put(repeatStyle, builder);
    }
    return builder;
  }

  private RepeatStyle getRepeatStyle(JMethod method) {
    ImageOptions options = method.getAnnotation(ImageOptions.class);
    if (options == null) {
      return RepeatStyle.None;
    } else {
      return options.repeatStyle();
    }
  }
}
