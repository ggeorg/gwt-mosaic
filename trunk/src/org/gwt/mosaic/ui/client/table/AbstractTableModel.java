/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that for {@link TableModel} implementations.
 * 
 * @param <T>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class AbstractTableModel<T> implements TableModel<T>,
    Serializable {
  private static final long serialVersionUID = 4235627486811656219L;

  /** List of {@link TableModelListener TableModelListeners}. */
  protected transient List<TableModelListener> listenerList = new ArrayList<TableModelListener>();

  /**
   * Adds a listener to the list that's notified each time a change to the data
   * model occurs.
   * 
   * @param listener
   * @see com.google.gwt.widgetideas.table.client.TableModel#addTableModelListener(com.google.gwt.widgetideas.table.client.TableModelListener)
   */
  public void addTableModelListener(TableModelListener listener) {
    listenerList.add(listener);
  }

  /**
   * Removes a listener from the list.
   * 
   * @param listener the {@link TableModelListener}
   */
  public void removeTableModelListener(TableModelListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Returns an array of all the table model listeners registered on this model.
   * 
   * @return all of this model's {@code TableModelListeners} or an empty array
   *         if no table model listeners are currently registered
   * 
   * @see #addTableModelListener
   * @see #removeTableModelListener
   */
  public TableModelListener[] getTableModelListeners() {
    return (TableModelListener[]) listenerList.toArray(new TableModelListener[listenerList.size()]);
  }

  /**
   * Notifies all listeners that all cell values in the table's rows may have
   * changed. The number of rows may also have changed and the {@code Table}
   * should redraw the table from scratch. The structure of the table (as in the
   * order of the columns) is assumed to be the same.
   * 
   * @see TableModelEvent
   * @see org.gwt.mosaic.ui.client.Table#tableChanged(TableModelEvent)
   */
  public void fireTableDataChanged() {
    fireTableChanged(new TableModelEvent(this));
  }

  /**
   * Notifies all listeners that the table's structure has changed. The number
   * of columns in the table, and the names and types of the new columns may be
   * different from the previous state. If the <code>JTable</code> receives this
   * event and its <code>autoCreateColumnsFromModel</code> flag is set it
   * discards any table columns that it had and reallocates default columns in
   * the order they appear in the model. This is the same as calling
   * <code>setModel(TableModel)</code> on the <code>JTable</code>.
   * 
   * @see TableModelEvent
   */
  public void fireTableStructureChanged() {
    fireTableChanged(new TableModelEvent(this, 0 /* TableModelEvent.HEADER_ROW, */));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been inserted.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   * 
   */
  public void fireTableRowsInserted(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.INSERT));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been updated.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   */
  public void fireTableRowsUpdated(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.UPDATE));
  }

  /**
   * Notifies all listeners that rows in the range
   * <code>[firstRow, lastRow]</code>, inclusive, have been deleted.
   * 
   * @param firstRow the first row
   * @param lastRow the last row
   * 
   * @see TableModelEvent
   */
  public void fireTableRowsDeleted(int firstRow, int lastRow) {
    fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
        TableModelEvent.ALL_COLUMNS, TableModelEvent.Type.DELETE));
  }

  /**
   * Notifies all listeners that the value of the cell at
   * <code>[row, column]</code> has been updated.
   * 
   * @param row row of cell which has been updated
   * @param column column of cell which has been updated
   * @see TableModelEvent
   */
  public void fireTableCellUpdated(int row, int column) {
    fireTableChanged(new TableModelEvent(this, row, row, column));
  }

  /**
   * Forwards the given notification event to all
   * <code>TableModelListeners</code> that registered themselves as listeners
   * for this table model.
   * 
   * @param event the event to be forwarded
   * 
   * @see #addTableModelListener
   * @see TableModelEvent
   */
  public void fireTableChanged(TableModelEvent event) {
    for (TableModelListener listener : listenerList) {
      listener.tableChanged(event);
    }
  }

}
