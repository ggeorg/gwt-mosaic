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
import org.gwt.mosaic.ui.client.table.ScrollTable2;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
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
public class ListBox2<T> extends LayoutComposite implements Focusable,
    ListDataListener {

  private static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

  private static final int INSERT_AT_END = -1;

  private final ScrollTable2 scrollTable;
  private final FixedWidthGrid dataTable;
  private final FixedWidthFlexTable headerTable;

  private ListModel<T> dataModel;

  private Map<Element, T> rowItems = new HashMap<Element, T>();

  /**
   * Creates an empty list box in single selection mode.
   */
  public ListBox2() {
    this(null);
  }

  public ListBox2(String[] columns) {
    super(impl.createFocusable());

    if (columns != null && columns.length > 0) {
      headerTable = new FixedWidthFlexTable();
      for (int column = 0; column < columns.length; ++column) {
        headerTable.setHTML(0, column, columns[column]);
      }
      setColumnsCount(columns.length);
    } else {
      headerTable = new FixedWidthFlexTable();
      headerTable.setHTML(0, 0, null);
      setColumnsCount(1);
    }

    dataTable = new FixedWidthGrid();

    scrollTable = new ScrollTable2(dataTable, headerTable);
    scrollTable.setResizePolicy(ResizePolicy.FILL_WIDTH);

    setMultipleSelect(false);

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

  /**
   * Gets the number of columns in this grid.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return dataTable.getColumnCount();
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
   * Gets the currently selected item. If multiple items are selected, this
   * method will return the first selected item ({@link #isItemSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   * @see #isItemSelected(int)
   */
  public int getSelectedIndex() {
    Set<Integer> selection = dataTable.getSelectedRows();
    if (selection != null && selection.size() > 0) {
      return selection.iterator().next();
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
    return dataTable.getSelectionPolicy() == SelectionPolicy.MULTI_ROW;
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
   * Sets whether an individual list item is selected.
   * <p>
   * Note that setting the selection programmatically does <em>not</em> cause
   * the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected {@code true} to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    checkIndex(index);
    dataTable.selectRow(index, false);
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
   * Removes all items from the list box.
   */
  protected void renderOnClear() {
    dataTable.resizeRows(0);
    rowItems.clear();
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
   * @param index the index of the item to be selected
   * @see #setItemSelected(int, boolean)
   * @see #getSelectedIndex()
   */
  public void setSelectedIndex(int index) {
    checkIndex(index);
    dataTable.selectRow(index, true);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  public int getTabIndex() {
    // TODO Auto-generated method stub
    return 0;
  }

  public void setAccessKey(char key) {
    // TODO Auto-generated method stub

  }

  public void setFocus(boolean focused) {
    // TODO Auto-generated method stub

  }

  public void setTabIndex(int index) {
    // TODO Auto-generated method stub

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
        if (i < getItemCount()) {
          renderItemOnUpdate(i, dataModel.getElementAt(i));
        }
      }
    }
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
      throw new NullPointerException("Cannot set ann item to null");
    }

    // Set the data in the row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      //cellRenderer.renderCell(this, index, cellIndex, item);
    }

    // Map the new item with <tr>
    rowItems.put(dataTable.getRowFormatter().getElement(index), item);

  }

  /**
   * One or more items have been added to the list. The {@code event} argument
   * can supply the indices for the range of items added.
   * 
   * @param event the event
   */
  public void intervalAdded(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      for (int i = event.getIndex0(), n = event.getIndex1(); i <= n; ++i) {
        if (i < getItemCount()) {
          renderItemOnInsert(dataModel.getElementAt(i), i);
        } else {
          renderItemOnInsert(dataModel.getElementAt(i), INSERT_AT_END);
        }
      }
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
      // cellRenderer.rende
    }

    // Map item with <tr>
    rowItems.put(dataTable.getRowFormatter().getElement(index), item);
  }

  /**
   * One or more items have been removed from the list. The {@code event}
   * argument can supply the indicies for range of items removed.
   * 
   * @param event the event
   */
  public void intervalRemoved(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      for (int i = event.getIndex1(), n = event.getIndex0(); i >= n; --i) {
        renderOnRemove(i);
      }
    }
  }

  /**
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  protected void renderOnRemove(int index) {
    checkIndex(index);
    rowItems.remove(getItem(index));
    dataTable.removeRow(index);
  }

}
