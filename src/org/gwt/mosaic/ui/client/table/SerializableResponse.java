package org.gwt.mosaic.ui.client.table;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A repsonse from the {@link TableModelHelper} that is serializable, and can by
 * used over RPC.
 * 
 * @param <RowType> the serializable data type of the row values
 */
public class SerializableResponse<RowType extends IsSerializable>
    extends
    com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse<RowType> {

  private int rowCount;

  /**
   * Default constructor used for RPC.
   */
  public SerializableResponse() {
    super();
  }

  /**
   * Create a new {@link SerializableResponse}.
   */
  public SerializableResponse(Collection<RowType> rowValues) {
    super(rowValues);
    if (rowValues != null) {
      this.rowCount = rowValues.size();
    } else {
      this.rowCount = 0;
    }
  }

  /**
   * Create a new {@link SerializableResponse}.
   */
  public SerializableResponse(Collection<RowType> rowValues, int rowCount) {
    super(rowValues);
    this.rowCount = rowCount;
  }

  /**
   * @return the rowCount
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * @param rowCount the rowCount to set
   */
  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }
}
