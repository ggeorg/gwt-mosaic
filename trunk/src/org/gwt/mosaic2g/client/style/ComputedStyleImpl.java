package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Element;

public class ComputedStyleImpl {

	public native String getProperty(Element elem, String property)
	/*-{
	  if (document.defaultView && document.defaultView.getComputedStyle) { // W3C DOM method
	    var value = null;
	    if (property == 'float') { // fix reserved word
	      property = 'cssFloat';
	    }
	    var computed = elem.ownerDocument.defaultView.getComputedStyle(elem, '');
	    if (computed) { // test computed before touching for safari
	      value = computed[property];
	    }
	    return (elem.style[property] || value || '');
	  } else { // default to inline only
	    return (elem.style[property] || '');
	  }
	}-*/;
}
