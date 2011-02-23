package gwt.mosaic.client.beans;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for BXMLSerializer service.
 */
@RemoteServiceRelativePath("bxml")
public interface BXMLSerializerService extends RemoteService {
	BXMLSerializerResponse readWindow(String resourceName) throws IllegalArgumentException;
}
