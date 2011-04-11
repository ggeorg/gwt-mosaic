package gwt.mosaic.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface PropertyChangeHandler extends EventHandler {
	void onPropertyChanged(PropertyChangeEvent event);
}
