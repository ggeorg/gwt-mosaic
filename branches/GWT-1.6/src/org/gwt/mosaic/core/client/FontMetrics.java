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
package org.gwt.mosaic.core.client;


import com.google.gwt.user.client.Element;

/**
 * The {@code FontMetrics} class defines a font metrics object, which
 * encapsulates information about the rendering of a particular font.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class FontMetrics {

  private final Element elem;

  /**
   * Creates a new {@code FontMetrics} object for finding out height and width
   * information about the specified font and specific character glyphs in that
   * font.
   */
  public FontMetrics() {
    elem = DOM.createSpan();
    DOM.setStyleAttribute(elem, "fontSize", "1em");
    DOM.setStyleAttribute(elem, "lineHeight", "normal");
    DOM.setStyleAttribute(elem, "letterSpacing", "normal");
  }

  /**
   * Gets the {@code Element} described by this {@code FontMetrics} object.
   * 
   * @return the {@code Element} described by this {@code FontMetrics} object
   */
  public Element getElement() {
    return elem;
  }

  /**
   * 
   * @param str
   * @return
   */
  public Dimension stringBoxSize(String str) {
    return DOM.getStringBoxSize(elem, str);
  }

}