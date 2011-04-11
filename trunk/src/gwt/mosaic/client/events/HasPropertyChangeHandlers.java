package gwt.mosaic.client.events;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasPropertyChangeHandlers extends HasHandlers {
	HandlerRegistration addPropertyChangeHandler(PropertyChangeHandler handler);
}
