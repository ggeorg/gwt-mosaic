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
import org.gwt.mosaic.ui.client.table.PagingScrollTable;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import org.gwt.mosaic.ui.client.table.TableColumnModel;
import org.gwt.mosaic.ui.client.table.TableModel;
import org.gwt.mosaic.ui.client.table.ScrollTable.DataGrid;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.widgetideas.table.client.CachedTableModel;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.widgetideas.table.client.TableBulkRenderer;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
public class Table<T> extends LayoutComposite implements HasFocus {

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

  private final PagingScrollTable<T> table;

  private FocusListenerCollection focusListeners;

  private KeyboardListenerCollection keyboardListeners;

  private TableModel<T> tableModel;
  private TableColumnModel<T> columnModel;

  public Table(TableModel<T> tableModel, TableColumnModel<T> columnModel) {
    super();

    this.tableModel = tableModel;
    this.columnModel = columnModel;

    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

    // Setup the controller
    CachedTableModel<T> cachedTableModel = new CachedTableModel<T>(
        new com.google.gwt.widgetideas.table.client.ClientTableModel<T>() {

          @Override
          public void requestRows(Request request, Callback<T> callback) {
            super.requestRows(request, callback);
          }

          @Override
          public Object getCell(int rowNum, int colNum) {
            return Table.this.tableModel.getValueAt(rowNum, colNum);
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
            return true;
          }
        });
    cachedTableModel.setPreCachedRowCount(50);
    cachedTableModel.setPostCachedRowCount(50);
    cachedTableModel.setRowCount(1000);

    final LayoutPanel layoutPanel = getWidget();
    table = new PagingScrollTable<T>(cachedTableModel, new DataGrid(),
        headerTable);
    layoutPanel.add(table);

    for (int i = 0, n = columnModel.getColumnCount(); i < n; ++i) {
      headerTable.setHTML(0, i, columnModel.getColumn(i).getLabel());
      table.setColumnWidth(i, columnModel.getColumn(i).getWidth());
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
    table.setPageSize(100);

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

}
