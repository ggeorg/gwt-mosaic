package gwt.mosaic.client.beans;

import gwt.mosaic.client.wtk.Window;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for BXMLSerializer service.
 */
@RemoteServiceRelativePath("bxml")
public interface BXMLSerializerService extends RemoteService {
	Window readWindow(String resourceName) throws IllegalArgumentException;
}
