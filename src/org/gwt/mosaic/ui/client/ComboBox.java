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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event; // import
                                         // com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import org.gwt.mosaic.ui.client.ListBox.CellRenderer;
import org.gwt.mosaic.ui.client.list.ComboBoxModel;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ComboBox<T> extends ComboBoxBase<ListBox<T>> {

  private final ListBox<T> listBox;

  // private Timer updateTimer = new Timer() {
  // public void run() {
  // if (!isPopupVisible()) {
  // showPopup();
  // } else {
  // onShowPopup();
  // }
  // }
  // };

  /**
   * Default constructor.
   */
  public ComboBox() {
    this(null);
  }

  /**
   * 
   * @param columns
   */
  public ComboBox(String[] columns) {
    super();

    listBox = new ListBox<T>(columns) {

      @Override
      public void setElement(Element elem) {
        super.setElement(elem);
        sinkEvents(Event.ONMOUSEUP);
      }

      @Override
      public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (isPopupVisible()) {
          switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEUP:
              DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                  updateInput();
                }
              });
              return;
          }
        }
      }
    };

    setCellRenderer(new ComboBoxCellRenderer<T>() {
      public String getDisplayText(T item) {
        return item.toString();
      }

      public void renderCell(ListBox<T> listBox, int row, int column, T item) {
        if (item instanceof Widget) {
          listBox.setWidget(row, column, (Widget) item);
        } else {
          listBox.setText(row, column, item.toString());
        }
      }
    });

    setModel(new DefaultComboBoxModel<T>());

    init();
  }

  /**
   * Sets the data model that the {@code ComboBox} uses to obtain the list of
   * items.
   * 
   * @param model the {@code ComboBoxModel} that provides the displayed list of
   *          items
   */
  public void setModel(ComboBoxModel<T> model) {
    // ComboBoxModel oldModel = dataModel;
    // if (oldModel != null) {
    // oldModel.removeListDataListener(this);
    // }
    dataModel = model;
    //
    // // set the current selected item.
    // selectedItemReminder = dataModel.getSelectedItem();

    listBox.setModel(dataModel);

    // firePropertyChange("model", oldModel, dataModel);
  }

  /**
   * Returns the data model currently used by the <code>JComboBox</code>.
   * 
   * @return the <code>ComboBoxModel</code> that provides the displayed list of
   *         items
   */
  public ComboBoxModel<T> getModel() {
    return dataModel;
  }

  private ComboBoxModel<T> dataModel;

  protected void init() {
    listBox.setMultipleSelect(false);
    listBox.addStyleName("mosaic-ComboBoxList");
  }

  /**
   * The render used to set cell contents.
   * 
   * @param <T>
   */
  public interface ComboBoxCellRenderer<T> extends CellRenderer<T> {
    /**
     * 
     * @return
     */
    String getDisplayText(T item);
  }

  @Override
  protected void updateInput() {
    final int index = listBox.getSelectedIndex();
    if (index != -1) {
      if (listBox.getCellRenderer() != null) {
        final ComboBoxCellRenderer<T> renderer = (ComboBoxCellRenderer<T>) listBox.getCellRenderer();
        ComboBox.this.setText(renderer.getDisplayText(listBox.getItem(index)));
      } else {
        ComboBox.this.setText(listBox.getItem(index).toString());
      }
    }
    super.updateInput();
  }

  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONKEYDOWN:
        if (isPopupVisible()) {
          int keyCode = DOM.eventGetKeyCode(event);
          switch (keyCode) {
            case KeyCodes.KEY_UP:
            case KeyCodes.KEY_DOWN:
            case KeyCodes.KEY_LEFT:
            case KeyCodes.KEY_RIGHT:
              listBox.onBrowserEvent(event);
              return;
          }
        }
    }
    super.onBrowserEvent(event);
  }

  @Override
  protected boolean onHidePopup() {
    return true;
  }

  @Override
  protected ListBox<T> onShowPopup() {
    return listBox;
  }

  /**
   * Gets the number of items present in the list box.
   * 
   * @return the number of items
   */
  public int getItemCount() {
    return listBox.getItemCount();
  }

  public T getItem(int index) {
    return listBox.getItem(index);
  }

  /**
   * Gets the currently-selected item. If multiple items are selected, this
   * method will return the first selected item ({@link #isItemSelected(int)}
   * can be used to query individual items).
   * 
   * @return the selected index, or <code>-1</code> if none is selected
   */
  public int getSelectedIndex() {
    return listBox.getSelectedIndex();
  }

  /**
   * Inserts an item into the list box. Has the same effect as
   * 
   * <pre>
   * insertItem(item, item, index)
   * </pre>
   * 
   * @param item the text of the item to be inserted
   * @param index the index at which to insert it
   */
  public void insertItem(T item, int index) {
    listBox.renderItemOnInsert(item, index);
  }

  /**
   * Determines whether an individual list item is selected.
   * 
   * @param index the index of the item to be tested
   * @return <code>true</code> if the item is selected
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public boolean isItemSelected(int index) {
    return listBox.isItemSelected(index);
  }

  /**
   * Sets whether an individual list item is selected.
   * 
   * @param index the index of the item to be selected or unselected
   * @param selected <code>true</code> to select the item
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItemSelected(int index, boolean selected) {
    listBox.setItemSelected(index, selected);
  }

  /**
   * Sets the text associated with the item at a given index.
   * 
   * @param index the index of the item to be set
   * @param item the item's new text
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItem(int index, T item) {
    listBox.renderItemOnUpdate(index, item);
  }

  /**
   * Sets the currently selected index.
   * 
   * After calling this method, only the specified item in the list will remain
   * selected. For a ListBox with multiple selection enabled, see
   * {@link #setItemSelected(int, boolean)} to select multiple items at a time.
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    listBox.setSelectedIndex(index);
  }

  /**
   * Set the {@link CellRenderer} used to render cell contents.
   * 
   * @param cellRenderer the new renderer
   */
  public void setCellRenderer(ComboBoxCellRenderer<T> cellRenderer) {
    listBox.setCellRenderer(cellRenderer);
  }

  /**
   * Get the {@link CellRenderer} used to render cells.
   * 
   * @return the current renderer
   */
  public ComboBoxCellRenderer<T> getCellRenderer() {
    return (ComboBoxCellRenderer<T>) listBox.getCellRenderer();
  }

}
