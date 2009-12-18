/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A response from the {@link TableModelHelper} that is serializable, and can by
 * used over RPC.
 * 
 * @author georgopoulos.georgios(at)gmail.com
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
