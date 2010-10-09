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

/**
 * 
 * @author ggeorg
 */
public class Container extends HasFeaturesImpl {

	public Container(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	@Override
	public int getPrefWidth() {
		int width = 0;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			int fw = f.getWidth().$();
			if (fw == Integer.MIN_VALUE) {
				fw = f.getPrefWidth();
			}
			width = Math.max(width, fw);
		}
		return width;
	}

	@Override
	public int getPrefHeight() {
		int height = 0;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			int fh = f.getHeight().$();
			if (fh == Integer.MIN_VALUE) {
				fh = f.getPrefHeight();
			}
			height = Math.max(height, fh);
		}
		return height;
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

		int x = getX().$();
		int y = getY().$();
		int width = getWidth().$();
		int height = getHeight().$();

		if (width == Integer.MIN_VALUE) {
			width = getPrefWidth();
		}

		if (height == Integer.MIN_VALUE) {
			height = getPrefHeight();
		}

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			f.getWidth().$(width);
			f.getHeight().$(height);

			final int dx = x - f.getX().$();
			final int dy = y - f.getY().$();

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);
		}

		paintDone();
	}

}
