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
package gwt.mosaic.client.wtk.content;

import gwt.mosaic.client.collections.HashSet;
import gwt.mosaic.client.wtk.ApplicationContext;
import gwt.mosaic.client.wtk.BoxPane;
import gwt.mosaic.client.wtk.Button;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.HorizontalAlignment;
import gwt.mosaic.client.wtk.ImageView;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.media.Image;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Default button data renderer.
 */
@SuppressWarnings("serial")
public class ButtonDataRenderer extends BoxPane implements Button.DataRenderer {
	protected ImageView imageView = new ImageView();
	protected Label label = new Label();

	public ButtonDataRenderer() {
		getStyles().put("horizontalAlignment", HorizontalAlignment.CENTER);
		getStyles().put("verticalAlignment", VerticalAlignment.CENTER);

		add(imageView);
		add(label);

		// imageView.getStyles().put("backgroundColor", null);

		// attach
		HasWidgets rendererContext = ApplicationContext.getRendererContext();
		rendererContext.add(getSkin().getWidget());
	}

	@Override
	public void setSize(int width, int height) {
		System.out.println("SET SIZE " + width + "x" + height);
		
		super.setSize(width, height);

		// Since this component doesn't have a parent, it won't be validated
		// via layout; ensure that it is valid here
		validate();
	}

	@Override
	public void render(Object data, Button button, boolean highlighted) {
		
		System.out.println("RENDER " +data);
		
		Image icon = null;
		String text = null;

		if (data instanceof ButtonData) {
			ButtonData buttonData = (ButtonData) data;
			icon = buttonData.getIcon();
			text = buttonData.getText();
		} else if (data instanceof Image) {
			icon = (Image) data;
		} else {
			if (data != null) {
				text = data.toString();
			}
		}

		// Update the image view
		if (icon == null) {
			imageView.setVisible(false);
		} else {
			imageView.setVisible(true);
			imageView.setImage(icon);

			imageView.getStyles().put("opacity",
					button.isEnabled() ? 1.0f : 0.5f);
		}

		// Update the label
		label.setText(text);

		if (text == null) {
			label.setVisible(false);
		} else {
			label.setVisible(true);

			Font font = (Font) button.getStyles().get("font");
			label.getStyles().put("font", font);

			Color color;
			if (button.isEnabled()) {
				color = (Color) button.getStyles().get("color");
			} else {
				color = (Color) button.getStyles().get("disabledColor");
			}

			label.getStyles().put("color", color);
		}
	}

	public int getIconWidth() {
		return imageView.getPreferredWidth(-1);
	}

	public void setIconWidth(int iconWidth) {
		imageView.setPreferredWidth(iconWidth);
	}

	public int getIconHeight() {
		return imageView.getPreferredHeight(-1);
	}

	public void setIconHeight(int iconHeight) {
		imageView.setPreferredHeight(iconHeight);
	}

	public boolean getShowIcon() {
		return imageView.isVisible();
	}

	public void setShowIcon(boolean showIcon) {
		imageView.setVisible(showIcon);
	}

	public boolean getFillIcon() {
		return (Boolean) imageView.getStyles().get("fill");
	}

	public void setFillIcon(boolean fillIcon) {
		imageView.getStyles().put("fill", fillIcon);
	}

	@Override
	public void repaint(Component component, boolean immediate) {
		component.paint();
	}

	@Override
	public String toString(Object data) {
		String string = null;

		if (data instanceof ButtonData) {
			ButtonData buttonData = (ButtonData) data;
			string = buttonData.getText();
		} else {
			if (data != null) {
				string = data.toString();
			}
		}

		return string;
	}

	@Override
	public String toString() {
		System.out.println("TO STRING");
		
		return getSkin().getWidget().toString();
	}
}
