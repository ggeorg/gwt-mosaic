/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.collections;

import gwt.mosaic.client.util.ListenerList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implementation of the {@link List} interface that is backed by an
 * <tt>java.lang.ArrayList</tt>.
 * <p>
 * NOTE This class is not thread-safe.
 */
@SuppressWarnings("serial")
public class ArrayList<T> implements List<T>, Serializable {
	private class ArrayListItemIterator implements ItemIterator<T> {
		private int index = 0;
		private int modificationCount;
		private boolean forward = true;

		public ArrayListItemIterator() {
			modificationCount = ArrayList.this.modificationCount;
		}

		@Override
		public boolean hasNext() {
			if (modificationCount != ArrayList.this.modificationCount) {
				throw new ConcurrentModificationException();
			}

			return (index < items.size());
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			forward = true;
			return get(index++);
		}

		@Override
		public boolean hasPrevious() {
			if (modificationCount != ArrayList.this.modificationCount) {
				throw new ConcurrentModificationException();
			}

			return (index > 0);
		}

		@Override
		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			forward = false;
			return get(--index);
		}

		@Override
		public void toStart() {
			index = 0;
		}

		@Override
		public void toEnd() {
			index = items.size();
		}

		@Override
		public void insert(T item) {
			indexBoundsCheck();

			ArrayList.this.insert(item, index);
			modificationCount++;
		}

		@Override
		public void update(T item) {
			indexBoundsCheck();

			ArrayList.this.update(forward ? index - 1 : index, item);
			modificationCount++;
		}

		@Override
		public void remove() {
			indexBoundsCheck();

			if (forward) {
				index--;
			}

			ArrayList.this.remove(index, 1);
			modificationCount++;
		}

