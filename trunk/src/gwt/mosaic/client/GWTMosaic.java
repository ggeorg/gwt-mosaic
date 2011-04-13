package gwt.mosaic.client;

import gwt.mosaic.client.beans.BeanAdapterFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class GWTMosaic implements EntryPoint {

	static {
		GWT.create(BeanAdapterFactory.class);
	}

	interface MyUiBinder extends UiBinder<Widget, GWTMosaic> {
		MyUiBinder INSTANCE = GWT.create(MyUiBinder.class);
	}

	public void onModuleLoad() {
		Widget w = MyUiBinder.INSTANCE.createAndBindUi(this);
		RootLayoutPanel.get().add(w);
	}

}
