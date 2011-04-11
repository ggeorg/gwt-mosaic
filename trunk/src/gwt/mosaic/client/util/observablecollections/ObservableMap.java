package gwt.mosaic.client.util.observablecollections;

import gwt.mosaic.client.events.HasKeyAddedHandlers;
import gwt.mosaic.client.events.HasKeyRemovedHandlers;
import gwt.mosaic.client.events.HasKeyValueUpdateHandlers;

import java.util.Map;

/**
 * A <tt>java.util.Map</tt> that notifies listeners of changes to the
 * <tt>java.util.Map</tt>.
 */
public interface ObservableMap<K, V> extends Map<K, V>, HasKeyAddedHandlers<K>,
		HasKeyRemovedHandlers<K>, HasKeyValueUpdateHandlers<K, V> {

}
