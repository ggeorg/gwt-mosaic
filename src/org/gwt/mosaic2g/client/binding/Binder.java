package org.gwt.mosaic2g.client.binding;

import java.io.Serializable;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;

public interface Binder<T extends Serializable> extends HasValueChangeHandlers<T> {

	T get();

	void set(T value);

}
