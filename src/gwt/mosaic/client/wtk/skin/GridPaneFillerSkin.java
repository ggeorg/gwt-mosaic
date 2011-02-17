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


import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Grid pane filler skin.
 */
public class GridPaneFillerSkin extends ComponentSkin {
	private SimplePanel widget;

	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new SimplePanel();
			widget.setStyleName("m-GridPanelFiller");
		}
		return widget;
	}

	@Override
	public boolean isFocusable() {
		return false;
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
		// No-op
	}
}
