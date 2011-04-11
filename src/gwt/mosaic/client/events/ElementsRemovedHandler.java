package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ElementsRemovedHandler<E> extends EventHandler {
	void onElementsRemoved(ElementsRemovedEvent<E> event);
}
