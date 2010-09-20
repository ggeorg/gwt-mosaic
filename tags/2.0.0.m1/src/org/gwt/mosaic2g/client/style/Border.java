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
