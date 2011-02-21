package gwt.mosaic.client;

import gwt.mosaic.client.beans.BXMLSerializerService;
import gwt.mosaic.client.beans.BXMLSerializerServiceAsync;
import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.beans.NamespaceBinding;
import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.util.Vote;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.BoxPane;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.Orientation;
import gwt.mosaic.client.wtk.TextInput;
import gwt.mosaic.client.wtk.TextInputContentListener;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;

public class DefaultApplication implements Application {

	interface TextInputBeanAdapter extends BeanAdapter<TextInput> {
	}

	interface LabelBeanAdapter extends BeanAdapter<Label> {
	}

	@SuppressWarnings("unused")
	private static BXMLSerializerServiceAsync rpc = GWT
			.create(BXMLSerializerService.class);

	private Window window;

	@Override
	public void startup(final Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();

		BoxPane pane = new BoxPane();
		pane.setOrientation(Orientation.VERTICAL);

		TextInput textInput = new TextInput();
		textInput.setPrompt("Name");
		pane.add(textInput);

		textInput.getTextInputContentListeners().add(
				new TextInputContentListener() {
					@Override
					public Vote previewInsertText(TextInput textInput,
							CharSequence text, int index) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void insertTextVetoed(TextInput textInput,
							Vote reason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void textInserted(TextInput textInput, int index,
							int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public Vote previewRemoveText(TextInput textInput,
							int index, int count) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void removeTextVetoed(TextInput textInput,
							Vote reason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void textRemoved(TextInput textInput, int index,
							int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void textChanged(TextInput textInput) {
						System.out.println("::::::::::::::::"+textInput);
					}
				});

		Label label = new Label();
		label.setText("");
		label.getStyles().put("backgroundColor", Color.YELLOW);
		pane.add(label);

		GWT.create(TextInputBeanAdapter.class);
		GWT.create(LabelBeanAdapter.class);

		Map<String, Object> namespace = new HashMap<String, Object>();
		namespace.put("textInput1", textInput);
		namespace.put("label1", label);
		NamespaceBinding namespaceBinding1 = new NamespaceBinding(namespace,
				"textInput1.text", "label1.text");

		namespaceBinding1.bind();

		window.setContent(pane);
		window.setX(20);
		window.setY(20);
		//window.setMaximized(true);
		window.open(display);
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
