package gwt.mosaic.client.beans;

import gwt.mosaic.client.events.HasPropertyChangeHandlers;
import gwt.mosaic.client.events.KeyAddedEvent;
import gwt.mosaic.client.events.KeyAddedHandler;
import gwt.mosaic.client.events.KeyRemovedEvent;
import gwt.mosaic.client.events.KeyRemovedHandler;
import gwt.mosaic.client.events.KeyValueUpdateEvent;
import gwt.mosaic.client.events.KeyValueUpdateHandler;
import gwt.mosaic.client.events.PropertyChangeEvent;
import gwt.mosaic.client.events.PropertyChangeHandler;
import gwt.mosaic.client.util.ImmutableIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Exposes Java bean properties of an object via the <tt>java.util.Map</tt>
 * interface.
 */
public abstract class AbstractBeanAdapter<T> implements BeanAdapter<T> {

	private transient HandlerManager handlerManager;

	/**
	 * Property iterator. Returns a value for each getter method and public,
	 * non-final field defined by the bean.
	 */
	protected abstract class PropertyIterator implements Iterator<String> {

	}

	protected T bean;

	protected final HashMap<String, GetterMethod> getterMap = new HashMap<String, GetterMethod>();
	protected final HashMap<String, SetterMethod> setterMap = new HashMap<String, SetterMethod>();

	protected final HashSet<String> notifyingProperties = new HashSet<String>();

	/**
	 * Returns the bean object this dictionary wraps.
	 * 
	 * @return The bean object, or <tt>null</null> if no bean has been set.
	 */
	@Override
	public T getBean() {
		return bean;
	}

	/**
	 * Creates a new bean dictionary.
	 * 
	 * @param bean
	 *            The bean object to wrap.
	 */
	@Override
	public void setBean(T bean) {
		if (bean == null) {
			throw new IllegalArgumentException("bean is null.");
		}
		if (this.bean != null) {
			throw new IllegalStateException("bean is already set.");
		}

		this.bean = bean;

		if (this.bean instanceof HasPropertyChangeHandlers) {
			HasPropertyChangeHandlers _bean = ((HasPropertyChangeHandlers) this.bean);
			_bean.addPropertyChangeHandler(new PropertyChangeHandler() {
				@Override
				public void onPropertyChanged(PropertyChangeEvent event) {
					fireEvent(event);
				}
			});
		}
	}

	/**
	 * Invokes the getter method for the given property.
	 * 
	 * @param key
	 *            The property name.
	 * 
	 * @return The value returned by the method, or <tt>null</tt> if no such
	 *         method exists.
	 */
	@Override
	public Object get(Object _key) {
		if (_key == null) {
			throw new IllegalArgumentException("key is null.");
		}
		String key = _key.toString();
		if (key.length() == 0) {
			throw new IllegalArgumentException("key is empty.");
		}
		return invokeGetterMethod(key);
	}

	protected Object invokeGetterMethod(String propertyName) {
		GetterMethod getterMethod = getterMap.get(propertyName);
		if (getterMethod != null) {
			return getterMethod.invokeGetterMethod();
		}
		return null;
	}

	/**
	 * Invokes the setter method for the given property. The method signature is
	 * determined by the type of the value. If the value is <tt>null</tt>, the
	 * return type of the getter method is used.
	 * 
	 * @param key
	 *            The property name.
	 * @param value
	 *            The new property value.
	 * @return Returns <tt>null</tt>, since returning the previous value would
	 *         require a call to the getter method, which may not be an
	 *         efficient operation.
	 * 
	 * @throws PropertyNotFoundException
	 *             If the given property does not exist or is read-only.
	 */
	@Override
	public Object put(String key, Object value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null.");
		}

		if (key.length() == 0) {
			throw new IllegalArgumentException("key is empty.");
		}

		invokeSetterMethod(key, value);

		return null;
	}

	protected void invokeSetterMethod(String propertyName, Object value) {
		SetterMethod setterMethod = setterMap.get(propertyName + "#"
				+ value.getClass().getName());
		if (setterMethod != null) {
			setterMethod.invokeSetterMethod(value);
			fireKeyValueUpdatedEvent(propertyName, null);
		} else if (value != null) {
			setterMethod = setterMap.get(propertyName);
			if (setterMethod != null) {
				setterMethod.invokeSetterMethod(value);
				fireKeyValueUpdatedEvent(propertyName, null);
			}
		}
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public int size() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isEmpty() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Verifies the existence of a property. The property must have a getter
	 * method; write-only properties are not supported.
	 * 
	 * @param key
	 *            The property name.
	 * 
	 * @return <tt>true</tt> if the property exists; <tt>false</tt>, otherwise.
	 */
	@Override
	public boolean containsKey(Object _key) {
		if (_key == null) {
			throw new IllegalArgumentException("key is null.");
		}
		String key = _key.toString();
		if (key.length() == 0) {
			throw new IllegalArgumentException("key is empty.");
		}
		return invokeGetterMethod(key) != null;
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean containsValue(Object value)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Object remove(Object key) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void clear() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		return getterMap.keySet();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Collection<Object> values() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet()
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Tests the read-only state of a property.
	 * 
	 * @param key
	 *            The property name.
	 * 
	 * @return <tt>true</tt> if the property is read-only; <tt>false</tt>,
	 *         otherwise.
	 */
	@Override
	public boolean isReadOnly(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null.");
		}
		if (key.length() == 0) {
			throw new IllegalArgumentException("key is empty.");
		}
		return isReadOnlyImpl(key);
	}

	protected boolean isReadOnlyImpl(String propertyName) {
		return !setterMap.containsKey(propertyName);
	}

	@Override
	public Iterator<String> getNotifyingProperties() {
		return new ImmutableIterator<String>(notifyingProperties.iterator());
	}

	@Override
	public boolean isNotifying(String key) {
		return notifyingProperties.contains(key);
	}

	@Override
	public HandlerRegistration addKeyAddedHandler(
			KeyAddedHandler<String> handler) {
		return ensureHandlers().addHandler(KeyAddedEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addKeyRemovedHandler(
			KeyRemovedHandler<String> handler) {
		return ensureHandlers().addHandler(KeyRemovedEvent.getType(), handler);
	}

	@Override
	public HandlerRegistration addKeyValueUpdateHandler(
			KeyValueUpdateHandler<String, Object> handler) {
		return ensureHandlers().addHandler(KeyValueUpdateEvent.getType(),
				handler);
	}

	@Override
	public HandlerRegistration addPropertyChangeHandler(
			PropertyChangeHandler handler) {
		return ensureHandlers().addHandler(PropertyChangeEvent.getType(),
				handler);
	}

	private HandlerManager ensureHandlers() {
		return handlerManager == null ? handlerManager = new HandlerManager(
				this) : handlerManager;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if (handlerManager != null) {
			handlerManager.fireEvent(event);
		}
	}

	protected void fireKeyValueUpdatedEvent(String key, T previousValue) {
		KeyValueUpdateEvent.fire(this, key, previousValue);
	}

}
