package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasElementsInsertedHandlers extends HasHandlers {
	HandlerRegistration addElemntInsertedAhndler(ElementsInsertedHandler handler);
}
