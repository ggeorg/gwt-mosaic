package org.gwt.mosaic2g.client.binding;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;

public interface Binder<T> extends HasValueChangeHandlers<T> {

	T get();

	void set(T value);
	
	boolean isReadOnly();
	
}
