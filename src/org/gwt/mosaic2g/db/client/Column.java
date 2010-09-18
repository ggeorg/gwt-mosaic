/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic2g.db.client;

import org.gwt.mosaic2g.binding.client.Binder;
import org.gwt.mosaic2g.binding.client.Property;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * 
 * @param <T>
 * 
 * @author ggeorg
 */
public abstract class Column<T> implements HasValueChangeHandlers<T> {

	private final String name;

	private final Property<T> value;

	private T cachedValue;

	public Column(String name) {
		this(name, (T) null);
	}

	public Column(String name, T value) {
		this(name, new Property<T>(value));
	}

	public Column(String name, Binder<T> binder) {
		this(name, new Property<T>(binder));
	}

	public Column(String name, Property<T> value) {
		this.name = name;
		this.value = value;
		init();
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return value.addValueChangeHandler(handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		value.fireEvent(event);
	}

	protected void init() {
		// do nothing
	}

	// ---------------------------------------------------------------------
	public String getName() {
		return name;
	}

	public Property<T> getValue() {
		return value;
	}

	public void restore() {
		value.$(cachedValue);
	}

	protected void reset() {
		cachedValue = value.$();
		ValueChangeEvent.fire(value, value.$());
	}

	public abstract Property<String> getDisplayValue();

	// ---------------------------------------------------------------------
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Column other = (Column) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
