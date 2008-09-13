/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class ImageButton extends PushButton {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ImageButton";

  private Image image;

  public ImageButton(Image image) {
    super(image);
    this.image = image;
    setStyleName(DEFAULT_STYLENAME);
  }

  public ImageButton(AbstractImagePrototype image) {
    this(image.createImage());
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
    getUpFace().setImage(image);
    setStyleName(DEFAULT_STYLENAME);
  }

}
