package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

public class OpacityImpl {

	public void applyTo(Element elem, int opacity) {
		final Style widgetStyle = elem.getStyle();
		widgetStyle.setOpacity(opacity / 255.0);
	}

	public String toString(int opacity) {
		return String.valueOf(opacity / 255.0);
	}

}
