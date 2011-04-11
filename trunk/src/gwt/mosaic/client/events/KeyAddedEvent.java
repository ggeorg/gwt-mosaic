package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class KeyAddedEvent<K> extends GwtEvent<KeyAddedHandler<K>> {

	private static Type<KeyAddedHandler<?>> TYPE;

	public static <K> void fire(HasKeyAddedHandlers<K> source, K key) {
		if (TYPE != null) {
			KeyAddedEvent<K> event = new KeyAddedEvent<K>(key);
			source.fireEvent(event);
		}
	}

	public static Type<KeyAddedHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<KeyAddedHandler<?>>();
		}
		return TYPE;
	}

	private final K key;

	protected KeyAddedEvent(K key) {
		this.key = key;
	}

	public K getKey() {
		return key;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<KeyAddedHandler<K>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(KeyAddedHandler<K> handler) {
		handler.onKeyAdded(this);
	}

}
