package gwt.mosaic.client;

import gwt.mosaic.client.beans.BXMLSerializerResponse;
import gwt.mosaic.client.beans.BXMLSerializerService;
import gwt.mosaic.client.beans.BXMLSerializerServiceAsync;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultApplication2 implements Application {
	private static BXMLSerializerServiceAsync rpc = GWT
			.create(BXMLSerializerService.class);

	private Window window;

	@Override
	public void startup(final Display display, Map<String, String> properties)
			throws Exception {

		rpc.readWindow("window.bxml", new AsyncCallback<BXMLSerializerResponse>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage(), caught);
			}

			@Override
			public void onSuccess(BXMLSerializerResponse result) {
				DefaultApplication2.this.window = (Window) result.getRoot();
				window.open(display);
			}
		});
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub
	}

}
