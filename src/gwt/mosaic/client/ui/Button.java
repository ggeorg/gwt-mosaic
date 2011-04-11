package gwt.mosaic.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;

public class Button extends com.google.gwt.user.client.ui.Button implements
		ConstrainedVisual {

	public Button() {
		super();
		init();
	}

	protected Button(com.google.gwt.dom.client.Element element) {
		super(element.<Element> cast());
		init();
	}

	public Button(SafeHtml html) {
		super(html.asString());
		init();
	}

	public Button(SafeHtml html, ClickHandler handler) {
		super(html.asString(), handler);
		init();
	}

	public Button(String html) {
		super(html);
		init();
	}

	public Button(String html, ClickHandler handler) {
		super(html, handler);
		init();
	}

	protected void init() {
		setStyleName("m-Button");
	}

	@Override
	public int getBaseline() {
		Element elem = getElement();
		return getBaseline(elem.getClientWidth(), elem.getClientHeight());
	}

	@Override
	public int getPreferredWidth(int height) {
		return WidgetHelper.getPreferredWidthImpl(this, height);
	}

	@Override
	public int getPreferredHeight(int width) {
		return WidgetHelper.getPreferredHeightImpl(this, width);
	}

	@Override
	public Dimensions getPreferredSize() {
		return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	}

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	//
	// UiBinder related layout hints
	//

	@Override
	public void setPreferredWidth(String preferredWidth) {
		WidgetHelper.setPreferredWidth(this, preferredWidth);
	}

	@Override
	public void setPreferredHeight(String preferredHeight) {
		WidgetHelper.setPreferredHeight(this, preferredHeight);
	}

	@Override
	public void setColumnSpan(int columnSpan) {
		WidgetHelper.setColumnSpan(this, columnSpan);
	}

	@Override
	public void setRowSpan(int rowSpan) {
		WidgetHelper.setRowSpan(this, rowSpan);
	}

	@Override
	public void setWeight(int weight) {
		WidgetHelper.setWeight(this, weight);
	}
}
