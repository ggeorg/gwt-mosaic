package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface KeyAddedHandler<K> extends EventHandler {
	void onKeyAdded(KeyAddedEvent<K> event);
}
