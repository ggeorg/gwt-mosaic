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
package com.google.gwt.libideas.resources.client;

import com.google.gwt.libideas.resources.rebind.ResourceGeneratorType;
import com.google.gwt.libideas.resources.rg.ImageResourceGenerator;
import com.google.gwt.user.client.ui.Image;

/**
 * Test for ImageBundle optimizations.
 */
@ResourceGeneratorType(ImageResourceGenerator.class)
public interface ImageResource extends ResourcePrototype {

  /**
   * Transforms an existing {@link Image} into the image represented by this
   * prototype.
   * 
   * @param image the instance to be transformed to match this prototype
   */
  void applyTo(Image image);

  /**
   * Creates a new {@link Image} instance based on the image represented by this
   * prototype.
   * 
   * @return a new <code>Image</code> based on this prototype
   */
  Image createImage();

  /**
   * Gets an HTML fragment that displays the image represented by this
   * prototype. The HTML returned is not necessarily a simple
   * <code>&lt;img&gt;</code> element. It may be a more complex structure that
   * should be treated opaquely.
   * 
   * @return the HTML representation of this prototype
   */
  String getHTML();
}
