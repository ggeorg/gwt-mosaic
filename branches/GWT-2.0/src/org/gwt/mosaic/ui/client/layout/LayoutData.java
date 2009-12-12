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

import org.gwt.mosaic.core.client.Dimension;

import com.google.gwt.dom.client.Style;
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

  protected void setPreferredSize(Dimension preferredSize) {
    this.preferredWidth = preferredSize.getWidth() + Style.Unit.PX.getType();
    this.preferredHeight = preferredSize.getHeight() + Style.Unit.PX.getType();
  }

  // ----

  protected int targetLeft, targetTop, targetWidth, targetHeight;

  protected int left, top, width, height;
  private int sourceLeft, sourceTop, sourceWidth, sourceHeight;

  /**
   * @return the sourceLeft
   */
  public int getSourceLeft() {
    return sourceLeft;
  }

  /**
   * @param sourceLeft the sourceLeft to set
   */
  public void setSourceLeft(int sourceLeft) {
    if (this.sourceLeft == -1) {
      this.sourceLeft = sourceLeft;
    }
  }

  /**
   * @return the sourceTop
   */
  public int getSourceTop() {
    return sourceTop;
  }

  /**
   * @param sourceTop the sourceTop to set
   */
  public void setSourceTop(int sourceTop) {
    if (this.sourceTop == -1) {
      this.sourceTop = sourceTop;
    }
  }

  /**
   * @return the sourceWidth
   */
  public int getSourceWidth() {
    return sourceWidth;
  }

  /**
   * @param sourceWidth the sourceWidth to set
   */
  public void setSourceWidth(int sourceWidth) {
    if (this.sourceWidth == -1) {
      this.sourceWidth = sourceWidth;
    }
  }

  /**
   * @return the sourceHeight
   */
  public int getSourceHeight() {
    return sourceHeight;
  }

  /**
   * @param sourceHeight the sourceHeight to set
   */
  public void setSourceHeight(int sourceHeight) {
    if (this.sourceHeight == -1) {
      this.sourceHeight = sourceHeight;
    }
  }

  public void clearSource() {
    sourceLeft = sourceTop = sourceWidth = sourceHeight = -1;
  }

}