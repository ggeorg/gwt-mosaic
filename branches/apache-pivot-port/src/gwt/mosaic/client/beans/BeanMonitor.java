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

import gwt.mosaic.client.util.ListenerList;

/**
 * Class for monitoring Java bean property changes.
 */
public class BeanMonitor {
	private final Object bean;
	private final BeanAdapter<?> beanAdapter;

	public BeanMonitor(Object bean) {
		if (bean == null) {
			throw new IllegalArgumentException();
		}

		this.bean = bean;
		this.beanAdapter = BeanAdapterFactory.createFor(bean);
	}

	/**
	 * Returns the bean object that this monitor wraps.
	 */
	public Object getBean() {
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
		return beanAdapter.isNotifying(key);
	}

	public ListenerList<PropertyChangeListener> getPropertyChangeListeners() {
		return beanAdapter.getPropertyChangeListeners();
	}

}
