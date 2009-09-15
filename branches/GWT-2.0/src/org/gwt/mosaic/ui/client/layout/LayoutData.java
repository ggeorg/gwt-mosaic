/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

/**
 * Base class for all layout data objects.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class LayoutData {

  final DecoratorPanel decoratorPanel;

  /**
   * Creates a new instance of {@code LayoutData} by specifying that the
   * associated widget should be decorated.
   * 
   * @param decorate specifies whether the associated widget will be decorated
   *          or not.
   */
  protected LayoutData(final boolean decorate) {
    if (decorate) {
      decoratorPanel = new DecoratorPanel();
    } else {
      decoratorPanel = null;
    }
  }

  /**
   * If the child widget is decorated (if the child widget is placed in a
   * {@code com.google.gwt.user.client.ui.DecoratorPanel}), this method returns
   * {@code true}, if not this method will return {@code false}.
   * 
   * @return {@code true} if the child widget is placed in a {@code
   *         com.google.gwt.user.client.ui.DecoratorPanel}, {@code false}
   *         otherwise.
   */
  public final boolean hasDecoratorPanel() {
    return decoratorPanel != null;
  }

  // ----

  protected String preferredWidth;

  protected String preferredHeight;

  /**
   * @return the preferredWidth
   */
  public String getPreferredWidth() {
    return preferredWidth;
  }

  /**
   * @param preferredWidth the preferredWidth to set
   */
  public void setPreferredWidth(String preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  /**
   * @return the preferredHeight
   */
  public String getPreferredHeight() {
    return preferredHeight;
  }

  /**
   * @param preferredHeight the preferredHeight to set
   */
  public void setPreferredHeight(String preferredHeight) {
    this.preferredHeight = preferredHeight;
  }

  // ----

  public void setPreferredSize(String preferredWidth, String preferredHeight) {
    this.preferredWidth = preferredWidth;
    this.preferredHeight = preferredHeight;
  }

  // ----

  protected int targetLeft, targetTop, targetWidth, targetHeight;

  protected int left, top, width, height;
  protected int sourceLeft, sourceTop, sourceWidth, sourceHeight;

}
