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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;

/**
 * Provides helper methods for DOM elements.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DOM extends com.google.gwt.user.client.DOM {

  private static final DOMImpl impl = GWT.create(DOMImpl.class);

  private static Element toPixelSizeTestElem = null;

  /**
   * If using standards mode, and the body (i.e. RootPanel.get()) is going to be
   * your boundary panel, you'll want to ensure that the body actually has
   * height; with only absolute positioned content, the height remains 0px.
   * Usually setting 100% height on the HTML and BODY elements does the trick.
   * <p>
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
      BodyElement body = doc.getBody();
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
  public static int fixQuirks(Element elem, int dim, char side) {
    int i1 = 0, i2 = 2;
    if (side == 'w') {
      i1 = 1;
      i2 = 3;
    }
    if (UserAgent.isIE() && !CompatMode.isStandardsMode()) {
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

    if (UserAgent.isIE() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getComputedStyleAttribute(elem, "borderTopWidth"), 10, 0);
    size[1] = parseInt(getComputedStyleAttribute(elem, "borderRightWidth"), 10,
        0);
    size[2] = parseInt(getComputedStyleAttribute(elem, "borderBottomWidth"),
        10, 0);
    size[3] = parseInt(getComputedStyleAttribute(elem, "borderLeftWidth"), 10,
        0);

    return size;
  }

  /**
   * Get's the elements <code>clientHeight</code> and <code>clientWidth</code>
   * plus the size of the borders and margins even if {@code visibility} is set
   * to {@code false}.
   * <p>
   * https://developer.mozilla.org/en/Determining_the_dimensions_of_elements
   * 
   * @param elem the element to get the size of
   * @return an array of width and height
   */
  public static Dimension getBoxSize(Element elem) {
    final int[] m = getMarginSizes(elem);
    final int[] b = getBorderSizes(elem);
    final Dimension c = getClientSize(elem);
    c.width += (b[1] + b[3]) + (m[1] + m[3]);
    c.height += (b[0] + b[2]) + (m[0] + m[2]);
    return c;
  }

  /**
   * Get's the elements {@code clientHeight} and {@code clientWidth}.
   * <p>
   * Both {@code clientHeight} and {@code clientWidth} are a non-standard,
   * HTML-specific property introduced in the IE object model.
   * <p>
   * https://developer.mozilla.org/en/Determining_the_dimensions_of_elements
   * 
   * @param elem the element to get the size of
   * @return an array of width and height
   */
  public static Dimension getClientSize(Element elem) {
    if (UserAgent.isIE() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }
    return new Dimension(elem.getClientWidth(), elem.getClientHeight());
  }

  /**
   * Gets an attribute of the given element's computed style.
   * <p>
   * http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSview-
   * getComputedStyle
   * 
   * @param elem the element whose style attribute is to be retrieved
   * @param attr the name of the style attribute to be retrieved
   * @return the computed style attribute's value
   */
  public static String getComputedStyleAttribute(Element elem, String attr) {
    return impl.getComputedStyleAttribute(elem, attr);
  }

  /**
   * Get the CSS margin size of the element passed.
   * 
   * @param elem the element to get the margin size of
   * @return an array of the top, right, bottom, left margins
   */
  public static int[] getMarginSizes(Element elem) {
    int[] size = new int[4];

    if (UserAgent.isIE() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getComputedStyleAttribute(elem, "marginTop"), 10, 0);
    size[1] = parseInt(getComputedStyleAttribute(elem, "marginRight"), 10, 0);
    size[2] = parseInt(getComputedStyleAttribute(elem, "marginBottom"), 10, 0);
    size[3] = parseInt(getComputedStyleAttribute(elem, "marginLeft"), 10, 0);

    return size;
  }

  public static int[] getPaddingSizes(Element elem) {
    int[] size = new int[4];

    if (UserAgent.isIE() /* && !CompatMode.isStandardsMode() */) {
      elem.getStyle().setProperty("zoom", "1");
    }

    // IE will return NaN on these if they are set to auto, well set them to 0

    size[0] = parseInt(getComputedStyleAttribute(elem, "paddingTop"), 10, 0);
    size[1] = parseInt(getComputedStyleAttribute(elem, "paddingRight"), 10, 0);
    size[2] = parseInt(getComputedStyleAttribute(elem, "paddingBottom"), 10, 0);
    size[3] = parseInt(getComputedStyleAttribute(elem, "paddingLeft"), 10, 0);

    return size;
  }

  /**
   * Returns the screen resolution in dots-per-inch.
   * 
   * @return the screen resolution, in dots-per-inch
   */
  public static int getScreenResolution() {
    return toPixelSize("1in", true);
  }

  public static Dimension getStringBoxSize(Element span, final String str) {
    final BodyElement body = Document.get().getBody();
    span.setInnerText(str);
    setStyleAttribute(span, "left", "");
    setStyleAttribute(span, "top", "");
    setStyleAttribute(span, "position", "absolute");
    setStyleAttribute(span, "visibility", "hidden");

    // force "auto" width
    setStyleAttribute(span, "width", "0px");
    setStyleAttribute(span, "height", "0px");

    span.getOffsetWidth();
    span.getOffsetHeight();

    setStyleAttribute(span, "width", "auto");
    setStyleAttribute(span, "height", "auto");

    try {
      body.appendChild(span);
      return getBoxSize(span);
    } finally {
      body.removeChild(span);
    }
  }

  public static boolean isVisible(Element element) {
    return !"none".equalsIgnoreCase(DOM.getComputedStyleAttribute(element,
        "display"));
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
  private static int parseInt(String str, int radix, int defaultValue) {
    final Integer result = parseInt(str, radix);
    return result == null ? defaultValue : result;
  }

  /**
   * Sets an attribute on the given element's style.
   * 
   * @param elem the element whose style attribute is to be set
   * @param attr the name of the style attribute to be set
   * @param value the style attribute's new value
   */
  public static void setStyleAttribute(Element elem, String attr, String value) {
    try {
      impl.setStyleAttribute(elem, attr, value);
    } catch (Exception ex) {
      GWT.log("Set style attribute error, tag=" + elem.getTagName() + ", attr="
          + attr + ", value=" + value, ex);
    }
  }

  public static int toPixelSize(final String value,
      final boolean useWidthAttribute) {
    if (toPixelSizeTestElem == null) {
      toPixelSizeTestElem = DOM.createSpan();
      setStyleAttribute(toPixelSizeTestElem, "left", "");
      setStyleAttribute(toPixelSizeTestElem, "top", "");
      setStyleAttribute(toPixelSizeTestElem, "position", "absolute"); // Safari
      setStyleAttribute(toPixelSizeTestElem, "visibility", "hidden");
      Document.get().getBody().appendChild(toPixelSizeTestElem);
    }
    DOM.setStyleAttribute(toPixelSizeTestElem, "width", value);
    DOM.setStyleAttribute(toPixelSizeTestElem, "height", value);

    Dimension size = DOM.getBoxSize(toPixelSizeTestElem);

    return (useWidthAttribute) ? size.width : size.height;
  }

  private static Dimension detectedScrollbarSize = null;

  public static int getNativeScrollbarSize(final boolean useWidthAttribute) {
    if (detectedScrollbarSize == null) {
      Element scroller = DOM.createDiv();
      setStyleAttribute(scroller, "width", "50px");
      setStyleAttribute(scroller, "height", "50px");
      setStyleAttribute(scroller, "overflow", "scroll");
      setStyleAttribute(scroller, "position", "scroll");
      setStyleAttribute(scroller, "marginLeft", "-5000px");
      Document.get().getBody().appendChild(scroller);
      detectedScrollbarSize = new Dimension(scroller.getOffsetWidth()
          - scroller.getPropertyInt("clientWidth"), scroller.getOffsetHeight()
          - scroller.getPropertyInt("clientHeight"));

      // Asserting the detected value causes a problem
      // at least in Hosted Mode Browser/Linux/GWT-1.5.3, so
      // use a default if detection fails.
      if (detectedScrollbarSize.width == 0) {
        detectedScrollbarSize.width = detectedScrollbarSize.height = 20;
      }

      Document.get().getBody().removeChild(scroller);
    }

    return (useWidthAttribute) ? detectedScrollbarSize.width
        : detectedScrollbarSize.height;
  }

}