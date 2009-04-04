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

import org.gwt.mosaic.ui.client.list.ComboBoxModel;
import org.gwt.mosaic.ui.client.list.DefaultComboBoxModel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ComboBox<T> extends ComboBoxBase<ListBox<T>> {

  private final ListBox<T> listBox = new ListBox<T>() {

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
            updateInput();
            return;
        }
      }
    }
  };

  private Timer updateTimer = new Timer() {
    public void run() {
      if (!isPopupVisible()) {
        showPopup();
      } else {
        onShowPopup();
      }
    }
  };

  /**
   * Default constructor.
   */
  public ComboBox() {
    super();
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

  @Override
  protected void updateInput() {
    final int index = listBox.getSelectedIndex();
    if (index != -1) {
      ComboBox.this.setText("" + listBox.getItem(index));
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
            case KeyboardListener.KEY_UP:
            case KeyboardListener.KEY_DOWN:
            case KeyboardListener.KEY_LEFT:
            case KeyboardListener.KEY_RIGHT:
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
   * <p>
   * Note that setting the selection programmatically does <em>not</em> cause
   * the {@link ChangeListener#onChange(Widget)} event to be fired.
   * </p>
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
   * <p>
   * Note that setting the selected index programmatically does <em>not</em>
   * cause the {@link ChangeListener#onChange(Widget)} event to be fired.
   * </p>
   * 
   * @param index the index of the item to be selected
   */
  public void setSelectedIndex(int index) {
    listBox.setSelectedIndex(index);
  }

}
