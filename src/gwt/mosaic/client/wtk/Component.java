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
package gwt.mosaic.client.wtk;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.PropertyNotFoundException;
import gwt.mosaic.client.collections.ArrayList;
import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.json.JSONSerializer;
import gwt.mosaic.client.util.AncestorComparator;
import gwt.mosaic.client.util.ImmutableIterator;
import gwt.mosaic.client.util.ListenerList;
import gwt.mosaic.client.wtk.effects.Decorator;
import gwt.mosaic.shared.beans.IDProperty;
import gwt.mosaic.shared.serialization.SerializationException;

import java.io.Serializable;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Top level abstract base class for all components. In MVC terminology, a
 * component represents the "controller". It has no inherent visual
 * representation and acts as an intermediary between the component's data (the
 * "model") and the skin, an implementation of {@link Skin} which serves as the
 * "view".
 */
@IDProperty("name")
@SuppressWarnings("serial")
public abstract class Component implements ConstrainedVisual, Serializable {
	/**
	 * Style dictionary implementation.
	 */
	public static final class StyleDictionary implements
			Dictionary<String, Object>, Iterable<String>, Serializable {
		private Component component;

		public StyleDictionary() {
			this(null);
		}

		public StyleDictionary(Component component) {
			this.component = component;
		}

		@Override
		public Object get(String key) {
			return component.styles.get(key);
		}

		@Override
		public Object put(String key, Object value) {
			Object previousValue = null;

			try {
				previousValue = component.styles.put(key, value);
				component.componentStyleListeners.styleUpdated(component, key,
						previousValue);
			} catch (PropertyNotFoundException exception) {
				System.err.println("\"" + key + "\" is not a valid style for "
						+ component);
			}

			return previousValue;
		}

		@Override
		public Object remove(String key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean containsKey(String key) {
			return component.styles.containsKey(key);
		}

		public boolean isReadOnly(String key) {
			return component.styles.isReadOnly(key);
		}

		public Class<?> getType(String key) {
			return component.styles.getType(key);
		}

		@Override
		public Iterator<String> iterator() {
			return new ImmutableIterator<String>(component.styles.iterator());
		}
	}

	/**
	 * User data dictionary implementation.
	 */
	public final class UserDataDictionary implements
			Dictionary<String, Object>, Iterable<String> {
		private static final long serialVersionUID = 323698131113657748L;

		private UserDataDictionary() {
		}

		@Override
		public Object get(String key) {
			return userData.get(key);
		}

		@Override
		public Object put(String key, Object value) {
			boolean update = userData.containsKey(key);
			Object previousValue = userData.put(key, value);

			if (update) {
				componentDataListeners.valueUpdated(Component.this, key,
						previousValue);
			} else {
				componentDataListeners.valueAdded(Component.this, key);
			}

			return previousValue;
		}

		@Override
		public Object remove(String key) {
			Object previousValue;
			if (userData.containsKey(key)) {
				previousValue = userData.remove(key);
				componentDataListeners.valueRemoved(Component.this, key,
						previousValue);
			} else {
				previousValue = null;
			}

			return previousValue;
		}

		@Override
		public boolean containsKey(String key) {
			return userData.containsKey(key);
		}

		@Override
		public Iterator<String> iterator() {
			return new ImmutableIterator<String>(userData.iterator());
		}
	}

	/**
	 * Decorator sequence implementation.
	 */
	public final class DecoratorSequence implements Sequence<Decorator>,
			Iterable<Decorator> {
		private static final long serialVersionUID = 6086912248184605244L;

		@Override
		public int add(Decorator decorator) {
			int index = getLength();
			insert(decorator, index);

			return index;
		}

		@Override
		public void insert(Decorator decorator, int index) {
			if (decorator == null) {
				throw new IllegalArgumentException("decorator is null");
			}

			// Repaint the the component's previous decorated region
			if (parent != null) {
				parent.repaint();
			}

			decorators.insert(decorator, index);

			// Repaint the the component's current decorated region
			if (parent != null) {
				parent.repaint();
			}

			componentDecoratorListeners
					.decoratorInserted(Component.this, index);
		}

		@Override
		public Decorator update(int index, Decorator decorator) {
			if (decorator == null) {
				throw new IllegalArgumentException("decorator is null.");
			}

			// Repaint the the component's previous decorated region
			if (parent != null) {
				parent.repaint();
			}

			Decorator previousDecorator = decorators.update(index, decorator);

			// Repaint the the component's current decorated region
			if (parent != null) {
				parent.repaint();
			}

			componentDecoratorListeners.decoratorUpdated(Component.this, index,
					previousDecorator);

			return previousDecorator;
		}

		@Override
		public int remove(Decorator decorator) {
			int index = indexOf(decorator);
			if (index != -1) {
				remove(index, 1);
			}

			return index;
		}

		@Override
		public Sequence<Decorator> remove(int index, int count) {
			if (count > 0) {
				// Repaint the the component's previous decorated region
				if (parent != null) {
					parent.repaint();
				}
			}

			Sequence<Decorator> removed = decorators.remove(index, count);

			if (count > 0) {
				if (parent != null) {
					// Repaint the the component's current decorated region
					parent.repaint();
				}

				componentDecoratorListeners.decoratorsRemoved(Component.this,
						index, removed);
			}

			return removed;
		}

		public Sequence<Decorator> removeAll() {
			return remove(0, getLength());
		}

		@Override
		public Decorator get(int index) {
			return decorators.get(index);
		}

		@Override
		public int indexOf(Decorator decorator) {
			return decorators.indexOf(decorator);
		}

		@Override
		public int getLength() {
			return decorators.getLength();
		}

		@Override
		public Iterator<Decorator> iterator() {
			return new ImmutableIterator<Decorator>(decorators.iterator());
		}
	}

	private static class ComponentListenerList extends
			ListenerList<ComponentListener> implements ComponentListener {
		@Override
		public void parentChanged(Component component, Container previousParent) {
			for (ComponentListener listener : this) {
				listener.parentChanged(component, previousParent);
			}
		}

		@Override
		public void sizeChanged(Component component, int previousWidth,
				int previousHeight) {
			for (ComponentListener listener : this) {
				listener.sizeChanged(component, previousWidth, previousHeight);
			}
		}

		@Override
		public void preferredSizeChanged(Component component,
				int previousPreferredWidth, int previousPreferredHeight) {
			for (ComponentListener listener : this) {
				listener.preferredSizeChanged(component,
						previousPreferredWidth, previousPreferredHeight);
			}
		}

		@Override
		public void widthLimitsChanged(Component component,
				int previousMinimumWidth, int previousMaximumWidth) {
			for (ComponentListener listener : this) {
				listener.widthLimitsChanged(component, previousMinimumWidth,
						previousMaximumWidth);
			}
		}

		@Override
		public void heightLimitsChanged(Component component,
				int previousMinimumHeight, int previousMaximumHeight) {
			for (ComponentListener listener : this) {
				listener.heightLimitsChanged(component, previousMinimumHeight,
						previousMaximumHeight);
			}
		}

		@Override
		public void locationChanged(Component component, int previousX,
				int previousY) {
			for (ComponentListener listener : this) {
				listener.locationChanged(component, previousX, previousY);
			}
		}

		@Override
		public void visibleChanged(Component component) {
			for (ComponentListener listener : this) {
				listener.visibleChanged(component);
			}
		}

		@Override
		public void cursorChanged(Component component, Cursor previousCursor) {
			for (ComponentListener listener : this) {
				listener.cursorChanged(component, previousCursor);
			}
		}

		@Override
		public void tooltipTextChanged(Component component,
				String previousTooltipText) {
			for (ComponentListener listener : this) {
				listener.tooltipTextChanged(component, previousTooltipText);
			}
		}

		// @Override
		// TODO public void tooltipDelayChanged(Component component, int
		// previousTooltipDelay) {}

		@Override
		public void dragSourceChanged(Component component,
				DragSource previousDragSource) {
			for (ComponentListener listener : this) {
				listener.dragSourceChanged(component, previousDragSource);
			}
		}

		@Override
		public void dropTargetChanged(Component component,
				DropTarget previousDropTarget) {
			for (ComponentListener listener : this) {
				listener.dropTargetChanged(component, previousDropTarget);
			}
		}

		@Override
		public void menuHandlerChanged(Component component,
				MenuHandler previousMenuHandler) {
			for (ComponentListener listener : this) {
				listener.menuHandlerChanged(component, previousMenuHandler);
			}
		}

		@Override
		public void nameChanged(Component component, String previousName) {
			for (ComponentListener listener : this) {
				listener.nameChanged(component, previousName);
			}
		}
	}

	private static class ComponentStateListenerList extends
			ListenerList<ComponentStateListener> implements
			ComponentStateListener {
		@Override
		public void enabledChanged(Component component) {
			for (ComponentStateListener listener : this) {
				listener.enabledChanged(component);
			}
		}

		@Override
		public void focusedChanged(Component component,
				Component obverseComponent) {
			for (ComponentStateListener listener : this) {
				listener.focusedChanged(component, obverseComponent);
			}
		}
	}

	private static class ComponentDecoratorListenerList extends
			ListenerList<ComponentDecoratorListener> implements
			ComponentDecoratorListener {
		@Override
		public void decoratorInserted(Component component, int index) {
			for (ComponentDecoratorListener listener : this) {
				listener.decoratorInserted(component, index);
			}
		}

		@Override
		public void decoratorUpdated(Component component, int index,
				Decorator previousDecorator) {
			for (ComponentDecoratorListener listener : this) {
				listener.decoratorUpdated(component, index, previousDecorator);
			}
		}

		@Override
		public void decoratorsRemoved(Component component, int index,
				Sequence<Decorator> decorators) {
			for (ComponentDecoratorListener listener : this) {
				listener.decoratorsRemoved(component, index, decorators);
			}
		}
	}

	private static class ComponentStyleListenerList extends
			ListenerList<ComponentStyleListener> implements
			ComponentStyleListener {
		@Override
		public void styleUpdated(Component component, String styleKey,
				Object previousValue) {
			for (ComponentStyleListener listener : this) {
				listener.styleUpdated(component, styleKey, previousValue);
			}
		}
	}

	private static class ComponentMouseListenerList extends
			ListenerList<ComponentMouseListener> implements
			ComponentMouseListener {
		@Override
		public boolean mouseMove(Component component, int x, int y) {
			boolean consumed = false;

			for (ComponentMouseListener listener : this) {
				consumed |= listener.mouseMove(component, x, y);
			}

			return consumed;
		}

		@Override
		public void mouseOver(Component component) {
			for (ComponentMouseListener listener : this) {
				listener.mouseOver(component);
			}
		}

		@Override
		public void mouseOut(Component component) {
			for (ComponentMouseListener listener : this) {
				listener.mouseOut(component);
			}
		}
	}

	private static class ComponentMouseButtonListenerList extends
			ListenerList<ComponentMouseButtonListener> implements
			ComponentMouseButtonListener {
		@Override
		public boolean mouseDown(Component component, Mouse.Button button,
				int x, int y) {
			boolean consumed = false;

			for (ComponentMouseButtonListener listener : this) {
				consumed |= listener.mouseDown(component, button, x, y);
			}

			return consumed;
		}

		@Override
		public boolean mouseUp(Component component, Mouse.Button button, int x,
				int y) {
			boolean consumed = false;

			for (ComponentMouseButtonListener listener : this) {
				consumed |= listener.mouseUp(component, button, x, y);
			}

			return consumed;
		}

		@Override
		public boolean mouseClick(Component component, Mouse.Button button,
				int x, int y, int count) {
			boolean consumed = false;

			for (ComponentMouseButtonListener listener : this) {
				consumed |= listener.mouseClick(component, button, x, y, count);
			}

			return consumed;
		}
	}

	private static class ComponentMouseWheelListenerList extends
			ListenerList<ComponentMouseWheelListener> implements
			ComponentMouseWheelListener {
		@Override
		public boolean mouseWheel(Component component,
				Mouse.ScrollType scrollType, int scrollAmount,
				int wheelRotation, int x, int y) {
			boolean consumed = false;

			for (ComponentMouseWheelListener listener : this) {
				consumed |= listener.mouseWheel(component, scrollType,
						scrollAmount, wheelRotation, x, y);
			}

			return consumed;
		}
	}

	private static class ComponentKeyListenerList extends
			ListenerList<ComponentKeyListener> implements ComponentKeyListener {
		@Override
		public boolean keyTyped(Component component, char character) {
			boolean consumed = false;

			for (ComponentKeyListener listener : this) {
				consumed |= listener.keyTyped(component, character);
			}

			return consumed;
		}

		@Override
		public boolean keyPressed(Component component, int keyCode,
				Keyboard.KeyLocation keyLocation) {
			boolean consumed = false;

			for (ComponentKeyListener listener : this) {
				consumed |= listener
						.keyPressed(component, keyCode, keyLocation);
			}

			return consumed;
		}

		@Override
		public boolean keyReleased(Component component, int keyCode,
				Keyboard.KeyLocation keyLocation) {
			boolean consumed = false;

			for (ComponentKeyListener listener : this) {
				consumed |= listener.keyReleased(component, keyCode,
						keyLocation);
			}

			return consumed;
		}
	}

	private static class ComponentTooltipListenerList extends
			ListenerList<ComponentTooltipListener> implements
			ComponentTooltipListener {
		@Override
		public void tooltipTriggered(Component component, int x, int y) {
			for (ComponentTooltipListener listener : this) {
				listener.tooltipTriggered(component, x, y);
			}
		}
	}

	private static class ComponentDataListenerList extends
			ListenerList<ComponentDataListener> implements
			ComponentDataListener {
		@Override
		public void valueAdded(Component component, String key) {
			for (ComponentDataListener listener : this) {
				listener.valueAdded(component, key);
			}
		}

		@Override
		public void valueUpdated(Component component, String key,
				Object previousValue) {
			for (ComponentDataListener listener : this) {
				listener.valueUpdated(component, key, previousValue);
			}
		}

		@Override
		public void valueRemoved(Component component, String key, Object value) {
			for (ComponentDataListener listener : this) {
				listener.valueRemoved(component, key, value);
			}
		}
	}

	private static class ComponentClassListenerList extends
			ListenerList<ComponentClassListener> implements
			ComponentClassListener {
		@Override
		public void focusedComponentChanged(Component previousFocusedComponent) {
			for (ComponentClassListener listener : this) {
				listener.focusedComponentChanged(previousFocusedComponent);
			}
		}
	}

	// The currently installed skin, or null if no skin is installed
	private transient Skin skin = null;

	// Preferred width and height values explicitly set by the user
	private int preferredWidth = -1;
	private int preferredHeight = -1;

	// Bounds on preferred size
	private int minimumWidth = 0;
	private int maximumWidth = Integer.MAX_VALUE;
	private int minimumHeight = 0;
	private int maximumHeight = Integer.MAX_VALUE;

	// Calculated preferred size value
	private Dimensions preferredSize = null;

	// Calculated baseline for current size
	private int baseline = -1;

	// The component's parent container, or null if the component does not have
	// a parent
	private Container parent = null;

	// The component's valid state
	private boolean valid = false;

	// The component's location, relative to the parent's origin
	private int x = 0;
	private int y = 0;

	// The component's visible flag
	private boolean visible = true;

	// The component's decorators
	private transient ArrayList<Decorator> decorators = new ArrayList<Decorator>();
	private transient DecoratorSequence decoratorSequence = new DecoratorSequence();

	// The component's enabled flag
	private boolean enabled = true;

	// The current mouse location
	private Point mouseLocation = null;

	// The cursor that is displayed over the component
	private Cursor cursor = null;

	// The tooltip text, delay, and trigger callback
	private String tooltipText = null;
	private int tooltipDelay = 1000;
	// private ApplicationContext.ScheduledCallback triggerTooltipCallback =
	// null;

	// The component's drag source
	private transient DragSource dragSource = null;

	// The component's drop target
	private transient DropTarget dropTarget = null;

	// The component's menu handler
	private transient MenuHandler menuHandler = null;

	// The component's name
	private String name = null;

	// The component's styles
	private transient BeanAdapter<? extends Skin> styles = null;
	private StyleDictionary styleDictionary = new StyleDictionary(this);
	private String jsonStyles = null;

	// User data
	private transient HashMap<String, Object> userData = new HashMap<String, Object>();
	private transient UserDataDictionary userDataDictionary = new UserDataDictionary();

	// Container attributes
	private transient HashMap<? extends Enum<?>, Object> attributes = null;// XXX

	// The component's automation ID
	private String automationID;

	// Event listener lists
	private transient ComponentListenerList componentListeners = new ComponentListenerList();
	private transient ComponentStateListenerList componentStateListeners = new ComponentStateListenerList();
	private transient ComponentDecoratorListenerList componentDecoratorListeners = new ComponentDecoratorListenerList();
	private transient ComponentStyleListenerList componentStyleListeners = new ComponentStyleListenerList();
	private transient ComponentMouseListenerList componentMouseListeners = new ComponentMouseListenerList();
	private transient ComponentMouseButtonListenerList componentMouseButtonListeners = new ComponentMouseButtonListenerList();
	private transient ComponentMouseWheelListenerList componentMouseWheelListeners = new ComponentMouseWheelListenerList();
	private transient ComponentKeyListenerList componentKeyListeners = new ComponentKeyListenerList();
	private transient ComponentTooltipListenerList componentTooltipListeners = new ComponentTooltipListenerList();
	private transient ComponentDataListenerList componentDataListeners = new ComponentDataListenerList();

	// The component that currently has the focus
	private static Component focusedComponent = null;

	// Typed and named styles
	private static transient HashMap<Class<? extends Component>, Map<String, ?>> typedStyles = new HashMap<Class<? extends Component>, Map<String, ?>>();
	private static transient HashMap<String, Map<String, ?>> namedStyles = new HashMap<String, Map<String, ?>>();

	// Class event listeners
	private static transient ComponentClassListenerList componentClassListeners = new ComponentClassListenerList();

	protected Component() {
		super();

		if (!GWT.isClient()) {
			return;
		}

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				try {
					if (jsonStyles != null) {
						setStyles(jsonStyles);
					}
				} catch (SerializationException e) {
					GWT.log(e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * Returns the component's automation ID.
	 * 
	 * @return The component's automation ID, or <tt>null</tt> if the component
	 *         does not have an automation ID.
	 */
	public String getAutomationID() {
		return automationID;
	}

	/**
	 * Sets the component's automation ID. This value can be used to obtain a
	 * reference to the component via {@link Automation#get(String)} when the
	 * component is attached to a component hierarchy.
	 * 
	 * @param automationID
	 *            The automation ID to use for the component, or <tt>null</tt>
	 *            to clear the automation ID.
	 */
	public void setAutomationID(String automationID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the currently installed skin.
	 * 
	 * @return The currently installed skin.
	 */
	protected Skin getSkin() {
		return skin;
	}

	/**
	 * Sets the skin, replacing any previous skin.
	 * 
	 * @param skin
	 *            The new skin.
	 */
	// @SuppressWarnings("unchecked")
	protected void setSkin(BeanAdapter<? extends Skin> styles) {
		if (styles == null) {
			throw new IllegalArgumentException("styles is null.");
		}

		if (styles.getBean() == null) {
			throw new IllegalArgumentException("skin is null.");
		}

		if (this.skin != null) {
			throw new IllegalStateException("Skin is already installed.");
		}

		this.styles = styles;
		this.skin = styles.getBean();
		this.skin.install(this);

		// Apply any defined type styles
		// LinkedList<Class<?>> styleTypes = new LinkedList<Class<?>>();
		//
		// Class<?> type = getClass();
		// while (type != Object.class) {
		// styleTypes.insert(type, 0);
		// type = type.getSuperclass();
		// }
		//
		// for (Class<?> styleType : styleTypes) {
		// Map<String, ?> styles = typedStyles
		// .get((Class<? extends Component>) styleType);
		//
		// if (styles != null) {
		// setStyles(styles);
		// }
		// }

		invalidate();
		repaint();
	}

	/**
	 * Installs the skin for the given component class, as defined by the
	 * current theme.
	 * 
	 * @param componentClass
	 */
	@SuppressWarnings("unchecked")
	protected void installSkin(Class<? extends Component> componentClass) {
		if (GWT.isClient()) {
			// Walk up component hierarchy from this type; if we find a match
			// and
			// the super class equals the given component class, install the
			// skin.
			// Otherwise, ignore - it will be installed later by a subclass of
			// the
			// component class.
			Class<? extends Component> type = getClass();

			Theme theme = Theme.getTheme();
			BeanAdapter<? extends Skin> adapter = theme.get(type);

			while (adapter == null && type != componentClass
					&& type != Component.class) {
				type = (Class<? extends Component>) type.getSuperclass();

				if (type != Component.class) {
					adapter = theme.get(type);
				}
			}

			if (type == Component.class) {
				throw new IllegalArgumentException(componentClass.getName()
						+ " is not an ancestor of " + getClass().getName());
			}

			if (adapter == null) {
				throw new IllegalArgumentException("No skin mapping for "
						+ componentClass.getName() + " found.");
			}

			if (type == componentClass) {
				setSkin(adapter);
			}
		}
	}

	public Container getParent() {
		return parent;
	}

	protected void setParent(Container parent) {
		// If this component is being removed from the component hierarchy
		// and is currently focused, clear the focus
		if (parent == null && isFocused()) {
			clearFocus();
		}

		Container previousParent = this.parent;
		this.parent = parent;

		if (previousParent != null) {
			previousParent.descendantRemoved(this);
		}

		if (parent != null) {
			parent.descendantAdded(this);
		}

		componentListeners.parentChanged(this, previousParent);
	}

	public Window getWindow() {
		return (Window) getAncestor(new AncestorComparator() {
			@Override
			public boolean isInstanceOf(Object o) {
				return o instanceof Window;
			}
		});
	}

	public Display getDisplay() {
		return (Display) getAncestor(new AncestorComparator() {
			@Override
			public boolean isInstanceOf(Object o) {
				return o instanceof Display;
			}
		});
	}

	public Container getAncestor(AncestorComparator comparator) {
		Component component = this;
		while (component != null && !comparator.isInstanceOf(component)) {
			component = component.getParent();
		}
		return (Container) component;
	}

	// public Container getAncestor(Class<? extends Container> ancestorType) {
	// Component component = this;
	//
	// while (component != null && !(ancestorType.isInstance(component))) {
	// component = component.getParent();
	// }
	//
	// return (Container) component;
	// }
	//
	// @SuppressWarnings("unchecked")
	// public Container getAncestor(String ancestorTypeName)
	// throws ClassNotFoundException {
	// if (ancestorTypeName == null) {
	// throw new IllegalArgumentException();
	// }
	//
	// return getAncestor((Class<? extends Container>) Class
	// .forName(ancestorTypeName));
	// }

	@Override
	public int getWidth() {
		return skin.getWidth();
	}

	public void setWidth(int width) {
		setSize(width, getHeight());
	}

	@Override
	public int getHeight() {
		return skin.getHeight();
	}

	public void setHeight(int height) {
		setSize(getWidth(), height);
	}

	public Dimensions getSize() {
		return new Dimensions(this.getWidth(), this.getHeight());
	}

	public final void setSize(Dimensions size) {
		if (size == null) {
			throw new IllegalArgumentException("size is null.");
		}

		setSize(size.width, size.height);
	}

	/**
	 * NOTE This method should only be called during layout. Callers should use
	 * {@link #setPreferredSize(int, int)}.
	 * 
	 * @param width
	 * @param height
	 */
	@Override
	public void setSize(int width, int height) {
		if (width < 0) {
			throw new IllegalArgumentException("width is negative.");
		}

		if (height < 0) {
			throw new IllegalArgumentException("height is negative.");
		}

		int previousWidth = getWidth();
		int previousHeight = getHeight();

		if (width != previousWidth || height != previousHeight) {
			// This component's size changed, most likely as a result
			// of being laid out; it must be flagged as invalid to ensure
			// that layout is propagated downward when validate() is
			// called on it
			invalidate();

			// Redraw the region formerly occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			// Set the size of the skin
			skin.setSize(width, height);

			// Redraw the region currently occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			componentListeners.sizeChanged(this, previousWidth, previousHeight);
		}
	}

	/**
	 * Returns the component's unconstrained preferred width.
	 */
	public int getPreferredWidth() {
		return getPreferredWidth(-1);
	}

	/**
	 * Returns the component's constrained preferred width.
	 * 
	 * @param height
	 *            The height value by which the preferred width should be
	 *            constrained, or <tt>-1</tt> for no constraint.
	 * 
	 * @return The constrained preferred width.
	 */
	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth;

		if (this.preferredWidth == -1) {
			if (height == -1) {
				preferredWidth = getPreferredSize().width;
			} else {
				if (preferredSize != null && preferredSize.height == height) {
					preferredWidth = preferredSize.width;
				} else {
					Limits widthLimits = getWidthLimits();
					preferredWidth = widthLimits.constrain(skin
							.getPreferredWidth(height));
				}
			}
		} else {
			preferredWidth = this.preferredWidth;
		}

		return preferredWidth;
	}

	/**
	 * Sets the component's preferred width.
	 * 
	 * @param preferredWidth
	 *            The preferred width value, or <tt>-1</tt> to use the default
	 *            value determined by the skin.
	 */
	public void setPreferredWidth(int preferredWidth) {
		setPreferredSize(preferredWidth, preferredHeight);
	}

	/**
	 * Returns a flag indicating whether the preferred width was explicitly set
	 * by the caller or is the default value determined by the skin.
	 * 
	 * @return <tt>true</tt> if the preferred width was explicitly set;
	 *         <tt>false</tt>, otherwise.
	 */
	public boolean isPreferredWidthSet() {
		return (preferredWidth != -1);
	}

	/**
	 * Returns the component's unconstrained preferred height.
	 */
	public int getPreferredHeight() {
		return getPreferredHeight(-1);
	}

	/**
	 * Returns the component's constrained preferred height.
	 * 
	 * @param width
	 *            The width value by which the preferred height should be
	 *            constrained, or <tt>-1</tt> for no constraint.
	 * 
	 * @return The constrained preferred height.
	 */
	@Override
	public int getPreferredHeight(int width) {
		int preferredHeight;

		if (this.preferredHeight == -1) {
			if (width == -1) {
				preferredHeight = getPreferredSize().height;
			} else {
				if (preferredSize != null && preferredSize.width == width) {
					preferredHeight = preferredSize.height;
				} else {
					Limits heightLimits = getHeightLimits();
					preferredHeight = heightLimits.constrain(skin
							.getPreferredHeight(width));
				}
			}
		} else {
			preferredHeight = this.preferredHeight;
		}

		return preferredHeight;
	}

	/**
	 * Sets the component's preferred height.
	 * 
	 * @param preferredHeight
	 *            The preferred height value, or <tt>-1</tt> to use the default
	 *            value determined by the skin.
	 */
	public void setPreferredHeight(int preferredHeight) {
		setPreferredSize(preferredWidth, preferredHeight);
	}

	/**
	 * Returns a flag indicating whether the preferred height was explicitly set
	 * by the caller or is the default value determined by the skin.
	 * 
	 * @return <tt>true</tt> if the preferred height was explicitly set;
	 *         <tt>false</tt>, otherwise.
	 */
	public boolean isPreferredHeightSet() {
		return (preferredHeight != -1);
	}

	/**
	 * Gets the component's unconstrained preferred size.
	 */
	@Override
	public Dimensions getPreferredSize() {
		if (preferredSize == null) {
			Dimensions preferredSize;
			if (preferredWidth == -1 && preferredHeight == -1) {
				preferredSize = skin.getPreferredSize();
			} else if (preferredWidth == -1) {
				preferredSize = new Dimensions(
						skin.getPreferredWidth(preferredHeight),
						preferredHeight);
			} else if (preferredHeight == -1) {
				preferredSize = new Dimensions(preferredWidth,
						skin.getPreferredHeight(preferredWidth));
			} else {
				preferredSize = new Dimensions(preferredWidth, preferredHeight);
			}

			Limits widthLimits = getWidthLimits();
			Limits heightLimits = getHeightLimits();

			int preferredWidth = widthLimits.constrain(preferredSize.width);
			int preferredHeight = heightLimits.constrain(preferredSize.height);

			if (preferredSize.width > preferredWidth) {
				preferredHeight = heightLimits.constrain(skin
						.getPreferredHeight(preferredWidth));
			}

			if (preferredSize.height > preferredHeight) {
				preferredWidth = widthLimits.constrain(skin
						.getPreferredWidth(preferredHeight));
			}

			this.preferredSize = new Dimensions(preferredWidth, preferredHeight);
		}

		return preferredSize;
	}

	public final void setPreferredSize(Dimensions preferredSize) {
		if (preferredSize == null) {
			throw new IllegalArgumentException("preferredSize is null.");
		}

		setPreferredSize(preferredSize.width, preferredSize.height);
	}

	/**
	 * Sets the component's preferred size.
	 * 
	 * @param preferredWidth
	 *            The preferred width value, or <tt>-1</tt> to use the default
	 *            value determined by the skin.
	 * 
	 * @param preferredHeight
	 *            The preferred height value, or <tt>-1</tt> to use the default
	 *            value determined by the skin.
	 */
	public void setPreferredSize(int preferredWidth, int preferredHeight) {
		if (preferredWidth < -1) {
			throw new IllegalArgumentException(preferredWidth
					+ " is not a valid value for preferredWidth.");
		}

		if (preferredHeight < -1) {
			throw new IllegalArgumentException(preferredHeight
					+ " is not a valid value for preferredHeight.");
		}

		int previousPreferredWidth = this.preferredWidth;
		int previousPreferredHeight = this.preferredHeight;

		if (previousPreferredWidth != preferredWidth
				|| previousPreferredHeight != preferredHeight) {
			this.preferredWidth = preferredWidth;
			this.preferredHeight = preferredHeight;

			invalidate();

			componentListeners.preferredSizeChanged(this,
					previousPreferredWidth, previousPreferredHeight);
		}
	}

	/**
	 * Returns a flag indicating whether the preferred size was explicitly set
	 * by the caller or is the default value determined by the skin.
	 * 
	 * @return <tt>true</tt> if the preferred size was explicitly set;
	 *         <tt>false</tt>, otherwise.
	 */
	public boolean isPreferredSizeSet() {
		return isPreferredWidthSet() && isPreferredHeightSet();
	}

	/**
	 * Returns the minimum width of this component.
	 */
	public int getMinimumWidth() {
		return minimumWidth;
	}

	/**
	 * Sets the minimum width of this component.
	 * 
	 * @param minimumWidth
	 */
	public void setMinimumWidth(int minimumWidth) {
		setWidthLimits(minimumWidth, getMaximumWidth());
	}

	/**
	 * Returns the maximum width of this component.
	 */
	public int getMaximumWidth() {
		return maximumWidth;
	}

	/**
	 * Sets the maximum width of this component.
	 * 
	 * @param maximumWidth
	 */
	public void setMaximumWidth(int maximumWidth) {
		setWidthLimits(getMinimumWidth(), maximumWidth);
	}

	/**
	 * Returns the width limits for this component.
	 */
	public Limits getWidthLimits() {
		return new Limits(minimumWidth, maximumWidth);
	}

	/**
	 * Sets the width limits for this component.
	 * 
	 * @param minimumWidth
	 * @param maximumWidth
	 */
	public void setWidthLimits(int minimumWidth, int maximumWidth) {
		int previousMinimumWidth = this.minimumWidth;
		int previousMaximumWidth = this.maximumWidth;

		if (previousMinimumWidth != minimumWidth
				|| previousMaximumWidth != maximumWidth) {
			if (minimumWidth < 0) {
				throw new IllegalArgumentException("minimumWidth is negative.");
			}

			if (minimumWidth > maximumWidth) {
				throw new IllegalArgumentException(
						"minimumWidth is greater than maximumWidth.");
			}

			this.minimumWidth = minimumWidth;
			this.maximumWidth = maximumWidth;

			invalidate();

			componentListeners.widthLimitsChanged(this, previousMinimumWidth,
					previousMaximumWidth);
		}
	}

	/**
	 * Sets the width limits for this component.
	 * 
	 * @param widthLimits
	 */
	public final void setWidthLimits(Limits widthLimits) {
		if (widthLimits == null) {
			throw new IllegalArgumentException("widthLimits is null.");
		}

		setWidthLimits(widthLimits.minimum, widthLimits.maximum);
	}

	/**
	 * Returns the minimum height of this component.
	 */
	public int getMinimumHeight() {
		return minimumHeight;
	}

	/**
	 * Sets the minimum height of this component.
	 * 
	 * @param minimumHeight
	 */
	public void setMinimumHeight(int minimumHeight) {
		setHeightLimits(minimumHeight, getMaximumHeight());
	}

	/**
	 * Returns the maximum height of this component.
	 */
	public int getMaximumHeight() {
		return maximumHeight;
	}

	/**
	 * Sets the maximum height of this component.
	 * 
	 * @param maximumHeight
	 */
	public void setMaximumHeight(int maximumHeight) {
		setHeightLimits(getMinimumHeight(), maximumHeight);
	}

	/**
	 * Returns the height limits for this component.
	 */
	public Limits getHeightLimits() {
		return new Limits(minimumHeight, maximumHeight);
	}

	/**
	 * Sets the height limits for this component.
	 * 
	 * @param minimumHeight
	 * @param maximumHeight
	 */
	public void setHeightLimits(int minimumHeight, int maximumHeight) {
		int previousMinimumHeight = this.minimumHeight;
		int previousMaximumHeight = this.maximumHeight;

		if (previousMinimumHeight != minimumHeight
				|| previousMaximumHeight != maximumHeight) {
			if (minimumHeight < 0) {
				throw new IllegalArgumentException("minimumHeight is negative.");
			}

			if (minimumHeight > maximumHeight) {
				throw new IllegalArgumentException(
						"minimumHeight is greater than maximumHeight.");
			}

			this.minimumHeight = minimumHeight;
			this.maximumHeight = maximumHeight;

			invalidate();

			componentListeners.heightLimitsChanged(this, previousMinimumHeight,
					previousMaximumHeight);
		}
	}

	/**
	 * Sets the height limits for this component.
	 * 
	 * @param heightLimits
	 */
	public final void setHeightLimits(Limits heightLimits) {
		if (heightLimits == null) {
			throw new IllegalArgumentException("heightLimits is null.");
		}

		setHeightLimits(heightLimits.minimum, heightLimits.maximum);
	}

	/**
	 * Returns the component's x-coordinate.
	 * 
	 * @return The component's horizontal position relative to the origin of the
	 *         parent container.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the component's x-coordinate.
	 * 
	 * @param x
	 *            The component's horizontal position relative to the origin of
	 *            the parent container.
	 */
	public void setX(int x) {
		setLocation(x, getY());
	}

	/**
	 * Returns the component's y-coordinate.
	 * 
	 * @return The component's vertical position relative to the origin of the
	 *         parent container.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the component's y-coordinate.
	 * 
	 * @param y
	 *            The component's vertical position relative to the origin of
	 *            the parent container.
	 */
	public void setY(int y) {
		setLocation(getX(), y);
	}

	/**
	 * Returns the component's location.
	 * 
	 * @return A point value containing the component's horizontal and vertical
	 *         position relative to the origin of the parent container.
	 */
	public Point getLocation() {
		return new Point(getX(), getY());
	}

	/**
	 * Sets the component's location.
	 * 
	 * NOTE This method should only be called when performing layout. However,
	 * since some containers do not reposition components during layout, it is
	 * valid for callers to invoke this method directly when such containers.
	 * 
	 * @param x
	 *            The component's horizontal position relative to the origin of
	 *            the parent container.
	 * 
	 * @param y
	 *            The component's vertical position relative to the origin of
	 *            the parent container.
	 */
	public void setLocation(int x, int y) {
		int previousX = this.x;
		int previousY = this.y;

		if (previousX != x || previousY != y) {
			// Redraw the region formerly occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			// Set the new coordinates
			this.x = x;
			this.y = y;

			// Redraw the region currently occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			componentListeners.locationChanged(this, previousX, previousY);
		}
	}

	/**
	 * Sets the component's location.
	 * 
	 * @param location
	 *            A point value containing the component's horizontal and
	 *            vertical position relative to the origin of the parent
	 *            container.
	 * 
	 * @see #setLocation(int, int)
	 */
	public final void setLocation(Point location) {
		if (location == null) {
			throw new IllegalArgumentException("location cannot be null.");
		}

		setLocation(location.x, location.y);
	}

	/**
	 * Returns the component's baseline.
	 * 
	 * @return The baseline relative to the origin of this component, or
	 *         <tt>-1</tt> if this component does not have a baseline.
	 */
	@Override
	public int getBaseline() {
		if (baseline == -1) {
			baseline = skin.getBaseline();
		}

		return baseline;
	}

	/**
	 * Returns the component's baseline for a given width and height.
	 * 
	 * @return The baseline relative to the origin of this component, or
	 *         <tt>-1</tt> if this component does not have a baseline.
	 */
	@Override
	public int getBaseline(int width, int height) {
		return skin.getBaseline(width, height);
	}

	/**
	 * Returns the component's bounding area.
	 * 
	 * @return The component's bounding area. The <tt>x</tt> and <tt>y</tt>
	 *         values are relative to the parent container.
	 */
	public Bounds getBounds() {
		return new Bounds(x, y, getWidth(), getHeight());
	}

	/**
	 * Returns the component's bounding area including decorators.
	 * 
	 * @return The decorated bounding area. The <tt>x</tt> and <tt>y</tt> values
	 *         are relative to the parent container.
	 */
	public Bounds getDecoratedBounds() {
		Bounds decoratedBounds = new Bounds(0, 0, getWidth(), getHeight());

		for (Decorator decorator : decorators) {
			decoratedBounds = decoratedBounds.union(decorator.getBounds(this));
		}

		return new Bounds(decoratedBounds.x + x, decoratedBounds.y + y,
				decoratedBounds.width, decoratedBounds.height);
	}

	/**
	 * Determines if the component contains a given location. This method
	 * facilitates mouse interaction with non-rectangular components.
	 * 
	 * @param x
	 * @param y
	 * 
	 * @return <tt>true</tt> if the component's shape contains the given
	 *         location; <tt>false</tt>, otherwise.
	 * 
	 * @throws UnsupportedOperationException
	 *             This method is not currently implemented.
	 */
	public boolean contains(int x, int y) {
		// TODO
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the component's visibility.
	 * 
	 * @return <tt>true</tt> if the component will be painted; <tt>false</tt>,
	 *         otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the component's visibility.
	 * 
	 * @param visible
	 *            <tt>true</tt> if the component should be painted;
	 *            <tt>false</tt>, otherwise.
	 */
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			// If this component is being hidden and has the focus, clear
			// the focus
			if (!visible) {
				if (isFocused()) {
					clearFocus();
				}

				// Ensure that the mouse out event is processed
				if (isMouseOver()) {
					mouseOut();
				}
			}

			// Redraw the region formerly occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			this.visible = visible;

			// Redraw the region currently occupied by this component
			if (parent != null) {
				parent.repaint();
			}

			// Ensure the layout is valid
			if (visible && !valid) {
				validate();
			}

			// Invalidate the parent
			if (parent != null) {
				parent.invalidate();
			}

			componentListeners.visibleChanged(this);
		}
	}

	/**
	 * Returns the component's decorator sequence.
	 * 
	 * @return The component's decorator sequence
	 */
	public DecoratorSequence getDecorators() {
		return decoratorSequence;
	}

	/**
	 * Maps a point in this component's coordinate system to the specified
	 * ancestor's coordinate space.
	 * 
	 * @param x
	 *            The x-coordinate in this component's coordinate space
	 * 
	 * @param y
	 *            The y-coordinate in this component's coordinate space
	 * 
	 * @return A point containing the translated coordinates, or <tt>null</tt>
	 *         if the component is not a descendant of the specified ancestor.
	 */
	public Point mapPointToAncestor(Container ancestor, int x, int y) {
		if (ancestor == null) {
			throw new IllegalArgumentException("ancestor is null");
		}

		Point coordinates = null;

		Component component = this;

		while (component != null && coordinates == null) {
			if (component == ancestor) {
				coordinates = new Point(x, y);
			} else {
				x += component.x;
				y += component.y;

				component = component.getParent();
			}
		}

		return coordinates;
	}

	public Point mapPointToAncestor(Container ancestor, Point location) {
		if (location == null) {
			throw new IllegalArgumentException();
		}

		return mapPointToAncestor(ancestor, location.x, location.y);
	}

	/**
	 * Maps a point in the specified ancestor's coordinate space to this
	 * component's coordinate system.
	 * 
	 * @param x
	 *            The x-coordinate in the ancestors's coordinate space.
	 * 
	 * @param y
	 *            The y-coordinate in the ancestor's coordinate space.
	 * 
	 * @return A point containing the translated coordinates, or <tt>null</tt>
	 *         if the component is not a descendant of the specified ancestor.
	 */
	public Point mapPointFromAncestor(Container ancestor, int x, int y) {
		if (ancestor == null) {
			throw new IllegalArgumentException("ancestor is null");
		}

		Point coordinates = null;

		Component component = this;

		while (component != null && coordinates == null) {
			if (component == ancestor) {
				coordinates = new Point(x, y);
			} else {
				x -= component.x;
				y -= component.y;

				component = component.getParent();
			}
		}

		return coordinates;
	}

	public Point mapPointFromAncestor(Container ancestor, Point location) {
		if (location == null) {
			throw new IllegalArgumentException();
		}

		return mapPointFromAncestor(ancestor, location.x, location.y);
	}

	/**
	 * Determines if this component is showing. To be showing, the component and
	 * all of its ancestors must be visible and attached to a display.
	 * 
	 * @return <tt>true</tt> if this component is showing; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean isShowing() {
		Component component = this;

		while (component != null && component.isVisible()
				&& !(component instanceof Display)) {
			component = component.getParent();
		}

		return (component != null && component.isVisible());
	}

	/**
	 * Determines the visible area of a component. The visible area is defined
	 * as the intersection of the component's area with the visible area of its
	 * ancestors, or, in the case of a Viewport, the viewport bounds.
	 * 
	 * @return The visible area of the component in the component's coordinate
	 *         space, or <tt>null</tt> if the component is either not showing or
	 *         not part of the component hierarchy.
	 */
	public Bounds getVisibleArea() {
		return getVisibleArea(0, 0, getWidth(), getHeight());
	}

	/**
	 * Determines the visible area of a component. The visible area is defined
	 * as the intersection of the component's area with the visible area of its
	 * ancestors, or, in the case of a Viewport, the viewport bounds.
	 * 
	 * @param area
	 * 
	 * @return The visible area of the component in the component's coordinate
	 *         space, or <tt>null</tt> if the component is either not showing or
	 *         not part of the component hierarchy.
	 */
	public Bounds getVisibleArea(Bounds area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null.");
		}

		return getVisibleArea(area.x, area.y, area.width, area.height);
	}

	/**
	 * Determines the visible area of a component. The visible area is defined
	 * as the intersection of the component's area with the visible area of its
	 * ancestors, or, in the case of a Viewport, the viewport bounds.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * 
	 * @return The visible area of the component in the component's coordinate
	 *         space, or <tt>null</tt> if the component is either not showing or
	 *         not part of the component hierarchy.
	 */
	public Bounds getVisibleArea(int x, int y, int width, int height) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ensures that the given area of a component is visible within the
	 * viewports of all applicable ancestors.
	 * 
	 * @param area
	 */
	public void scrollAreaToVisible(Bounds area) {
		if (area == null) {
			throw new IllegalArgumentException("area is null.");
		}

		scrollAreaToVisible(area.x, area.y, area.width, area.height);
	}

	/**
	 * Ensures that the given area of a component is visible within the
	 * viewports of all applicable ancestors.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void scrollAreaToVisible(int x, int y, int width, int height) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the component's valid state.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Flags the component's hierarchy as invalid, and clears any cached
	 * preferred size.
	 */
	public void invalidate() {
		valid = false;

		// Clear the preferred size and baseline
		preferredSize = null;
		baseline = -1;

		if (parent != null) {
			parent.invalidate();
		}
	}

	/**
	 * Lays out the component by calling {@link Skin#layout()}.
	 */
	public void validate() {
		if (!valid && visible) {
			layout();
			valid = true;
		}
	}

	/**
	 * Called to lay out the component.
	 */
	protected void layout() {
		System.out.println("---L-------------" + getClass().getName());
		skin.layout();
	}

	private boolean needsPainting = false;

	/**
	 * Flags the entire component as needing to be repainted.
	 */
	public final void repaint() {
		repaint(false);
	}

	/**
	 * Flags the entire component as needing to be repainted.
	 * 
	 * @param immediate
	 */
	public void repaint(boolean immediate) {
		needsPainting = true;

		if (parent != null) {
			// Notify the parent that the region needs updating
			parent.repaint(immediate);

			// Repaint any affected decorators
		}
	}

	/**
	 * Paints the component. Delegates to the skin.
	 */
	@Override
	public void paint(Widget context) {
		System.out.println("---P-------------" + getClass().getName());

		if (context == null) {
			getSkin().getWidget().removeFromParent();
			return;
		}

		if (!getSkin().getWidget().isAttached()) {
			if (context instanceof HasOneWidget) {
				((HasOneWidget) context).setWidget((IsWidget) getSkin()
						.getWidget());
			} else if (context instanceof HasWidgets) {
				((HasWidgets) context).add(getSkin().getWidget());
			}
			invalidate();
		}

		if (!needsPainting) {
			return;
		}

		skin.paint(context);
		needsPainting = false;
	}

	/**
	 * Returns the component's enabled state.
	 * 
	 * @return <tt>true</tt> if the component is enabled; <tt>false</tt>,
	 *         otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the component's enabled state. Enabled components respond to user
	 * input events; disabled components do not.
	 * 
	 * @param enabled
	 *            <tt>true</tt> if the component is enabled; <tt>false</tt>,
	 *            otherwise.
	 */
	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			if (!enabled) {
				// If this component has the focus, clear it
				if (isFocused()) {
					clearFocus();
				}

				// Ensure that the mouse out event is processed
				if (isMouseOver()) {
					mouseOut();
				}
			}

			this.enabled = enabled;

			componentStateListeners.enabledChanged(this);
		}
	}

