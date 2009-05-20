/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * The default implementation of {@link ListModel} based on a {@code
 * java.util.Vector}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @parem <T>
 */
public class DefaultListModel<E> extends Vector<E> implements ListModel<E> {
  private static final long serialVersionUID = -7770341141882890130L;

  /** List of {@link ListDataListener ListDataListeners}. */
  private List<ListDataListener> listenerList = new ArrayList<ListDataListener>();

  /**
   * Constructs an empty {@code DefaultListModel} instance.
   */
  public DefaultListModel() {
    super();
  }

  /**
   * Constructs a model containing the elements of the specified collection, in
   * the order they are returned by the collection's iterator.
   * 
   * @param c the collection whose elements are to be placed into this model
   * @throws {@code NullPointerException} if the specified collection is {@code
   *         null}
   */
  public DefaultListModel(Collection<? extends E> c) {
    super(c);
  }

  /**
   * Appends the specified element to the end of this model.
   * 
   * @param e element to be appended to this model
   * @return {@code true} (as specified by {@link Collection#add})
   */
  @Override
  public boolean add(E e) {
    if (!super.add(e)) {
      return false;
    }
    int index = super.size() - 1;
    fireIntervalAdded(this, index, index);
    return true;
  }

  /**
   * Inserts the specified element at the specified position.
   * 
   * @param index index at which the specified element is to be inserted
   * @param element element to be inserted
   * @throws ArrayIndexOutOfBoundsException if the index is out of range (
   *           {@code index < 0 || index > size()})
   */
  @Override
  public void add(int index, E element) {
    super.add(index, element);
    fireIntervalAdded(this, index, index);
  }

  /**
   * Appends all of the elements in the specified Collection to the end of this
   * model, in the order that they are returned by the specified Collection's
   * Iterator.
   * 
   * @param c elements to be inserted
   * @return {@code true} if this model changed as a result of the call
   * @throws NullPointerException if the specified collection is null
   */
  public boolean addAll(Collection<? extends E> c) {
    return addAll(super.size(), c);
  }

  /**
   * Inserts all of the elements in the specified Collection into this model at
   * the specified position.
   * 
   * @param index index at which to insert the first element from the specified
   *          collection
   * @param c elements to be inserted
   * @return {@code true} if this model changed as a result of the call
   * @throws ArrayIndexOutOfBoundsException if the index is out of range (
   *           {@code index < 0 || index > size()})
   * @throws NullPointerException if the specified collection is null
   */
  public boolean addAll(int index, Collection<? extends E> c) {
    if (!super.addAll(index, c)) {
      return false;
    }
    fireContentsChanged(this, index, index + c.size() - 1);
    return true;
  }

  /**
   * Adds the specified element to the end of this model.
   * 
   * @param obj the component to be added
   * @deprecated Replaced by {@link #add(Object)}
   */
  @Override
  public void addElement(E obj) {
    add(obj);
  }

  /**
   * Add a listener object to this model that's notified each time a change to
   * the data model occurs.
   * 
   * @param listener the listener to add
   */
  public void addListDataListener(ListDataListener listener) {
    listenerList.add(listener);
  }

  /**
   * Removes all of the elements from this model.
   */
  @Override
  public void clear() {
    this.removeAllElements();
  }

  /**
   * {@code AbstractListModel} subclasses must call this method <b>after</b> one
   * or more elements of the list change.
   * 
   * @param source the {@link ListModel} that changed, typically {@code this}
   * @param index0 one end of the new interval
   * @param index1 the other end of the new interval
   */
  protected void fireContentsChanged(Object source, int index0, int index1) {
    ListDataEvent event = new ListDataEvent(source,
        ListDataEvent.Type.CONTENTS_CHANGED, index0, index1);
    for (ListDataListener listener : listenerList) {
      listener.contentsChanged(event);
    }
  }

  /**
   * 
   * @param source
   * @param index0
   * @param index1
   */
  protected void fireIntervalAdded(Object source, int index0, int index1) {
    ListDataEvent event = new ListDataEvent(source,
        ListDataEvent.Type.INTERVAL_ADDED, index0, index1);
    for (ListDataListener listener : listenerList) {
      listener.intervalAdded(event);
    }
  }

  /**
   * 
   * @param source
   * @param index0
   * @param index1
   */
  protected void fireIntervalRemoved(Object source, int index0, int index1) {
    ListDataEvent event = new ListDataEvent(source,
        ListDataEvent.Type.INTERVAL_REMOVED, index0, index1);
    for (ListDataListener listener : listenerList) {
      listener.intervalRemoved(event);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.list.ListModel#getElementAt(int)
   */
  public E getElementAt(int index) {
    return get(index);
  }

  /**
   * Returns an array of all the list data listeners registered on this {@code
   * AbstractListModel}.
   * 
   * @return all of this model's {@link ListDataListener ListDataListeners}, or
   *         an empty array if no list data listeners are currently registered
   */
  public ListDataListener[] getListeners() {
    return listenerList.toArray(new ListDataListener[listenerList.size()]);
  }

  /**
   * Gets the size of the list.
   * 
   * @return the number of elements currently in the list
   * @see org.gwt.mosaic.ui.client.list.ListModel#getSize()
   */
  public int getSize() {
    return super.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#remove(int)
   */
  @Override
  public E remove(int index) {
    final E element = super.remove(index);
    fireIntervalRemoved(this, index, index);
    return element;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    int index1 = super.size() - 1;
    boolean result = super.removeAll(c);
    if (result) {
      fireIntervalRemoved(this, 0, index1);
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeAllElements()
   */
  @Override
  public void removeAllElements() {
    int index1 = super.size() - 1;
    if (index1 >= 0) {
      super.removeAllElements();
      fireIntervalRemoved(this, 0, index1);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeElement(java.lang.Object)
   */
  @Override
  public boolean removeElement(Object o) {
    return remove(o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeElementAt(int)
   */
  @Override
  public void removeElementAt(int index) {
    remove(index);
  }

  /**
   * Removes a listener object from the list.
   * 
   * @param listener the listener to remove
   * @see org.gwt.mosaic.ui.client.list.ListModel#removeListDataListener(org.gwt.mosaic.ui.client.list.ListDataListener)
   */
  public void removeListDataListener(ListDataListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Replaces the element at the specified position in this Vector with the
   * specified element.
   * 
   * @param index
   * @param element
   * @return the replaced element
   */
  @Override
  public E set(int index, E element) {
    final E result = super.set(index, element);
    fireContentsChanged(this, index, index);
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#setElementAt(java.lang.Object, int)
   */
  @Override
  public void setElementAt(E o, int index) {
    set(index, o);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#setSize(int)
   */
  @Override
  public void setSize(int newSize) {
    super.setSize(newSize);
  }

  /**
   * Returns the number of elements in this model.
   * 
   * @return the number of elements in this model
   * @deprecated Replaced by {@link #getSize()}
   */
  public int size() {
    return super.size();
  }

}
