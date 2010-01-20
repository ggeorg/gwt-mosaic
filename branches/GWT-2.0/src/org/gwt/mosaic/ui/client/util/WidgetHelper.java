/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.util;

import org.gwt.mosaic.core.client.CompatMode;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Point;
import org.gwt.mosaic.core.client.Rectangle;
import org.gwt.mosaic.core.client.UserAgent;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class WidgetHelper {

  private static final String MIN_WIDTH = "minWidth";

  private static final String MIN_HEIGHT = "minHeight";

  private static final String MAX_WIDTH = "maxWidth";

  private static final String MAX_HEIGHT = "maxHeight";

  /**
   * Returns closest {@link HasLayoutManager} parent of the given {@code Widget}
   * .
   * 
   * @param widget the given {@code Widget}
   * @return the {@code Widget}'s closest {@link HasLayoutManager} parent, or
   *         {@code null} if no {@link HasLayoutManager} parent is found
   */
  public static HasLayoutManager getParent(Widget widget) {
    Widget parent = widget.getParent();
    if (parent == null) {
      return null;
    }
    if (parent instanceof HasLayoutManager) {
      return (HasLayoutManager) parent;
    } else {
      return getParent(parent);
    }
  }

  /**
   * Invalidates the given {@code Widget} and all parents above it. This method
   * can be called often, so it needs to execute quickly.
   * 
   * @param widget the given {@code Widget}
   */
  public static void invalidate(Widget widget) {
    if (widget instanceof HasLayoutManager) {
      ((HasLayoutManager) widget).invalidate(null);
    } else {
      HasLayoutManager lm = getParent(widget);
      if (lm != null) {
        lm.invalidate(widget);
      }
    }
  }

  /**
   * Ensures that {@code widget} has a valid layout. This operates on child
   * widgets that implement {@code HasLayoutManager}.
   * 
   * @param widget the child {@code Widget}.
   */
  public static void layout(Widget widget) {
    if (widget instanceof DecoratorPanel) {
      widget = ((DecoratorPanel) widget).getWidget();
    }
    if (widget instanceof FormPanel) {
      widget = ((FormPanel) widget).getWidget();
    }
    if (widget instanceof HasLayoutManager) {
      ((HasLayoutManager) widget).layout();
    }
  }

  /**
   * Calls {@link WidgetHelper#invalidate(Widget)} and then the first
   * {@link HasLayoutManager#layout()} (if any) is called when walking up the
   * containment hierarchy of the given {@code Widget} is called.
   * 
   * @param widget the given {@code Widget}
   */
  public static void revalidate(Widget widget) {
    invalidate(widget);

    final HasLayoutManager parent = getParent(widget);
    if (parent != null) {
      revalidate((Widget) parent);
    } else {
      layout(widget);
    }
  }

  /**
   * Returns the current size of the {@code Widget} in the form of a
   * {@link Dimension} object.
   * <p>
   * Get's the elements <code>height</code> and <code>width</code> plus the size
   * of the borders and margins.
   * <p>
   * https://developer.mozilla.org/en/Determining_the_dimensions_of_elements
   * 
   * @param widget the given {@code Widget}
   * @return a {@link Dimension} object that indicates the size of this
   *         component
   */
  public static Dimension getOffsetSize(final Widget widget) {
    return new Dimension(widget.getOffsetWidth(), widget.getOffsetHeight());
  }

  @Deprecated
  public static Dimension getPreferredSize(Widget widget) {
    // Ignore FormPanel if getWidget() returns a HasLayoutManager implementation
    if (widget instanceof FormPanel) {
      final Widget child = ((FormPanel) widget).getWidget();
      if (child != null && (child instanceof HasLayoutManager)) {
        widget = child;
      }
    }
    if (widget instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) widget;
      return lp.getPreferredSize();
    } else {
      final Element clonedElem = widget.getElement().cloneNode(true).cast();
      final Element parentElem;
      if (widget.getParent() instanceof DecoratorPanel) {
        parentElem = widget.getParent().getParent().getElement();
      } else {
        parentElem = widget.getParent().getElement();
      }
      final Style style = clonedElem.getStyle();
      style.setPosition(Position.STATIC);
      style.setProperty("width", "auto");
      style.setProperty("height", "auto");
      style.setProperty("visibility", "hidden");
      parentElem.appendChild(clonedElem);
      final Dimension d = new Dimension(clonedElem.getOffsetWidth(),
          clonedElem.getOffsetHeight());
      parentElem.removeChild(clonedElem);
      return d;
    }
  }

  public static void setMaxHeight(final Widget w, final String height) {
    DOM.setStyleAttribute(w.getElement(), MAX_HEIGHT, height);
  }

  public static void setMaxSize(Widget widget, String width, String height) {
    setMaxWidth(widget, width);
    setMaxHeight(widget, height);
  }

  public static void setMaxWidth(final Widget w, final String width) {
    DOM.setStyleAttribute(w.getElement(), MAX_WIDTH, width);
  }

  public static void setMinHeight(final Widget w, final String height) {
    DOM.setStyleAttribute(w.getElement(), MIN_HEIGHT, height);
  }

  public static void setMinSize(Widget widget, String width, String height) {
    setMinWidth(widget, width);
    setMinHeight(widget, height);
  }

  public static void setMinWidth(final Widget w, final String width) {
    DOM.setStyleAttribute(w.getElement(), MIN_WIDTH, width);
  }

  /**
   * Moves and resizes the given {@code Widget} to conform to the new bounding
   * {@code Rectangle}.
   * 
   * @param layoutPanel the parent {@code LayoutPanel}
   * @param widget the given {@code Widget}
   * @param r the new bounding {@code Rectangle} for this component
   */
  public static void setBounds(final LayoutPanel layoutPanel,
      final Widget widget, Rectangle r) {
    setBounds(layoutPanel, widget, r.x, r.y, r.width, r.height);
  }

  /**
   * Moves and resizes the given {@code Widget}.
   * 
   * @param layoutPanel the parent {@code LayoutPanel}
   * @param widget the given {@code Widget}
   * @param x the new <i>x</i>-coordinate
   * @param y the new <i>y</i>-coordinate
   * @param width the new width
   * @param height the new height
   */
  public static void setBounds(final LayoutPanel layoutPanel,
      final Widget widget, final int x, final int y, int width, int height) {
    final Element elem = widget.getElement();

    setXY(layoutPanel, widget, x, y);

    setSize(widget, width, height, DOM.getMarginSizes(elem),
        DOM.getBorderSizes(elem), DOM.getPaddingSizes(elem));
  }

  /**
   * Resizes the given {@code Widget}.
   * <p>
   * This method fixes the box calculations for IE in QuirksMode, and also, the
   * <em>button box model</em> bug in Explorer Windows and Mozilla:
   * <ol>
   * <li>
   * Earlier versions of Internet Explorer calculate the {@code width} and
   * {@code height} CSS properties in a way that does not comply with the CSS1
   * box model. In CSS1, the {@code width} property is defined as the distance
   * between the left and right edges of the bounding box that surrounds the
   * element's content. Likewise, the {@code height} property is defined in CSS1
   * as the distance between the top and bottom edges of the bounding box. With
   * earlier versions of Internet Explorer, however, the {@code width} and
   * {@code height} CSS properties also include the {@code border} and {@code
   * padding} belts that surround the element's bounding box. The following
   * graphic illustrates this difference.
   * <p>
   * <img border="1" src="BOXMODEL.gif">
   * <p>
   * <em>Note: To check the rendering mode in IE, type {@code
   * javascript:alert(document.compatMode)} in IE's address line.</em>
   * <p>
   * For more information, see <a target="_blank"
   * href="http://msdn.microsoft.com/en-us/library/bb250395%28VS.85%29.aspx">CSS
   * Enhancements in Internet Explorer 6</a>.
   * </li>
   * <li>
   * Both Explorer 6 Windows and Mozilla always give a button the traditional
   * box model instead of the W3C one, even in strict mode.
   * <p>
   * Solution: For Mozilla, add -moz-box-sizing: content-box to the button.
   * Unfortunately this bug is unsolvable in Explorer.
   * <p>
   * See: <a href="http://www.quirksmode.org/css/tests/mozie_button.html"
   * target="_blank">Explorer Windows and Mozilla bug - button box model</a>
   * </li>
   * </ol>
   * 
   * @param widget the given {@code Widget}
   * @param d the {@code Dimension} specifying the new size in pixels
   * @see #setSize(Widget, int, int)
   * @see #setSize(Widget, int, int, int[], int[], int[])
   */
  public static void setSize(final Widget widget, final Dimension d) {
    setSize(widget, d.width, d.height);
  }

  /**
   * Resizes the given {@code Widget}.
   * <p>
   * This method fixes the box calculations for IE in QuirksMode, and also, the
   * <em>button box model</em> bug in Explorer Windows and Mozilla:
   * <ol>
   * <li>
   * Earlier versions of Internet Explorer calculate the {@code width} and
   * {@code height} CSS properties in a way that does not comply with the CSS1
   * box model. In CSS1, the {@code width} property is defined as the distance
   * between the left and right edges of the bounding box that surrounds the
   * element's content. Likewise, the {@code height} property is defined in CSS1
   * as the distance between the top and bottom edges of the bounding box. With
   * earlier versions of Internet Explorer, however, the {@code width} and
   * {@code height} CSS properties also include the {@code border} and {@code
   * padding} belts that surround the element's bounding box. The following
   * graphic illustrates this difference.
   * <p>
   * <img border="1" src="BOXMODEL.gif">
   * <p>
   * <em>Note: To check the rendering mode in IE, type {@code
   * javascript:alert(document.compatMode)} in IE's address line.</em>
   * <p>
   * For more information, see <a target="_blank"
   * href="http://msdn.microsoft.com/en-us/library/bb250395%28VS.85%29.aspx">CSS
   * Enhancements in Internet Explorer 6</a>.
   * </li>
   * <li>
   * Both Explorer 6 Windows and Mozilla always give a button the traditional
   * box model instead of the W3C one, even in strict mode.
   * <p>
   * Solution: For Mozilla, add -moz-box-sizing: content-box to the button.
   * Unfortunately this bug is unsolvable in Explorer.
   * <p>
   * See: <a href="http://www.quirksmode.org/css/tests/mozie_button.html"
   * target="_blank">Explorer Windows and Mozilla bug - button box model</a>
   * </li>
   * </ol>
   * 
   * @param widget the given {@code Widget}
   * @param width the new width in pixels
   * @param height the new height in pixels
   * @see #setSize(Widget, Dimension)
   * @see #setSize(Widget, int, int, int[], int[], int[])
   */
  public static void setSize(final Widget widget, final int width,
      final int height) {
    final Element elem = widget.getElement();
    setSize(widget, width, height, DOM.getMarginSizes(elem),
        DOM.getBorderSizes(elem), DOM.getPaddingSizes(elem));
  }

  /**
   * Resizes the given {@code Widget}.
   * <p>
   * This method fixes the box calculations for IE in QuirksMode, and also, the
   * <em>button box model</em> bug in Explorer Windows and Mozilla:
   * <ol>
   * <li>
   * Earlier versions of Internet Explorer calculate the {@code width} and
   * {@code height} CSS properties in a way that does not comply with the CSS1
   * box model. In CSS1, the {@code width} property is defined as the distance
   * between the left and right edges of the bounding box that surrounds the
   * element's content. Likewise, the {@code height} property is defined in CSS1
   * as the distance between the top and bottom edges of the bounding box. With
   * earlier versions of Internet Explorer, however, the {@code width} and
   * {@code height} CSS properties also include the {@code border} and {@code
   * padding} belts that surround the element's bounding box. The following
   * graphic illustrates this difference.
   * <p>
   * <img border="1" src="BOXMODEL.gif">
   * <p>
   * <em>Note: To check the rendering mode in IE, type {@code
   * javascript:alert(document.compatMode)} in IE's address line.</em>
   * <p>
   * For more information, see <a target="_blank"
   * href="http://msdn.microsoft.com/en-us/library/bb250395%28VS.85%29.aspx">CSS
   * Enhancements in Internet Explorer 6</a>.
   * </li>
   * <li>
   * Both Explorer 6 Windows and Mozilla always give a button the traditional
   * box model instead of the W3C one, even in strict mode.
   * <p>
   * Solution: For Mozilla, add -moz-box-sizing: content-box to the button.
   * Unfortunately this bug is unsolvable in Explorer.
   * <p>
   * See: <a href="http://www.quirksmode.org/css/tests/mozie_button.html"
   * target="_blank">Explorer Windows and Mozilla bug - button box model</a>
   * </li>
   * </ol>
   * 
   * @param widget the given {@code Widget}
   * @param width the new width in pixels
   * @param height the new height in pixels
   * @param margin the widget's margin sizes
   * @param border the widget's border width sizes
   * @param padding the widget's padding sizes
   * @see #setSize(Widget, Dimension)
   * @see #setSize(Widget, int, int)
   */
  public static void setSize(final Widget widget, int width, int height,
      final int[] margin, final int[] border, final int[] padding) {

    // TODO review and write a test case!
    if (widget instanceof FormPanel) {
      final Widget child = ((FormPanel) widget).getWidget();
      if (child != null && child instanceof HasLayoutManager) {
        int[] margins = DOM.getMarginSizes(child.getElement());
        if (width != -1) {
          width -= (margins[1] + margins[3]);
        }
        if (height != -1) {
          height -= (margins[0] + margins[2]);
        }
        setSize(child, width, height);

        // (ggeorg) needs:
        // border: none;
        // padding: 0px;
        // margin: 0px
        setSize(widget, WidgetHelper.getPreferredSize(child));

        return;
      }
    }

    //
    // Note: we use Widget calls, e.g. Widget.setWidth() or Widget.setHeight()
    //

    if (width >= 0) {

      width -= (margin[1] + margin[3]);

      if (CompatMode.isStandardsMode() || !UserAgent.isIE()) {
        final int w = width - (border[1] + border[3] + padding[1] + padding[3]);
        widget.setWidth(Math.max(0, w) + Unit.PX.getType());

        //
        // handle button box model bug
        //

        if (width != widget.getOffsetWidth()) {
          widget.setWidth(Math.max(0, width) + Unit.PX.getType());
        }

      } else {
        // IE Quirks Mode
        widget.setWidth(Math.max(0, width) + Unit.PX.getType());
      }
    }

    if (height >= 0) {

      height -= (margin[0] + margin[2]);

      if (CompatMode.isStandardsMode() || !UserAgent.isIE()) {
        final int h = height
            - (border[0] + border[2] + padding[0] + padding[2]);
        widget.setHeight(Math.max(0, h) + Unit.PX.getType());

        //
        // handle button box model bug
        //

        if (height != widget.getOffsetHeight()) {
          widget.setHeight(Math.max(0, height) + Unit.PX.getType());
        }

      } else {
        // IE Quirks Mode
        widget.setHeight(Math.max(0, height) + Unit.PX.getType());
      }
    }
  }

  /**
   * Moves the given {@code Widget} to a new location.
   * 
   * @param layoutPanel the parent {@code LayoutPanel}
   * @param widget the given {@code Widget}
   * @param x the new <i>x</i>-coordinate
   * @param y the new <i>y</i>-coordinate
   * 
   * @deprecated replaced by {@link #setXY(LayoutPanel, Widget, Point)}
   */
  public static void setXY(final LayoutPanel layoutPanel, final Widget widget,
      final int x, final int y) {
    layoutPanel.setWidgetPosition(widget, x, y);
  }

  /**
   * Moves the given {@code Widget} to a new location.
   * 
   * @param layoutPanel the parent {@code LayoutPanel}
   * @param widget the given {@code Widget}
   * @param p the point defining the top-left corner of the new location given
   *          in the coordinate space of the parent {@code LayoutPanel}
   */
  public static void setXY(final LayoutPanel layoutPanel, final Widget widget,
      final Point p) {
    setXY(layoutPanel, widget, p.x, p.y);
  }
}
