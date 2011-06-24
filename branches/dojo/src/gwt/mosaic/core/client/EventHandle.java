package gwt.mosaic.core.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventListener;

public class EventHandle extends JavaScriptObject {

  public static EventHandle connect(Element elem, String event, EventListener listener) {
    return EventHandle.addEventListener(elem, event, false, listener);
  }

  public static EventHandle connect(Element elem, String event, boolean capture,
      EventListener listener) {
    EventHandle eventHandle = EventHandle.addEventListener(elem, event, capture, listener);
    return eventHandle;
  }

  private static native EventHandle addEventListener(Element ele, String event, boolean capture,
      EventListener listener)
  /*-{
    var callBack = function(e) {
      listener.@com.google.gwt.user.client.EventListener::onBrowserEvent(Lcom/google/gwt/user/client/Event;)(e);
    };
    ele.addEventListener(event, callBack, capture);
    return {callBack: callBack, ele: ele, event: event, capture: capture};
  }-*/;

  private static native void removeEventListener(EventHandle h)
  /*-{
    try {
    h.ele.removeEventListener(h.event, h.callBack, h.capture);
    }catch(e){alert(e);}
  }-*/;

  protected EventHandle() {
  }

  public final void disconnect() {
    EventHandle.removeEventListener(this);
  }

}
