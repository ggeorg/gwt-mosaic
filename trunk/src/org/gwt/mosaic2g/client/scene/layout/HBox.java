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

import com.google.gwt.event.logical.shared.ValueChangeEvent;

/**
 * The {@code HBox} container lays out its managed content nodes in a single
 * horizontal row.
 * 
 * @author ggeorg
 */
public class HBox extends AbstractLayout {

	private int lastX, lastY, lastWidth, lastHeight;
	private int lastFlexSum, lastFlexWidth, lastMaxFWidth, lastMaxFHeight,
			lastPrefWidth;

	private int spacing = 8;

	public HBox(Show show) {
		this(show, Property.valueOf(0), Property.valueOf(0));
	}

	public HBox(Show show, int x, int y) {
		this(show, Property.valueOf(x), Property.valueOf(y));
	}

	public HBox(Show show, int x, int y, int width, int height) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(width), Property.valueOf(height));
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
			lastFlexSum = lastFlexWidth = lastMaxFWidth = lastMaxFHeight = lastPrefWidth = 0;
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
				width = 0;
				final Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					final int fflex = f.getFlex();
					if (fflex > 0) {
						continue;
					} else {
						width += f.getWidth().$() + spacing;
					}
				}
				getWidth().$(width = Math.max(0, width - spacing));
				ValueChangeEvent.fire(getWidth(), getWidth().$());
			}

			if (height == Integer.MIN_VALUE) {
				height = 0;

				Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					height = Math.max(height, f.getHeight().$());
				}
				getHeight().$(height);
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
			int maxHeight = 0;
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
						fw = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
								.getPrefWidth().$() : 0;
					}
					if (fw > maxWidth) {
						maxWidth = fw;
					}
					prefWidth += fw;
				}
				maxHeight = Math.max(maxHeight, f.getHeight().$());
			}

			int size = getParts().size();

			if (prefWidth > 0) {
				if (isEqualSize()) {
					prefWidth = size * maxWidth;
				}
				prefWidth += (spacing * (size - 1));
			}

			if (lastMaxFWidth == Integer.MIN_VALUE) {
				getWidth().$(lastMaxFWidth = maxWidth);
				ValueChangeEvent.fire(getWidth(), getWidth().$());
			}
			if (lastMaxFHeight == Integer.MIN_VALUE) {
				getHeight().$(lastMaxFHeight = maxHeight);
				ValueChangeEvent.fire(getHeight(), getHeight().$());
			}

			lastFlexSum = flexSum;
			lastFlexWidth = lastWidth - prefWidth;
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
					if (fw == Integer.MIN_VALUE) {
						fw = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
								.getPrefWidth().$() : 0;
					}
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
					fh = (f instanceof HasPrefSize) ? ((HasPrefSize) f)
							.getPrefHeight().$() : 0;
				}
				if (align == HBox.ALIGN_MIDDLE) {
					startY = lastY + (lastHeight - fh) / 2;
				} else {
					startY = lastY + (lastHeight - fh);
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

			startX += fw + spacing;
		}

		paintDone();
	}

}
