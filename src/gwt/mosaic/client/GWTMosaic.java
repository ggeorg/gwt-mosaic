package gwt.mosaic.client;

import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.beans.NamespaceBinding;
import gwt.mosaic.client.beans.NamespaceBinding.BindMapping;
import gwt.mosaic.client.ui.Box;
import gwt.mosaic.client.ui.Orientation;
import gwt.mosaic.shared.Bean;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

@Bean
public class GWTMosaic implements EntryPoint {

	static {
		System.out.println(GWT.create(BeanAdapterFactory.class));
	}

	interface MyUiBinder extends UiBinder<Widget, GWTMosaic> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	Box box1;

	@UiField
	Box box2;

	@UiField
	Button button;

	@UiHandler("button")
	void handleClick(ClickEvent e) {
		if (box1.getOrientation() == Orientation.HORIZONTAL) {
			box1.setOrientation(Orientation.VERTICAL);
		} else {
			box1.setOrientation(Orientation.HORIZONTAL);
		}
	}

	public void onModuleLoad() {
		Widget w = uiBinder.createAndBindUi(this);

		// Transforms a source value during a bind operation.
		BindMapping bindMapping = new BindMapping() {
			@Override
			public Object evaluate(Object value) {
				Orientation o = (Orientation) value;
				if (o == Orientation.HORIZONTAL) {
					return Orientation.VERTICAL;
				} else {
					return Orientation.HORIZONTAL;
				}
			}
		};

		NamespaceBinding b = new NamespaceBinding(
				BeanAdapterFactory.createFor(this), "box1.orientation",
				"box2.orientation", bindMapping);
		b.bind();
		
		// ----

		RootLayoutPanel.get().add(w);
	}

	public Box getBox1() {
		return box1;
	}

	public Box getBox2() {
		return box2;
	}

	public Button getButton() {
		return button;
	}
}
