package gwt.mosaic.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget representing a horizontal divider.
 */
public class Separator extends Widget {
	interface MyUiBinder extends UiBinder<Element, Separator> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	SpanElement txtSpan;

	public Separator() {
		this(null);
	}

	public Separator(String heading) {
		setElement(uiBinder.createAndBindUi(this));
		setHeading(heading);
	}

	public String getHeading() {
		return txtSpan.getInnerText();
	}

	public void setHeading(String heading) {
		String previousHeading = getHeading();

		if (previousHeading != heading) {
			if (heading != null) {
				txtSpan.setInnerText(heading);
				txtSpan.getStyle().setDisplay(Display.INLINE);
			} else {
				txtSpan.setInnerText("");
				txtSpan.getStyle().setDisplay(Display.NONE);
			}
			// separatorListeners.headingChanged(this, previousHeading);
		}
	}
}
