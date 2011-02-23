package gwt.mosaic.server;

import gwt.mosaic.client.beans.BXMLSerializerResponse;
import gwt.mosaic.client.beans.BXMLSerializerService;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.server.beans.BXMLSerializer;

import java.io.InputStream;
import java.io.Serializable;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BXMLSerializerServiceImpl extends RemoteServiceServlet implements
		BXMLSerializerService {

	@SuppressWarnings("unchecked")
	@Override
	public BXMLSerializerResponse readWindow(String resourceName)
			throws IllegalArgumentException {
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(resourceName);

		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		Window window;
		try {
			window = (Window) bxmlSerializer.readObject(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}

		return new BXMLSerializerResponse(window,
				(Map<String, ? extends Serializable>) bxmlSerializer
						.getNamespace());
	}

}
