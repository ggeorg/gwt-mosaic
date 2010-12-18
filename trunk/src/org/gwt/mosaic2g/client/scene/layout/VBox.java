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
 * The {@code VBox} container lays out its managed content nodes in a single
 * vertical column.
 * 
 * @author ggeorg
 */
public class VBox extends AbstractLayout {

	private int lastX, lastY, lastWidth, lastHeight;
	private int lastFlexSum, lastFlexHeight, lastMaxFHeight, lastPrefHeight;

	private int spacing = 8;

	public VBox(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public VBox(Show show, int x, int y) {
		this(show, Property.valueOf(x), Property.valueOf(y));
	}

	public VBox(Show show, int x, int y, int width, int height) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(width), Property.valueOf(height));
	}

	public VBox(Show show, Property<Integer> x, Property<Integer> y) {
		this(show, x, y, Property.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
	}

	public VBox(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show, x, y, width, height);
		setVerticalAlignment(ALIGN_TOP);
	}

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

	@Override
	public boolean nextFrame(Scene scene) {
		changed = (changed || super.nextFrame(scene));
		if (changed) {
			int x = getX().$();
			int y = getY().$();
			int width = getWidth().$();
			int height = getHeight().$();
			int size = getParts().size();

			// --------

			int flexSum = 0;
			int maxHeight = 0;
			int prefHeight = 0;
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				final Feature f = it.next();
				final int fflex = f.getFlex();
				if (fflex > 0) {
					flexSum += fflex;
				} else {
					int fh = f.getHeight().$();
					if (fh == Integer.MIN_VALUE) {
						fh = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
								.getPrefHeight().$() : 0;
					}
					if (fh > maxHeight) {
						maxHeight = fh;
					}
					prefHeight += fh;
				}
			}

			if (isEqualSize()) {
				prefHeight = size * maxHeight;
			}
			prefHeight += (spacing * (size - 1));

			// --------

			if (width == Integer.MIN_VALUE) {
				width = 0;
				it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					int fw = f.getWidth().$();
					if (fw == Integer.MIN_VALUE) {
						fw = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
								.getPrefWidth().$() : 0;
					}
					width = Math.max(width, fw);
				}
			}

			if (height == Integer.MIN_VALUE) {
				height = prefHeight;
			}

			// --------

			if (lastWidth != width) {
				getPrefWidth().$(width);
			}

			if (lastHeight != height) {
				getPrefHeight().$(height);
			}

			// --------

			lastX = x;
			lastY = y;
			lastWidth = width;
			lastHeight = height;
			lastFlexSum = flexSum;
			lastFlexHeight = height - prefHeight;
			lastPrefHeight = prefHeight;
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
			VerticalAlignmentConstant pack = getVerticalAlignment();
			if (pack == VBox.ALIGN_BOTTOM) {
				startY = lastY + (lastHeight - lastPrefHeight);
			} else if (pack == VBox.ALIGN_MIDDLE) {
				startY = lastY + (lastHeight - lastPrefHeight) / 2;
			} else {
				startY = lastY;
			}
		} else {
			startY = lastY;
		}

		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();

			int fh;

			int fflex = f.getFlex();
			if (fflex > 0) {
				fh = (int) (lastFlexHeight * ((double) fflex / (double) lastFlexSum));
				f.getHeight().$(fh);
			} else if (isEqualSize()) {
				fh = lastMaxFHeight;
				f.getWidth().$(fh);
			} else {
				fh = f.getHeight().$();
				if (fh == Integer.MIN_VALUE) {
					fh = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
							.getPrefHeight().$() : 0;
				}
			}

			HorizontalAlignmentConstant align = getHorizontalAlignment();
			if (align == null) {
				startX = lastX;
				f.getWidth().$(lastWidth);
			} else if (align == VBox.ALIGN_LEFT) {
				startX = lastX;
			} else {
				int fw = f.getWidth().$();
				if (fw == Integer.MIN_VALUE) {
					fw = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
							.getPrefWidth().$() : 0;
				}
				if (align == VBox.ALIGN_CENTER) {
					startX = lastX + (lastWidth - fw) / 2;
				} else {
					startX = lastX + (lastWidth - fw);
				}
			}

			// int dx = startX - f.getX().$();
			// int dy = startY - f.getY().$();

			f.getX().$(startX);
			f.getY().$(startY);

			f.markAsChanged();

			// scene.translate(dx, dy);
			f.paintFrame(scene);
			// scene.translate(-dx, -dy);

			startY += fh + spacing;
		}

		paintDone();
	}

}
