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
public class CellUnhighlightEvent extends GwtEvent<CellUnhighlightHandler> {
  
  /**
   * Handler type.
   */
  private static Type<CellUnhighlightHandler> TYPE;
  
  /**
   * Fires an cell unhighlight event on all registered handlers in the handler manager.
   * 
   * @param source the source of the handlers
   * @param cell the value highlighted
   */
  public static void fire(
      HasCellUnhighlightHandlers source, Cell cell) {
    if (getType() != null) {
      CellUnhighlightEvent event = new CellUnhighlightEvent(cell);
      source.fireEvent(event);
    }
  }
  
  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CellUnhighlightHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<CellUnhighlightHandler>();
    }
    return TYPE;
  }
  
  private Cell unhighlightedCell;
  
  /**
   * Construct a new {@link CellUnhighlightEvent}.
   * 
   * @param rowIndex the index of the highlighted row
   * @param cellIndex the index of the highlighted cell
   */
  public CellUnhighlightEvent(int rowIndex, int cellIndex) {
    this(new Cell(rowIndex, cellIndex));
  }

  protected CellUnhighlightEvent(Cell cell) {
    unhighlightedCell = cell;
  }
  
  /**
   * @return the unhighlightedCell
   */
  public Cell getUnhighlightedCell() {
    return unhighlightedCell;
  }

  @Override
  protected void dispatch(CellUnhighlightHandler handler) {
    handler.onCellUnhighlight(this);
  }

  @Override
  public Type<CellUnhighlightHandler> getAssociatedType() {
    return TYPE;
  }

}
