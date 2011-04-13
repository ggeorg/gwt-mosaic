package gwt.mosaic.client;

import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.effects.Transition;
import gwt.mosaic.client.effects.easing.Cubic;
import gwt.mosaic.client.effects.easing.Easing;
import gwt.mosaic.client.effects.easing.Easing.Type;
import gwt.mosaic.client.effects.easing.Linear;
import gwt.mosaic.client.effects.easing.Quadratic;
import gwt.mosaic.client.ui.Button;
import gwt.mosaic.client.ui.WidgetHelper;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class TransitionDemoGWTMosaic implements EntryPoint {

	static {
		System.out.println(GWT.create(BeanAdapterFactory.class));
	}

	interface MyUiBinder extends UiBinder<Widget, TransitionDemoGWTMosaic> {
		MyUiBinder INSTANCE = GWT.create(MyUiBinder.class);
	}

	@UiField
	Button button1;

	@UiField
	Button button2;

	@UiField
	Button button3;

	@UiField
	Button button4;

	@UiHandler(value = { "button1", "button2", "button3", "button4" })
	public void onClickEvent(ClickEvent e) {
		final Widget w = (Widget) e.getSource();

		Transition transition = new Transition(new Quadratic(),
				Type.EASE_IN_OUT) {

			int initialWidth = w.getElement().getClientWidth()
					- WidgetHelper.getBoxModel(w).getPaddingWidthContribution();

			@Override
			protected void onUpdate(double progress) {
				WidgetHelper.setPreferredWidth(w,
						(int) ((1.0 - progress) * initialWidth) + "px", true);
			}

			@Override
			protected void onComplete() {
				w.setVisible(false);
				WidgetHelper.setPreferredWidth(w, null);
				WidgetHelper.invalidate(w, true);
			}
		};

		transition.run(250);
	}

	public void onModuleLoad() {
		Widget w = MyUiBinder.INSTANCE.createAndBindUi(this);
		RootLayoutPanel.get().add(w);
	}

}
