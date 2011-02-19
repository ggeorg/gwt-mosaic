package gwt.mosaic.client.beans;

import gwt.mosaic.client.wtk.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <tt>BXMLSerializerService</tt>.
 */
public interface BXMLSerializerServiceAsync {
	void readWindow(String resourceName, AsyncCallback<Window> callback)
			throws IllegalArgumentException;
}
