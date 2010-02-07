/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.ui.client.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.gwt.mosaic.ui.client.event.RowInsertionEvent;
import org.gwt.mosaic.ui.client.event.RowInsertionHandler;
import org.gwt.mosaic.ui.client.event.RowRemovalEvent;
import org.gwt.mosaic.ui.client.event.RowRemovalHandler;
import org.gwt.mosaic.ui.client.event.RowValueChangeEvent;
import org.gwt.mosaic.ui.client.event.RowValueChangeHandler;
import org.gwt.mosaic.ui.client.table.TableModelHelper.ColumnSortList;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Response;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public class DefaultTableModel<RowType> extends
    AbstractMutableTableModel<RowType> {

  public static interface ColumnComparator<T> {
    int compare(T t1, T t2, int column);
  }

  public static interface Provider<RowType> {
    void requestRows(Request request, TableModel.Callback<RowType> callback);
  }

  public static interface Resolver<RowType> extends RowInsertionHandler,
      RowRemovalHandler, RowValueChangeHandler<RowType> {
  }

  /**
   * An iterator over the visible rows in an iterator over many rows.
   */
  private class VisibleRowsIterator implements Iterator<RowType> {
    /**
     * The iterator of row data.
     */
    private Iterator<RowType> rows;

    /**
     * The current row of the rows iterator.
     */
    private int curRow;

    /**
     * The last visible row in the grid.
     */
    private int lastVisibleRow;

    /**
     * Constructor.
     */
    public VisibleRowsIterator(Iterator<RowType> rows, int startRow, int numRows) {
      this.curRow = 0;
      this.lastVisibleRow = startRow + numRows;

      // Iterate up to the first row
      while (curRow < startRow && rows.hasNext()) {
        rows.next();
        curRow++;
      }
      this.rows = rows;
    }

    public boolean hasNext() {
      return (curRow < lastVisibleRow && rows.hasNext());
    }

    public RowType next() {
      // Check that the next row exists
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      ++curRow;
      return rows.next();
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove not supported");
    }
  }

  private final Provider<RowType> provider;
  private final Resolver<RowType> resolver;

  /**
   * The column comparator used by the column sorter.
   */
  private ColumnComparator<RowType> columnComparator;

  private List<RowType> data;

  private boolean readOnly = false;

  public DefaultTableModel() {
    this(new ArrayList<RowType>());
  }

  public DefaultTableModel(Collection<RowType> collection) {
    super();

    data = new ArrayList<RowType>(collection);

    provider = new Provider<RowType>() {
      public void requestRows(final Request request,
          TableModel.Callback<RowType> callback) {
        // Get the primary column and sort order
        final ColumnSortList sortList = request.getColumnSortList();
        final int column = sortList.getPrimaryColumn();
        final boolean ascending = sortList.isPrimaryAscending();

        // Sort the row elements
        if (DefaultTableModel.this.columnComparator != null) {
          Collections.sort(data, new Comparator<RowType>() {
            public int compare(RowType o1, RowType o2) {
              if (ascending) {
                return DefaultTableModel.this.columnComparator.compare(o1, o2,
                    column);
              } else {
                return DefaultTableModel.this.columnComparator.compare(o2, o1,
                    column);
              }
            }
          });
        }

        // numRows = -1 means all rows (see PagingScrollTable API)
        final int numRows = Math.min(request.getNumRows() < 0 ? data.size()
            : request.getNumRows(), data.size() - request.getStartRow());
        callback.onRowsReady(request, new Response<RowType>() {
          @Override
          public Iterator<RowType> getRowValues() {
            return new VisibleRowsIterator(data.iterator(),
                request.getStartRow(), numRows);
          }
        });
      }
    };

    resolver = new Resolver<RowType>() {
      public void onRowInsertion(RowInsertionEvent event) {
        data.add(event.getRowIndex(), null);
      }

      public void onRowRemoval(RowRemovalEvent event) {
        data.remove(event.getRowIndex());
      }

      public void onRowValueChange(RowValueChangeEvent<RowType> event) {
        data.set(event.getRowIndex(), event.getRowValue());
      }
    };

    bind();
    
    setRowCount(data.size());
  }

  public DefaultTableModel(Provider<RowType> provider) {
    this(provider, null);
  }

  public DefaultTableModel(Provider<RowType> provider,
      Resolver<RowType> resolver) {
    super();

    this.provider = provider;
    this.resolver = resolver;

    bind();
  }

  /**
   * @return the columnComparator
   */
  public ColumnComparator<RowType> getColumnComparator() {
    return columnComparator;
  }

  /**
   * @return the provider
   */
  public Provider<RowType> getProvider() {
    return provider;
  }

  /**
   * @return the resolver
   */
  public Resolver<RowType> getResolver() {
    return resolver;
  }

  /**
   * @return the readOnly
   */
  public boolean isReadOnly() {
    return readOnly || resolver == null;
  }

  public boolean onRowInserted(int beforeRow) {
    return !isReadOnly();
  }

  public boolean onRowRemoved(int row) {
    return !isReadOnly();
  }

  public boolean onSetRowValue(int row, RowType rowValue) {
    return !isReadOnly();
  }

  @Override
  public void requestRows(Request request, TableModel.Callback<RowType> callback) {
    getProvider().requestRows(request, callback);
  }

  /**
   * @param columnComparator the columnComparator to set
   */
  public void setColumnComparator(ColumnComparator<RowType> columnComparator) {
    this.columnComparator = columnComparator;
  }

  /**
   * @param readOnly the readOnly to set
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  private void bind() {
    if (resolver != null) {
      addRowInsertionHandler(resolver);
      addRowRemovalHandler(resolver);
      addRowValueChangeHandler(resolver);
    }
  }
}