	/**
	 * Determines if this component is blocked. A component is blocked if the
	 * component or any of its ancestors is disabled.
	 * 
	 * @return <tt>true</tt> if the component is blocked; <tt>false</tt>,
	 *         otherwise.
	 */
	public boolean isBlocked() {
		boolean blocked = false;

		Component component = this;

		while (component != null && !blocked) {
			blocked = !component.isEnabled();
			component = component.getParent();
		}

		return blocked;
	}

	/**
	 * Determines if the mouse is positioned over this component.
	 * 
	 * @return <tt>true</tt> if the mouse is currently located over this
	 *         component; <tt>false</tt>, otherwise.
	 */
	public boolean isMouseOver() {
		return (mouseLocation != null);
	}

	/**
	 * Returns the current mouse location in the component's coordinate space.
	 * 
	 * @return The current mouse location, or <tt>null</tt> if the mouse is not
	 *         currently positioned over this component.
	 */
	public Point getMouseLocation() {
		return mouseLocation;
	}

	/**
	 * Returns the cursor that is displayed when the mouse pointer is over this
	 * component.
	 * 
	 * @return The cursor that is displayed over the component.
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Sets the cursor that is displayed when the mouse pointer is over this
	 * component.
	 * 
	 * @param cursor
	 *            The cursor to display over the component, or <tt>null</tt> to
	 *            inherit the cursor of the parent container.
	 */
	public void setCursor(Cursor cursor) {
		Cursor previousCursor = this.cursor;

		if (previousCursor != cursor) {
			this.cursor = cursor;

			if (isMouseOver()) {
				Mouse.setCursor(this);
			}

			componentListeners.cursorChanged(this, previousCursor);
		}
	}

