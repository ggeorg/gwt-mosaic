package gwt.mosaic.client.beans;

import com.google.gwt.core.client.GWT;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;

public abstract class BeanAdapterFactory<T> {
	private static Map<Class<?>, BeanAdapterFactory<?>> reg;

	public static BeanAdapter<?> createFor(final Object value) {
			BeanAdapterFactory<?> factory = reg.get(value.getClass());
			if (factory != null) {
				return factory.create(value);
			} else {
				return null;
			}
	}

	public static void register(Class<?> beanType, BeanAdapterFactory<?> factory) {
		if (reg == null) {
			reg = new HashMap<Class<?>, BeanAdapterFactory<?>>();
		}
		reg.put(beanType, factory);
	}

	protected abstract BeanAdapter<T> create(Object value);
}
