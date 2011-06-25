package gwt.mosaic.core.client;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImplWebkit extends ComputedStyleImpl {

  @Override
  public native ComputedStyle getComputedStyle(Element elem)
  /*-{
    var s;
    if(elem.nodeType == 1){
      var dv = elem.ownerDocument.defaultView;
      s = dv.getComputedStyle(elem, null);
      if(!s && elem.style){
        elem.style.display = "";
        s = dv.getComputedStyle(elem, null);
      }
    }
    return s || {};
  }-*/;

}
