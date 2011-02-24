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

import gwt.mosaic.client.wtk.Checkbox;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Keyboard;
import gwt.mosaic.client.wtk.Keyboard.KeyCode;
import gwt.mosaic.client.wtk.Mouse;

import com.google.gwt.event.dom.client.KeyCodes;

/**
 * Abstract base class for checkbox skins.
 */
public abstract class CheckboxSkin extends ButtonSkin {

	@Override
	public boolean mouseClick(Component component, Mouse.Button button, int x,
			int y, int count) {
		boolean consumed = super.mouseClick(component, button, x, y, count);

		Checkbox checkbox = (Checkbox) getComponent();

		checkbox.requestFocus();
		checkbox.press();

		return consumed;
	}

	/**
	 * {@link KeyCode#SPACE SPACE} 'presses' the button.
	 */
	@Override
	public boolean keyReleased(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		boolean consumed = false;

		Checkbox checkbox = (Checkbox) getComponent();

		if (keyCode == KeyCodes.KEY_ENTER /* XXX Keyboard.KeyCode.SPACE */) {
			checkbox.press();
		} else {
			consumed = super.keyReleased(component, keyCode, keyLocation);
		}

		return consumed;
	}

}
