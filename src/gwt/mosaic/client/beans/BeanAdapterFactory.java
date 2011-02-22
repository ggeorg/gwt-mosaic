package gwt.mosaic.client.beans;

import java.util.Comparator;
import java.util.Iterator;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.MapListener;
import gwt.mosaic.client.util.ListenerList;

public abstract class BeanAdapterFactory<T> {
	private static Map<Class<?>, BeanAdapterFactory<?>> reg;

	public static BeanAdapter<?> createFor(final Object value) {
		if (value instanceof Map<?, ?>) {
			BeanAdapter<Map<?, ?>> adapter = new BeanAdapter<Map<?, ?>>() {
				Map<String, Object> map;

				@Override
				public Object put(String key, Object value) {
					return map.put(key, value);
				}

				@Override
				public Object remove(String key) {
					return map.remove(key);
				}

				@Override
				public void clear() {
					map.clear();
				}

				@Override
				public int getCount() {
					return map.getCount();
				}

				@Override
				public void setComparator(Comparator<String> comparator) {
					map.setComparator(comparator);
				}

				@Override
				public ListenerList<MapListener<String, Object>> getMapListeners() {
					return map.getMapListeners();
				}

				@Override
				public Object get(String key) {
					return map.get(key);
				}

				@Override
				public boolean containsKey(String key) {
					return map.containsKey(key);
				}

				@Override
				public boolean isEmpty() {
					return map.isEmpty();
				}

				@Override
				public Comparator<String> getComparator() {
					return map.getComparator();
				}

				@Override
				public Iterator<String> iterator() {
					return map.iterator();
				}

				@Override
				public Map<?, ?> getBean() {
					return map;
				}

				@SuppressWarnings("unchecked")
				@Override
				public void setBean(Map<?, ?> bean) {
					map = (Map<String, Object>) bean;
				}

				@Override
				public boolean isReadOnly(String key) {
					return false;
				}

				@Override
				public Iterator<String> getNotifyingProperties() {
					return map.iterator();
				}

				@Override
				public Class<?> getType(String key) {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean isNotifying(String key) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void registerBeanListeners() {
					// TODO Auto-generated method stub

				}

				@Override
				public void unregisterBeanListeners() {
					// TODO Auto-generated method stub

				}

				@Override
				public ListenerList<PropertyChangeListener> getPropertyChangeListeners() {
					// TODO Auto-generated method stub
					return null;
				}
			};
			adapter.setBean((Map<?, ?>) value);

			return adapter;

		} else {
			BeanAdapterFactory<?> factory = reg.get(value.getClass());
			if (factory != null) {
				return factory.create(value);
			} else {
				throw new NullPointerException("BeanAdapterFactory for class '"
						+ value.getClass() + "' is not registered.");
			}
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
