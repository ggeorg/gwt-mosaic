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

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.Feature;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Show;

/**
 * The {@code Stack} container arranges its child nodes in a back-to-front
 * stack. The z-order of the child nodes is defined by the order the child nodes
 * are added to the {@code Stack}.
 * <p>
 * By default, the preferred size of a stack will be the size required the
 * largest preferred width and height of its managed child nodes.
 * <p>
 * {@link Resizable} child nodes will be resized to fill the width/height of the
 * {@code Stack} container unless either of the of the dimensions are greater
 * than the resizable's maximum width/height, in which case the node's maximum
 * dimension will be used. Nodes which cannot be resized to fill the stack
 * (either because they are not {@link Resizable} or their size prevents it)
 * will be aligned within the space according to the TODO, which both default to
 * {@code CENTER}.
 * 
 * @author ggeorg
 */
public class Stack extends AbstractLayout {

	private int lastX, lastY, lastWidth, lastHeight;

	public Stack(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public Stack(Show show, Property<Integer> x, Property<Integer> y) {
		super(show, x, y, Property.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
		setHorizontalAlignment(ALIGN_CENTER);
		setVerticalAlignment(ALIGN_MIDDLE);
	}

	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			lastX = lastY = OFFSCREEN;
			lastWidth = lastHeight = Integer.MIN_VALUE;
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		changed = (changed || super.nextFrame(scene));
		if (changed) {
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

			final HorizontalAlignmentConstant horzAlign = getHorizontalAlignment();
			final VerticalAlignmentConstant vertAlign = getVerticalAlignment();

			if (horzAlign == Stack.ALIGN_RIGHT) {
				x += width;
			} else if (horzAlign == Stack.ALIGN_CENTER) {
				x += (width / 2);
			}/* else { default } */

			if (vertAlign == Stack.ALIGN_BOTTOM) {
				y += height;
			} else if (vertAlign == Stack.ALIGN_MIDDLE) {
				y += (height / 2);
			}/* else { default } */

			if (lastX != x || lastY != y || lastWidth != width
					|| lastHeight != height) {
				lastX = x;
				lastY = y;
				lastWidth = width;
				lastHeight = height;
			}
		}
		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		final HorizontalAlignmentConstant horzAlign = getHorizontalAlignment();
		final VerticalAlignmentConstant vertAlign = getVerticalAlignment();

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			int fw = f.getWidth().$();
			int fh = f.getHeight().$();

			if (fw == Integer.MIN_VALUE) {
				fw = f.getPrefWidth();
			} else {
				f.getWidth().$(fw = lastWidth);
			}
			if (fw == Integer.MIN_VALUE) {
				fh = f.getPrefHeight();
			} else {
				f.getHeight().$(fh = lastHeight);
			}

			int dx = (lastX - f.getX().$());
			int dy = (lastY - f.getY().$());

			if (horzAlign == Stack.ALIGN_RIGHT) {
				dx -= fw;
			} else if (horzAlign == Stack.ALIGN_CENTER) {
				dx -= (fw / 2);
			} /* else { default } */

			if (vertAlign == Stack.ALIGN_BOTTOM) {
				dy -= fh;
			} else if (vertAlign == Stack.ALIGN_MIDDLE) {
				dy -= (fh / 2);
			} /* else { default } */

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);
		}

		paintDone();
	}

}
