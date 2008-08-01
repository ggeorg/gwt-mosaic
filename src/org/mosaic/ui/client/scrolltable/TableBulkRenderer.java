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

import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.IncrementalCommand;

import java.util.Iterator;

import org.mosaic.ui.client.scrolltable.overrides.HTMLTable;

/**
 * Enables bulk rendering of tables. Each subclass that needs special handling
 * for bulk rendering should have its own bulk renderer.
 */
public abstract class TableBulkRenderer {

  /**
   * Controls how each cell is rendered. The default implementation calls
   * toString on the cell data.
   * 
   */
  public abstract static class CellRenderer {

    /**
     * Renders the current cell data calling <code>append</code> as needed .
     * 
     * @param row cell's row
     * @param column cell's column
     * @param cellData data associated with cell
     * @param accum the string buffer to append to
     */
    public abstract void renderCell(int row, int column, Object cellData,
        StringBuffer accum);
  }

  /**
   * Controls how each cell is rendered. The default implementation calls
   * toString on the cell data.
   */
  public static class StringCellRenderer extends CellRenderer {

    /**
     * Renders the current cell data to a {@link String}.
     * 
     * @param row cell's row
     * @param column cell's column
     * @param cellData data associated with cell
     * @param accum the string buffer to append too
     */
    @Override
    public void renderCell(int row, int column, Object cellData, StringBuffer accum) {
      accum.append(cellData.toString());
    }
  }

  /**
   * Convenience class used to specify rendering options for the table.
   */
  protected class RenderingOptions {
    public int startRow = 0;
    public int numRows = TableModel.ALL_ROWS;
    public boolean syncCall = false;
    public String headerRow = null;
    public RendererCallback callback = null;

    public String getEndCell(int column) {
      return "</td>";
    }

    public String getStartCell(int column) {
      return "<td>";
    }
  }

  /**
   * Time slice in milliseconds that the construction of the string can take
   * before flushing the event cue.
   */
  public static int TIME_SLICE = 1000;

  /**
   * Maximum time allowable to create the table string. Ever.
   */
  public static int MAX_TIME = 20000;

  /**
   * How many rows should be processed before time is checked and the event loop
   * is potentially flushed.
   */
  // TODO check how long time check takes to see if this guard is worth having.
  public static int ROWS_PER_TIME_CHECK = 10;

  /**
   * The default string renderer.
   */
  private static CellRenderer DEFAULT_RENDERER = new StringCellRenderer();

  /**
   * Scratch Element used to render table and row strings.
   */
  private static final Element WRAPPER_DIV = DOM.createElement("div");

  /**
   * Stamp used to detect when a request has been orphaned.
   */
  private int requestStamp = 0;

  /**
   * Table to be bulk rendered.
   */
  private final HTMLTable table;

  /**
   * The renderer used to render cell data.
   */
  private CellRenderer renderer;

  /**
   * Constructor for the bulk renderer.
   * 
   * @param table the table to be bulk rendered
   */
  public TableBulkRenderer(HTMLTable table) {
    this(table, DEFAULT_RENDERER);
  }

  /**
   * Constructor for the bulk renderer.
   * 
   * @param table the table to be bulk rendered
   * @param renderer the renderer that should be used during bulk rendering
   */
  public TableBulkRenderer(HTMLTable table, CellRenderer renderer) {
    this.table = table;
    this.setCellRenderer(renderer);
  }

  /**
   * Gets the cell renderer.
   * 
   * @return the renderer
   */
  public CellRenderer getCellRenderer() {
    return renderer;
  }

  /**
   * Removes all rows in the current table replaces them with the rows provided.
   * <p>
   * This method should only be used when the number of rows is known and of
   * reasonable size, therefore this call is synchronous by default.
   * </p>
   * 
   * @param rows {@link Iterable} of rows. Each row itself must be another
   *            {@link Iterable}.
   */
  public final void renderRows(Iterable<? extends Iterable> rows) {
    renderRows(rows, null);
  }

  /**
   * Removes all rows in the current table replaces them with the rows provided.
   * <p>
   * This method should only be used when the number of rows is known and of
   * reasonable size, therefore this call is synchronous by default.
   * </p>
   * 
   * @param rows list of rows. Each row itself must be another list
   * @param callback callback to be called after the rows are rendered
   */
  public final void renderRows(Iterable<? extends Iterable> rows,
      RendererCallback callback) {
    IterableTableModel tableModel = new IterableTableModel(rows);
    RenderingOptions options = createRenderingOptions();
    options.syncCall = true;
    options.callback = callback;
    renderRows(tableModel, options);
  }

  /**
   * Removes all rows in the current table replaces them with the roews supplied
   * by the iterator. Each element of the rows iterator is an {@link Iterator}
   * which represents a single row.
   * 
   * @param rows iterator of row iterators
   * @param callback callback to be called after the rows are rendered
   */
  public final void renderRows(Iterator<Iterator<Object>> rows, RendererCallback callback) {
    RenderingOptions options = createRenderingOptions();
    options.callback = callback;
    renderRows(rows, options);
  }

