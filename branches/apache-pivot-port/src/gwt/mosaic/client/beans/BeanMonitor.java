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

import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.MapListener;
import gwt.mosaic.client.util.ListenerList;

import java.util.Comparator;

/**
 * Class for monitoring Java bean property changes.
 */
public class BeanMonitor<T> {
	private class PropertyChangeListenerList extends
			ListenerList<PropertyChangeListener> implements
			PropertyChangeListener {

		@Override
		public void add(PropertyChangeListener listener) {
			if (isEmpty()) {
				registerBeanListeners();
			}

			super.add(listener);
		}

		@Override
		public void remove(PropertyChangeListener listener) {
			super.remove(listener);

			if (isEmpty()) {
				unregisterBeanListeners();
			}
		}

		@Override
		public void propertyChanged(Object bean, String propertyName) {
			for (PropertyChangeListener listener : this) {
				listener.propertyChanged(bean, propertyName);
			}
		}
	}

	private final BeanAdapter<T> bean;

	private final PropertyChangeListenerList propertyChangeListenerList = new PropertyChangeListenerList();
	private final MapListener<String, Object> mapListener = new MapListener<String, Object>() {
		@Override
		public void valueAdded(Map<String, Object> map, String key) {
			// No-op
		}

		@Override
		public void valueUpdated(Map<String, Object> map, String key,
				Object previousValue) {
			propertyChangeListenerList.propertyChanged(bean, key);
		}

		@Override
		public void valueRemoved(Map<String, Object> map, String key,
				Object value) {
			// No-op
		}

		@Override
		public void mapCleared(Map<String, Object> map) {
			// No-op
		}

		@Override
		public void comparatorChanged(Map<String, Object> map,
				Comparator<String> previousComparator) {
			// No-op
		}
	};

	public BeanMonitor(BeanAdapter<T> bean) {
		this.bean = bean;
	}

	/**
	 * Returns the bean object that this monitor wraps.
	 */
	public BeanAdapter<T> getBean() {
		return bean;
	}

	/**
	 * Tests whether a property fires change events.
	 * 
	 * @param key
	 *            The property name.
	 * 
	 * @return <tt>true</tt> if the property fires change events; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean isNotifying(String key) {
		return !bean.isReadOnly(key);
	}

	/**
	 * Registers event listeners on the bean so that the dictionary can fire
	 * property change events and report which properties can fire change
	 * events.
	 */
	private void registerBeanListeners() {
		ListenerList<MapListener<String, Object>> listenersMap = bean
				.getMapListeners();

		listenersMap.add(mapListener);
	}

	/**
	 * Un-registers event listeners on the bean.
	 */
	private void unregisterBeanListeners() {
		ListenerList<MapListener<String, Object>> listenersMap = bean
				.getMapListeners();

		listenersMap.remove(mapListener);
	}

	public PropertyChangeListenerList getPropertyChangeListenerList() {
		return propertyChangeListenerList;
	}

}
