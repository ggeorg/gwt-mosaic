package gwt.mosaic.client;

import gwt.mosaic.client.beans.BXMLSerializer;
import gwt.mosaic.client.beans.Bindable;
import gwt.mosaic.client.beans.NamespaceBinding;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Window;

import com.google.gwt.core.client.GWT;

public class DefaultApplication implements Application {

	@SuppressWarnings("serial")
	public static class MyWindow extends Window implements Bindable {
		
		public MyWindow() {
			// No-op
		}
		
		@Override
		public void initialize(Map<String, Object> namespace) {
			NamespaceBinding namespaceBinding1 = new NamespaceBinding(
					namespace, "textInput1.text", "label1.text");
			namespaceBinding1.bind();
		}
	}

	private Window window;

	@Override
	public void startup(final Display display, Map<String, String> properties)
			throws Exception {

		BXMLSerializer<Window> bxmlSerializer = new BXMLSerializer<Window>() {
			@Override
			protected void onFailure(Throwable caught) {
				GWT.log(caught.getMessage(), caught);
			}

			@Override
			protected void onSuceess(Window root) {
				DefaultApplication.this.window = root;

				window.open(display);
			}
		};
		bxmlSerializer.readObject("binding.bxml");
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
