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
package org.gwt.mosaic.ui.client;

import java.util.Iterator;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Viewport extends LayoutComposite implements
    WindowResizeListener, WindowCloseListener {

  private final Timer resizeTimer = new Timer() {
    @Override
    public void run() {
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
      layout();
    }
  };

  /**
   * Default constructor.
   */
  public Viewport() {
    super();

    Window.addWindowCloseListener(this);
    Window.addWindowResizeListener(this);

    Window.enableScrolling(false);
  }

  @Deprecated
  public void add(Widget widget) {
    add(widget, false);
  }

  @Deprecated
  public void add(Widget w, boolean decorate) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.clear();
    if (!(layoutPanel.getLayout() instanceof FillLayout)) {
      layoutPanel.setLayout(new FillLayout());
    }
    layoutPanel.add(w, new FillLayoutData(decorate));
  }

  @Deprecated
  public void clear() {
    getLayoutPanel().clear();
  }

  /**
   * Returns the internal {@link LayoutPanel} for this {@code Viewport}.
   * 
   * @return the internal {@link LayoutPanel}
   */
  @Override
  public LayoutPanel getLayoutPanel() {
    return super.getLayoutPanel();
  }

  @Deprecated
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    resizeTimer.schedule(CoreConstants.MIN_DELAY_MILLIS);
  }

  public void onWindowClosed() {
    Window.removeWindowResizeListener(this);
    Window.removeWindowCloseListener(this);
  }

  public String onWindowClosing() {
    return null;
  }

  public void onWindowResized(int width, int height) {
    if (isAttached()) {
      resizeTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
    }
  }

  @Deprecated
  public boolean remove(Widget w) {
    return getLayoutPanel().remove(w);
  }

  private void setBounds(final int x, final int y, int width, int height) {
    RootPanel.get().setWidgetPosition(this, x, y);

    final Element elem = getElement();
    final int[] margins = DOM.getMarginSizes(elem);

    WidgetHelper.setSize(this, width - (margins[1] + margins[3]), height
        - (margins[0] + margins[2]));
  }
  
  /**
   * 
   * @see #removeFromParent()
   */
  public void attach() {
    if (!isAttached()) {
      RootPanel.get().add(this, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
  }

}
