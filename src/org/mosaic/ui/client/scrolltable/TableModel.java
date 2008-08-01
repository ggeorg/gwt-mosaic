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

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class to retrieve row data to be used in a table.
 * 
 * @param <R> the data type of the row values
 */
public abstract class TableModel<R> implements SourceTableModelEvents {
  /**
   * Callback for {@link TableModel}. Every Request should be associated with a
   * callback that should be called after a Response is generated.
   * 
   * @param <R> the data type of the row values
   */
  public static interface Callback<R> {
    /**
     * Called when an error occurs and the rows cannot be loaded.
     * 
     * @param caught the exception that was thrown
     */
    void onFailure(Throwable caught);

    /**
     * Consume the data created by {@link TableModel} in response to a Request.
     * 
     * @param request the request
     * @param response the response
     */
    void onRowsReady(Request request, Response<R> response);
  }

  /**
   * Information about the sort order of a specific column in a table.
   */
  public static class ColumnSortInfo implements IsSerializable {
    /**
     * Is the sort ascending.
     */
    private boolean ascending;

    /**
     * The column index.
     */
    private int column;

    /**
     * Default Constructor.
     */
    public ColumnSortInfo() {
      this(0, true);
    }

    /**
     * Constructor.
     * 
     * @param column the column index
     * @param ascending true if sorted ascending
     */
    public ColumnSortInfo(int column, boolean ascending) {
      this.column = column;
      this.ascending = ascending;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ColumnSortInfo) {
        return equals((ColumnSortInfo) obj);
      }
      return false;
    }

    /**
     * Check if this object is equal to another. The objects are equal if the
     * column and ascending values are equal.
     * 
     * @param csi the other object
     * @return true if objects are the same
     */
    public boolean equals(ColumnSortInfo csi) {
      if (csi == null) {
        return false;
      }
      return getColumn() == csi.getColumn() && isAscending() == csi.isAscending();
    }

    /**
     * @return the column index
     */
    public int getColumn() {
      return column;
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }

    /**
     * @return true if ascending, false if descending
     */
    public boolean isAscending() {
      return ascending;
    }

    /**
     * Set whether or not the sorting is ascending or descending.
     * 
     * @param ascending true if ascending, false if descending
     */
    public void setAscending(boolean ascending) {
      this.ascending = ascending;
    }

