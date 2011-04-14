package gwt.mosaic.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class ElementsInsertedEvent extends GwtEvent<ElementsInsertedHandler> {

	private static Type<ElementsInsertedHandler> TYPE;

	public static void fire(HasElementsInsertedHandlers source, int index,
			int length) {
		if (TYPE != null) {
			ElementsInsertedEvent event = new ElementsInsertedEvent(index,
					length);
			source.fireEvent(event);
		}
	}

	public static Type<ElementsInsertedHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<ElementsInsertedHandler>();
		}
		return TYPE;
	}

	private final int index;
	private final int length;

	protected ElementsInsertedEvent(int index, int length) {
		this.index = index;
		this.length = length;
	}

	/**
	 * @return the index the elements were added to
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the number of elements that were added
	 */
	public int getLength() {
		return length;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<ElementsInsertedHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ElementsInsertedHandler handler) {
		handler.onElementsInserted(this);
	}

}
