/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos
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
package com.google.gwt.gen2.table.client;

import com.google.gwt.user.client.Element;

/**
 * Helper class that exports {@link FixedWidthGrid#getInputColumnWidth()}
 * protected method.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class FixedWidthGridHelper {

  private FixedWidthGridHelper() {
    // Nothing to do here!
  }

  /**
   * Get the width of the input column used in the current
   * {@link SelectionGrid.SelectionPolicy}.
   * 
   * @return the width of the input element
   */
  public static int getInputColumnWidth(FixedWidthGrid grid) {
    return grid.getInputColumnWidth();
  }
  
  /**
   * Set the current highlighted cell.
   * 
   * @param cellElem the cell element
   */
  public static void highlightCell(FixedWidthGrid grid, Element cellElem) {
    grid.highlightCell(cellElem);
  }
  
}
