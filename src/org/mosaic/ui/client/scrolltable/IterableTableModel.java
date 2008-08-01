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
package org.mosaic.ui.client.scrolltable;

import java.util.Iterator;

/**
 * A class to retrieve row data to be used in a table.
 * 
 * @param <R> the data type of the row values
 */
public class IterableTableModel<R> extends ReadOnlyTableModel<R> {
  Iterable<? extends Iterable> rows;

  public IterableTableModel(Iterable<? extends Iterable> rows) {
    this.rows = rows;
  }

  class RowIterator implements Iterator<Iterator<Object>> {
    Iterator<? extends Iterable> rowIterator;

    public RowIterator(Iterator<? extends Iterable> iterator) {
      rowIterator = iterator;
    }

    public boolean hasNext() {
      return rowIterator.hasNext();
    }

    public Iterator<Object> next() {
      return rowIterator.next().iterator();
    }

    public void remove() {
      rowIterator.remove();
    }
  }

  @Override
  public void requestRows(TableModel.Request request, TableModel.Callback<R> callback) {

    callback.onRowsReady(request, new TableModel.Response<R>() {

      @Override
      public Iterator<Iterator<Object>> getIterator() {
        return new RowIterator(rows.iterator());
      }
    });
  }
}