	/**
	 * Returns the component's tooltip text.
	 * 
	 * @return The component's tooltip text, or <tt>null</tt> if no tooltip is
	 *         specified.
	 */
	public String getTooltipText() {
		return tooltipText;
	}

	/**
	 * Sets the component's tooltip text.
	 * 
	 * @param tooltipText
	 *            The component's tooltip text, or <tt>null</tt> for no tooltip.
	 */
	public void setTooltipText(String tooltipText) {
		String previousTooltipText = this.tooltipText;

		if (previousTooltipText != tooltipText) {
			this.tooltipText = tooltipText;
			componentListeners.tooltipTextChanged(this, previousTooltipText);
		}
	}

	/**
	 * Returns the component's tooltip delay.
	 * 
	 * @return The tooltip delay, in milliseconds.
	 */
	public int getTooltipDelay() {
		return tooltipDelay;
	}

	/**
	 * Sets the component's tooltip delay.
	 * 
	 * @param tooltipDelay
	 *            The tooltip delay, in milliseconds.
	 */
	public void setTooltipDelay(int tooltipDelay) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Tells whether or not this component is fully opaque when painted.
	 * 
	 * @return <tt>true</tt> if this component is opaque; </tt>false</tt> if any
	 *         part of it is transparent or translucent.
	 */
	public boolean isOpaque() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns this component's focusability. A focusable component is capable
	 * of receiving the focus only when it is showing, unblocked, and its window
	 * is not closing.
	 * 
	 * @return <tt>true</tt> if the component is capable of receiving the focus;
	 *         <tt>false</tt>, otherwise.
	 */
	public boolean isFocusable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the component's focused state.
	 * 
	 * @return <tt>true</tt> if the component has the input focus;
	 *         <tt>false</tt> otherwise.
	 */
	public boolean isFocused() {
		return (focusedComponent == this);
	}

