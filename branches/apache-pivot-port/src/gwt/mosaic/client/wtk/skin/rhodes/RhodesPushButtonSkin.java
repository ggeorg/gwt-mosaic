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
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.Theme;
import gwt.mosaic.client.wtk.skin.PushButtonSkin;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Terra push button skin.
 */
public class RhodesPushButtonSkin extends PushButtonSkin {

	public interface UI extends IsWidget {
		void setPresender(Component presender);

		void setBorderColor(Color borderColor);

		void setBackgroundColor(Color backgroundColor);

		void setInnerHTML(String string);

		void setPadding(Insets padding);
	}

	private Font font;
	private Color color;
	private Color disabledColor;

	private Color backgroundColor;
	private boolean backgroundColorChanged = false;

	private Color disabledBackgroundColor;

	private Color borderColor;
	private boolean borderColorChanged = false;

	private Color disabledBorderColor;

	private Insets padding;
	private boolean paddingChanged = false;

	private float minimumAspectRatio;
	private float maximumAspectRatio;
	private boolean toolbar;

	private Color bevelColor;
	private Color pressedBevelColor;
	private Color disabledBevelColor;

	private static final int CORNER_RADIUS = 4;
	private static final int BORDER_WIDTH = 1;

	private UI ui = null;

	public RhodesPushButtonSkin() {
		Theme theme = Theme.getTheme();

		setFont(new Font(FontStyle.NORMAL, FontWeight.BOLD, 11, "Verdana"));

		setColor(new Color(0x00, 0x00, 0x00));
		setDisabledColor(new Color(0x99, 0x99, 0x99));
		setBackgroundColor(new Color(0xdd, 0xdc, 0xd5));
		setDisabledBackgroundColor(new Color(0xdd, 0xdc, 0xd5));
		setBorderColor(new Color(0x99, 0x99, 0x99));
		setDisabledBorderColor(new Color(0x99, 0x99, 0x99));
		setPadding(new Insets(2, 5, 2, 5));
		minimumAspectRatio = Float.NaN;
		maximumAspectRatio = Float.NaN;
		toolbar = false;

		// Set the derived colors
		bevelColor = backgroundColor.brighter(0.1);// TerraTheme.brighten(backgroundColor);
		pressedBevelColor = backgroundColor.darker(0.1);// TerraTheme.darken(backgroundColor);
		disabledBevelColor = disabledBackgroundColor;
	}

	@Override
	public Widget getWidget() {
		if (ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-PushButton");
		}
		return ui.asWidget();
	}

	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth = 0;

		if (height == -1) {
			preferredWidth = getPreferredSize().getWidth();
		} else {
			PushButton pushButton = (PushButton) getComponent();
			Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

			dataRenderer.render(pushButton.getButtonData(), pushButton, false);

			// Include padding in constraint
			int contentHeight = height;
			if (contentHeight != -1) {
				contentHeight = Math.max(contentHeight
						- (padding.top + padding.bottom + 2 * BORDER_WIDTH), 0);
			}

			preferredWidth = dataRenderer.getPreferredWidth(contentHeight)
					+ padding.left + padding.right + 2 * BORDER_WIDTH;

			// Adjust for preferred aspect ratio
			if (!Float.isNaN(minimumAspectRatio)
					&& (float) preferredWidth / (float) height < minimumAspectRatio) {
				preferredWidth = (int) (height * minimumAspectRatio);
			}
		}

