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

import java.util.Iterator;

import org.gwt.mosaic2g.client.scene.Feature;
import org.gwt.mosaic2g.client.scene.Group;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Show;

/**
 * 
 * @author ggeorg
 */
public class Stack extends Group {

	private int x, y, lastX, lastY;

	public Stack(Show show) {
		this(show, 0, 0);
	}

	public Stack(Show show, int x, int y) {
		super(show);
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			lastX = Integer.MAX_VALUE;
			lastY = Integer.MAX_VALUE;
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		changed = super.nextFrame(scene);
		int X = x + getWidth() / 2;
		int Y = y + getHeight() / 2;
		if (lastX != X || lastY != Y) {
			lastX = X;
			lastY = Y;
			markAsChanged();
		}
		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			final int w = f.getWidth();
			final int h = f.getHeight();

			if (w == Integer.MIN_VALUE || h == Integer.MIN_VALUE) {
				continue;
			}
			int dx = (lastX - f.getX()) - (w / 2);
			int dy = (lastY - f.getY()) - (h / 2);

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);
		}

		changed = false;
	}

}
