package org.gwt.mosaic2g.client.scene.ui;

import org.gwt.mosaic2g.client.MyClientBundle;
import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.layout.Resizable;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@code Text} is useful for displaying text that is required to fit within a
 * specific space, and thus may need to use truncation to size the string to
 * fit.
 * 
 * @author ggeorg
 */
public class Text extends Control implements HasAlignment, Resizable {

	private String text;
	private boolean asHTML;
	private boolean textChanged;

	private HorizontalAlignmentConstant horzAlign = HasHorizontalAlignment.ALIGN_CENTER;
	private boolean horzAlignChanged;

	private VerticalAlignmentConstant vertAlign = HasVerticalAlignment.ALIGN_MIDDLE;
	private boolean vertAlignChanged;

	private LabelWidget cachedWidget;

	public Text(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		setText(text, false);
	}

	public void setText(String text, boolean asHTML) {
		if (text != null && text.equals(this.text) && asHTML == this.asHTML) {
			return;
		}
		this.text = text;
		this.asHTML = asHTML;
		this.textChanged = true;
		markAsChanged();
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return horzAlign;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		if (horzAlign == align) {
			return;
		}
		horzAlign = align;
		horzAlignChanged = true;
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return vertAlign;
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		if (vertAlign == align) {
			return;
		}
		vertAlign = align;
		vertAlignChanged = true;
	}

	public int getPrefWidth() {
		if (cachedWidget == null) {
			return super.getWidth();
		} else {
			return cachedWidget.getOffsetWidth();
		}
	}

	public int getPrefHeight() {
		if (cachedWidget == null) {
			return super.getHeight();
		} else {
			return cachedWidget.getOffsetHeight();
		}
	}

	@Override
	protected Widget createWidget() {
		updateWidget(cachedWidget = new LabelWidget(), true);
		return cachedWidget;
	}

	@Override
	protected void updateWidget(Widget w, boolean init) {
		super.updateWidget(w, init);
		if (w instanceof HasHTML && (init || textChanged)) {
			final HasHTML hasHTML = (HasHTML) w;
			if (asHTML) {
				hasHTML.setHTML(text);
			} else {
				hasHTML.setText(text);
			}
			textChanged = false;
		}
		if (w instanceof HasHorizontalAlignment && (init || horzAlignChanged)) {
			((HasHorizontalAlignment) w).setHorizontalAlignment(horzAlign);
			horzAlignChanged = false;
		}
		if (w instanceof HasVerticalAlignment && (init || vertAlignChanged)) {
			((HasVerticalAlignment) w).setVerticalAlignment(vertAlign);
			vertAlignChanged = false;
		}
	}

	// -------------------------
	private class LabelWidget extends Composite implements HasAlignment,
			HasHTML, HasWordWrap {
		private final SimplePanel div;
		private final HTML htmlDiv;

		public LabelWidget() {
			initWidget(div = new SimplePanel());
			div.add(htmlDiv = new HTML());
			// setWordWrap(false);
			MyClientBundle.INSTANCE.css().ensureInjected();
			setStyleName(MyClientBundle.INSTANCE.css().labelWidget());
			htmlDiv.setStyleName(MyClientBundle.INSTANCE.css()
					.labelWidgetText());
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
			return horzAlign;
		}

		public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
			horzAlign = align;
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

	}

}
