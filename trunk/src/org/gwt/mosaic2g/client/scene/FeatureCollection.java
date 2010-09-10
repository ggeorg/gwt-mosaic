/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic2g.client.scene;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author ggeorg
 */
public class FeatureCollection implements Iterable<Feature> {

	private class FeatureIterator implements Iterator<Feature> {

		private int index = -1;

		public boolean hasNext() {
			return index < (size - 1);
		}

		public Feature next() {
			if (index >= size) {
				throw new NoSuchElementException();
			}
			return array[++index];
		}

		public void remove() {
			if ((index < 0) || (index >= size)) {
				throw new IllegalStateException();
			}
			parent.remove(array[index--]);
		}
	}

	private static final int INITIAL_SIZE = 4;

	private Feature[] array;
	private int size;
	private HasFeatures parent;

	public FeatureCollection(HasFeatures parent) {
		this(parent, INITIAL_SIZE);
	}

	public FeatureCollection(HasFeatures parent, int size) {
		assert size > 0;
		this.parent = parent;
		array = new Feature[INITIAL_SIZE];
	}

	public void add(Feature f) {
		insert(f, size);
	}

	public boolean contains(Feature f) {
		return (indexOf(f) != -1);
	}

	public Feature get(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}

	public int indexOf(Feature f) {
		for (int i = 0; i < size; i++) {
			if (array[i] == f) {
				return i;
			}
		}
		return -1;
	}

	public void insert(Feature f, int beforeIndex) {
		if ((beforeIndex < 0) || (beforeIndex > size)) {
			throw new IndexOutOfBoundsException();
		}

		// Realloc array if necessary (doubling).
		if (size == array.length) {
			final int len = array.length;
			Feature[] newArray = new Feature[len + len];
			System.arraycopy(array, 0, newArray, 0, len);
			array = newArray;
		}

		++size;

		// Move all features after 'beforeIndex' back a slot.
		for (int i = size - 1; i > beforeIndex; --i) {
			array[i] = array[i - 1];
		}

		array[beforeIndex] = f;
	}

	public Iterator<Feature> iterator() {
		return new FeatureIterator();
	}

	public void remove(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IndexOutOfBoundsException();
		}

		--size;
		for (int i = index; i < size; ++i) {
			array[i] = array[i + 1];
		}
		
		array[size] = null;
	}
	
	public void remove(Feature f) {
		int index = indexOf(f);
		if (index == -1) {
			throw new NoSuchElementException();
		}
		remove(index);
	}
	
	public int size() {
		return size;
	}

}
