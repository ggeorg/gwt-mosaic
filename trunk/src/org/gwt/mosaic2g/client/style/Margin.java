package org.gwt.mosaic2g.client.style;


import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

public class Margin {
	private static final String SEPARATOR = " ";

	private Unit unit;

	private double marginTop;
	private double marginRight;
	private double marginBottom;
	private double marginLeft;

	public Margin() {
		unit = Unit.PX;
		marginTop = 0.0;
		marginRight = 0.0;
		marginBottom = 0.0;
		marginLeft = 0.0;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(double marginTop) {
		this.marginTop = marginTop;
	}

	public double getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(double marginRight) {
		this.marginRight = marginRight;
	}

	public double getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(double marginBottom) {
		this.marginBottom = marginBottom;
	}

	public double getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(double marginLeft) {
		this.marginLeft = marginLeft;
	}
	
	public void applyTo(Element elem) {
		final Style style = elem.getStyle();
		style.setPaddingTop(marginTop, unit);
		style.setPaddingRight(marginRight, unit);
		style.setPaddingBottom(marginBottom, unit);
		style.setPaddingLeft(marginLeft, unit);
	}
	
	public void readFrom(Element elem) {
		unit = Unit.PX;
		marginTop = ComputedStyle.getPaddingTop(elem);
		marginRight = ComputedStyle.getPaddingRight(elem);
		marginBottom = ComputedStyle.getPaddingBottom(elem);
		marginLeft = ComputedStyle.getPaddingLeft(elem);
	}

	@Override
	public String toString() {
		final String u = unit.getType();
		StringBuilder sb = new StringBuilder();
		sb.append(marginTop).append(u).append(SEPARATOR).append(marginRight)
				.append(u).append(SEPARATOR).append(marginBottom).append(u)
				.append(SEPARATOR).append(marginLeft).append(u);
		return sb.toString();
	}
}
