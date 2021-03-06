package gwt.mosaic.client.beans;

import java.util.HashMap;
import java.util.Map;

public abstract class BeanAdapterFactory<T> {
	private static final Map<Class<?>, BeanAdapterFactory<?>> beanAdapterFactoryMap = new HashMap<Class<?>, BeanAdapterFactory<?>>();

	public static BeanAdapter<?> createFor(Object value) {
		BeanAdapterFactory<?> factory = beanAdapterFactoryMap.get(value
				.getClass());
		return (factory != null) ? factory.create(value) : null;
	}

	public static void register(Class<?> beanType, BeanAdapterFactory<?> factory) {
		beanAdapterFactoryMap.put(beanType, factory);
	}

	protected abstract BeanAdapter<T> create(Object value);
}
