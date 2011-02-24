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

import java.util.Iterator;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for application contexts.
 */
public class ApplicationContext {
	/**
	 * Native display host.
	 */
	public static class DisplayHost implements IsWidget, HasWidgets {
		// private boolean paintPending = false;
		// private boolean debugPaint = false; // system property

		private final AbsolutePanel panel;
		private final Display display;

		public DisplayHost(AbsolutePanel panel) {
			this.panel = panel;
			
			panel.add(ApplicationContext.rendererContext, Short.MIN_VALUE, Short.MIN_VALUE);

			display = new Display(this);
		}

		@Override
		public Widget asWidget() {
			return panel;
		}

		public Display getDisplay() {
			return display;
		}

		@Override
		public void add(Widget w) {
			panel.add(w);
		}

		@Override
		public void clear() {
			panel.clear();
		}

		@Override
		public Iterator<Widget> iterator() {
			return panel.iterator();
		}

		@Override
		public boolean remove(Widget w) {
			return panel.remove(w);
		}

		public void processKeyDownEvent(KeyDownEvent event) {
			Component focusedComponent = Component.getFocusedComponent();

			boolean consumed = false;

			int keyCode = event.getNativeKeyCode();

			// Get the key location
			Keyboard.KeyLocation keyLocation = Keyboard.KeyLocation.STANDARD;
			try {
				if (!focusedComponent.isBlocked()) {
					consumed = focusedComponent
							.keyPressed(keyCode, keyLocation);
				}
			} catch (Exception exception) {
				handleUncaughtException(exception);
			}

			if (consumed) {
				event.preventDefault();
				event.stopPropagation();
			}
		}

		public void processKeyUpEvent(KeyUpEvent event) {
			Component focusedComponent = Component.getFocusedComponent();

			boolean consumed = false;

			int keyCode = event.getNativeKeyCode();

			// Get the key location
			Keyboard.KeyLocation keyLocation = Keyboard.KeyLocation.STANDARD;
			// TODO if (dragDescendant == null) {
			try {
				if (focusedComponent != null) {
					if (!focusedComponent.isBlocked()) {
						consumed = focusedComponent.keyReleased(keyCode,
								keyLocation);
					}
				}
			} catch (Exception exception) {
				handleUncaughtException(exception);
			}

			if (consumed) {
				event.preventDefault();
				event.stopPropagation();
			}
		}

		public void processKeyPressEvent(KeyPressEvent event) {
			Component focusedComponent = Component.getFocusedComponent();

			boolean consumed = false;

			char keyChar = event.getCharCode();

			try {
				if (focusedComponent != null) {
					if (!focusedComponent.isBlocked()) {
						consumed = focusedComponent.keyTyped(keyChar);
					}
				}
			} catch (Exception exception) {
				handleUncaughtException(exception);
			}

			if (consumed) {
				event.stopPropagation();
			}
		}
	}
	
	private static final AbsolutePanel rendererContext = new AbsolutePanel();
	
	public static AbsolutePanel getRendererContext() {
		return rendererContext;
	}

	private static void handleUncaughtException(Exception exception) {
		throw new UnsupportedOperationException("TODO", exception);
	}
}
