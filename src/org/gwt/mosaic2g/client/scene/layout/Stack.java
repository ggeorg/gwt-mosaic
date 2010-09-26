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
import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Feature;
import org.gwt.mosaic2g.client.scene.Group;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Show;

import com.google.gwt.i18n.client.BidiUtils;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

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
public class Stack extends Group implements HasAutoHorizontalAlignment,
		HasVerticalAlignment {

	private HorizontalAlignmentConstant horzAlign = HasHorizontalAlignment.ALIGN_CENTER;

	private VerticalAlignmentConstant vertAlign = HasVerticalAlignment.ALIGN_MIDDLE;

	private AutoHorizontalAlignmentConstant autoHorizontalAlignment;
	private boolean autoHorzAlignChanged;

	private int lastX, lastY, lastWidth, lastHeight;

	public Stack(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public Stack(Show show, Property<Integer> x, Property<Integer> y) {
		super(show);
		setX(x);
		setY(y);
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
		if (autoHorzAlignChanged) {
			HorizontalAlignmentConstant align;
			if (autoHorizontalAlignment == null) {
				align = null;
			} else if (autoHorizontalAlignment instanceof HorizontalAlignmentConstant) {
				align = (HorizontalAlignmentConstant) autoHorizontalAlignment;
			} else {
				/*
				 * autoHorizontalAlignment is a truly automatic policy, i.e.
				 * either ALIGN_CONTENT_START or ALIGN_CONTENT_END
				 */
				Direction sceneDir = BidiUtils.getDirectionOnElement(scene
						.getElement());
				align = (autoHorizontalAlignment == ALIGN_CONTENT_START) ? HorizontalAlignmentConstant
						.startOf(sceneDir) : HorizontalAlignmentConstant
						.endOf(sceneDir);
			}
			if (align != horzAlign) {
				horzAlign = align;
			}
		}
		if (changed) {
			int x = getX().$();
			int y = getY().$();
			int width = getWidth().$();
			int height = getHeight().$();

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

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			int w = f.getWidth().$();
			int h = f.getHeight().$();

			if (f instanceof Resizable) {
				f.getWidth().$(lastWidth);
				f.getHeight().$(lastHeight);
			}

			if (w == Integer.MIN_VALUE) {
				w = ((Control) f).getPrefWidth();// lastWidth;
			}
			if (h == Integer.MIN_VALUE) {
				h = ((Control) f).getPrefHeight();// lastHeight;
			}

			int dx = (lastX - f.getX().$());
			int dy = (lastY - f.getY().$());

			if (horzAlign == Stack.ALIGN_RIGHT) {
				dx -= w;
			} else if (horzAlign == Stack.ALIGN_CENTER) {
				dx -= (w / 2);
			} /* else { default } */

			if (vertAlign == Stack.ALIGN_BOTTOM) {
				dy -= h;
			} else if (vertAlign == Stack.ALIGN_MIDDLE) {
				dy -= (h / 2);
			} /* else { default } */

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);
		}

		changed = false;
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return horzAlign;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		if (this.horzAlign == align) {
			return;
		}
		this.horzAlign = align;
		markAsChanged();
	}

	public AutoHorizontalAlignmentConstant getAutoHorizontalAlignment() {
		return autoHorizontalAlignment;
	}

	public void setAutoHorizontalAlignment(
			AutoHorizontalAlignmentConstant autoHorizontalAlignment) {
		if (this.autoHorizontalAlignment == autoHorizontalAlignment) {
			return;
		}
		this.autoHorizontalAlignment = autoHorizontalAlignment;
		this.autoHorzAlignChanged = true;
		markAsChanged();
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return vertAlign;
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		if (this.vertAlign == align) {
			return;
		}
		this.vertAlign = align;
		markAsChanged();
	}

}
