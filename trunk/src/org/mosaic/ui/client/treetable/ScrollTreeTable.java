/*
 * Copyright 2006-2008 Google Inc. Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client.treetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.LayoutComposite;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.ScrollTable.ResizePolicy;
import com.google.gwt.widgetideas.table.client.ScrollTable.ScrollPolicy;
import com.google.gwt.widgetideas.table.client.overrides.OverrideDOM;

public class ScrollTreeTable extends LayoutComposite {

  /**
   * The resize policies related to user resizing.
   * 
   * <ul>
   * <li>DISABLED - Columns cannot be resized by the user</li>
   * <li>SINGLE_CELL - Only cells with a colspan of 1 can be resized</li>
   * <li>MULTI_CELL - All cells can be resized by the user</li>
   * </ul>
   */
  public enum ColumnResizePolicy {
    DISABLED, SINGLE_CELL, MULTI_CELL
  }

  /**
   * A helper class that handles some of the mouse events associated with
   * resizing columns.
   */
  private static class MouseResizeWorker {

    /**
     * A Linked List Node that contains information about a column within the
     * colspan of the current cell including its offset from the current cell's
     * index and its original width.
     * 
     */
    private class ColumnNode {

      /**
       * The cell index.
       */
      private int cellIndex;

      /**
       * The original width of this cell.
       */
      private int originalWidth;

      public ColumnNode(int cellIndex, int originalWidth) {
        this.cellIndex = cellIndex;
        this.originalWidth = originalWidth;
      }

      public int getCellIndex() {
        return cellIndex;
      }

      public int getOriginalWidth() {
        return originalWidth;
      }
    }

    /**
     * The current header cell that the mouse is affecting.
     */
    private Element curCell = null;

    /**
     * The current columns under the colSpan of the current cell, ordered from
     * narrowest to widest.
     */
    private List<ColumnNode> curCellColumns = new ArrayList<ColumnNode>();

    /**
     * The index of the current header cell.
     */
    private int curCellIndex = 0;

    /**
     * The current x position of the mouse.
     */
    private int mouseXCurrent = 0;

    /**
     * The last x position of the mouse when we resized.
     */
    private int mouseXLast = 0;

    /**
     * The starting x position of the mouse when resizing a column.
     */
    private int mouseXStart = 0;

    /**
     * A timer used to resize the columns. As long as the timer is active, it
     * will poll for the new row size and resize the columns.
     */
    private Timer resizeTimer = new Timer() {
      @Override
      public void run() {
        resizeColumn();
        schedule(100);
      }
    };

    /**
     * A boolean indicating that we are resizing the current header cell.
     */
    private boolean resizing = false;

    /**
     * The column that will be sacrificed.
     */
    private int sacrificeColumn = -1;

    /**
     * The tree table that this worker affects.
     */
    private ScrollTreeTable table = null;

    /**
     * Get the actual cell index of a cell in the header table.
     * 
     * @param cell the cell element
     * @return the cell index
     */
    private int getCellIndex(Element cell) {
      int row = OverrideDOM.getRowIndex(DOM.getParent(cell)) - 1;
      int column = OverrideDOM.getCellIndex(cell);
      return table.headerTable.getColumnIndex(row, column);
    }

    /**
     * Returns the current cell.
     * 
     * @return the current cell
     */
    public Element getCurrentCell() {
      return curCell;
    }

    /**
     * Get the scroll tree table.
     * 
     * @return the scroll tree table
     */
    protected ScrollTreeTable getScrollTable() {
      return table;
    }

    /**
     * Returns true if a header is currently being resized.
     * 
     * @return true if resizing
     */
    public boolean isResizing() {
      return resizing;
    }

    /**
     * Helper method that actually sets the column size. This method is called
     * periodically by a timer.
     */
    private void resizeColumn() {
      if (mouseXLast != mouseXCurrent) {
        mouseXLast = mouseXCurrent;

        table.disableScrollTables = true;
        int columnsRemaining = curCellColumns.size();
        int totalDelta = mouseXCurrent - mouseXStart;
        for (ColumnNode curNode : curCellColumns) {
          int originalWidth = curNode.getOriginalWidth();
          int column = curNode.getCellIndex();
          int delta = totalDelta / columnsRemaining;
          int colWidth = table.setColumnWidth(column, originalWidth + delta,
              sacrificeColumn);

          totalDelta -= (colWidth - originalWidth);
          columnsRemaining--;
        }

        table.disableScrollTables = false;
        table.scrollTables(false);
      }
    }

    /**
     * Resize the column on a mouse event. This method also marks the client as
     * busy so we do not try to change the size repeatedly.
     * 
     * @param event the mouse event
     */
    public void resizeColumn(Event event) {
      mouseXCurrent = DOM.eventGetClientX(event);
    }

    /**
     * Set the current cell that will be resized based on the mouse event.
     * 
     * @param event the event that triggered the new cell
     * @return true if the cell was actually changed
     */
    public boolean setCurrentCell(Event event) {
      // Check the resize policy of the table
      Element cell = null;
      if (table.columnResizePolicy == ColumnResizePolicy.MULTI_CELL) {
        cell = table.headerTable.getEventTargetCell(event);
      } else if (table.columnResizePolicy == ColumnResizePolicy.SINGLE_CELL) {
        cell = table.headerTable.getEventTargetCell(event);
        if (cell != null && DOM.getElementPropertyInt(cell, "colSpan") > 1) {
          cell = null;
        }
      }

      // See if we are near the edge of the cell
      int clientX = DOM.eventGetClientX(event);
      if (cell != null) {
        int absRight = DOM.getAbsoluteLeft(cell)
            + DOM.getElementPropertyInt(cell, "offsetWidth");
        if (clientX < absRight - 15 || clientX > absRight) {
          cell = null;
        }
      }

      // Change out the current cell
      if (cell != curCell) {
        // Clear the old cell
        if (curCell != null) {
          DOM.setStyleAttribute(curCell, "cursor", "default");
        }

        // Set the new cell
        curCell = cell;
        if (curCell != null) {
          curCellIndex = getCellIndex(curCell);
          if (table.isColumnWidthGuaranteed(curCellIndex)) {
            curCell = null;
            return false;
          }
          DOM.setStyleAttribute(curCell, "cursor", "e-resize");
        }
        return true;
      }

      // The cell did not change
      return false;
    }

    /**
     * Set the ScrollTable table that this worker affects.
     * 
     * @param table the scroll table
     */
    public void setScrollTreeTable(ScrollTreeTable table) {
      this.table = table;
    }

    /**
     * Start resizing the current cell when the user clicks on the right edge of
     * the cell.
     * 
     * @param event the mouse event
     */
    public void startResizing(Event event) {
      if (curCell != null) {
        resizing = true;
        mouseXStart = DOM.eventGetClientX(event);
        mouseXLast = mouseXStart;
        mouseXCurrent = mouseXStart;

        // Get the columns under this cell's colspan
        int colSpan = DOM.getElementPropertyInt(curCell, "colSpan");
        sacrificeColumn = curCellIndex + colSpan;
        for (int i = 0; i < colSpan; i++) {
          int newCellIndex = curCellIndex + i;
          if (!table.isColumnWidthGuaranteed(newCellIndex)) {
            int originalWidth = table.dataTable.getColumnWidth(newCellIndex);

            // Insert the node into the ordered list by width
            int insertIndex = 0;
            for (ColumnNode curNode : curCellColumns) {
              if (originalWidth > curNode.getOriginalWidth()) {
                insertIndex++;
              } else {
                break;
              }
            }

            // Add the node at the correct index
            curCellColumns.add(insertIndex, new ColumnNode(newCellIndex,
                originalWidth));
          }
        }

        // Start the timer and listen for changes
        DOM.setCapture(table.headerWrapper.getElement());
        resizeTimer.schedule(20);
      }
    }

    /**
     * Stop resizing the current cell.
     * 
     * @param event the mouse event
     */
    public void stopResizing(Event event) {
      if (curCell != null && resizing) {
        curCellColumns.clear();
        resizing = false;
        DOM.releaseCapture(table.headerWrapper.getElement());
        resizeTimer.cancel();
        resizeColumn();
      }
    }
  }

  /**
   * The Opera version of the mouse worker fixes an Opera bug where the cursor
   * isn't updated if the mouse is hovering over an element DOM object when its
   * cursor style is changed.
   */
  @SuppressWarnings("unused")
  private static class MouseResizeWorkerOpera extends MouseResizeWorker {
    /**
     * A div used to force the cursor to update.
     */
    private Element cursorUpdateDiv;

    /**
     * Constructor.
     */
    public MouseResizeWorkerOpera() {
      cursorUpdateDiv = DOM.createDiv();
      DOM.setStyleAttribute(cursorUpdateDiv, "position", "absolute");
    }

    /**
     * Remove the cursor update div from the page.
     */
    private void removeCursorUpdateDiv() {
      if (DOM.getCaptureElement() != null) {
        DOM.removeChild(RootPanel.getBodyElement(), cursorUpdateDiv);
        DOM.releaseCapture(getScrollTable().headerWrapper.getElement());
      }
    }

    /**
     * Set the current cell that will be resized based on the mouse event.
     * 
     * @param event the event that triggered the new cell
     * @return true if the cell was actually changed
     */
    @Override
    public boolean setCurrentCell(Event event) {
      // Check if cursor update div is active
      if (DOM.eventGetTarget(event) == cursorUpdateDiv) {
        removeCursorUpdateDiv();
        return false;
      }

      // Use the parent method
      boolean cellChanged = super.setCurrentCell(event);

      // Position a div that forces a cursor redraw in Opera
      if (cellChanged) {
        DOM.setCapture(getScrollTable().headerWrapper.getElement());
        DOM.setStyleAttribute(cursorUpdateDiv, "height",
            (Window.getClientHeight() - 1) + "px");
        DOM.setStyleAttribute(cursorUpdateDiv, "width",
            (Window.getClientWidth() - 1) + "px");
        DOM.setStyleAttribute(cursorUpdateDiv, "left", "0px");
        DOM.setStyleAttribute(cursorUpdateDiv, "top", "0px");
        DOM.appendChild(RootPanel.getBodyElement(), cursorUpdateDiv);
      }
      return cellChanged;
    }

    /**
     * Start resizing the current cell.
     * 
     * @param event the mouse event
     */
    @Override
    public void startResizing(Event event) {
      removeCursorUpdateDiv();
      super.startResizing(event);
    }
  }

  /**
   * The resize policies of table cells.
   * 
   * <ul>
   * <li>UNCONSTRAINED - Columns shrink and expand independently of each other</li>
   * <li>FLOW - As one column expands or shrinks, the columns to the right will
   * do the opposite, trying to maintain the same size. The user can still
   * expand the grid if there is no more space to take from the columns on the
   * right.</li>
   * <li>FIXED_WIDTH - As one column expands or shrinks, the columns to the
   * right will do the opposite, trying to maintain the same size. The width of
   * the grid will remain constant, ignoring column resizing that would result
   * in the grid growing in size.</li>
   * <li>FILL_WIDTH - Same as FIXED_WIDTH, but the grid will always fill the
   * available width, even if the widget is resized.</li>
   * </ul>
   */
  public enum ResizePolicy {
    UNCONSTRAINED, FLOW, FIXED_WIDTH, FILL_WIDTH
  }

  /**
   * The scroll policy of the table.
   * 
   * <ul>
   * <li>HORIZONTAL - Only a horizontal scrollbar will be present.</li>
   * <li>BOTH - Both a vertical and horizontal scrollbar will be present.</li>
   * <li>DISABLED - Scrollbars will not appear, even if content doesn't fit</li>
   * </ul>
   */
  public enum ScrollPolicy {
    HORIZONTAL, BOTH, DISABLED
  }

  /**
   * The default style name.
   */
  public static final String DEFAULT_STYLENAME = "mosaic-ScrollTreeTable";

  /**
   * The policy applied to user actions that resize columns.
   */
  private ColumnResizePolicy columnResizePolicy = ColumnResizePolicy.MULTI_CELL;

  /**
   * The minimum allowed width of the data table.
   */
  private int minWidth = -1;

  /**
   * A boolean indicating whether or not the grid should try to maintain its
   * width as much as possible.
   */
  private ResizePolicy resizePolicy = ResizePolicy.FLOW;

  /**
   * Columns which have guaranteed sizes.
   */
  private Set<Integer> guaranteedColumns = new HashSet<Integer>();

  /**
   * The worker that helps with mouse resize events.
   */
  private MouseResizeWorker resizeWorker = GWT.create(MouseResizeWorker.class);

  /**
   * The scrolling policy.
   */
  private ScrollPolicy scrollPolicy = ScrollPolicy.BOTH;

  /**
   * The header table.
   */
  private FixedWidthFlexTable headerTable = null;

  /**
   * The non-scrollable wrapper div around the header table.
   */
  private AbsolutePanel headerWrapper = null;

  /**
   * A spacer used to stretch the headerTable area so we can scroll past the
   * edge of the header table.
   */
  private Element headerSpacer = null;

  /**
   * The data table.
   */
  private FastTreeTable dataTable;

  /**
   * The scrollable wrapper div around the data table.
   */
  private AbsolutePanel dataWrapper;

  /**
   * The footer table.
   */
  private FixedWidthFlexTable footerTable = null;

  /**
   * The non-scrollable wrapper div around the footer table.
   */
  private AbsolutePanel footerWrapper = null;

  /**
   * A spacer used to stretch the footerTable area so we can scroll past the
   * edge of the footer table.
   */
  private Element footerSpacer = null;

  /**
   * If true, ignore all requests to scroll the tables. This boolean is used to
   * improve performance of certain operations that would otherwise force
   * repeated calls to scrollTables.
   */
  private boolean disableScrollTables = false;

  /**
   * An image used to show a fill width button.
   */
  private Image fillWidthImage;

  /**
   * The Image use to indicate the currently sorted column.
   */
  private Image sortedColumnIndicator = new Image();

  /**
   * The TD cell that initiated a column sort operation.
   */
  private Element sortedColumnTrigger = null;

  /**
   * The wrapper around the image indicator.
   */
  private Element sortedColumnWrapper = null;

  /**
   * A boolean indicating whether or not sorting is enabled.
   */
  private boolean sortingEnabled = true;

  /**
   * A map of columns that cannot be sorted.
   * 
   * key = the column index
   * 
   * value = true if the column is sortable, false of not
   */
  private Map<Integer, Boolean> unsortableColumns = new HashMap<Integer, Boolean>();

  public ScrollTreeTable(FastTreeTable dataTable,
      FixedWidthFlexTable headerTable) {
    super();

    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BorderLayout());
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    this.dataTable = dataTable;
    this.headerTable = headerTable;
    resizeWorker.setScrollTreeTable(this);

    // Prepare the header and data tables
    prepareTable(dataTable, "dataTable");
    prepareTable(headerTable, "headerTable");

    setStylePrimaryName(DEFAULT_STYLENAME);

    // Create the table wrapper and spacer
    headerWrapper = createWrapper("headerWrapper");
    headerSpacer = createSpacer(headerWrapper.getElement());
    dataWrapper = createWrapper("dataWrapper");

    // Create image to fill width
    // fillWidthImage = new Image() {
    // @Override
    // public void onBrowserEvent(Event event) {
    // super.onBrowserEvent(event);
    // if (DOM.eventGetType(event) == Event.ONCLICK) {
    // fillWidth();
    // }
    // }
    // };

    // fillWidthImage.setTitle("Shrink/Expand to fill visible area");
    // images.scrollTableFillWidth().applyTo(fillWidthImage);
    // Element fillWidthImageElem = fillWidthImage.getElement();
    // DOM.setStyleAttribute(fillWidthImageElem, "cursor", "pointer");
    // DOM.setStyleAttribute(fillWidthImageElem, "position", "absolute");
    // DOM.setStyleAttribute(fillWidthImageElem, "top", "0px");
    // DOM.setStyleAttribute(fillWidthImageElem, "right", "0px");
    // DOM.setStyleAttribute(fillWidthImageElem, "zIndex", "1");
    // add(fillWidthImage, getElement());

    // Adopt the header and the data tables into the panel
    adoptTable(headerTable, headerWrapper, BorderLayoutRegion.NORTH);
    adoptTable(dataTable, dataWrapper, BorderLayoutRegion.CENTER);

    // Create the sort indicator Image
    // TODO

    // Add some event handling
    sinkEvents(Event.ONMOUSEOUT);
    DOM.setEventListener(dataWrapper.getElement(), this);
    dataWrapper.sinkEvents(Event.ONSCROLL);
    DOM.setEventListener(headerWrapper.getElement(), this);
    headerWrapper.sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEDOWN
        | Event.ONMOUSEUP | Event.ONCLICK);

    // Listen for sorting events in the data table
    // TODO

    // Set the default supported operations
    // try {
    // setSortingEnabled(sortingEnabled);
    // } catch (UnsupportedOperationException e) {
    // // Ignore, this may not be implemented
    // }
  }

  /**
   * Adjust all column width so they take up the maximum amount of space without
   * needing a horizontal scroll bar. The distribution will be proportional to
   * the current width of each column.
   * 
   * The {@link ScrollTreeTable} must be visible on the page for this method to
   * work.
   */
  public void fillWidth() {
    final Element dataWrapperElem = dataWrapper.getElement();
    // Calculate how much room we have to work width
    int clientWidth = -1;
    if (scrollPolicy == ScrollPolicy.BOTH) {
      DOM.setStyleAttribute(dataWrapperElem, "overflow", "scroll");
      clientWidth = DOM.getElementPropertyInt(dataWrapper.getElement(),
          "clientWidth") - 1;
      DOM.setStyleAttribute(dataWrapperElem, "overflow", "auto");
    } else {
      clientWidth = DOM.getElementPropertyInt(dataWrapperElem, "clientWidth");
    }
    if (clientWidth <= 0) {
      return;
    }
    clientWidth = Math.max(clientWidth, minWidth);
    int diff = clientWidth - ((Widget) dataTable).getOffsetWidth();

    // Temporarily set resize policy to unconstrained
    ResizePolicy tempResizePolicy = getResizePolicy();
    resizePolicy = ResizePolicy.UNCONSTRAINED;

    // Calculate the total width of the columns that aren't guaranteed
    int totalWidth = 0;
    int numColumns = dataTable.getColumnCount();
    int[] colWidths = new int[numColumns];
    for (int i = 0; i < numColumns; i++) {
      if (isColumnWidthGuaranteed(i)) {
        colWidths[i] = 0;
      } else {
        colWidths[i] = dataTable.getColumnWidth(i);
      }
      totalWidth += colWidths[i];
    }

    // Prevent tables from aligning, we'll call this manually at the end
    disableScrollTables = true;

    // Distribute the difference across all columns, weighted by current size
    int remainingDiff = diff;
    for (int i = 0; i < numColumns; i++) {
      if (colWidths[i] > 0) {
        int colDiff = (int) (diff * (colWidths[i] / (float) totalWidth));
        colDiff = setColumnWidth(i, colWidths[i] + colDiff) - colWidths[i];
        remainingDiff -= colDiff;
        colWidths[i] += colDiff;
      }
    }

    // Spread out remaining diff however possible
    if (remainingDiff != 0) {
      for (int i = 0; i < numColumns && remainingDiff != 0; i++) {
        if (!isColumnWidthGuaranteed(i)) {
          int colWidth = setColumnWidth(i, colWidths[i] + remainingDiff);
          remainingDiff -= (colWidth - colWidths[i]);
        }
      }
    }

    // Reset the resize policy
    resizePolicy = tempResizePolicy;
    disableScrollTables = false;
    scrollTables(false);
  }

  /**
   * Set the width of a column. If the column has already been set using the
   * {@link #setGuaranteedColumnWidth(int, int)} method, the column will no
   * longer have a guaranteed column width.
   * 
   * @param column the index of the column
   * @param width the width in pixels
   * @return the new column width
   */
  public int setColumnWidth(int column, int width) {
    guaranteedColumns.remove(new Integer(column));
    return setColumnWidth(column, width, column + 1);
  }

  /**
   * @return the resize policy
   */
  public ResizePolicy getResizePolicy() {
    return resizePolicy;
  }

  /**
   * @return the current scroll policy
   */
  public ScrollPolicy getScrollPolicy() {
    return scrollPolicy;
  }

  /**
   * Adopt a table into this {@link ScrollTable} within its wrapper.
   * 
   * @param table the table to adopt
   * @param wrapper the wrapper panel
   * @param region the border layout region to insert the wrapper
   */
  private void adoptTable(Widget table, AbsolutePanel wrapper,
      BorderLayoutRegion region) {
    wrapper.add(table);
    getWidget().add(wrapper, new BorderLayoutData(region));
  }

  /**
   * Create a spacer element that allows scrolling past the edge of a table.
   * 
   * @param wrapper the wrapper element
   * @return a new spacer element
   */
  private Element createSpacer(Element wrapper) {
    Element spacer = DOM.createDiv();
    DOM.setStyleAttribute(spacer, "height", "1px");
    DOM.setStyleAttribute(spacer, "width", "10000px");
    DOM.setStyleAttribute(spacer, "position", "absolute");
    DOM.setStyleAttribute(spacer, "top", "1px");
    DOM.setStyleAttribute(spacer, "left", "1px");
    DOM.appendChild(wrapper, spacer);
    return spacer;
  }

  /**
   * Create a wrapper element that will hold a table.
   * 
   * @param cssName the style name added to the base name
   * @return a new wrapper element
   */
  private AbsolutePanel createWrapper(String cssName) {
    final AbsolutePanel wrapper = new AbsolutePanel();
    final Element wrapperElem = wrapper.getElement();
    DOM.setIntStyleAttribute(wrapperElem, "margin", 0);
    DOM.setIntStyleAttribute(wrapperElem, "border", 0);
    DOM.setIntStyleAttribute(wrapperElem, "padding", 0);
    wrapper.setStyleName(cssName);
    return wrapper;
  }

  /**
   * Get the cell padding of the tables, in pixels.
   * 
   * @return the cell padding of the tables
   */
  public int getCellPadding() {
    return dataTable.getCellPadding();
  }

  /**
   * Get the cell spacing of the tables, in pixels.
   * 
   * @return the cell spacing of the tables
   */
  public int getCellSpacing() {
    return dataTable.getCellSpacing();
  }

  /**
   * Get the footer table.
   * 
   * @return the footer table
   */
  public FixedWidthFlexTable getFooterTable() {
    return footerTable;
  }

  /**
   * 
   * @param column the column index
   * @return true if the column is sortable, false if it is not sortable
   */
  public boolean isColumnSortable(int column) {
    Boolean sortable = unsortableColumns.get(new Integer(column));
    if (sortable == null) {
      return sortingEnabled;
    } else {
      return sortable.booleanValue();
    }
  }

  /**
   * 
   * @param column the column index
   * @return true if the column width is guaranteed
   */
  public boolean isColumnWidthGuaranteed(int column) {
    return guaranteedColumns.contains(new Integer(column));
  }

  /**
   * Prepare a table to be added to the {@link ScrollTreeTable}.
   * 
   * @param table the table to prepare
   * @param cssName the style name added to the base name
   */
  private void prepareTable(Widget table, String cssName) {
    Element tableElem = table.getElement();
    DOM.setStyleAttribute(tableElem, "margin", "0px");
    DOM.setStyleAttribute(tableElem, "border", "0px");
    table.addStyleName(cssName);
  }

  /**
   * Sets the amount of padding to be added around all cells.
   * 
   * @param padding the cell padding, in pixels
   */
  public void setCellPaddind(int padding) {
    headerTable.setCellPadding(padding);
    dataTable.setCellPadding(padding);
    if (footerTable != null) {
      footerTable.setCellPadding(padding);
    }
  }

  /**
   * Sets the amount of spacing to be added around all cells.
   * 
   * @param spacing the cell spacing, in pixels
   */
  public void setCellSpacing(int spacing) {
    headerTable.setCellSpacing(spacing);
    dataTable.setCellSpacing(spacing);
    if (footerTable != null) {
      footerTable.setCellSpacing(spacing);
    }
  }

  /**
   * Set the width of a column.
   * 
   * @param column the index of the column
   * @param width the width in pixels
   * @param sacrificeColumn the column that will be shrunk to maintain the width
   * @return the new column width
   */
  public int setColumnWidth(int column, int width, int sacrificeColumn) {
    // A zero width will render improperly, so the width must be at least 1
    width = Math.max(width, 1);

    // Try to constrain the size of the grid
    if (resizePolicy != ResizePolicy.UNCONSTRAINED) {
      int diff = width - getColumnWidth(column);
      diff += redistributeWidth(-diff, sacrificeColumn);

      // Prevent over resizing
      if (resizePolicy == ResizePolicy.FIXED_WIDTH
          || resizePolicy == ResizePolicy.FILL_WIDTH) {
        width -= diff;
      }
    }

    // Resize the column
    dataTable.setColumnWidth(column, width);
    headerTable.setColumnWidth(column, width);
    if (footerTable != null) {
      footerTable.setColumnWidth(column, width);
    }

    // Reposition things as needed
    repositionHeaderSpacer();
    scrollTables(false);
    return width;
  }

  /**
   * Sets the scroll property of the header and data wrappers when scrolling so
   * that the header, footer, and data tables line up.
   * 
   * @param baseHeader If true, use the header as the alignment source
   */
  protected void scrollTables(boolean baseHeader) {
    // Return if scrolling is disabled
    if (scrollPolicy == ScrollPolicy.DISABLED) {
      return;
    }

    if (isAttached() && !disableScrollTables) {
      if (baseHeader) {
        int headerScrollLeft = headerWrapper.getElement().getScrollLeft();
        dataWrapper.getElement().setScrollLeft(headerScrollLeft);
      }
      int scrollLeft = dataWrapper.getElement().getScrollLeft();
      headerWrapper.getElement().setScrollLeft(scrollLeft);
      if (footerWrapper != null) {
        footerWrapper.getElement().setScrollLeft(scrollLeft);
      }
    }
  }

  /**
   * Reposition the header spacer next to the header table.
   */
  private void repositionHeaderSpacer() {
    int headerWidth = headerTable.getOffsetWidth();
    headerSpacer.getStyle().setPropertyPx("left", headerWidth);
    if (footerTable != null) {
      int footerWidth = footerTable.getOffsetWidth();
      footerSpacer.getStyle().setPropertyPx("left", footerWidth);
    }
  }

  /**
   * Distribute a given amount of width over all columns to the right of the
   * specified column.
   * 
   * @param width the width to distribute
   * @param startColumn the index of the first column to receive the width
   * @return the actual amount of distributed width
   */
  private int redistributeWidth(int width, int startColumn) {
    // Make sure we have a column to distribute to
    int numColumns = Math.max(headerTable.getColumnCount(),
        dataTable.getColumnCount());
    if (startColumn >= numColumns) {
      return 0;
    }

    // Find the first column with a non-guaranteed width
    if (isColumnWidthGuaranteed(startColumn)) {
      boolean columnFound = false;
      for (int i = startColumn + 1; i < numColumns; i++) {
        if (!isColumnWidthGuaranteed(i)) {
          startColumn = i;
          columnFound = true;
          break;
        }
      }
      if (!columnFound) {
        return 0;
      }
    }

    // Redistribute the width across the columns
    int actualWidth = 0;
    if (width > 0) {
      int startWidth = getColumnWidth(startColumn);
      int newWidth = startWidth + width;
      dataTable.setColumnWidth(startColumn, newWidth);
      headerTable.setColumnWidth(startColumn, newWidth);
      if (footerTable != null) {
        footerTable.setColumnWidth(startColumn, newWidth);
      }
      actualWidth = width;
    } else if (width < 0) {
      for (int i = startColumn; i < numColumns && width < 0; i++) {
        if (!isColumnWidthGuaranteed(i)) {
          int startWidth = getColumnWidth(i);
          int newWidth = startWidth + width;
          dataTable.setColumnWidth(i, newWidth);
          headerTable.setColumnWidth(i, newWidth);
          if (footerTable != null) {
            footerTable.setColumnWidth(i, newWidth);
          }
          int colDiff = startWidth - getColumnWidth(i);
          width += colDiff;
          actualWidth -= colDiff;
        }
      }
    }

    // Return the actual width
    return actualWidth;
  }

  /**
   * Return the column width for a given column index.
   * 
   * @param column the column index
   * @return the column width in pixels
   */
  public int getColumnWidth(int column) {
    return dataTable.getColumnWidth(column);
  }

  /**
   * Set the footer table that appears under the data table. If set to null, the
   * footer table will not be shown.
   * 
   * @param footerTable the table to use in the footer
   */
  public void setFooterTable(FixedWidthFlexTable footerTable) {
    // Disown the old footer table
    if (this.footerTable != null) {
      getWidget().remove(footerWrapper);
      footerWrapper.remove(footerTable);
      DOM.removeChild(footerWrapper.getElement(), footerSpacer);
    }

    // Set the new footer table
    this.footerTable = footerTable;
    if (footerTable != null) {
      footerTable.setCellSpacing(getCellSpacing());
      footerTable.setCellSpacing(getCellPadding());
      prepareTable(footerTable, "footerTable");

      // Create the footer table wrapper and spacer
      if (footerWrapper == null) {
        footerWrapper = createWrapper("footerWrapper");
        footerSpacer = createSpacer(footerWrapper.getElement());
        DOM.setEventListener(footerWrapper.getElement(), this);
        footerWrapper.sinkEvents(Event.ONMOUSEUP);
      }

      // Adopt the footer table into the panel
      adoptTable(footerTable, footerWrapper, BorderLayoutRegion.SOUTH);
    }
  }

  /**
   * Set the resize policy of the table.
   * 
   * @param resizePolicy the resize policy
   */
  public void setResizePolicy(ResizePolicy resizePolicy) {
    this.resizePolicy = resizePolicy;
    // updateFillWidthImage();
    maybeFillWidth();
  }

  /**
   * Extend the columns to exactly fill the available space, if the current
   * {@link ResizePolicy} requires it.
   */
  private void maybeFillWidth() {
    if (resizePolicy == ResizePolicy.FILL_WIDTH) {
      fillWidth();
    }
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    Element target = DOM.eventGetTarget(event);
    switch (DOM.eventGetType(event)) {
      case Event.ONSCROLL:
        // Reposition the tables on scroll
        scrollTables(false);
        break;

      case Event.ONMOUSEDOWN:
        // Start resizing a header column
        if (DOM.eventGetButton(event) != Event.BUTTON_LEFT) {
          return;
        }
        if (resizeWorker.getCurrentCell() != null) {
          DOM.eventPreventDefault(event);
          DOM.eventCancelBubble(event, true);
          resizeWorker.startResizing(event);
        }
        break;
      case Event.ONMOUSEUP:
        if (DOM.eventGetButton(event) != Event.BUTTON_LEFT) {
          return;
        }
        // Stop resizing the header column
        if (resizeWorker.isResizing()) {
          resizeWorker.stopResizing(event);
        } else {
          // Scroll tables if needed
          if (DOM.isOrHasChild(headerWrapper.getElement(), target)) {
            scrollTables(true);
          } else {
            scrollTables(false);
          }

          // Get the actual column index
          Element cellElem = headerTable.getEventTargetCell(event);
          if (cellElem != null) {
            int row = OverrideDOM.getRowIndex(DOM.getParent(cellElem)) - 1;
            int cell = OverrideDOM.getCellIndex(cellElem);
            int column = headerTable.getColumnIndex(row, cell);
            if (isColumnSortable(column)) {
              if (dataTable.getColumnCount() > column) {
                sortedColumnTrigger = cellElem;
                dataTable.sortColumn(column);
              }
            }
          }
        }
        break;
      case Event.ONMOUSEMOVE:
        // Resize the header column
        if (resizeWorker.isResizing()) {
          resizeWorker.resizeColumn(event);
        } else {
          resizeWorker.setCurrentCell(event);
        }
        break;
      case Event.ONMOUSEOUT:
        // Unhover if the mouse leaves the table
        Element toElem = DOM.eventGetToElement(event);
        if (toElem == null
            || !DOM.isOrHasChild(dataWrapper.getElement(), toElem)) {
          // Check that the coordinates are not directly over the table
          int clientX = DOM.eventGetClientX(event);
          int clientY = DOM.eventGetClientY(event);
          int tableLeft = DOM.getAbsoluteLeft(dataWrapper.getElement());
          int tableTop = DOM.getAbsoluteTop(dataWrapper.getElement());
          int tableWidth = DOM.getElementPropertyInt(dataWrapper.getElement(),
              "offsetWidth");
          int tableHeight = DOM.getElementPropertyInt(dataWrapper.getElement(),
              "offsetHeight");
          int tableBottom = tableTop + tableHeight;
          int tableRight = tableLeft + tableWidth;
          if (clientX > tableLeft && clientX < tableRight && clientY > tableTop
              && clientY < tableBottom) {
            return;
          }

          // TODO dataTable.hoverCell(null);
        }
        break;
    }
  }

  /**
   * Set the scroll policy of the table.
   * 
   * @param scrollPolicy the new scroll policy
   */
  public void setScrollPolicy(ScrollPolicy scrollPolicy) {
    this.scrollPolicy = scrollPolicy;

    if (scrollPolicy == ScrollPolicy.DISABLED) {
      // Disabled scroll bars
      super.setHeight("auto");
      dataWrapper.getElement().getStyle().setProperty("height", "auto");
      dataWrapper.getElement().getStyle().setProperty("overflow", "");
      getElement().getStyle().setProperty("overflow", "");
    } else if (scrollPolicy == ScrollPolicy.HORIZONTAL) {
      // Only show horizontal scroll bar
      super.setHeight("auto");
      dataWrapper.getElement().getStyle().setProperty("overflow", "auto");
      getElement().getStyle().setProperty("overflow", "hidden");
    } else if (scrollPolicy == ScrollPolicy.BOTH) {
      // Show both scroll bars
      // if (lastHeight != null) {
      // super.setHeight(lastHeight);
      // }
      dataWrapper.getElement().getStyle().setProperty("overflow", "auto");
      getElement().getStyle().setProperty("overflow", "hidden");
    }

    // Resize the tables
    // redraw();
  }

  @Override
  public void layout() {
    super.layout();
    
    // Force browser to redraw
    if (scrollPolicy == ScrollPolicy.DISABLED) {
      dataWrapper.getElement().getStyle().setProperty("overflow", "auto");
      dataWrapper.getElement().getStyle().setProperty("overflow", "");
    } else {
      dataWrapper.getElement().getStyle().setProperty("overflow", "hidden");
      dataWrapper.getElement().getStyle().setProperty("overflow", "auto");
      scrollTables(true);
    }
  }

}
