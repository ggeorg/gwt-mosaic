package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.skin.CheckboxUI;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class RhodesCheckboxUI extends Composite implements
		RhodesCheckboxSkin.UI, AcceptsOneWidget {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, RhodesCheckboxUI> {
	}

	@UiField
	HTMLPanel inputDiv;

	@UiField
	CheckboxUI checkBox;

	private Component presender;

	RhodesCheckboxUI() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresender(Component presender) {
		this.presender = presender;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if (presender != null) {
					presender.repaint();
				}
			}
		});
	}

	@Override
	public void setWidget(IsWidget w) {
		inputDiv.add(w);
	}
}
