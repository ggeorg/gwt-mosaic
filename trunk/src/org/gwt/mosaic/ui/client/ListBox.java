/*
 * Copyright 2006-2008 Google Inc.
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
package org.gwt.mosaic.ui.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gwt.mosaic.ui.client.ColumnWidget.ResizePolicy;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.SourceTableSelectionEvents;
import com.google.gwt.widgetideas.table.client.TableSelectionListener;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;

/**
 * This widget is used to create a list of items where one or more of the items
 * may be selected. A {@code ListBox} may contain multiple columns. There are
 * numerous methods which allow the items in the {@code ListBox} to be retrieved
 * and modified.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
public class ListBox<T extends Object> extends LayoutComposite {

  /**
   * The render used to set cell contents.
   * 
   * @param <T>
   */
  public interface CellRenderer<T> {
    /**
     * Render the contents of a cell.
     * 
     * @param grid the grid to render the contents in
     * @param row the row index
     * @param column the column index
     * @param item the item to render
     */
    void renderCell(DataGrid grid, int row, int column, T item);
  }

  public static class DataGrid extends FixedWidthGrid {
    @Override
    protected void hoverCell(Element cellElem) {
      super.hoverCell(cellElem);
    }
  }

  private static final int INSERT_AT_END = -1;

  private final ColumnWidget columnWidget;
  private final DataGrid dataTable = new DataGrid();
  private final FixedWidthFlexTable headerTable;

  /**
   * The cell renderer used on the data table.
   */
  private CellRenderer<T> cellRenderer = new CellRenderer<T>() {
    public void renderCell(DataGrid grid, int row, int column, T item) {
      if (item instanceof Widget) {
        grid.setWidget(row, column, (Widget) item);
      } else {
        grid.setText(row, column, item.toString());
      }
    }
  };

  /**
   * Get the {@link CellRenderer} used to render cells.
   * 
   * @return the current renderer
   */
  public CellRenderer<T> getCellRenderer() {
    return cellRenderer;
  }

  /**
   * Set the {@link CellRenderer} used to render cell contents.
   * 
   * @param cellRenderer the new renderer
   */
  public void setCellRenderer(CellRenderer<T> cellRenderer) {
    this.cellRenderer = cellRenderer;
  }

  /**
   * The values associated with each row.
   */
  private Map<Element, T> rowItems = new HashMap<Element, T>();

  /**
   * Creates an empty list box in single selection mode.
   */
  public ListBox() {
    this(null);
  }

  /**
   * 
   * @param strings
   */
  public ListBox(String[] columns) {
    if (columns != null && columns.length > 0) {
      headerTable = new FixedWidthFlexTable();
      for (int column = 0; column < columns.length; ++column) {
        headerTable.setHTML(0, column, columns[column]);
      }
      setColumnsCount(columns.length);
    } else {
      headerTable = null;
    }

    final LayoutPanel layoutPanel = getWidget();
    columnWidget = new ColumnWidget(dataTable, headerTable) {
      @Override
      protected void hoverCell(Element cellElem) {
        dataTable.hoverCell(cellElem);
      }
    };
    setMultipleSelect(false);
    columnWidget.setResizePolicy(ResizePolicy.FILL_WIDTH);
    layoutPanel.add(columnWidget);
  }

  /**
   * Adds an item to the list box.
   * 
   * @param item the item to be added
   */
  public void addItem(T item) {
    insertItem(item, INSERT_AT_END);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public void layout() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        columnWidget.fillWidth();
      }
    });
    super.layout();
  }

  /**
   * Removes all items from the list box.
   */
  public void clear() {
    dataTable.clearAll();
    rowItems.clear();
  }

  /**
   * Gets the item at the specified index.
   * 
   * @param index the index of the item to be retrieved
   * @return the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public T getItem(int index) {
    checkIndex(index);
    return rowItems.get(dataTable.getRowFormatter().getElement(index));
  }

  /**
   * Gets the number of items present in the list box.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return dataTable.getRowCount();
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this
   * method will returns the first selected item ({@link #isItemeSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   */
  public int getSelectedIndex() {
    Set<Integer> selection = dataTable.getSelectedRows();
    for (Integer i : selection) {
      return i.intValue();
    }
    return -1;
  }

  /**
   * Inserts an item into the list box.
   * 
   * @param item the item to be inserted
   * @param index the index at which to insert it
   */
  public void insertItem(T item, int index) {
    if (dataTable.getColumnCount() == 0) {
      dataTable.resizeColumns(1);
    }
    if ((index == INSERT_AT_END) || (index == dataTable.getRowCount())) {
      // Insert the new row
      dataTable.insertRow(index = dataTable.getRowCount());
    } else {
      // Insert the new row
      dataTable.insertRow(index);
    }
    // Set the data in the new row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      cellRenderer.renderCell(dataTable, index, cellIndex, item);
    }
    // Map item with <tr>
    final Element tr = dataTable.getRowFormatter().getElement(index);
    rowItems.put(tr, item);
  }

  /**
   * Sets the item at a given index.
   * 
   * @param index the index of the item to be set
   * @param text the item's new value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItem(int index, T item) {
    checkIndex(index);
    if (item == null) {
      throw new NullPointerException("Cannot set an item to null");
    }

    // Set the data in the row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      cellRenderer.renderCell(dataTable, index, cellIndex, item);
    }

    // Map the new item with <tr>
    rowItems.put(dataTable.getRowFormatter().getElement(index), item);
  }

  /**
   * Determines whether as individual list is selected.
   * 
   * @param index the index of the item to be tested
   * @return {@code true} if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return dataTable.isRowSelected(index);
  }

  /**
   * Gets whether this list allows multiple selection.
   * 
   * @return {@code true} if multiple selection is allowed
   */
  public boolean isMultipleSelect() {
    return dataTable.getSelectionPolicy() == SelectionPolicy.MULTI_ROW;
  }

  /**
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    checkIndex(index);
    rowItems.remove(getItem(index));
    dataTable.removeRow(index);
  }

  /**
   * Sets whether an individual list item is selected.
   * <p>
   * Note that setting the selection programmatically does <em>not</em> cause
   * the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected {@code true} to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    dataTable.selectRow(index, false);
  }

  /**
   * Sets whether this list allows multiple selections.
   * 
   * @param multiple {@code true} to allow multiple selections
   */
  public void setMultipleSelect(boolean multiple) {
    dataTable.setSelectionPolicy(multiple ? SelectionPolicy.MULTI_ROW
        : SelectionPolicy.ONE_ROW);
  }

  /**
   * Sets the currently selected index.
   * <p>
   * After calling this method, only the specified item in the list will remain
   * selected. For a {@code ListBox} with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    checkIndex(index);
    dataTable.selectRow(index, true);
  }

  /**
   * Resizes the {@code ListBox} to the specified number of columns.
   * 
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
  public void setColumnsCount(int columns) {
    dataTable.resizeColumns(columns);
  }

  /**
   * Gets the number of columns in this grid.
   * 
   * @return the number of columns
   */
  public int getColumnsCount() {
    return dataTable.getColumnCount();
  }

  private ChangeListenerCollection changeListeners;

  public void addChangeListener(ChangeListener listener) {
    if (changeListeners == null) {
      changeListeners = new ChangeListenerCollection();
      dataTable.addTableSelectionListener(new TableSelectionListener() {
        public void onAllRowsDeselected(SourceTableSelectionEvents sender) {
          // Nothing to do here!
        }

        public void onCellHover(SourceTableSelectionEvents sender, int row,
            int cell) {
          // Nothing to do here!
        }

        public void onCellUnhover(SourceTableSelectionEvents sender, int row,
            int cell) {
          // Nothing to do here!
        }

        public void onRowDeselected(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowHover(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowUnhover(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowsSelected(SourceTableSelectionEvents sender,
            int firstRow, int numRows) {
          changeListeners.fireChange(ListBox.this);
        }
      });
    }
    changeListeners.add(listener);
  }

  public void removeChangeListener(ChangeListener listener) {
    if (changeListeners != null) {
      changeListeners.remove(listener);
    }
  }

}
