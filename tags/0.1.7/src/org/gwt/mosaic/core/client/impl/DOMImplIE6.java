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
package org.gwt.mosaic.core.client.impl;

import org.gwt.mosaic.core.client.Point;

import com.google.gwt.user.client.Element;

public class DOMImplIE6 extends DOMImpl {

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.core.client.DOM#getStyleAttribute(com.google.gwt.user.client.Element,
   *      java.lang.String)
   */
  @Override
  public native String getStyleAttribute(Element elem, String attr)
  /*-{
    if (document.documentElement.currentStyle) { // IE method
      switch(attr) {
        case 'opacity' : // IE opacity uses filter
          var val = 100;
          try { // will error if no DXImageTransform
            val = elem.filters['DXImageTransform.Microsoft.Alpha'].opacity;
          } catch(e) {
            try { // make sure its in the document
              val = elem.filters('alpha').opacity;
            } catch(e) {
              // ignore
            }
          }
          return val / 100;
        case 'float': // fix reserved word
          attr = 'styleFloat'; // fall through
        default:
          // test currentStyle before touching
          var value = elem.currentStyle ? elem.currentStyle[attr] : null;
          // FIX by ArekZ (exception in DOMImplIE6/Sep 22)
          return ( ''+elem.style[attr] || value );
      }
    } else { // default to inline only
      return elem.style[attr];
    }
  }-*/;

  /**
   * Sets an attribute on the given element's style.
   * 
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new value
   */
  public native void setStyleAttribute(Element elem, String attr, String value)
  /*-{
    switch(attr) {
      case 'opacity':
        elem.style.filter = 'alpha(opacity=' + value * 100 + ')';
        
        if (!elem.currentStyle || !elem.currentStyle.hasLayout) {
          elem.style.zoom = 1; // when no layout or can't tell
        }
        break;
      case 'float':
        attr = 'styleFloat';
      default:
        elem.style[attr] = value;
    };
  }-*/;

  public Point getXY(Element elem) {
    return new Point(getX(elem), getY(elem));
  }

  public native int getX(Element elem)
  /*-{
    var box = elem.getBoundingClientRect();
    var rootNode = elem.ownerDocument;
    return box.left + this.@org.gwt.mosaic.core.client.impl.DOMImpl::getDocumenScrollLeft(Lcom/google/gwt/user/client/Element;)(rootNode);
  }-*/;

  public native int getY(Element elem)
  /*-{
    var box = elem.getBoundingClientRect();
    var rootNode = elem.ownerDocument;
    return box.top + this.@org.gwt.mosaic.core.client.impl.DOMImpl::getDocumenScrollTop(Lcom/google/gwt/user/client/Element;)(rootNode);
  }-*/;

}
