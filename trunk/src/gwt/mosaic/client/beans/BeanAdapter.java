package gwt.mosaic.client.beans;

import gwt.mosaic.client.events.HasPropertyChangeHandlers;
import gwt.mosaic.client.util.observablecollections.ObservableMap;

import java.util.Iterator;

/**
 * @parm T The Java bean type.
 */
public interface BeanAdapter<T> extends ObservableMap<String, Object>,
		HasPropertyChangeHandlers {

	public T getBean();

	public void setBean(T bean);

	public boolean isReadOnly(String key);

	public Iterator<String> getNotifyingProperties();

	public boolean isNotifying(String key);

}
