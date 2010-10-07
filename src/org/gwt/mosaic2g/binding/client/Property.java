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

import java.util.Date;

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
public final class Property<T> implements HasValueChangeHandlers<T> {

	public static Property<Boolean> valueOf(boolean value) {
		return new Property<Boolean>(value);
	}

	public static Property<Byte> valueOf(byte value) {
		return new Property<Byte>(value);
	}

	public static Property<Character> valueOf(char value) {
		return new Property<Character>(value);
	}

	public static final Property<Float> valueOf(float value) {
		return new Property<Float>(value);
	}

	public static final Property<Date> valueOf(Date value) {
		return new Property<Date>(value);
	}

	public static final Property<Double> valueOf(double value) {
		return new Property<Double>(value);
	}

	public static final Property<Integer> valueOf(int value) {
		return new Property<Integer>(value);
	}

	public static final Property<Long> valueOf(long value) {
		return new Property<Long>(value);
	}

	public static final Property<Short> valueOf(short value) {
		return new Property<Short>(value);
	}

	public static final Property<String> valueOf(String value) {
		return new Property<String>(value);
	}

	// ---------------------------------------------------------------------
	private final Binder<T> binder;

	public Property() {
		this((T) null);
	}

	public Property(T value) {
		this(new AbstractBinder<T>() {
			private T value;

			@Override
			public T get() {
				return value;
			}

			@Override
			public void set(T value) {
				if (value != this.value) {
					this.value = value;
					fireValueChangeEvent(this.value);
				}
			}

			@Override
			public boolean isReadOnly() {
				return false;
			}
		});
		binder.set(value);
	}

	public Property(Binder<T> binder) {
		this.binder = binder;
	}

	// ---------------------------------------------------------------------
	public T $() {
		return binder.get();
	}

	public Property<T> $(T value) {
		binder.set(value);
		return this;
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return binder.addValueChangeHandler(handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		binder.fireEvent(event);
	}

	// ---------------------------------------------------------------------
	private HandlerRegistration otherHR = null;
	private HandlerRegistration thizHR = null;

	public void bindReadOnly(final Property<T> other) {
		if (other == null) {
			throw new NullPointerException("Can't bind to 'null'");
		}
		if (otherHR != null) {
			otherHR.removeHandler();
			otherHR = null;
		}
		if (thizHR != null) {
			thizHR.removeHandler();
			thizHR = null;
		}
		final Property<T> thiz = this;
		other.addValueChangeHandler(new ValueChangeHandler<T>() {
			public void onValueChange(ValueChangeEvent<T> event) {
				thiz.$(other.$());
			}
		});
	}
	
	public void bind(final Property<T> other) {
		if (other == null) {
			throw new NullPointerException("Can't bind to 'null'");
		}
		if (otherHR != null) {
			otherHR.removeHandler();
			otherHR = null;
		}
		if (thizHR != null) {
			thizHR.removeHandler();
			thizHR = null;
		}
		final Property<T> thiz = this;
		otherHR = other.addValueChangeHandler(new ValueChangeHandler<T>() {
			public void onValueChange(ValueChangeEvent<T> event) {
				thiz.$(other.$());
			}
		});
		thizHR = thiz.addValueChangeHandler(new ValueChangeHandler<T>() {
			public void onValueChange(ValueChangeEvent<T> event) {
				other.$(thiz.$());
			}
		});
		$(other.$());
	}

	// ---------------------------------------------------------------------
	public Property<T> createReadOnlyBinding() {
		return createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		});
	}

	public Property<T> createBinding() {
		return this.createBinding((Validator<T>) null);
	}

	public Property<T> createBinding(final Validator<T> validator) {
		return createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		}, new Setter<T>() {
			public T set(T value) {
				// TODO validate
				return value;
			}
		});
	}

	// ---------------------------------------------------------------------
	public Property<T> createBinding(final Getter<T> getter) {
		return createBinding(getter, null, (Validator<T>) null);
	}

	public Property<T> createBinding(final Getter<T> getter,
			final Validator<T> validator) {
		return createBinding(getter, null, (Validator<T>) null);
	}

	public Property<T> createBinding(final Getter<T> getter,
			final Setter<T> setter) {
		return this.createBinding(getter, setter, (Validator<T>) null);
	}

	public Property<T> createBinding(final Getter<T> getter,
			final Setter<T> setter, final Validator<T> validator) {
		assert getter != null;
		final Property<T> thiz = this;
		return new Property<T>(new Binder<T>() {
			public T get() {
				return getter.get(thiz.$());
			}

			public void set(T value) {
				if (setter != null) {
					thiz.$(setter.set(value));
				} else {
					throw new UnsupportedOperationException(
							"Property is read-only.");
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
				return setter == null;
			}
		});
	}

	// ---------------------------------------------------------------------

	public <T2> Property<T2> createReadOnlyBinding(
			final Converter<T, T2> converter) {
		return this.createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		}, null, converter, (Validator<T>) null);
	}

	public <T2> Property<T2> createBinding(final Converter<T, T2> converter) {
		return createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		}, new Setter<T>() {
			public T set(T value) {
				// TODO validate
				return value;
			}
		}, converter, (Validator<T>) null);
	}

	public <T2> Property<T2> createBinding(final Converter<T, T2> converter,
			final Validator<T> validator) {
		return createBinding(new Getter<T>() {
			public T get(T value) {
				return value;
			}
		}, new Setter<T>() {
			public T set(T value) {
				// TODO validate
				return value;
			}
		}, converter, validator);
	}

	public <T2> Property<T2> createBinding(final Getter<T> getter,
			final Converter<T, T2> converter) {
		return this.createBinding(getter, null, converter, (Validator<T>) null);
	}

	public <T2> Property<T2> createBinding(final Getter<T> getter,
			final Setter<T> setter, final Converter<T, T2> converter) {
		return this.createBinding(getter, setter, converter,
				(Validator<T>) null);
	}

	public <T2> Property<T2> createBinding(final Getter<T> getter,
			final Setter<T> setter, final Converter<T, T2> converter,
			final Validator<T> validator) {
		assert getter != null;
		assert converter != null;
		final Property<T> thiz = this;
		return new Property<T2>(new AbstractBinder<T2>() {

			@Override
			public T2 get() {
				return converter.convertForward(getter.get(thiz.$()));
			}

			@Override
			public void set(T2 value) {
				if (setter != null) {
					// TODO validate
					thiz.$(setter.set(converter.convertReverse(value)));
				} else {
					throw new UnsupportedOperationException(
							"Property is read-only.");
				}
			}

			public HandlerRegistration addValueChangeHandler(
					final ValueChangeHandler<T2> handler) {
				return new PropertyHandlerRegistration(
						super.addValueChangeHandler(handler),
						thiz.addValueChangeHandler(new ValueChangeHandler<T>() {
							public void onValueChange(ValueChangeEvent<T> event) {
								fireValueChangeEvent(converter
										.convertForward(event.getValue()));
							}
						}));
			}

			@Override
			public boolean isReadOnly() {
				return setter == null;
			}
		});
	}

	// ---------------------------------------------------------------------
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
