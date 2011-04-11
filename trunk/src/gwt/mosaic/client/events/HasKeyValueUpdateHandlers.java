package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasKeyValueUpdateHandlers<K, V> extends HasHandlers {
	HandlerRegistration addKeyValueUpdateHandler(
			KeyValueUpdateHandler<K, V> handler);
}
