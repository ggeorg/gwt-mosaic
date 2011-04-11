package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasElementsRemovedHandlers<E> extends HasHandlers {
	HandlerRegistration addElemntRemovedAhndler(ElementsRemovedHandler<E> handler);
}
