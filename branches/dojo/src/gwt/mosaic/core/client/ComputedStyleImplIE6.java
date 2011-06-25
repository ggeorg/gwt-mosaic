package gwt.mosaic.core.client;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImplIE6 extends ComputedStyleImpl {

  @Override
  public native ComputedStyle getComputedStyle(Element elem)
  /*-{
    return elem.nodeType == 1 ? elem.currentStyle : {};
  }-*/;

  @Override
  public native String getProperty(ComputedStyle computedStyle, String property)
  /*-{
    var value;
    switch (property) {
    case 'opacity': // IE opacity uses filter
      var val = 100;
      try { // will error if no DXImageTransform
        val = elem.filters['DXImageTransform.Microsoft.Alpha'].opacity;
      } catch (e) {
        try { // make sure its in the document
          val = elem.filters('alpha').opacity;
        } catch (e) {
          // ignore
        }
      }
      return val / 100;
    case 'float': // fix reserved word
      property = 'styleFloat'; // fall through
    default:
      // test currentStyle before touching
      value = computedStyle[property];
    }
    return (value == null) ? null : '' + value;
  }-*/;

}
