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

import org.gwt.mosaic.core.client.impl.DOMImpl;
import org.gwt.mosaic.ui.client.WidgetWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Provides helper methods for DOM elements.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DOM extends com.google.gwt.user.client.DOM {

  private static final DOMImpl impl = GWT.create(DOMImpl.class);

  public static final int OTHER_KEY_UP = 63232;

  public static final int OTHER_KEY_DOWN = 63233;

  public static final int OTHER_KEY_LEFT = 63234;

  public static final int OTHER_KEY_RIGHT = 63235;

  /**
   * If using standards mode, and the body (i.e. RootPanel.get()) is going to be
   * your boundary panel, you'll want to ensure that the body actually has
   * height; with only absolute positioned content, the height remains 0px.
   * Usually setting 100% height on the HTML and BODY elements does the trick.
   * 
   * html,body { height: 100%; }
   */
  static {
    if (CompatMode.isStandardsMode()) {
      Document doc = Document.get();
      NodeList<com.google.gwt.dom.client.Element> nodeList = doc.getElementsByTagName("html");
      if (nodeList != null && nodeList.getLength() > 0) {
        Element elem = Document.get().getElementsByTagName("html").getItem(0).cast();
        elem.getStyle().setProperty("height", "100%");
      }
      BodyElement body = Document.get().getBody();
      body.getStyle().setProperty("height", "100%");
    }
  }

  /**
   * Fixes the box calculations for IE in QuirksMode.
   * 
   * @param elem the element to set the dimension on
   * @param dim the number of the dimension to fix
   * @param side the dimension ('h' or 'w') to fix (defaults to 'h')
   * @return the fixed dimension
   */
  private static int fixQuirks(Element elem, int dim, char side) {
    int i1 = 0, i2 = 2;
    if (side == 'w') {
      i1 = 1;
      i2 = 3;
    }
    if (UserAgent.isIE6() && !CompatMode.isStandardsMode()) {
      // Internet Explorer - Quirks Mode
      int[] b = getBorderSizes(elem);
      int[] bp = getBorderSizes((Element) elem.getParentElement());
      if ((b[i1] == 0) && (b[i2] == 0)) { // No Borders, check parent
        if ((bp[i1] != 0) && (bp[i2] != 0)) { // Parent has borders
          dim -= (bp[i1] + bp[i2]);
        }
      } else {
        if ((bp[i1] == 0) && (bp[i2] == 0)) { // Parent has borders
          dim += (b[i1] + b[i2]);
        }
      }
    }
    return dim;
  }

  /**
   * Get the CSS border size of the element passed.
   * 
   * @param elem the element to get the border size of
   * @return an array of the top, right, bottom, left borders
   */
  public static int[] getBorderSizes(Element elem) {
    int[] size = new int[4];

    if (UserAgent.isIE6() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getStyleAttribute(elem, "borderTopWidth"), 10, 0);
    size[1] = parseInt(getStyleAttribute(elem, "borderRightWidth"), 10, 0);
    size[2] = parseInt(getStyleAttribute(elem, "borderBottomWidth"), 10, 0);
    size[3] = parseInt(getStyleAttribute(elem, "borderLeftWidth"), 10, 0);

    return size;
  }

  /**
   * Get's the elements <code>clientHeight</code> and <code>clientWidth</code>
   * plus the size of the borders.
   * 
   * @param elem the element to get the size of
   * @return an array of width and height
   */
  public static int[] getBoxSize(Element elem) {
    int[] size = new int[2];

    final int[] b = getBorderSizes(elem);
    final int[] c = getClientSize(elem);
    size[0] = c[0] + (b[1] + b[3]);
    size[1] = c[1] + (b[0] + b[2]);

    return size;
  }

  /**
   * Gets the cell index of a cell within a table row.
   * 
   * @param td the cell element
   * @return the cell index
   */
  public static int getCellIndex(Element td) {
    return impl.getCellIndex(td);
  }

  /**
   * Returns the inner height of an element in pixels, including padding but not
   * the horizontal scrollbar height, border, or margin.
   * <p>
   * <code>clientHeight</code> can be calculated as CSS height + CSS padding -
   * height of horizontal scrollbar (if present).
   * <p>
   * NOTE: Not part of any W3C specification.
   * 
   * @param elem the element to get the <code>clientHeight</code> of
   * @return the <code>clientHeight</code> of the given element
   */
  private native static int getClientHeight(Element elem)
  /*-{
    return elem.clientHeight;
  }-*/;

  /**
   * Get's the elements <code>clientHeight</code> and <code>clientWidth</code>.
   * <code>clientHeight/Width</code> is a non-standard, HTML-specific property
   * introduced in the IE object model.
   * 
   * @param elem the element to get the size of
   * @return an array of width and height
   */
  public static int[] getClientSize(Element elem) {
    int[] size = new int[2];

    if (UserAgent.isIE6() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    size[0] = getClientWidth(elem);
    size[1] = getClientHeight(elem);

    return size;
  }

  /**
   * Returns the inner width of an element in pixels, including padding but not
   * the horizontal scrollbar width, border, or margin.
   * <p>
   * <code>clientWidth</code> can be calculated as CSS width + CSS padding -
   * width of vertical scrollbar (if present).
   * <p>
   * NOTE: Not part of any W3C specification.
   * 
   * @param elem the element to get the <code>clientWidth</code> of
   * @return the <code>clientWidth</code> of the given element
   */
  private native static int getClientWidth(Element elem)
  /*-{
    return elem.clientWidth;
  }-*/;

  /**
   * Returns the height of the document.
   * 
   * @return the height of the actual document (which includes the body and its
   *         margin).
   */
  public static native int getDocumentHeight()
  /*-{
    var scrollHeight = (document.compatMode != 'CSS1Compat') ?
      document.body.scrollHeight : document.documentElement.scrollHeight;
      
    return Math.max(scrollHeight, @com.google.gwt.user.client.Window::getClientHeight()()); 
  }-*/;;

  /**
   * Returns the width of the document.
   * 
   * @return the width of the actual document (which includes the body and its
   *         margin).
   */
  public static native int getDocumentWidth()
  /*-{
    var scrollWidth = (document.compatMode != 'CSS1Compat') ?
      document.body.scrollWidth : document.documentElement.scrollWidth;
   
    return Math.max(scrollWidth, @com.google.gwt.user.client.Window::getClientWidth()());
  }-*/;;

  /**
   * Get the CSS margin size of the element passed.
   * 
   * @param elem the element to get the margin size of
   * @return an array of the top, right, bottom, left margins
   */
  public static int[] getMarginSizes(Element elem) {
    int[] size = new int[4];

    if (UserAgent.isIE6() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getStyleAttribute(elem, "marginTop"), 10, 0);
    size[1] = parseInt(getStyleAttribute(elem, "marginRight"), 10, 0);
    size[2] = parseInt(getStyleAttribute(elem, "marginBottom"), 10, 0);
    size[3] = parseInt(getStyleAttribute(elem, "marginLeft"), 10, 0);

    return size;
  };

  public static int[] getPaddingSizes(Element elem) {
    int[] size = new int[4];

    if (UserAgent.isIE6() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getStyleAttribute(elem, "paddingTop"), 10, 0);
    size[1] = parseInt(getStyleAttribute(elem, "paddingRight"), 10, 0);
    size[2] = parseInt(getStyleAttribute(elem, "paddingBottom"), 10, 0);
    size[3] = parseInt(getStyleAttribute(elem, "paddingLeft"), 10, 0);

    return size;
  }

  /**
   * Gets the row index of a row element.
   * 
   * @param tr the row element
   * @return the row index
   */
  public static int getRowIndex(Element tr) {
    return getElementPropertyInt(tr, "rowIndex");
  }

  /**
   * Gets an attribute of the given element's style.
   * 
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the style attribute to be retrieved
   * @return the style attribute's value
   */
  public static String getStyleAttribute(Element elem, String attr) {
    return impl.getStyleAttribute(elem, attr);
  }

  public static boolean isArrowKey(int code) {
    switch (code) {
      case OTHER_KEY_DOWN:
      case OTHER_KEY_RIGHT:
      case OTHER_KEY_UP:
      case OTHER_KEY_LEFT:
      case KeyboardListener.KEY_DOWN:
      case KeyboardListener.KEY_RIGHT:
      case KeyboardListener.KEY_UP:
      case KeyboardListener.KEY_LEFT:
        return true;
      default:
        return false;
    }
  }

  public static boolean isVisible(Element element) {
    return !"none".equalsIgnoreCase(DOM.getStyleAttribute(element, "display"));
  }

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number,
   * <code>parseInt()</code> returns <code>null</code>.
   * 
   * @param str the string to be parsed
   * @return the parsed value
   */
  protected static Integer parseInt(String str) {
    return parseInt(str, 10);
  }

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number,
   * <code>parseInt()</code> returns <code>null</code>.
   * 
   * @param str the string to be parsed
   * @param radix a number (from 2 to 36) that represents the numeric system to
   *          be used
   * @return the parsed value
   */
  protected native static Integer parseInt(String str, int radix)
  /*-{
    var number = parseInt(str, radix);
    if (isNaN(number))
      return null;
    else
      return @java.lang.Integer::valueOf(I)(number);
  }-*/;

  /**
   * Parses a string and returns an integer.
   * <p>
   * NOTE: Only the first number in the string is returned!
   * <p>
   * NOTE: Leading and trailing spaces are allowed.
   * <p>
   * NOTE: If the first character cannot be converted to a number,
   * <code>parseInt()</code> returns <code>null</code>.
   * 
   * @param str the string to be parsed
   * @param radix a number (from 2 to 36) that represents the numeric system to
   *          be used
   * @param defaultValue the value to return if the parsed value was
   *          <code>null</code>
   * @return the parsed value
   */
  protected static int parseInt(String str, int radix, int defaultValue) {
    Integer result = parseInt(str, radix);
    return result == null ? defaultValue : result;
  }

  /**
   * Gets the first child. You must *KNOW* that the first child exists and is an
   * element to use this method safely.
   */
  public static native Element rawFirstChild(Element elem)
  /*-{
    return elem.firstChild;
  }-*/;

  /**
   * Sets the height of the element based on the border and padding size of the
   * element.
   * 
   * @param elem the elements to have it's height set
   * @param height the height that you want it the element set to
   * @return the new height, fixed for borders, paddings and IE QuirksMode
   */
  public static int setContentAreaHeight(Element elem, int height) {
    final int[] b = getBorderSizes(elem);
    final int[] p = getPaddingSizes(elem);
    final int h = b[0] + b[2] + p[0] + p[2];
    final int fixedHeight = fixQuirks(elem, height - h, 'h');
    elem.getStyle().setPropertyPx("height", Math.max(0, fixedHeight));
    // Intrinsic height?
    if (height != elem.getOffsetHeight()) {
      elem.getStyle().setPropertyPx("height",
          Math.max(0, fixedHeight + (height - elem.getOffsetHeight())));
      if (height != elem.getOffsetHeight()) {
        // System.out.println(elem + " " + DOM.isVisible(elem));
        // System.out.println("Height: " + height + " ? " +
        // elem.getOffsetHeight());
        elem.getStyle().setPropertyPx("height", Math.max(0, height));
        // System.out.println(" : " + elem.getOffsetHeight());
      }
    }
    return height;
  }

  /**
   * Sets the width of the element based on the border and padding size of the
   * element.
   * 
   * @param elem the elements to have it's width set
   * @param width the width that you want it the element set to
   * @return the new width, fixed for borders, paddings and IE QuirksMode
   */
  public static int setContentAreaWidth(Element elem, int width) {
    final int[] b = getBorderSizes(elem);
    final int[] p = getPaddingSizes(elem);
    final int w = b[1] + b[3] + p[1] + p[3];
    final int fixedWidth = fixQuirks(elem, width - w, 'w');
    elem.getStyle().setPropertyPx("width", Math.max(0, fixedWidth));
    // Intrinsic width?
    if (width != elem.getOffsetWidth()) {
      elem.getStyle().setPropertyPx("width",
          Math.max(0, fixedWidth + (width - elem.getOffsetWidth())));
      if (width != elem.getOffsetWidth()) {
        // System.out.println(elem + " " + DOM.isVisible(elem));
        // System.out.println("Width: " + width + " ? " +
        // elem.getOffsetWidth());
        elem.getStyle().setPropertyPx("width", Math.max(0, width));
        // System.out.println(" : " + elem.getOffsetWidth());
      }
    }
    return width;
  }

  /**
   * Sets an attribute on the given element's style.
   * 
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new value
   */
  public static void setStyleAttribute(Element elem, String attr, String value) {
    impl.setStyleAttribute(elem, attr, value);
  }

  /**
   * Normalized key codes. Also switches KEY_RIGHT and KEY_LEFT in RTL
   * languages.
   */
  public static int standardizeKeycode(int code) {

    switch (code) {
      case OTHER_KEY_DOWN:
        code = KeyboardListener.KEY_DOWN;
        break;
      case OTHER_KEY_RIGHT:
        code = KeyboardListener.KEY_RIGHT;
        break;
      case OTHER_KEY_UP:
        code = KeyboardListener.KEY_UP;
        break;
      case OTHER_KEY_LEFT:
        code = KeyboardListener.KEY_LEFT;
        break;
    }
    if (LocaleInfo.getCurrentLocale().isRTL()) {
      if (code == KeyboardListener.KEY_RIGHT) {
        code = KeyboardListener.KEY_LEFT;
      } else if (code == KeyboardListener.KEY_LEFT) {
        code = KeyboardListener.KEY_RIGHT;
      }
    }
    return code;
  }

  /**
   * Returns the screen resolution in dots-per-inch.
   * 
   * @return the screen resolution, in dots-per-inch
   */
  public static int getScreenResolution() {
    return toPixelSize("1in");
  }

  private static Element toPixelSizeTestElem = null;

  public static int toPixelSize(final String width) {
    if (toPixelSizeTestElem == null) {
      toPixelSizeTestElem = DOM.createDiv();
      setStyleAttribute(toPixelSizeTestElem, "left", "");
      setStyleAttribute(toPixelSizeTestElem, "top", "");
      setStyleAttribute(toPixelSizeTestElem, "position", "");
      setStyleAttribute(toPixelSizeTestElem, "visibility", "hidden");
      Document.get().getBody().appendChild(toPixelSizeTestElem);
    }
    setStyleAttribute(toPixelSizeTestElem, "width", width);

    return getBoxSize(toPixelSizeTestElem)[0];
  }

  public static int[] getStringBoxSize(Element div, final String str) {
    final BodyElement body = Document.get().getBody();
    div.setInnerText(str);
    setStyleAttribute(div, "left", "");
    setStyleAttribute(div, "top", "");
    setStyleAttribute(div, "position", "");
    setStyleAttribute(div, "visibility", "hidden");
    if (UserAgent.isIE6()) {
      final WidgetWrapper wrapper = new WidgetWrapper(new SimplePanel(div) {
      }, HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_MIDDLE);
      div = wrapper.getElement();
    } else {
      setStyleAttribute(div, "display", "table");
    }
    // force "auto" width
    setStyleAttribute(div, "width", "0px");
    setStyleAttribute(div, "height", "0px");
    div.getOffsetWidth();
    setStyleAttribute(div, "width", "auto");
    setStyleAttribute(div, "height", "auto");
    try {
      body.appendChild(div);
      return getBoxSize(div);
    } finally {
      body.removeChild(div);
    }
  }

}
