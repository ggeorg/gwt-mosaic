package gwt.mosaic.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class Label extends Composite implements ConstrainedVisual, HasText,
		HasWordWrap, RequiresResize {

	interface MyUiBinder extends UiBinder<Widget, Label> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static LabelImpl impl = GWT.create(LabelImpl.class);

	@UiField
	HTML htmlDiv;

	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.START;
	private VerticalAlignment verticalAlignment = VerticalAlignment.MIDDLE;

	public Label() {
		initWidget(uiBinder.createAndBindUi(this));

		setWordWrap(true);
		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);

		DOM.setStyleAttribute(getElement(), "display", "table");
		DOM.setStyleAttribute(getElement(), "overflow", "hidden");
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				WidgetHelper.invalidate(Label.this);
			}
		});
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment align) {
		this.horizontalAlignment = align;
		switch (align) {
		case LEFT:
			htmlDiv.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
			break;
		case CENTER:
			htmlDiv.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
			break;
		case RIGHT:
			htmlDiv.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
			break;
		case START:
			htmlDiv.setAutoHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_LOCALE_START);
			break;
		case END:
			htmlDiv.setAutoHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_LOCALE_END);
			break;
		default:
			htmlDiv.setAutoHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_LOCALE_START);
			break;
		}
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment align) {
		this.verticalAlignment = align;
		Style style = htmlDiv.getElement().getStyle();
		switch (align) {
		case TOP:
			style.setProperty("verticalAlign",
					HasAlignment.ALIGN_TOP.getVerticalAlignString());
			break;
		case MIDDLE:
			style.setProperty("verticalAlign",
					HasAlignment.ALIGN_MIDDLE.getVerticalAlignString());
			break;
		case BOTTOM:
			style.setProperty("verticalAlign",
					HasAlignment.ALIGN_MIDDLE.getVerticalAlignString());
			break;
		default:
			style.setProperty("verticalAlign",
					HasAlignment.ALIGN_TOP.getVerticalAlignString());
			break;
		}
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
	public void setVisible(boolean visible) {
		if (visible) {
			DOM.setStyleAttribute(getElement(), "display", "table");
		} else {
			super.setVisible(visible);
		}
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
	public int getPreferredWidth(int height) {
		return impl.getPreferredWidth(this, height);
	}

	@Override
	public int getPreferredHeight(int width) {
		return impl.getPreferredHeight(this, width);
	}

	@Override
	public Dimensions getPreferredSize() {
		return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	}

	@Override
	public int getBaseline(int width, int height) {
		return 0;
	}

	@Override
	public int getBaseline() {
		return getBaseline(getElement().getClientWidth(), getElement()
				.getClientHeight());
	}

	@Override
	public void onResize() {
		WidgetHelper.invalidate(this);
	}

	//
	// UiBinder related layout hints
	//
	
	@Override
	public void setPreferredWidth(String preferredWidth) {
		WidgetHelper.setPreferredWidth(this, preferredWidth);
	}

	@Override
	public void setPreferredHeight(String preferredHeight) {
		WidgetHelper.setPreferredHeight(this, preferredHeight);
	}

	@Override
	public void setColumnSpan(int columnSpan) {
		WidgetHelper.setColumnSpan(this, columnSpan);
	}

	@Override
	public void setRowSpan(int rowSpan) {
		WidgetHelper.setRowSpan(this, rowSpan);
	}

	@Override
	public void setWeight(int weight) {
		WidgetHelper.setWeight(this, weight);
	}
}
