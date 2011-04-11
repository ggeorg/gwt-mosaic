package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ElementUpdateHandler<E> extends EventHandler {
	void onElementUpdate(ElementUpdateEvent<E> event);
}
