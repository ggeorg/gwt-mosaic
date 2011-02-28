package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class RhodesPushButtonUI extends Composite implements
		RhodesPushButtonSkin.UI, AcceptsOneWidget {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, RhodesPushButtonUI> {
	}

	@UiField
	SimplePanel innerDiv;

	private Component presender;

	RhodesPushButtonUI() {
		initWidget(uiBinder.createAndBindUi(this));
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
				if (presender != null) {
					//presender.repaint();
					presender.invalidate();
				}
			}
		});
	}

	@Override
	public void setBorderColor(Color borderColor) {
		Style style = innerDiv.getElement().getStyle();
		if (borderColor != null) {
			style.setBorderColor(borderColor.toString());
		} else {
			style.setBorderColor("");
		}
	}

	@Override
	public void setBackgroundColor(Color backgroundColor) {
		if (backgroundColor != null) {
			backgroundColor.applyTo(innerDiv.getElement(), true);
		} else {
			Style style = innerDiv.getElement().getStyle();
			style.setBackgroundColor("");
		}
	}

	@Override
	public void setWidget(IsWidget w) {
		innerDiv.setWidget(w);
	}

	@Override
	public void setInnerHTML(String html) {
		innerDiv.getElement().setInnerHTML(html);
	}
	
	@Override
	public void setPadding(Insets padding) {
		Style style = innerDiv.getElement().getStyle();
		style.setPaddingTop(padding.top, Unit.PX);
		style.setPaddingRight(padding.right, Unit.PX);
		style.setPaddingBottom(padding.bottom, Unit.PX);
		style.setPaddingLeft(padding.left, Unit.PX);
	}
}
