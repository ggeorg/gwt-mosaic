package org.gwt.mosaic2g.client.binding;

import java.io.Serializable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractBinder<T extends Serializable> implements
		Binder<T> {

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
		// do nothing
	}

}
