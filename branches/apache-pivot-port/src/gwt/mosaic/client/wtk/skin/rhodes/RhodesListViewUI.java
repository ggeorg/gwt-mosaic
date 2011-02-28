package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.wtk.Component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class RhodesListViewUI extends Composite implements RhodesListViewSkin.UI {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, RhodesListViewUI> {
	}
	
	@UiField
	FlowPanel innerDiv;

	private Component presender;

	RhodesListViewUI() {
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
					presender.repaint();
					presender.invalidate();
				}
			}
		});
	}

	@Override
	public void setInnerHTML(String html) {
		innerDiv.getElement().setInnerHTML(html);
	}
}
