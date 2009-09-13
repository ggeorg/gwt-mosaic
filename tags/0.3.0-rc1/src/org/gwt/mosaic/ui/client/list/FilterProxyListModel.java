/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.Vector;

import org.gwt.mosaic.ui.client.list.AbstractListModel;
import org.gwt.mosaic.ui.client.list.ListDataEvent;
import org.gwt.mosaic.ui.client.list.ListDataListener;
import org.gwt.mosaic.ui.client.list.ListModel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 *
 * @param <T1>
 * @param <T2>
 */
public class FilterProxyListModel<T1, T2> extends AbstractListModel<T1>
    implements ListDataListener {

  private ListModel<T1> underlying;
  private Vector<Integer> filter;

  private T2 regexp;

  private Filter<T1, T2> modelFilter;

  public FilterProxyListModel(ListModel<T1> underlying) {
    this.underlying = underlying;
    this.underlying.addListDataListener(this);
  }

  public void contentsChanged(ListDataEvent event) {
    filter(regexp);
  }

  public void filter(T2 str) {
    if (modelFilter == null) {
      return;
    }

    if (regexp == str) {
      return;
    } else if (regexp != null && regexp.equals(str)) {
      return;
    }

    int index = (filter == null) ? (underlying.getSize() - 1)
        : (filter.size() - 1);
    if (index >= 0) {
      fireIntervalRemoved(this, 0, index);
    }

    if (str == null) {
      regexp = null;
      filter = null;
    } else {
      regexp = str;
      filter = new Vector<Integer>();
      for (int iter = 0; iter < underlying.getSize(); iter++) {
        if (modelFilter.select(underlying.getElementAt(iter), regexp)) {
          filter.addElement(new Integer(iter));
        }
      }
    }

    index = (filter == null) ? (underlying.getSize() - 1) : (filter.size() - 1);
    if (index >= 0) {
      fireIntervalAdded(this, 0, index);
    }
  }

  public T1 getElementAt(int index) {
    return underlying.getElementAt(getFilterOffset(index));
  }

  private int getFilterOffset(int index) {
    if (filter == null) {
      return index;
    }
    if (filter.size() > index) {
      return filter.elementAt(index);
    }
    return filter.indexOf(index);
  }

  public int getSize() {
    if (filter == null) {
      return underlying.getSize();
    }
    return filter.size();
  }

  private int getUnderlyingOffset(int index) {
    if (filter == null) {
      return index;
    }
    return filter.indexOf(index);
  }

  public void intervalAdded(ListDataEvent event) {
    filter(regexp); // TODO fine tune
  }

  public void intervalRemoved(ListDataEvent event) {
    filter(regexp);// TODO fine tune
  }

  public Filter<T1, T2> getModelFilter() {
    return modelFilter;
  }

  public void setModelFilter(Filter<T1, T2> modelFilter) {
    assert (modelFilter != null);
    this.modelFilter = modelFilter;
  }

}
