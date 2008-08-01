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

import com.google.gwt.user.client.ui.ListBox;

/**
 * A cell editor that lists its options in a drop down box.
 * 
 * @param <R> the type of the row value associated with the editor
 */
public class ListCellEditor<R> extends InlineCellEditor<R> {
  /**
   * The list box of options.
   */
  private ListBox listBox;

  /**
   * Constructor.
   */
  public ListCellEditor() {
    this(new ListBox());
  }

  /**
   * Constructor.
   * 
   * @param listBox a custom {@link ListBox} to use
   */
  public ListCellEditor(ListBox listBox) {
    super(listBox);
    this.listBox = listBox;
  }

  /**
   * Constructor.
   * 
   * @param listBox a custom {@link ListBox} to use
   * @param images the images to use for the accept/cancel buttons
   */
  public ListCellEditor(ListBox listBox, InlineCellEditorImages images) {
    super(listBox, images);
    this.listBox = listBox;
  }

  /**
   * @see AbstractCellEditor#editCell(CellEditInfo, Callback)
   */
  @Override
  public void editCell(CellEditInfo<R> cellEditInfo,
      Callback<R> callback) {
    super.editCell(cellEditInfo, callback);
    listBox.setFocus(true);
  }

  /**
   * Get the {@link ListBox} of options.
   * 
   * @return the listBox
   */
  public ListBox getListBox() {
    return listBox;
  }

  @Override
  protected Object getValue() {
    return listBox.getValue(listBox.getSelectedIndex());
  }

  @Override
  protected void setValue(Object value) {
    int numItems = listBox.getItemCount();
    for (int i = 0; i < numItems; i++) {
      if (listBox.getValue(i).equals(value)) {
        listBox.setSelectedIndex(i);
        return;
      }
    }
  }
}
