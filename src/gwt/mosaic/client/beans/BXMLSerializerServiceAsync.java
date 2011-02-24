package gwt.mosaic.client.beans;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <tt>BXMLSerializerService</tt>.
 */
public interface BXMLSerializerServiceAsync {
	void readObject(String resourceName, AsyncCallback<BXMLSerializerDTO> callback)
			throws IllegalArgumentException;
}
