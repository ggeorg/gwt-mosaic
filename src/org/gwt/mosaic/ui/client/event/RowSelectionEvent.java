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

import java.util.Set;
import java.util.TreeSet;

import org.gwt.mosaic.ui.client.event.TableEvent.Row;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Logical event fired when a cell is highlighted.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class RowSelectionEvent extends GwtEvent<RowSelectionHandler> {
  
  /**
   * Handler type.
   */
  private static Type<RowSelectionHandler> TYPE;

  /**
   * Fires an unhighlight event on all registered handlers in the handler manager.
   * 
   * @param <V> the unhighlighted value type
   * @param <S> The event source
   * @param source the source of the handlers
   * @param unhighlighted the value highlighted
   */
  public static <S extends HasRowSelectionHandlers & HasHandlers> void fire(
      S source, Set<Row> oldList, Set<Row> newList) {
    if (TYPE != null) {
      RowSelectionEvent event = new RowSelectionEvent(oldList, newList);
      source.fireEvent(event);
    }
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowSelectionHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowSelectionHandler>();
    }
    return TYPE;
  }
  
  private Set<Row> oldList, newList;

  /**
   * Construct a new {@link RowSelectionEvent}.
   * 
   * @param oldList the set of rows that were previously selected
   * @param newList the set of rows that are now selected
   */
  public RowSelectionEvent(Set<Row> oldList, Set<Row> newList) {
    this.oldList = oldList;
    this.newList = newList;
  }

  /**
   * @return the newly deselected rows
   */
  public Set<Row> getDeselectedRows() {
    Set<Row> deselected = new TreeSet<Row>();
    for (Row row : oldList) {
      if (!newList.contains(row)) {
        deselected.add(row);
      }
    }
    return deselected;
  }

  /**
   * @return the newly selected rows
   */
  public Set<Row> getSelectedRows() {
    Set<Row> selected = new TreeSet<Row>();
    for (Row row : newList) {
      if (!oldList.contains(row)) {
        selected.add(row);
      }
    }
    return selected;
  }

  @Override
  protected void dispatch(RowSelectionHandler handler) {
    handler.onRowSelection(this);
  }

  @Override
  public Type<RowSelectionHandler> getAssociatedType() {
    return TYPE;
  }
}
