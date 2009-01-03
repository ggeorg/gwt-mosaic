/*
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract definition of {@link ListModel} that provides a {@code ListBox} with
 * its contents.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class AbstractListModel<T> implements ListModel<T>,
    Serializable {
  private static final long serialVersionUID = 4151800085271288709L;

  /** List of {@link ListDataListener ListDataListeners}. */
  protected transient List<ListDataListener> listenerList = new ArrayList<ListDataListener>();

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
   * Removes a listener object from the list.
   * 
   * @param listener the listener to remove
   */
  public void removeListDataListener(ListDataListener listener) {
    listenerList.remove(listener);
  }
}