	/**
	 * Called to notify a component that its focus state has changed.
	 * 
	 * @param focused
	 *            <tt>true</tt> if the component has received the input focus;
	 *            <tt>false</tt> if the component has lost the focus.
	 * 
	 * @param obverseComponent
	 *            If <tt>focused</tt> is true, the component that has lost the
	 *            focus; otherwise, the component that has gained the focus.
	 */
	protected void setFocused(boolean focused, Component obverseComponent) {
		if (focused) {
			parent.descendantGainedFocus(this, obverseComponent);
		} else {
			parent.descendantLostFocus(this);
		}

		componentStateListeners.focusedChanged(this, obverseComponent);
	}

	/**
	 * Requests that focus be given to this component.
	 * 
	 * @return <tt>true</tt> if the component gained the focus; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean requestFocus() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Transfers focus to the next focusable component.
	 * 
	 * @param direction
	 *            The direction in which to transfer focus.
	 */
	public Component transferFocus(FocusTraversalDirection direction) {
		Component component = null;

		Container parent = getParent();
		if (parent != null) {
			component = parent.transferFocus(this, direction);
		}

		return component;
	}

	/**
	 * Returns the currently focused component.
	 * 
	 * @return The component that currently has the focus, or <tt>null</tt> if
	 *         no component is focused.
	 */
	public static Component getFocusedComponent() {
		return focusedComponent;
	}

