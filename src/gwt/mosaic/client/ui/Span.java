package gwt.mosaic.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class Span extends Widget {
	public Span() {
		setElement(DOM.createSpan());
		Style style = getElement().getStyle();
		style.setVisibility(Visibility.HIDDEN);
	}
}
