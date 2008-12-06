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
package org.gwt.mosaic.ui.client.table;

import java.util.EventObject;

/**
 * {@code TableModelEvent} is used to notify listeners that a table model has
 * changed.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see TableModel
 */
public class TableModelEvent extends EventObject {

  /** The event type. */
  enum Type {
    /** Identifies the addition of new rows or columns. */
    INSERT,
    /** Identifies a change to existing data. */
    UPDATE,
    /** Identifies the removal of rows. */
    DELETE
  }

  private static final long serialVersionUID = 1349259405508032072L;

  /** Specifies all rows in the table. */
  public static final int ALL_ROWS = -1;

  /** Specifies all columns in a row or rows. */
  public static final int ALL_COLUMNS = -1;

  protected int firstRow;
  protected int lastRow;
  protected int column;
  protected Type type;

  /**
   * All row data changed, listeners should discard any state that was based on
   * the rows and requery the {@link TableModel}.
   * <p>
   * Note: The structure of the {@link TableModel} ie, the column names, types
   * and order have not changed.
   * 
   * @param source
   */
  public TableModelEvent(TableModel source) {
    this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, Type.UPDATE);
  }

  /**
   * This {@code row} of data has been updated.
   * <p>
   * Note: To denote the arrival of a completely new table structure use
   * {@code ALL_ROWS} as the {@code row} value.
   * 
   * @param source
   * @param row
   */
  public TableModelEvent(TableModel source, int row) {
    this(source, row, row, ALL_COLUMNS, Type.UPDATE);
  }

  /**
   * The data in rows [{@code firstRow}, {@code lastRow}] have been updated.
   * 
   * @param source
   * @param firstRow
   * @param lastRow
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow) {
    this(source, firstRow, lastRow, ALL_COLUMNS, Type.UPDATE);
  }

  /**
   * The cells in column {@code column} in the range [{@code firstRow},
   * {@code lastRow}] have been updated.
   * 
   * @param source
   * @param firstRow
   * @param lastRow
   * @param column
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow,
      int column) {
    this(source, firstRow, lastRow, column, Type.UPDATE);
  }

  /**
   * The cells from (firstRow, column) to (lastRow, column) have been changed.
   * 
   * @param source
   * @param firstRow
   * @param lastRow
   * @param column
   * @param type
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow,
      int column, Type type) {
    super(source);
    this.firstRow = firstRow;
    this.lastRow = lastRow;
    this.column = column;
    this.type = type;
  }

  /**
   * Returns the first row that changed. {@code ALL_ROWS} means table structure
   * change.
   * 
   * @return
   */
  public int getFirstRow() {
    return firstRow;
  }

  /**
   * Returns the last row that changed.
   * 
   * @return
   */
  public int getLastRow() {
    return lastRow;
  }

  /**
   * Returns the column data column. {@code ALL_COLUMNS} means every column in
   * the specified rows changed.
   * 
   * @return
   */
  public int getColumn() {
    return column;
  }

  /**
   * Returns the {@link Type} of the event.
   * 
   * @return
   */
  public Type getType() {
    return type;
  }

}
