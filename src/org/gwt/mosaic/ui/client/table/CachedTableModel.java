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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gwt.mosaic.ui.client.table.TableModelHelper.ColumnSortList;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Response;

/**
 * A {@link MutableTableModel} that wraps another {@link MutableTableModel} and
 * adds its own cache so subsequent requests for the same data will not require
 * another {@link MutableTableModel} request.
 * 
 * <h1>Cache</h1>
 * 
 * The CachedTableModel supports caching rows of data before they are needed,
 * allowing listeners to request data much more quickly. When another class
 * requests data, the CachedTableModel will feed data from its cache if
 * available, and then it will request unavailable data from your specific
 * implementation.
 * <p>
 * The pre cache and post cache refers to the number of rows to request in
 * addition to the actual request. Pre cache refers to rows before the request,
 * and post cache refers to rows after the request. For example, in applications
 * where you expect the user to move forward quickly, you would want a large
 * post cache.
 * <p>
 * The size of your cache depends on the implementation and usage of your view
 * component. If you are using a view that supports paging, such as the
 * {@link PagingScrollTable}, you should set your cache to a multiple of the
 * page size so the user can go to the next and previous pages quickly.
 * 
 * <h1>Limitations</h1>
 * 
 * The cache is cleared every time the sort order changes. However, if you
 * disallow column sorting or expect that the user will not sort the columns
 * repeatedly, the cache will still improve paging performance.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public class CachedTableModel<RowType> extends
    AbstractMutableTableModel<RowType> {
  /**
   * An intermediate callback that adds the response from the inner table model
   * into the cache, then responds to the original request.
   */
  private class CacheCallback implements Callback<RowType> {
    /**
     * The actual callback from the original requester.
     */
    private Callback<RowType> actualCallback;

    /**
     * The actual number of requested rows.
     */
    private int actualNumRows;

    /**
     * The original request.
     */
    private Request actualRequest;

    /**
     * The actual first requested row.
     */
    private int actualStartRow;

    /**
     * Construct a new {@link CacheCallback}.
     * 
     * @param request the original request
     * @param callback The actual callback from the requestor
     * @param startRow The actual first requested row
     * @param numRows The actual number of requested rows
     */
    public CacheCallback(Request request, Callback<RowType> callback,
        int startRow, int numRows) {
      actualRequest = request;
      actualCallback = callback;
      actualStartRow = startRow;
      actualNumRows = numRows;
    }

    public void onFailure(Throwable caught) {
      actualCallback.onFailure(caught);
    }

    public void onRowsReady(Request request, Response<RowType> response) {
      // Save the response data into the cache
      if (response != null) {
        Iterator<RowType> rowValues = response.getRowValues();
        if (rowValues != null) {
          int curRow = request.getStartRow();
          while (rowValues.hasNext()) {
            rowValuesMap.put(Integer.valueOf(curRow), rowValues.next());
            curRow++;
          }
        }
      }

      // Forward the data to the actual callback
      actualCallback.onRowsReady(actualRequest, new CacheResponse(
          actualStartRow, actualStartRow + actualNumRows - 1));
    }
  }

  /**
   * An {@link Iterator} that iterates over the cached rows of data.
   */
  private class CacheIterator implements Iterator<RowType> {
    /**
     * The current row that will be returned on the next call to next.
     */
    int curRow;

    /**
     * The last row to iterate, inclusively.
     */
    int lastRow;

    /**
     * Construct a new iterator.
     * 
     * @param firstRow the first row of data
     * @param lastRow the last row of data
     */
    public CacheIterator(int firstRow, int lastRow) {
      this.curRow = firstRow - 1;
      this.lastRow = lastRow;
    }

    public boolean hasNext() {
      return curRow < lastRow
          && rowValuesMap.containsKey(Integer.valueOf(curRow + 1));
    }

    public RowType next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      curRow++;
      return rowValuesMap.get(Integer.valueOf(curRow));
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove not supported.");
    }
  }

  /**
   * A Response that comes directly from the cache.
   */
  private class CacheResponse extends Response<RowType> {
    /**
     * An iterator over the row values.
     */
    private CacheIterator it;

    public CacheResponse(int firstRow, int lastRow) {
      it = new CacheIterator(firstRow, lastRow);
    }

    @Override
    public Iterator<RowType> getRowValues() {
      return it;
    }
  }

  /**
   * The sort list included with the last request.
   */
  private ColumnSortList lastSortList = null;

  /**
   * The number of rows to request that come after the actual requested rows.
   */
  private int postCacheRows = 0;

  /**
   * The number of rows to request that come before the actual requested rows.
   */
  private int preCacheRows = 0;

  /**
   * A mapping of rows to the associated row values.
   */
  private HashMap<Integer, RowType> rowValuesMap = new HashMap<Integer, RowType>();

  /**
   * The underlying, non-cached table model.
   */
  private MutableTableModel<RowType> tableModel;

  /**
   * Construct a new {@link CachedTableModel}.
   * 
   * @param tableModel the underlying {@link MutableTableModel}
   */
  public CachedTableModel(MutableTableModel<RowType> tableModel) {
    this.tableModel = tableModel;
  }

  /**
   * Clear all data from the cache.
   */
  public void clearCache() {
    rowValuesMap.clear();
  }

  /**
   * @return the number of rows to cache after the requested rows
   */
  public int getPostCachedRowCount() {
    return postCacheRows;
  }

  /**
   * Set the number of rows to cache after the visible data area.
   * 
   * @param postCacheRows the number of rows to post cache
   */
  public void setPostCachedRowCount(int postCacheRows) {
    this.postCacheRows = postCacheRows;
  }

  /**
   * @return the number of rows to cache before the requested rows
   */
  public int getPreCachedRowCount() {
    return preCacheRows;
  }

  /**
   * Set the number of rows to cache before the visible data area.
   * 
   * @param preCacheRows the number of rows to pre cache
   */
  public void setPreCachedRowCount(int preCacheRows) {
    this.preCacheRows = preCacheRows;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.AbstractTableModel#getRowCount()
   */
  @Override
  public int getRowCount() {
    return tableModel.getRowCount();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.AbstractTableModel#setRowCount(int)
   */
  @Override
  public void setRowCount(int rowCount) {
    tableModel.setRowCount(rowCount);
    super.setRowCount(rowCount);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.AbstractTableModel#requestRows(org.gwt.mosaic.ui.client.table.TableModelHelper.Request,
   *      org.gwt.mosaic.ui.client.table.TableModel.Callback)
   */
  @Override
  public void requestRows(Request request, Callback<RowType> callback) {
    // Clear the cache if the sort order has changed
    ColumnSortList sortList = request.getColumnSortList();
    if (sortList == null) {
      if (lastSortList != null) {
        clearCache();
        lastSortList = null;
      }
    } else if (!sortList.equals(lastSortList)) {
      clearCache();
      lastSortList = sortList.copy();
    }

    // Check if all requested rows are in the cache
    int startRow = request.getStartRow();
    int numRows = request.getNumRows();
    int lastRow = startRow + numRows - 1;
    int totalNumRows = getRowCount();
    if (totalNumRows != UNKNOWN_ROW_COUNT) {
      lastRow = Math.min(lastRow, totalNumRows - 1);
    }
    boolean fullyCached = true;
    for (int row = startRow; row <= lastRow; row++) {
      if (!rowValuesMap.containsKey(Integer.valueOf(row))) {
        fullyCached = false;
        break;
      }
    }

    // Return the fully cached data
    if (fullyCached) {
      callback.onRowsReady(request, new CacheResponse(startRow, lastRow));
      return;
    }

    // Calculate bounds including the pre and post cache
    int uncachedFirstRow = Math.max(0, startRow - preCacheRows);
    int uncachedLastRow = lastRow + postCacheRows;

    // Check the upper bounds against the total number of rows
    if (totalNumRows != UNKNOWN_ROW_COUNT) {
      lastRow = Math.min(totalNumRows - 1, lastRow);
      uncachedLastRow = Math.min(totalNumRows - 1, uncachedLastRow);
    }

    // Skip past any data already retrieved starting at the first row
    for (int row = uncachedFirstRow; row <= lastRow; row++) {
      if (rowValuesMap.containsKey(Integer.valueOf(row))) {
        uncachedFirstRow++;
      } else {
        // Need to request the remaining rows
        break;
      }
    }

    // Skip past any data already retrieved from the last row
    for (int row = uncachedLastRow; row >= startRow; row--) {
      if (rowValuesMap.containsKey(Integer.valueOf(row))) {
        uncachedLastRow--;
      } else {
        // Need to request the remaining rows
        break;
      }
    }

    // Request the remaining rows that aren't in the cache
    int uncachedNumRows = uncachedLastRow - uncachedFirstRow + 1;
    Request newRequest = new Request(uncachedFirstRow, uncachedNumRows,
        sortList);
    tableModel.requestRows(newRequest, new CacheCallback(request, callback,
        startRow, lastRow - startRow + 1));
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#onRowInserted(int)
   */
  public boolean onRowInserted(int beforeRow) {
    clearCache();
    return tableModel.onRowInserted(beforeRow);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#onRowRemoved(int)
   */
  public boolean onRowRemoved(int row) {
    clearCache();
    return tableModel.onRowRemoved(row);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#onSetRowValue(int,
   *      java.lang.Object)
   */
  public boolean onSetRowValue(int row, RowType rowValue) {
    rowValuesMap.put(Integer.valueOf(row), rowValue);
    return tableModel.onSetRowValue(row, rowValue);
  }
}
