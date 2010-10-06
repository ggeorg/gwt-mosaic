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

import com.google.gwt.user.client.ui.HasAlignment;

/**
 * The {@code HBox} container lays out its managed content nodes in a single
 * horizontal row. Its content is layed out from left to right in the order of
 * content sequence, spaced by {@code spacing}. The vertical alignement of each
 * node is controlled by {@code nodeVpos}, which defaults to
 * {@link HasAlignment#ALIGN_TOP}
 * 
 * @author ggeorg
 */
public class HBox extends AbstractLayout {

	private int count;

	private int lastX, lastY, lastWidth, lastHeight;
	private int lastFlexSum, lastFlexWidth, lastMaxFWidth, lastPrefWidth;

	public HBox(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public HBox(Show show, Property<Integer> x, Property<Integer> y) {
		this(show, x, y, Property.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
	}

	public HBox(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show, x, y, width, height);
		setAutoHorizontalAlignment(ALIGN_CONTENT_START);
	}

	@Override
	public int getPrefWidth() {
		count = -1;

		int result = 0;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			if (f.instanceOfHasPrefSize()) {
				result += f.getPrefWidth();
			} else {
				int val = f.getWidth().$();
				if (val == Integer.MIN_VALUE) {
					continue;
				}
				result += val;
			}
			++count;
		}

		if (count > 0) {
			result += spacing * count;
		}

		return result;
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
			lastFlexSum = lastFlexWidth = lastMaxFWidth = lastPrefWidth = 0;
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

			// if(lastX != x || lastY != y || lastWidth != width || lastHeight
			// != height) {
			lastX = x;
			lastY = y;
			lastWidth = width;
			lastHeight = height;
			// }

			int flexSum = 0;
			int maxWidth = 0;
			int prefWidth = 0;
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				final Feature f = it.next();
				final int fflex = f.getFlex();
				if (fflex > 0) {
					flexSum += fflex;
				} else {
					int fw = f.getWidth().$();
					if (fw == Integer.MIN_VALUE) {
						fw = f.getPrefWidth();
					}
					if (fw > maxWidth) {
						maxWidth = fw;
					}
					prefWidth += fw;
				}
			}
			
			int size = getParts().size();

			if (prefWidth > 0) {
				if(isEqualSize()) {
					prefWidth = size * maxWidth;
				}
				prefWidth += (spacing * (size - 1));
			}

			lastFlexSum = flexSum;
			lastFlexWidth = lastWidth - prefWidth;
			lastMaxFWidth = maxWidth;
			lastPrefWidth = prefWidth;
		}
		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		int startX, startY;

		if (lastFlexSum == 0) {
			HorizontalAlignmentConstant pack = getHorizontalAlignment();
			if (pack == HBox.ALIGN_RIGHT) {
				startX = lastX + (lastWidth - lastPrefWidth);
			} else if (pack == HBox.ALIGN_CENTER) {
				startX = lastX + (lastWidth - lastPrefWidth) / 2;
			} else {
				startX = lastX;
			}
		} else {
			startX = lastX;
		}
		
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			int fw;

			int fflex = f.getFlex();
			if (fflex > 0) {
				fw = (int) (lastFlexWidth * ((double) fflex / (double) lastFlexSum));
				f.getWidth().$(fw);
			} else if (isEqualSize()) {
				fw = lastMaxFWidth;
				f.getWidth().$(fw);
			} else {
				fw = f.getWidth().$();
				if (fw == Integer.MIN_VALUE) {
					fw = f.getPrefWidth();
				}
			}

			VerticalAlignmentConstant align = getVerticalAlignment();
			if (align == null) {
				startY = lastY;
				f.getHeight().$(lastHeight);
			} else if (align == HBox.ALIGN_TOP) {
				startY = lastY;
			} else {
				int fh = f.getHeight().$();
				if (fh == Integer.MIN_VALUE) {
					fh = f.getPrefHeight();
				}
				if (align == HBox.ALIGN_MIDDLE) {
					startY = lastY + (lastHeight - fh) / 2;
				} else {
					startY = lastY + (lastHeight - fh);
				}
			}

			int dx = startX - f.getX().$();
			int dy = startY - f.getY().$();

			scene.translate(dx, dy);
			f.paintFrame(scene);
			scene.translate(-dx, -dy);

			startX += fw + spacing;
		}

		paintDone();
	}

}
