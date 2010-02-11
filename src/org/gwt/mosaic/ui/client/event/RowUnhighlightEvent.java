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

import org.gwt.mosaic.ui.client.event.TableEvent.Row;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class RowUnhighlightEvent extends GwtEvent<RowUnhighlightHandler> {

  /**
   * Handler type.
   */
  private static Type<RowUnhighlightHandler> TYPE;

  /**
   * Fires an cell highlight event on all registered handlers in the handler
   * manager.
   * 
   * @param source the source of the handlers
   * @param cell the value highlighted
   */
  public static void fire(HasRowUnhighlightHandlers source, Row cell) {
    if (getType() != null) {
      RowUnhighlightEvent event = new RowUnhighlightEvent(cell);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowUnhighlightHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowUnhighlightHandler>();
    }
    return TYPE;
  }
  
  private Row unhighlightedRow;
  
  /**
   * Construct a new {@link RowUnhighlightEvent}.
   * 
   * @param rowIndex the index of the highlighted row
   */
  public RowUnhighlightEvent(int rowIndex) {
    this(new Row(rowIndex));
  }

  protected RowUnhighlightEvent(Row cell) {
    this.unhighlightedRow = cell;
  }
  
  /**
   * @return the unhighlightedRow
   */
  public Row getUnhighlightedRow() {
    return unhighlightedRow;
  }

  @Override
  protected void dispatch(RowUnhighlightHandler handler) {
    handler.onRowUnhighlight(this);
  }

  @Override
  public Type<RowUnhighlightHandler> getAssociatedType() {
    return TYPE;
  }

}
