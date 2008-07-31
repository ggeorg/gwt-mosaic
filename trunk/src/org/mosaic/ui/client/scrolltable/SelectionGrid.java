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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.scrolltable.overrides.Grid;

/**
 * A variation of the {@link Grid} that supports row or cell hovering and row
 * selection.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li> tr.selected { applied to selected rows } </li>
 * <li> tr.hovering { applied to row currently being hovered } </li>
 * <li> td.hovering { applied to cell currently being hovered } </li>
 * </ul>
 */
public class SelectionGrid extends Grid implements SourceTableSelectionEvents {
  /**
   * This class contains methods used to format a table's cells.
   */
  public class SelectionGridCellFormatter extends CellFormatter {
    @Override
    protected Element getRawElement(int row, int column) {
      return super.getRawElement(row, column);
    }
  }

  /**
   * This class contains methods used to format a table's rows.
   */
  public class SelectionGridRowFormatter extends RowFormatter {
    @Override
    protected Element getRawElement(int row) {
      return super.getRawElement(row);
    }
  }

  /**
   * Selection policies.
   * 
   * <ul>
   * <li>DISABLED - selection is disabled</li>
   * <li>ONE_ROW - one row can be selected at a time</li>
   * <li>MULTI_ROW - multiple rows can be selected at a time</li>
   * </ul>
   */
  public static enum SelectionPolicy {
    DISABLED, ONE_ROW, MULTI_ROW
  }

  /**
   * The cell element currently being hovered.
   */
  private Element hoveringCellElem = null;

  /**
   * The index of the cell currently being hovered.
   */
  private int hoveringCellIndex = -1;

  /**
   * The row element currently being hovered.
   */
  private Element hoveringRowElem = null;

  /**
   * The index of the row currently being hovered.
   */
  private int hoveringRowIndex = -1;

  /**
   * The index of the row that the user selected last.
   */
  private int lastSelectedRowIndex = -1;

  /**
   * The rows that are currently selected.
   */
  private Map<Integer, Element> selectedRows = new HashMap<Integer, Element>();

  /**
   * The selection policy determines if the user can select zero, one, or many
   * rows.
   */
  private SelectionPolicy selectionPolicy = SelectionPolicy.MULTI_ROW;

  /**
   * The {@link TableSelectionListener}s attached to this grid.
   */
  private TableSelectionListenerCollection tableSelectionListeners = null;

