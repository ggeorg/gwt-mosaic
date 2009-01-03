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
package org.gwt.mosaic.ui.client.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.AbstractCellEditor;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.widgetideas.table.client.RendererCallback;
import com.google.gwt.widgetideas.table.client.RowPagingListener;
import com.google.gwt.widgetideas.table.client.RowPagingListenerCollection;
import com.google.gwt.widgetideas.table.client.SortableGrid;
import com.google.gwt.widgetideas.table.client.SourceRowPagingEvents;
import com.google.gwt.widgetideas.table.client.TableModel;
import com.google.gwt.widgetideas.table.client.TableModelListener;
import com.google.gwt.widgetideas.table.client.AbstractCellEditor.CellEditInfo;
import com.google.gwt.widgetideas.table.client.ScrollTable.ScrollTableImages;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorter;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorterCallback;
import com.google.gwt.widgetideas.table.client.TableModel.Callback;
import com.google.gwt.widgetideas.table.client.TableModel.ColumnSortList;
import com.google.gwt.widgetideas.table.client.TableModel.Response;
import com.google.gwt.widgetideas.table.client.TableModelHelper.Request;

/**
 * A {@link ScrollTable} that acts as a view for an underlying
 * {@link TableModel}.
 * 
 * @param <R> the data type of the row values
 */
