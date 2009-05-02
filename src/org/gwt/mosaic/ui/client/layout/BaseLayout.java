/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.layout;

import java.util.Iterator;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.UserAgent;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is the abstract base class for layouts.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class BaseLayout extends LayoutManagerHelper implements
    LayoutManager {

  protected int[] margins = {0, 0};
  protected int[] paddings = {0, 0};
  protected int[] borders = {0, 0};
  
  protected boolean initialized = false;
  
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }
    
    margins = DOM.getMarginSizes(layoutPanel.getElement());
    paddings = DOM.getPaddingSizes(layoutPanel.getElement());
    borders = DOM.getBorderSizes(layoutPanel.getElement());
    
    return true;
  }

  protected void recalculate(Map<Widget, Dimension> widgetSizes) {
    for (Iterator<Map.Entry<Widget, Dimension>> iter = widgetSizes.entrySet().iterator(); iter.hasNext();) {
      Map.Entry<Widget, Dimension> entry = iter.next();
      Widget w = entry.getKey();
      entry.getValue().setSize(WidgetHelper.getPreferredSize(w));
    }
  }

  private static void changeToStaticPositioning(Element elem) {
    DOM.setStyleAttribute(elem, "left", "");
    DOM.setStyleAttribute(elem, "top", "");
    // ggeorg: see
    // http://groups.google.com/group/gwt-mosaic/browse_thread/thread/83d2bd6d6791ca62
    // DOM.setStyleAttribute(elem, "position", "");
  }

  /**
   * TODO: move this method to DOM
   */
  public static int xgetFlowHeight(Widget child) {
    int flowHeight;
    if (child instanceof FormPanel) {
      final Widget _child = ((FormPanel) child).getWidget();
      if (_child != null && (_child instanceof HasLayoutManager)) {
        child = _child;
      }
    }
    if (child instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) child;
      final Dimension preferredSize = lp.getPreferredSize();
      flowHeight = preferredSize.height;
    } else {
//      if (!UserAgent.isIE6()) {
//        Object layoutDataObject = getLayoutData(child);
//        if (layoutDataObject != null && layoutDataObject instanceof LayoutData) {
//          LayoutData layoutData = (LayoutData) layoutDataObject;
//          if (layoutData.cachedHeight == null) {
//            layoutData.cachedHeight = child.getElement().getStyle().getProperty(
//                "height");
//            if (layoutData.cachedHeight != null
//                && layoutData.cachedHeight.length() > 0) {
//              return DOM.toPixelSize(layoutData.cachedHeight);
//            } else {
//              layoutData.cachedHeight = "".intern();
//            }
//          }
//        }
//      }
      changeToStaticPositioning(child.getElement());
      // ggeorg: set to "0px", read and set it to "auto"
      // I don't know why but it works for widget like 'ListBox'
      child.setHeight("0px");
      child.getOffsetHeight();
      child.setHeight("auto");
      // ---------------------------------
      final int[] m = DOM.getMarginSizes(child.getElement());
      flowHeight = child.getOffsetHeight() + m[0] + m[2];
    }
    return flowHeight;
  }

  /**
   * TODO: move this method to DOM
   */
  public static int xgetFlowWidth(Widget child) {
    int flowWidth;
    if (child instanceof FormPanel) {
      final Widget _child = ((FormPanel) child).getWidget();
      if (_child != null && (_child instanceof HasLayoutManager)) {
        child = _child;
      }
    }
    if (child instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) child;
      final Dimension preferredSize = lp.getPreferredSize();
      flowWidth = preferredSize.width;
    } else {
//      if (!UserAgent.isIE6()) {
//        Object layoutDataObject = getLayoutData(child);
//        if (layoutDataObject != null && layoutDataObject instanceof LayoutData) {
//          LayoutData layoutData = (LayoutData) layoutDataObject;
//          if (layoutData.cachedWidth == null) {
//            layoutData.cachedWidth = child.getElement().getStyle().getProperty(
//                "width");
//            if (layoutData.cachedWidth != null
//                && layoutData.cachedWidth.length() > 0) {
//              return DOM.toPixelSize(layoutData.cachedWidth);
//            } else {
//              layoutData.cachedWidth = "".intern();
//            }
//          }
//        }
//      }
      changeToStaticPositioning(child.getElement());
      // ggeorg: set to "0px", read and set it to "auto"
      // I don't know why but it works for widget like 'ListBox'
      child.setWidth("0px");
      child.getOffsetWidth();
      child.setWidth("auto");
      // ---------------------------------
      final int[] m = DOM.getMarginSizes(child.getElement());
      flowWidth = child.getOffsetWidth() + m[1] + m[3];
    }
    return flowWidth;
  }

  /**
   * Gets the panel-defined layout data associated with this widget.
   * 
   * @param widget the widget
   * @return the widget's layout data
   */
  protected final static Object getLayoutData(Widget widget) {
    return LayoutManagerHelper._getLayoutData(widget);
  }

  /**
   * Sets the panel-defined layout data associated with this widget. Only the
   * panel that currently contains a widget should ever set this value. It
   * serves as a place to store layout bookkeeping data associated with a
   * widget.
   * 
   * @param widget the widget
   * @param layoutData the widget's layout data
   */
  protected final static void setLayoutData(Widget widget, Object layoutData) {
    LayoutManagerHelper._setLayoutData(widget, layoutData);
  }

  /**
   * {@inheritDoc}
   * <p>
   * The default implementation returns {@code false}.
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#runTwice()
   */
  public boolean runTwice() {
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#flushCache()
   */
  public void flushCache() {
    // Nothing to do here!
  }
}
