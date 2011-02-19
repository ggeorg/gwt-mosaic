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
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.user.client.ui.Widget;

/**
 * Display skin.
 */
public class DisplaySkin extends ContainerSkin {
	private Widget widget = null;

	public DisplaySkin() {
		super();

		setBackgroundColor(Color.LIGHT_GRAY);
	}

	@Override
	public void install(Component component) {
		if (!(component instanceof Display)) {
			throw new IllegalArgumentException(
					"DisplaySkin can only be installed on instances of Display.");
		}

		super.install(component);
	}

	@Override
	public Widget getWidget() {
		if (widget == null) {
			widget = ((Display) getComponent()).getDisplayHost().asWidget();
			widget.setStyleName("m-Display");
		}
		return widget;
	}

	@Override
	public void layout() {
		super.layout();

		Display display = (Display) getComponent();

		// Set all components to their preferred sizes
		for (Component component : display) {
			Window window = (Window) component;

			if (window.isVisible()) {
				if (window.isMaximized()) {
					window.setSize(display.getSize());
				} else {
					Dimensions preferredSize = window.getPreferredSize();

					if (window.getWidth() != preferredSize.getWidth()
							|| window.getHeight() != preferredSize.getHeight()) {
						window.setSize(preferredSize.getWidth(),
								preferredSize.getHeight());
					}
				}
			}
		}
	}

}
