package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface KeyValueUpdateHandler<K, V> extends EventHandler {
	void onKeyValueUpdate(KeyValueUpdateEvent<K, V> event);
}
