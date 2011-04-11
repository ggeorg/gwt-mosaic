package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasElementUpdateHandlers<E> extends HasHandlers {
	HandlerRegistration addElementUpdateHandler(ElementUpdateHandler<E> handler);
}
