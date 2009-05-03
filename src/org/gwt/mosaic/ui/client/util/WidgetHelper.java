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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Point;
import org.gwt.mosaic.core.client.Rectangle;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

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
      ((HasLayoutManager) widget).invalidate();
    } else {
      HasLayoutManager lm = getParent(widget);
      if (lm != null) {
        lm.invalidate();
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
   * Moves and resizes the given {@code Widget}.
   * 
   * @param layoutPanel the parent {@code LayoutPanel}
   * @param widget the given {@code Widget}
   * @param x the new <i>x</i>-coordinate
   * @param y the new <i>y</i>-coordinate
   * @param width the new width
   * @param height the new height
   * 
   * @deprecated replaced by {@code WidgetHelper#setBounds(LayoutPanel, Widget,
   *             Rectangle)}
   */
  @Deprecated
  public static void setBounds(final LayoutPanel layoutPanel,
      final Widget widget, final int x, final int y, int width, int height) {
    int[] margins = DOM.getMarginSizes(widget.getElement());
    if (width != -1) {
      width -= (margins[1] + margins[3]);
    }
    if (height != -1) {
      height -= (margins[0] + margins[2]);
    }
    setXY(layoutPanel, widget, x, y);
    setSize(widget, width, height);

    if (widget instanceof FormPanel) {
      final Widget child = ((FormPanel) widget).getWidget();
      if (child != null && (child instanceof HasLayoutManager)) {
        setSize(child, width, height);
      }
    }
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
   * Resizes the given {@code Widget}.
   * 
   * @param widget the given {@code Widget}
   * @param d the {@code Dimension} specifying the new size in pixels
   */
  public static void setSize(final Widget widget, final Dimension d) {
    setSize(widget, d.width, d.height);
  }

  /**
   * Resizes the given {@code Widget}.
   * 
   * @param widget the given {@code Widget}
   * @param width the new width in pixels
   * @param height the new height in pixels
   * @deprecated replaced by {@link #setSize(Widget, Dimension)}
   */
  @Deprecated
  public static void setSize(final Widget widget, final int width,
      final int height) {
    final Element elem = widget.getElement();

    if (width >= 0) {
      widget.setWidth(width + "px");
      if (width != widget.getOffsetWidth()) {
        setSize(widget, width, height, DOM.getBorderSizes(elem),
            DOM.getPaddingSizes(elem));
        return;
      }
    } else if (width == -1) {
      widget.setWidth("auto");
    }

    if (height >= 0) {
      widget.setHeight(height + "px");
      if (height != widget.getOffsetHeight()) {
        final int[] b = DOM.getBorderSizes(elem);
        final int[] p = DOM.getPaddingSizes(elem);
        final int h = b[0] + b[2] + p[0] + p[2];
        final int fixedHeight = DOM.fixQuirks(elem, height - h, 'h');
        widget.setHeight(Math.max(0, fixedHeight) + "px");
      }
    } else if (height == -1) {
      widget.setHeight("auto");
    }

    if (width != widget.getOffsetWidth()) {
      System.out.println(elem.getTagName() + " :: " + widget.getOffsetWidth()
          + "x" + widget.getOffsetHeight() + " ? " + width + "x" + height);
    }

  }

  private static void setSize(Widget widget, int width, int height, int[] b,
      int[] p) {
    final Element elem = widget.getElement();

    if (width >= 0) {
      widget.setWidth(width + "px");
      if (width != widget.getOffsetWidth()) {
        final int w = b[1] + b[3] + p[1] + p[3];
        final int fixedWidth = DOM.fixQuirks(elem, width - w, 'w');
        widget.setWidth(Math.max(0, fixedWidth) + "px");
      }
    } else if (width == -1) {
      widget.setWidth("auto");
    }

    if (height >= 0) {
      widget.setHeight(height + "px");
      if (height != widget.getOffsetHeight()) {
        final int h = b[0] + b[2] + p[0] + p[2];
        final int fixedHeight = DOM.fixQuirks(elem, height - h, 'h');
        widget.setHeight(Math.max(0, fixedHeight) + "px");
      }
    } else if (height == -1) {
      widget.setHeight("auto");
    }

    if (width != widget.getOffsetWidth()) {
      System.out.println(elem.getTagName() + " :: " + widget.getOffsetWidth()
          + "x" + widget.getOffsetHeight() + " ? " + width + "x" + height);
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
  @Deprecated
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

  /**
   * 
   * @param widget
   * @return
   */
  public static Dimension getPreferredSize(Widget widget) {
    // Ignore FormPanel if getWidget() returns a HasLayoutManager implementation
    if (widget instanceof FormPanel) {
      final Widget _widget = ((FormPanel) widget).getWidget();
      if (_widget != null && (_widget instanceof HasLayoutManager)) {
        widget = _widget;
      }
    }
    if (widget instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) widget;
      return lp.getPreferredSize();
    } else {
      final Element elem = widget.getElement();
      
      final String w = widget.getElement().getStyle().getProperty("width");
      final String h = widget.getElement().getStyle().getProperty("height");

      final String prefWidth = DOM.getElementProperty(elem, "prefWidth");
      final String prefHeight = DOM.getElementProperty(elem, "prefHeight");
      
      changeToStaticPositioning(elem);

      if (prefWidth == null) {
        DOM.setElementProperty(elem, "prefWidth", w);
      } else {
        widget.setWidth("0px");
        widget.getOffsetWidth();
        widget.setWidth("auto");
      }

      if (prefHeight == null) {
        DOM.setElementProperty(elem, "prefHeight", h);
      } else {
        widget.setHeight("0px");
        widget.getOffsetHeight();
        widget.setHeight("auto");
      }

      return getOffsetSize(widget);
    }
  }

  private static void changeToStaticPositioning(Element elem) {
    DOM.setStyleAttribute(elem, "left", "");
    DOM.setStyleAttribute(elem, "top", "");
    // ggeorg: see
    // http://groups.google.com/group/gwt-mosaic/browse_thread/thread/83d2bd6d6791ca62
    // DOM.setStyleAttribute(elem, "position", "");
  }

}
