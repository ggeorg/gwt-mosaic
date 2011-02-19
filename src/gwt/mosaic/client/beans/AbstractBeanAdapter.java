/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.beans;

import java.util.Comparator;
import java.util.Iterator;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.MapListener;
import gwt.mosaic.client.util.ImmutableIterator;
import gwt.mosaic.client.util.ListenerList;

/**
 * Exposes Java bean properties of an object via the {@link Map} interface. A
 * call to {@link Map#get(Object} invokes the getter for the corresponding
 * property, and a call to {@link Map#put(Object, Object)} invokes the
 * property's setter.
 * <p>
 * Properties may provide multiple setters; the appropriate setter to invoke is
 * determined by the type of the value being set. If the value is <tt>null</tt>,
 * the return type of the getter method is used.
 */
public class AbstractBeanAdapter<T> implements BeanAdapter<T> {

	/**
	 * Property iterator. Returns a value for each getter method and public,
	 * non-final field defined by the bean.
	 */
	protected abstract class PropertyIterator implements Iterator<String> {
	}

	private T bean;

	private MapListenerList<String, Object> mapListeners = new MapListenerList<String, Object>();

	protected HashMap<String, GetterMethod> getterMap = new HashMap<String, GetterMethod>();
	protected HashMap<String, SetterMethod> setterMap = new HashMap<String, SetterMethod>();

	/**
	 * Creates a new bean dictionary.
	 * 
	 * @param bean
	 *            The bean object to wrap.
	 */
	public void setBean(T bean) {
		if (bean == null) {
			throw new IllegalArgumentException();
		}

		this.bean = bean;
	}

	/**
	 * Returns the bean object this dictionary wraps.
	 * 
	 * @return The bean object, or <tt>null</null> if no bean has been set.
	 */
	public T getBean() {
		return bean;
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
	public Object get(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null.");
		}

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

			mapListeners.valueUpdated(this, propertyName, null);
		} else if (value != null) {
			setterMethod = setterMap.get(propertyName);
			if (setterMethod != null) {
				setterMethod.invokeSetterMethod(value);

				mapListeners.valueUpdated(this, propertyName, null);
			}
		}
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Object remove(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void clear() {
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
	public boolean containsKey(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null.");
		}

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
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 *             This method is not supported.
	 */
	@Override
	public int getCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comparator<String> getComparator() {
		return null;
	}

	/**
	 * @throws UnsupportedOperationException
	 *             This method is not supported.
	 */
	@Override
	public void setComparator(Comparator<String> comparator) {
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

	/**
	 * Returns an iterator over the bean's properties.
	 * 
	 * @return A property iterator for this bean.
	 */
	@Override
	public Iterator<String> iterator() {
		return new ImmutableIterator<String>(getterMap.iterator());
	}

	@Override
	public ListenerList<MapListener<String, Object>> getMapListeners() {
		return mapListeners;
	}

	@Override
	public Iterator<String> getNotifyingProperties() {
		return new ImmutableIterator<String>(setterMap.iterator());
	}

	@Override
	public Class<?> getType(String key) {
		throw new UnsupportedOperationException();
	}
}
