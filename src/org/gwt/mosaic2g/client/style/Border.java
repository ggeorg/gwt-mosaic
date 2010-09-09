package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Style.Unit;

public class Border {

	private Unit unit;

	private double borderTopWidth;
	private double borderRightWidth;
	private double borderBottomWidth;
	private double borderLeftWidth;
	
	public Border() {
		unit = Unit.PX;
		borderTopWidth = 0.0;
		borderRightWidth = 0.0;
		borderBottomWidth = 0.0;
		borderLeftWidth = 0.0;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getBorderTopWidth() {
		return borderTopWidth;
	}

	public void setBorderTopWidth(double borderTopWidth) {
		this.borderTopWidth = borderTopWidth;
	}

	public double getBorderRightWidth() {
		return borderRightWidth;
	}

	public void setBorderRightWidth(double borderRightWidth) {
		this.borderRightWidth = borderRightWidth;
	}

	public double getBorderBottomWidth() {
		return borderBottomWidth;
	}

	public void setBorderBottomWidth(double borderBottomWidth) {
		this.borderBottomWidth = borderBottomWidth;
	}

	public double getBorderLeftWidth() {
		return borderLeftWidth;
	}

	public void setBorderLeftWidth(double borderLeftWidth) {
		this.borderLeftWidth = borderLeftWidth;
	}
	
	
}
