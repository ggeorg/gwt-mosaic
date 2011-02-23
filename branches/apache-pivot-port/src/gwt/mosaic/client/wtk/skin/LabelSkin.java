package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.HorizontalAlignment;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.LabelListener;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class LabelSkin extends ComponentSkin implements LabelListener {

	public interface UI extends IsWidget, HasAlignment, HasHTML, HasWordWrap {

		void setPresender(Component component);

		void setFont(Font font);

		void setTextDecoration(TextDecoration textDecoration);

		void setColor(Color color);

		void setBackgroundColor(Color backgroundColor);

		void setPadding(Insets padding);

	}

	// ---------------------------------------------------------------------
	private Font font;
	private boolean fontChanged;

	private Color color;
	private boolean colorChanged;

	private Color backgroundColor;
	private boolean backgroundColorChanged;

	private TextDecoration textDecoration;
	private boolean textDecorationChanged;

	private HorizontalAlignment horizontalAlignment;
	private boolean horizontalAlignmentChanged;

	private VerticalAlignment verticalAlignment;
	private boolean verticalAlignmentChanged;

	private Insets padding;
	private boolean paddingChanged;

	private boolean wrapText;
	private boolean wrapTextChanged;

	// Changed by LabelListener implementation.
	private boolean textChanged = true;

	private UI ui = null;

	public LabelSkin() {
		// Theme theme = Theme.getTheme();

		font = new Font();// theme.getFont();
		fontChanged = true;

		color = Color.BLACK;
		colorChanged = true;

		backgroundColor = null;
		backgroundColorChanged = true;

		textDecoration = null;
		textDecorationChanged = true;

		horizontalAlignment = HorizontalAlignment.LEFT;
		horizontalAlignmentChanged = true;

		verticalAlignment = VerticalAlignment.TOP;
		verticalAlignmentChanged = true;

		padding = Insets.NONE;
		paddingChanged = true;

		wrapText = false;
		wrapTextChanged = true;
	}

	@Override
	public void install(Component component) {
		super.install(component);

		Label label = (Label) getComponent();
		label.getLabelListeners().add(this);
	}

	@Override
	public Widget getWidget() {
		if (ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-Label");
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
	}

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	@Override
	public void paint(Widget context) {
		UI ui = (UI) getWidget();

		if (backgroundColorChanged) {
			ui.setBackgroundColor(backgroundColor);
			backgroundColorChanged = false;
		}

		if (colorChanged) {
			ui.setColor(color);
			colorChanged = false;
		}

		if (fontChanged) {
			ui.setFont(font);
			fontChanged = false;
		}

		if (horizontalAlignmentChanged) {
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				ui.setHorizontalAlignment(UI.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				ui.setHorizontalAlignment(UI.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				ui.setHorizontalAlignment(UI.ALIGN_RIGHT);
			}
			horizontalAlignmentChanged = false;
		}

		if (paddingChanged) {
			ui.setPadding(padding);
			paddingChanged = false;
		}

		if (textDecorationChanged) {
			ui.setTextDecoration(textDecoration);
			textDecorationChanged = false;
		}

		if (verticalAlignmentChanged) {
			if (verticalAlignment == VerticalAlignment.TOP) {
				ui.setVerticalAlignment(UI.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				ui.setVerticalAlignment(UI.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				ui.setVerticalAlignment(UI.ALIGN_BOTTOM);
			}
			verticalAlignmentChanged = false;
		}

		if (wrapTextChanged) {
			ui.setWordWrap(wrapText);
			wrapTextChanged = false;
		}

		if (textChanged) {
			Label label = (Label) getComponent();
			ui.setHTML(label.getText());
			textChanged = false;
		}
		
		invalidateComponent();
	}

	/**
	 * @return <tt>false</tt>; labels are not focusable.
	 */
	@Override
	public boolean isFocusable() {
		return false;
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

	public void setFont(String font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null");
		}
		setFont(Font.decode(font));
	}

	public void setFont(Dictionary<String, ?> font) {
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

	public void setColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}
		setColor(Color.decode(color));
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.backgroundColorChanged = true;
		repaintComponent();
	}

	public void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null");
		}
		setBackgroundColor(Color.decode(backgroundColor));
	}

	public TextDecoration getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(TextDecoration textDecoration) {
		this.textDecoration = textDecoration;
		this.textDecorationChanged = true;
		repaintComponent();
	}

	public void setTextDecoration(String textDecoration) {
		setTextDecoration(TextDecoration.valueOf(textDecoration.toUpperCase()));
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		if (horizontalAlignment == null) {
			throw new IllegalArgumentException("horizontalAlignment is null.");
		}

		if (!horizontalAlignment.equals(this.horizontalAlignment)) {
			this.horizontalAlignment = horizontalAlignment;
			this.horizontalAlignmentChanged = true;
			repaintComponent();
		}
	}

	public void setHorizontalAlignment(String horizontalAlignment) {
		if (horizontalAlignment == null) {
			throw new IllegalArgumentException("horizontalAlignment is null.");
		}
		setHorizontalAlignment(HorizontalAlignment.valueOf(horizontalAlignment
				.toUpperCase()));
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		if (verticalAlignment == null) {
			throw new IllegalArgumentException("verticalAlignment is null.");
		}

		if (!verticalAlignment.equals(this.verticalAlignment)) {
			this.verticalAlignment = verticalAlignment;
			verticalAlignmentChanged = true;
			repaintComponent();
		}
	}

	public void setVerticalAlignment(String verticalAlignment) {
		if (verticalAlignment == null) {
			throw new IllegalArgumentException("verticalAlignment is null.");
		}

		setVerticalAlignment(VerticalAlignment.valueOf(verticalAlignment
				.toUpperCase()));
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

	public void setPadding(Dictionary<String, ?> padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}
		setPadding(new Insets(padding));
	}

	public void setPadding(int padding) {
		setPadding(new Insets(padding));
	}

	public void setPadding(Number padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}
		setPadding(padding.intValue());
	}

	public void setPadding(String padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}
		setPadding(Insets.decode(padding));
	}

	public boolean getWrapText() {
		return wrapText;
	}

	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
		this.wrapTextChanged = true;
		invalidateComponent();
	}

	@Override
	public void textChanged(Label label, String previousText) {
		textChanged = true;
		System.out.println("=================== Text changed '" + previousText
				+ "' to '" + label.getText()+"'");
		invalidateComponent();
	}
}
