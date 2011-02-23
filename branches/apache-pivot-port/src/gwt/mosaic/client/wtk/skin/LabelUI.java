package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.Widget;

public class LabelUI extends Composite implements LabelSkin.UI,
		HasAlignment, HasHTML, HasWordWrap {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, LabelUI> {
	}

	@UiField
	HTML htmlDiv;

	private VerticalAlignmentConstant valign = HasVerticalAlignment.ALIGN_TOP;

	private Component presender;

	LabelUI() {
		initWidget(uiBinder.createAndBindUi(this));

		setWordWrap(false);
		setVerticalAlignment(valign);
	}
	
	@Override
	public void setPresender(Component component) {
		this.presender = component;
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if(presender != null) {
					presender.repaint();
				}
			}
		});
	}

	@Override
	public void setFont(Font font) {
		if (font != null) {
			font.applyTo(htmlDiv.getElement());
		}
	}

	@Override
	public void setTextDecoration(TextDecoration textDecoration) {
		Style style = htmlDiv.getElement().getStyle();
		if (textDecoration != null) {
			style.setTextDecoration(textDecoration);
		} else {
			style.setTextDecoration(TextDecoration.NONE);
		}
	}

	@Override
	public void setColor(Color color) {
		if (color != null) {
			color.applyTo(htmlDiv.getElement(), false);
		} else {
			Style style = htmlDiv.getElement().getStyle();
			style.setColor("");
		}
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null) {
			backgroundColor.applyTo(getElement(), true);
		} else {
			Style style = getElement().getStyle();
			style.setBackgroundColor("");
		}
	}

	@Override
	public void setPadding(Insets padding) {
		Style style = htmlDiv.getElement().getStyle();
		style.setPaddingTop(padding.top, Unit.PX);
		style.setPaddingRight(padding.right, Unit.PX);
		style.setPaddingBottom(padding.bottom, Unit.PX);
		style.setPaddingLeft(padding.left, Unit.PX);
	}

	@Override
	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return htmlDiv.getHorizontalAlignment();
	}

	@Override
	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		htmlDiv.setHorizontalAlignment(align);
	}

	@Override
	public VerticalAlignmentConstant getVerticalAlignment() {
		return valign;
	}

	@Override
	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		this.valign = align;

		Style style = htmlDiv.getElement().getStyle();
		style.setProperty("verticalAlign",
				valign == null ? "" : valign.getVerticalAlignString());
	}

	@Override
	public String getText() {
		return htmlDiv.getText();
	}

	@Override
	public void setText(String text) {
		htmlDiv.setText(text);
	}

	@Override
	public boolean getWordWrap() {
		return htmlDiv.getWordWrap();
	}

	@Override
	public void setWordWrap(boolean wrap) {
		htmlDiv.setWordWrap(wrap);
	}

	@Override
	public String getHTML() {
		return htmlDiv.getHTML();
	}

	@Override
	public void setHTML(String html) {
		this.htmlDiv.setHTML(html);
	}

}
