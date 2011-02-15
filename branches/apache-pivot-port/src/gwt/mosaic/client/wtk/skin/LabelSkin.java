package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.wtk.ApplicationContext;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.ComponentSkin;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.HorizontalAlignment;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.LabelListener;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LabelSkin extends ComponentSkin implements LabelListener {

	private Font font;
	private boolean fontChanged = false;

	private Color color;
	private boolean colorChanged = false;

	private Color backgroundColor;
	private boolean backgroundColorChanged = false;

	private TextDecoration textDecoration;
	private boolean textDecorationChanged = false;

	private HorizontalAlignment horizontalAlignment;
	private boolean horizontalAlignmentChanged = false;

	private VerticalAlignment verticalAlignment;
	private boolean verticalAlignmentChanged = false;

	private Insets padding;
	private boolean paddingChanged = false;

	private boolean wrapText;
	private boolean wrapTextChanged = false;

	// Managed by LabelListener implementation.
	private boolean textChanged = false;

	private LabelWidget widget = null;

	public LabelSkin() {
		// Theme theme = Theme.getTheme();
		font = new Font();// theme.getFont();
		color = Color.BLACK;
		backgroundColor = null;
		textDecoration = null;
		horizontalAlignment = HorizontalAlignment.LEFT;
		verticalAlignment = VerticalAlignment.TOP;
		padding = Insets.NONE;
		wrapText = false;
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
			widget = new LabelWidget() {
				@Override
				protected void onLoad() {
					super.onLoad();
					final Element elem = getElement();
					elem.getStyle().setVisibility(Visibility.HIDDEN);
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							elem.getStyle().setVisibility(Visibility.VISIBLE);
						}
					});
				}
			};
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
	public void paint(ApplicationContext.DisplayHost displayHost) {
		Widget widget = asWidget();
		Style style = widget.getElement().getStyle();

		if (backgroundColorChanged) {
			if (backgroundColor != null) {
				backgroundColor.applyTo(widget.getElement(), true);
			} else {
				style.setBackgroundColor("transparent");
			}
			backgroundColorChanged = false;
		}

		if (colorChanged) {
			color.applyTo(widget.getElement(), false);
			colorChanged = false;
		}

		if (fontChanged) {
			font.applyTo(widget.getElement());
			fontChanged = false;
		}

		if (horizontalAlignmentChanged) {
			if (horizontalAlignment == HorizontalAlignment.LEFT) {
				((LabelWidget) widget)
						.setHorizontalAlignment(LabelWidget.ALIGN_LEFT);
			} else if (horizontalAlignment == HorizontalAlignment.CENTER) {
				((LabelWidget) widget)
						.setHorizontalAlignment(LabelWidget.ALIGN_CENTER);
			} else if (horizontalAlignment == HorizontalAlignment.RIGHT) {
				((LabelWidget) widget)
						.setHorizontalAlignment(LabelWidget.ALIGN_RIGHT);
			}
			horizontalAlignmentChanged = false;
		}

		if (paddingChanged) {
			style.setPaddingTop(padding.top, Unit.PX);
			style.setPaddingRight(padding.right, Unit.PX);
			style.setPaddingBottom(padding.bottom, Unit.PX);
			style.setPaddingLeft(padding.left, Unit.PX);
			paddingChanged = false;
		}

		if (textDecorationChanged) {
			if (textDecoration != null) {
				style.setTextDecoration(textDecoration);
			} else {
				style.setTextDecoration(TextDecoration.NONE);
			}
			textDecorationChanged = false;
		}

		if (verticalAlignmentChanged) {
			if (verticalAlignment == VerticalAlignment.TOP) {
				((LabelWidget) widget)
						.setVerticalAlignment(LabelWidget.ALIGN_TOP);
			} else if (verticalAlignment == VerticalAlignment.CENTER) {
				((LabelWidget) widget)
						.setVerticalAlignment(LabelWidget.ALIGN_MIDDLE);
			} else if (verticalAlignment == VerticalAlignment.BOTTOM) {
				((LabelWidget) widget)
						.setVerticalAlignment(LabelWidget.ALIGN_BOTTOM);
			}
			verticalAlignmentChanged = false;
		}

		if (wrapTextChanged) {
			((LabelWidget) widget).setWordWrap(wrapText);
			wrapTextChanged = false;
		}

		if (textChanged) {
			Label label = (Label) getComponent();
			((HasHTML) asWidget()).setHTML(label.getText());
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
		private final HTML htmlDiv;

		private VerticalAlignmentConstant vertAlign = HasVerticalAlignment.ALIGN_MIDDLE;

		public LabelWidget() {
			initWidget(div = new SimplePanel());
			div.add(htmlDiv = new HTML());
			setWordWrap(false);
			
			SkinClientBundle.INSTANCE.css().ensureInjected();
			setStyleName(SkinClientBundle.INSTANCE.css().labelWidget());
			htmlDiv.setStyleName(SkinClientBundle.INSTANCE.css().labelWidgetText());
		}

		public String getText() {
			return htmlDiv.getText();
		}

		public void setText(String text) {
			htmlDiv.setText(text);
		}

		public boolean getWordWrap() {
			return htmlDiv.getWordWrap();
		}

		public void setWordWrap(boolean wrap) {
			htmlDiv.setWordWrap(wrap);
		}

		public String getHTML() {
			return htmlDiv.getHTML();
		}

		public void setHTML(String html) {
			htmlDiv.setHTML(html);
		}

		public HorizontalAlignmentConstant getHorizontalAlignment() {
			return htmlDiv.getHorizontalAlignment();
		}

		public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
			htmlDiv.setHorizontalAlignment(align);
		}

		public VerticalAlignmentConstant getVerticalAlignment() {
			return vertAlign;
		}

		public void setVerticalAlignment(VerticalAlignmentConstant align) {
			vertAlign = align;
			htmlDiv.getElement()
					.getStyle()
					.setProperty(
							"verticalAlign",
							vertAlign == null ? "" : vertAlign
									.getVerticalAlignString());
		}

		@Override
		public void setWidth(String width) {
			super.setWidth(width);
			htmlDiv.setWidth(width);
		}

		@Override
		public void setHeight(String height) {
			super.setHeight(height);
			htmlDiv.setHeight(height);
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
