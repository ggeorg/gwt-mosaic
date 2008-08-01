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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import java.util.Iterator;

import org.mosaic.ui.client.scrolltable.overrides.HTMLTable;

/**
 * Helper class to bulk load {@link FixedWidthGrid} tables.
 */
public class FixedWidthGridBulkRenderer extends GridBulkRenderer {

  /**
   * Chaining callback for fixed width grid.
   */
  protected class ChainingCallback extends GridBulkRenderer.ChainingCallback {
    RendererCallback userCallback;

    ChainingCallback(RendererCallback userCallback) {
      super(userCallback);
    }

    @Override
    public void localOnRendered() {
      // We're saving an extra object creation by calling super rather than
      // chaining them.
      super.localOnRendered();
      Element newGhostRow = getBulkLoadedGhostRow(getTable());

      // Set the ghost row to point at correct location
      ((FixedWidthGrid) getTable()).setGhostRow(newGhostRow);
      if (userCallback != null) {
        userCallback.onRendered();
      }
    }
  }

  private int numColumns;
  private String[] colStarts;
  private String[] colEnds;

  private RenderingOptions options = new RenderingOptions() {
    @Override
    public String getEndCell(int column) {
      return colEnds[column];
    }

    @Override
    public String getStartCell(int column) {
      return colStarts[column];
    }
  };

  /**
   * Constructor. Takes in the number of columns in the table to allow efficient
   * creation of the header row.
   * 
   * @param table table to be bulk rendered
   * @param numColumns number of columns in table
   */
  public FixedWidthGridBulkRenderer(FixedWidthGrid table, int numColumns) {
    super(table);
    this.numColumns = numColumns;
    table.resizeColumns(numColumns);
    initCustomRenderValues();
  }

  /**
   * Sets all the style names for cells in a given column.
   * 
   * @param column the column
   * @param styleName the style name
   */
  public void setStyleNameByColumn(int column, String styleName) {
 
    colStarts[column] = "<td class='" + styleName + "'><span>";
  }

  @Override
  protected void addChainingCallback(final RenderingOptions options) {
    // Set the callback.
    options.callback = new ChainingCallback(options.callback);
  }

  @Override
  protected RenderingOptions createRenderingOptions() {
    initCustomRenderValues();
    return options;
  }

  /**
   * Gets the new ghost element from the table.
   * 
   * @param table the table
   * @return the new ghost row
   */
  protected native Element getBulkLoadedGhostRow(HTMLTable table)
  /*-{
    return table.@org.mosaic.ui.client.scrolltable.overrides.HTMLTable::getBodyElement()(table).rows[0];
  }-*/;

  @Override
  protected void renderRows(Iterator<Iterator<Object>> iterator,
      final RenderingOptions options) {

    FixedWidthGrid table = (FixedWidthGrid) super.getTable();
    if (table.getColumnCount() == 0) {
      throw new IllegalStateException(
          "Bulk Rendered FixedWidthGrids must call resizeColumns() before being loaded");
    }
    table.updateGhostRow();
    options.headerRow = DOM.toString(table.getGhostRow());
    super.renderRows(iterator, options);
  }

  protected void setColumnBeginAndEnd(int column, String start, String end) {
    colStarts[column] = start;
    colEnds[column] = end;
  }

  private void initCustomRenderValues() {
    if (colStarts == null) {
      colStarts = new String[numColumns];
      for (int i = 0; i < numColumns; i++) {
        colStarts[i] = "<td>";
      }

      colEnds = new String[numColumns];
      for (int i = 0; i < numColumns; i++) {
        colEnds[i] = "</td>";
      }
    }
  }
}
