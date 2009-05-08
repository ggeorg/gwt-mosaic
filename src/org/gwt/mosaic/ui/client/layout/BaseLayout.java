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

import com.google.gwt.user.client.ui.DecoratorPanel;
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

  protected int getDecoratorFrameWidth(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetWidth() - child.getOffsetWidth();
  }

  protected Dimension getDecoratorFrameSize(DecoratorPanel decPanel, Widget child) {
    return new Dimension(decPanel.getOffsetWidth() - child.getOffsetWidth(),
        decPanel.getOffsetHeight() - child.getOffsetHeight());
  }

  protected int getDecoratorFrameHeight(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetHeight() - child.getOffsetHeight();
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    margins = DOM.getMarginSizes(layoutPanel.getElement());
    paddings = DOM.getPaddingSizes(layoutPanel.getElement());
    borders = DOM.getBorderSizes(layoutPanel.getElement());

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
