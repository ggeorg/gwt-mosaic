package org.mosaic.ui.client.event;

import org.mosaic.core.client.model.ModelChangeEvent;
import org.mosaic.ui.client.table.TableModel;

public class TableModelEvent extends ModelChangeEvent {
  private static final long serialVersionUID = 7927105935104758860L;

  /** Identifies the header row. */
  public static final int HEADER_ROW = -1;

  /** Specifies all columns in a row or rows. */
  public static final int ALL_COLUMNS = -1;

  public enum TableModelEventType {
    INSERT, UPDATE, DELETE
  };

  protected TableModelEventType type;

  protected int column;

  /**
   * All row data in the table has changed, listeners should discard any state
   * that was based on the rows and requery the <code>TableModel</code> to get
   * the new row count and all the appropriate values.
   * <p>
   * The <code>Table</code> with repaint the entire visible region on
   * receiving this event, querying the model for the cell values that are
   * visible. The structure of the table ie, the column names, types and order
   * have not changed.
   * 
   * @param source
   */
  public TableModelEvent(TableModel source) {
    this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, TableModelEventType.UPDATE);
  }

  /**
   * This row of data has been updated.
   * 
   * @param source
   * @param row
   */
  public TableModelEvent(TableModel source, int row) {
    this(source, row, row, ALL_COLUMNS, TableModelEventType.UPDATE);
  }

  /**
   * The date in rows [<i>firstRow<.i>, <i>lastRow</i>] have been updated.
   * 
   * @param source
   * @param fistRow
   * @param lastRow
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow) {
    this(source, firstRow, lastRow, ALL_COLUMNS, TableModelEventType.UPDATE);
  }

  /**
   * The cells in column <i>column</i> in the range [<i>firstRow<.i>,
   * <i>lastRow</i>] have been updated.
   * 
   * @param source
   * @param firstRow
   * @param lastRow
   * @param column
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow, int column) {
    this(source, firstRow, lastRow, column, TableModelEventType.UPDATE);
  }

  /**
   * The cells from (firstRow, column) to (lastRow, column) have been changed.
   * The <I>column</I> refers to the column index of the cell in the model's
   * co-ordinate system. When <I>column</I> is ALL_COLUMNS, all cells in the
   * specified range of rows are considered changed.
   * 
   * @param source
   * @param firstRow
   * @param lastRow
   * @param column
   * @param type
   */
  public TableModelEvent(TableModel source, int firstRow, int lastRow, int column,
      TableModelEventType type) {
    super(source, firstRow, lastRow);
    this.column = column;
    this.type = type;
  }

  /**
   * Returns the column for the event. If the return value is ALL_COLUMNS; it
   * means every column in the specified rows changed.
   * 
   * @return the column column or ALL_COLUMNS
   */
  public int getColumn() {
    return column;
  }

  /**
   * Returns the type of event - one of: INSERT, UPDATE and DELETE.
   * 
   * @return the type of event
   */
  public TableModelEventType getType() {
    return type;
  }

}
