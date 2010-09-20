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

import org.gwt.mosaic2g.client.util.Rectangle;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ggeorg
 */
public abstract class Control extends Feature implements HasScalingModel {
	private static final String DEFAULT_BORDER = "0px none";

	private int x, y, width, height;
	private InterpolatedModel scalingModel;
	private boolean managedSM;

	private Rectangle scaledBounds;

	private String border = DEFAULT_BORDER;
	private boolean borderChanged;

	private String styleName, newStyleName;
	private boolean styleNameChanged;

	private Widget widget = null;

	public Control(Show show, int x, int y, int width, int height) {
		super(show);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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

	public Rectangle getScaledBounds() {
		return scaledBounds;
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
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		markAsChanged();
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
		this.borderChanged = true;
		markAsChanged();
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.newStyleName = styleName;
		this.styleNameChanged = true;
		markAsChanged();
	}

	@Override
	protected void setSetupMode(boolean mode) {
		if (mode) {
			widget = createWidget();
		} else {
			widget = null;
		}
	}

	protected abstract Widget createWidget();

	protected void updateWidget(Widget w, boolean init) {
		final Style widgetStyle = w.getElement().getStyle();
		if (init || borderChanged) {
			widgetStyle.setProperty("border", border);
			borderChanged = false;
		}
		if ((init || styleNameChanged) && newStyleName != null) {
			if (styleName != null) {
				w.removeStyleName(styleName);
			}
			w.addStyleName(styleName = newStyleName);
			newStyleName = null;
			styleNameChanged = false;
		}
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			if (scalingModel != null) {
				if (!managedSM) {
					scalingModel.activate();
				}
				if (scaledBounds == null) {
					scaledBounds = new Rectangle();
				} else {
					scaledBounds.reshape(0, 0, 0, 0);
				}
			}
			markAsChanged();
		} else {
			widget.removeFromParent();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		if (scalingModel != null && !managedSM) {
			scalingModel.nextFrame(scene);
		}
		if (scalingModel != null && width != Integer.MIN_VALUE
				&& height != Integer.MIN_VALUE
				&& scalingModel.scaleBounds(x, y, width, height, scaledBounds)) {
			if (scaledBounds.width < 0) {
				scaledBounds.width = -scaledBounds.width;
				scaledBounds.x -= scaledBounds.width;
			}
			if (scaledBounds.height < 0) {
				scaledBounds.height = -scaledBounds.height;
				scaledBounds.y -= scaledBounds.height;
			}

			markAsChanged();
		}

		return changed;
	}

	@Override
	public final void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		updateWidget(widget, false);
		if (scalingModel != null && width != Integer.MIN_VALUE
				&& height != Integer.MIN_VALUE) {
			scene.renderWidget(widget, scaledBounds.x, scaledBounds.y,
					scaledBounds.width, scaledBounds.height);
		} else {
			scene.renderWidget(widget, x, y, width, height);
		}

		changed = false;
	}

}
