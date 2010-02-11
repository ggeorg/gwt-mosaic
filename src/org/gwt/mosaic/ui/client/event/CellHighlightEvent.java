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

import org.gwt.mosaic.ui.client.event.TableEvent.Cell;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class CellHighlightEvent extends GwtEvent<CellHighlightHandler> {

  /**
   * Handler type.
   */
  private static Type<CellHighlightHandler> TYPE;

  /**
   * Fires an cell highlight event on all registered handlers in the handler
   * manager.
   * 
   * @param source the source of the handlers
   * @param cell the value highlighted
   */
  public static void fire(HasCellHighlightHandlers source, Cell cell) {
    if (getType() != null) {
      CellHighlightEvent event = new CellHighlightEvent(cell);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CellHighlightHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<CellHighlightHandler>();
    }
    return TYPE;
  }
  
  private Cell highlightedCell;
  
  /**
   * Construct a new {@link CellHighlightEvent}.
   * 
   * @param rowIndex the index of the highlighted row
   * @param cellIndex the index of the highlighted cell
   */
  public CellHighlightEvent(int rowIndex, int cellIndex) {
    this(new Cell(rowIndex, cellIndex));
  }

  protected CellHighlightEvent(Cell cell) {
    this.highlightedCell = cell;
  }
  
  /**
   * @return the highlightedCell
   */
  public Cell getHighlightedCell() {
    return highlightedCell;
  }

  @Override
  protected void dispatch(CellHighlightHandler handler) {
    handler.onCellHighlight(this);
  }

  @Override
  public Type<CellHighlightHandler> getAssociatedType() {
    return TYPE;
  }

}
