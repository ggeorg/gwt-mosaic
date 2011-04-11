package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class KeyRemovedEvent<K> extends GwtEvent<KeyRemovedHandler<K>> {

	private static Type<KeyRemovedHandler<?>> TYPE;

	public static <K> void fire(HasKeyRemovedHandlers<K> source, K key) {
		if (TYPE != null) {
			KeyRemovedEvent<K> event = new KeyRemovedEvent<K>(key);
			source.fireEvent(event);
		}
	}
	
	public static Type<KeyRemovedHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<KeyRemovedHandler<?>>();
		}
		return TYPE;
	}

	private final K key;

	protected KeyRemovedEvent(K key) {
		this.key = key;
	}

	public K getKey() {
		return key;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<KeyRemovedHandler<K>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(KeyRemovedHandler<K> handler) {
		handler.onKeyRemoved(this);
	}

}
