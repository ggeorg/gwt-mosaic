package gwt.mosaic.client.events;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class ElementsRemovedEvent<E> extends GwtEvent<ElementsRemovedHandler<E>> {

	private static Type<ElementsRemovedHandler<?>> TYPE;

	public static <E> void fire(HasElementsRemovedHandlers<E> source, int index,
			List<E> oldElements) {
		if (TYPE != null) {
			ElementsRemovedEvent<E> event = new ElementsRemovedEvent<E>(index, oldElements);
			source.fireEvent(event);
		}
	}

	public static Type<ElementsRemovedHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ElementsRemovedHandler<?>>();
		}
		return TYPE;
	}

	private final int index;
	private final List<E> oldElements;

	protected ElementsRemovedEvent(int index, List<E> oldElements) {
		this.index = index;
		this.oldElements = oldElements;
	}

	/**
	 * @return the index the elements were removed from
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return a list containing the elements that were removed
	 */
	public List<E> getOldElements() {
		return oldElements;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public GwtEvent.Type<ElementsRemovedHandler<E>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ElementsRemovedHandler<E> handler) {
		handler.onElementsRemoved(this);
	}

}
