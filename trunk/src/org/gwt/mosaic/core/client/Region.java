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
package org.gwt.mosaic.core.client;

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
