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
package org.gwt.mosaic.ui.client.layout;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

public abstract class BaseLayout extends LayoutManagerHelper implements
    LayoutManager {

  /**
   * TODO: move this method to DOM
   */
  public static int getFlowHeight(Widget child) {
    final int[] m = DOM.getMarginSizes(child.getElement());
    int flowHeight;
    if (child instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) child;
      final int[] preferredSize = lp.getPreferredSize();
      flowHeight = preferredSize[1] + m[0] + m[2];
    } else {
      forceFlowLayout(child.getElement());
      child.setHeight("auto");
      flowHeight = child.getOffsetHeight() + m[0] + m[2];
    }
    return flowHeight;
  }

  private native static void forceFlowLayout(Element elem)
  /*-{
    elem.style['top'] = '';
    elem.style['left'] = '';
  }-*/;

  /**
   * TODO: move this method to DOM
   */
  public static int getFlowWidth(Widget child) {
    final int[] m = DOM.getMarginSizes(child.getElement());
    int flowWidth;
    if (child instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) child;
      final int[] preferredSize = lp.getPreferredSize();
      flowWidth = preferredSize[0] + m[1] + m[3];
    } else {
      forceFlowLayout(child.getElement());
      child.setWidth("auto");
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
    return LayoutManagerHelper.getLayoutData(widget);
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
    LayoutManagerHelper.setLayoutData(widget, layoutData);
  }

  /**
   * TODO: move this method to DOM
   */
  public static void setSize(final Widget widget, final int width,
      final int height) {
    final Element elem = widget.getElement();
    if (width != -1) {
      DOM.setContentAreaWidth(elem, width);
    }
    if (height != -1) {
      DOM.setContentAreaHeight(elem, height);
    }
  }

  protected void setBounds(final LayoutPanel layoutPanel, final Widget widget,
      final int x, final int y, int width, int height) {
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
      if (child != null) {
        if (child instanceof HasLayoutManager) {
          setSize(child, width, height);
          ((HasLayoutManager) child).layout();
        }
      }
    }
  }

  protected void setXY(final LayoutPanel layoutPanel, final Widget widget,
      final int x, final int y) {
    layoutPanel.setWidgetPosition(widget, x, y);
  }

}
