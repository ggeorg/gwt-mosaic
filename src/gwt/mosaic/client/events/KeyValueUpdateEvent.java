package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class KeyValueUpdateEvent<K, V> extends
		GwtEvent<KeyValueUpdateHandler<K, V>> {

	private static Type<KeyValueUpdateHandler<?, ?>> TYPE;

	public static <K, V> void fire(HasKeyValueUpdateHandlers<K, V> source,
			K key, V previousValue) {
		if (TYPE != null) {
			KeyValueUpdateEvent<K, V> event = new KeyValueUpdateEvent<K, V>(
					key, previousValue);
			source.fireEvent(event);
		}
	}

	public static <K, V> void fire(HasKeyValueUpdateHandlers<K, V> source,
			K key, V oldValue, V newValue) {
		if (shouldFire(source, key, oldValue, newValue)) {
			KeyValueUpdateEvent<K, V> event = new KeyValueUpdateEvent<K, V>(
					key, oldValue);
			source.fireEvent(event);
		}
	}

	protected static <K, V> boolean shouldFire(
			HasKeyValueUpdateHandlers<K, V> source, K key, V oldValue,
			V newValue) {
		return TYPE != null && oldValue != newValue
				&& (oldValue == null || !oldValue.equals(newValue));
	}

	public static Type<KeyValueUpdateHandler<?, ?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<KeyValueUpdateHandler<?, ?>>();
		}
		return TYPE;
	}

	private final K key;
	private final V previousValue;

	protected KeyValueUpdateEvent(K key, V previousValue) {
		this.key = key;
		this.previousValue = previousValue;
	}

	public K getKey() {
		return key;
	}

	public V getPreviousValue() {
		return previousValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<KeyValueUpdateHandler<K, V>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(KeyValueUpdateHandler<K, V> handler) {
		handler.onKeyValueUpdate(this);
	}

}
