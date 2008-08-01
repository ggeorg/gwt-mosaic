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
 * A read-only version of the {@link TableModel}. If any modification methods
 * are called, an exception is thrown.
 * 
 * @param <R> the data type of the row values
 */
public abstract class ReadOnlyTableModel<R> extends TableModel<R> {
  /**
   * Error message used when calling a write operation.
   */
  public static final String READ_ONLY_ERROR = "TableModel is read only.";

  /**
   * Throw an exception if a read-only method is called.
   */
  static void throwReadOnlyException() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(READ_ONLY_ERROR);
  }

  @Override
  protected boolean onRowInserted(int beforeRow) {
    throwReadOnlyException();
    return false;
  }

  @Override
  protected boolean onRowRemoved(int row) {
    throwReadOnlyException();
    return false;
  }

  @Override
  protected boolean onSetData(int row, int cell, Object data) {
    throwReadOnlyException();
    return false;
  }
}
