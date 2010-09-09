package org.gwt.mosaic2g.client.util;

import java.util.NoSuchElementException;

/**
 * This class maintains a queue that avoids generating heap traffic.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Queue {

	private Object[] buffer;
	private int addPos = 0;
	private int removePos = 0;
	private Queue overflow = null;

	/**
	 * Create a queue. It will fill up to the given capacity without creating
	 * any new objects. If the queue gets more entries than this, it will start
	 * allocating objects.
	 */
	public Queue(int capacity) {
		buffer = new Object[capacity];
	}

	public synchronized void add(Object el) {
		int n = (addPos + 1) % buffer.length;
		if (n != removePos) {
			buffer[addPos] = el;
			addPos = n;
		} else { // overflow
			if (overflow == null) {
				overflow = new Queue(buffer.length);
			}
			overflow.add(el);
		}
	}

	public synchronized boolean isEmpty() {
		return addPos == removePos;
	}

	/**
	 * @throws NoSuchElementException
	 *             if isEmpty() is true
	 */
	public synchronized Object remove() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Object result = buffer[removePos];
		buffer[removePos] = null;
		removePos = (removePos + 1) % buffer.length;
		if (overflow != null) {
			buffer[addPos] = overflow.remove();
			addPos = (addPos + 1) % buffer.length;
			if (overflow.isEmpty()) {
				overflow = null;
			}
		}
		return result;
	}

}
