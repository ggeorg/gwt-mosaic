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

	public static int getWidth(Widget w) {
		return getLayoutData(w).getBoxModel().getMarginWidthContribution()
				+ w.getOffsetWidth();
	}

	public static int getHeight(Widget w) {
		return getLayoutData(w).getBoxModel().getMarginHeightContribution()
				+ w.getOffsetHeight();
	}

	public static void setSize(Widget w, int width, int height) {
		// System.out.println(w.getClass().getName() + ":: " + width + "x"
		// + height);
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
		invalidate(w, false);
	}

	public static void invalidate(Widget w, boolean immediate) {
		if (w instanceof LayoutPanel) {
			((LayoutPanel) w).invalidate(immediate);
		} else {
			Widget parent = w.getParent();
			if (parent != null && (parent instanceof LayoutPanel)) {
				((LayoutPanel) parent).invalidate(immediate);
			}
		}
	}

	public static int getPreferredWidth(Widget w) {
		return getPreferredWidth(w, -1);
	}

	public static int getPreferredWidth(Widget w, int clientHeight) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredWidth(clientHeight);
		} else {
			return getPreferredWidthImpl(w, clientHeight);
		}
	}

	static int getPreferredWidthImpl(Widget w, int clientHeight) {
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
				DOM.setStyleAttribute(elem, "height", (clientHeight < 0) ? ""
						: (clientHeight + Unit.PX.getType()));
				return getWidth(w);
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
		setPreferredWidth(w, preferredWidth, false);
	}

	public static void setPreferredWidth(Widget w, String preferredWidth,
			boolean invalidate) {
		if (getLayoutData(w).setPreferredWidth(preferredWidth)) {
			invalidate(w, invalidate);
		}
	}

	public static int getPreferredHeight(Widget w) {
		return getPreferredHeight(w, -1);
	}

	public static int getPreferredHeight(Widget w, int clientWidth) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredHeight(clientWidth);
		} else {
			return getPreferredHeightImpl(w, clientWidth);
		}
	}

	static int getPreferredHeightImpl(Widget w, int clientWidth) {
		LayoutData layoutData = getLayoutData(w);
		String heightHint = layoutData.getPreferredHeight();

		if (w.isAttached()) {
			Element elem = w.getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			String oldWidth = DOM.getStyleAttribute(elem, "width");
			String oldHeight = DOM.getStyleAttribute(elem, "height");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				DOM.setStyleAttribute(elem, "width", (clientWidth < 0) ? ""
						: (clientWidth + Unit.PX.getType()));
				if (heightHint == null) {
					DOM.setStyleAttribute(elem, "height", "");
				} else {
					DOM.setStyleAttribute(elem, "height", heightHint);
				}
				return getHeight(w);
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

	}

	public static void setPreferredHeight(Widget w, String preferredHeight,
			boolean invalidate) {
		if (getLayoutData(w).setPreferredHeight(preferredHeight)) {
			invalidate(w);
		}
	}

	public static Dimensions getPreferredSize(Widget w) {
		if (w instanceof ConstrainedVisual) {
			return ((ConstrainedVisual) w).getPreferredSize();
		} else {
			return new Dimensions(getPreferredWidth(w), getPreferredHeight(w));
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

}
