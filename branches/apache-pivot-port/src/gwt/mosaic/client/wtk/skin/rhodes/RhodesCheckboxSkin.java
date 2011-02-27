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
package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.wtk.Button;
import gwt.mosaic.client.wtk.Checkbox;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Theme;
import gwt.mosaic.client.wtk.skin.CheckboxSkin;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Terra checkbox skin.
 * <p>
 * TODO Button alignment style (vertical only).
 */
public class RhodesCheckboxSkin extends CheckboxSkin {

	public interface UI extends IsWidget {
		void setPresender(Component presender);
	}

	private Font font;
	private Color color;
	private Color disabledColor;
	private int spacing;

	private Color buttonColor;
	private Color buttonBorderColor;
	private Color buttonSelectionColor;
	private Color disabledButtonColor;
	private Color disabledButtonBorderColor;
	private Color disabledButtonSelectionColor;

	private static final int CHECKBOX_SIZE = 14;
	private static final int CHECKMARK_SIZE = 10;

	private UI ui = null;

	public RhodesCheckboxSkin() {
		Theme theme = Theme.getTheme();
		font = theme.getFont();
		color = theme.getColor(1);
		disabledColor = theme.getColor(7);
		spacing = 3;

		buttonColor = theme.getColor(4);
		buttonBorderColor = theme.getColor(7);
		buttonSelectionColor = theme.getColor(15);
		disabledButtonColor = theme.getColor(3);
		disabledButtonBorderColor = theme.getColor(7);
		disabledButtonSelectionColor = theme.getColor(7);
	}

	@Override
	public Widget getWidget() {
		if (ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-Checkbox");
		}
		return ui.asWidget();
	}

	@Override
	public int getPreferredWidth(int height) {
		Checkbox checkbox = (Checkbox) getComponent();
		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();

		int preferredWidth = CHECKBOX_SIZE;

		Object buttonData = checkbox.getButtonData();
		if (buttonData != null) {
			dataRenderer.render(buttonData, checkbox, false);
			preferredWidth += dataRenderer.getPreferredWidth(height) + spacing
					* 2;
		}

		return preferredWidth;
	}

	@Override
	public int getPreferredHeight(int width) {
		Checkbox checkbox = (Checkbox) getComponent();
		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();

		int preferredHeight = CHECKBOX_SIZE;

		Object buttonData = checkbox.getButtonData();
		if (buttonData != null) {
			if (width != -1) {
				width = Math.max(width - (CHECKBOX_SIZE + spacing), 0);
			}

			dataRenderer.render(checkbox.getButtonData(), checkbox, false);

			preferredHeight = Math.max(preferredHeight,
					dataRenderer.getPreferredHeight(width));
		}

		return preferredHeight;
	}

	@Override
	public Dimensions getPreferredSize() {
		Checkbox checkbox = (Checkbox) getComponent();
		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();

		dataRenderer.render(checkbox.getButtonData(), checkbox, false);

		int preferredWidth = CHECKBOX_SIZE;
		int preferredHeight = CHECKBOX_SIZE;

		Object buttonData = checkbox.getButtonData();
		if (buttonData != null) {
			dataRenderer.render(buttonData, checkbox, false);
			preferredWidth += dataRenderer.getPreferredWidth(-1) + spacing * 2;

			preferredHeight = Math.max(preferredHeight,
					dataRenderer.getPreferredHeight(-1));
		}

		return new Dimensions(preferredWidth, preferredHeight);
	}

	@Override
	public int getBaseline(int width, int height) {
		Checkbox checkbox = (Checkbox) getComponent();

		int baseline = -1;

		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();
		dataRenderer.render(checkbox.getButtonData(), checkbox, false);

		int clientWidth = Math.max(width - (CHECKBOX_SIZE + spacing), 0);
		baseline = dataRenderer.getBaseline(clientWidth, height);

		return baseline;
	}
	
	@Override
	public void layout() {
		super.layout();
		
		Checkbox checkbox = (Checkbox) getComponent();

		int width = getWidth();
		int height = getHeight();

		// Paint the content
		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();
		Object buttonData = checkbox.getButtonData();
		System.out.println("=========================="+buttonData);
		dataRenderer.render(buttonData, checkbox, false);
		dataRenderer.setSize(
				Math.max(width - (CHECKBOX_SIZE + spacing * 2), 0), height);
	}

	@Override
	public void paint() {
		Checkbox checkbox = (Checkbox) getComponent();
		int width = getWidth();
		int height = getHeight();

		// Paint the content
		Button.DataRenderer dataRenderer = checkbox.getDataRenderer();
		Object buttonData = checkbox.getButtonData();
		dataRenderer.render(buttonData, checkbox, false);
		dataRenderer.setSize(
				Math.max(width - (CHECKBOX_SIZE + spacing * 2), 0), height);

		dataRenderer.paint();

		// Paint the focus state
		if (checkbox.isFocused()) {
			if (buttonData == null) {
				Color focusColor = new Color(buttonSelectionColor.getRed(),
						buttonSelectionColor.getGreen(),
						buttonSelectionColor.getBlue(), 0x44);
				// graphics.setColor(focusColor);
				// graphics.fillRect(0, 0, CHECKBOX_SIZE, CHECKBOX_SIZE);
			} else {
				// BasicStroke dashStroke = new BasicStroke(1.0f,
				// BasicStroke.CAP_ROUND,
				// BasicStroke.JOIN_ROUND, 1.0f, new float[] {0.0f, 2.0f},
				// 0.0f);
				//
				// graphics.setStroke(dashStroke);
				// graphics.setColor(buttonBorderColor);
				//
				// graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				// RenderingHints.VALUE_ANTIALIAS_ON);
				//
				// Rectangle2D focusRectangle = new
				// Rectangle2D.Double(CHECKBOX_SIZE + 1, 0.5,
				// dataRenderer.getWidth() + spacing * 2 - 2,
				// dataRenderer.getHeight() - 1);
				// graphics.draw(focusRectangle);
			}
		}
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		this.font = font;
		invalidateComponent();
	}

	public final void setFont(String font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}
		setFont(Font.decode(font));
	}

	public final void setFont(Dictionary<String, ?> font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}
		setFont(new Font(font));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}

		this.color = color;
		repaintComponent();
	}

	public final void setColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}
		setColor(Color.decode(color));
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public void setDisabledColor(Color disabledColor) {
		if (disabledColor == null) {
			throw new IllegalArgumentException("disabledColor is null.");
		}

		this.disabledColor = disabledColor;
		repaintComponent();
	}

	public final void setDisabledColor(String disabledColor) {
		if (disabledColor == null) {
			throw new IllegalArgumentException("disabledColor is null.");
		}
		setDisabledColor(Color.decode(disabledColor));
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		if (spacing < 0) {
			throw new IllegalArgumentException("spacing is negative.");
		}
		this.spacing = spacing;
		invalidateComponent();
	}

	public final void setSpacing(Number spacing) {
		if (spacing == null) {
			throw new IllegalArgumentException("spacing is null.");
		}

		setSpacing(spacing.intValue());
	}
}
