package org.mosaic.core.client;

import java.io.Serializable;

public class Region implements Serializable {
  private static final long serialVersionUID = 3365575120114516987L;

  public int left;
  
  public int top; 
  
  public int right;
  
  public int bottom;
  
  public Region() {
    this(0, 0, 0, 0);
  }
  
  public Region(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  public int getWidth() {
    return right - left;
  }

  public int getHeight() {
    return bottom - top;
  }
}
