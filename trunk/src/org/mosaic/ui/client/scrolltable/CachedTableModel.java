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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A {@link TableModel} that wraps another {@link TableModel} and adds its own
 * cache so subsequent requests for the same data will not require another
 * {@link TableModel} request.
 * 
 * <h1>Cache</h1>
 * <p>
 * The CachedTableModel supports caching rows of data before they are needed,
 * allowing listeners to request data much more quickly. When another class
 * requests data, the CachedTableModel will feed data from its cache if
 * available, and then it will request unavailable data from your specific
 * implementation.
 * </p>
 * <p>
 * The pre cache and post cache refers to the number of rows to request in
 * addition to the actual request. Pre cache refers to rows before the request,
 * and post cache refers to rows after the request. For example, in applications
 * where you expect the user to move forward quickly, you would want a large
 * post cache.
 * </p>
 * <p>
 * The size of your cache depends on the implementation and usage of your view
 * component. If you are using a view that supports paging, such as the
 * {@link PagingScrollTable}, you should set your cache to a multiple of the
 * page size so the user can go to the next and previous pages quickly.
 * </p>
 * <h1>Limitations</h1>
 * <p>
 * The cache is cleared every time the sort order changes. However, if you
 * disallow column sorting or expect that the user will not sort the columns
 * repeatedly, the cache will still improve paging performance.
 * </p>
 * 
 * @param <R> the data type of the row values
 */
public class CachedTableModel<R> extends TableModel<R> {
  /**
   * A callback that retrieves data from the cache.
   */
  public class CacheCallback implements Callback<R> {
    /**
     * The actual callback from the requestor.
     */
    private Callback<R> actualCallback;

    /**
     * The actual number of requested rows.
     */
    private int actualNumRows;

    /**
     * The actual request.
     */
    private Request actualRequest;

    /**
     * The actual first requested row.
     */
    private int actualStartRow;

    /**
     * Constructor.
     * 
     * @param callback The actual callback from the requestor
     * @param startRow The actual first requested row
     * @param numRows The actual number of requested rows
     */
    public CacheCallback(Request request, Callback<R> callback, int startRow,
        int numRows) {
      actualRequest = request;
      actualCallback = callback;
      actualStartRow = startRow;
      actualNumRows = numRows;
    }

    public void onFailure(Throwable caught) {
      actualCallback.onFailure(caught);
    }

