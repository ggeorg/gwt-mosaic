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
 * The default implementation of {@link ComboBoxModel} based on a {@code
 * java.util.Vector}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @parem <T>
 */
public class DefaultComboBoxModel<E> extends Vector<E> implements
    ComboBoxModel<E> {
  private static final long serialVersionUID = -9092492462680778920L;

  /** List of {@link ListDataListener ListDataListeners}. */
  private List<ListDataListener> listenerList = new ArrayList<ListDataListener>();

  private E selectedObject;

  /**
   * Constructs an empty {@code DefaultComboBoxModel} instance.
   */
  public DefaultComboBoxModel() {
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
  public DefaultComboBoxModel(Collection<? extends E> c) {
    super(c);
  }

  public void addListDataListener(ListDataListener listener) {
    listenerList.add(listener);
  }

  protected void fireContentsChanged(Object source, int index0, int index1) {
    ListDataEvent event = new ListDataEvent(source,
        ListDataEvent.Type.CONTENTS_CHANGED, index0, index1);
    for (ListDataListener listener : listenerList) {
      listener.contentsChanged(event);
    }
  }

  protected void fireIntervalAdded(Object source, int index0, int index1) {
    ListDataEvent event = new ListDataEvent(source,
        ListDataEvent.Type.INTERVAL_ADDED, index0, index1);
    for (ListDataListener listener : listenerList) {
      listener.intervalAdded(event);
    }
  }

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

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.list.ComboBoxModel#getSelectedItem()
   */
  public E getSelectedItem() {
    return selectedObject;
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

  public void removeListDataListener(ListDataListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Sets the value of the selected item (the selected item may be {@code null}.
   * 
   * @param item the combo box value or {@code null} for no selection
   */
  public void setSelectedItem(E item) {
    if ((selectedObject != null && !selectedObject.equals(item))
        || selectedObject == null && item != null) {
      selectedObject = item;
      fireContentsChanged(this, -1, -1);
    }
  }

  @Override
  public void add(int index, E element) {
    super.add(index, element);
    fireIntervalAdded(this, index, index);
  }

  @Override
  public boolean add(E e) {
    boolean result = super.add(e);
    if (result) {
      int index = super.size() - 1;
      fireIntervalAdded(this, index, index);
      return true;
    }
    return false;
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
    if (super.addAll(index, c)) {
      fireContentsChanged(this, index, index + c.size() - 1);
      return true;
    }
    return false;
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
   * Removes all of the elements from this model.
   */
  @Override
  public void clear() {
    int index1 = super.size() - 1;
    if (index1 >= 0) {
      super.clear();
      fireIntervalRemoved(this, 0, index1);
    }
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
    E result = super.set(index, element);
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
   * @see java.util.Vector#remove(int)
   */
  @Override
  public E remove(int index) {
    E element = super.remove(index);
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
    super.removeAllElements();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Vector#removeElement(java.lang.Object)
   */
  @Override
  public boolean removeElement(Object o) {
    return super.removeElement(o);
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

}
