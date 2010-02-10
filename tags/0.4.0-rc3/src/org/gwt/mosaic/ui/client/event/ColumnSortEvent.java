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

import org.gwt.mosaic.ui.client.table.TableModelHelper.ColumnSortList;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Logical event fired when a column is sorted.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ColumnSortEvent extends GwtEvent<ColumnSortHandler> {

  /**
   * Handler type.
   */
  private static Type<ColumnSortHandler> TYPE;

  /**
   * Fires an column sort event on all registered handlers in the handler
   * manager.
   * 
   * @param source the source of the handlers
   * @param unhighlighted the value highlighted
   */
  public static <S extends HasColumnSortHandlers & HasHandlers> void fire(
      S source, ColumnSortList sortList) {
    if (TYPE != null) {
      ColumnSortEvent event = new ColumnSortEvent(sortList);
      source.fireEvent(event);
    }
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ColumnSortHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<ColumnSortHandler>();
    }
    return TYPE;
  }

  /**
   * Information about the column sorting.
   */
  private ColumnSortList sortList;

  /**
   * Construct a new {@code ColumnSortEvent}.
   * 
   * @param sortList information about the sort order
   */
  public ColumnSortEvent(ColumnSortList sortList) {
    this.sortList = sortList;
  }

  /**
   * @return information about the sort order of columns
   */
  public ColumnSortList getColumnSortList() {
    return sortList;
  }

  @Override
  protected void dispatch(ColumnSortHandler handler) {
    handler.onColumnSorted(this);
  }

  @Override
  public Type<ColumnSortHandler> getAssociatedType() {
    return TYPE;
  }
}
