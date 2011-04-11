package gwt.mosaic.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class LabelImpl {
	public int getPreferredWidth(Widget w, int height) {
		int preferredWidth;

		LayoutData layoutData = WidgetHelper.getLayoutData(w);
		String widthHint = layoutData.getPreferredWidth();

		if (w.isAttached()) {
			Element elem = w.getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				if (widthHint == null) {
					DOM.setStyleAttribute(elem, "width", "");
				} else {
					DOM.setStyleAttribute(elem, "width", widthHint);
				}
				DOM.setStyleAttribute(elem, "height", (height < 0) ? ""
						: (height + Unit.PX.getType()));
				preferredWidth = elem.getOffsetWidth()
						+ WidgetHelper.getBoxModel(w)
								.getMarginWidthContribution();
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
			}
		} else {
			// TODO check layoutData
			preferredWidth = -1;
		}

		return preferredWidth;
	}

	public int getPreferredHeight(Widget w, int width) {
		int preferredHeight;

		LayoutData layoutData = WidgetHelper.getLayoutData(w);
		String heightHint = layoutData.getPreferredHeight();

		if (w.isAttached()) {
			Element elem = w.getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				DOM.setStyleAttribute(elem, "width", (width < 0) ? ""
						: (width + Unit.PX.getType()));
				if (heightHint == null) {
					DOM.setStyleAttribute(elem, "height", "");
				} else {
					DOM.setStyleAttribute(elem, "height", heightHint);
				}
				preferredHeight = elem.getOffsetHeight()
						+ WidgetHelper.getBoxModel(w)
								.getMarginHeightContribution();
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
			}
		} else {
			// TODO check layoutData
			preferredHeight = -1;
		}
		
		return preferredHeight;
	}
}
