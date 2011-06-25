package gwt.mosaic.core.client;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImpl {

  public native ComputedStyle getComputedStyle(Element elem)
  /*-{
    return elem.nodeType == 1 ?
        elem.ownerDocument.defaultView.getComputedStyle(elem, null) : {};
  }-*/;

  public native String getProperty(ComputedStyle computedStyle, String name)
  /*-{
    return computedStyle[name];
  }-*/;

}