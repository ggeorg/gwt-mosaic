package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class WidgetHelper {

	static LayoutData getLayoutData(Widget w) {
		LayoutData layoutData = null;

		Object obj = w.getLayoutData();
		if (obj == null || !(obj instanceof LayoutData)) {
			layoutData = new LayoutData(w);
			w.setLayoutData(layoutData);
		} else {
			layoutData = (LayoutData) obj;
		}

		return layoutData;
	}

	public static void setSize(Widget w, int width, int height) {
//		System.out.println(w.getClass().getName() + ":: " + width + "x"
//				+ height);
		if (width >= 0) {
			int oldWidth = w.getOffsetWidth();
			width -= getBoxModel(w).getWidthContribution();
			w.setWidth(Math.max(width, 1) + Unit.PX.toString()); // XXX
			if (oldWidth != w.getOffsetWidth()) {
				WidgetHelper.invalidate(w);
			}
			// NOTE: don't use widgetStyle.setWidth(width, Unit.PX) !!!
		}
		if (height >= 0) {
			int oldHeight = w.getOffsetHeight();
			height -= getBoxModel(w).getHeightContribution();
			w.setHeight(Math.max(height, 1) + Unit.PX.toString()); // XXX
			if (oldHeight != w.getOffsetHeight()) {
				WidgetHelper.invalidate(w);
			}
			// NOTE: don't use widgetStyle.setHeight(height, Unit.PX) !!!
		}
	}

	public static void setLocation(Widget w, int x, int y) {
		LayoutData layoutData = getLayoutData(w);
		x += layoutData.getDx();
		y += layoutData.getDy();
		Element elem = w.getElement();
		DOM.setStyleAttribute(elem, "position", "absolute");
		DOM.setStyleAttribute(elem, "left", x + "px");
		DOM.setStyleAttribute(elem, "top", y + "px");
	}

	// ------------------------------------------------------------------------

	public static BoxModel getBoxModel(Widget w) {
		return getLayoutData(w).getBoxModel();
	}

	public static void invalidate(Widget w) {
		if (w instanceof LayoutPanel) {
			((LayoutPanel) w).invalidate();
		} else {
			Widget parent = w.getParent();
			if (parent != null && parent instanceof LayoutPanel) {
				((LayoutPanel) parent).invalidate();
			}
		}
	}

	public static int getPreferredWidth(Widget w, int height) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredWidth(height);
		} else {
			return getPreferredWidthImpl(w, height);
		}
	}

	static int getPreferredWidthImpl(Widget w, int height) {
		LayoutData layoutData = getLayoutData(w);
		String widthHint = layoutData.getPreferredWidth();

		if (w.isAttached()) {
			Element elem = w.getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			String oldWidth = DOM.getStyleAttribute(elem, "width");
			String oldHeight = DOM.getStyleAttribute(elem, "height");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				if (widthHint == null) {
					DOM.setStyleAttribute(elem, "width", "");
				} else {
					DOM.setStyleAttribute(elem, "width", widthHint);
				}
				DOM.setStyleAttribute(elem, "height", (height < 0) ? ""
						: (height + Unit.PX.getType()));
				return elem.getOffsetWidth()
						+ getBoxModel(w).getMarginWidthContribution();
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
				DOM.setStyleAttribute(elem, "width", oldWidth);
				DOM.setStyleAttribute(elem, "height", oldHeight);
			}
		} else {
			// TODO check layoutData
			return -1;
		}
	}

	public static void setPreferredWidth(Widget w, String preferredWidth) {
		getLayoutData(w).setPreferredWidth(preferredWidth);
		invalidate(w);
	}

	public static int getPreferredHeight(Widget w, int width) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredHeight(width);
		} else {
			return getPreferredHeightImpl(w, width);
		}
	}

	static int getPreferredHeightImpl(Widget w, int width) {
		LayoutData layoutData = getLayoutData(w);
		String heightHint = layoutData.getPreferredHeight();

		if (w.isAttached()) {
			Element elem = w.getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			String oldWidth = DOM.getStyleAttribute(elem, "width");
			String oldHeight = DOM.getStyleAttribute(elem, "height");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				DOM.setStyleAttribute(elem, "width", (width < 0) ? ""
						: (width + Unit.PX.getType()));
				if (heightHint == null) {
					DOM.setStyleAttribute(elem, "height", "");
				} else {
					DOM.setStyleAttribute(elem, "height", heightHint);
				}
				return elem.getOffsetHeight()
						+ getBoxModel(w).getMarginHeightContribution();
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
				DOM.setStyleAttribute(elem, "width", oldWidth);
				DOM.setStyleAttribute(elem, "height", oldHeight);
			}
		} else {
			// TODO check layoutData
			return -1;
		}
	}

	public static void setPreferredHeight(Widget w, String preferredHeight) {
		getLayoutData(w).setPreferredHeight(preferredHeight);
		invalidate(w);
	}

	public static Dimensions getPreferredSize(Widget w) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredSize();
		} else {
			return new Dimensions(getPreferredWidth(w, -1), getPreferredHeight(
					w, -1));
		}
	}

	public static int getBaseline(Widget w) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getBaseline();
		} else {
			Element elem = w.getElement();
			return getBaseline(w, elem.getClientWidth(), elem.getClientHeight());
		}
	}

	public static int getBaseline(Widget w, int width, int height) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getBaseline(width, height);
		} else {
			return 0;
		}
	}

	public static int getColumnSpan(Widget w) {
		return getLayoutData(w).getColumnSpan();
	}

	public static void setColumnSpan(Widget w, int columnSpan) {
		getLayoutData(w).setColumnSpan(columnSpan);
		invalidate(w);
	}

	public static int getRowSpan(Widget w) {
		return getLayoutData(w).getRowSpan();
	}

	public static void setRowSpan(Widget w, int rowSpan) {
		getLayoutData(w).setRowSpan(rowSpan);
		invalidate(w);
	}

	public static int getWeight(Widget w) {
		return getLayoutData(w).getWeight();
	}

	public static void setWeight(Widget w, int weight) {
		getLayoutData(w).setWeight(weight);
		invalidate(w);
	}

	public static void translate(Box w, int dx, int dy) {
		getLayoutData(w).translate(dx, dy);
		invalidate(w);
	}
}