		private void indexBoundsCheck() {
			if (index < 0 || index > +ArrayList.this.getLength()) {
				throw new IllegalStateException("index  " + index
						+ " out of bounds");
			}
		}
	}

	private java.util.ArrayList<T> items;

	private transient Comparator<T> comparator = null;

	private transient int modificationCount = 0;
	private transient ListListenerList<T> listListeners = null;

	public ArrayList(java.util.ArrayList<T> items) {
		this.items = items;
	}

	public ArrayList() {
		items = new java.util.ArrayList<T>();
	}

	public ArrayList(Comparator<T> comparator) {
		this();
		this.comparator = comparator;
	}

	public ArrayList(int capacity) {
		ArrayList.verifyNonNegative("capacity", capacity);

		items = new java.util.ArrayList<T>(capacity);
	}

	public ArrayList(T... items) {
		this(items, 0, items.length);
	}

	public ArrayList(T[] items, int index, int count) {
		verifyNotNull("items", items);
		verifyIndexBounds(index, count, 0, items.length);

		this.items = new java.util.ArrayList<T>(count);

		for (int i = 0; i < count; i++) {
			this.items.add(i, items[index + i]);
		}
	}

	public ArrayList(Sequence<T> items) {
		this(items, 0, items.getLength());
	}

	public ArrayList(Sequence<T> items, int index, int count) {
		verifyNotNull("items", items);
		verifyIndexBounds(index, count, 0, items.getLength());

		this.items = new java.util.ArrayList<T>(count);

		for (int i = 0; i < count; i++) {
			this.items.add(i, items.get(index + i));
		}
	}

	public ArrayList(ArrayList<T> arrayList) {
		this();

		verifyNotNull("arrayList", arrayList);

		items.addAll(arrayList.items);
	}

	public ArrayList(ArrayList<T> arrayList, int index, int count) {
		this();

		verifyNotNull("arrayList", arrayList);
		verifyIndexBounds(index, count, 0, arrayList.getLength());

		items.addAll(arrayList.items.subList(index, index + count));

		comparator = arrayList.comparator;
	}

	@Override
	public int add(T item) {
		int index = -1;

		if (comparator == null) {
			index = items.size();
			insert(item, index);
		} else {
			// Perform a binary search to find the insertion point
			index = binarySearch(this, item, comparator);
			if (index < 0) {
				index = -(index + 1);
			}

			insert(item, index, false);
		}

		return index;
	}

	@Override
	public void insert(T item, int index) {
		insert(item, index, true);
	}

	private void insert(T item, int index, boolean validate) {
		verifyIndexBounds(index, 0, items.size());

		if (comparator != null && validate) {
			int i = binarySearch(this, item, comparator);
			if (i < 0) {
				i = -(i + 1);
			}

			if (index != i) {
				throw new IllegalArgumentException("Illegal insertion point.");
			}
		}

		// Insert item
		items.add(index, item);

		modificationCount++;

		if (listListeners != null) {
			listListeners.itemInserted(this, index);
		}
	}

	@Override
	public T update(int index, T item) {
		verifyIndexBounds(index, 0, items.size() - 1);

		T previousItem = items.get(index);

		if (previousItem != item) {
			if (comparator != null) {
				// Ensure that the new item is greater or equal to its
				// predecessor and less than or equal to its successor
				T predecessorItem = (index > 0 ? items.get(index - 1) : null);
				T successorItem = (index < items.size() - 1 ? items
						.get(index + 1) : null);

				if ((predecessorItem != null && comparator.compare(item,
						predecessorItem) == -1)
						|| (successorItem != null && comparator.compare(item,
								successorItem) == 1)) {
					throw new IllegalArgumentException(
							"Illegal item modification.");
				}
			}

			items.set(index, item);

			modificationCount++;
		}

		if (listListeners != null) {
			listListeners.itemUpdated(this, index, previousItem);
		}

		return previousItem;
	}

	@Override
	public int remove(T item) {
		int index = indexOf(item);

		if (index >= 0) {
			remove(index, 1);
		}

		return index;
	}

	@Override
	public Sequence<T> remove(int index, int count) {
		verifyIndexBounds(index, count, 0, items.size());

		java.util.ArrayList<T> removedItems = new java.util.ArrayList<T>(
				items.subList(index, index + count));
		ArrayList<T> removed = new ArrayList<T>(removedItems);

		// Remove items
		if (count > 0) {

			modificationCount++;

			if (listListeners != null) {
				listListeners.itemsRemoved(this, index, removed);
			}
		}

		return removed;
	}

	@Override
	public void clear() {
		if (items.size() > 0) {
			items.clear();

			modificationCount++;

			if (listListeners != null) {
				listListeners.listCleared(this);
			}
		}
	}

	@Override
	public T get(int index) {
		verifyIndexBounds(index, 0, items.size() - 1);

		return items.get(index);
	}

	@Override
	public int indexOf(T item) {
		int index = -1;

		if (comparator == null) {
			index = items.indexOf(item);
		} else {
			// Perform a binary search to find the index
			index = binarySearch(this, item, comparator);
			if (index < 0) {
				index = -1;
			}
		}

		return index;
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public int getLength() {
		return items.size();
	}

	public void trimToSize() {
		// No-op
	}

	public void ensureCapacity(int capacity) {
		// No-op
	}

	public int getCapacity() {
		return items.size();
	}

	public Object[] toArray() {
		return items.toArray();
	}

	public T[] toArray(Class<? extends T[]> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comparator<T> getComparator() {
		return comparator;
	}

	@Override
	public void setComparator(Comparator<T> comparator) {
		Comparator<T> previousComparator = this.comparator;

		if (comparator != null) {
			sort(this, comparator);
		}

		this.comparator = comparator;

		if (listListeners != null) {
			listListeners.comparatorChanged(this, previousComparator);
		}
	}

	@Override
	public ItemIterator<T> iterator() {
		return new ArrayListItemIterator();
	}

	@Override
	public ListenerList<ListListener<T>> getListListeners() {
		if (listListeners == null) {
			listListeners = new ListListenerList<T>();
		}

		return listListeners;
	}

	@Override
	public boolean equals(Object o) {
		return items.equals(o);
	}

	@Override
	public int hashCode() {
		return items.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getName());
		sb.append(" [");

		int i = 0;
		for (T item : this) {
			if (i > 0) {
				sb.append(", ");
			}

			sb.append(item);
			i++;
		}

		sb.append("]");

		return sb.toString();
	}

	public static <T> void sort(ArrayList<T> arrayList, Comparator<T> comparator) {
		sort(arrayList, 0, arrayList.getLength(), comparator);
	}

	@SuppressWarnings("unchecked")
	public static <T> void sort(ArrayList<T> arrayList, int from, int to,
			Comparator<T> comparator) {
		verifyNotNull("arrayList", arrayList);
		verifyNotNull("comparator", comparator);

		Arrays.sort((T[]) arrayList.items.toArray(), from, to, comparator);

		arrayList.modificationCount++;
	}

	public static <T extends Comparable<? super T>> void sort(
			ArrayList<T> arrayList) {
		sort(arrayList, new Comparator<T>() {
			@Override
			public int compare(T t1, T t2) {
				return t1.compareTo(t2);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <T> int binarySearch(ArrayList<T> arrayList, T item,
			Comparator<T> comparator) {
		verifyNotNull("arrayList", arrayList);
		verifyNotNull("comparator", comparator);
		verifyNotNull("item", item);

		int index = Arrays.binarySearch((T[]) arrayList.items.toArray(), item,
				comparator);

		return index;
	}

	public static <T extends Comparable<? super T>> int binarySearch(
			ArrayList<T> arrayList, T item) {
		return binarySearch(arrayList, item, new Comparator<T>() {
			@Override
			public int compare(T t1, T t2) {
				return t1.compareTo(t2);
			}
		});
	}

	private static void verifyNotNull(String argument, Object value) {
		if (value == null) {
			throw new IllegalArgumentException(argument + " cannot be null.");
		}
	}

	private static void verifyNonNegative(String argument, int value) {
		if (value < 0) {
			throw new IllegalArgumentException(argument
					+ " cannot be negative.");
		}
	}

	private static void verifyIndexBounds(int index, int start, int end) {
		if (index < start || index > end) {
			throw new IndexOutOfBoundsException("index " + index
					+ " out of bounds.");
		}
	}

	private static void verifyIndexBounds(int index, int count, int start,
			int end) {
		if (count < 0) {
			throw new IllegalArgumentException();
		}

		if (index < start) {
			throw new IndexOutOfBoundsException("index " + index
					+ " out of bounds.");
		}

		if (index + count > end) {
			throw new IndexOutOfBoundsException("index + count " + index + ","
					+ count + " out of range.");
		}
	}
}
