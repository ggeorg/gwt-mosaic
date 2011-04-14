package gwt.mosaic.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class Spacer extends Widget implements ConstrainedVisual {
	public Spacer() {
		setElement(DOM.createSpan());
		Style style = getElement().getStyle();
		style.setVisibility(Visibility.HIDDEN);
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
