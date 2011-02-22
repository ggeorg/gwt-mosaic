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
import gwt.mosaic.client.util.ListenerList;

import java.util.Iterator;

/**
 * @param <T>
 *            The Java bean type.
 */
public interface BeanAdapter<T> extends Map<String, Object> {

	public T getBean();

	public void setBean(T bean);

	public boolean isReadOnly(String key);

	public Iterator<String> getNotifyingProperties();

	public Class<?> getType(String key);

	public boolean isNotifying(String key);

	/**
	 * Registers event listeners on the bean so that the dictionary can fire
	 * property change events and report which properties can fire change
	 * events.
	 */
	public void registerBeanListeners();

	/**
	 * Un-registers event listeners on the bean.
	 */
	public void unregisterBeanListeners();

	public ListenerList<PropertyChangeListener> getPropertyChangeListeners();

}
