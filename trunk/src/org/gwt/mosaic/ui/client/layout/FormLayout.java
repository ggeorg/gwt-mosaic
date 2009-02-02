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
package org.gwt.mosaic.ui.client.layout;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated Replaced by org.gwt.mosaic.forms.client.layout.FormLayout
 */
public class FormLayout extends GridLayout {

  private double[] colSpecs;

  private double[] rowSpecs;

  public FormLayout(double[] colSpecs, double[] rowSpecs) {
    super(colSpecs.length, rowSpecs.length);
    this.colSpecs = colSpecs;
    this.rowSpecs = rowSpecs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.
   * mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};

    try {
      if (layoutPanel == null) {
        return result;
      }

      buildWidgetMatrix(layoutPanel);

      for (int r = 0; r < getRows(); r++) {
        int cellWidth = 0;
        int cellHeight = 0;
        double rowSpec = rowSpecs[r];
        for (int c = 0; c < getColumns(); c++) {
          Widget widget = widgetMatrix[c][r];
          if (widget == null || widget == SPAN) {
            continue;
          }
          if (widget instanceof DecoratorPanel) {
            widget = ((DecoratorPanel) widget).getWidget();
          }

          GridLayoutData layoutData = (GridLayoutData) getLayoutData(widget);

          double colSpec = colSpecs[c];
          if (colSpec >= 0.0 && colSpec < 1.0) {
            // cellWidth = Math.max(cellWidth, (int) Math.ceil(width * colSpec
            // / (double) layoutData.colspan));
          } else if (colSpec != -1 && colSpec > 0) {
            cellWidth = Math.max(cellWidth, (int) Math.ceil(colSpec
                / (double) layoutData.colspan));
          } else if (colSpec != -2) {
            int flowWidth = getFlowWidth(widget);
            cellWidth = Math.max(cellWidth, (int) Math.ceil((double) flowWidth
                / (double) layoutData.colspan));
          }

          if (rowSpec == -1) {
            int flowHeight = getFlowHeight(widget);
            cellHeight = Math.max(cellHeight,
                (int) Math.ceil((double) flowHeight
                    / (double) layoutData.rowspan));
          }
        }
        result[0] = Math.max(result[0], cellWidth);
        result[1] = Math.max(result[1], cellHeight);
      }

      result[0] *= getColumns();
      int h = 0;
      for (int r = 0; r < getRows(); r++) {
        double rowSpec = rowSpecs[r];
        if (rowSpec >= 0.0 && rowSpec < 1.0) {
          h += result[1] * getRows() * rowSpec;
        } else if (rowSpec >= 1.0) {
          h += rowSpec;
        } else {
          h += result[1];
        }
      }
      result[1] = h;

      final int[] margins = DOM.getMarginSizes(layoutPanel.getElement());
      result[0] += (margins[1] + margins[3]);
      result[1] += (margins[0] + margins[2]);

      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());
      result[0] += (paddings[1] + paddings[3]);
      result[1] += (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();
      result[0] += ((getColumns() - 1) * spacing);
      result[1] += ((getRows() - 1) * spacing);
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    layoutPanel.setPreferredSize(result[0], result[1]);

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.gwt.mosaic
   * .ui.client.layout.LayoutPanel)
   */
  @Override
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null) {
        return;
      }

      final int[] box = DOM.getClientSize(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      final int spacing = layoutPanel.getWidgetSpacing();

      int width = box[0] - (paddings[1] + paddings[3]);
      int height = box[1] - (paddings[0] + paddings[2]);
      int left = paddings[3];
      int top = paddings[0];

      // adjust for spacing
      width -= ((getColumns() - 1) * spacing);
      height -= ((getRows() - 1) * spacing);

      int colWidth = width;

      int prefCols = 0;
      for (int c = 0; c < getColumns(); c++) {
        double colSpec = colSpecs[c];
        if (colSpec >= 0.0 && colSpec < 1.0) {
          colWidth -= width * colSpec;
        } else if (colSpec != -1 && colSpec > 0) {
          colWidth -= colSpec;
        } else {
          ++prefCols;
        }
      }
      if (prefCols > 0) {
        colWidth /= prefCols;
      }

      int rowHeight = height;

      int prefRows = 0;
      for (int r = 0; r < getRows(); r++) {
        double rowSpec = rowSpecs[r];
        if (rowSpec >= 0.0 && rowSpec < 1.0) {
          rowHeight -= height * rowSpec;
        } else if (rowSpec >= 1.0) {
          rowHeight -= rowSpec;
        } else {
          ++prefRows;
        }
      }
      if (prefRows > 0) {
        rowHeight = (int) Math.ceil((double) rowHeight / (double) prefRows);
      }

      buildWidgetMatrix(layoutPanel);

      for (int r = 0; r < getRows(); r++) {
        left = paddings[3];

        // ----
        int h;
        double rowSpec = rowSpecs[r];
        if (rowSpec >= 0.0 && rowSpec < 1.0) {
          h = (int) Math.ceil(height * rowSpec);
        } else if (rowSpec >= 1.0) {
          h = (int) rowSpec;
        } else {
          h = rowHeight;
        }
        // ----
        for (int c = 0; c < getColumns(); c++) {
          Widget widget = widgetMatrix[c][r];
          if (widget == null || widget == SPAN) {
            left += spacing + ((colSpecs[c] > 0) ? colSpecs[c] : colWidth);
            continue;
          }
          if (widget instanceof DecoratorPanel) {
            widget = ((DecoratorPanel) widget).getWidget();
          }
          final GridLayoutData layoutData = (GridLayoutData) getLayoutData(widget);
          // ----
          int w;
          double colSpec = colSpecs[c];
          if (colSpec >= 0.0 && colSpec < 1.0) {
            w = (int) Math.ceil(width * colSpec);
          } else if (colSpec > 1.0) {
            w = (int) colSpec;
          } else {
            w = colWidth;
          }
          for (int i = 1; i < layoutData.colspan; ++i) {
            colSpec = colSpecs[c + i];
            if (colSpec >= 0.0 && colSpec < 1.0) {
              w += Math.ceil(width * colSpec);
            } else if (colSpec > 1.0) {
              w += colSpec;
            } else {
              w += colWidth;
            }
          }
          // ----
          for (int i = 1; i < layoutData.rowspan; ++i) {
            rowSpec = rowSpecs[r + i];
            if (rowSpec >= 0.0 && rowSpec < 1.0) {
              h += Math.ceil(height * rowSpec);
            } else if (rowSpec >= 1.0) {
              h += rowSpec;
            } else {
              h += rowHeight;
            }
          }
          // ----
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int offsetWidth = decPanel.getOffsetWidth()
                - widget.getOffsetWidth();
            final int offsetHeight = decPanel.getOffsetHeight()
                - widget.getOffsetHeight();
            setBounds(layoutPanel, widget, left, top, w - offsetWidth + spacing
                * (layoutData.colspan - 1), h - offsetHeight + spacing
                * (layoutData.rowspan - 1));
          } else {
            setBounds(layoutPanel, widget, left, top, w + spacing
                * (layoutData.colspan - 1), h + spacing
                * (layoutData.rowspan - 1));
          }
          // ----
          colSpec = colSpecs[c];
          if (colSpec >= 0.0 && colSpec < 1.0) {
            left += spacing + Math.ceil(width * colSpec);
          } else if (colSpec > 1.0) {
            left += spacing + colSpec;
          } else {
            left += spacing + colWidth;
          }
        }
        rowSpec = rowSpecs[r];
        if (rowSpec >= 0.0 && rowSpec < 1.0) {
          top += spacing + Math.ceil(height * rowSpec);
        } else if (rowSpec >= 1.0) {
          top += spacing + rowSpec;
        } else {
          top += spacing + rowHeight;
        }
      }

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    layoutPanel.setPreferredSize(-1, -1);
  }

  /**
   * Sets the number of columns in the grid.
   * 
   * @param columns the new number of columns in the grid
   */
  @Override
  public void setColumns(int columns) {
    super.setColumns(Math.max(1, columns));
    double[] newColSpecs = new double[columns];
    for (int i = 0; i < columns; ++i) {
      newColSpecs[i] = (colSpecs != null && colSpecs.length > i) ? colSpecs[i]
          : -1;
    }
    colSpecs = newColSpecs;
  }

  /**
   * Sets the number of rows in the grid.
   * 
   * @param rows the new number of rows in the grid
   */
  @Override
  public void setRows(int rows) {
    super.setRows(Math.max(1, rows));
    double[] newRowSpecs = new double[rows];
    for (int i = 0; i < rows; ++i) {
      newRowSpecs[i] = (rowSpecs != null && rowSpecs.length > i) ? rowSpecs[i]
          : -1;
    }
    rowSpecs = newRowSpecs;
  }

}
