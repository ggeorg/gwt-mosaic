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

/**
 * Event listener for {@link TableModel} class.
 */
public interface TableModelListener {
  /**
   * Event fired when the number of rows changes.
   * 
   * @param rowCount the row count
   */
  void onRowCountChanged(int rowCount);

  /**
   * Event fired when a row is inserted.
   * 
   * @param beforeRow the row index of the new row
   */
  void onRowInserted(int beforeRow);

  /**
   * Event fired when a row is removed.
   * 
   * @param row the row index of the removed row
   */
  void onRowRemoved(int row);

  /**
   * Event fired when the data changes.
   * 
   * @param row the row index
   * @param cell the cell index
   * @param data the new contents of the cell
   */
  void onSetData(int row, int cell, Object data);
}