public class PagingScrollTable<R> extends ScrollTable implements
    SourceRowPagingEvents {
  /**
   * The renderer used to set cell contents.
   */
  public static interface CellRenderer {
    /**
     * Render the contents of a cell.
     * 
     * @param grid the grid to render the contents in
     * @param row the row index
     * @param column the column index
     * @param data the data to render
     */
    void renderCell(DataGrid grid, int row, int column, Object data);
  }

  /**
   * An iterator over the visible rows in an iterator over many rows.
   */
  private static class VisibleRowsIterator implements
      Iterator<Iterator<Object>> {
    /**
     * The iterator of row data.
     */
    private Iterator<Iterator<Object>> rows;

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
     * 
     * @param rows the iterator over row data
     * @param firstRow the first absolute row of the rows iterator
     * @param firstVisibleRow the first visible row in this grid
     * @param lastVisibleRow the last visible row in this grid
     */
    public VisibleRowsIterator(Iterator<Iterator<Object>> rows, int firstRow,
        int firstVisibleRow, int lastVisibleRow) {
      this.curRow = firstRow;
      this.lastVisibleRow = lastVisibleRow;

      // Iterate up to the first row
      while (curRow < firstVisibleRow && rows.hasNext()) {
        rows.next();
        curRow++;
      }
      this.rows = rows;
    }

    public boolean hasNext() {
      return (curRow <= lastVisibleRow && rows.hasNext());
    }

    public Iterator<Object> next() {
      // Check that the next row exists
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return rows.next();
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove not supported");
    }
  }

  /**
   * The bulk render used to render the contents of this table.
   */
  private FixedWidthGridBulkRenderer bulkRenderer = null;

  /**
   * The callback used with cell editors.
   */
  private AbstractCellEditor.Callback<R> cellEditorCallback = null;

  /**
   * The cell editors for each column.
   */
  private Map<Integer, AbstractCellEditor<R>> cellEditors = null;

  /**
   * The cell renderer used on the data table.
   */
  private CellRenderer cellRenderer = null;

  /**
   * The current visible page.
   */
  private int currentPage = -1;

  /**
   * The wrapper around the empty table widget.
   */
  private SimplePanel emptyTableWidgetWrapper = new SimplePanel();

  /**
   * The old page count, used to detect when the number of pages changes.
   */
  private int oldPageCount;

  /**
   * The number of rows per page. If the number of rows per page is equal to the
   * number of rows, paging is disabled because only one page exists.
   */
  private int pageSize = 0;

  /**
   * The callback that handles page requests.
   */
  private Callback<R> pagingCallback = new Callback<R>() {
    public void onFailure(Throwable caught) {
      if (rowPagingListeners != null) {
        rowPagingListeners.firePagingFailuire(caught);
      }
    }

    public void onRowsReady(Request request, Response<R> response) {
      setData(request.getStartRow(), response.getIterator(),
          response.getRowValues());
    }
  };

  /**
   * The listeners attached to this table.
   */
  private RowPagingListenerCollection rowPagingListeners = null;

  /**
   * The values associated with each row. This is an optional list of data that
   * ties the visible content in each row to an underlying object.
   */
  private List<R> rowValues = null;

  /**
   * The underlying table model.
   */
  private TableModel<R> tableModel;

  /**
   * The {@link RendererCallback} used when table rendering completes.
   */
  private RendererCallback tableRendererCallback = new RendererCallback() {
    public void onRendered() {
      if (rowPagingListeners != null) {
        rowPagingListeners.firePageLoaded(currentPage);
      }
    }
  };

  /**
   * Constructor.
   * 
   * @param tableModel the underlying table model
   * @param dataTable the table used to display data
   * @param headerTable the header table
   */
  public PagingScrollTable(TableModel<R> tableModel, DataGrid dataTable,
      FixedWidthFlexTable headerTable) {
    this(tableModel, dataTable, headerTable,
        GWT.<ScrollTableImages> create(ScrollTableImages.class));
  }

  /**
   * Constructor.
   * 
   * @param tableModel the underlying table model
   * @param dataTable the table used to display data
   * @param headerTable the header table
   * @param images the images to use in the table
   */
  public PagingScrollTable(TableModel<R> tableModel, DataGrid dataTable,
      FixedWidthFlexTable headerTable, ScrollTableImages images) {
    super(dataTable, headerTable, images);
    this.tableModel = tableModel;
    // TODO insert(emptyTableWidgetWrapper, getElement(), 2, true);
    // TODO setEmptyTableWidgetVisible(false);
    oldPageCount = getNumPages();

    // Listen to table model events
    tableModel.addTableModelListener(new TableModelListener() {
      public void onRowCountChanged(int rowCount) {
        int pageCount = getNumPages();
        if (pageCount != oldPageCount && rowPagingListeners != null) {
          oldPageCount = pageCount;
          rowPagingListeners.fireNumPagesChanged(pageCount);
        }
      }

      public void onRowInserted(int beforeRow) {
        insertAbsoluteRow(beforeRow);
      }

      public void onRowRemoved(int row) {
        removeAbsoluteRow(row);
      }

      public void onSetData(int row, int cell, Object data) {
        setAbsoluteData(row, cell, data);
      }
    });

    // Listen for cell click events
    dataTable.addTableListener(new TableListener() {
      public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        editCell(row, cell);
      }
    });

    // Override the column sorter
    if (dataTable.getColumnSorter() == null) {
      ColumnSorter sorter = new ColumnSorter() {
        @Override
        public void onSortColumn(SortableGrid grid, ColumnSortList sortList,
            ColumnSorterCallback callback) {
          reloadPage();
          callback.onSortingComplete();
        }
      };
      dataTable.setColumnSorter(sorter);
    }
  }

  /**
   * Add a new {@link RowPagingListener}.
   * 
   * @param listener the listener
   */
  public void addRowPagingListener(RowPagingListener listener) {
    if (rowPagingListeners == null) {
      rowPagingListeners = new RowPagingListenerCollection();
    }
    rowPagingListeners.add(listener);
  }

  /**
   * Invoke the cell editor on a cell, if one is set. If a cell editor is not
   * specified, this method has no effect.
   */
  protected void editCell(int row, int column) {
    AbstractCellEditor<R> cellEditor = getCellEditor(column);
    if (cellEditor == null) {
      return;
    }
    CellEditInfo<R> editInfo = new CellEditInfo<R>(getDataTable(), row, column,
        getRowValue(row));
    cellEditor.editCell(editInfo, getCellEditorCallback());
  }

  /**
   * Get the column editor for a column.
   * 
   * @param column the column index
   * @return the cell editor
   */
  public AbstractCellEditor<R> getCellEditor(int column) {
    if (cellEditors == null) {
      return null;
    }
    return cellEditors.get(new Integer(column));
  }

  /**
   * @return the cell editor callback.
   */
  protected AbstractCellEditor.Callback<R> getCellEditorCallback() {
    if (cellEditorCallback == null) {
      cellEditorCallback = new AbstractCellEditor.Callback<R>() {
        public void onCancel(CellEditInfo<R> cellEditInfo) {
        }

        public void onComplete(CellEditInfo<R> cellEditInfo, Object value) {
          tableModel.setData(cellEditInfo.getRow(), cellEditInfo.getCell(), value);
        }
      };
    }
    return cellEditorCallback;
  }

  /**
   * @return the {@link CellRenderer} used to render cells.
   */
  public CellRenderer getCellRenderer() {
    return cellRenderer;
  }

  /**
   * @return the current page
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * @return the widget displayed when the data table is empty
   */
  public Widget getEmptyTableWidget() {
    return emptyTableWidgetWrapper.getWidget();
  }

  /**
   * Get the first visible row index.
   * 
   * @return the first row index
   */
  protected int getFirstRow() {
    return currentPage * pageSize;
  }

  /**
   * Get the last visible row index.
   * 
   * @return the last row index
   */
  protected int getLastRow() {
    if (tableModel.getRowCount() < 0) {
      // Unknown row count, so just return based on current page
      return (currentPage + 1) * pageSize - 1;
    } else if (pageSize == 0) {
      // Only one page, so return row count
      return tableModel.getRowCount() - 1;
    }
    return Math.min(tableModel.getRowCount(), (currentPage + 1) * pageSize) - 1;
  }

  /**
   * @return the number of pages, or -1 if not known
   */
  public int getNumPages() {
    if (pageSize < 1) {
      return 1;
    } else {
      int numDataRows = tableModel.getRowCount();
      if (numDataRows < 0) {
        return -1;
      }
      return (int) Math.ceil(numDataRows / (pageSize + 0.0));
    }
  }

  /**
   * @return the number of rows per page
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Get the value associated with a row.
   * 
   * @param row the row index
   * @return the value associated with the row
   */
  public R getRowValue(int row) {
    if (rowValues == null || rowValues.size() <= row) {
      return null;
    }
    return rowValues.get(row);
  }

  /**
   * @return the table model
   */
  public TableModel<R> getTableModel() {
    return tableModel;
  }

  /**
   * Go to the first page.
   */
  public void gotoFirstPage() {
    gotoPage(0, false);
  }

  /**
   * Go to the last page. If the number of pages is not known, this method is
   * ignored.
   */
  public void gotoLastPage() {
    if (getNumPages() >= 0) {
      gotoPage(getNumPages(), false);
    }
  }

  /**
   * Go to the next page.
   */
  public void gotoNextPage() {
    gotoPage(currentPage + 1, false);
  }

  /**
   * Set the current page. If the page is out of bounds, it will be
   * automatically set to zero or the last page without throwing any errors.
   * 
   * @param page the page
   * @param forced reload the page even if it is already loaded
   */
  public void gotoPage(int page, boolean forced) {
    int oldPage = currentPage;
    int numPages = getNumPages();
    if (numPages >= 0) {
      currentPage = Math.max(0, Math.min(page, numPages - 1));
    } else {
      currentPage = page;
    }

    if (currentPage != oldPage || forced) {
      // Deselect rows when switching pages
      DataGrid dataTable = (DataGrid) getDataTable();
      dataTable.deselectAllRows();

      // Fire listeners
      if (rowPagingListeners != null) {
        rowPagingListeners.firePageChanged(currentPage);
      }

      // Clear out existing data if we aren't bulk rendering
      if (bulkRenderer == null) {
        int rowCount = getLastRow() - getFirstRow() + 1;
        if (rowCount != dataTable.getRowCount()) {
          dataTable.resizeRows(rowCount);
        }
        dataTable.clearAll();
      }

      // Request the new data from the table model
      Request request = new Request(currentPage * pageSize, pageSize,
          dataTable.getColumnSortList());
      tableModel.requestRows(request, pagingCallback);
    }
  }

  /**
   * Go to the previous page.
   */
  public void gotoPreviousPage() {
    gotoPage(currentPage - 1, false);
  }

  /**
   * Check whether a column has a cell editor.
   * 
   * @param column the column index
   * @return true if a cell editor is assigned
   */
  public boolean hasCellEditor(int column) {
    if (cellEditors == null) {
      return false;
    }
    return cellEditors.containsKey(new Integer(column));
  }

  /**
   * Insert a row into the table relative to the total number of rows.
   * 
   * @param beforeRow the row index
   */
  protected void insertAbsoluteRow(int beforeRow) {
    // Physically insert the row
    int lastRow = getLastRow() + 1;
    if (beforeRow <= lastRow) {
      int firstRow = getFirstRow();
      if (beforeRow >= firstRow) {
        // Insert row in the middle of the page
        getDataTable().insertRow(beforeRow - firstRow);
      } else {
        // Insert zero row because row is before this page
        getDataTable().insertRow(0);
      }
      if (getDataTable().getRowCount() > pageSize) {
        getDataTable().removeRow(pageSize);
      }
    }
  }

  /**
   * This method is called immediately after a widget becomes attached to the
   * browser's document.
   */
  @Override
  protected void onLoad() {
    super.onLoad();

    // If we have not loaded any pages, load one now
    if (currentPage < 0) {
      gotoPage(0, true);
    }
  }

  /**
   * Reload the current page.
   */
  public void reloadPage() {
    if (currentPage >= 0) {
      gotoPage(currentPage, true);
    } else {
      gotoPage(0, true);
    }
  }

  /**
   * Remove a row from the table relative to the total number of rows.
   * 
   * @param row the row index
   */
  protected void removeAbsoluteRow(int row) {
    // Physically remove the row
    int lastRow = getLastRow();
    if (row <= lastRow) {
      int firstRow = getFirstRow();
      if (row >= firstRow) {
        // Remove a row in the middle of the page
        getDataTable().removeRow(row - firstRow);
      } else {
        // Remove first row because row is before this page
        getDataTable().removeRow(0);
      }
      getDataTable().insertRow(pageSize - 1);
    }
  }

  /**
   * Remove a {@link RowPagingListener}.
   * 
   * @param listener the listener to remove
   */
  public void removeRowPagingListener(RowPagingListener listener) {
    if (rowPagingListeners != null) {
      rowPagingListeners.remove(listener);
    }
  }

  /**
   * Render the contents of the cell.
   * 
   * @param row the row index
   * @param column the column index
   * @param data the data to render
   */
  protected void renderCell(int row, int column, Object data) {
    if (cellRenderer == null) {
      if (data instanceof Widget) {
        getDataTable().setWidget(row, column, (Widget) data);
      } else {
        getDataTable().setHTML(row, column, data + "");
      }
    } else {
      cellRenderer.renderCell((DataGrid) getDataTable(), row, column, data);
    }
  }

  /**
   * Set the data in a cell. The data object will be rendered using the
   * {@link CellRenderer}, if one is specified.
   * 
   * The row index in this method is relative to the total number of rows across
   * all pages. It is not the same as the row index passed into setHTML, which
   * is relative to the current page.
   * 
   * @param row the row index
   * @param column the column index
   * @param data the data to set
   */
  protected void setAbsoluteData(int row, int column, Object data) {
    int firstRow = getFirstRow();
    if ((row >= firstRow) && (row <= getLastRow())) {
      renderCell(row - firstRow, column, data);
    }
  }

  /**
   * Set the bulk table renderer.
   * 
   * @param bulkRenderer the table renderer
   */
  public void setBulkRenderer(FixedWidthGridBulkRenderer bulkRenderer) {
    this.bulkRenderer = bulkRenderer;
  }

  /**
   * Set the cell editor for a column.
   * 
   * @param column the column index
   * @param editor the cell editor
   */
  public void setCellEditor(int column, AbstractCellEditor<R> editor) {
    if (cellEditors == null) {
      cellEditors = new HashMap<Integer, AbstractCellEditor<R>>();
    }
    if (editor == null) {
      cellEditors.remove(new Integer(column));
    } else {
      cellEditors.put(new Integer(column), editor);
    }
  }

  /**
   * Set the {@link CellRenderer} used to render cell contents.
   * 
   * @param cellRenderer the new renderer
   */
  public void setCellRenderer(CellRenderer cellRenderer) {
    this.cellRenderer = cellRenderer;
  }

  /**
   * Set a block of data. This method is used when responding to data requests.
   * 
   * This method takes an iterator of iterators, where each iterator represents
   * one row of data starting with the first row.
   * 
   * @param firstRow the row index
   * @param rows the 2D Iterator of data
   * @param rowValues the values associated with each row
   */
  protected void setData(int firstRow, Iterator<Iterator<Object>> rows,
      List<R> rowValues) {
    getDataTable().deselectAllRows();
    this.rowValues = rowValues;
    if (rows != null && rows.hasNext()) {
      // TODO setEmptyTableWidgetVisible(false);

      // Get an iterator over the visible rows
      int firstVisibleRow = getFirstRow();
      int lastVisibleRow = getLastRow();
      Iterator<Iterator<Object>> visibleIter = new VisibleRowsIterator(rows,
          firstRow, firstVisibleRow, lastVisibleRow);

      // Use the table renderer
      if (bulkRenderer != null) {
        bulkRenderer.renderRows(visibleIter, tableRendererCallback);
        return;
      }

      // Render the cells the default way
      int rowCount = 0;
      int colCount = 0;
      while (visibleIter.hasNext()) {
        Iterator<Object> columnIt = visibleIter.next();
        int curColumn = 0;
        while (columnIt.hasNext()) {
          renderCell(rowCount, curColumn, columnIt.next());
          curColumn++;
        }

        // Increment the number of rows and cells
        rowCount++;
        colCount = Math.max(colCount, curColumn);
      }

      // Get rid of unneeded rows
      getDataTable().resize(rowCount, colCount);
    } else {
      // TODO setEmptyTableWidgetVisible(true);
    }

    // Fire page loaded event
    tableRendererCallback.onRendered();
  }

  /**
   * Set the {@link Widget} that will be displayed in place of the data table
   * when the data table has no data to display.
   * 
   * @param emptyTableWidget the widget to display when the data table is empty
   */
  public void setEmptyTableWidget(Widget emptyTableWidget) {
    emptyTableWidgetWrapper.setWidget(emptyTableWidget);
  }

  /**
   * Set whether or not the empty table widget is visible.
   * 
   * @param visible true to show the empty table widget
   */
  protected void setEmptyTableWidgetVisible(boolean visible) {
    emptyTableWidgetWrapper.setVisible(visible);
    if (visible) {
      getDataWrapper().getElement().getStyle().setProperty("display", "none");
    } else {
      getDataWrapper().getElement().getStyle().setProperty("display", "");
    }
  }

  /**
   * Set the number of rows per page.
   * 
   * By default, the page size is zero, which indicates that all rows should be
   * shown on the page.
   * 
   * @param pageSize the number of rows per page
   */
  public void setPageSize(int pageSize) {
    pageSize = Math.max(0, pageSize);
    this.pageSize = pageSize;

    int pageCount = getNumPages();
    if (pageCount != oldPageCount && rowPagingListeners != null) {
      oldPageCount = pageCount;
      rowPagingListeners.fireNumPagesChanged(pageCount);
    }

    // Reset the page
    if (currentPage >= 0) {
      gotoPage(currentPage, true);
    }
  }

  /**
   * Associate a row in the table with a value.
   * 
   * @param row the row index
   * @param value the value to associate
   */
  public void setRowValue(int row, R value) {
    // Make sure the list can fit the row
    if (rowValues == null) {
      rowValues = new ArrayList<R>(row + 1);
    }
    for (int i = rowValues.size(); i <= row; i++) {
      rowValues.add(null);
    }

    // Set the row value
    rowValues.set(row, value);
  }
}
