package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

public class OpacityImplIE6 extends OpacityImpl {

	@Override
	public void applyTo(Element elem, int opacity) {
		final Style widgetStyle = elem.getStyle();
		widgetStyle.setProperty("filter", "alpha(opacity=" + opacity + ")");
	}

	@Override
	public String toString(int opacity) {
		return "alpha(opacity=" + opacity + ")";
	}

}
