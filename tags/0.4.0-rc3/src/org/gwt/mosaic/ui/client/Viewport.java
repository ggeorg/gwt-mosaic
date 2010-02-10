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

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A top level panel that accepts browser window resize events.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Viewport extends LayoutComposite implements ResizeHandler {

  private final Timer resizeTimer = new Timer() {
    @Override
    public void run() {
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
      layout();
    }
  };

  private final HandlerRegistration resizeHandlerRegistration;
  private final HandlerRegistration closeHandlerRegistration;

  /**
   * Creates a new {@code Viewport} with {@link FillLayout}.
   */
  public Viewport() {
    this(new FillLayout());
  }

  /**
   * Creates a new {@code Viewport} with the specified layout manager.
   * 
   * @param layout the {@link LayoutManager} to use
   */
  public Viewport(LayoutManager layout) {
    super(layout);

    resizeHandlerRegistration = Window.addResizeHandler(this);
    closeHandlerRegistration = Window.addCloseHandler(new CloseHandler<Window>() {
      public void onClose(CloseEvent<Window> event) {
        if (resizeHandlerRegistration != null) {
          resizeHandlerRegistration.removeHandler();
        }
        if (closeHandlerRegistration != null) {
          closeHandlerRegistration.removeHandler();
        }
      }
    });

    Window.enableScrolling(false);
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

  @Override
  protected void onLoad() {
    super.onLoad();

    resizeTimer.schedule(CoreConstants.MIN_DELAY_MILLIS);
  }

  /**
   * Called when the browser window is resized.
   * 
   * @param event the {@code ResizeEvent} fired when the browser window is
   *          resized
   * 
   * @see com.google.gwt.event.logical.shared.ResizeHandler#onResize(com.google.gwt.event.logical.shared.ResizeEvent)
   */
  public void onResize(ResizeEvent event) {
    if (isAttached()) {
      resizeTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
    }
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