    public void onRowsReady(Request request, Response<R> response) {
      // Save the response data into the cache
      if (response != null) {
        // Save the data for the cells
        Iterator<Iterator<Object>> rows = response.getIterator();
        if (rows != null) {
          int curRow = request.getStartRow();
          while (rows.hasNext()) {
            Iterator<Object> columnIt = rows.next();
            List<Object> rowList = ensureRowList(curRow);
            if (columnIt != null) {
              rowList.clear();
              while (columnIt.hasNext()) {
                rowList.add(columnIt.next());
              }
            }

            // Go to next row
            curRow++;
          }
        }

        // Save the row values for each row
        List<R> rowValues = response.getRowValues();
        if (rowValues != null) {
          int curRow = request.getStartRow();
          for (R rowValue : rowValues) {
            rowValuesMap.put(new Integer(curRow), rowValue);
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
   * An iterator that iterates over the cached rows of data.
   */
  private class CacheIterator implements Iterator<Iterator<Object>> {
    /**
     * The current row that will be returns on the next call to next.
     */
    int curRow;

    /**
     * The last row to iterate, inclusively.
     */
    int lastRow;

    /**
     * Constructor.
     * 
     * @param firstRow the first row of data
     * @param lastRow the last row of data
     */
    public CacheIterator(int firstRow, int lastRow) {
      this.curRow = firstRow - 1;
      this.lastRow = lastRow;
    }

    public boolean hasNext() {
      return curRow < lastRow;
    }

    public Iterator<Object> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      curRow++;
      List<Object> rowList = getRowList(curRow);
      if (rowList == null) {
        return getEmptyIterator();
      } else {
        return rowList.iterator();
      }
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove not supported.");
    }
  }

  /**
   * A response that comes directly from the cache.
   */
  private class CacheResponse extends Response<R> {
    /**
     * The first row of data to be retrieved, inclusively.
     */
    private int firstRow;

    /**
     * The first row of data to be retrieved, inclusively.
     */
    private int lastRow;

    private CacheIterator it;

    /**
     * The row values associated with the data.
     */
    private List<R> rowValues = null;

    public CacheResponse(int firstRow, int lastRow) {
      this.firstRow = firstRow;
      this.lastRow = lastRow;
      it = new CacheIterator(firstRow, lastRow);
    }

    @Override
    public Iterator<Iterator<Object>> getIterator() {
      return it;
    }

    @Override
    public List<R> getRowValues() {
      if (rowValues == null) {
        rowValues = new ArrayList<R>();
        for (int i = firstRow; i <= lastRow; i++) {
          rowValues.add(rowValuesMap.get(new Integer(i)));
        }
      }
      return rowValues;
    }
  }

  /**
   * The cached data contained within this CachedTableController. The HashMap
   * contains a collection of "rows", each of which is another List of objects.
   */
  private HashMap<Integer, List<Object>> dataMap = new HashMap<Integer, List<Object>>();

  /**
   * An empty iterator used for rows that have no data.
   */
  private Iterator<Object> emptyIterator = null;

  /**
   * The last sort info.
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
   * The cached row values.
   */
  private HashMap<Integer, R> rowValuesMap = new HashMap<Integer, R>();

  /**
   * The underlying, non-cached table model.
   */
  private TableModel<R> tableModel;

  /**
   * Constructor.
   * 
   * @param tableModel the underlying {@link TableModel}
   */
  public CachedTableModel(TableModel<R> tableModel) {
    this.tableModel = tableModel;
    setRowCount(tableModel.getRowCount());
  }

  /**
   * Clear all data from the cache.
   */
  public void clearCache() {
    dataMap.clear();
    rowValuesMap.clear();
  }

  /**
   * @return the number of rows to cache after the requested rows
   */
  public int getPostCachedRowCount() {
    return postCacheRows;
  }

  /**
   * @return the number of rows to cache before the requested rows
   */
  public int getPreCachedRowCount() {
    return preCacheRows;
  }

  @Override
  public void requestRows(Request request, Callback<R> callback) {
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
    boolean fullyCached = true;
    for (int row = startRow; row <= lastRow; row++) {
      if (!dataMap.containsKey(new Integer(row))) {
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
    int totalNumRows = getRowCount();
    if (totalNumRows != UNKNOWN_ROW_COUNT) {
      lastRow = Math.min(totalNumRows - 1, lastRow);
      uncachedLastRow = Math.min(totalNumRows - 1, uncachedLastRow);
    }

    // Skip past any data already retrieved starting at the first row
    for (int row = uncachedFirstRow; row <= lastRow; row++) {
      if (dataMap.containsKey(new Integer(row))) {
        uncachedFirstRow++;
      } else {
        // Need to request the remaining rows
        break;
      }
    }

    // Skip past any data already retrieved from the last Row
    for (int row = uncachedLastRow; row >= startRow; row--) {
      if (dataMap.containsKey(new Integer(row))) {
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
   * Set the number of rows to cache after the visible data area.
   * 
   * @param postCacheRows the number of rows to post cache
   */
  public void setPostCachedRowCount(int postCacheRows) {
    this.postCacheRows = postCacheRows;
  }

  /**
   * Set the number of rows to cache before the visible data area.
   * 
   * @param preCacheRows the number of rows to pre cache
   */
  public void setPreCachedRowCount(int preCacheRows) {
    this.preCacheRows = preCacheRows;
  }

  @Override
  protected boolean onRowInserted(int beforeRow) {
    clearCache();
    return tableModel.onRowInserted(beforeRow);
  }

  @Override
  protected boolean onRowRemoved(int row) {
    clearCache();
    return tableModel.onRowRemoved(row);
  }

  @Override
  protected boolean onSetData(int row, int cell, Object data) {
    // Save the data to the cache
    List<Object> rowList = ensureRowList(row);
    for (int i = rowList.size(); i < cell + 1; i++) {
      rowList.add(null);
    }
    rowList.set(cell, data);

    // Forward to the table model
    return tableModel.onSetData(row, cell, data);
  }

  /**
   * Get a row from the dataMap, creating it if it doesn't exist yet.
   * 
   * @param row the row index
   * @return the List of row data
   */
  private List<Object> ensureRowList(int row) {
    Integer rowI = new Integer(row);
    List<Object> rowList = dataMap.get(rowI);
    if (rowList == null) {
      rowList = new ArrayList<Object>();
      dataMap.put(rowI, rowList);
    }
    return rowList;
  }

  /**
   * Get a row from the dataMap.
   * 
   * @param row the row index
   * @return the List of row data
   */
  private List<Object> getRowList(int row) {
    return dataMap.get(new Integer(row));
  }

  /**
   * Get the empty iterator.
   * 
   * @return an empty iterator
   */
  private Iterator<Object> getEmptyIterator() {
    if (emptyIterator == null) {
      emptyIterator = new Iterator<Object>() {
        public boolean hasNext() {
          return false;
        }

        public Object next() {
          throw new NoSuchElementException();
        }

        public void remove() {
          throw new UnsupportedOperationException("Remove not supported.");
        }
      };
    }
    return emptyIterator;
  }
}
