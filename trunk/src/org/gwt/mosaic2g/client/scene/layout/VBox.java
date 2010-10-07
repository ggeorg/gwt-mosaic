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
package org.gwt.mosaic2g.client.scene.layout;

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.Show;

/**
 * 
 * @author ggeorg
 */
public class VBox extends AbstractLayout {
	
	private int count;

	private int lastX, lastY, lastWidth, lastHeight;
	private int lastFlexSum, lastFlexHeight, lastMaxFHeight, lastPrefHeight;

	public VBox(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}
	
	public VBox(Show show, Property<Integer> x, Property<Integer> y) {
		this(show, x, y, Property.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
	}
	
	public VBox(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show, x, y, width, height);
	}

	private int spacing = 8;

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
		markAsChanged();
	}
	
	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			lastX = lastY = OFFSCREEN;
			lastWidth = lastHeight = Integer.MIN_VALUE;
			lastFlexSum = lastFlexHeight = lastMaxFHeight = lastPrefHeight = 0;
		}
	}
	
}
