package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasKeyRemovedHandlers<K> extends HasHandlers {
	HandlerRegistration addKeyRemovedHandler(KeyRemovedHandler<K> handler);
}