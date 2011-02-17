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
	}
}
