/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.override.client.HTMLTable;
import org.gwt.mosaic.ui.client.LayoutPopupPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * An abstract representation of an editor used to edit the contents of a cell.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.mosaic-SimpleInlineCellEditor</dt>
 * <dd>applied to the entire widget</dt>
 * 
 * </dl>
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <ColType> the data type of the column
 * 
 */
public abstract class SimpleInlineCellEditor<ColType> extends LayoutPopupPanel implements
    CellEditor<ColType> {

  /** Default style name. */
  private static final String DEFAULT_STYLENAME = "mosaic-SimpleInlineCellEditor";

  /** The current {@link CellEditor.Callback}. */
  private Callback<ColType> curCallback = null;

  /** The current {@link CellEditor.CellEditInfo}. */
  private CellEditInfo curCellEditInfo = null;

  /**
   * Construct a new {@code SimpleInlineCellEditor}.
   * 
   * @param content the {@link Widget} used to edit
   */
  protected SimpleInlineCellEditor(Widget content) {
    super(true, true);
    setStylePrimaryName(DEFAULT_STYLENAME);

    setWidget(content);
  }

  public void editCell(CellEditInfo cellEditInfo, ColType cellValue,
      CellEditor.Callback<ColType> callback) {
    // Save the current values
    curCallback = callback;
    curCellEditInfo = cellEditInfo;

    // Get the info about the cell
    HTMLTable table = curCellEditInfo.getTable();
    int row = curCellEditInfo.getRowIndex();
    int cell = curCellEditInfo.getCellIndex();

    // Get the location of the cell
    Element cellElem = table.getCellFormatter().getElement(row, cell);
    int top = DOM.getAbsoluteTop(cellElem);
    int left = DOM.getAbsoluteLeft(cellElem);
    int width = cellElem.getOffsetWidth();
    int height = cellElem.getOffsetHeight();
    setPixelSize(width, height);
    setPopupPosition(left, top);

    // Set the current value
    setValue(cellValue);

    // Show the editor
    show();
  }

  /**
   * Accept the contents of the cell editor as the new cell value.
   */
  protected void accept() {
    // Check if we are ready to accept
    if (!onAccept()) {
      return;
    }

    // Get the value before hiding the editor
    ColType cellValue = getValue();

    // Hide the editor
    hide();

    // Send the new cell value to the callback
    curCallback.onComplete(curCellEditInfo, cellValue);
    curCallback = null;
    curCellEditInfo = null;
  }
  
  /**
   * Called before an accept takes place.
   * 
   * @return true to allow the accept, false to prevent it
   */
  protected boolean onAccept() {
    return true;
  }

  /**
   * Cancel the cell edit.
   */
  protected void cancel() {
    // Fire the event
    if (!onCancel()) {
      return;
    }

    // Hide the popup
    hide();

    // Call the callback
    if (curCallback != null) {
      curCallback.onCancel(curCellEditInfo);
      curCellEditInfo = null;
      curCallback = null;
    }
  }

  /**
   * Called before a cancel takes place.
   * 
   * @return true to allow the cancel, false to prevent it
   */
  protected boolean onCancel() {
    return true;
  }

  /**
   * Get the new cell value from the editor.
   * 
   * @return the new cell value
   */
  protected abstract ColType getValue();
  
  /**
   * Set the cell value in the editor.
   * 
   * @param cellValue the value in the cell
   */
  protected abstract void setValue(ColType cellValue);
}
