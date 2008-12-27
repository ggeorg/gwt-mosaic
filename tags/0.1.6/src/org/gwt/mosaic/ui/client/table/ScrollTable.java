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
package org.gwt.mosaic.ui.client.table;

import java.util.Set;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.ColumnWidget;
import org.gwt.mosaic.ui.client.DoubleClickListener;
import org.gwt.mosaic.ui.client.DoubleClickListenerCollection;
import org.gwt.mosaic.ui.client.PopupMenu;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.ScrollTable.ScrollTableImages;

/**
 * <p>
 * ScrollTable consists of a fixed header and footer (optional) that remain
 * visible and a scrollable body that contains the data.
 * </p>
 * 
 * <p>
 * In order for the columns in the header table and data table to line up, the
 * two table must have the same margin, padding, and border widths. You can use
 * CSS style sheets to manipulate the colors and styles of the cell's, but you
 * must keep the actual sizes consistent (especially with respect to the left
 * and right side of the cells).
 * </p>
 * 
 * <h3>CSS Style Rules</h3> <ul class="css"> <li>.gwt-ScrollTable { applied to
 * the entire widget }</li> <li>.gwt-ScrollTable .headerTable { applied to the
 * header table } <li>.gwt-ScrollTable .dataTable { applied to the data table }
 * <li>.gwt-ScrollTable .footerTable { applied to the footer table } <li>
 * .gwt-ScrollTable .headerWrapper { wrapper around the header table }</li> <li>
 * .gwt-ScrollTable .dataWrapper { wrapper around the data table }</li> <li>
 * .gwt-ScrollTable .footerWrapper { wrapper around the footer table }</li>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ScrollTable extends ColumnWidget {

  public static class DataGrid extends FixedWidthGrid {

    private Event onMouseDownEvent = null;

    private DoubleClickListenerCollection doubleClickListeners;

    private PopupMenu contextMenu;

    public DataGrid() {
      super();
      // sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.KEYEVENTS);
    }

    public void addDoubleClickListener(DoubleClickListener listener) {
      if (doubleClickListeners == null) {
        doubleClickListeners = new DoubleClickListenerCollection();
        sinkEvents(Event.ONDBLCLICK);
      }
      doubleClickListeners.add(listener);
    }

    public PopupMenu getContextMenu() {
      return contextMenu;
    }

    @Override
    protected int getInputColumnWidth() {
      return super.getInputColumnWidth();
    }

    @Override
    protected void hoverCell(Element cellElem) {
      super.hoverCell(cellElem);
    }

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

    public void removeDoubleClickListener(DoubleClickListener listener) {
      if (doubleClickListeners != null) {
        doubleClickListeners.remove(listener);
      }
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

  }

  static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

  /**
   * The default style name.
   */
  public static final String DEFAULT_STYLE_NAME = "gwt-ScrollTable";

  private DoubleClickListenerCollection doubleClickListeners;

  /**
   * Constructor.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   */
  public ScrollTable(DataGrid dataTable, FixedWidthFlexTable headerTable) {
    super(dataTable, headerTable);
  }

  /**
   * Constructor.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   * @param images the images to use in the table
   */
  public ScrollTable(DataGrid dataTable, FixedWidthFlexTable headerTable,
      ScrollTableImages images) {
    super(impl.createFocusable(), dataTable, headerTable, images);

    sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
        | Event.ONFOCUS | Event.ONKEYDOWN);

    // Hide focus outline in Mozilla/Webkit/Opera
    DOM.setStyleAttribute(getElement(), "outline", "0px");

    // Hide focus outline in IE 6/7
    DOM.setElementAttribute(getElement(), "hideFocus", "true");

    setStylePrimaryName(DEFAULT_STYLENAME);
  }

  public void addDoubleClickListener(DoubleClickListener listener) {
    if (doubleClickListeners == null) {
      doubleClickListeners = new DoubleClickListenerCollection();
      getDataTable().addDoubleClickListener(new DoubleClickListener() {
        public void onDoubleClick(Widget sender) {
          doubleClickListeners.fireDblClick(ScrollTable.this);
        }
      });
    }
    doubleClickListeners.add(listener);
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= getDataTable().getRowCount()) {
      throw new IndexOutOfBoundsException();
    }
  }

  private void eatEvent(Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);
  }

  public PopupMenu getContextMenu() {
    return getDataTable().getContextMenu();
  }

  @Override
  public DataGrid getDataTable() {
    return (DataGrid) super.getDataTable();
  }

  @Override
  protected int getInputColumnWidth() {
    return ((DataGrid) getDataTable()).getInputColumnWidth();
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this
   * method will returns the first selected item ({@link #isItemeSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or {@code -1} if none is selected
   */
  public int getSelectedIndex() {
    Set<Integer> selection = getDataTable().getSelectedRows();
    for (Integer i : selection) {
      return i.intValue();
    }
    return -1;
  }

  @Override
  protected void hoverCell(Element cellElem) {
    ((DataGrid) getDataTable()).hoverCell(cellElem);
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
    selectPrevItemItem();
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
        impl.focus(getElement());
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
            DOM.scrollIntoView((Element) getDataTable().getRowFormatter().getElement(
                getSelectedIndex()).getFirstChild());
            break;
          case KeyboardListener.KEY_RIGHT:
            DOM.scrollIntoView((Element) getDataTable().getRowFormatter().getElement(
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

  public void removeDoubleClickListener(DoubleClickListener listener) {
    if (doubleClickListeners != null) {
      doubleClickListeners.remove(listener);
    }
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

  private void selectNextItem() {
    int index = getSelectedIndex();
    if (index == -1) {
      return;
    }

    if (index < getDataTable().getRowCount() - 1) {
      setSelectedIndex(++index);
    }

    DOM.scrollIntoView((Element) getDataTable().getRowFormatter().getElement(
        getSelectedIndex()).getFirstChild());
  }

  private void selectPrevItemItem() {
    int index = getSelectedIndex();
    if (index == -1) {
      return;
    }

    if (index > 0) {
      setSelectedIndex(--index);
    }

    DOM.scrollIntoView((Element) getDataTable().getRowFormatter().getElement(
        getSelectedIndex()).getFirstChild());
  }

  public void setContextMenu(PopupMenu contextMenu) {
    getDataTable().setContextMenu(contextMenu);
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
    getDataTable().selectRow(index, true);
  }

}
