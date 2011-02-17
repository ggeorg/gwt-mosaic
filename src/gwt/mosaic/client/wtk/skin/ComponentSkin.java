/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.ComponentKeyListener;
import gwt.mosaic.client.wtk.ComponentListener;
import gwt.mosaic.client.wtk.ComponentMouseButtonListener;
import gwt.mosaic.client.wtk.ComponentMouseListener;
import gwt.mosaic.client.wtk.ComponentMouseWheelListener;
import gwt.mosaic.client.wtk.ComponentStateListener;
import gwt.mosaic.client.wtk.Container;
import gwt.mosaic.client.wtk.Cursor;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.DragSource;
import gwt.mosaic.client.wtk.DropTarget;
import gwt.mosaic.client.wtk.Keyboard;
import gwt.mosaic.client.wtk.Keyboard.KeyCode;
import gwt.mosaic.client.wtk.Keyboard.Modifier;
import gwt.mosaic.client.wtk.MenuHandler;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.Skin;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public abstract class ComponentSkin implements Skin, ComponentListener,
		ComponentStateListener, ComponentMouseListener,
		ComponentMouseButtonListener, ComponentMouseWheelListener,
		ComponentKeyListener/* , ComponentTooltipListener */{
	private Component component = null;

	private int width = 0;
	private int height = 0;
	private boolean sizeChanged = false;

	// Managed by ComponentListener#locationChanged().
	private boolean locationChanged = false;

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.sizeChanged = true;
	}

	@Override
	public Dimensions getPreferredSize() {
		return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	}

	@Override
	public final int getBaseline() {
		return getBaseline(width, height);
	}

	@Override
	public int getBaseline(int width, int height) {
		return -1;
	}

	@Override
	public void install(Component component) {
		assert (this.component == null) : "Skin is already installed on a component.";

		component.getComponentListeners().add(this);
		// component.getComponentStateListeners().add(this);
		// component.getComponentMouseListeners().add(this);
		// component.getComponentMouseButtonListeners().add(this);
		// component.getComponentMouseWheelListeners().add(this);
		// component.getComponentKeyListeners().add(this);
		// component.getComponentTooltipListeners().add(this);

		this.component = component;
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public void layout() {
		System.out.println(getComponent().getClass().getName() + ", sizeChanged " + sizeChanged);

		if (locationChanged) {
			Widget w = asWidget();
			Element elem = w.getElement();
			DOM.setStyleAttribute(elem, "position", "absolute");
			DOM.setStyleAttribute(elem, "left", component.getX() + "px");
			DOM.setStyleAttribute(elem, "top", component.getY() + "px");
		}

		if (sizeChanged) {
			Widget w = asWidget();
//			Element elem = w.getElement();
			if (width >= 0) {
//				width -= ComputedStyle.getPaddingLeft(elem);
//				width -= ComputedStyle.getPaddingRight(elem);
				w.setWidth(width + Unit.PX.toString());
				// NOTE: don't use widgetStyle.setWidth(width, Unit.PX) !!!
			}
			if (height >= 0) {
//				height -= ComputedStyle.getPaddingTop(elem);
//				height -= ComputedStyle.getPaddingBottom(elem);
				w.setHeight(height + Unit.PX.toString());
				// NOTE: don't use widgetStyle.setHeight(height, Unit.PX) !!!
			}
			sizeChanged = false;
		}
	}

	/**
	 * By default, skins are focusable.
	 */
	@Override
	public boolean isFocusable() {
		return true;
	}

	// Component events
	@Override
	public void parentChanged(Component component, Container previousParent) {
		// No-op
	}

	@Override
	public void sizeChanged(Component component, int previousWidth,
			int previousHeight) {
		// No-op
	}

	@Override
	public void preferredSizeChanged(Component component,
			int previousPreferredWidth, int previousPreferredHeight) {
		// No-op
	}

	@Override
	public void widthLimitsChanged(Component component,
			int previousMinimumWidth, int previousMaximumWidth) {
		// No-op
	}

	@Override
	public void heightLimitsChanged(Component component,
			int previousMinimumHeight, int previousMaximumHeight) {
		// No-op
	}

	@Override
	public void locationChanged(Component component, int previousX,
			int previousY) {
		locationChanged = true;
	}

	@Override
	public void visibleChanged(Component component) {
		// No-op
	}

	@Override
	public void cursorChanged(Component component, Cursor previousCursor) {
		// No-op
	}

	@Override
	public void tooltipTextChanged(Component component,
			String previousTooltipText) {
	}

	@Override
	public void dragSourceChanged(Component component,
			DragSource previousDragSource) {
		// No-op
	}

	@Override
	public void dropTargetChanged(Component component,
			DropTarget previousDropTarget) {
		// No-op
	}

	@Override
	public void menuHandlerChanged(Component component,
			MenuHandler previousMenuHandler) {
		// No-op
	}

	@Override
	public void nameChanged(Component component, String previousName) {
		// No-op
	}

	// Component state events
	@Override
	public void enabledChanged(Component component) {
		// No-op
	}

	@Override
	public void focusedChanged(Component component, Component obverseComponent) {
		// No-op
	}

	// Component mouse events
	@Override
	public boolean mouseMove(Component component, int x, int y) {
		return false;
	}

	@Override
	public void mouseOver(Component component) {
	}

	@Override
	public void mouseOut(Component component) {
	}

	// Component mouse button events
	@Override
	public boolean mouseDown(Component component, Mouse.Button button, int x,
			int y) {
		return false;
	}

	@Override
	public boolean mouseUp(Component component, Mouse.Button button, int x,
			int y) {
		return false;
	}

	@Override
	public boolean mouseClick(Component component, Mouse.Button button, int x,
			int y, int count) {
		return false;
	}

	// Component mouse wheel events
	@Override
	public boolean mouseWheel(Component component, Mouse.ScrollType scrollType,
			int scrollAmount, int wheelRotation, int x, int y) {
		return false;
	}

	// Component key events
	@Override
	public boolean keyTyped(Component component, char character) {
		return false;
	}

	/**
	 * {@link KeyCode#TAB TAB} Transfers focus forwards<br> {@link KeyCode#TAB TAB}
	 * + {@link Modifier#SHIFT SHIFT} Transfers focus backwards
	 */
	@Override
	public boolean keyPressed(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean keyReleased(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		return false;
	}

	// Utility methods
	protected void invalidateComponent() {
		if (component != null) {
			component.invalidate();
			component.repaint();
		}
	}

	protected void repaintComponent() {
		repaintComponent(false);
	}

	protected void repaintComponent(boolean immediate) {
		if (component != null) {
			component.repaint(immediate);
		}
	}

	// public static Font decodeFont(String value) {
	// Font font;
	// if (value.startsWith("{")) {
	// try {
	// font = Theme.deriveFont(JSONSerializer.parseMap(value));
	// } catch (SerializationException exception) {
	// throw new IllegalArgumentException(exception);
	// }
	// } else {
	// font = Font.decode(value);
	// }
	//
	// return font;
	// }
}
