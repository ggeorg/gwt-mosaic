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

import com.google.gwt.user.client.Element;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class DOMImpl {

  /**
   * Gets an attribute of the given element's style.
   * 
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the style attribute to be retrieved
   * @return the style attribute's value
   */
  public native String getComputedStyleAttribute(Element elem, String attr) 
  /*-{
    if (document.defaultView && document.defaultView.getComputedStyle) { // W3C DOM method
      var value = null;
      if (attr == 'float') { // fix reserved word
        attr = 'cssFloat';
      }
      var computed = elem.ownerDocument.defaultView.getComputedStyle(elem, '');
      if (computed) { // test computed before touching for safari
        value = computed[attr];
      }
      return (value || elem.style[attr] || '');
    } else { // default to inline only
      return (el.style[attr] || '');
    }
  }-*/;

  /**
   * Sets an attribute on the given element's style.
   * 
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new value
   */
  public void setStyleAttribute(Element elem, String attr, String value) {
    if (attr.equals("float")) {
      attr = "cssFloat";
    }
    elem.getStyle().setProperty(attr, value);
  }

}