	/**
	 * Sets the focused component.
	 * 
	 * @param focusedComponent
	 *            The component to focus, or <tt>null</tt> to clear the focus.
	 */
	private static void setFocusedComponent(Component focusedComponent) {
		Component previousFocusedComponent = Component.focusedComponent;

		if (previousFocusedComponent != focusedComponent) {
			Component.focusedComponent = focusedComponent;

			if (previousFocusedComponent != null) {
				previousFocusedComponent.setFocused(false, focusedComponent);
			}

			if (focusedComponent != null) {
				focusedComponent.setFocused(true, previousFocusedComponent);
			}

			componentClassListeners
					.focusedComponentChanged(previousFocusedComponent);
		}
	}

	/**
	 * Clears the focus.
	 */
	public static void clearFocus() {
		setFocusedComponent(null);
	}

	/**
	 * Copies bound values from the bind context to the component. This
	 * functionality must be provided by the subclass; the base implementation
	 * is a no-op.
	 * 
	 * @param context
	 */
	public void load(Object context) {
	}

	/**
	 * Copies bound values from the component to the bind context. This
	 * functionality must be provided by the subclass; the base implementation
	 * is a no-op.
	 * 
	 * @param context
	 */
	public void store(Object context) {
	}

	/**
	 * Clears any bound values in the component.
	 */
	public void clear() {
	}

