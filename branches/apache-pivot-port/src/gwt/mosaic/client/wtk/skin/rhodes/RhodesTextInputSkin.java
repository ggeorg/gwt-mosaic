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
import gwt.mosaic.client.text.CharSequenceCharacterIterator;
import gwt.mosaic.client.util.Vote;
import gwt.mosaic.client.wtk.ApplicationContext;
import gwt.mosaic.client.wtk.Bounds;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Cursor;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.FocusTraversalDirection;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Keyboard;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.Orientation;
import gwt.mosaic.client.wtk.TextInput;
import gwt.mosaic.client.wtk.TextInputContentListener;
import gwt.mosaic.client.wtk.TextInputListener;
import gwt.mosaic.client.wtk.TextInputSelectionListener;
import gwt.mosaic.client.wtk.Theme;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.skin.ComponentSkin;
import gwt.mosaic.client.wtk.style.Color;
import gwt.mosaic.client.wtk.validator.Validator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Text input skin.
 */
public class RhodesTextInputSkin extends ComponentSkin implements
		TextInput.Skin, TextInputListener, TextInputContentListener,
		TextInputSelectionListener {

	public interface UI extends IsWidget, HasValue<String> {
		void setPresender(Component component);

		void setFont(Font font);

		void setColor(Color color);

		void setBackgroundColor(Color backgroundColor);

		void setPromptColor(Color color);

		void setPrompt(String prompt);

		void setBorderColor(Color borderColor);
	}

	private int anchor = -1;

	private int scrollLeft = 0;

	private boolean caretOn = true;

	private FocusTraversalDirection scrollDirection = null;

	private Font font;
	private boolean fontChanged;

	private Color color;
	private boolean colorChanged;

	private Color disabledColor;
	private boolean disabledColorChanged;

	private Color promptColor;
	private boolean promptColorChanged;

	private Color backgroundColor;
	private boolean backgroundColorChanged;

	private Color disabledBackgroundColor;
	private boolean disabledBackgroundColorChanged;

	private Color invalidColor;
	private boolean invalidColorChanged;

	private Color invalidBackgroundColor;
	private boolean invalidBackgroundColorChanged;

	private Color borderColor;
	private boolean borderColorChanged;

	private Color disabledBorderColor;
	private boolean disabledBorderColorChanged;

	private Color selectionColor;
	private Color selectionBackgroundColor;
	private Color inactiveSelectionColor;
	private Color inactiveSelectionBackgroundColor;

	private Color bevelColor;
	private Color disabledBevelColor;
	private Color invalidBevelColor;

	private Insets padding;

	private Dimensions averageCharacterSize;

	private static final int SCROLL_RATE = 50;
	private static final char BULLET = 0x2022;

	// Changed by XXX listener
	private boolean promptChanged = true;
	private boolean textChanged = true;

	private UI ui = null;

	public RhodesTextInputSkin() {
		RhodesTheme theme = (RhodesTheme) Theme.getTheme();

		font = new Font(); // setFont(theme.getFont());
		fontChanged = true;

		color = theme.getColor(Theme.WINDOW_TEXT_COLOR);
		colorChanged = true;

		promptColor = theme.getColor(Theme.GRAY_TEXT_COLOR);
		promptColorChanged = true;

		disabledColor = theme.getColor(Theme.GRAY_TEXT_COLOR);
		disabledColorChanged = true;

		backgroundColor = theme.getColor(Theme.WINDOW_COLOR);
		backgroundColorChanged = true;

		disabledBackgroundColor = theme.getColor(Theme.THREE_D_FACE_COLOR);
		disabledBackgroundColorChanged = true;

		invalidColor = theme.getColor(Theme.GRAY_TEXT_COLOR);
		invalidColorChanged = true;

		invalidBackgroundColor = Color.PINK;

		// borderColor = theme.getColor(7);
		// disabledBorderColor = theme.getColor(7);
		padding = new Insets(2);

		// selectionColor = theme.getColor(4);
		// selectionBackgroundColor = theme.getColor(14);
		// inactiveSelectionColor = theme.getColor(1);
		// inactiveSelectionBackgroundColor = theme.getColor(9);

		// bevelColor = TerraTheme.darken(backgroundColor);
		// disabledBevelColor = disabledBackgroundColor;
		// invalidBevelColor = TerraTheme.darken(invalidBackgroundColor);
	}

	@Override
	public void install(Component component) {
		super.install(component);

		TextInput textInput = (TextInput) component;
		textInput.getTextInputListeners().add(this);
		textInput.getTextInputContentListeners().add(this);
		textInput.getTextInputSelectionListeners().add(this);

		// textInput.setCursor(Cursor.TEXT);

		updateSelection();
	}

	@Override
	public Widget getWidget() {
		if (ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-TextInput");
		}
		return ui.asWidget();
	}

	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth;
		if (getWidget().isAttached()) {
			Element elem = getWidget().getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				DOM.setStyleAttribute(elem, "width", "");
				DOM.setStyleAttribute(elem, "height", (height < 0) ? ""
						: (height + Unit.PX.getType()));
				preferredWidth = (int) Math.ceil(elem.getClientWidth());
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
			}
		} else {
			preferredWidth = 0;

			// XXX paddings are included in clientWidth!
			preferredWidth += (padding.left + padding.right);
		}

		return preferredWidth;
		// TextInput textInput = (TextInput) getComponent();
		// int textSize = textInput.getTextSize();
		//
		// return averageCharacterSize.getWidth() * textSize
		// + (padding.left + padding.right) + 2;
	}

	@Override
	public int getPreferredHeight(int width) {
		int preferredHeight;
		if (getWidget().isAttached()) {
			Element elem = getWidget().getElement();
			String oldPosition = DOM.getStyleAttribute(elem, "position");
			try {
				DOM.setStyleAttribute(elem, "position", "static");
				DOM.setStyleAttribute(elem, "width", "");
				DOM.setStyleAttribute(elem, "height", (width < 0) ? ""
						: (width + Unit.PX.getType()));
				preferredHeight = (int) Math.ceil(elem.getClientHeight());
			} finally {
				DOM.setStyleAttribute(elem, "position", oldPosition);
			}
		} else {
			preferredHeight = 0;

			// XXX paddings are included in clientHeight!
			preferredHeight += (padding.top + padding.bottom);
		}

		return preferredHeight;
		// return averageCharacterSize.getHeight()
		// + (padding.top + padding.bottom) + 2;
	}

	// @Override
	// public Dimensions getPreferredSize() {
	// return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	// }

	@Override
	public int getBaseline(int width, int height) {
		// FontRenderContext fontRenderContext =
		// Platform.getFontRenderContext();
		// LineMetrics lm = font.getLineMetrics("", fontRenderContext);
		// float ascent = lm.getAscent();
		// float textHeight = lm.getHeight();

		int baseline = -1;// Math.round((height - textHeight) / 2 + ascent);

		return baseline;
	}

	@Override
	public void layout() {
		super.layout();
		// No-op

		// TextInput textInput = (TextInput) getComponent();
		//
		// glyphVector = null;
		//
		// int n = textInput.getCharacterCount();
		// if (n > 0) {
		// CharSequence characters;
		// if (textInput.isPassword()) {
		// StringBuilder passwordBuilder = new StringBuilder(n);
		// for (int i = 0; i < n; i++) {
		// passwordBuilder.append(BULLET);
		// }
		//
		// characters = passwordBuilder;
		// } else {
		// characters = textInput.getCharacters();
		// }
		//
		// CharSequenceCharacterIterator ci = new CharSequenceCharacterIterator(
		// characters);
		//
		// // XXX FontRenderContext fontRenderContext =
		// // Platform.getFontRenderContext();
		// // glyphVector = font.createGlyphVector(fontRenderContext, ci);
		//
		// Rectangle2D textBounds = glyphVector.getLogicalBounds();
		// int textWidth = (int) textBounds.getWidth();
		// int width = getWidth();
		//
		// if (textWidth - scrollLeft + padding.left + 1 < width
		// - padding.right - 1) {
		// // The right edge of the text is less than the right inset;
		// // align
		// // the text's right edge with the inset
		// scrollLeft = Math.max(textWidth
		// + (padding.left + padding.right + 2) - width, 0);
		// } else {
		// // Scroll lead selection to visible
		// int selectionStart = textInput.getSelectionStart();
		// if (selectionStart <= n && textInput.isFocused()) {
		// scrollCharacterToVisible(selectionStart);
		// }
		// }
		// }
		//
		// updateSelection();
		// showCaret(textInput.isFocused() && textInput.getSelectionLength() ==
		// 0);
	}

	@Override
	public void paint() {
		TextInput textInput = (TextInput) getComponent();

		Color backgroundColor;
		Color borderColor;
		Color bevelColor;

		// Set the font
		if (fontChanged) {
			ui.setFont(font);
			fontChanged = false;
		}

		if (textInput.isEnabled()) {
			if (textInput.isTextValid()) {
				backgroundColor = this.backgroundColor;
				bevelColor = this.bevelColor;
			} else {
				backgroundColor = invalidBackgroundColor;
				bevelColor = invalidBevelColor;
			}
			borderColor = this.borderColor;
		} else {
			backgroundColor = disabledBackgroundColor;
			borderColor = disabledBorderColor;
			bevelColor = disabledBevelColor;
		}

		// Paint the background
		if (backgroundColorChanged) {
			ui.setBackgroundColor(backgroundColor);
			backgroundColorChanged = false;
		}

		// Paint the bevel
		// graphics.setColor(bevelColor);
		// GraphicsUtilities.drawLine(graphics, 0, 0, width,
		// Orientation.HORIZONTAL);

		String text = textInput.getText();
		String prompt = textInput.getPrompt();

		if (!textInput.isFocused() && (prompt != null)
				&& (text == null || text.length() == 0)) {
			// if (promptColorChanged) {
			ui.setPromptColor(promptColor);
			// promptColorChanged = false;
			// colorChanged = true;
			// }
			// if (promptChanged) {
			ui.setPrompt(prompt);
			// promptChanged = false;
			// }
		} else {
			boolean textValid = textInput.isTextValid();

			Color color;
			if (textInput.isEnabled()) {
				if (!textValid) {
					color = invalidColor;
				} else {
					color = this.color;
				}
			} else {
				color = disabledColor;
			}

			// if (colorChanged) {
			ui.setColor(color);
			// colorChanged = false;
			// promptColorChanged = true;
			// }
			// if (textChanged) {
			ui.setValue(text, false);
			// textChanged = false;
			// promptChanged = true;
			// }
		}

		// Paint the border
		if (borderColorChanged) {
			ui.setBorderColor(borderColor);
		}
		
		super.paint();
	}

	public int getInsertionPoint(int x) {
		int offset = -1;

		// if (glyphVector == null) {
		// offset = 0;
		// } else {
		// // Translate to glyph coordinates
		// x -= (padding.left - scrollLeft + 1);
		//
		// Rectangle2D textBounds = glyphVector.getLogicalBounds();
		//
		// if (x < 0) {
		// offset = 0;
		// } else if (x > textBounds.getWidth()) {
		// offset = glyphVector.getNumGlyphs();
		// } else {
		// int n = glyphVector.getNumGlyphs();
		// int i = 0;
		// while (i < n) {
		// Shape glyphBounds = glyphVector.getGlyphLogicalBounds(i);
		// Rectangle2D glyphBounds2D = glyphBounds.getBounds2D();
		//
		// float glyphX = (float) glyphBounds2D.getX();
		// float glyphWidth = (float) glyphBounds2D.getWidth();
		// if (x >= glyphX && x < glyphX + glyphWidth) {
		//
		// if (x - glyphX > glyphWidth / 2) {
		// // The user clicked on the right half of the
		// // character; select
		// // the next character
		// i++;
		// }
		//
		// offset = i;
		// break;
		// }
		//
		// i++;
		// }
		// }
		// }

		return offset;
	}

	public Bounds getCharacterBounds(int index) {
		Bounds characterBounds = null;

		// if (glyphVector != null) {
		// int x, width;
		// if (index < glyphVector.getNumGlyphs()) {
		// Shape glyphBounds = glyphVector.getGlyphLogicalBounds(index);
		// Rectangle2D glyphBounds2D = glyphBounds.getBounds2D();
		//
		// x = (int) Math.floor(glyphBounds2D.getX());
		// width = (int) Math.ceil(glyphBounds2D.getWidth());
		// } else {
		// // This is the terminator character
		// Rectangle2D glyphVectorBounds = glyphVector.getLogicalBounds();
		// x = (int) Math.floor(glyphVectorBounds.getWidth());
		// width = 0;
		// }
		//
		// characterBounds = new Bounds(x + padding.left - scrollLeft + 1,
		// padding.top + 1, width, getHeight()
		// - (padding.top + padding.bottom + 2));
		// }

		return characterBounds;
	}

	private void setScrollLeft(int scrollLeft) {
		this.scrollLeft = scrollLeft;
		updateSelection();
		repaintComponent();
	}

	private void scrollCharacterToVisible(int offset) {
		int width = getWidth();
		Bounds characterBounds = getCharacterBounds(offset);

		if (characterBounds != null) {
			int glyphX = characterBounds.x - (padding.left + 1) + scrollLeft;

			if (characterBounds.x < padding.left + 1) {
				setScrollLeft(glyphX);
			} else if (characterBounds.x + characterBounds.width > width
					- (padding.right + 1)) {
				setScrollLeft(glyphX + (padding.left + padding.right + 2)
						+ characterBounds.width - width);
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

		if (!font.equals(this.font)) {
			this.font = font;
			this.fontChanged = true;
			invalidateComponent();
		}
	}

	public final void setFont(String font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null");
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

		if (!color.equals(this.color)) {
			this.color = color;
			this.colorChanged = true;
			repaintComponent();
		}
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

	public Color getPromptColor() {
		return promptColor;
	}

	public void setPromptColor(Color promptColor) {
		if (promptColor == null) {
			throw new IllegalArgumentException("promptColor is null.");
		}

		if (!promptColor.equals(this.promptColor)) {
			this.promptColor = promptColor;
			this.promptColorChanged = true;
			repaintComponent();
		}
	}

	public final void setPromptColor(String promptColor) {
		if (promptColor == null) {
			throw new IllegalArgumentException("promptColor is null.");
		}
		setPromptColor(Color.decode(promptColor));
	}

	public final void setPromptColor(int promptColor) {
		setPromptColor(Theme.getTheme().getColor(promptColor));
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public void setDisabledColor(Color disabledColor) {
		if (disabledColor == null) {
			throw new IllegalArgumentException("disabledColor is null.");
		}

		if (disabledColor.equals(this.disabledColor)) {
			this.disabledColor = disabledColor;
			this.disabledColorChanged = true;
			repaintComponent();
		}
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

		if (backgroundColor.equals(this.backgroundColor)) {
			this.backgroundColor = backgroundColor;
			this.backgroundColorChanged = true;
			repaintComponent();
		}

		// XXX
		// bevelColor = backgroundColor.darker(0.1);
	}

	public final void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}

		throw new UnsupportedOperationException();
		// setBackgroundColor(GraphicsUtilities.decodeColor(backgroundColor));
	}

	public final void setBackgroundColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setBackgroundColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getInvalidColor() {
		return invalidColor;
	}

	public void setInvalidColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}

		if (invalidColor.equals(this.invalidColor)) {
			this.invalidColor = color;
			this.invalidColorChanged = true;
			repaintComponent();
		}
	}

	public final void setInvalidColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}
		setInvalidColor(Color.decode(color));
	}

	public final void setInvalidColor(int color) {
		setInvalidColor(Theme.getTheme().getColor(color));
	}

	public Color getInvalidBackgroundColor() {
		return invalidBackgroundColor;
	}

	public void setInvalidBackgroundColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}

		this.invalidBackgroundColor = color;
		invalidBevelColor = color.darker(0.1);// XXX TerraTheme.darken(color);
		repaintComponent();
	}

	public final void setInvalidBackgroundColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException(
					"invalidBackgroundColor is null.");
		}

		throw new UnsupportedOperationException();
		// setInvalidBackgroundColor(GraphicsUtilities.decodeColor(color));
	}

	public final void setInvalidBackgroundColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setInvalidBackgroundColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getDisabledBackgroundColor() {
		return disabledBackgroundColor;
	}

	public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
		if (disabledBackgroundColor == null) {
			throw new IllegalArgumentException(
					"disabledBackgroundColor is null.");
		}

		if (disabledBackgroundColor.equals(this.disabledBackgroundColor)) {
			this.disabledBackgroundColor = disabledBackgroundColor;
			this.disabledBackgroundColorChanged = true;
			repaintComponent();
		}

		// XXX disabledBevelColor = disabledBackgroundColor;
	}

	public final void setDisabledBackgroundColor(String disabledBackgroundColor) {
		if (disabledBackgroundColor == null) {
			throw new IllegalArgumentException(
					"disabledBackgroundColor is null.");
		}
		setDisabledBackgroundColor(Color.decode(disabledBackgroundColor));
	}

	public final void setDisabledBackgroundColor(int color) {
		setDisabledBackgroundColor(Theme.getTheme().getColor(color));
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		if (borderColor == null) {
			throw new IllegalArgumentException("borderColor is null.");
		}

		this.borderColor = borderColor;
		repaintComponent();
	}

	public final void setBorderColor(String borderColor) {
		if (borderColor == null) {
			throw new IllegalArgumentException("borderColor is null.");
		}

		throw new UnsupportedOperationException();
		// setBorderColor(GraphicsUtilities.decodeColor(borderColor));
	}

	public final void setBorderColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setBorderColor(theme.getColor(color));
		throw new UnsupportedOperationException();
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

		throw new UnsupportedOperationException();
		// setDisabledBorderColor(GraphicsUtilities.decodeColor(disabledBorderColor));
	}

	public final void setDisabledBorderColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setDisabledBorderColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getSelectionColor() {
		return selectionColor;
	}

	public void setSelectionColor(Color selectionColor) {
		if (selectionColor == null) {
			throw new IllegalArgumentException("selectionColor is null.");
		}

		this.selectionColor = selectionColor;
		repaintComponent();
	}

	public final void setSelectionColor(String selectionColor) {
		if (selectionColor == null) {
			throw new IllegalArgumentException("selectionColor is null.");
		}

		throw new UnsupportedOperationException();
		// setSelectionColor(GraphicsUtilities.decodeColor(selectionColor));
	}

	public final void setSelectionColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setSelectionColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getSelectionBackgroundColor() {
		return selectionBackgroundColor;
	}

	public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
		if (selectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"selectionBackgroundColor is null.");
		}

		this.selectionBackgroundColor = selectionBackgroundColor;
		repaintComponent();
	}

	public final void setSelectionBackgroundColor(
			String selectionBackgroundColor) {
		if (selectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"selectionBackgroundColor is null.");
		}

		throw new UnsupportedOperationException();
		// setSelectionBackgroundColor(GraphicsUtilities.decodeColor(selectionBackgroundColor));
	}

	public final void setSelectionBackgroundColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setSelectionBackgroundColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getInactiveSelectionColor() {
		return inactiveSelectionColor;
	}

	public void setInactiveSelectionColor(Color inactiveSelectionColor) {
		if (inactiveSelectionColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionColor is null.");
		}

		this.inactiveSelectionColor = inactiveSelectionColor;
		repaintComponent();
	}

	public final void setInactiveSelectionColor(String inactiveSelectionColor) {
		if (inactiveSelectionColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionColor is null.");
		}

		throw new UnsupportedOperationException();
		// setInactiveSelectionColor(GraphicsUtilities.decodeColor(inactiveSelectionColor));
	}

	public final void setInactiveSelectionColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setInactiveSelectionColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Color getInactiveSelectionBackgroundColor() {
		return inactiveSelectionBackgroundColor;
	}

	public void setInactiveSelectionBackgroundColor(
			Color inactiveSelectionBackgroundColor) {
		if (inactiveSelectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionBackgroundColor is null.");
		}

		this.inactiveSelectionBackgroundColor = inactiveSelectionBackgroundColor;
		repaintComponent();
	}

	public final void setInactiveSelectionBackgroundColor(
			String inactiveSelectionBackgroundColor) {
		if (inactiveSelectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionBackgroundColor is null.");
		}

		throw new UnsupportedOperationException();
		// setInactiveSelectionBackgroundColor(GraphicsUtilities.decodeColor(inactiveSelectionBackgroundColor));
	}

	public final void setInactiveSelectionBackgroundColor(int color) {
		// TerraTheme theme = (TerraTheme)Theme.getTheme();
		// setInactiveSelectionBackgroundColor(theme.getColor(color));
		throw new UnsupportedOperationException();
	}

	public Insets getPadding() {
		return padding;
	}

	public void setPadding(Insets padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		this.padding = padding;
		invalidateComponent();
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

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		boolean consumed = super.mouseMove(component, x, y);

		// if (Mouse.getCapturer() == component) {
		// TextInput textInput = (TextInput)getComponent();
		// int width = getWidth();
		//
		// if (x >= 0
		// && x < width) {
		// // Stop the scroll selection timer
		// if (scheduledScrollSelectionCallback != null) {
		// scheduledScrollSelectionCallback.cancel();
		// scheduledScrollSelectionCallback = null;
		// }
		//
		// scrollDirection = null;
		//
		// int offset = getInsertionPoint(x);
		//
		// if (offset != -1) {
		// // Select the range
		// if (offset > anchor) {
		// textInput.setSelection(anchor, offset - anchor);
		// } else {
		// textInput.setSelection(offset, anchor - offset);
		// }
		// }
		// } else {
		// if (scheduledScrollSelectionCallback == null) {
		// scrollDirection = (x < 0) ? FocusTraversalDirection.BACKWARD :
		// FocusTraversalDirection.FORWARD;
		//
		// scheduledScrollSelectionCallback =
		// ApplicationContext.scheduleRecurringCallback(scrollSelectionCallback,
		// SCROLL_RATE);
		//
		// // Run the callback once now to scroll the selection immediately
		// scrollSelectionCallback.run();
		// }
		// }
		// } else {
		// if (Mouse.isPressed(Mouse.Button.LEFT)
		// && Mouse.getCapturer() == null
		// && anchor != -1) {
		// // Capture the mouse so we can select text
		// Mouse.capture(component);
		// }
		// }

		return consumed;
	}

	@Override
	public boolean mouseDown(Component component, Mouse.Button button, int x,
			int y) {
		boolean consumed = super.mouseDown(component, button, x, y);

		// if (button == Mouse.Button.LEFT) {
		// TextInput textInput = (TextInput)getComponent();
		//
		// anchor = getInsertionPoint(x);
		//
		// if (anchor != -1) {
		// if (Keyboard.isPressed(Keyboard.Modifier.SHIFT)) {
		// // Select the range
		// int selectionStart = textInput.getSelectionStart();
		//
		// if (anchor > selectionStart) {
		// textInput.setSelection(selectionStart, anchor - selectionStart);
		// } else {
		// textInput.setSelection(anchor, selectionStart - anchor);
		// }
		// } else {
		// // Move the caret to the insertion point
		// textInput.setSelection(anchor, 0);
		// consumed = true;
		// }
		// }
		//
		//
		// // Set focus to the text input
		// textInput.requestFocus();
		// }

		return consumed;
	}

	@Override
	public boolean mouseUp(Component component, Mouse.Button button, int x,
			int y) {
		boolean consumed = super.mouseUp(component, button, x, y);

		// if (Mouse.getCapturer() == component) {
		// // Stop the scroll selection timer
		// if (scheduledScrollSelectionCallback != null) {
		// scheduledScrollSelectionCallback.cancel();
		// scheduledScrollSelectionCallback = null;
		// }
		//
		// Mouse.release();
		// }
		//
		// anchor = -1;

		return consumed;
	}

	@Override
	public boolean mouseClick(Component component, Mouse.Button button, int x,
			int y, int count) {
		if (button == Mouse.Button.LEFT && count > 1) {
			TextInput textInput = (TextInput) getComponent();
			textInput.selectAll();
		}

		return super.mouseClick(component, button, x, y, count);
	}

	@Override
	public boolean keyReleased(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		boolean consumed = super.keyReleased(component, keyCode, keyLocation);

		TextInput textInput = (TextInput) getComponent();
		textInput.setText(ui.getValue());

		return consumed;
	}

	// Component state events
	@Override
	public void enabledChanged(Component component) {
		super.enabledChanged(component);

		repaintComponent();
	}

	@Override
	public void focusedChanged(Component component, Component obverseComponent) {
		super.focusedChanged(component, obverseComponent);

		TextInput textInput = (TextInput) component;
		Window window = textInput.getWindow();

		if (component.isFocused()) {
			// If focus was permanently transferred within this window,
			// select all
			if (obverseComponent == null
					|| obverseComponent.getWindow() == window) {
				if (Mouse.getCapturer() != component) {
					textInput.selectAll();
				}
			}

			if (textInput.getSelectionLength() == 0) {
				int selectionStart = textInput.getSelectionStart();
				if (selectionStart < textInput.getCharacterCount()) {
					scrollCharacterToVisible(selectionStart);
				}

				showCaret(true);
			} else {
				showCaret(false);
			}
		} else {
			// If focus was permanently transferred within this window,
			// clear the selection
			if (obverseComponent == null
					|| obverseComponent.getWindow() == window) {
				textInput.clearSelection();
			}

			showCaret(false);
		}

		repaintComponent();
	}

	// Text input events
	@Override
	public void textSizeChanged(TextInput textInput, int previousTextSize) {
		invalidateComponent();
	}

	@Override
	public void maximumLengthChanged(TextInput textInput,
			int previousMaximumLength) {
		// No-op
	}

	@Override
	public void passwordChanged(TextInput textInput) {
		layout();
		repaintComponent();
	}

	@Override
	public void promptChanged(TextInput textInput, String previousPrompt) {
		promptChanged = true;
		repaintComponent();
	}

	@Override
	public void textValidatorChanged(TextInput textInput,
			Validator previousValidator) {
		repaintComponent();
	}

	@Override
	public void strictValidationChanged(TextInput textInput) {
		// No-op
	}

	@Override
	public void textValidChanged(TextInput textInput) {
		repaintComponent();
	}

	// Text input character events
	@Override
	public Vote previewInsertText(TextInput textInput, CharSequence text,
			int index) {
		Vote vote = Vote.APPROVE;

		if (textInput.isStrictValidation()) {
			Validator validator = textInput.getValidator();
			if (validator != null) {
				StringBuilder textBuilder = new StringBuilder();
				textBuilder.append(textInput.getText(0, index));
				textBuilder.append(text);
				textBuilder.append(textInput.getText(index,
						textInput.getCharacterCount()));

				if (!validator.isValid(textBuilder.toString())) {
					vote = Vote.DENY;
					// XXX Toolkit.getDefaultToolkit().beep();
				}
			}
		}

		return vote;
	}

	@Override
	public void insertTextVetoed(TextInput textInput, Vote reason) {
		// No-op
	}

	@Override
	public void textInserted(TextInput textInput, int index, int count) {
		// No-op
	}

	@Override
	public Vote previewRemoveText(TextInput textInput, int index, int count) {
		Vote vote = Vote.APPROVE;

		if (textInput.isStrictValidation()) {
			Validator validator = textInput.getValidator();
			if (validator != null) {
				StringBuilder textBuilder = new StringBuilder();
				textBuilder.append(textInput.getText(0, index));
				textBuilder.append(textInput.getText(index + count,
						textInput.getCharacterCount()));

				if (!validator.isValid(textBuilder.toString())) {
					vote = Vote.DENY;
					// XXX Toolkit.getDefaultToolkit().beep();
				}
			}
		}

		return vote;
	}

	@Override
	public void removeTextVetoed(TextInput textInput, Vote reason) {
		// No-op
	}

	@Override
	public void textRemoved(TextInput textInput, int index, int count) {
		// No-op
	}

	@Override
	public void textChanged(TextInput textInput) {
		textChanged = true;

		// layout();
		// repaintComponent();
	}

	// Text input selection events
	@Override
	public void selectionChanged(TextInput textInput,
			int previousSelectionStart, int previousSelectionLength) {
		// If the text input is valid, repaint the selection state; otherwise,
		// the selection will be updated in layout()
		if (textInput.isValid()) {
			// Repaint any previous caret bounds
			// if (caret != null) {
			// textInput.repaint(caret.x, caret.y, caret.width, caret.height);
			// }

			// Repaint any previous selection bounds
			// if (selection != null) {
			// Rectangle bounds = selection.getBounds();
			// textInput.repaint(bounds.x, bounds.y, bounds.width,
			// bounds.height);
			// }

			// if (textInput.getSelectionLength() == 0) {
			// updateSelection();
			// showCaret(textInput.isFocused());
			// } else {
			// updateSelection();
			// showCaret(false);

			// Rectangle bounds = selection.getBounds();
			// textInput.repaint(bounds.x, bounds.y, bounds.width,
			// bounds.height);
			// }

			textInput.repaint();
		}
	}

	private void updateSelection() {
		// TextInput textInput = (TextInput)getComponent();
		//
		// int height = getHeight();
		//
		// int selectionStart = textInput.getSelectionStart();
		// int selectionLength = textInput.getSelectionLength();
		//
		// int n = textInput.getCharacterCount();
		//
		// Bounds leadingSelectionBounds;
		// if (selectionStart < n) {
		// leadingSelectionBounds = getCharacterBounds(selectionStart);
		// } else {
		// // The insertion point is after the last character
		// int x;
		// if (n == 0) {
		// x = padding.left - scrollLeft + 1;
		// } else {
		// Rectangle2D textBounds = glyphVector.getLogicalBounds();
		// x = (int)Math.ceil(textBounds.getWidth()) + (padding.left -
		// scrollLeft + 1);
		// }
		//
		// int y = padding.top + 1;
		//
		// leadingSelectionBounds = new Bounds(x, y, 0, height - (padding.top +
		// padding.bottom + 2));
		// }
		//
		// caret = leadingSelectionBounds.toRectangle();
		// caret.width = 1;
		//
		// if (selectionLength > 0) {
		// Bounds trailingSelectionBounds = getCharacterBounds(selectionStart
		// + selectionLength - 1);
		// selection = new Rectangle(leadingSelectionBounds.x,
		// leadingSelectionBounds.y,
		// trailingSelectionBounds.x + trailingSelectionBounds.width -
		// leadingSelectionBounds.x,
		// trailingSelectionBounds.y + trailingSelectionBounds.height -
		// leadingSelectionBounds.y);
		// } else {
		// selection = null;
		// }
	}

	public void showCaret(boolean show) {
		// if (scheduledBlinkCaretCallback != null) {
		// scheduledBlinkCaretCallback.cancel();
		// }
		//
		// if (show) {
		// caretOn = true;
		// scheduledBlinkCaretCallback =
		// ApplicationContext.scheduleRecurringCallback(blinkCaretCallback,
		// Platform.getCursorBlinkRate());
		//
		// // Run the callback once now to show the cursor immediately
		// blinkCaretCallback.run();
		// } else {
		// scheduledBlinkCaretCallback = null;
		// }
	}
}
