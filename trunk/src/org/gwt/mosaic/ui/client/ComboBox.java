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
package org.gwt.mosaic.ui.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ComboBox<T> extends ComboBoxBase<ListBox<T>> {

  private final ListBox<T> listBox;

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
    this(new ListBox<T>());
  }

  protected ComboBox(final ListBox<T> listBox) {
    super();

    this.listBox = listBox;
    this.listBox.setMultipleSelect(false);
    //this.listBox.setVisibleItemCount(10);
    this.listBox.addStyleName("mosaic-ComboBoxList");

    super.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        switch (keyCode) {
          case KEY_ENTER:
          case KEY_TAB:
          case KEY_ESCAPE:
          case KEY_UP:
            break;
          default:
            updateTimer.schedule(333);
        }
      }
    });

    listBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        setText((String)listBox.getItem(listBox.getSelectedIndex()));
        hidePopup();
      }
    });
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
   * Adds an item to the list box. This method has the same effect as
   * 
   * <pre>
   * addItem(item, item)
   * </pre>
   * 
   * @param item the text of the item to be added
   */
  public void addItem(T item) {
    listBox.addItem(item);
  }

  /**
   * Removes all items from the list box.
   */
  public void clear() {
    listBox.clear();
    setText("");
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
   * Gets the number of items that are visible. If only one item is visible,
   * then the box will be displayed as a drop-down list.
   * 
   * @return the visible item count
   */
//  public int getVisibleItemCount() {
//    return listBox.getVisibleItemCount();
//  }

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
    listBox.insertItem(item, index);
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
   * Removes the item at the specified index.
   * 
   * @param index the index of the item to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void removeItem(int index) {
    listBox.removeItem(index);
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
   * @param text the item's new text
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public void setItem(int index, T item) {
    listBox.setItem(index, item);
  }
  
  /**
   * Sets the currently selected index.
   * 
   * After calling this method, only the specified item in the list will
   * remain selected.  For a ListBox with multiple selection enabled, see
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
  
  /**
   * Sets the number of items that are visible. If only one item is visible,
   * then the box will be displayed as a drop-down list.
   * 
   * @param visibleItems the visible item count
   */
//  public void setVisibleItemCount(int visibleItems) {
//    if (visibleItems < 2) {
//      throw new IllegalArgumentException();
//    }
//    listBox.setVisibleItemCount(visibleItems);
//  }

}
