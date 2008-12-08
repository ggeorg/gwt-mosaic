/*
 * Copyright 2006-2008 Google Inc.
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
import org.gwt.mosaic.ui.client.ColumnWidget.ResizePolicy;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.ListDataEvent;
import org.gwt.mosaic.ui.client.list.ListDataListener;
import org.gwt.mosaic.ui.client.list.ListModel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.SourceTableSelectionEvents;
import com.google.gwt.widgetideas.table.client.TableSelectionListener;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;

/**
 * This widget is used to create a list of items where one or more of the items
 * may be selected. A {@code ListBox} may contain multiple columns. There are
 * numerous methods which allow the items in the {@code ListBox} to be retrieved
 * and modified.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
public class ListBox<T> extends LayoutComposite implements HasFocus,
    ListDataListener {

  static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

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

  private static class DataGrid extends FixedWidthGrid {

    public DataGrid() {
      super();
      // sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.KEYEVENTS);
    }

    private Event onMouseDownEvent = null;

    /**
     * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
     */
    @Override
    public void onBrowserEvent(Event event) {
      Element targetRow = null;
      Element targetCell = null;

      switch (DOM.eventGetType(event)) {
        // Select a row on click
        case Event.ONMOUSEDOWN:
          onMouseDownEvent = event;
          super.onBrowserEvent(event);
          break;

        // Fire double click event
        case Event.ONDBLCLICK:
          doubleClickListeners.fireDblClick(this);
          break;

        // Show context menu
        case Event.ONCONTEXTMENU:
          targetCell = getEventTargetCell(event);
          if (targetCell == null) {
            return;
          }
          targetRow = DOM.getParent(targetCell);
          int targetRowIndex = getRowIndex(targetRow);
          if (!isRowSelected(targetRowIndex)) {
            super.onBrowserEvent(onMouseDownEvent);
          }
          DOM.eventPreventDefault(event);
          showContextMenu(event);
          break;

        default:
          super.onBrowserEvent(event);
      }
    }

    private DoubleClickListenerCollection doubleClickListeners;

    public void addDoubleClickListener(DoubleClickListener listener) {
      if (doubleClickListeners == null) {
        doubleClickListeners = new DoubleClickListenerCollection();
        sinkEvents(Event.ONDBLCLICK);
      }
      doubleClickListeners.add(listener);
    }

    public void removeDoubleClickListener(DoubleClickListener listener) {
      if (doubleClickListeners != null) {
        doubleClickListeners.remove(listener);
      }
    }

    private PopupMenu contextMenu;

    public PopupMenu getContextMenu() {
      return contextMenu;
    }

    public void setContextMenu(PopupMenu contextMenu) {
      this.contextMenu = contextMenu;
      if (this.contextMenu != null) {
        sinkEvents(Event.ONCONTEXTMENU);
      }
    }

    private void showContextMenu(final Event event) {
      contextMenu.setPopupPositionAndShow(new PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          contextMenu.setPopupPosition(event.getClientX(), event.getClientY());
        }
      });
    }

    @Override
    protected void hoverCell(Element cellElem) {
      super.hoverCell(cellElem);
    }

  }

  private static final int INSERT_AT_END = -1;

  private final ColumnWidget columnWidget;
  private final DataGrid dataTable = new DataGrid();
  private final FixedWidthFlexTable headerTable;

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
   * Get the {@link CellRenderer} used to render cells.
   * 
   * @return the current renderer
   */
  public CellRenderer<T> getCellRenderer() {
    return cellRenderer;
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
   * The values associated with each row.
   */
  private Map<Element, T> rowItems = new HashMap<Element, T>();

  /**
   * Creates an empty list box in single selection mode.
   */
  public ListBox() {
    this(null);
  }

  /**
   * 
   * @param strings
   */
  public ListBox(String[] columns) {
    super(impl.createFocusable());

    if (columns != null && columns.length > 0) {
      headerTable = new FixedWidthFlexTable();
      for (int column = 0; column < columns.length; ++column) {
        headerTable.setHTML(0, column, columns[column]);
      }
      setColumnsCount(columns.length);
    } else {
      headerTable = null;
    }

    final LayoutPanel layoutPanel = getWidget();
    columnWidget = new ColumnWidget(dataTable, headerTable) {
      @Override
      protected void hoverCell(Element cellElem) {
        dataTable.hoverCell(cellElem);
      }
    };
    setMultipleSelect(false);
    columnWidget.setResizePolicy(ResizePolicy.FILL_WIDTH);
    layoutPanel.add(columnWidget);

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
   * {@inheritDoc}
   * 
   * @see com.google.gwt.widgetideas.table.client.overrides.HTMLTable
   */
  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONCLICK:
        setFocus(true);
        super.onBrowserEvent(event);
        break;
      case Event.ONKEYDOWN:
        int keyCode = DOM.eventGetKeyCode(event);
        switch (keyCode) {
          case KeyboardListener.KEY_UP:
            moveUp();
            eatEvent(event);
            break;
          case KeyboardListener.KEY_DOWN:
            moveDown();
            eatEvent(event);
            break;
          case KeyboardListener.KEY_LEFT:
            DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
                getSelectedIndex()).getFirstChild());
            break;
          case KeyboardListener.KEY_RIGHT:
            DOM.scrollIntoView((Element) dataTable.getRowFormatter().getElement(
                getSelectedIndex()).getLastChild());
            break;
          default:
            super.onBrowserEvent(event);
            break;
        }
        break;

      default:
        super.onBrowserEvent(event);
    }
  }

  private void eatEvent(Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);
  }

  /**
   * Selects the firs item in the list if no items are currently selected. This
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

  private void moveUp() {
    if (selectFirstItemIfNodeSelected()) {
      return;
    }
    selectPrevItemItem();
  }

  private void moveDown() {
    if (selectFirstItemIfNodeSelected()) {
      return;
    }
    selectNextItem();
  }

  private void selectPrevItemItem() {
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

  /**
   * Adds an item to the list box.
   * 
   * @param item the item to be added
   */
  public void addItem(T item) {
    insertItem(item, INSERT_AT_END);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getItemCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public void layout() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        columnWidget.fillWidth();
      }
    });
    super.layout();
  }

  /**
   * Removes all items from the list box.
   */
  public void clear() {
    dataTable.clearAll();
    rowItems.clear();
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
   * Gets the currently-selected item. If multiple items are selected, this
   * method will returns the first selected item ({@link #isItemeSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   */
  public int getSelectedIndex() {
    Set<Integer> selection = dataTable.getSelectedRows();
    for (Integer i : selection) {
      return i.intValue();
    }
    return -1;
  }
  
  public Set<Integer> getSelectedItems() {
    return dataTable.getSelectedRows();
  }

  /**
   * Inserts an item into the list box.
   * 
   * @param item the item to be inserted
   * @param index the index at which to insert it
   */
  public void insertItem(T item, int index) {
    if (dataTable.getColumnCount() == 0) {
      dataTable.resizeColumns(1);
    }
    if ((index == INSERT_AT_END) || (index == dataTable.getRowCount())) {
      // Insert the new row
      dataTable.insertRow(index = dataTable.getRowCount());
    } else {
      // Insert the new row
      dataTable.insertRow(index);
    }
    // Set the data in the new row
    for (int cellIndex = 0, n = dataTable.getColumnCount(); cellIndex < n; ++cellIndex) {
      cellRenderer.renderCell(this, index, cellIndex, item);
    }
    // Map item with <tr>
    final Element tr = dataTable.getRowFormatter().getElement(index);
    rowItems.put(tr, item);
  }

  /**
   * Sets the item at a given index.
   * 
   * @param index the index of the item to be set
   * @param text the item's new value
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItem(int index, T item) {
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
   * Determines whether as individual list is selected.
   * 
   * @param index the index of the item to be tested
   * @return {@code true} if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    checkIndex(index);
    return dataTable.isRowSelected(index);
  }

  /**
   * Gets whether this list allows multiple selection.
   * 
   * @return {@code true} if multiple selection is allowed
   */
  public boolean isMultipleSelect() {
    return dataTable.getSelectionPolicy() == SelectionPolicy.MULTI_ROW;
  }

  /**
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    checkIndex(index);
    rowItems.remove(getItem(index));
    dataTable.removeRow(index);
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
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    checkIndex(index);
    dataTable.selectRow(index, true);
  }

  /**
   * Resizes the {@code ListBox} to the specified number of columns.
   * 
   * @param columns the number of columns
   * @throws IndexOutOfBoundsException
   */
  public void setColumnsCount(int columns) {
    dataTable.resizeColumns(columns);
  }

  /**
   * Gets the number of columns in this grid.
   * 
   * @return the number of columns
   */
  public int getColumnsCount() {
    return dataTable.getColumnCount();
  }

  private DoubleClickListenerCollection doubleClickListeners;

  public void addDoubleClickListener(DoubleClickListener listener) {
    if (doubleClickListeners == null) {
      doubleClickListeners = new DoubleClickListenerCollection();
      dataTable.addDoubleClickListener(new DoubleClickListener() {
        public void onDoubleClick(Widget sender) {
          doubleClickListeners.fireDblClick(ListBox.this);
        }
      });
    }
    doubleClickListeners.add(listener);
  }

  public void removeDoubleClickListener(DoubleClickListener listener) {
    if (doubleClickListeners != null) {
      doubleClickListeners.remove(listener);
    }
  }

  private ChangeListenerCollection changeListeners;

  public void addChangeListener(ChangeListener listener) {
    if (changeListeners == null) {
      changeListeners = new ChangeListenerCollection();
      dataTable.addTableSelectionListener(new TableSelectionListener() {
        public void onAllRowsDeselected(SourceTableSelectionEvents sender) {
          // Nothing to do here!
        }

        public void onCellHover(SourceTableSelectionEvents sender, int row,
            int cell) {
          // Nothing to do here!
        }

        public void onCellUnhover(SourceTableSelectionEvents sender, int row,
            int cell) {
          // Nothing to do here!
        }

        public void onRowDeselected(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowHover(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowUnhover(SourceTableSelectionEvents sender, int row) {
          // Nothing to do here!
        }

        public void onRowsSelected(SourceTableSelectionEvents sender,
            int firstRow, int numRows) {
          changeListeners.fireChange(ListBox.this);
        }
      });
    }
    changeListeners.add(listener);
  }

  public void removeChangeListener(ChangeListener listener) {
    if (changeListeners != null) {
      changeListeners.remove(listener);
    }
  }

  public PopupMenu getContextMenu() {
    return dataTable.getContextMenu();
  }

  public void setContextMenu(PopupMenu contextMenu) {
    dataTable.setContextMenu(contextMenu);
  }

  public int getTabIndex() {
    return impl.getTabIndex(getElement());
  }

  public void setAccessKey(char key) {
    impl.setAccessKey(getElement(), key);
  }

  public void setFocus(boolean focused) {
    if (focused) {
      impl.focus(getElement());
    } else {
      impl.blur(getElement());
    }
  }

  public void setTabIndex(int index) {
    impl.setTabIndex(getElement(), index);
  }

  private FocusListenerCollection focusListeners;

  public void addFocusListener(FocusListener listener) {
    if (focusListeners == null) {
      focusListeners = new FocusListenerCollection();
    }
    focusListeners.add(listener);
  }

  public void removeFocusListener(FocusListener listener) {
    if (focusListeners != null) {
      focusListeners.remove(listener);
    }
  }

  private KeyboardListenerCollection keyboardListeners;

  public void addKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners == null) {
      keyboardListeners = new KeyboardListenerCollection();
    }
    keyboardListeners.add(listener);
  }

  public void removeKeyboardListener(KeyboardListener listener) {
    if (keyboardListeners != null) {
      keyboardListeners.remove(listener);
    }
  }

  private ListModel<T> dataModel;

  /**
   * Sets the model that represents the contents of the {@code ListBox}, and
   * then clears the list's selection.
   * 
   * @param dataModel the {@link ListModel} that provides the list of items for
   *          display
   * @see #getModel
   */
  public void setModel(ListModel<T> dataModel) {
    if (this.dataModel != null) {
      this.dataModel.removeListDataListener(this);
    }
    dataTable.deselectRows();
    // dataTable.clear();
    this.dataModel = dataModel;
    if (this.dataModel != null) {
      this.dataModel.addListDataListener(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.list.ListDataListener#contentsChanged(org.gwt.mosaic.ui.client.list.ListDataEvent)
   */
  public void contentsChanged(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      System.out.println("contentsChanged:" + event);
      for (int i = event.getIndex0(), n = event.getIndex1(); i < n; ++i) {
        if (i < getItemCount()) {
          insertItem(dataModel.getElementAt(i), i);
        } else {
          addItem(dataModel.getElementAt(i));
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.list.ListDataListener#intervalAdded(org.gwt.mosaic.ui.client.list.ListDataEvent)
   */
  public void intervalAdded(ListDataEvent event) {
    if (dataModel == event.getSource()) {
      System.out.println("intervalAdded:" + event);
      for (int i = event.getIndex0(), n = event.getIndex1(); i <= n; ++i) {
        if (i < getItemCount()) {
          setItem(i, dataModel.getElementAt(i));
        } else {
          addItem(dataModel.getElementAt(i));
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.list.ListDataListener#intervalRemoved(org.gwt.mosaic.ui.client.list.ListDataEvent)
   */
  public void intervalRemoved(ListDataEvent event) {
    // TODO Auto-generated method stub
    System.out.println("interval removed");
  }

  public void setText(int row, int column, String text) {
    dataTable.setText(row, column, text);
  }

  public void setHTML(int row, int column, String html) {
    dataTable.setHTML(row, column, html);
  }

  public void setWidget(int row, int column, Widget widget) {
    dataTable.setWidget(row, column, widget);
  }

}
