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

import org.gwt.mosaic.ui.client.event.HasRowInsertionHandlers;
import org.gwt.mosaic.ui.client.event.HasRowRemovalHandlers;
import org.gwt.mosaic.ui.client.event.HasRowValueChangeHandlers;

/**
 * A mutable version of the {@link TableModel} that supports inserting and
 * removind rows and setting cell data.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public interface MutableTableModel<RowType> extends TableModel<RowType>,
    HasRowInsertionHandlers, HasRowRemovalHandlers,
    HasRowValueChangeHandlers<RowType> {

  /**
   * Insert a row and increment the row count by one.
   * 
   * @param beforeRow the row index of the new row TODO (jlabanca): should this
   *          require a row value?
   */
  void insertRow(int beforeRow);

  /**
   * Remove a row and decrement the row count by one.
   * 
   * @param row the row index of the removed row
   */
  void removeRow(int row);

  /**
   * Set a new row value.
   * 
   * @param row the row index
   * @param rowValue the new row value at this row
   */
  void setRowValue(int row, RowType rowValue);

  /**
   * Event fired when a row is inserted. Returning {@code true} will increment
   * the row count by one.
   * 
   * @param beforeRow the row index of the new row
   * @return true if the action is successful
   */
  boolean onRowInserted(int beforeRow);

  /**
   * Event fired when a row is removed. Returning {@code true} will decrement
   * the row count by one.
   * 
   * @param row the row index of the removed row
   * @return true if the action is successful
   */
  boolean onRowRemoved(int row);

  /**
   * Event fired when the local data changes. Returning {@code true} will ensure
   * that the row count is at least as one greater than the row index.
   * 
   * @param row the row index
   * @param rowValue the new row value at this row
   * @return true if the action is successful
   */
  boolean onSetRowValue(int row, RowType rowValue);
}
