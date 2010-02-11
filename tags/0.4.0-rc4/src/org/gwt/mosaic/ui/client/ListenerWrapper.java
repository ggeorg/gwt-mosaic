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
package org.gwt.mosaic.ui.client;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.AbstractWindowClosingEvent;
import com.google.gwt.user.client.BaseListenerWrapper;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T> listener type
 * @deprecated will be removed in GWT 2.0 with the handler listeners themselves
 */
// FIXME: Bug: The class name org.gwt.mosaic.ui.client.ListenerWrapper shadows
// the simple name of the superclass
// com.google.gwt.user.client.ui.ListenerWrapper
@Deprecated
public abstract class ListenerWrapper<T> extends
    com.google.gwt.user.client.ui.ListenerWrapper<T> {

  public static class WrappedDoubleClickListener extends
      ListenerWrapper<DoubleClickListener> implements DoubleClickHandler {

    @Deprecated
    public static WrappedDoubleClickListener add(HasDoubleClickHandlers source,
        DoubleClickListener listener) {
      WrappedDoubleClickListener rtn = new WrappedDoubleClickListener(listener);
      source.addDoubleClickHandler(rtn);
      return rtn;
    }

    @Deprecated
    public static void remove(Widget eventSource, DoubleClickListener listener) {
      baseRemove(eventSource, listener, DoubleClickEvent.getType());
    }

    protected WrappedDoubleClickListener(DoubleClickListener listener) {
      super(listener);
    }

    public void onDoubleClick(DoubleClickEvent event) {
      getListener().onDoubleClick(getSource(event));
    }

  }

  static class WrapWindowPanelClose extends
      BaseListenerWrapper<WindowCloseListener> implements ClosingHandler,
      CloseHandler<PopupPanel> {
    @Deprecated
    public static void add(WindowPanel windowPanel, WindowCloseListener listener) {
      WrapWindowPanelClose handler = new WrapWindowPanelClose(listener);
      windowPanel.addWindowClosingHandler(handler);
      windowPanel.addCloseHandler(handler);
    }

    public static void remove(HandlerManager manager,
        WindowCloseListener listener) {
      baseRemove(manager, listener, AbstractWindowClosingEvent.getType());
    }

    private WrapWindowPanelClose(WindowCloseListener listener) {
      super(listener);
    }

    public void onClose(CloseEvent<PopupPanel> event) {
      getListener().onWindowClosed();
    }

    public void onWindowClosing(ClosingEvent event) {
      String message = getListener().onWindowClosing();
      if (event.getMessage() != null) {
        event.setMessage(message);
      }
    }

  }

  static class WrapWindowPanelResize extends
      BaseListenerWrapper<WindowResizeListener> implements ResizeHandler {
    @Deprecated
    public static void add(WindowPanel windowPanel,
        WindowResizeListener listener) {
      windowPanel.addResizeHandler(new WrapWindowPanelResize(listener));
    }

    public static void remove(HandlerManager manager,
        WindowResizeListener listener) {
      baseRemove(manager, listener, ResizeEvent.getType());
    }

    protected WrapWindowPanelResize(WindowResizeListener listener) {
      super(listener);
    }

    public void onResize(ResizeEvent event) {
      getListener().onWindowResized(event.getWidth(), event.getHeight());
    }

  }

  protected ListenerWrapper(T listener) {
    super(listener);
  }
}
