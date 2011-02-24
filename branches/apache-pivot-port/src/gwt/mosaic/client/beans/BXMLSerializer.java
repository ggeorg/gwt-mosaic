package gwt.mosaic.client.beans;

import gwt.mosaic.client.collections.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class BXMLSerializer<T> {
	private static BXMLSerializerServiceAsync rpc = GWT
			.create(BXMLSerializerService.class);

	private BXMLSerializerDTO bxmlSerializerDTO;

	public final void readObject(String resourceName) {
		if (resourceName == null || resourceName.length() == 0) {
			throw new IllegalArgumentException();
		}

		rpc.readObject(resourceName, new AsyncCallback<BXMLSerializerDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				BXMLSerializer.this.onFailure(caught);
			}

			@Override
			public void onSuccess(BXMLSerializerDTO result) {
				bxmlSerializerDTO = result;

				T root = (T) getRoot();
				// Apply the namespace bindings
				// Bind the root to the namespace
				if (root instanceof Bindable) {
					((Bindable) root).initialize(getNamespace());
				}
				BXMLSerializer.this.onSuceess(root);
			}
		});
	}

	public Map<String, Object> getNamespace() {
		return bxmlSerializerDTO.getNamespace();
	}

	@SuppressWarnings("unchecked")
	public T getRoot() {
		return (T) bxmlSerializerDTO.getRoot();
	}

	protected abstract void onFailure(Throwable caught);

	protected abstract void onSuceess(T root);
}
