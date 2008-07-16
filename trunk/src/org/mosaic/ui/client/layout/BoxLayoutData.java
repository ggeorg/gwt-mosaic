package org.mosaic.ui.client.layout;

import com.google.gwt.user.client.ui.DecoratorPanel;

public final class BoxLayoutData extends LayoutData {

  public enum FillStyle {
    BOTH, HORIZONTAL, VERTICAL
  }

  float width = -1, height = -1;

  boolean fillWidth, fillHeight;

  float calcWidth, calcHeight;

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

//  public BoxLayoutData(float width, float height) {
//    this.width = width;
//    this.height = height;
//  }

  float getCalcHeight() {
    return calcHeight;
  }

  float getCalcWidth() {
    return calcWidth;
  }

  public float getHeight() {
    return height;
  }

  public float getWidth() {
    return width;
  }

  public boolean isFillHeight() {
    return fillHeight;
  }

  public boolean isFillWidth() {
    return fillWidth;
  }

  void setCalcHeight(float calcHeight) {
    this.calcHeight = calcHeight;
  }

  void setCalcWidth(float calcWidth) {
    this.calcWidth = calcWidth;
  }

  public void setFillHeight(boolean fillHeight) {
    this.fillHeight = fillHeight;
  }

  public void setFillWidth(boolean fillWidth) {
    this.fillWidth = fillWidth;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public void setWidth(float width) {
    this.width = width;
  }

}