  /**
   * Removes all rows in the current table and replaces them with the rows
   * supplied by the provided {@link TableModel}.
   * 
   * @param tableModel the table data
   * @param startRow the tableModel's start row index
   * @param numRows the number of rows to request from the tableModel -1
   *            indicates all of them *
   * @param callback callback to call after the table is finished being rendered
   */
  public final void renderRows(TableModel<?> tableModel, int startRow, int numRows,
      RendererCallback callback) {
    RenderingOptions options = createRenderingOptions();
    options.startRow = startRow;
    options.numRows = numRows;
    options.callback = callback;
    renderRows(tableModel, options);
  }

  /**
   * Removes all rows in the current table and replaces them with the rows
   * supplied by the provided {@link TableModel}.
   * 
   * @param tableModel the table model
   * @param callback callback to call after the table is finished being rendered
   */
  public final void renderRows(TableModel<?> tableModel, RendererCallback callback) {
    renderRows(tableModel, 0, TableModel.ALL_ROWS, callback);
  }

  /**
   * Sets the cell renderer.
   * 
   * @param renderer the renderer to set
   */
  public void setCellRenderer(CellRenderer renderer) {
    this.renderer = renderer;
  }

  /**
   * Creates the rendering options associated with this renderer.
   * 
   * @return the rendering options
   */
  protected RenderingOptions createRenderingOptions() {
    return new RenderingOptions();
  }

  /**
   * Gets the table.
   * 
   * @returns the current html table.
   */
  protected HTMLTable getTable() {
    return table;
  }

  /**
   * Work horse protected rendering method.
   * 
   * @param rows Iterator of row iterators
   * @param options rendering options for this table
   */
  protected void renderRows(final Iterator<Iterator<Object>> rows,
      final RenderingOptions options) {
    final StringBuffer temp = new StringBuffer();
    temp.append("<table><tbody>");
    if (options.headerRow != null) {
      temp.append(options.headerRow);
    }
    final double endTime = Duration.currentTimeMillis() + MAX_TIME;

    class RenderTableCommand implements IncrementalCommand {
      int rowIndex = 0;
      int myStamp = ++requestStamp;

      public boolean execute() {
        // Poor man's cancel() event.
        if (myStamp != requestStamp) {
          return false;
        }
        int checkRow = ROWS_PER_TIME_CHECK;
        double endSlice = Duration.currentTimeMillis() + TIME_SLICE;
        for (; rows.hasNext(); ++rowIndex) {
          if (options.syncCall == false && --checkRow == 0) {
            checkRow = ROWS_PER_TIME_CHECK;
            double time = Duration.currentTimeMillis();
            if (time > endSlice) {
              // We only check endTime at each slice.
              if (time > endTime) {
                throw new IllegalStateException(
                    "Cannot bulk render a table which takes more than " + MAX_TIME
                        + " milliseconds to render");
              }
              return true;
            }
          }
          temp.append("<tr>");
          Iterator<Object> row = rows.next();
          for (int cellIndex = 0; row.hasNext(); ++cellIndex) {
            temp.append(options.getStartCell(cellIndex));
            Object next = row.next();
            getCellRenderer().renderCell(rowIndex, cellIndex, next, temp);
            temp.append(options.getEndCell(cellIndex));
          }
          temp.append("</tr>");
        }
        temp.append("</tbody> </table>");
        renderRows(temp.toString());
        if (options.callback != null) {
          options.callback.onRendered();
        }
        return false;
      }
    }
    RenderTableCommand renderTable = new RenderTableCommand();
    if (renderTable.execute()) {
      DeferredCommand.addCommand(renderTable);
    }
  }

  /**
   * Render rows using a table model.
   * 
   * @param tableModel table model
   * @param options options
   */
  protected final void renderRows(TableModel<?> tableModel, final RenderingOptions options) {

    // Create a callback to handle the request
    TableModel.Callback requestCallback = new TableModel.Callback() {
      public void onFailure(Throwable caught) {
      }

      public void onRowsReady(TableModel.Request request,
          final TableModel.Response response) {
        final Iterator<Iterator<Object>> rows = response.getIterator();
        renderRows(rows, options);
      }
    };

    tableModel.requestRows(new TableModel.Request(options.startRow, options.numRows),
        requestCallback);
  }

  private void renderRows(String rawHTMLTable) {
    DOM.setInnerHTML(WRAPPER_DIV, rawHTMLTable);
    Element tableElement = DOM.getFirstChild(WRAPPER_DIV);
    Element newBody = renderRowsImpl(table.getElement(), tableElement);
    setBodyElement(table, newBody);
  }

  /**
   * Helper native method for <code>initRows</code>.
   */
  private native Element renderRowsImpl(Element table, Element thatBody) /*-{ 
     table.removeChild(table.tBodies[0]);
     var thatChild = thatBody.tBodies[0];
     table.appendChild(thatChild);
     return thatChild;
   }-*/;

  /**
   * Short term hack to get protected setBodyElement.
   */
  private native void setBodyElement(HTMLTable table, Element newBody) /*-{
     table.@org.mosaic.ui.client.scrolltable.overrides.HTMLTable::setBodyElement(Lcom/google/gwt/user/client/Element;)(newBody);
   }-*/;

}
