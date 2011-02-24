package gwt.mosaic.client;

import com.google.gwt.core.client.GWT;

import gwt.mosaic.client.beans.BXMLSerializer;
import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.beans.Bindable;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.examples.Address;
import gwt.mosaic.client.examples.Contact;
import gwt.mosaic.client.examples.IMAccount;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.BoxPane;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Window;

public class DefaultApplication implements Application {

	@SuppressWarnings("serial")
	public static class MyWindow extends Window implements Bindable {
		private BoxPane form;

		@Override
		public void initialize(Map<String, Object> namespace) {
			form = (BoxPane) namespace.get("form");

			@SuppressWarnings("unchecked")
			BeanAdapter<Contact> adapter = (BeanAdapter<Contact>) BeanAdapterFactory
					.createFor(CONTACT);
			form.load(adapter);
		}
	}

	private static final Contact CONTACT = new Contact("101", "Joe User",
			new Address("123 Main St.", "Cambridge", "MA", "02142"),
			"(617) 555-1234", "joe_user@foo.com", new IMAccount("juser1234",
					"AIM"));

	private MyWindow window;

	@Override
	public void startup(final Display display, Map<String, String> properties)
			throws Exception {
		BXMLSerializer<MyWindow> bxmlSerializer = new BXMLSerializer<MyWindow>() {
			@Override
			protected void onFailure(Throwable caught) {
				GWT.log(caught.getMessage(), caught);
			}

			@Override
			protected void onSuceess(MyWindow root) {
				DefaultApplication.this.window = root;

				window.open(display);
			}
		};
		bxmlSerializer.readObject("form.bxml");
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
