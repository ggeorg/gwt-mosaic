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
import java.util.NoSuchElementException;

/**
 * A {@link TableModel} used when the data source can be accessed synchronously.
 * 
 * @param <R> the data type of the row values
 * 
 */
public abstract class ClientTableModel<R> extends TableModel<R> {
  /**
   * A Response that can be used when the response is not going over a RPC
   * request.
   */
  public static class ClientResponse<R> extends Response<R> {
    /**
     * An iterator over the rows of data.
     */
    private Iterator<Iterator<Object>> rows = null;

    /**
     * Constructor.
     */
    public ClientResponse() {
    }

    /**
     * Constructor. Create a response with the data to enter into each cell of
     * each row.
     * 
     * @param rows the data for the table
     */
    public ClientResponse(Iterator<Iterator<Object>> rows) {
      this.rows = rows;
    }

    /**
     * Get the rows data.
     * 
     * @return the rows data
     */
    @Override
    public Iterator<Iterator<Object>> getIterator() {
      return rows;
    }
  }

  private class ColumnIterator extends StubIterator<Object> {
    private int row = 0;

    @Override
    public Object computeNext() {
      return getCell(row, index++);
    }
  }

  private class RowIterator extends StubIterator<Iterator<Object>> {
    int max;

    public RowIterator(Request request) {
      index = request.getStartRow();
      if (request.getNumRows() == -1) {
        max = Integer.MAX_VALUE;
      } else {
        max = request.getNumRows() + index;
      }
    }

    @Override
    protected Iterator<Object> computeNext() {
      // Reset column iterator rather than creating new one.
      columnIter.index = 0;
      columnIter.row = index++;
      columnIter.done = false;
      columnIter.next = null;
      // Now check for next.
      if (columnIter.hasNext() && columnIter.row < max) {
        return columnIter;
      } else {
        return null;
      }
    }
  }

  /**
   * Convenience class to support custom iterators.
   */
  private abstract class StubIterator<E> implements Iterator<E> {
    int index;
    E next;
    boolean done = false;

    protected abstract E computeNext();

    public boolean hasNext() {
      if (done) {
        return false;
      }
      if (next == null) {
        next = computeNext();
        if (next == null) {
          done = true;
          return false;
        }
      }
      return true;
    }

    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      } else {
        E accum = next;
        next = null;
        return accum;
      }
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private ColumnIterator columnIter = new ColumnIterator();

  /**
   * Get the value for a given cell. Return null if no more values are
   * available.
   * 
   * @param rowNum the row index
   * @param colNum the column index
   * @return the value at the given row and column
   */
  public abstract Object getCell(int rowNum, int colNum);

  /**
   * See {@link TableModel}.
   */
  @Override
  public void requestRows(Request request, Callback<R> callback) {
    RowIterator rowIter = new RowIterator(request);
    callback.onRowsReady(request, new ClientResponse<R>(rowIter));
  }
}