	public DragSource getDragSource() {
		return dragSource;
	}

	public void setDragSource(DragSource dragSource) {
		DragSource previousDragSource = this.dragSource;

		if (previousDragSource != dragSource) {
			this.dragSource = dragSource;
			componentListeners.dragSourceChanged(this, previousDragSource);
		}
	}

	public DropTarget getDropTarget() {
		return dropTarget;
	}

	public void setDropTarget(DropTarget dropTarget) {
		DropTarget previousDropTarget = this.dropTarget;

		if (previousDropTarget != dropTarget) {
			this.dropTarget = dropTarget;
			componentListeners.dropTargetChanged(this, previousDropTarget);
		}
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public void setMenuHandler(MenuHandler menuHandler) {
		MenuHandler previousMenuHandler = this.menuHandler;

		if (previousMenuHandler != menuHandler) {
			this.menuHandler = menuHandler;
			componentListeners.menuHandlerChanged(this, previousMenuHandler);
		}
	}

	/**
	 * Returns the component's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the component's name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		String previousName = this.name;

		if (previousName != name) {
			this.name = name;
			componentListeners.nameChanged(this, previousName);
		}
	}

	/**
	 * Returns the component's style dictionary.
	 */
	public final StyleDictionary getStyles() {
		return styleDictionary;
	}

	/**
	 * Applies a set of styles.
	 * 
	 * @param styles
	 *            A map containing the styles to apply.
	 */
	public void setStyles(Map<String, ?> styles) {
		if (styles == null) {
			throw new IllegalArgumentException("styles is null.");
		}

		for (String key : styles) {
			getStyles().put(key, styles.get(key));
		}
	}

	/**
	 * Applies a set of styles.
	 * 
	 * @param styles
	 *            The styles encoded as a JSON map.
	 */
	public void setStyles(String styles) throws SerializationException {
		if (styles == null) {
			throw new IllegalArgumentException("styles is null.");
		}

		if (!GWT.isClient()) {
			jsonStyles = styles;
		} else {
			setStyles(JSONSerializer.parseMap(styles));
		}
	}

	/**
	 * Returns the typed style dictionary.
	 */
	public static Map<Class<? extends Component>, Map<String, ?>> getTypedStyles() {
		return typedStyles;
	}

	/**
	 * Returns the named style dictionary.
	 */
	public static Map<String, Map<String, ?>> getNamedStyles() {
		return namedStyles;
	}

	/**
	 * Applies a set of named styles.
	 * 
	 * @param styleName
	 */
	public void setStyleName(String styleName) {
		if (styleName == null) {
			throw new IllegalArgumentException();
		}

		Map<String, ?> styles = namedStyles.get(styleName);

		if (styles == null) {
			System.err.println("Named style \"" + styleName
					+ "\" does not exist.");
		} else {
			setStyles(styles);
		}
	}

	/**
	 * Applies a set of named styles.
	 * 
	 * @param styleNames
	 */
	public void setStyleNames(Sequence<String> styleNames) {
		if (styleNames == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0, n = styleNames.getLength(); i < n; i++) {
			setStyleName(styleNames.get(i));
		}
	}

	/**
	 * Applies a set of named styles.
	 * 
	 * @param styleNames
	 */
	public void setStyleNames(String styleNames) {
		if (styleNames == null) {
			throw new IllegalArgumentException();
		}

		String[] styleNameArray = styleNames.split(",");

		for (int i = 0; i < styleNameArray.length; i++) {
			String styleName = styleNameArray[i];
			setStyleName(styleName.trim());
		}
	}

	/**
	 * Returns the user data dictionary.
	 */
	public UserDataDictionary getUserData() {
		return userDataDictionary;
	}

	/**
	 * Gets the specified component attribute. While attributes can be used to
	 * store arbitrary data, they are intended to be used by containers to store
	 * layout-related metadata in their child components.
	 * 
	 * @param key
	 *            The attribute key
	 * 
	 * @return The attribute value, or <tt>null</tt> if no such attribute exists
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> Object getAttribute(T key) {
		Object attribute = null;

		if (attributes != null) {
			attribute = ((HashMap<T, Object>) attributes).get(key);
		}

		return attribute;
	}

	/**
	 * Sets the specified component attribute. While attributes can be used to
	 * store arbitrary data, they are intended to be used by containers to store
	 * layout-related metadata in their child components.
	 * 
	 * @param key
	 *            The attribute key
	 * 
	 * @param value
	 *            The attribute value, or <tt>null</tt> to clear the attribute
	 * 
	 * @return The previous value of the attribute, or <tt>null</tt> if the
	 *         attribute was unset
	 */
	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> Object setAttribute(T key, Object value) {
		if (attributes == null) {
			attributes = new HashMap<T, Object>();
		}

		Object previousValue;

		if (value != null) {
			previousValue = ((HashMap<T, Object>) attributes).put(key, value);
		} else {
			previousValue = ((HashMap<T, Object>) attributes).remove(key);
		}

		return previousValue;
	}

	/**
	 * If the mouse is currently over the component, causes the component to
	 * fire <tt>mouseOut()</tt> and a <tt>mouseMove()</tt> at the current mouse
	 * location.
	 * <p>
	 * This method is primarily useful when consuming container mouse motion
	 * events, since it allows a caller to reset the mouse state based on the
	 * event consumption logic.
	 */
	public void reenterMouse() {
		throw new UnsupportedOperationException();
	}

	protected boolean mouseMove(int x, int y) {
		throw new UnsupportedOperationException();
	}

	protected void mouseOver() {
		if (isEnabled()) {
			mouseLocation = new Point(-1, -1);

			componentMouseListeners.mouseOver(this);
		}
	}

	protected void mouseOut() {
		throw new UnsupportedOperationException();
	}

	protected boolean mouseDown(Mouse.Button button, int x, int y) {
		throw new UnsupportedOperationException();
	}

	protected boolean mouseUp(Mouse.Button button, int x, int y) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentMouseButtonListeners
					.mouseUp(this, button, x, y);
		}

