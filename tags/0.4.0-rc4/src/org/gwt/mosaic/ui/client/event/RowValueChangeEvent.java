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
package org.gwt.mosaic.ui.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Logical event fired when a row is inserted.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public class RowValueChangeEvent<RowType> extends
    GwtEvent<RowValueChangeHandler<RowType>> {

  /**
   * Handler type.
   */
  private static Type<RowValueChangeHandler<?>> TYPE;

  /**
   * Fires a page change event on all registered handlers in the handler
   * manager.
   * 
   * @param <S> The event source
   * @param source the source of the handlers
   */
  public static <RowType, S extends RowValueChangeHandler<RowType> & HasHandlers> void fire(
      S source, int rowIndex, RowType rowValue) {
    if (TYPE != null) {
      RowValueChangeEvent<RowType> event = new RowValueChangeEvent<RowType>(
          rowIndex, rowValue);
      source.fireEvent(event);
    }
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowValueChangeHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowValueChangeHandler<?>>();
    }
    return TYPE;
  }

  /**
   * The new row value.
   */
  private RowType rowValue;

  /**
   * The index of the row.
   */
  private int rowIndex;

  /**
   * Construct a new {@link RowValueChangeEvent}.
   * 
   * @param rowIndex the index of the removed row
   * @param rowValue the new row value
   */
  public RowValueChangeEvent(int rowIndex, RowType rowValue) {
    this.rowIndex = rowIndex;
    this.rowValue = rowValue;
  }

  /**
   * @return the index of the row
   */
  public int getRowIndex() {
    return rowIndex;
  }

  /**
   * @return the row value
   */
  public RowType getRowValue() {
    return rowValue;
  }

  @Override
  protected void dispatch(RowValueChangeHandler<RowType> handler) {
    handler.onRowValueChange(this);
  }

  // Because of type erasure, our static type is
  // wild carded, yet the "real" type should use our I param.
  @SuppressWarnings("unchecked")
  @Override
  public Type<RowValueChangeHandler<RowType>> getAssociatedType() {
    return (Type) TYPE;
  }
}