		return preferredWidth;
	}

	@Override
	public int getPreferredHeight(int width) {
		int preferredHeight = 0;

		if (width == -1) {
			preferredHeight = getPreferredSize().getHeight();
		} else {
			PushButton pushButton = (PushButton) getComponent();
			Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

			dataRenderer.render(pushButton.getButtonData(), pushButton, false);

			// Include padding in constraint
			int contentWidth = width;
			if (contentWidth != -1) {
				contentWidth = Math.max(contentWidth
						- (padding.left + padding.right + 2 * BORDER_WIDTH), 0);
			}

			preferredHeight = dataRenderer.getPreferredHeight(contentWidth)
					+ padding.top + padding.bottom + 2 * BORDER_WIDTH;

			// Adjust for preferred aspect ratio
			if (!Float.isNaN(maximumAspectRatio)
					&& (float) width / (float) preferredHeight > maximumAspectRatio) {
				preferredHeight = (int) (width / maximumAspectRatio);
			}
		}

		return preferredHeight;
	}

	@Override
	public Dimensions getPreferredSize() {
		PushButton pushButton = (PushButton) getComponent();
		Button.DataRenderer dataRenderer = pushButton.getDataRenderer();

		dataRenderer.render(pushButton.getButtonData(), pushButton, false);

		Dimensions preferredContentSize = dataRenderer.getPreferredSize();

		int preferredWidth = preferredContentSize.getWidth() + padding.left
				+ padding.right + 2 * BORDER_WIDTH;

		int preferredHeight = preferredContentSize.getHeight() + padding.top
				+ padding.bottom + 2 * BORDER_WIDTH;

		// Adjust for preferred aspect ratio
		float aspectRatio = (float) preferredWidth / (float) preferredHeight;

		if (!Float.isNaN(minimumAspectRatio)
				&& aspectRatio < minimumAspectRatio) {
			preferredWidth = (int) (preferredHeight * minimumAspectRatio);
		}

		if (!Float.isNaN(maximumAspectRatio)
				&& aspectRatio > maximumAspectRatio) {
			preferredHeight = (int) (preferredWidth / maximumAspectRatio);
		}

		return new Dimensions(preferredWidth, preferredHeight);
	}

	@Override
	public int getBaseline(int width, int height) {
		PushButton pushButton = (PushButton) getComponent();

		Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
		dataRenderer.render(pushButton.getButtonData(), pushButton, false);

		int clientWidth = Math.max(width - (padding.left + padding.right + 2),
				0);
		int clientHeight = Math.max(
				height - (padding.top + padding.bottom + 2), 0);

		int baseline = dataRenderer.getBaseline(clientWidth, clientHeight);

		if (baseline != -1) {
			baseline += padding.top + 1;
		}

		return baseline;
	}

