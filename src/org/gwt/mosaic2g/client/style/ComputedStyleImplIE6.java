package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImplIE6 extends ComputedStyleImpl {

	@Override
	public native String getProperty(Element elem, String property)
	/*-{
	  var value;
	  if (document.documentElement.currentStyle) { // IE method
	    switch(property) {
	      case 'opacity' : // IE opacity uses filter
	        var val = 100;
	        try { // will error if no DXImageTransform
	          val = elem.filters['DXImageTransform.Microsoft.Alpha'].opacity;
	        } catch(e) {
	          try { // make sure its in the document
	            val = elem.filters('alpha').opacity;
	          } catch(e) {
	            // ignore
	          }
	        }
	        return val / 100;
	      case 'float': // fix reserved word
	        property = 'styleFloat'; // fall through
	      default:
	        // test currentStyle before touching
	        value = elem.currentStyle ? elem.currentStyle[property] : null;
	        value = (elem.style[property] || value || null);
	    }
	  } else { // default to inline only
	    value = (elem.style[property] || null);
	  }
	  return (value == null) ? null : ''+value;
	}-*/;

}
