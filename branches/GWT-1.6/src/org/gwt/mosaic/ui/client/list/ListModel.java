/*
 * Copyright 2008 Google Inc.
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

/**
 * The data model that is typically used in {@link ListBox}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface ListModel<T> {

  /**
   * Return the length of the list.
   * 
   * @return the length of the list.
   */
  int getSize();

  /**
   * Retrieves a data element at the specified index.
   * 
   * @param index the index of the element to retrieve
   * @return the data element at the specified index
   */
  T getElementAt(int index);

  /**
   * Adds a listener to the list that's notified each time a change to the data
   * model occurs.
   * 
   * @param listener the {@code ListDataListener} to be added
   */
  void addListDataListener(ListDataListener listener);

  /**
   * Removes a listener from the list that's notified each time a change to the
   * data model occurs.
   * 
   * @param l the {@code ListDataListener} to be removed
   */
  void removeListDataListener(ListDataListener l);
}
