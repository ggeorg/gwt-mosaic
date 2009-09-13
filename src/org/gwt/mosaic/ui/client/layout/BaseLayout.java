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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Insets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
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

  protected boolean initialized = false;

  @Deprecated
  protected int[] margins = {0, 0};
  @Deprecated
  protected int[] borders = {0, 0};
  @Deprecated
  protected int[] paddings = {0, 0};

  protected static Dimension getPreferredSize(LayoutPanel layoutPanel,
      Widget widget, LayoutData layoutData) {
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

      if (layoutData.preferredWidth != null
          && layoutData.preferredHeight != null) {
        return new Dimension(layoutPanel.toPixelSize(layoutData.preferredWidth,
            true), layoutPanel.toPixelSize(layoutData.preferredHeight, false));
      }

      final Dimension result = new Dimension();

      // final Element parentElem = layoutPanel.getElement();
      final Element clonedElem = widget.getElement();// .cloneNode(true).cast();

      final Style style = clonedElem.getStyle();
      style.setProperty("position", "static");
      // style.setProperty("visibility", "hidden");
      // style.setProperty("width", "auto");
      // style.setProperty("height", "auto");

      // parentElem.appendChild(clonedElem);

      if (layoutData.preferredWidth != null) {
        result.width = layoutPanel.toPixelSize(layoutData.preferredWidth, true);
      } else {
        style.setProperty("width", "auto");
        result.width = clonedElem.getOffsetWidth();
      }

      if (layoutData.preferredHeight != null) {
        result.height = layoutPanel.toPixelSize(layoutData.preferredHeight,
            false);
      } else {
        style.setProperty("height", "auto");
        result.height = clonedElem.getOffsetHeight();
      }

      // parentElem.removeChild(clonedElem);
      style.setProperty("position", "absolute");

      return result;
    }
  }

  protected void syncDecoratorVisibility(Widget child) {
    LayoutData layoutData = (LayoutData) getLayoutData(child);
    if (layoutData != null && layoutData.hasDecoratorPanel()) {
      layoutData.decoratorPanel.setVisible(DOM.isVisible(child.getElement()));
    }
  }

  protected int getDecoratorFrameWidth(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetWidth() - child.getOffsetWidth();
  }

  protected Dimension getDecoratorFrameSize(DecoratorPanel decPanel,
      Widget child) {
    return new Dimension(decPanel.getOffsetWidth() - child.getOffsetWidth(),
        decPanel.getOffsetHeight() - child.getOffsetHeight());
  }

  protected int getDecoratorFrameHeight(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetHeight() - child.getOffsetHeight();
  }

  protected Insets insets = new Insets(0, 0, 0, 0);

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    final Element layoutPanelElem = layoutPanel.getElement();
    margins = DOM.getMarginSizes(layoutPanelElem);
    borders = DOM.getBorderSizes(layoutPanelElem);
    paddings = DOM.getPaddingSizes(layoutPanelElem);

    insets.top = margins[0] + borders[0] + paddings[0];
    insets.right = margins[1] + borders[1] + paddings[1];
    insets.bottom = margins[2] + borders[2] + paddings[2];
    insets.left = margins[3] + borders[3] + paddings[3];

    return true;
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
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#flushCache()
   */
  public void flushCache() {
    initialized = false;
  }

}
