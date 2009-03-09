/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.core.client.impl;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Element;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class DOMImplSafari extends DOMImpl {
  /**
   * Gets the cell index of a cell within a table row.
   * 
   * The cellIndex property is not defined in Safari, so we must calculate the
   * cell index manually.
   * 
   * @param td the cell element
   * @return the cell index
   */
  @Override
  public int getCellIndex(Element td) {
    return DOM.getChildIndex(DOM.getParent(td), td);
  }
}
