/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client;

import java.util.Set;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.table.DataTable;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.HasTableDefinition;
import com.google.gwt.gen2.table.client.LiveScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ColumnResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollTableImages;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param RowType
 */
public class LiveTable<RowType> extends LayoutComposite implements Focusable, HasTableDefinition<RowType> {

  private static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

  private final LiveScrollTable<RowType> liveScrollTable;

  public LiveTable(TableModel<RowType> tableModel,
      TableDefinition<RowType> tableDefinistion) {
    super(impl.createFocusable());

    liveScrollTable = new LiveScrollTable<RowType>(tableModel, new DataTable(),
        new FixedWidthFlexTable(), tableDefinistion);
    liveScrollTable.setHeaderGenerated(true);
    liveScrollTable.setFooterGenerated(true);

    init();
  }

  public LiveTable(TableModel<RowType> tableModel,
      TableDefinition<RowType> tableDefinistion, ScrollTableImages images) {
    super(impl.createFocusable());

    liveScrollTable = new LiveScrollTable<RowType>(tableModel, new DataTable(),
        new FixedWidthFlexTable(), tableDefinistion, images);
    liveScrollTable.setHeaderGenerated(true);
    liveScrollTable.setFooterGenerated(true);

    init();
  }

  public LiveTable(TableModel<RowType> tableModel,
      FixedWidthFlexTable headerTable, TableDefinition<RowType> tableDefinition) {
    super(impl.createFocusable());

    liveScrollTable = new LiveScrollTable<RowType>(tableModel, new DataTable(),
        headerTable, tableDefinition);
    liveScrollTable.setHeaderGenerated(false);
    liveScrollTable.setFooterGenerated(true);

    init();
  }

  public LiveTable(TableModel<RowType> tableModel,
      FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition, ScrollTableImages images) {
    super(impl.createFocusable());

    liveScrollTable = new LiveScrollTable<RowType>(tableModel, new DataTable(),
        headerTable, tableDefinition, images);
    liveScrollTable.setHeaderGenerated(false);
    liveScrollTable.setFooterGenerated(true);

    init();
  }

  protected void init() {
    liveScrollTable.setPageSize(50); // TODO
    liveScrollTable.setEmptyTableWidget(new HTML("There is no data to display"));
    
    getLayoutPanel().add(liveScrollTable);
    
    setStyleName("mosaic-Table");
    
    // sinkEvents(Event.FOCUSEVENTS | Event.KEYEVENTS | Event.ONCLICK
    // | Event.MOUSEEVENTS | Event.ONMOUSEWHEEL);
    sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
        | Event.ONFOCUS | Event.ONKEYDOWN);

    // Hide focus outline in Mozilla/Webkit/Opera
    DOM.setStyleAttribute(getElement(), "outline", "0px");

