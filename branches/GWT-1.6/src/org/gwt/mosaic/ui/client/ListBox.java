/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.gwt.mosaic.ui.client.list.ListDataEvent;
import org.gwt.mosaic.ui.client.list.ListDataListener;
import org.gwt.mosaic.ui.client.list.ListModel;
import org.gwt.mosaic.ui.client.table.DataTable;
import org.gwt.mosaic.ui.client.table.ScrollTable2;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ColumnResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.override.client.HTMLTable.CellFormatter;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;

/**
 * This widget is used to create a list of items where one or more of the items
 * may be selected. A {@code ListBox} may contain multiple columns. A separate
 * model, {@link ListModel}, maintains the contents of the list.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @parem <T>
 */
public class ListBox<T> extends LayoutComposite implements Focusable,
    ListDataListener {
  /**
   * The render used to set cell contents.
   * 
   * @param <T>
   */
  public interface CellRenderer<T> {
    /**
     * Render the contents of a cell.
     * 
     * @param listBox the {@code ListBox} that is asking the renderer to draw
     * @param row the row index
     * @param column the column index
     * @param item the item to render
     */
    void renderCell(ListBox<T> listBox, int row, int column, T item);
  }

  private static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

  private static final int INSERT_AT_END = -1;

  private final ScrollTable2 scrollTable;
  private final DataTable dataTable = new DataTable();

  /**
   * The cell renderer used on the data table.
   */
  private CellRenderer<T> cellRenderer = new CellRenderer<T>() {
    public void renderCell(ListBox<T> listBox, int row, int column, T item) {
      if (item instanceof Widget) {
        listBox.setWidget(row, column, (Widget) item);
      } else {
        listBox.setText(row, column, item.toString());
      }
    }
  };

  /**
   * The values associated with each row.
   */
  private Map<Element, T> rowItems = new HashMap<Element, T>();

  private ListModel<T> dataModel;

  /**
   * Creates an empty list box in single selection mode.
   */
  public ListBox() {
    this(null);
  }

  public ListBox(String[] columns) {
    super(impl.createFocusable());

    final FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
    createHeaderTable(headerTable, columns);

    scrollTable = new ScrollTable2(dataTable, headerTable);
    scrollTable.setResizePolicy(ResizePolicy.FILL_WIDTH);
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);

    setMultipleSelect(false);

    dataTable.resize(0, getColumnCount());

    getLayoutPanel().add(scrollTable);

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
    return ((DataTable) scrollTable.getDataTable()).addDoubleClickHandler(handler);
  }

  public com.google.gwt.gen2.event.shared.HandlerRegistration addRowSelectionHandler(
      RowSelectionHandler handler) {
    return scrollTable.getDataTable().addRowSelectionHandler(handler);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * The contents of the list have changed in some way. This method will be
   * called if the change cannot be notified via the
   * {@link #intervalAdded(ListDataEvent)} or the
   * {@link #intervalRemoved(ListDataEvent)} methods.
   * 
   * @param event the event
   */
  public void contentsChanged(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      for (int i = event.getIndex0(), n = event.getIndex1(); i <= n; ++i) {
        if (i >= 0 && i < getItemCount()) {
          renderItemOnUpdate(i, dataModel.getElementAt(i));
        }
      }
    }
  }

  protected void createHeaderTable(FixedWidthFlexTable headerTable,
      String[] columns) {
    if (columns != null && columns.length > 0) {
      for (int column = 0; column < columns.length; ++column) {
        headerTable.setHTML(0, column, columns[column]);
      }
      setColumnsCount(columns.length);
    } else {
      headerTable.setText(0, 0, null);
      headerTable.setVisible(false);
      setColumnsCount(1);
    }
  }

  private void eatEvent(Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);
  }

  /**
   * Gets the {@link CellFormatter} associated with this table.
   * 
   * @return this table's cell formatter
   */
  public CellFormatter getCellFormatter() {
    return scrollTable.getDataTable().getCellFormatter();
  }

  /**
   * Get the {@link CellRenderer} used to render cells.
   * 
   * @return the current renderer
   */
  public CellRenderer<T> getCellRenderer() {
    return cellRenderer;
  }

  /**
   * Gets the number of columns in this grid.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return dataTable.getColumnCount();
  }

  /**
   * @return the column resize policy
   */
  public ColumnResizePolicy getColumnResizePolicy() {
    return scrollTable.getColumnResizePolicy();
  }

  public int getColumnWidth(int column) {
    return scrollTable.getColumnWidth(column);
  }

  public PopupMenu getContextMenu() {
    return dataTable.getContextMenu();
  }

  /**
   * Gets the item at the specified index.
   * 
   * @param index the index of the item to be retrieved
   * @return the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public T getItem(int index) {
    checkIndex(index);
    return rowItems.get(dataTable.getRowFormatter().getElement(index));
  }

  /**
   * Gets the number of items present in the list box.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return dataTable.getRowCount();
  }

  /**
   * Get the absolute maximum width of a column.
   * 
   * @param column the column index
   * @return the maximum allowable width of the column
   */
  public int getMaximumColumnWidth(int column) {
    return scrollTable.getMaximumColumnWidth(column);
  }

  /**
   * Get the absolute minimum width of a column.
   * 
   * @param column the column index
   * @return the minimum allowable width of the column
   */
  public int getMinimumColumnWidth(int column) {
    return scrollTable.getMinimumColumnWidth(column);
  }

  /**
   * Returns the data model.
   * 
   * @return the {@code ListModel} that provides the displayed list of items
   */
  public ListModel<T> getModel() {
    if (dataModel == null) {
      setModel(new DefaultListModel<T>());
    }
    return dataModel;
  }

  /**
   * Get the preferred width of a column.
   * 
   * @param column the column index
   * @return the preferred width of the column
   */
  public int getPreferredColumnWidth(int column) {
    return scrollTable.getPreferredColumnWidth(column);
  }

  /**
   * Gets the currently selected item. If multiple items are selected, this
   * method will return the first selected item ({@link #isItemSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   * @see #isItemSelected(int)
   */
  public int getSelectedIndex() {
    if (dataTable.isSelectionEnabled()) {
      Set<Integer> selection = dataTable.getSelectedRows();
      if (selection != null && selection.size() > 0) {
        return selection.iterator().next();
      }
    }
    return -1;
  }

  /**
   * Returns a {@code Set} of all the selected indices.
   * 
   * @return all of the selected indices in a {@code Set}
   */
  public Set<Integer> getSelectedIndices() {
    return dataTable.getSelectedRows();
  }

  /**
   * @return the current sort policy
   */
  public SortPolicy getSortPolicy() {
    return scrollTable.getSortPolicy();
  }

  public int getTabIndex() {
    return impl.getTabIndex(getElement());
  }

  /**
   * One or more items have been added to the list. The {@code event} argument
   * can supply the indices for the range of items added.
   * 
   * @param event the event
   */
  public void intervalAdded(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      for (int i = event.getIndex0(), n = event.getIndex1(); i <= n && i >= 0; ++i) {
        if (i < getItemCount()) {
          renderItemOnInsert(dataModel.getElementAt(i), i);
        } else {
          renderItemOnInsert(dataModel.getElementAt(i), INSERT_AT_END);
        }
      }
    }
  }

  /**
   * One or more items have been removed from the list. The {@code event}
   * argument can supply the indicies for range of items removed.
   * 
   * @param event the event
   */
  public void intervalRemoved(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      for (int i = event.getIndex1(), n = event.getIndex0(); i >= n && i >= 0; --i) {
        renderOnRemove(i);
      }
    }
  }

  /**
   * Returns true if the specified column is sortable.
   * 
   * @param column the column index
   * @return true if the column is sortable, false if it is not sortable
   */
  public boolean isColumnSortable(int column) {
    return isColumnSortable(column);
  }

  /**
   * Returns true if the specified column can be truncated. If it cannot be
   * truncated, its minimum width will be adjusted to ensure the cell content is
   * visible.
   * 
   * @param column the column index
   * @return true if the column is truncatable, false if it is not
   */
  public boolean isColumnTruncatable(int column) {
    return scrollTable.isColumnTruncatable(column);
  }

  /**
   * Determines whether an individual list item is selected.
   * 
   * @param index the index of the item to be tested
   * @return {@code true} if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   * @see #getSelectedIndices()
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return dataTable.isRowSelected(index);
  }

  /**
   * Gets whether this list allows multiple selection.
   * 
   * @return {@code true} if multiple selection is allowed
   * @see #setMultipleSelect(boolean)
   */
  public boolean isMultipleSelect() {
    return dataTable.isSelectionEnabled()
        && (dataTable.getSelectionPolicy() == SelectionPolicy.MULTI_ROW || dataTable.getSelectionPolicy() == SelectionPolicy.CHECKBOX);
  }

  /**
   * @return {@code true} if selection is enabled, {@code false} otherwise
   */
  public boolean isSelectionEnabled() {
    return dataTable.isSelectionEnabled();
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

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
   */
  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONKEYDOWN:
        int keyCode = DOM.eventGetKeyCode(event);
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
            DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
                getSelectedIndex()).getFirstChild());
            break;
          case KeyCodes.KEY_RIGHT:
            DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
                getSelectedIndex()).getLastChild());
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

  /**
   * Renders an inserted item.
   * 
   * @param item the item to be inserted
   * @param index the index at which to insert it
   */
  protected void renderItemOnInsert(T item, int index) {
    if ((index == INSERT_AT_END) || (index == dataTable.getRowCount())) {
      index = dataTable.getRowCount();
    }

    dataTable.insertRow(index);

    // Set the data in the new row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      cellRenderer.renderCell(this, index, cellIndex, item);
    }

    // Map item with <tr>
    rowItems.put(dataTable.getRowFormatter().getElement(index), item);
  }

  /**
   * Renders an updated item.
   * 
   * @param index the index of the item to be set
   * @param item the item's new value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  protected void renderItemOnUpdate(int index, T item) {
    checkIndex(index);

    if (item == null) {
      throw new NullPointerException("Cannot set an item to null");
    }

    // Set the data in the row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      cellRenderer.renderCell(this, index, cellIndex, item);
    }

    // Map the new item with <tr>
    rowItems.put(dataTable.getRowFormatter().getElement(index), item);
  }

  /**
   * Removes all items from the list box.
   */
  protected void renderOnClear() {
    dataTable.resizeRows(0);
    rowItems.clear();
  }

  /**
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  protected void renderOnRemove(int index) {
    checkIndex(index);
    Element tr = dataTable.getRowFormatter().getElement(index);
    dataTable.removeRow(index);
    rowItems.remove(tr);
  }

  /**
   * Selects the first item in the list if no items are currently selected. This
   * method assumes that the list has at least 1 item.
   * 
   * @return {@code true} if no item was previosly selected and the first item
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

    if (index < getItemCount() - 1) {
      setSelectedIndex(++index);
    } else {
      // we're at the end, loop around to the start
      setSelectedIndex(0);
    }

    DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
        getSelectedIndex()).getFirstChild());
  }

  private void selectPrevItem() {
    int index = getSelectedIndex();
    if (index == -1) {
      return;
    }

    if (index > 0) {
      setSelectedIndex(--index);
    } else {
      // we're at the start, loop around to the end
      setSelectedIndex(getItemCount() - 1);
    }

    DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
        getSelectedIndex()).getFirstChild());
  }

  public void setAccessKey(char key) {
    impl.setAccessKey(getElement(), key);
  }

  /**
   * Set the {@link CellRenderer} used to render cell contents.
   * 
   * @param cellRenderer the new renderer
   */
  public void setCellRenderer(CellRenderer<T> cellRenderer) {
    this.cellRenderer = cellRenderer;
  }

  /**
   * Set the resize policy applied to user actions that resize columns.
   * 
   * @param columnResizePolicy the resize policy
   */
  public void setColumnResizePolicy(ColumnResizePolicy columnResizePolicy) {
    scrollTable.setColumnResizePolicy(columnResizePolicy);
  }

  /**
   * Resizes the {@code ListBox} to be the specified number of columns.
   * 
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
  public void setColumnsCount(int columns) {
    dataTable.resizeColumns(columns);
  }

  /**
   * Enable or disable sorting on a specific column. All columns are sortable by
   * default.
   * 
   * @param column the index of the column
   * @param sortable {@code true} to enable sorting for this column, {@code
   *          false} to disable
   */
  public void setColumnSortable(int column, boolean sortable) {
    scrollTable.setColumnSortable(column, sortable);
  }

  /**
   * Enable or disable truncation on a specific column. When enabled, the column
   * width will be adjusted to fit the content. All columns are truncatable by
   * default.
   * 
   * @param column the index of the column
   * @param truncatable true to enable truncation, false to disable
   */
  public void setColumnTruncatable(int column, boolean truncatable) {
    scrollTable.setColumnTruncatable(column, truncatable);
  }

  /**
   * Set the width of a column.
   * 
   * @param column the index of the column
   * @param width the width in pixels
   * @return the new column width
   */
  public int setColumnWidth(int column, int width) {
    return scrollTable.setColumnWidth(column, width);
  }

  public void setContextMenu(PopupMenu contextMenu) {
    dataTable.setContextMenu(contextMenu);
  }

  public void setFocus(boolean focused) {
    if (focused) {
      impl.focus(getElement());
    } else {
      impl.blur(getElement());
    }
  }

  /**
   * Sets whether an individual list item is selected.
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeHandler#onChange(ChangeEvent)} event to be fired.
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected {@code true} to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   * @see #setSelectedIndex(int)
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    if (selected) {
      dataTable.selectRow(index, false);
    } else {
      dataTable.deselectRow(index);
    }
  }

  /**
   * Set the maximum width of the column.
   * 
   * @param column the column index
   * @param maxWidth the maximum width
   */
  public void setMaximumColumnWidth(int column, int maxWidth) {
    scrollTable.setMaximumColumnWidth(column, maxWidth);
  }

  /**
   * Set the minimum width of the column.
   * 
   * @param column the column index
   * @param minWidth the minimum width
   */
  public void setMinimumColumnWidth(int column, int minWidth) {
    scrollTable.setMinimumColumnWidth(column, minWidth);
  }

  /**
   * Sets the model that represents the contents of the {@code ListBox}, and
   * then clears the list's selection.
   * 
   * @param dataModel the {@link ListModel} that provides the list of items for
   *          display
   * @see #getModel
   */
  public void setModel(ListModel<T> dataModel) {
    if (dataModel == null) {
      throw new IllegalArgumentException("model must be non null");
    }
    if (this.dataModel == dataModel) {
      return;
    }
    if (this.dataModel != null) {
      this.dataModel.removeListDataListener(this);
      dataTable.deselectAllRows();
      renderOnClear();
    }

    this.dataModel = dataModel;

    // TODO bulk update
    for (int i = 0, n = dataModel.getSize(); i < n; ++i) {
      renderItemOnInsert(dataModel.getElementAt(i), INSERT_AT_END);
    }

    this.dataModel.addListDataListener(this);

    layout();
  }

  /**
   * Sets whether this list allows multiple selections.
   * 
   * @param multiple {@code true} to allow multiple selections
   */
  public void setMultipleSelect(boolean multiple) {
    dataTable.setSelectionPolicy(multiple ? SelectionPolicy.MULTI_ROW
        : SelectionPolicy.ONE_ROW);
  }

  /**
   * Set the preferred width of a column. The table will attempt maintain the
   * preferred width of the column. If it cannot, the preferred widths will
   * serve as relative weights when distributing available width.
   * 
   * @param column the column index
   * @param preferredWidth the preferred width
   */
  public void setPreferredColumnWidth(int column, int preferredWidth) {
    scrollTable.setPreferredColumnWidth(column, preferredWidth);
  }

  /**
   * Sets the currently selected index.
   * <p>
   * After calling this method, only the specified item in the list will remain
   * selected. For a {@code ListBox} with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeHandler#onChange(ChangeEvent)} event to be fired.
   * fired.
   * 
   * @param index the index of the item to be selected
   * @see #setItemSelected(int, boolean)
   * @see #getSelectedIndex()
   */
  public void setSelectedIndex(int index) {
    checkIndex(index);
    dataTable.selectRow(index, true);
  }

  /**
   * Enable or disable row selection.
   * 
   * @param enabled {@code true} to enable, {@code false} to disable
   */
  public void setSelectionEnabled(boolean enabled) {
    dataTable.setSelectionEnabled(enabled);
  }

  /**
   * Set the {@link SortPolicy} that defines what columns users can sort.
   * 
   * @param sortPolicy the {@link SortPolicy}
   */
  public void setSortPolicy(SortPolicy sortPolicy) {
    scrollTable.setSortPolicy(sortPolicy);
  }

  public void setTabIndex(int index) {
    impl.setTabIndex(getElement(), index);
  }

  public void setText(int row, int column, String text) {
    dataTable.setText(row, column, text);
  }

  public void setWidget(int row, int column, Widget widget) {
    dataTable.setWidget(row, column, widget);
  }

}
