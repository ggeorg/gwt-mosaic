package gwt.mosaic.core.client;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImpl {

  public native ComputedStyle getComputedStyle(Element elem)
  /*-{
    return node.nodeType == 1 ?
        node.ownerDocument.defaultView.getComputedStyle(node, null) : {};
  }-*/;

  public native String getProperty(ComputedStyle computedStyle, String name)
  /*-{
    return computedStyle[name];
  }-*/;

}