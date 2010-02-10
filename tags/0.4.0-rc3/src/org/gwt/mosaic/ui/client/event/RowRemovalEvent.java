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
 */
public class RowRemovalEvent extends GwtEvent<RowRemovalHandler> {

  /**
   * Handler type.
   */
  private static Type<RowRemovalHandler> TYPE;
  
  /**
   * Fires a row removal event on all registered handlers in the handler manager.
   * 
   * @param <S> The event source
   * @param source the source of the handlers
   */
  public static <S extends RowRemovalHandler & HasHandlers> void fire(
      S source, int rowIndex) {
    if (TYPE != null) {
      RowRemovalEvent event = new RowRemovalEvent(rowIndex);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowRemovalHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowRemovalHandler>();
    }
    return TYPE;
  }
  
  /**
   * The index of the removed row.
   */
  private int rowIndex;

  /**
   * Construct a new {@link RowRemovalEvent}.
   * 
   * @param rowIndex the index of the removed row
   */
  public RowRemovalEvent(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  /**
   * @return the index of the removed row
   */
  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  protected void dispatch(RowRemovalHandler handler) {
    handler.onRowRemoval(this);
  }

  @Override
  public Type<RowRemovalHandler> getAssociatedType() {
    return TYPE;
  }
}
