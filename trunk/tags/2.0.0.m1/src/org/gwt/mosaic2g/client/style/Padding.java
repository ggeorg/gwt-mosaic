/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic2g.client.style;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

public class Padding {
	private static final String SEPARATOR = " ";

	private Unit unit;

	private double paddingTop;
	private double paddingRight;
	private double paddingBottom;
	private double paddingLeft;

	public Padding() {
		unit = Unit.PX;
		paddingTop = 0.0;
		paddingRight = 0.0;
		paddingBottom = 0.0;
		paddingLeft = 0.0;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getMarginTop() {
		return paddingTop;
	}

	public void setMarginTop(double paddingTop) {
		this.paddingTop = paddingTop;
	}

	public double getMarginRight() {
		return paddingRight;
	}

	public void setMarginRight(double paddingRight) {
		this.paddingRight = paddingRight;
	}

	public double getMarginBottom() {
		return paddingBottom;
	}

	public void setMarginBottom(double paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public double getMarginLeft() {
		return paddingLeft;
	}

	public void setMarginLeft(double paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	
	public void applyTo(Element elem) {
		final Style style = elem.getStyle();
		style.setPaddingTop(paddingTop, unit);
		style.setPaddingRight(paddingRight, unit);
		style.setPaddingBottom(paddingBottom, unit);
		style.setPaddingLeft(paddingLeft, unit);
	}
	
	public void readFrom(Element elem) {
		unit = Unit.PX;
		paddingTop = ComputedStyle.getPaddingTop(elem);
		paddingRight = ComputedStyle.getPaddingRight(elem);
		paddingBottom = ComputedStyle.getPaddingBottom(elem);
		paddingLeft = ComputedStyle.getPaddingLeft(elem);
	}

	@Override
	public String toString() {
		final String u = unit.getType();
		StringBuilder sb = new StringBuilder();
		sb.append(paddingTop).append(u).append(SEPARATOR).append(paddingRight)
				.append(u).append(SEPARATOR).append(paddingBottom).append(u)
				.append(SEPARATOR).append(paddingLeft).append(u);
		return sb.toString();
	}
}
