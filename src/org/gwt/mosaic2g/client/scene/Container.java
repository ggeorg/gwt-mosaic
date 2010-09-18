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
import org.gwt.mosaic2g.client.scene.layout.Resizable;
import org.gwt.mosaic2g.client.util.Rectangle;

/**
 * 
 * @author ggeorg
 */
public class Container extends Group implements Resizable {

	private InterpolatedModel scalingModel;
	private boolean managedSM;

	public Container(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public InterpolatedModel getScalingModel() {
		return scalingModel;
	}

	public void setScalingModel(InterpolatedModel scalingModel) {
		setScalingModel(scalingModel, false);
	}

	public void setScalingModel(InterpolatedModel scalingModel, boolean managed) {
		this.scalingModel = scalingModel;
		this.managedSM = managed;
	}

	public int getPrefWidth() {
		return super.getWidth().$();
	}

	public int getPrefHeight() {
		return super.getHeight().$();
	}

	@Override
	protected void setSetupMode(boolean mode) {
		if (scalingModel != null) {
			if (mode) {
				Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					if (f instanceof HasScalingModel) {
						((HasScalingModel) f).setScalingModel(scalingModel,
								true);
					}
				}
			} else {
				Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					if (f instanceof HasScalingModel) {
						((HasScalingModel) f).setScalingModel(null);
					}
				}
			}
		}
		super.setSetupMode(mode);
	}

	@Override
	protected void setActivateMode(boolean mode) {
		super.setActivateMode(mode);
		if (mode) {
			if (scalingModel != null && !managedSM) {
				scalingModel.activate();
			}
			markAsChanged();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		if (scalingModel != null && !managedSM) {
			scalingModel.nextFrame(scene);
		}
		return super.nextFrame(scene);
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

		changed = false;
	}
}
