/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import org.gwt.mosaic.ui.client.event.HasRowCountChangeHandlers;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Response;

/**
 * An interface to retrieve row data to be used in a table.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public interface TableModel<RowType> extends HasRowCountChangeHandlers {

  /**
   * Callback for {@link TableModel}. Every {@link Request} should be associated
   * with a {@link TableModel.Callback} that should be called after a
   * {@link Response} is generated.
   * 
   * @param <RowType> the data type of the row values
   */
  public static interface Callback<RowType> {
    /**
     * Called when an error occurs and the rows cannot be loaded.
     * 
     * @param caught the exception that was thrown
     */
    void onFailure(Throwable caught);

    /**
     * Consume the data created by {@link TableModel} in response to a Request.
     * 
     * @param request the request
     * @param response the response
     */
    void onRowsReady(Request request, Response<RowType> response);
  }

  /**
   * Use the {@code ALL_ROWS} value in place of the {@code numRows} variable
   * when requesting all rows.
   */
  int ALL_ROWS = -1;

  /**
   * Indicates that the number of rows is unknown, and therefore unbounded.
   */
  int UNKNOWN_ROW_COUNT = -1;

  /**
   * Return the total number of rows. If the number is not known, return
   * {@link #UNKNOWN_ROW_COUNT}.
   * 
   * @return the total number of rows, or {@link #UNKNOWN_ROW_COUNT}.
   */
  int getRowCount();
  
  /**
   * Set the total number of rows.
   * 
   * @param rowCount the row count
   */
  void setRowCount(int rowCount);

  /**
   * Generate a {@link Response} based on a specific {@link Request}. The
   * response is passed into the {@link Callback}.
   * 
   * @param request the {@link Request} for row data
   * @param callback the {@link Callback} to use for the {@link Response}
   */
  void requestRows(Request request, Callback<RowType> callback);
}
