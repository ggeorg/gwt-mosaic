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

import org.mosaic.ui.client.scrolltable.overrides.HTMLTable;

/**
 * Cell editors provide a mechanism to edit cells.
 * 
 * @param <R> the type of the row value associated with the editor
 */
public interface AbstractCellEditor<R> {
  /**
   * Callback for {@link AbstractCellEditor}. The callback will be used when
   * the user finishes editing the cell.
   * 
   * @param <R> the type of the row value associated with the editor
   */
  public static interface Callback<R> {
    /**
     * Use this callback to return a value to the table.
     * 
     * @param cellEditInfo information about the source of the edit request
     * @param value the new value of the cell
     */
    void onComplete(CellEditInfo<R> cellEditInfo, Object value);

    /**
     * Use this callback to cancel the edit request.
     * 
     * @param cellEditInfo information about the source of the edit request
     */
    void onCancel(CellEditInfo<R> cellEditInfo);
  }

  /**
   * The information about the cell to edit.
   * 
   * @param <R> the type of the row value associated with the editor
   */
  public static class CellEditInfo<R> {
    /**
     * The cell index.
     */
    private int cell;

    /**
     * The row index.
     */
    private int row;

    /**
     * The object associated with the row.
     */
    private R rowValue;

    /**
     * The table that triggered the editor.
     */
    private HTMLTable table;

    /**
     * Constructor.
     * 
     * @param table the table that opened the editor
     * @param row the row index
     * @param cell the cell index
     * @param rowValue the object associated with the row
     */
    public CellEditInfo(HTMLTable table, int row, int cell, R rowValue) {
      this.table = table;
      this.row = row;
      this.cell = cell;
      this.rowValue = rowValue;
    }

    /**
     * @return the cell index
     */
    public int getCell() {
      return cell;
    }

    /**
     * @return the row index
     */
    public int getRow() {
      return row;
    }

    /**
     * @return the row value associated with the row
     */
    public R getRowValue() {
      return rowValue;
    }

    /**
     * @return the table that opened the editor
     */
    public HTMLTable getTable() {
      return table;
    }
  }

  /**
   * Handle a request from the table to edit a cell.
   * 
   * @param cellEditInfo information about the source of the edit request
   * @param callback callback used when editing is complete
   */
  void editCell(CellEditInfo<R> cellEditInfo, Callback<R> callback);
}