  /**
   * Constructor.
   */
  public SelectionGrid() {
    super();
    setCellFormatter(new SelectionGridCellFormatter());
    setRowFormatter(new SelectionGridRowFormatter());

    // Sink hover and selection events
    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEDOWN);
  }

  /**
   * Constructs a {@link SelectionGrid} with the requested size.
   * 
   * @param rows the number of rows
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
  public SelectionGrid(int rows, int columns) {
    this();
    resize(rows, columns);
  }

  /**
   * Add a {@link TableSelectionListener}.
   * 
   * @param listener the listener
   */
  public void addTableSelectionListener(TableSelectionListener listener) {
    if (tableSelectionListeners == null) {
      tableSelectionListeners = new TableSelectionListenerCollection();
    }
    tableSelectionListeners.add(listener);
  }

  /**
   * Deselect a row in the grid. This method is safe to call even if the row is
   * not selected, or doesn't exist (out of bounds).
   * 
   * @param row the row index
   */
  public void deselectRow(int row) {
    Element rowElem = selectedRows.remove(new Integer(row));
    if (rowElem != null) {
      setStyleName(rowElem, "selected", false);
      if (tableSelectionListeners != null) {
        tableSelectionListeners.fireRowDeselected(this, row);
      }
    }
  }

  /**
   * Deselect all selected rows in the data table.
   */
  public void deselectRows() {
    // Deselect all rows
    for (Map.Entry<Integer, Element> entry : selectedRows.entrySet()) {
      setStyleName(entry.getValue(), "selected", false);
    }

    // Fire grid listeners
    if (tableSelectionListeners != null) {
      tableSelectionListeners.fireAllRowsDeselected(this);
    }

    // Clear out the rows
    selectedRows.clear();
  }

  /**
   * @return the set of selected row indexes
   */
  public Set<Integer> getSelectedRows() {
    return selectedRows.keySet();
  }

  /**
   * Explicitly gets the {@link SelectionGridCellFormatter}. The results of
   * {@link com.google.gwt.widgetideas.table.client.overrides.HTMLTable#getCellFormatter()}
   * may also be downcast to a {@link SelectionGridCellFormatter}.
   * 
   * @return the FlexTable's cell formatter
   */
  public SelectionGridCellFormatter getSelectionGridCellFormatter() {
    return (SelectionGridCellFormatter) getCellFormatter();
  }

  /**
   * Explicitly gets the {@link SelectionGridRowFormatter}. The results of
   * {@link com.google.gwt.widgetideas.table.client.overrides.HTMLTable#getRowFormatter()}
   * may also be downcast to a {@link SelectionGridRowFormatter}.
   * 
   * @return the FlexTable's cell formatter
   */
  public SelectionGridRowFormatter getSelectionGridRowFormatter() {
    return (SelectionGridRowFormatter) getRowFormatter();
  }

  /**
   * @return the selection policy
   */
  public SelectionPolicy getSelectionPolicy() {
    return selectionPolicy;
  }

  /**
   * @param row the row index
   * @return true if the row is selected, false if not
   */
  public boolean isRowSelected(int row) {
    return selectedRows.containsKey(new Integer(row));
  }

  /**
   * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
   */
  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    Element targetRow = null;
    Element targetCell = null;

    switch (DOM.eventGetType(event)) {
      // Hover the cell on mouse over
      case Event.ONMOUSEOVER:
        Element cellElem = getEventTargetCell(event); 
        if (cellElem != null) {
          hoverCell(cellElem);
        }
        break;

      // Unhover on mouse out
      case Event.ONMOUSEOUT:
        Element toElem = DOM.eventGetToElement(event);
        if (hoveringRowElem != null
            && (toElem == null || !DOM.isOrHasChild(hoveringRowElem, toElem))) {
          // Check that the coordinates are not directly over the cell
          int clientX = DOM.eventGetClientX(event);
          int clientY = DOM.eventGetClientY(event);
          int rowLeft = DOM.getAbsoluteLeft(hoveringRowElem);
          int rowTop = DOM.getAbsoluteTop(hoveringRowElem);
          int rowWidth = DOM.getElementPropertyInt(hoveringRowElem,
              "offsetWidth");
          int rowHeight = DOM.getElementPropertyInt(hoveringRowElem,
              "offsetHeight");
          int rowBottom = rowTop + rowHeight;
          int rowRight = rowLeft + rowWidth;
          if (clientX > rowLeft && clientX < rowRight && clientY > rowTop
              && clientY < rowBottom) {
            return;
          }

          // Unhover the current cell
          hoverCell(null);
        }
        break;

      // Select a row on click
      case Event.ONMOUSEDOWN:
        // Get the target row
        targetCell = getEventTargetCell(event);
        if (targetCell == null) {
          return;
        }
        targetRow = DOM.getParent(targetCell);
        int targetRowIndex = getRowIndex(targetRow);

        // Select the row
        if (selectionPolicy == SelectionPolicy.MULTI_ROW) {
          boolean shiftKey = DOM.eventGetShiftKey(event);
          boolean ctrlKey = DOM.eventGetCtrlKey(event)
              || DOM.eventGetMetaKey(event);

          // Prevent default text selection
          if (ctrlKey || shiftKey) {
            DOM.eventPreventDefault(event);
          }

          // Select the rows
          selectRow(targetRowIndex, ctrlKey, shiftKey);
        } else if (selectionPolicy == SelectionPolicy.ONE_ROW) {
          selectRow(-1, targetRow, true, true);
          lastSelectedRowIndex = targetRowIndex;
          break;
        }
        break;
    }
  }

  /**
   * Remove a {@link TableSelectionListener}.
   * 
   * @param listener the listener to remove
   */
  public void removeTableSelectionListener(TableSelectionListener listener) {
    if (tableSelectionListeners != null) {
      tableSelectionListeners.remove(listener);
    }
  }

  /**
   * Select a row in the data table.
   * 
   * @param row the row index
   * @param unselectAll unselect all other rows first
   * @throws IndexOutOfBoundsException
   */
  public void selectRow(int row, boolean unselectAll) {
    selectRow(row, getRowFormatter().getElement(row), unselectAll, true);
  }

  /**
   * Select a row in the data table. Simulate the effect of a shift click and/or
   * control click. This method ignores the selection policy, which only applies
   * to user selection via mouse events.
   * 
   * @param row the row index
   * @param ctrlKey true to simulate a control click
   * @param shiftKey true to simulate a shift selection
   * @throws IndexOutOfBoundsException
   */
  public void selectRow(int row, boolean ctrlKey, boolean shiftKey) {
    // Check the row bounds
    checkRowBounds(row);

    // Deselect all rows
    if (!ctrlKey) {
      deselectRows();
    }

    if (shiftKey && (lastSelectedRowIndex > -1)) {
      // Shift+select rows
      SelectionGridRowFormatter formatter = getSelectionGridRowFormatter();
      int firstRow = Math.min(row, lastSelectedRowIndex);
      int lastRow = Math.max(row, lastSelectedRowIndex);
      lastRow = Math.min(lastRow, getRowCount() - 1);
      for (int curRow = firstRow; curRow <= lastRow; curRow++) {
        selectRow(curRow, formatter.getRawElement(curRow), false, false);
      }

      // Fire grid listeners
      if (tableSelectionListeners != null) {
        tableSelectionListeners.fireRowsSelected(this, firstRow, lastRow
            - firstRow + 1);
      }
    } else if (selectedRows.containsKey(new Integer(row))) {
      // Ctrl+unselect a selected row
      deselectRow(row);
      lastSelectedRowIndex = row;
    } else {
      // Select the row
      SelectionGridRowFormatter formatter = getSelectionGridRowFormatter();
      selectRow(row, formatter.getRawElement(row), false, true);
      lastSelectedRowIndex = row;
    }
  }

  /**
   * Set the selection policy, which determines if the user can select zero,
   * one, or multiple rows.
   * 
   * @param selectionPolicy the selection policy
   */
  public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
    if (this.selectionPolicy != selectionPolicy) {
      deselectRows();
      this.selectionPolicy = selectionPolicy;
    }
  }

  /**
   * @return a map or selected row indexes to their elements
   */
  protected Map<Integer, Element> getSelectedRowsMap() {
    return selectedRows;
  }

  /**
   * Set the current hovering cell.
   * 
   * @param cellElem the cell element
   */
  protected void hoverCell(Element cellElem) {
    // Ignore if the cell is already being hovered
    if (DOM.compare(cellElem, hoveringCellElem)) {
      return;
    }

    // Get the row element
    Element rowElem = null;
    if (cellElem != null) {
      rowElem = DOM.getParent(cellElem);
    }

    // Unhover the current cell
    if (hoveringCellElem != null) {
      setStyleName(hoveringCellElem, "hovering", false);
      if (tableSelectionListeners != null) {
        tableSelectionListeners.fireCellUnhover(this, hoveringRowIndex,
            hoveringCellIndex);
      }
      hoveringCellElem = null;
      hoveringCellIndex = -1;

      // Unhover the current row if it changed
      if (!DOM.compare(rowElem, hoveringRowElem)) {
        setStyleName(hoveringRowElem, "hovering", false);
        if (tableSelectionListeners != null) {
          tableSelectionListeners.fireRowUnhover(this, hoveringRowIndex);
        }
        hoveringRowElem = null;
        hoveringRowIndex = -1;
      }
    }

    // Hover the cell
    if (cellElem != null) {
      setStyleName(cellElem, "hovering", true);
      hoveringCellElem = cellElem;
      hoveringCellIndex = DOM.getCellIndex(cellElem);

      // Hover the row if it changed
      if (hoveringRowElem == null) {
        setStyleName(rowElem, "hovering", true);
        hoveringRowElem = rowElem;
        hoveringRowIndex = getRowIndex(hoveringRowElem);
        if (tableSelectionListeners != null) {
          tableSelectionListeners.fireRowHover(this, hoveringRowIndex);
        }
      }

      // Fire listeners
      if (tableSelectionListeners != null) {
        tableSelectionListeners.fireCellHover(this, hoveringRowIndex,
            hoveringCellIndex);
      }
    }
  }

  /**
   * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
   */
  @Override
  protected int insertRow(int beforeRow) {
    deselectRows();
    return super.insertRow(beforeRow);
  }

  /**
   * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
   */
  @Override
  protected void removeRow(int row) {
    super.removeRow(row);
    deselectRows();
  }

  /**
   * Select a row in the data table.
   * 
   * @param row the row index, or -1 if unknown
   * @param rowElem the row element
   * @param unselectAll true to unselect all currently selected rows
   * @param fireEvent true to fire the select event to listeners
   */
  protected void selectRow(int row, Element rowElem, boolean unselectAll,
      boolean fireEvent) {
    // Get the row index if needed
    if (row < 0) {
      row = getRowIndex(rowElem);
    }

    // Ignore request if row already selected
    Integer rowI = new Integer(row);
    if (selectedRows.containsKey(rowI)) {
      return;
    }

    // Deselect current rows
    if (unselectAll) {
      deselectRows();
    }

    // Select the new row
    selectedRows.put(rowI, rowElem);
    setStyleName(rowElem, "selected", true);

    // Fire grid listeners
    if (fireEvent && tableSelectionListeners != null) {
      tableSelectionListeners.fireRowsSelected(this, row, 1);
    }
  }
}
