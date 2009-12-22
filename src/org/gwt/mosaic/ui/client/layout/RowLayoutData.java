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

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class RowLayoutData extends LayoutData {
  private int flexibility;
  
  int calcHeight;
  
  RowLayoutSplitBar splitBar;

  public RowLayoutData() {
    this(1, false);
  }
  
  public RowLayoutData(boolean decorate) {
    this(1, decorate);
  }

  public RowLayoutData(int flex) {
    this(flex, false);
  }
  
  public RowLayoutData(int flex, boolean decorate) {
    super(decorate);
    setFlexibility(flex);
  }
  
  public RowLayoutData(String preferredHeight) {
    this(preferredHeight, false);
  }
  
  public RowLayoutData(String preferredHeight, boolean decorate) {
    super(decorate);
    setPreferredHeight(preferredHeight);
    setFlexibility(1);
  }

  /**
   * @return the flexibility
   */
  public int getFlexibility() {
    return flexibility;
  }

  /**
   * @param flexibility the flexibility to set
   */
  public void setFlexibility(int flexibility) {
    if (flexibility < 1) {
      throw new IllegalArgumentException("flexibility can't be < 1 ("
          + flexibility + ")");
    }
    this.flexibility = flexibility;
  }
  
  /**
   * Used by UiBinder parser.
   * 
   * @param flexibility the flexibility to set
   */
  public void setFlexibility(String flexibility) {
    setFlexibility(Integer.parseInt(flexibility));
  }

}
