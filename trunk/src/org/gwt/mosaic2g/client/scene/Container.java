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
import org.gwt.mosaic2g.client.scene.layout.Resizable;
import org.gwt.mosaic2g.client.util.Rectangle;

/**
 * 
 * @author ggeorg
 */
public class Container extends HasFeaturesImpl implements HasPrefSize, Resizable {

	public Container(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public int getPrefWidth() {
		throw new UnsupportedOperationException("TODO");
	}

	public int getPrefHeight() {
		throw new UnsupportedOperationException("TODO");
	}
	
	@Override
	public boolean nextFrame(Scene scene) {
		Iterator<Feature> it = iterator();
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

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		final Rectangle bounds = new Rectangle(getX().$(), getY().$(),
				getWidth().$(), getHeight().$());

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			final int fw = f.getWidth().$();
			final int fh = f.getHeight().$();
			if (fw == Integer.MIN_VALUE || fh == Integer.MIN_VALUE) {
				continue;
			}

			final int dx = bounds.x - f.getX().$();
			final int dy = bounds.y - f.getY().$();

			f.getWidth().$(bounds.width);
			f.getHeight().$(bounds.height);

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);
		}

		paintDone();
	}
	
}
