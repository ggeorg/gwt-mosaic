package org.gwt.mosaic.ui.client;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.Widget;

@Deprecated
public abstract class ListenerWrapper<T> extends
    com.google.gwt.user.client.ui.ListenerWrapper<T> {

  protected ListenerWrapper(T listener) {
    super(listener);
  }

  public static class WrappedDoubleClickListener extends
      ListenerWrapper<DoubleClickListener> implements DoubleClickHandler {

    protected WrappedDoubleClickListener(DoubleClickListener listener) {
      super(listener);
    }

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

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
      getListener().onDoubleClick(getSource(event));
    }

  }
}
