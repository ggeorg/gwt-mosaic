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

import org.gwt.mosaic.ui.client.event.RowInsertionEvent;
import org.gwt.mosaic.ui.client.event.RowInsertionHandler;
import org.gwt.mosaic.ui.client.event.RowRemovalEvent;
import org.gwt.mosaic.ui.client.event.RowRemovalHandler;
import org.gwt.mosaic.ui.client.event.RowValueChangeEvent;
import org.gwt.mosaic.ui.client.event.RowValueChangeHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An abstract {@link MutableTableModel} implementation.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public abstract class AbstractMutableTableModel<RowType> extends
    AbstractTableModel<RowType> implements MutableTableModel<RowType> {

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.event.HasRowInsertionHandlers#addRowInsertionHandler(org.gwt.mosaic.ui.client.event.RowInsertionHandler)
   */
  public HandlerRegistration addRowInsertionHandler(RowInsertionHandler handler) {
    return addHandler(handler, RowInsertionEvent.getType());
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.event.HasRowRemovalHandlers#addRowRemovalHandler(org.gwt.mosaic.ui.client.event.RowRemovalHandler)
   */
  public HandlerRegistration addRowRemovalHandler(RowRemovalHandler handler) {
    return addHandler(handler, RowRemovalEvent.getType());
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.event.HasRowValueChangeHandlers#addRowValueChangeHandler(org.gwt.mosaic.ui.client.event.RowValueChangeHandler)
   */
  public HandlerRegistration addRowValueChangeHandler(
      RowValueChangeHandler<RowType> handler) {
    return addHandler(handler, RowValueChangeEvent.getType());
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#insertRow(int)
   */
  public void insertRow(int beforeRow) {
    if (onRowInserted(beforeRow)) {
      // Fire listeners
      fireEvent(new RowInsertionEvent(beforeRow));

      // Increment the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT) {
        setRowCount(numRows + 1);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#removeRow(int)
   */
  public void removeRow(int row) {
    if (onRowRemoved(row)) {
      // Fire listeners
      fireEvent(new RowRemovalEvent(row));

      // Decrement the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT) {
        setRowCount(numRows - 1);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.MutableTableModel#setRowValue(int,
   *      java.lang.Object)
   */
  public void setRowValue(int row, RowType rowValue) {
    if (onSetRowValue(row, rowValue)) {
      // Fire the listeners
      fireEvent(new RowValueChangeEvent<RowType>(row, rowValue));

      // Update the row count
      int numRows = getRowCount();
      if (numRows != UNKNOWN_ROW_COUNT && row >= numRows) {
        setRowCount(row + 1);
      }
    }
  }
}
