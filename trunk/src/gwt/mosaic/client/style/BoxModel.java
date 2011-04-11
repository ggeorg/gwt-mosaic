package gwt.mosaic.client.style;

import gwt.mosaic.client.ui.Insets;
import gwt.mosaic.client.ui.WidgetHelper;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class BoxModel {
	private Insets margin = Insets.NONE;
	private Insets border = Insets.NONE;
	private Insets padding = Insets.NONE;

	private final Widget widget;

	public BoxModel(Widget widget) {
		if (widget == null) {
			throw new IllegalArgumentException("widget is null");
		}
		this.widget = widget;
	}

	public Insets getMargin() {
		if (margin == Insets.NONE && widget.isAttached()) {
			Element elem = widget.getElement();
			margin = new Insets(ComputedStyle.getMarginTop(elem),
					ComputedStyle.getMarginLeft(elem),
					ComputedStyle.getMarginBottom(elem),
					ComputedStyle.getMarginRight(elem));
			if (Insets.NONE.equals(margin)) {
				WidgetHelper.invalidate(widget);
			}
		}
		return margin;
	}

	public Insets getBorder() {
		if (border == Insets.NONE && widget.isAttached()) {
			Element elem = widget.getElement();
			border = new Insets(ComputedStyle.getBorderTopWidth(elem),
					ComputedStyle.getBorderLeftWidth(elem),
					ComputedStyle.getBorderBottomWidth(elem),
					ComputedStyle.getBorderRightWidth(elem));
			if (!Insets.NONE.equals(border)) {
				WidgetHelper.invalidate(widget);
			}
		}
		return border;
	}

	public Insets getPadding() {
		if (padding == Insets.NONE && widget.isAttached()) {
			Element elem = widget.getElement();
			padding = new Insets(ComputedStyle.getPaddingTop(elem),
					ComputedStyle.getPaddingLeft(elem),
					ComputedStyle.getPaddingBottom(elem),
					ComputedStyle.getPaddingRight(elem));
			if (Insets.NONE.equals(padding)) {
				WidgetHelper.invalidate(widget);
			}
		}
		return padding;
	}

	public int getWidthContribution() {
		Insets margin = getMargin();
		Insets border = getBorder();
		Insets padding = getPadding();
		return (margin.left + margin.right) + (border.left + border.right)
				+ (padding.left + padding.right);
	}

	public int getHeightContribution() {
		Insets margin = getMargin();
		Insets border = getBorder();
		Insets padding = getPadding();
		return (margin.top + margin.bottom) + (border.top + border.bottom)
				+ (padding.top + padding.bottom);
	}

	public int getMarginWidthContribution() {
		Insets margin = getMargin();
		return (margin.left + margin.right);
	}

	public int getMarginHeightContribution() {
		Insets margin = getMargin();
		return (margin.top + margin.bottom);
	}
	
	public int getBorderWidthContribution() {
		Insets border = getBorder();
		return (border.left + border.right);
	}
	
	public int getBorderHeightContribution() {
		Insets border = getBorder();
		return (border.top + border.bottom);
	}

	public int getPaddingWidthContribution() {
		Insets padding = getPadding();
		return (padding.left + padding.right);
	}

	public int getPaddingHeightContribution() {
		Insets padding = getPadding();
		return (padding.top + padding.bottom);
	}

}
