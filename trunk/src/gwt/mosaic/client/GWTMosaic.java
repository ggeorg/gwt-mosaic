package gwt.mosaic.client;

import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.beans.NamespaceBinding;
import gwt.mosaic.client.beans.NamespaceBinding.BindMapping;
import gwt.mosaic.client.effects.AnimationEngine;
import gwt.mosaic.client.effects.Fade;
import gwt.mosaic.client.effects.GrinFile;
import gwt.mosaic.client.effects.InterpolatedModelParser;
import gwt.mosaic.client.effects.Translator;
import gwt.mosaic.client.style.Opacity;
import gwt.mosaic.client.ui.Box;
import gwt.mosaic.client.ui.Orientation;
import gwt.mosaic.client.ui.WidgetHelper;
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
		MyUiBinder INSTANCE = GWT.create(MyUiBinder.class);
	}

	@UiHandler("button")
	void handleClick(ClickEvent e) {
		if (box1.getOrientation() == Orientation.HORIZONTAL) {
			box1.setOrientation(Orientation.VERTICAL);
		} else {
			box1.setOrientation(Orientation.HORIZONTAL);
		}
	}
	
	@GrinFile(value = "fade_model.txt")
	interface MyFadeModel extends InterpolatedModelParser {
		MyFadeModel INSTANCE = GWT.create(MyFadeModel.class);
	};

	@GrinFile(value = "translator_model.txt")
	interface MyTranslatorModel extends InterpolatedModelParser {
		MyTranslatorModel INSTANCE = GWT.create(MyTranslatorModel.class);
	};
	
	@UiField
	Box box1;

	@UiField
	Box box2;

	@UiField
	Button button;

	public void onModuleLoad() {
		Widget w = MyUiBinder.INSTANCE.createAndBindUi(this);

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

		MyFadeModel fm = GWT.create(MyFadeModel.class);
		Fade f = new Fade(fm.createModel()) {
			@Override
			protected void update(Opacity opacity) {
				opacity.applyTo(box2.getElement());
			}
		};

		MyTranslatorModel tm = GWT.create(MyTranslatorModel.class);
		Translator t = new Translator(tm.createModel()) {
			@Override
			protected void update(int dx, int dy) {
				WidgetHelper.translate(box2, dx, dy);
			}
		};

		AnimationEngine.get().add(f);
		AnimationEngine.get().add(t);
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
