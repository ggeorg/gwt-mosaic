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

import org.mosaic.ui.client.scrolltable.overrides.HTMLTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * An abstract representation of an editor used to edit the contents of a cell.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li> .gwt-InlineCellEditor { applied to the entire widget } </li>
 * <li> .gwt-InlineCellEditor .accept { applied to the accept image } </li>
 * <li> .gwt-InlineCellEditor .cancel { applied to the cancel image } </li>
 * </ul>
 * 
 * @param <R> the type of the row value associated with the editor
 */
public abstract class InlineCellEditor<R> extends PopupPanel implements
    AbstractCellEditor<R> {
  /**
   * An {@link ImageBundle} that provides images for {@link InlineCellEditor}.
   */
  public static interface InlineCellEditorImages extends ImageBundle {
    /**
     * An image used to fill the available width.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype cellEditorAccept();

    /**
     * An image indicating that a column is sorted in ascending order.
     * 
     * @return a prototype of this image
     */
    AbstractImagePrototype cellEditorCancel();
  }

  /**
   * <code>ClickDecoratorPanel</code> decorates any widget with the minimal
   * amount of machinery to receive clicks for delegation to the parent.
   */
  private static final class ClickDecoratorPanel extends SimplePanel {
    private ClickListener delegate;

    public ClickDecoratorPanel(Widget child, ClickListener delegate) {
      this.delegate = delegate;
      setWidget(child);
      sinkEvents(Event.ONCLICK);
    }

    @Override
    public void onBrowserEvent(Event event) {
      // No need for call to super.
      switch (DOM.eventGetType(event)) {
        case Event.ONCLICK:
          delegate.onClick(this);
      }
    }
  }

  /**
   * Default style name.
   */
  public static final String DEFAULT_STYLENAME = "gwt-InlineCellEditor";

  /**
   * The click listener used to accept.
   */
  private ClickListener cancelListener = new ClickListener() {
    public void onClick(Widget sender) {
      cancel();
    }
  };

  /**
   * The click listener used to accept.
   */
  private ClickListener acceptListener = new ClickListener() {
    public void onClick(Widget sender) {
      accept();
    }
  };

  /**
   * The current callback.
   */
  private Callback<R> curCallback = null;

  /**
   * The current cell edit info.
   */
  private CellEditInfo<R> curCellEditInfo = null;

  /**
   * The main grid used for layout.
   */
  private FlexTable layoutTable;

  /**
   * Constructor.
   * 
   * @param content the {@link Widget} used to edit
   */
  protected InlineCellEditor(Widget content) {
    this(content,
        GWT.<InlineCellEditorImages> create(InlineCellEditorImages.class));
  }

  /**
   * Constructor.
   * 
   * @param content the {@link Widget} used to edit
   * @param images the images to use for the accept/cancel buttons
   */
  protected InlineCellEditor(Widget content, InlineCellEditorImages images) {
    super(true, true);
    setStylePrimaryName(DEFAULT_STYLENAME);

    // Wrap contents in a table
    layoutTable = new FlexTable();
    FlexCellFormatter formatter = layoutTable.getFlexCellFormatter();
    layoutTable.setCellSpacing(0);
    setWidget(layoutTable);

    // Add a label
    setLabel("");
    formatter.setColSpan(0, 0, 3);

    // Add content widget
    layoutTable.setWidget(1, 0, content);

    // Add accept and cancel buttons
    setAcceptWidget(images.cellEditorAccept().createImage());
    setCancelWidget(images.cellEditorCancel().createImage());
  }

  /**
   * @see AbstractCellEditor#editCell(CellEditInfo, Callback)
   */
  public void editCell(CellEditInfo<R> cellEditInfo, Callback<R> callback) {
    // Save the current values
    curCallback = callback;
    curCellEditInfo = cellEditInfo;

    // Get the info about the cell
    HTMLTable table = curCellEditInfo.getTable();
    int row = curCellEditInfo.getRow();
    int cell = curCellEditInfo.getCell();

    // Get the location of the cell
    Element cellElem = table.getCellFormatter().getElement(row, cell);
    int top = DOM.getAbsoluteTop(cellElem);
    int left = DOM.getAbsoluteLeft(cellElem);
    setPopupPosition(left, top);

    // Set the current value
    Widget widget = table.getWidget(row, cell);
    if (widget == null) {
      setValue(table.getHTML(row, cell));
    } else {
      setValue(widget);
    }

    // Show the editor
    show();
  }

  /**
   * @return the label text
   */
  public String getLabel() {
    return layoutTable.getHTML(0, 0);
  }

  /**
   * Set the label for this cell editor.
   * 
   * @param label the new label
   */
  public void setLabel(String label) {
    layoutTable.setHTML(0, 0, label);
  }

  /**
   * Save the value in the cell editor to the actual cell.
   */
  protected void accept() {
    // Check if we are ready to accept
    if (!onAccept()) {
      return;
    }

    // Get the value
    Object value = getValue();

    // Hide the popup
    hide();

    // Set the cell's contents
    curCallback.onComplete(curCellEditInfo, value);
    curCallback = null;
    curCellEditInfo = null;
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
   * @return the Widget that is used to accept the current value.
   */
  protected Widget getAcceptWidget() {
    ClickDecoratorPanel clickPanel = (ClickDecoratorPanel) layoutTable.getWidget(
        1, 1);
    return clickPanel.getWidget();
  }

  /**
   * @return the Widget that is used to cancel editing.
   */
  protected Widget getCancelWidget() {
    ClickDecoratorPanel clickPanel = (ClickDecoratorPanel) layoutTable.getWidget(
        1, 2);
    return clickPanel.getWidget();
  }

  /**
   * @return the content widget
   */
  protected Widget getContentWidget() {
    return layoutTable.getWidget(1, 0);
  }

  /**
   * Get the value from the editor to put into the cell. If the value is a
   * Widget, it will be set as a Widget in the table. If it is not a Widget, it
   * will be set as an HTML string.
   * 
   * @return the new value to put into the cell
   */
  protected abstract Object getValue();

  /**
   * Called before an accept takes place.
   * 
   * @return true to allow the accept, false to prevent it
   */
  protected boolean onAccept() {
    return true;
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
   * Set the Widget that is used to accept the current value.
   * 
   * @param w the widget
   */
  protected void setAcceptWidget(Widget w) {
    ClickDecoratorPanel clickPanel = new ClickDecoratorPanel(w, acceptListener);
    clickPanel.setStyleName("accept");
    layoutTable.setWidget(1, 1, clickPanel);
  }

  /**
   * Set the Widget that is used to cancel editing.
   * 
   * @param w the widget
   */
  protected void setCancelWidget(Widget w) {
    ClickDecoratorPanel clickPanel = new ClickDecoratorPanel(w, cancelListener);
    clickPanel.setStyleName("cancel");
    layoutTable.setWidget(1, 2, clickPanel);
  }

  /**
   * Set the value in the editor based on the value in the cell.
   * 
   * @param value the value in the cell, either a Widget or HTML string
   */
  protected abstract void setValue(Object value);
}
