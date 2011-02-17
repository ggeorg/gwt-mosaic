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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LabelSkin extends ComponentSkin implements LabelListener {

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

	private LabelWidget widget = null;

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
	public Widget asWidget() {
		if (widget == null) {
			widget = new LabelWidget();
			widget.addStyleName("m-Label");
		}
		return widget;
	}

	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth;
		if (asWidget().isAttached()) {
			Element elem = asWidget().getElement();
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
		if (asWidget().isAttached()) {
			Element elem = asWidget().getElement();
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
		LabelWidget labelWidget = (LabelWidget) asWidget();

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
				labelWidget.setHorizontalAlignment(LabelWidget.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				labelWidget.setHorizontalAlignment(LabelWidget.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				labelWidget.setHorizontalAlignment(LabelWidget.ALIGN_RIGHT);
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
				labelWidget.setVerticalAlignment(LabelWidget.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				labelWidget.setVerticalAlignment(LabelWidget.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				labelWidget.setVerticalAlignment(LabelWidget.ALIGN_BOTTOM);
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

	// -------------------------
	private class LabelWidget extends Composite implements HasAlignment,
			HasHTML, HasWordWrap, HasClickHandlers, HasDoubleClickHandlers,
			HasAllMouseHandlers {
		private final SimplePanel div;
		private final HTML innerDiv;

		private VerticalAlignmentConstant vertAlign;

		public LabelWidget() {
			initWidget(div = new SimplePanel());
			div.add(innerDiv = new HTML());
			setWordWrap(false);

			SkinClientBundle.INSTANCE.css().ensureInjected();
			setStyleName(SkinClientBundle.INSTANCE.css().labelWidget());
			innerDiv.setStyleName(SkinClientBundle.INSTANCE.css()
					.labelWidgetInner());
		}

		public void setFont(Font font) {
			if (font != null) {
				font.applyTo(innerDiv.getElement());
			}
		}

		public void setTextDecoration(TextDecoration textDecoration) {
			Style style = innerDiv.getElement().getStyle();
			if (textDecoration != null) {
				style.setTextDecoration(textDecoration);
			} else {
				style.setTextDecoration(TextDecoration.NONE);
			}
		}

		public void setColor(Color color) {
			if (color != null) {
				color.applyTo(innerDiv.getElement(), false);
			} else {
				Style style = innerDiv.getElement().getStyle();
				style.setColor("");
			}
		}

		public void setBackgroundColor(Color backgroundColor) {
			if (backgroundColor != null) {
				backgroundColor.applyTo(getElement(), true);
			} else {
				Style style = getElement().getStyle();
				style.setBackgroundColor("");
			}
		}

		public void setPadding(Insets padding) {
			Style style = innerDiv.getElement().getStyle();
			style.setPaddingTop(padding.top, Unit.PX);
			style.setPaddingRight(padding.right, Unit.PX);
			style.setPaddingBottom(padding.bottom, Unit.PX);
			style.setPaddingLeft(padding.left, Unit.PX);
		}

		public String getText() {
			return innerDiv.getText();
		}

		public void setText(String text) {
			innerDiv.setText(text);
		}

		public boolean getWordWrap() {
			return innerDiv.getWordWrap();
		}

		public void setWordWrap(boolean wrap) {
			innerDiv.setWordWrap(wrap);
		}

		public String getHTML() {
			return innerDiv.getHTML();
		}

		public void setHTML(String html) {
			this.innerDiv.setHTML(html);
		}

		public HorizontalAlignmentConstant getHorizontalAlignment() {
			return innerDiv.getHorizontalAlignment();
		}

		public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
			innerDiv.setHorizontalAlignment(align);
		}

		public VerticalAlignmentConstant getVerticalAlignment() {
			return vertAlign;
		}

		public void setVerticalAlignment(VerticalAlignmentConstant align) {
			vertAlign = align;
			innerDiv.getElement()
					.getStyle()
					.setProperty(
							"verticalAlign",
							vertAlign == null ? "" : vertAlign
									.getVerticalAlignString());
		}

		@Override
		public void setWidth(String width) {
			super.setWidth(width);
			innerDiv.setWidth(width);
		}

		@Override
		public void setHeight(String height) {
			super.setHeight(height);
			innerDiv.setHeight(height);
		}

		public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
			return addDomHandler(handler, MouseDownEvent.getType());
		}

		public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
			return addDomHandler(handler, MouseUpEvent.getType());
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
			return addDomHandler(handler, MouseMoveEvent.getType());
		}

		public HandlerRegistration addMouseWheelHandler(
				MouseWheelHandler handler) {
			return addDomHandler(handler, MouseWheelEvent.getType());
		}

		public HandlerRegistration addDoubleClickHandler(
				DoubleClickHandler handler) {
			return addHandler(handler, DoubleClickEvent.getType());
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}
	}
}
