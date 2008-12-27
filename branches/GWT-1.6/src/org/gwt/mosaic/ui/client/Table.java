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

import java.io.Serializable;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.table.AbstractTableModel;
import org.gwt.mosaic.ui.client.table.PagingScrollTable;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import org.gwt.mosaic.ui.client.table.TableColumnModel;
import org.gwt.mosaic.ui.client.table.TableColumnModelEvent;
import org.gwt.mosaic.ui.client.table.TableColumnModelListener;
import org.gwt.mosaic.ui.client.table.TableModel;
import org.gwt.mosaic.ui.client.table.TableModelEvent;
import org.gwt.mosaic.ui.client.table.TableModelListener;
import org.gwt.mosaic.ui.client.table.ScrollTable.DataGrid;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.widgetideas.table.client.AbstractCellEditor;
import com.google.gwt.widgetideas.table.client.CachedTableModel;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.widgetideas.table.client.TableBulkRenderer;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.TableModelHelper.Request;

/**
 * A {@link PaddingScrollTable} that acts as a view of the underlying
 * {@link TableModel}.
 * 
 * @param <T> the data type of the row values
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class Table<T> extends LayoutComposite implements HasFocus,
    TableModelListener, TableColumnModelListener {

  /**
   * The renderer used to set cell contents.
   */
  public static interface CellRenderer<T> {
    String renderCell(int row, int column, T item);
  };

  private CellRenderer<Serializable> cellRenderer = new CellRenderer<Serializable>() {
    public String renderCell(int row, int column, Serializable item) {
      return item.toString();
    }
  };

  private PagingScrollTable<T> table;

  private FocusListenerCollection focusListeners;

  private KeyboardListenerCollection keyboardListeners;

  private TableModel<T> dataModel;
  private TableColumnModel<T> columnModel;

  /**
   * 
   * @param tableModel
   * @param columnModel
   */
  public Table(final TableModel<T> tableModel,
      final TableColumnModel<T> columnModel) {
    super();

    // this.dataModel = tableModel;
    this.columnModel = columnModel;
    this.columnModel.addColumnModelListener(this);

    setModel(tableModel);

    FixedWidthFlexTable headerTable = table.getHeaderTable();

    for (int i = 0, n = columnModel.getColumnCount(); i < n; ++i) {
      headerTable.setHTML(0, i, columnModel.getColumn(i).getLabel());
      table.setColumnWidth(i, columnModel.getColumn(i).getWidth());
      table.setCellEditor(i,
          (AbstractCellEditor<T>) columnModel.getColumn(i).getCellEditor());
    }

    // table.setCellRenderer(new
    // org.gwt.mosaic.ui.client.table.PagingScrollTable.CellRenderer() {
    // @SuppressWarnings("unchecked")
    // public void renderCell(DataGrid grid, int row, int column, Object data) {
    // System.out.println("----------------");
    // if (data == null) {
    // grid.clearCell(row, column);
    // return;
    // }
    // grid.setHTML(row, column,
    // cellRenderer.renderCell(row, column, (T) data));
    // }
    // });
    // table.setPageSize(0);

    // Setup the bulk renderer
    FixedWidthGridBulkRenderer bulkRenderer = new FixedWidthGridBulkRenderer(
        table.getDataTable(), columnModel.getColumnCount());
    bulkRenderer.setCellRenderer(new TableBulkRenderer.CellRenderer() {
      @SuppressWarnings("unchecked")
      public void renderCell(int row, int column, Object cellData,
          StringBuffer accum) {
        if (cellData == null) {
          return;
        }
        accum.append(Table.this.cellRenderer.renderCell(row, column,
            (Serializable) cellData));
      }
    });
    table.setBulkRenderer(bulkRenderer);

    // Setup the scroll table
    table.setCellPadding(3);
    table.setCellSpacing(0);
    table.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    setMultipleSelect(false);
  }

  /**
   * @param data
   * @param columnModel
   */
  public Table(final Object[][] data, final TableColumnModel<T> columnModel) {
    this(new AbstractTableModel<T>() {
      private static final long serialVersionUID = 5132764391482749460L;

      public int getRowCount() {
        return data.length;
      }

      public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
      }

      public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
      }
    }, columnModel);
  }

  /**
   * Set the number of rows per page.
   * 
   * TODO By default, the page size is zero, which indicates that all rows
   * should be shown on the page.
   * 
   * @param pageSize the number of rows per page
   */
  public void setPageSize(int pageSize) {
    table.setPageSize(pageSize);
  }

  /**
   * Get the number of rows per page.
   * 
   * @return the number of rows per page
   */
  public void getPageSize() {
    table.getPageSize();
  }

  public void addFocusListener(FocusListener listener) {
    if (focusListeners == null) {
      focusListeners = new FocusListenerCollection();
    }
    focusListeners.add(listener);
  }

  public void addKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners == null) {
      keyboardListeners = new KeyboardListenerCollection();
    }
    keyboardListeners.add(listener);
  }

  public int getTabIndex() {
    return 0; // TODO table.getTabIndex(getElement());
  }

  public void removeFocusListener(FocusListener listener) {
    if (focusListeners != null) {
      focusListeners.remove(listener);
    }
  }

  public void removeKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners != null) {
      keyboardListeners.remove(listener);
    }
  }

  public void setAccessKey(char key) {
    // TODO table.setAccessKey(getElement(), key);
  }

  public void setFocus(boolean focused) {
    // TODO table.setFocus(focused);
  }

  /**
   * Sets whether this list allows multiple selections.
   * 
   * @param multiple {@code true} to allow multiple selections
   */
  public void setMultipleSelect(boolean multiple) {
    table.getDataTable().setSelectionPolicy(
        multiple ? SelectionPolicy.MULTI_ROW : SelectionPolicy.ONE_ROW);
  }

  public void setTabIndex(int index) {
    // TODO table.setTabIndex(getElement(), index);
  }

  /**
   * A penel that wraps a {@link Table} and icludes options to manipulate the
   * page.
   * 
   * <h3>CSS Style Rules</h3>
   * <ul class="css">
   * <li> .gwt-PagingOptions { applied to the entire widget } </li>
   * <li> .gwt-PagingOptions .errorMessage { applied to the error message }
   * </li>
   * </ul>
   */
  public static class PagingOptions extends
      org.gwt.mosaic.ui.client.table.PagingOptions {
    public PagingOptions(Table<?> table) {
      super(table.table);
    }
  }

  /**
   * Set the data model.
   * 
   * @param model the new data model
   * @exception IllegalArgumentException if {@code model} is {@code null}
   * @see #getModel
   */
  public void setModel(TableModel<T> model) {
    if (model == null) {
      throw new IllegalArgumentException();
    }
    TableModel<T> oldValue = dataModel;
    if (oldValue != null) {
      oldValue.removeTableModelListener(this);
    }
    dataModel = model;
    dataModel.addTableModelListener(this);
    if (oldValue != dataModel) {
      // Setup the controller
      CachedTableModel<T> cachedTableModel = new CachedTableModel<T>(
          new com.google.gwt.widgetideas.table.client.ClientTableModel<T>() {

            @Override
            public void requestRows(Request request, Callback<T> callback) {
              super.requestRows(request, callback);
            }

            @Override
            public Object getCell(int rowNum, int colNum) {
              // XXX Get the value for a given cell. Return null if no more
              // values are available!
              if (rowNum >= dataModel.getRowCount()
                  || colNum >= columnModel.getColumnCount()) {
                return null;
              }
              Object value = dataModel.getValueAt(rowNum, colNum);
              return value == null ? "" : value.toString();
            }

            @Override
            protected boolean onRowInserted(int beforeRow) {
              return true;
            }

            @Override
            protected boolean onRowRemoved(int row) {
              return true;
            }

            @Override
            protected boolean onSetData(int row, int cell, Object data) {
              dataModel.setValueAt(data, row, cell);
              return true;
            }
          });
      cachedTableModel.setRowCount(dataModel.getRowCount());
      cachedTableModel.setPreCachedRowCount(50);
      cachedTableModel.setPostCachedRowCount(50);

      final LayoutPanel layoutPanel = getWidget();
      table = new PagingScrollTable<T>(cachedTableModel, new DataGrid(),
          new FixedWidthFlexTable());
      layoutPanel.clear();
      layoutPanel.add(table);
    }
  }

  /**
   * Receives notification when the table model changes.
   * 
   * @see org.gwt.mosaic.ui.client.table.TableModelListener#tableChanged(org.gwt.mosaic.ui.client.table.TableModelEvent)
   */
  public void tableChanged(TableModelEvent event) {
    if (event == null || event.getFirstRow() == TableModelEvent.ALL_ROWS) {
      table.getDataTable().deselectAllRows();

      // TODO

      return;
    }

    if (event.getType() == TableModelEvent.Type.INSERT) {

    } else if (event.getType() == TableModelEvent.Type.DELETE) {

    } else {

    }
  }

  /**
   * Invoked when a column is added to the {@code TableColumnModel}.
   * 
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.TableColumnModelListener#columnAdded(org.gwt.mosaic.ui.client.table.TableColumnModelEvent)
   */
  public void columnAdded(TableColumnModelEvent event) {
    final FixedWidthFlexTable headerTable = table.getHeaderTable();
    for (int i = 0, n = columnModel.getColumnCount(); i < n; ++i) {
      headerTable.setHTML(0, i, columnModel.getColumn(i).getLabel());
      table.setColumnWidth(i, columnModel.getColumn(i).getWidth());
      table.setCellEditor(i,
          (AbstractCellEditor<T>) columnModel.getColumn(i).getCellEditor());
    }
    //table.
  }

  /**
   * Invoked when a column is removed from the {@code TableColumnModel}.
   * 
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.TableColumnModelListener#columnRemoved(org.gwt.mosaic.ui.client.table.TableColumnModelEvent)
   */
  public void columnRemoved(TableColumnModelEvent event) {
    final FixedWidthFlexTable headerTable = table.getHeaderTable();
    for (int i = 0, n = columnModel.getColumnCount(); i < n; ++i) {
      headerTable.setHTML(0, i, columnModel.getColumn(i).getLabel());
      table.setColumnWidth(i, columnModel.getColumn(i).getWidth());
      table.setCellEditor(i,
          (AbstractCellEditor<T>) columnModel.getColumn(i).getCellEditor());
    }
    table.reloadPage();
  }
}
