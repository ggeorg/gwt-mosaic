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

import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Container;
import gwt.mosaic.client.wtk.ContainerListener;
import gwt.mosaic.client.wtk.ContainerMouseListener;
import gwt.mosaic.client.wtk.FocusTraversalDirection;
import gwt.mosaic.client.wtk.FocusTraversalPolicy;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for container skins.
 */
public abstract class ContainerSkin extends ComponentSkin implements
		ContainerListener, ContainerMouseListener {
	/**
	 * Focus traversal policy that determines traversal order based on the order
	 * of components in the container's component sequence.
	 */
	public static class IndexFocusTraversalPolicy implements
			FocusTraversalPolicy {
		private boolean wrap;

		public IndexFocusTraversalPolicy() {
			this(false);
		}

		public IndexFocusTraversalPolicy(boolean wrap) {
			this.wrap = wrap;
		}

		@Override
		public Component getNextComponent(Container container,
				Component component, FocusTraversalDirection direction) {
			if (container == null) {
				throw new IllegalArgumentException("container is null.");
			}

			if (direction == null) {
				throw new IllegalArgumentException("direction is null.");
			}

			Component nextComponent = null;

			int n = container.getLength();
			if (n > 0) {
				switch (direction) {
				case FORWARD: {
					if (component == null) {
						// Return the first component in the sequence
						nextComponent = container.get(0);
					} else {
						// Return the next component in the sequence
						int index = container.indexOf(component);
						if (index == -1) {
							throw new IllegalArgumentException();
						}

						if (index < n - 1) {
							nextComponent = container.get(index + 1);
						} else {
							if (wrap) {
								nextComponent = container.get(0);
							}
						}
					}

					break;
				}

				case BACKWARD: {
					if (component == null) {
						// Return the last component in the sequence
						nextComponent = container.get(n - 1);
					} else {
						// Return the previous component in the sequence
						int index = container.indexOf(component);
						if (index == -1) {
							throw new IllegalArgumentException();
						}

						if (index > 0) {
							nextComponent = container.get(index - 1);
						} else {
							if (wrap) {
								nextComponent = container.get(n - 1);
							}
						}
					}

					break;
				}
				}
			}

			return nextComponent;
		}
	}

	protected interface HasBackgroundColor {
		public void setBackgroundColor(Color backgroundColor);
	}

	private Color backgroundColor = null;
	private boolean backgroundColorChanged = false;

	private static FocusTraversalPolicy DEFAULT_FOCUS_TRAVERSAL_POLICY = new IndexFocusTraversalPolicy();

	@Override
	public void install(Component component) {
		super.install(component);

		Container container = (Container) component;

		// Add this as a container listener
		container.getContainerListeners().add(this);
		container.getContainerMouseListeners().add(this);

		// Set the focus traversal policy
		container.setFocusTraversalPolicy(DEFAULT_FOCUS_TRAVERSAL_POLICY);
	}

	@Override
	public int getPreferredWidth(int height) {
		return 0;
	}

	@Override
	public int getPreferredHeight(int width) {
		return 0;
	}

	@Override
	public void paint(Widget context) {
		if (backgroundColorChanged) {
			if (asWidget() instanceof HasBackgroundColor) {
				((HasBackgroundColor) asWidget())
						.setBackgroundColor(backgroundColor);
				backgroundColorChanged = false;
			} else {
				if (backgroundColor != null) {
					backgroundColor.applyTo(asWidget().getElement(), true);
				} else {
					Style style = asWidget().getElement().getStyle();
					style.setBackgroundColor("transparent");
				}
			}
		}
	}

	/**
	 * @return <tt>false</tt>; by default, containers are not focusable.
	 */
	@Override
	public boolean isFocusable() {
		return false;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.backgroundColorChanged = true;
		repaintComponent();
	}

	// Container events
	@Override
	public void componentInserted(Container container, int index) {
	}

	@Override
	public void componentsRemoved(Container container, int index,
			Sequence<Component> removed) {
	}

	@Override
	public void componentMoved(Container container, int from, int to) {
	}

	@Override
	public void focusTraversalPolicyChanged(Container container,
			FocusTraversalPolicy previousFocusTraversalPolicy) {
		// No-op
	}

	@Override
	public boolean mouseMove(Container container, int x, int y) {
		return false;
	}

	@Override
	public boolean mouseDown(Container container, Mouse.Button button, int x,
			int y) {
		return false;
	}

	@Override
	public boolean mouseUp(Container container, Mouse.Button button, int x,
			int y) {
		return false;
	}

	@Override
	public boolean mouseWheel(Container container, Mouse.ScrollType scrollType,
			int scrollAmount, int wheelRotation, int x, int y) {
		return false;
	}
}
