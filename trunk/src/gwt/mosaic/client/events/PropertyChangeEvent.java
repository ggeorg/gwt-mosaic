package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class PropertyChangeEvent extends GwtEvent<PropertyChangeHandler> {

	private static Type<PropertyChangeHandler> TYPE;

	public static void fire(HasPropertyChangeHandlers source,
			String propertyName) {
		if (TYPE != null) {
			PropertyChangeEvent event = new PropertyChangeEvent(propertyName);
			source.fireEvent(event);
		}
	}

	public static void fireIfNotEqual(HasPropertyChangeHandlers source,
			String propertyName, Object oldValue, Object newValue) {
		if (shouldFire(source, propertyName, oldValue, newValue)) {
			PropertyChangeEvent event = new PropertyChangeEvent(propertyName);
			source.fireEvent(event);
		}
	}

	public static Type<PropertyChangeHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<PropertyChangeHandler>();
		}
		return TYPE;
	}

	protected static boolean shouldFire(HasPropertyChangeHandlers source,
			String propertyName, Object oldValue, Object newValue) {
		return TYPE != null && oldValue != newValue
				&& (oldValue == null || !oldValue.equals(newValue));
	}

	private final String propertyName;

	protected PropertyChangeEvent(String propertyName) {
		this.propertyName = propertyName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<PropertyChangeHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + getPropertyName();
	}

	@Override
	protected void dispatch(PropertyChangeHandler handler) {
		handler.onPropertyChanged(this);
	}
}