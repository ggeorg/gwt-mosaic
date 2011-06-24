package gwt.mosaic.core.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class BoundingClientRect extends JavaScriptObject {

  public static native BoundingClientRect get(Element elem)
  /*-{
    return elem.getBoundingClientRect();
  }-*/;

  protected BoundingClientRect() {
  }

  public final native int getLeft() /*-{ return left; }-*/;

  public final native int getTop() /*-{ return top; }-*/;

  public final native int getRight() /*-{return right; }-*/;

  public final native int getBottom() /*-{ return bottom; }-*/;

}
