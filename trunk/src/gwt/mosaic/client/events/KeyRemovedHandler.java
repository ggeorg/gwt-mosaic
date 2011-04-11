package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface KeyRemovedHandler<K> extends EventHandler {
	void onKeyRemoved(KeyRemovedEvent<K> event);
}
