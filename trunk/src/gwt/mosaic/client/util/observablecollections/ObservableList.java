package gwt.mosaic.client.util.observablecollections;

import gwt.mosaic.client.events.HasElementUpdateHandlers;
import gwt.mosaic.client.events.HasElementsInsertedHandlers;
import gwt.mosaic.client.events.HasElementsRemovedHandlers;

import java.util.List;

/**
 * A <tt>java.util.List</tt> that notifies listeners of changes to the
 * <tt>java.util.List</tt>.
 */
public interface ObservableList<E> extends List<E>,
		HasElementsInsertedHandlers, HasElementsRemovedHandlers<E>,
		HasElementUpdateHandlers<E> {
}
