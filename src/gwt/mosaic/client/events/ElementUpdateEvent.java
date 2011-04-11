package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class ElementUpdateEvent<E> extends GwtEvent<ElementUpdateHandler<E>> {

	private static Type<ElementUpdateHandler<?>> TYPE;

	public static <E> void fire(HasElementUpdateHandlers<E> source, int index,
			E previousValue) {
		if (TYPE != null) {
			ElementUpdateEvent<E> event = new ElementUpdateEvent<E>(index,
					previousValue);
			source.fireEvent(event);
		}
	}

	public static <E> void fire(HasElementUpdateHandlers<E> source, int index,
			E oldValue, E newValue) {
		if (shouldFire(source, index, oldValue, newValue)) {
			ElementUpdateEvent<E> event = new ElementUpdateEvent<E>(index,
					oldValue);
			source.fireEvent(event);
		}
	}

	protected static <E> boolean shouldFire(HasElementUpdateHandlers<E> source,
			int index, E oldValue, E newValue) {
		return TYPE != null && oldValue != newValue
				&& (oldValue == null || !oldValue.equals(newValue));
	}

	public static Type<ElementUpdateHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ElementUpdateHandler<?>>();
		}
		return TYPE;
	}

	private final int index;
	private final E previousValue;

	protected ElementUpdateEvent(int index, E previousValue) {
		this.index = index;
		this.previousValue = previousValue;
	}

	public int getIndex() {
		return index;
	}

	public E getPreviousValue() {
		return previousValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<ElementUpdateHandler<E>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ElementUpdateHandler<E> handler) {
		handler.onElementUpdate(this);
	}

}
