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

import java.util.ArrayList;

/**
 * A helper class for implementers of {@link SourceTableModelEvents}.
 */
public class TableModelListenerCollection extends ArrayList<TableModelListener> {
  private static final long serialVersionUID = -7057694055746622269L;

  /**
   * Fire row changed event.
   * 
   * @param rowCount the row count
   */
  public void fireRowCountChanged(int rowCount) {
    for (TableModelListener listener : this) {
      listener.onRowCountChanged(rowCount);
    }
  }

  /**
   * Fire row inserted event.
   * 
   * @param beforeRow the row index of the new row
   */
  public void fireRowInserted(int beforeRow) {
    for (TableModelListener listener : this) {
      listener.onRowInserted(beforeRow);
    }
  }

  /**
   * Fire row removed event.
   * 
   * @param row the row index of the removed row
   */
  public void fireRowRemoved(int row) {
    for (TableModelListener listener : this) {
      listener.onRowRemoved(row);
    }
  }

  /**
   * Fire data set event.
   * 
   * @param row the row index
   * @param cell the cell index
   * @param data the new contents of the cell
   */
  public void fireSetData(int row, int cell, Object data) {
    for (TableModelListener listener : this) {
      listener.onSetData(row, cell, data);
    }
  }
}
