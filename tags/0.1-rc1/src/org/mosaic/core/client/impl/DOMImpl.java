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
package org.mosaic.core.client.impl;

import org.mosaic.core.client.DOM;
import org.mosaic.core.client.Point;
import org.mosaic.core.client.UserAgent;

import com.google.gwt.user.client.Element;

public class DOMImpl {

  protected native boolean testOP_SCROLL(String str)
  /*-{
    var OP_SCROLL = /^(?:inline|table-row)$/i;
    return OP_SCROLL.test(str);
  }-*/;

  /**
   * Gets an attribute of the given element's style.
   * 
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the style attribute to be retrieved
   * @return the style attribute's value
   */
  public native String getStyleAttribute(Element elem, String attr) 
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
      return elem.style[attr] || value;
    } else { // default to inline only
      return el.style[attr];
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

  public Point getXY(Element elem) {
    // manually calculate by crawling up offsetParents
    Point pos = new Point(elem.getOffsetLeft(), elem.getOffsetTop());
    Element parentNode = (Element) elem.getOffsetParent();

    // safari: subtract body offsets if elem is abs (or any offsetParent),
    // unless body is offsetParent
    boolean accountForBody = UserAgent.isSafari()
        && "absolute".equals(getStyleAttribute(elem, "position"))
        && elem.getOffsetParent() == elem.getOwnerDocument().getBody();

    if (parentNode != elem) {
      while (parentNode != null) {
        pos.x += parentNode.getOffsetLeft();
        pos.y += parentNode.getOffsetTop();
        if (!accountForBody && UserAgent.isSafari()
            && "absolute".equals(getStyleAttribute(parentNode, "position"))) {
          accountForBody = true;
        }
        parentNode = (Element) parentNode.getOffsetParent();
      }
    }

    if (accountForBody) { // safari doubles in this case
      pos.x -= elem.getOwnerDocument().getBody().getOffsetLeft();
      pos.y -= elem.getOwnerDocument().getBody().getOffsetTop();
    }
    parentNode = (Element) elem.getParentNode();

    // account for any scrolled ancestors
    while (parentNode.getTagName() != null
        && !("body".equals(parentNode.getTagName()) || "html".equals(parentNode.getTagName()))) {
      if (parentNode.getScrollTop() != 0 || parentNode.getScrollLeft() != 0) {
        // work around opera inline/table scrollLeft/Top bug (false reports
        // offset as scroll)
        if (testOP_SCROLL(getStyleAttribute(parentNode, "display"))) {
          if (!UserAgent.isOpera()
              || !"visible".equals(getStyleAttribute(parentNode, "overflow"))) {
            pos.x -= parentNode.getScrollLeft();
            pos.y -= parentNode.getScrollTop();
          }
        }
      }

      parentNode = (Element) parentNode.getParentNode();
    }

    return pos;
  }

  /**
   * Returns the left scroll value of the document.
   * 
   * @param doc the document to get the scroll value of
   * @return the amount that the document is scrolled to the left
   */
  public native int getDocumenScrollLeft(Element doc)
  /*-{
    doc = doc || document;
    return Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
  }-*/;

  /**
   * Returns the top scroll value of the document.
   * 
   * @param doc the document to get the scroll value of
   * @return the amount that the document is scrolled to the top
   */
  public native int getDocumenScrollTop(Element doc) 
  /*-{
    doc = doc || document;
    return Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);
  }-*/;

  /**
   * Gets the cell index of a cell within a table row.
   * 
   * @param td the cell element
   * @return the cell index
   */
  public int getCellIndex(Element td) {
    return DOM.getElementPropertyInt(td, "cellIndex");
  }
}
