package gwt.mosaic.client.beans;

import gwt.mosaic.client.events.PropertyChangeHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Class for monitoring Java bean property changes.
 */
public class BeanMonitor {
	private final Object bean;
	private final BeanAdapter<?> beanAdapter;

	public BeanMonitor(Object bean) {
		if (bean == null) {
			throw new IllegalArgumentException("bean is null.");
		}

		this.bean = bean;
		this.beanAdapter = BeanAdapterFactory.createFor(bean);
	}

	/**
	 * Returns the bean object that this monitor wraps.
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * Tests whether a property fires change events.
	 * 
	 * @param propertyName
	 *            The property name.
	 * @return <tt>true</tt> if the property fires change events; <tt>false</tt>
	 *         otherwise.
	 */
	public boolean isNotifying(String propertyName) {
		return beanAdapter.isNotifying(propertyName);
	}

	public HandlerRegistration addPropertyChangeHandler(
			PropertyChangeHandler handler) {
		return beanAdapter.addPropertyChangeHandler(handler);
	}
}
