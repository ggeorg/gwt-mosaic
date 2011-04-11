package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasKeyAddedHandlers<K> extends HasHandlers {
	HandlerRegistration addKeyAddedHandler(KeyAddedHandler<K> handler);
}
