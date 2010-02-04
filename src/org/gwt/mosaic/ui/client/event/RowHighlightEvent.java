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
public class RowHighlightEvent extends GwtEvent<RowHighlightHandler> {

  /**
   * Handler type.
   */
  private static Type<RowHighlightHandler> TYPE;

  /**
   * Fires an cell highlight event on all registered handlers in the handler
   * manager.
   * 
   * @param source the source of the handlers
   * @param cell the value highlighted
   */
  public static void fire(HasRowHighlightHandlers source, Row cell) {
    if (getType() != null) {
      RowHighlightEvent event = new RowHighlightEvent(cell);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<RowHighlightHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<RowHighlightHandler>();
    }
    return TYPE;
  }
  
  private Row highlightedRow;
  
  /**
   * Construct a new {@link RowHighlightEvent}.
   * 
   * @param rowIndex the index of the highlighted row
   */
  public RowHighlightEvent(int rowIndex) {
    this(new Row(rowIndex));
  }

  protected RowHighlightEvent(Row cell) {
    this.highlightedRow = cell;
  }
  
  /**
   * @return the highlightedRow
   */
  public Row getHighlightedRow() {
    return highlightedRow;
  }

  @Override
  protected void dispatch(RowHighlightHandler handler) {
    handler.onRowHighlight(this);
  }

  @Override
  public Type<RowHighlightHandler> getAssociatedType() {
    return TYPE;
  }

}
