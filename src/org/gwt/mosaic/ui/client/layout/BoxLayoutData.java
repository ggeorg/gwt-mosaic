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
package org.gwt.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public final class BoxLayoutData extends LayoutData {

  public enum FillStyle {
    BOTH, HORIZONTAL, VERTICAL
  }

  int width = -1, height = -1;

  boolean fillWidth, fillHeight;

  int calcWidth, calcHeight;

  public BoxLayoutData() {
    // Nothing to do here!
  }

  public BoxLayoutData(boolean decorate) {
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

  public BoxLayoutData(FillStyle fillStyle) {
    this(fillStyle, false);
  }

  public BoxLayoutData(FillStyle fillStyle, boolean decorate) {
    if (fillStyle == FillStyle.BOTH) {
      fillWidth = true;
      fillHeight = true;
    } else if (fillStyle == FillStyle.HORIZONTAL) {
      fillWidth = true;
    } else if (fillStyle == FillStyle.VERTICAL) {
      fillHeight = true;
    }
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

  public BoxLayoutData(int width, int height) {
    this(width, height, false);
  }

  public BoxLayoutData(int width, int height, boolean decorate) {
    this.width = width;
    this.height = height;
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

  int getCalcHeight() {
    return calcHeight;
  }

  int getCalcWidth() {
    return calcWidth;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public boolean isFillHeight() {
    return fillHeight;
  }

  public boolean isFillWidth() {
    return fillWidth;
  }

  void setCalcHeight(int calcHeight) {
    this.calcHeight = calcHeight;
  }

  void setCalcWidth(int calcWidth) {
    this.calcWidth = calcWidth;
  }

  public void setFillHeight(boolean fillHeight) {
    this.fillHeight = fillHeight;
  }

  public void setFillWidth(boolean fillWidth) {
    this.fillWidth = fillWidth;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setWidth(int width) {
    this.width = width;
  }

}
