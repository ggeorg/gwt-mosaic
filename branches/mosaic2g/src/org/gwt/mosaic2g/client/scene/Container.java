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
package org.gwt.mosaic2g.client.scene;

import java.util.Iterator;

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.layout.HasPrefSize;

/**
 * 
 * @author ggeorg
 */
public abstract class Container extends HasFeaturesImpl implements HasPrefSize {

	public Container(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	private Property<Integer> prefWidth = new Property<Integer>(0);

	public Property<Integer> getPrefWidth() {
		return prefWidth;
	}

	private Property<Integer> prefHeight = new Property<Integer>(0);

	public Property<Integer> getPrefHeight() {
		return prefHeight;
	}

	@Override
	public boolean nextFrame(Scene scene) {
		final Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			Feature f = it.next();
			if (f.nextFrame(scene)) {
				if (!changed) {
					changed = true;
				}
			}
		}
		return changed;
	}

}