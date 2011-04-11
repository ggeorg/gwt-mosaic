package gwt.mosaic.client.util;

import java.util.Iterator;

/**
 * Immutable implementation of the {@link Iterator} interface.
 */
public class ImmutableIterator<T> implements Iterator<T> {
	private Iterator<T> iterator;

	public ImmutableIterator(Iterator<T> iterator) {
		if (iterator == null) {
			throw new IllegalArgumentException("iterator is null.");
		}
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}