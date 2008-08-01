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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Iterator;

/**
 * A cell editor that lists its options as radio buttons.
 * 
 * @param <R> the type of the row value associated with the editor
 */
public class RadioCellEditor<R> extends InlineCellEditor<R> {
  /**
   * The vertical panel used to layout the contents.
   */
  private VerticalPanel vpanel = new VerticalPanel();

  /**
   * Constructor.
   */
  public RadioCellEditor() {
    this(GWT.<InlineCellEditorImages> create(InlineCellEditorImages.class));
  }

  /**
   * Constructor.
   * 
   * @param images the images to use for the accept/cancel buttons
   */
  public RadioCellEditor(InlineCellEditorImages images) {
    this(new VerticalPanel(), images);
  }

  /**
   * Constructor.
   * 
   * @param images the images to use for the accept/cancel buttons
   */
  private RadioCellEditor(VerticalPanel vPanel, InlineCellEditorImages images) {
    super(vPanel, images);
    this.vpanel = vPanel;
  }

  /**
   * Add a radio button to the editor.
   * 
   * @param radio the radio button to add
   */
  public void addRadioButton(RadioButton radio) {
    vpanel.add(radio);
  }

  /**
   * @see AbstractCellEditor#editCell(CellEditInfo, Callback)
   */
  @Override
  public void editCell(CellEditInfo<R> cellEditInfo, Callback<R> callback) {
    super.editCell(cellEditInfo, callback);
    Iterator<Widget> it = vpanel.iterator();
    while (it.hasNext()) {
      RadioButton radio = (RadioButton) it.next();
      if (radio.isChecked()) {
        radio.setFocus(true);
        return;
      }
    }
  }

  /**
   * Remove a radio button to the editor.
   * 
   * @param radio the radio button to remove
   */
  public void removeRadioButton(RadioButton radio) {
    vpanel.remove(radio);
  }

  @Override
  protected Object getValue() {
    Iterator<Widget> it = vpanel.iterator();
    while (it.hasNext()) {
      RadioButton radio = (RadioButton) it.next();
      if (radio.isChecked()) {
        return radio.getText();
      }
    }
    return null;
  }

  @Override
  protected void setValue(Object value) {
    Iterator<Widget> it = vpanel.iterator();
    while (it.hasNext()) {
      RadioButton radio = (RadioButton) it.next();
      if (radio.getText().equals(value)) {
        radio.setChecked(true);
      } else {
        radio.setChecked(false);
      }
    }
  }
}