    /**
     * Set the column index.
     * 
     * @param column the column index
     */
    public void setColumn(int column) {
      this.column = column;
    }
  }

  /**
   * An ordered list of sorting info where each entry tells us how to sort a
   * single column. The first entry is the primary sort order, the second entry
   * is the first tie-breaker, and so on.
   */
  public static class ColumnSortList implements IsSerializable, Iterable<ColumnSortInfo> {
    /**
     * A List used to manage the insertion/removal of {@link ColumnSortInfo}.
     */
    private List<ColumnSortInfo> infos = new ArrayList<ColumnSortInfo>();

    /**
     * Add a {@link ColumnSortInfo} to this list. If the column already exists,
     * it will be removed from its current position and placed at the start of
     * the list, becoming the primary sort info.
     * 
     * This add method inserts an entry at the beginning of the list. It does
     * not append the entry to the end of the list.
     * 
     * @param sortInfo the {@link ColumnSortInfo} to add
     */
    public void add(ColumnSortInfo sortInfo) {
      add(0, sortInfo);
    }

    /**
     * Inserts the specified {@link ColumnSortInfo} at the specified position in
     * this list. If the column already exists in the sort info, the index will
     * be adjusted to account for any removed entries.
     * 
     * @param sortInfo the {@link ColumnSortInfo} to add
     */
    public void add(int index, ColumnSortInfo sortInfo) {
      // Remove sort info for duplicate columns
      int column = sortInfo.getColumn();
      for (int i = 0; i < infos.size(); i++) {
        ColumnSortInfo curInfo = infos.get(i);
        if (curInfo.getColumn() == column) {
          infos.remove(i);
          i--;
          if (column < index) {
            index--;
          }
        }
      }

      // Insert the new sort info
      infos.add(index, sortInfo);
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
      infos.clear();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ColumnSortList) {
        return equals((ColumnSortList) obj);
      }
      return false;
    }

    /**
     * Check if this object is equal to another.
     * 
     * @param csl the other object
     * @return true if objects are equal
     */
    public boolean equals(ColumnSortList csl) {
      // Object is null
      if (csl == null) {
        return false;
      }

      // Check the size of the lists
      int size = size();
      if (size != csl.size()) {
        return false;
      }

      // Compare the entries
      for (int i = 0; i < size; i++) {
        if (!infos.get(i).equals(csl.infos.get(i))) {
          return false;
        }
      }

      // Everything is equal
      return true;
    }

    /**
     * Get the primary (first) {@link ColumnSortInfo}'s column index.
     * 
     * @return the primary column or -1 if not sorted
     */
    public int getPrimaryColumn() {
      ColumnSortInfo primaryInfo = getPrimaryColumnSortInfo();
      if (primaryInfo == null) {
        return -1;
      }
      return primaryInfo.getColumn();
    }

    /**
     * Get the primary (first) {@link ColumnSortInfo}.
     * 
     * @return the primary column sort info
     */
    public ColumnSortInfo getPrimaryColumnSortInfo() {
      if (infos.size() > 0) {
        return infos.get(0);
      }
      return null;
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }

    /**
     * Get the primary (first) {@link ColumnSortInfo}'s sort order.
     * 
     * @return true if ascending, false if descending
     */
    public boolean isPrimaryAscending() {
      ColumnSortInfo primaryInfo = getPrimaryColumnSortInfo();
      if (primaryInfo == null) {
        return true;
      }
      return primaryInfo.isAscending();
    }

    public Iterator<ColumnSortInfo> iterator() {
      return new ImmutableIterator<ColumnSortInfo>(infos.iterator());
    }

    /**
     * Remove a {@link ColumnSortInfo} from the list.
     * 
     * @param sortInfo the {@link ColumnSortInfo} to remove
     */
    public boolean remove(Object sortInfo) {
      return infos.remove(sortInfo);
    }

    /**
     * Returns the number of {@link ColumnSortInfo} in the list.
     * 
     * @return the size of the list
     */
    public int size() {
      return infos.size();
    }

    /**
     * Copy this {@link ColumnSortList}.
     * 
     * @return a duplicate of this list
     */
    ColumnSortList copy() {
      ColumnSortList copy = new ColumnSortList();
      for (ColumnSortInfo info : this) {
        copy.infos.add(new ColumnSortInfo(info.getColumn(), info.isAscending()));
      }
      return copy;
    }
  }

  /**
   * A {@link TableModel} request.
   */
  public static class Request implements IsSerializable {
    /**
     * The number of rows to request.
     */
    private int numRows;

    /**
     * An ordered list of {@link ColumnSortInfo}.
     */
    private ColumnSortList columnSortList;

    /**
     * The first row of table data to request.
     */
    private int startRow;

    /**
     * Default constructor.
     */
    public Request() {
      this(0, 0, null);
    }

    /**
     * Constructor.
     * 
     * @param startRow the first row to request
     * @param numRows the number of rows to request
     */
    public Request(int startRow, int numRows) {
      this(startRow, numRows, null);
    }

    /**
     * Constructor.
     * 
     * @param startRow the first row to request
     * @param numRows the number of rows to request
     * @param columnSortList column sort orders
     */
    public Request(int startRow, int numRows, ColumnSortList columnSortList) {
      this.startRow = startRow;
      this.numRows = numRows;
      this.columnSortList = columnSortList;
    }

    /**
     * Get the {@link ColumnSortInfo}, which includes the sort indexes and
     * ascending or descending order info.
     * 
     * @return the sort info
     */
    public ColumnSortList getColumnSortList() {
      return columnSortList;
    }

    /**
     * Get the number of rows to request.
     * 
     * @return the number of requested rows
     */
    public int getNumRows() {
      return numRows;
    }

    /**
     * Get the first row to request.
     * 
     * @return the first requested row
     */
    public int getStartRow() {
      return startRow;
    }
  }

  /**
   * A response from the {@link TableModel}.
   * 
   * @param <R> the data type of the row values
   */
  public abstract static class Response<R> {
    /**
     * The values associated with each row.
     */
    private List<R> rowValues;

    /**
     * Constructor.
     */
    public Response() {
      this(null);
    }

    /**
     * Constructor.
     * 
     * @param rowValues the values to associate with each row
     */
    public Response(List<R> rowValues) {
      this.rowValues = rowValues;
    }

    /**
     * Gets the iterator associated with the row data generated by the
     * {@link Response}.
     * 
     * @return the rows data
     */
    public abstract Iterator<Iterator<Object>> getIterator();

    /**
     * Get a list of objects associated with the retrieved rows.
     * 
     * @return the list of objects associated with the retrieved row
     */
    public List<R> getRowValues() {
      return rowValues;
    }
  }

  /**
   * A response from the {@link TableModel} that is serializable, and can by
   * used over RPC.
   * 
   * @param <R> the serializable data type of the row values
   */
  public static class SerializableResponse<R extends Serializable> extends Response<R>
      implements IsSerializable {
    /**
     * The 2D {@link Collection} of serializable cell data.
     */
    private Collection<Collection<Serializable>> rows;

    /**
     * Default constructor.
     */
    public SerializableResponse() {
      this(null);
    }

    /**
     * Constructor.
     * 
     * @param rows the row data
     */
    public SerializableResponse(Collection<Collection<Serializable>> rows) {
      this(rows, null);
    }

    /**
     * Constructor.
     * 
     * @param rows the row data
     * @param rowValues the values to associate with each row
     */
    public SerializableResponse(Collection<Collection<Serializable>> rows,
        List<R> rowValues) {
      super(rowValues);
      this.rows = rows;
    }

    @Override
    public Iterator<Iterator<Object>> getIterator() {
      if (rows == null) {
        return null;
      }
      return new SerializableResponseIterator(rows);
    }
  }

  /**
   * An iterator over an array.
   */
  private static class ImmutableIterator<E> implements Iterator<E> {
    private Iterator<E> iterator;

    public ImmutableIterator(Iterator<E> iterator) {
      this.iterator = iterator;
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public E next() {
      return iterator.next();
    }

    public void remove() {
      throw (new UnsupportedOperationException());
    }
  }

  /**
   * Converts an iterator of some type into an iterator of objects.
   * 
   * @param <E> the type of iterator to wraps
   */
  private static class ObjectIterator<E> implements Iterator<Object> {
    private Iterator<E> iterator;

    public ObjectIterator(Iterator<E> iterator) {
      this.iterator = iterator;
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    public Object next() {
      return iterator.next();
    }

    public void remove() {
      throw (new UnsupportedOperationException());
    }
  }

  /**
   * An {@link Iterator} over a 2D {@link Collection} of column data.
   */
  private static class SerializableResponseIterator implements Iterator<Iterator<Object>> {
    /**
     * The {@link Iterator} of row data. Each row is a {@link Collection} of
     * column data.
     */
    private Iterator<Collection<Serializable>> rows;

    /**
     * Default constructor.
     */
    public SerializableResponseIterator() {
      this(null);
    }

    /**
     * Constructor.
     * 
     * @param rows the row data
     */
    public SerializableResponseIterator(Collection<Collection<Serializable>> rows) {
      if (rows != null) {
        this.rows = rows.iterator();
      }
    }

    public boolean hasNext() {
      if (rows == null) {
        return false;
      }
      return rows.hasNext();
    }

    public Iterator<Object> next() {
      if (!hasNext()) {
        throw (new NoSuchElementException());
      }
      Collection<Serializable> next = rows.next();
      if (next == null) {
        return null;
      } else {
        return new ObjectIterator<Serializable>(next.iterator());
      }
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Use the ALL_ROWS value in place of the numRows variable when requesting all
   * rows.
   */
  public static final int ALL_ROWS = -1;

  /**
   * Indicates that the number of rows is unknown, and therefore unbounded.
   */
  public static final int UNKNOWN_ROW_COUNT = -1;

  /**
   * The listeners of this Table Model.
   */
  private TableModelListenerCollection listeners = null;

  /**
   * The number of rows.
   */
  private int rowCount = UNKNOWN_ROW_COUNT;

  /**
   * Add a new {@link TableModelListener}.
   * 
   * @param listener the listener
   */
  public void addTableModelListener(TableModelListener listener) {
    if (listeners == null) {
      listeners = new TableModelListenerCollection();
    }
    listeners.add(listener);
  }

  /**
   * Return the total number of rows. If the number is not known, return
   * {@link #UNKNOWN_ROW_COUNT}.
   * 
   * @return the total number of rows, or {@link #UNKNOWN_ROW_COUNT}
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * Insert a row and increment the row count by one.
   * 
   * @param beforeRow the row index of the new row
   */
  public void insertRow(int beforeRow) {
    if (onRowInserted(beforeRow)) {
      // Fire listeners
      if (listeners != null) {
        listeners.fireRowInserted(beforeRow);
      }

      // Increment the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT) {
        setRowCount(numRows + 1);
      }
    }
  }

  /**
   * Remove a row and decrement the row count by one.
   * 
   * @param row the row index of the removed row
   */
  public void removeRow(int row) {
    if (onRowRemoved(row)) {
      // Fire listeners
      if (listeners != null) {
        listeners.fireRowRemoved(row);
      }

      // Decrement the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT) {
        setRowCount(numRows - 1);
      }
    }
  }

  /**
   * Remove a {@link TableModelListener}.
   * 
   * @param listener the listener to remove
   */
  public void removeTableModelListener(TableModelListener listener) {
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

  /**
   * Generate a {@link Response} based on a specific {@link Request}. The
   * response is passed into the {@link Callback}.
   * 
   * @param request the {@link Request} for row data
   * @param callback the {@link Callback} to use for the {@link Response}
   */
  public abstract void requestRows(Request request, Callback<R> callback);

  /**
   * Set the data in a specific cell.
   * 
   * @param row the row index
   * @param cell the cell index
   * @param data the new contents of the cell
   */
  public void setData(int row, int cell, Object data) {
    if (onSetData(row, cell, data)) {
      // Fire the listeners
      if (listeners != null) {
        listeners.fireSetData(row, cell, data);
      }

      // Update the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT && row >= numRows) {
        setRowCount(row + 1);
      }
    }
  }

  /**
   * Set the total number of rows.
   * 
   * @param rowCount the row count
   */
  public void setRowCount(int rowCount) {
    if (this.rowCount != rowCount) {
      this.rowCount = rowCount;
      if (listeners != null) {
        listeners.fireRowCountChanged(rowCount);
      }
    }
  }

  /**
   * Event fired when a row is inserted. Returning true will increment the row
   * count by one.
   * 
   * @param beforeRow the row index of the new row
   * @return true if the action is successful
   */
  protected abstract boolean onRowInserted(int beforeRow);

  /**
   * Event fired when a row is removed. Returning true will decrement the row
   * count by one.
   * 
   * @param row the row index of the removed row
   * @return true if the action is successful
   */
  protected abstract boolean onRowRemoved(int row);

  /**
   * Event fired when the local data changes. Returning true will ensure that
   * the row count is at least as one greater than the row index.
   * 
   * @param row the row index
   * @param cell the cell index
   * @param data the new contents of the cell
   * @return true if the action is successful
   */
  protected abstract boolean onSetData(int row, int cell, Object data);
}
