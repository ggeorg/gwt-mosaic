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
package org.gwt.mosaic2g.binding.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 *
 * @param <T>
 * 
 * @author ggeorg
 */
public abstract class AbstractBinder<T> implements Binder<T> {

	private transient HandlerManager handlerManager;

	public AbstractBinder() {
		init();
	}

	protected void init() {
		// do nothing
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return ensureHandlers().addHandler(ValueChangeEvent.getType(), handler);
	}

	private HandlerManager ensureHandlers() {
		return handlerManager == null ? handlerManager = new HandlerManager(
				this) : handlerManager;
	}

	public void fireEvent(GwtEvent<?> event) {
		if (handlerManager != null) {
			handlerManager.fireEvent(event);
		}
	}

	protected void fireValueChangeEvent(T value) {
		ValueChangeEvent.fire(this, value);
	}

	protected int getHandlerCount(Type<?> type) {
		return handlerManager == null ? 0 : handlerManager
				.getHandlerCount(type);
	}

	public abstract T get();

	public void set(T value) {
		throw new UnsupportedOperationException("Property is read-only.");
	}
	
	public boolean isReadOnly() {
		return true;
	}

}
