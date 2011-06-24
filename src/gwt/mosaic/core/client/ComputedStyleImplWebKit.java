package gwt.mosaic.core.client;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImplWebKit extends ComputedStyleImpl {

  @Override
  public native ComputedStyle getComputedStyle(Element elem)
  /*-{
    var s;
    if(node.nodeType == 1){
      var dv = node.ownerDocument.defaultView;
      s = dv.getComputedStyle(node, null);
      if(!s && node.style){
        node.style.display = "";
        s = dv.getComputedStyle(node, null);
      }
    }
    return s || {};
  }-*/;

}
