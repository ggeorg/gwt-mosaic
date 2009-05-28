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

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.gwt.beansbinding.observablecollections.client.ObservableCollections;
import org.gwt.beansbinding.observablecollections.client.ObservableList;
import org.gwt.beansbinding.observablecollections.client.ObservableListListener;
import org.gwt.mosaic.ui.client.list.AbstractListModel;

public class DefaultListModel<T> extends AbstractListModel<T> implements
    ObservableListListener {

  private final ObservableList<T> list;

  public DefaultListModel() {
    super();
    list = ObservableCollections.observableList(new Vector<T>());
    list.addObservableListListener(this);
  }

  public DefaultListModel(Collection<? extends T> c) {
    super();
    list = ObservableCollections.observableList(new Vector<T>(c));
    list.addObservableListListener(this);
  }

  public boolean add(T element) {
    return list.add(element);
  }

  public void add(int index, T element) {
    list.add(index, element);
  }

  public boolean addAll(Collection<? extends T> c) {
    return list.addAll(c);
  }

  public boolean addAll(int index, Collection<? extends T> c) {
    return list.addAll(index, c);
  }

  public void clear() {
    list.clear();
  }

  public T remove(int index) {
    return list.remove(index);
  }

  public boolean removeAll(Collection<?> c) {
    return list.retainAll(c);
  }

  public boolean remove(Object o) {
    return list.remove(o);
  }

  public T set(int index, T element) {
    return list.set(index, element);
  }

  public T getElementAt(int index) {
    return list.get(index);
  }

  public int getSize() {
    return list.size();
  }

  public void listElementPropertyChanged(ObservableList list, int index) {
    fireContentsChanged(this, index, index);
  }

  public void listElementReplaced(ObservableList list, int index,
      Object oldElement) {
    fireContentsChanged(this, index, index);
  }

  public void listElementsAdded(ObservableList list, int index, int length) {
    fireIntervalAdded(this, index, index + length - 1);
  }

  public void listElementsRemoved(ObservableList list, int index,
      List oldElements) {
    fireIntervalRemoved(this, index, index + oldElements.size() - 1);
  }

}
