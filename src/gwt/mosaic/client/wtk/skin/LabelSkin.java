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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class LabelSkin extends ComponentSkin implements LabelListener {

	public interface View extends IsWidget, HasAlignment, HasHTML, HasWordWrap {

		void setFont(Font font);

		void setTextDecoration(TextDecoration textDecoration);

		void setColor(Color color);

		void setBackgroundColor(Color backgroundColor);

		void setPadding(Insets padding);
	}

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

	private View display = null;

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
		if (display == null) {
			display = GWT.create(View.class);
			display.asWidget().addStyleName("m-Label");
		}
		return display.asWidget();
	}

	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth;
		if (getWidget().isAttached()) {
			Element elem = getWidget().getElement();
			preferredWidth = (int) Math.ceil(elem.getClientWidth());
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
			preferredHeight = (int) Math.ceil(elem.getClientHeight());
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
		View labelWidget = (View) getWidget();

		if (backgroundColorChanged) {
			labelWidget.setBackgroundColor(backgroundColor);
			backgroundColorChanged = false;
		}

		if (colorChanged) {
			labelWidget.setColor(color);
			colorChanged = false;
		}

		if (fontChanged) {
			labelWidget.setFont(font);
			fontChanged = false;
		}

		if (horizontalAlignmentChanged) {
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				labelWidget.setHorizontalAlignment(View.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				labelWidget.setHorizontalAlignment(View.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				labelWidget.setHorizontalAlignment(View.ALIGN_RIGHT);
			}
			horizontalAlignmentChanged = false;
		}

		if (paddingChanged) {
			labelWidget.setPadding(padding);
			paddingChanged = false;
		}

		if (textDecorationChanged) {
			labelWidget.setTextDecoration(textDecoration);
			textDecorationChanged = false;
		}

		if (verticalAlignmentChanged) {
			if (verticalAlignment == VerticalAlignment.TOP) {
				labelWidget.setVerticalAlignment(View.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				labelWidget.setVerticalAlignment(View.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				labelWidget.setVerticalAlignment(View.ALIGN_BOTTOM);
			}
			verticalAlignmentChanged = false;
		}

		if (wrapTextChanged) {
			labelWidget.setWordWrap(wrapText);
			wrapTextChanged = false;
		}

		if (textChanged) {
			Label label = (Label) getComponent();
			labelWidget.setHTML(label.getText());
		}
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

	public final void setFont(String fond) {
		if (font == null) {
			throw new IllegalArgumentException("font is null");
		}

		throw new UnsupportedOperationException();
		// setFont(decodeFont(font));
	}

	public final void setFont(Dictionary<String, ?> font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		throw new UnsupportedOperationException();
		// setFont(Theme.deriveFont(font));
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

		throw new UnsupportedOperationException();
		// setColor(GraphicsUtilities.decodeColor(color));
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.backgroundColorChanged = true;
		repaintComponent();
	}

	public final void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null");
		}

		throw new UnsupportedOperationException();
		// setColor(GraphicsUtilities.decodeColor(color));
	}

	public TextDecoration getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(TextDecoration textDecoration) {
		this.textDecoration = textDecoration;
		this.textDecorationChanged = true;
		repaintComponent();
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
		invalidateComponent();
	}
}