		return consumed;
	}

	protected boolean mouseClick(Mouse.Button button, int x, int y, int count) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentMouseButtonListeners.mouseClick(this, button,
					x, y, count);
		}

		return consumed;
	}

	protected boolean mouseWheel(Mouse.ScrollType scrollType, int scrollAmount,
			int wheelRotation, int x, int y) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentMouseWheelListeners.mouseWheel(this,
					scrollType, scrollAmount, wheelRotation, x, y);
		}

		return consumed;
	}

	protected boolean keyTyped(char character) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentKeyListeners.keyTyped(this, character);

			if (!consumed && parent != null) {
				consumed = parent.keyTyped(character);
			}
		}

		return consumed;
	}

	protected boolean keyPressed(int keyCode, Keyboard.KeyLocation keyLocation) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentKeyListeners.keyPressed(this, keyCode,
					keyLocation);

			if (!consumed && parent != null) {
				consumed = parent.keyPressed(keyCode, keyLocation);
			}
		}

		return consumed;
	}

	protected boolean keyReleased(int keyCode, Keyboard.KeyLocation keyLocation) {
		boolean consumed = false;

		if (isEnabled()) {
			consumed = componentKeyListeners.keyReleased(this, keyCode,
					keyLocation);

			if (!consumed && parent != null) {
				consumed = parent.keyReleased(keyCode, keyLocation);
			}
		}

		return consumed;
	}

	@Override
	public String toString() {
		String s = this.getClass().getName();

		if (automationID != null) {
			s += "#" + automationID;
		}

		return s;
	}

	public ListenerList<ComponentListener> getComponentListeners() {
		return componentListeners;
	}

	public ListenerList<ComponentStateListener> getComponentStateListeners() {
		return componentStateListeners;
	}

	public ListenerList<ComponentDecoratorListener> getComponentDecoratorListeners() {
		return componentDecoratorListeners;
	}

	public ListenerList<ComponentStyleListener> getComponentStyleListeners() {
		return componentStyleListeners;
	}

	public ListenerList<ComponentMouseListener> getComponentMouseListeners() {
		return componentMouseListeners;
	}

	public ListenerList<ComponentMouseButtonListener> getComponentMouseButtonListeners() {
		return componentMouseButtonListeners;
	}

	public ListenerList<ComponentMouseWheelListener> getComponentMouseWheelListeners() {
		return componentMouseWheelListeners;
	}

	public ListenerList<ComponentKeyListener> getComponentKeyListeners() {
		return componentKeyListeners;
	}

	public ListenerList<ComponentTooltipListener> getComponentTooltipListeners() {
		return componentTooltipListeners;
	}

	public ListenerList<ComponentDataListener> getComponentDataListeners() {
		return componentDataListeners;
	}

	public static ListenerList<ComponentClassListener> getComponentClassListeners() {
		return componentClassListeners;
	}
}
