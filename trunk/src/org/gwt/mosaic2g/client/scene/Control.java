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

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.util.Rectangle;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ggeorg
 */
public abstract class Control extends Feature implements HasScalingModel,
		HasAllMouseHandlers, HasClickHandlers, HasDoubleClickHandlers {
	private static final String DEFAULT_BORDER = "0px none";
	private static final String DEFAULT_BACKGROUND = "none";

	private InterpolatedModel scalingModel;
	private boolean managedSM;

	private Rectangle scaledBounds;

	private Property<String> border;
	private boolean borderChanged;

	private Property<String> background;
	private boolean backgroundChanged;

	private String lastStyleName;
	private Property<String> styleName;
	private boolean styleNameChanged;

	private Widget widget = null;

	public Control(Show show, int x, int y) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(Integer.MIN_VALUE), Property
				.valueOf(Integer.MIN_VALUE));
	}

	public Control(Show show, int x, int y, int width, int height) {
		this(show, Property.valueOf(x), Property.valueOf(y), Property
				.valueOf(width), Property.valueOf(height));
	}

	public Control(Show show, int x, int y, Property<Integer> width,
			Property<Integer> height) {
		this(show, Property.valueOf(x), Property.valueOf(y), width, height);
	}

	public Control(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	@Override
	public int getPrefWidth() {
		if (widget == null || !widget.isAttached()) {
			return super.getWidth().$();
		} else {
			return widget.getElement().getClientWidth();
		}
	}

	@Override
	public int getPrefHeight() {
		if (widget == null || !widget.isAttached()) {
			return super.getHeight().$();
		} else {
			return widget.getElement().getClientHeight();
		}
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
		if (isSetup()) {
			getShow().runCommand(new Command() {
				public void execute() {
					setActivateMode(true);
				}
			});
		}
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
			if (lastStyleName != null) {
				w.removeStyleName(lastStyleName);
			}
			lastStyleName = getStyleName().$();
			if (lastStyleName != null) {
				w.addStyleName(lastStyleName);
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
			resized = true;
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
		} else if (resized) {
			scene.renderWidget(widget, getX().$(), getY().$(), getWidth().$(),
					getHeight().$());
			resized = false;
		} else {
			scene.renderWidget(widget, getX().$(), getY().$(),
					Integer.MIN_VALUE, Integer.MIN_VALUE);
		}

		paintDone();
	}

	// ---------------------------------------------------------------------
	// Mouse Handlers
	// ---------------------------------------------------------------------

	private void checkSetup() {
		if (!isSetup()) {
			throw new IllegalStateException(
					"Can't add a handler to a control that is not setup");
		}
	}

	public void fireEvent(GwtEvent<?> event) {
		checkSetup();
		widget.fireEvent(event);
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseDownHandlers) {
			return ((HasMouseDownHandlers) widget).addMouseDownHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseDownHandlers.class.getName()
						+ " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseUpHandlers) {
			return ((HasMouseUpHandlers) widget).addMouseUpHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseUpHandlers.class.getName() + " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseOutHandlers) {
			return ((HasMouseOutHandlers) widget).addMouseOutHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseOutHandlers.class.getName() + " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseOverHandlers) {
			return ((HasMouseOverHandlers) widget).addMouseOverHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseOverHandlers.class.getName()
						+ " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseMoveHandlers) {
			return ((HasMouseMoveHandlers) widget).addMouseMoveHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseMoveHandlers.class.getName()
						+ " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		checkSetup();
		if (widget instanceof HasMouseWheelHandlers) {
			return ((HasMouseWheelHandlers) widget)
					.addMouseWheelHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasMouseWheelHandlers.class.getName()
						+ " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		checkSetup();
		if (widget instanceof HasDoubleClickHandlers) {
			return ((HasDoubleClickHandlers) widget)
					.addDoubleClickHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasDoubleClickHandlers.class.getName()
						+ " is not implemented by "
						+ widget.getClass().getName());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		checkSetup();
		if (widget instanceof HasClickHandlers) {
			return ((HasClickHandlers) widget).addClickHandler(handler);
		}
		throw new UnsupportedOperationException(
				HasClickHandlers.class.getName() + " is not implemented by "
						+ widget.getClass().getName());
	}

}
