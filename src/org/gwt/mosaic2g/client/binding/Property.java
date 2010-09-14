package org.gwt.mosaic2g.client.binding;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class Property<T> implements HasValueChangeHandlers<T> {

	public static final Property<String> valueOf(String value) {
		return new Property<String>(value);
	}

	public static final Property<Integer> valueOf(int value) {
		return new Property<Integer>(value);
	}

	private final Binder<T> binder;

	public Property() {
		this((T) null);
	}

	public Property(T value) {
		this(new AbstractBinder<T>() {
			private T value;

			public T get() {
				return value;
			}

			public void set(T value) {
				if (value != this.value) {
					this.value = value;
					fireValueChangeEvent(this.value);
				}
			}
			
			public boolean isReadOnly() {
				return false;
			}
		});
		binder.set(value);
	}

	public Property(Binder<T> binder) {
		this.binder = binder;
	}

	public T $() {
		return binder.get();
	}

	public void $(T value) {
		binder.set(value);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return binder.addValueChangeHandler(handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		binder.fireEvent(event);
	}

	public Property<T> createBinding(final Getter<T> getter,
			final Setter<T> setter) {
		assert getter != null;
		final Property<T> thiz = this;
		return new Property<T>(new Binder<T>() {
			public T get() {
				return getter.get(thiz.$());
			}

			public void set(T value) {
				if (setter != null) {
					thiz.$(setter.set(value));
				}
			}

			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<T> handler) {
				return thiz.addValueChangeHandler(handler);
			}

			public void fireEvent(GwtEvent<?> event) {
				thiz.fireEvent(event);
			}

			public boolean isReadOnly() {
				return false;
			}
		});
	}

	public Property<T> createBinding() {
		return createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		}, new Setter<T>() {
			public T set(T value) {
				return value;
			}
		});
	}

	public Property<T> createBinding(final Getter<T> getter) {
		return createBinding(getter, null);
	}

	@Override
	public int hashCode() {
		final T value = binder.get();
		return (value == null) ? 0 : value.hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Property other = (Property) obj;
		final T value = binder.get();
		if (value == null) {
			if (other.$() != null) {
				return other.$().equals(obj);
			}
		} else if (!binder.equals(other.binder)) {
			return value.equals(other.$());
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(binder.get());
	}

}
