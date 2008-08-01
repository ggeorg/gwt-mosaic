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


import java.util.Iterator;

import org.mosaic.ui.client.scrolltable.overrides.Grid;

/**
 * Allows bulk rendering of {@link Grid}s.
 * <p>
 * Must use the {@link Grid} in the overrides package.
 * </p>
 * 
 */

public class GridBulkRenderer extends TableBulkRenderer {

  /**
   * Grids need repair code after the table is constructed. The
   * {@link ChainingCallback} allows the bulk renderer to insert repair code
   * before the user's callback is called.
   */
  protected class ChainingCallback implements RendererCallback {
    private RendererCallback userCallback;

    ChainingCallback(RendererCallback userCallback) {
      this.userCallback = userCallback;
    }

    public void onRendered() {
      localOnRendered();
      if (this.userCallback != null) {
        this.userCallback.onRendered();
      }
    }

    protected void localOnRendered() {
      Grid table = (Grid) getTable();
      setGridDimensions(table);
    }
  }

  /**
   * Constructor.
   * 
   * @param grid Grid to be be bulk rendered
   */
  public GridBulkRenderer(Grid grid) {
    super(grid);
    if (grid instanceof FixedWidthGrid
        && (!(this instanceof FixedWidthGridBulkRenderer))) {
      throw new UnsupportedOperationException(
          "Must use a FixedWidthGridBulkLoader to bulk load a fixed grid");
    }
  }

  /**
   * Adds the chaining callback to the given options. Pulled out to allow
   * subclasses to override.
   * 
   * @param options options to add the chaining callback to
   */
  protected void addChainingCallback(final RenderingOptions options) {
    // Set the callback.
    options.callback = new ChainingCallback(options.callback);
  }

  @Override
  protected void renderRows(Iterator<Iterator<Object>> iterator,
      final RenderingOptions options) {
    addChainingCallback(options);

    // TODO long term we want to copy the generic algorithm and do some checks
    // that each row is the same length.
    // Render the rows.
    super.renderRows(iterator, options);
  }

  /**
   * Short term hack to set protected row and columns.
   */
  native void setGridDimensions(Grid table) /*-{
    var numRows =  table.@org.mosaic.ui.client.scrolltable.overrides.HTMLTable::getDOMRowCount()();
    table.@org.mosaic.ui.client.scrolltable.overrides.Grid::numRows = numRows;
    table.@org.mosaic.ui.client.scrolltable.overrides.Grid::numColumns =
      table.@org.mosaic.ui.client.scrolltable.overrides.HTMLTable::getDOMCellCount(I)(numRows - 1);
   }-*/;

}
