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

import java.util.ArrayList;

/**
 * A helper class for implementers of the {@link SourceTableSelectionEvents}
 * interface. This subclass of {@link ArrayList} assumes that all objects added
 * to it will be of type {@link TableSelectionListener}.
 */
public class TableSelectionListenerCollection extends
    ArrayList<TableSelectionListener> {
  private static final long serialVersionUID = 4417155253362518990L;

  /**
   * Fired when all rows are deselected.
   * 
   * @param sender the source of the event
   */
  public void fireAllRowsDeselected(SourceTableSelectionEvents sender) {
    for (TableSelectionListener listener : this) {
      listener.onAllRowsDeselected(sender);
    }
  }

  /**
   * Fired when a cell is hovered.
   * 
   * @param sender the source of the event
   * @param row the row index
   * @param cell the cell index
   */
  public void fireCellHover(SourceTableSelectionEvents sender, int row, int cell) {
    for (TableSelectionListener listener : this) {
      listener.onCellHover(sender, row, cell);
    }
  }

  /**
   * Fired when a cell is unhovered.
   * 
   * @param sender the source of the event
   * @param row the row index
   * @param cell the cell index
   */
  public void fireCellUnhover(SourceTableSelectionEvents sender, int row,
      int cell) {
    for (TableSelectionListener listener : this) {
      listener.onCellUnhover(sender, row, cell);
    }
  }

  /**
   * Fired when a single row is deselected. This method will not fire when all
   * rows are deselected. In that case, use the onAllRowsDeselected events.
   * 
   * @param sender the source of the event
   * @param row the row index
   */
  public void fireRowDeselected(SourceTableSelectionEvents sender, int row) {
    for (TableSelectionListener listener : this) {
      listener.onRowDeselected(sender, row);
    }
  }

  /**
   * Fired when a row is hovered.
   * 
   * @param sender the source of the event
   * @param row the row index
   */
  public void fireRowHover(SourceTableSelectionEvents sender, int row) {
    for (TableSelectionListener listener : this) {
      listener.onRowHover(sender, row);
    }
  }

  /**
   * Fired when one or more rows are selected.
   * 
   * @param sender the source of the event
   * @param firstRow the row index of the first row
   * @param numRows the number of selected rows
   */
  public void fireRowsSelected(SourceTableSelectionEvents sender, int firstRow,
      int numRows) {
    for (TableSelectionListener listener : this) {
      listener.onRowsSelected(sender, firstRow, numRows);
    }
  }

  /**
   * Fired when a row is unhovered.
   * 
   * @param sender the source of the event
   * @param row the row index
   */
  public void fireRowUnhover(SourceTableSelectionEvents sender, int row) {
    for (TableSelectionListener listener : this) {
      listener.onRowUnhover(sender, row);
    }
  }
}