//	@Override
//	public void layout() {
//		super.layout();
//
//		PushButton pushButton = (PushButton) getComponent();
//
//		int width = getWidth();
//		int height = getHeight();
//
//		Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
//		dataRenderer
//				.render(pushButton.getButtonData(), pushButton, highlighted);
//		dataRenderer
//				.setSize(
//						Math.max(
//								width
//										- (padding.left + padding.right + 2 * BORDER_WIDTH),
//								0),
//						Math.max(
//								height
//										- (padding.top + padding.bottom + 2 * BORDER_WIDTH),
//								0));
//		dataRenderer.paint();
//
//		ui.setInnerHTML(dataRenderer.toString());
//	}

	@Override
	public void paint() {
		PushButton pushButton = (PushButton) getComponent();

		int width = getWidth();
		int height = getHeight();

		if (!toolbar || highlighted || pushButton.isEnabled()) {
			if (pushButton.isEnabled()) {
				backgroundColor = this.backgroundColor;
				bevelColor = (pressed || pushButton.isSelected()) ? pressedBevelColor
						: this.bevelColor;
				borderColor = this.borderColor;
			} else {
				backgroundColor = disabledBackgroundColor;
				bevelColor = disabledBevelColor;
				borderColor = disabledBorderColor;
			}
		}

		// PushButtonWidget pushButtonWidget = (PushButtonWidget) getWidget();

		// Paint the border
		if (borderColorChanged) {
			ui.setBorderColor(borderColor);
			borderColorChanged = false;
		}

		// Paint the background
		if (backgroundColorChanged) {
			ui.setBackgroundColor(backgroundColor);
			backgroundColorChanged = false;
		}

		if (paddingChanged) {
			ui.setPadding(padding);
			paddingChanged = false;
		}

		// Paint the content

		final Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
		dataRenderer
				.render(pushButton.getButtonData(), pushButton, highlighted);
		
		System.out.println("--------------------------------------------- "+width+"x"+height);
		
		dataRenderer
				.setSize(
						Math.max(
								width
										- (padding.left + padding.right + 2 * BORDER_WIDTH),
								0),
						Math.max(
								height
										- (padding.top + padding.bottom + 2 * BORDER_WIDTH),
								0));
		dataRenderer.paint();
		
		ui.setInnerHTML(dataRenderer.toString());

		// // Paint the focus state
		// if (pushButton.isFocused()
		// && !toolbar) {
		// BasicStroke dashStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
		// BasicStroke.JOIN_ROUND, 1.0f, new float[] {0.0f, 2.0f}, 0.0f);
		// graphics.setStroke(dashStroke);
		// graphics.setColor(this.borderColor);
		// graphics.draw(new RoundRectangle2D.Double(2.5, 2.5, Math.max(width -
		// 5,
		// 0),
		// Math.max(height - 5, 0), CORNER_RADIUS / 2, CORNER_RADIUS / 2));
		// }

	}

	// @Override
	// public void paint(Graphics2D graphics) {
	// PushButton pushButton = (PushButton) getComponent();
	//
	// int width = getWidth();
	// int height = getHeight();
	//
	// Color backgroundColor = null;
	// Color bevelColor = null;
	// Color borderColor = null;
	//
	// if (!toolbar
	// || highlighted
	// || pushButton.isFocused()) {
	// if (pushButton.isEnabled()) {
	// backgroundColor = this.backgroundColor;
	// bevelColor = (pressed
	// || pushButton.isSelected()) ? pressedBevelColor : this.bevelColor;
	// borderColor = this.borderColor;
	// } else {
	// backgroundColor = disabledBackgroundColor;
	// bevelColor = disabledBevelColor;
	// borderColor = disabledBorderColor;
	// }
	// }
	//
	// // Paint the background
	// graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	//
	// if (backgroundColor != null
	// && bevelColor != null) {
	// graphics.setPaint(new GradientPaint(width / 2f, 0, bevelColor,
	// width / 2f, height / 2f, backgroundColor));
	// graphics.fill(new RoundRectangle2D.Double(0.5, 0.5, width - 1, height -
	// 1,
	// CORNER_RADIUS, CORNER_RADIUS));
	// }
	//
	// // Paint the content
	// graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_OFF);
	//
	// Button.DataRenderer dataRenderer = pushButton.getDataRenderer();
	// dataRenderer.render(pushButton.getButtonData(), pushButton, highlighted);
	// dataRenderer.setSize(Math.max(width - (padding.left + padding.right + 2),
	// 0),
	// Math.max(getHeight() - (padding.top + padding.bottom + 2), 0));
	//
	// Graphics2D contentGraphics = (Graphics2D) graphics.create();
	// contentGraphics.translate(padding.left + 1, padding.top + 1);
	// contentGraphics.clipRect(0, 0, dataRenderer.getWidth(),
	// dataRenderer.getHeight());
	// dataRenderer.paint(contentGraphics);
	// contentGraphics.dispose();
	//
	// graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	//
	// // Paint the border
	// if (borderColor != null) {
	// graphics.setPaint(borderColor);
	// graphics.setStroke(new BasicStroke(1));
	// graphics.draw(new RoundRectangle2D.Double(0.5, 0.5, width - 1, height -
	// 1,
	// CORNER_RADIUS, CORNER_RADIUS));
	// }
	//
	// // Paint the focus state
	// if (pushButton.isFocused()
	// && !toolbar) {
	// BasicStroke dashStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
	// BasicStroke.JOIN_ROUND, 1.0f, new float[] {0.0f, 2.0f}, 0.0f);
	// graphics.setStroke(dashStroke);
	// graphics.setColor(this.borderColor);
	// graphics.draw(new RoundRectangle2D.Double(2.5, 2.5, Math.max(width - 5,
	// 0),
	// Math.max(height - 5, 0), CORNER_RADIUS / 2, CORNER_RADIUS / 2));
	// }
	// }

	@Override
	public boolean isFocusable() {
		return true;
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

	public final void setColor(int color) {
		setColor(Theme.getTheme().getColor(color));
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

	public final void setDisabledColor(int disabledColor) {
		setDisabledColor(Theme.getTheme().getColor(disabledColor));
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}

		if (!backgroundColor.equals(this.backgroundColor)) {
			this.backgroundColor = backgroundColor;
			this.backgroundColorChanged = true;
			// TODO bevelColor = TerraTheme.brighten(backgroundColor);
			// TODO pressedBevelColor = TerraTheme.darken(backgroundColor);
			repaintComponent();
		}
	}

	public final void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}
		setBackgroundColor(Color.decode(backgroundColor));
	}

	public final void setBackgroundColor(int backgroundColor) {
		setBackgroundColor(Theme.getTheme().getColor(backgroundColor));
	}

	public Color getDisabledBackgroundColor() {
		return disabledBackgroundColor;
	}

	public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
		if (disabledBackgroundColor == null) {
			throw new IllegalArgumentException(
					"disabledBackgroundColor is null.");
		}

		this.disabledBackgroundColor = disabledBackgroundColor;
		disabledBevelColor = disabledBackgroundColor;
		repaintComponent();
	}

	public final void setDisabledBackgroundColor(String disabledBackgroundColor) {
		if (disabledBackgroundColor == null) {
			throw new IllegalArgumentException(
					"disabledBackgroundColor is null.");
		}
		setDisabledBackgroundColor(Color.decode(disabledBackgroundColor));
	}

	public final void setDisabledBackgroundColor(int disabledBackgroundColor) {
		setDisabledBackgroundColor(Theme.getTheme().getColor(
				disabledBackgroundColor));
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		if (borderColor == null) {
			throw new IllegalArgumentException("borderColor is null.");
		}

		if (!borderColor.equals(this.borderColor)) {
			this.borderColor = borderColor;
			this.borderColorChanged = true;
			repaintComponent();
		}
	}

	public final void setBorderColor(String borderColor) {
		if (borderColor == null) {
			throw new IllegalArgumentException("borderColor is null.");
		}
		setBorderColor(Color.decode(borderColor));
	}

	public final void setBorderColor(int borderColor) {
		setBorderColor(Theme.getTheme().getColor(borderColor));
	}

	public Color getDisabledBorderColor() {
		return disabledBorderColor;
	}

	public void setDisabledBorderColor(Color disabledBorderColor) {
		if (disabledBorderColor == null) {
			throw new IllegalArgumentException("disabledBorderColor is null.");
		}

		this.disabledBorderColor = disabledBorderColor;
		repaintComponent();
	}

	public final void setDisabledBorderColor(String disabledBorderColor) {
		if (disabledBorderColor == null) {
			throw new IllegalArgumentException("disabledBorderColor is null.");
		}
		setDisabledBorderColor(Color.decode(disabledBorderColor));
	}

	public final void setDisabledBorderColor(int disabledBorderColor) {
		setDisabledBorderColor(Theme.getTheme().getColor(disabledBorderColor));
	}

	public Insets getPadding() {
		return padding;
	}

	public void setPadding(Insets padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		if (!padding.equals(this.padding)) {
			this.padding = padding;
			this.paddingChanged = true;
			invalidateComponent();
		}
	}

	public final void setPadding(Dictionary<String, ?> padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		setPadding(new Insets(padding));
	}

	public final void setPadding(int padding) {
		setPadding(new Insets(padding));
	}

	public final void setPadding(Number padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		setPadding(padding.intValue());
	}

	public final void setPadding(String padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		setPadding(Insets.decode(padding));
	}

	public float getMinimumAspectRatio() {
		return minimumAspectRatio;
	}

	public void setMinimumAspectRatio(float minimumAspectRatio) {
		if (!Float.isNaN(maximumAspectRatio)
				&& minimumAspectRatio > maximumAspectRatio) {
			throw new IllegalArgumentException(
					"minimumAspectRatio is greater than maximumAspectRatio.");
		}

		this.minimumAspectRatio = minimumAspectRatio;
		invalidateComponent();
	}

	public final void setMinimumAspectRatio(Number minimumAspectRatio) {
		if (minimumAspectRatio == null) {
			throw new IllegalArgumentException("minimumAspectRatio is null.");
		}

		setMinimumAspectRatio(minimumAspectRatio.floatValue());
	}

	public float getMaximumAspectRatio() {
		return maximumAspectRatio;
	}

	public void setMaximumAspectRatio(float maximumAspectRatio) {
		if (!Float.isNaN(minimumAspectRatio)
				&& maximumAspectRatio < minimumAspectRatio) {
			throw new IllegalArgumentException(
					"maximumAspectRatio is less than minimumAspectRatio.");
		}

		this.maximumAspectRatio = maximumAspectRatio;
		invalidateComponent();
	}

	public final void setMaximumAspectRatio(Number maximumAspectRatio) {
		if (maximumAspectRatio == null) {
			throw new IllegalArgumentException("maximumAspectRatio is null.");
		}

		setMaximumAspectRatio(maximumAspectRatio.floatValue());
	}

	public boolean isToolbar() {
		return toolbar;
	}

	public void setToolbar(boolean toolbar) {
		this.toolbar = toolbar;

		if (toolbar && getComponent().isFocused()) {
			Component.clearFocus();
		}

		repaintComponent();
	}

	@Override
	public void mouseOut(Component component) {
		super.mouseOut(component);

		if (toolbar && component.isFocused()) {
			Component.clearFocus();
		}
	}

	@Override
	public boolean mouseClick(Component component, Mouse.Button button, int x,
			int y, int count) {
		if (!toolbar) {
			component.requestFocus();
		}

		return super.mouseClick(component, button, x, y, count);
	}
}
