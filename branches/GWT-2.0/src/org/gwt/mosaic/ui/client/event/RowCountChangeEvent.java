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
 * Logical event fired when the number of rows changes.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class RowCountChangeEvent extends GwtEvent<RowCountChangeHandler> {

  /**
   * Handler type.
   */
  private static Type<RowCountChangeHandler> TYPE;
  
  /**
   * Fires a page change event on all registered handlers in the handler manager.
   * 
   * @param <S> The event source
   * @param source the source of the handlers
   */
  public static <S extends RowCountChangeHandler & HasHandlers> void fire(
      S source, int oldRowCount, int newRowCount) {
    if (TYPE != null) {
      RowCountChangeEvent event = new RowCountChangeEvent(oldRowCount, newRowCount);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowCountChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowCountChangeHandler>();
    }
    return TYPE;
  }
  
  /**
   * The new row count.
   */
  private int newRowCount;

  /**
   * The previous row count.
   */
  private int oldRowCount;

  /**
   * Construct a new {@link RowCountChangeEvent}.
   * 
   * @param oldRowCount the previous page
   * @param newRowCount the page that was requested
   */
  public RowCountChangeEvent(int oldRowCount, int newRowCount) {
    this.oldRowCount = oldRowCount;
    this.newRowCount = newRowCount;
  }

  /**
   * @return the new row count
   */
  public int getNewRowCount() {
    return newRowCount;
  }

  /**
   * @return the old row count
   */
  public int getOldRowCount() {
    return oldRowCount;
  }
  
  @Override
  protected void dispatch(RowCountChangeHandler handler) {
    handler.onRowCountChange(this);
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<RowCountChangeHandler> getAssociatedType() {
    return TYPE;
  }

}
