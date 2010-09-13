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

import org.gwt.mosaic2g.client.binding.Property;
import org.gwt.mosaic2g.client.util.Rectangle;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ggeorg
 */
public abstract class Control extends Feature implements HasScalingModel {
	private static final String DEFAULT_BORDER = "0px none";
	private static final String DEFAULT_BACKGROUND = "none";

	private InterpolatedModel scalingModel;
	private boolean managedSM;

	private Rectangle scaledBounds;

	private Property<String> border;
	private boolean borderChanged;

	private Property<String> background;
	private boolean backgroundChanged;

	private String newStyleName;
	private Property<String> styleName;
	private boolean styleNameChanged;

	private Widget widget = null;

	public Control(Show show, int x, int y, int width, int height) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(width), Property.valueOf(height));
	}

	public Control(Show show, Property<Integer> x, Property<Integer> y,
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

	public Rectangle getScaledBounds() {
		return scaledBounds;
	}

	public Property<String> getBorder() {
		if (border == null) {
			border = Property.valueOf(DEFAULT_BORDER);
			border.addValueChangeHandler(new ValueChangeHandler<String>() {
				public void onValueChange(ValueChangeEvent<String> event) {
					borderChanged = true;
					markAsChanged();
				}
			});
		}
		return border;
	}

	public void setBorder(String value) {
		getBorder().$(value);
	}

	public Property<String> getBackground() {
		if (background == null) {
			background = Property.valueOf(DEFAULT_BACKGROUND);
			background.addValueChangeHandler(new ValueChangeHandler<String>() {
				public void onValueChange(ValueChangeEvent<String> event) {
					backgroundChanged = true;
					markAsChanged();
				}
			});
		}
		return background;
	}

	public void setBackground(String value) {
		getBackground().$(value);
	}

	public Property<String> getStyleName() {
		if (styleName == null) {
			styleName = new Property<String>();
			styleName.addValueChangeHandler(new ValueChangeHandler<String>() {
				public void onValueChange(ValueChangeEvent<String> event) {
					newStyleName = event.getValue();
					styleNameChanged = true;
					markAsChanged();
				}
			});
		}
		return styleName;
	}

	public void setStyleName(String styleName) {
		getStyleName().$(styleName);
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
			widgetStyle.setProperty("border", getBorder().$());
			borderChanged = false;
		}
		if (init || backgroundChanged) {
			widgetStyle.setProperty("background", getBackground().$());
			backgroundChanged = false;
		}
		if (init || styleNameChanged) {
			if (newStyleName != null) {
				w.removeStyleName(newStyleName);
				newStyleName = null;
			}
			if (getStyleName().$() != null) {
				w.addStyleName(getStyleName().$());
			}
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
		if (scalingModel != null
				&& getWidth().$() != Integer.MIN_VALUE
				&& getHeight().$() != Integer.MIN_VALUE
				&& scalingModel.scaleBounds(getX().$(), getY().$(), getWidth()
						.$(), getHeight().$(), scaledBounds)) {
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
		if (scalingModel != null && getWidth().$() != Integer.MIN_VALUE
				&& getHeight().$() != Integer.MIN_VALUE) {
			scene.renderWidget(widget, scaledBounds.x, scaledBounds.y,
					scaledBounds.width, scaledBounds.height);
		} else {
			scene.renderWidget(widget, getX().$(), getY().$(), getWidth().$(),
					getHeight().$());
		}

		changed = false;
	}

}
