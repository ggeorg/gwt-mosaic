/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.ui.client;

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Viewport extends Composite implements HasWidgets, WindowResizeListener,
    WindowCloseListener {

  private Timer delayedResize = new Timer() {
    @Override
    public void run() {
      final Widget widget = Viewport.this.getWidget();

      final int width = Window.getClientWidth();
      final int height = Window.getClientHeight();

      Viewport.this.setBounds(widget, 0, 0, width, height);

      if (widget instanceof HasLayoutManager) {
        final HasLayoutManager layoutWidget = (HasLayoutManager) widget;
        layoutWidget.layout();
      }
    }
  };

  /**
   * Default constructor.
   */
  public Viewport() {
    final LayoutPanel panel = new LayoutPanel();
    initWidget(panel);

    Window.addWindowCloseListener(this);
    Window.addWindowResizeListener(this);
    Window.enableScrolling(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget widget) {
    add(widget, false);
  }

  public void add(Widget widget, boolean decorate) {
    final LayoutPanel panel = getWidget();
    panel.clear();
    panel.add(widget, new FillLayoutData(decorate));
    
    onLoad();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    getWidget().clear();
  }

  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return getWidget().iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Widget#onLoad()
   */
  @Override
  protected void onLoad() {
    super.onLoad();

    delayedResize.schedule(1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowCloseListener#onWindowClosed()
   */
  public void onWindowClosed() {
    Window.removeWindowResizeListener(this);
    Window.removeWindowCloseListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowCloseListener#onWindowClosing()
   */
  public String onWindowClosing() {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.WindowResizeListener#onWindowResized(int,
   *      int)
   */
  public void onWindowResized(int width, int height) {
    delayedResize.schedule(333);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget w) {
    return getWidget().remove(w);
  }

  /**
   * 
   * @param layoutPanel
   * @param widget
   * @param x
   * @param y
   * @param width
   * @param height
   */
  protected void setBounds(final Widget widget, final int x, final int y,
      final int width, final int height) {
    RootPanel.get().setWidgetPosition(this, x, y);
    setSize(widget, width, height);
  }

  /**
   * 
   * @param widget
   * @param width
   * @param height
   */
  protected void setSize(final Widget widget, int width, int height) {
    Element elem = widget.getElement();

    int[] margins = DOM.getMarginSizes(widget.getElement());

    if (width != -1) {
      width -= (margins[1] + margins[3]);
      DOM.setContentAreaWidth(elem, Math.max(0, width));
    }
    if (height != -1) {
      height -= (margins[0] + margins[2]);
      DOM.setContentAreaHeight(elem, Math.max(0, height));
    }
  }

}