    // Hide focus outline in IE 6/7
    DOM.setElementAttribute(getElement(), "hideFocus", "true");
  }
  
  public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
    return ((DataTable) liveScrollTable.getDataTable()).addDoubleClickHandler(handler);
  }
  
  public com.google.gwt.gen2.event.shared.HandlerRegistration addRowSelectionHandler(
      RowSelectionHandler handler) {
    return liveScrollTable.getDataTable().addRowSelectionHandler(handler);
  }
  
  private void eatEvent(Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);
  }
  
  /**
   * Adjust all column widths so they take up the maximum amount of space
   * without needing a horizontal scroll bar. The distribution will be
   * proportional to the current width of each column.
   * 
   * The {@link AbstractScrollTable} must be visible on the page for this method
   * to work.
   */
  public void fillWidth() {
    liveScrollTable.fillWidth();
  }
  
  /**
   * @return the column resize policy
   */
  public ColumnResizePolicy getColumnResizePolicy() {
    return liveScrollTable.getColumnResizePolicy();
  }
  
  /**
   * Return the column width for a given column index.
   * 
   * @param column the column index
   * @return the column width in pixels
   */
  public int getColumnWidth(int column) {
    return liveScrollTable.getColumnWidth(column);
  }
  
  /**
   * @return the widget displayed when the data table is empty
   */
  public Widget getEmptyTableWidget() {
    return liveScrollTable.getEmptyTableWidget();
  }
  
  /**
   * Get the absolute maximum width of a column.
   * 
   * @param column the column index
   * @return the maximum allowable width of the column
   */
  public int getMaximumColumnWidth(int column) {
    return liveScrollTable.getMaximumColumnWidth(column);
  }
  
  /**
   * Get the absolute minimum width of a column.
   * 
   * @param column the column index
   * @return the minimum allowable width of the column
   */
  public int getMinimumColumnWidth(int column) {
    return liveScrollTable.getMinimumColumnWidth(column);
  }
  
  /**
   * Get the preferred width of a column.
   * 
   * @param column the column index
   * @return the preferred width of the column
   */
  public int getPreferredColumnWidth(int column) {
    return liveScrollTable.getPreferredColumnWidth(column);
  }
  
  /**
   * @return the resize policy
   */
  public ResizePolicy getResizePolicy() {
    return liveScrollTable.getResizePolicy();
  }
  
  /**
   * Get the value associated with a row.
   * 
   * @param row the row index
   * @return the value associated with the row
   */
  public RowType getRowValue(int row) {
    return liveScrollTable.getRowValue(row);
  }
  
  /**
   * Gets the currently selected item. If multiple items are selected, this
   * method will return the first selected item ({@link #isItemSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   * @see #isItemSelected(int)
   * @see #addChangeListener(ChangeListener)
   */
  public int getSelectedIndex() {
    Set<Integer> selection = liveScrollTable.getDataTable().getSelectedRows();
    if (selection != null && selection.size() > 0) {
      return selection.iterator().next();
    }
    return -1;
  }
  
  /**
   * @return the set of selected row indexes
   */
  public Set<Integer> getSelectedIndices() {
    return liveScrollTable.getDataTable().getSelectedRows();
  }
  
  /**
   * @return the current sort policy
   */
  public SortPolicy getSortPolicy() {
    return liveScrollTable.getSortPolicy();
  }

  public int getTabIndex() {
    return impl.getTabIndex(getElement());
  }
  
  public TableDefinition<RowType> getTableDefinition() {
    return liveScrollTable.getTableDefinition();
  }
  
  /**
   * Returns true if the specified column is sortable.
   * 
   * @param column the column index
   * @return true if the column is sortable, false if it is not sortable
   */
  public boolean isColumnSortable(int column) {
    return liveScrollTable.isColumnSortable(column);
  }
  
  /**
   * @return the table model
   */
  public TableModel<RowType> getTableModel() {
    return liveScrollTable.getTableModel();
  }
  
  private void moveDown() {
    if (selectFirstItemIfNodeSelected()) {
      return;
    }
    selectNextItem();
  }

  private void moveUp() {
    if (selectFirstItemIfNodeSelected()) {
      return;
    }
    selectPrevItem();
  }

  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONKEYDOWN:
        int keyCode = event.getKeyCode();
        switch (keyCode) {
          case KeyCodes.KEY_UP:
            moveUp();
            eatEvent(event);
            break;
          case KeyCodes.KEY_DOWN:
            moveDown();
            eatEvent(event);
            break;
          case KeyCodes.KEY_LEFT:

            break;
          case KeyCodes.KEY_RIGHT:

            break;
          default:
            super.onBrowserEvent(event);
            break;
        }
        break;
      case Event.ONCLICK:
        setFocus(true);
      default:
        super.onBrowserEvent(event);
    }
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    liveScrollTable.gotoPage(0, false);
  }
  
  /**
   * Redraw the table.
   */
  public void redraw() {
    liveScrollTable.redraw();
  }
  
  /**
   * Reset the widths of all columns to their preferred sizes.
   */
  public void resetColumnWidths() {
    liveScrollTable.resetColumnWidths();
  }
  
  /**
   * Selects the first item in the list if no items are currently selected. This
   * method assumes that the list has at least 1 item.
   * 
   * @return {@code true} if no item was previously selected and the first item
   *         in the list was selected, {@code false} otherwise
   */
  private boolean selectFirstItemIfNodeSelected() {
    if (getSelectedIndex() == -1) {
      setSelectedIndex(0);
      return true;
    }
    return false;
  }
  
  private void selectNextItem() {
    int index = getSelectedIndex();
    if (index == -1) {
      return;
    }

    if (index < liveScrollTable.getDataTable().getRowCount() - 1) {
      setSelectedIndex(++index);

      DOM.scrollIntoView((Element) liveScrollTable.getDataTable().getRowFormatter().getElement(
          getSelectedIndex()).getFirstChild());
    }
  }
  
  private void selectPrevItem() {
    int index = getSelectedIndex();
    if (index == -1) {
      return;
    }

    if (index > 0) {
      setSelectedIndex(--index);

      DOM.scrollIntoView((Element) liveScrollTable.getDataTable().getRowFormatter().getElement(
          getSelectedIndex()).getFirstChild());
    }
  }
  
  public void setAccessKey(char key) {
    impl.setAccessKey(getElement(), key);
  }
  
  /**
   * Set the resize policy applied to user actions that resize columns.
   * 
   * @param columnResizePolicy the resize policy
   */
  public void setColumnResizePolicy(ColumnResizePolicy columnResizePolicy) {
    liveScrollTable.setColumnResizePolicy(columnResizePolicy);
  }
  
  /**
   * Set the width of a column.
   * 
   * @param column the index of the column
   * @param width the width in pixels
   * @return the new column width
   */
  public int setColumnWidth(int column, int width) {
    return liveScrollTable.setColumnWidth(column, width);
  }
  
  /**
   * Set the {@link Widget} that will be displayed in place of the data table
   * when the data table has no data to display.
   * 
   * @param emptyTableWidget the widget to display when the data table is empty
   */
  public void setEmptyTableWidget(Widget emptyTableWidget) {
    liveScrollTable.setEmptyTableWidget(emptyTableWidget);
  }

  public void setFocus(boolean focused) {
    if (focused) {
      impl.focus(getElement());
    } else {
      impl.blur(getElement());
    }
  }
  
  /**
   * Set the resize policy of the table.
   * 
   * @param resizePolicy the resize policy
   */
  public void setResizePolicy(ResizePolicy resizePolicy) {
    liveScrollTable.setResizePolicy(resizePolicy);
  }
  
  /**
   * Associate a row in the table with a value.
   * 
   * @param row the row index
   * @param value the value to associate
   */
  public void setRowValue(int row, RowType value) {
    liveScrollTable.setRowValue(row, value);
  }
  
  /**
   * Sets the currently selected index.
   * <p>
   * After calling this method, only the specified item in the list will remain
   * selected. For a {@code ListBox} with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * <p>
   * TODO (check) Note that setting the selected index programmatically does
   * <em>not</em> cause the {@link ChangeListener#onChange(Widget)} event to be
   * fired.
   * 
   * @param row the row of the item to be selected
   * @see #setItemSelected(int, boolean)
   * @see #getSelectedIndex()
   */
  public void setSelectedIndex(int row) {
    liveScrollTable.getDataTable().selectRow(row, true);
  }

  /**
   * Set the {@link SortPolicy} that defines what columns users can sort.
   * 
   * @param sortPolicy the {@link SortPolicy}
   */
  public void setSortPolicy(SortPolicy sortPolicy) {
    liveScrollTable.setSortPolicy(sortPolicy);
  }

  public void setTabIndex(int index) {
    impl.setTabIndex(getElement(), index);
  }

  /**
   * Set the {@link TableDefinition} used to define the columns.
   * 
   * @param tableDefinition the new table definition.
   */
  public void setTableDefinition(TableDefinition<RowType> tableDefinition) {
    liveScrollTable.setTableDefinition(tableDefinition);
  }
}
