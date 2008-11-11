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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class GridLayout extends BaseLayout {

  private static final Widget SPAN = new SimplePanel();

  /**
   * Initial grid columns.
   */
  private int cols;

  /**
   * Initial grid rows.
   */
  private int rows;

  /**
   * The widget matrix to render.
   */
  private Widget[][] widgetMatrix;

  /**
   * Constructs an empty grid layout.
   */
  public GridLayout() {
    this(1, 1);
  }

  /**
   * Constructor for grid of given size (number of cells).
   * 
   * @param columns number of columns in the grid
   * @param rows number of rows in the grid
   */
  public GridLayout(int columns, int rows) {
    setColumns(columns);
    setRows(rows);
  }

  private void buildWidgetMatrix(LayoutPanel layoutPanel) {
    final int size = layoutPanel.getWidgetCount();

    int cursorX = 0;
    int cursorY = 0;

    widgetMatrix = new Widget[cols][rows];

    for (int i = 0; i < size; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      if (!DOM.isVisible(child.getElement())) {
        continue;
      }

      Object layoutDataObject = getLayoutData(child);
      if (layoutDataObject == null
          || !(layoutDataObject instanceof GridLayoutData)) {
        layoutDataObject = new GridLayoutData();
        setLayoutData(child, layoutDataObject);
      }
      GridLayoutData layoutData = (GridLayoutData) layoutDataObject;

      while (widgetMatrix[cursorX][cursorY] != null) {
        if (++cursorX >= cols) {
          cursorX = 0;
          if (++cursorY >= rows) {
            break;
          }
        }
      }

      for (int r = cursorY; r < (cursorY + layoutData.rowspan); r++) {
        if (r >= rows) {
          break;
        }
        for (int c = cursorX; c < (cursorX + layoutData.colspan); c++) {
          if (c >= cols) {
            break;
          }
          widgetMatrix[c][r] = SPAN;
        }
      }

      widgetMatrix[cursorX][cursorY] = child;

      cursorX += layoutData.colspan;
      if (cursorX >= cols) {
        cursorX = 0;
        if (++cursorY >= rows) {
          break;
        }
      }
    }
  }

  /**
   * Get the number of columns in the grid.
   * 
   * @return the number of columns in the grid
   */
  public final int getColumns() {
    return cols;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};

    try {
      if (layoutPanel == null) {
        return result;
      }

      buildWidgetMatrix(layoutPanel);
      
      for (int r = 0; r < rows; r++) {
        int cellWidth = 0;
        int cellHeight = 0;
        for (int c = 0; c < cols; c++) {
          Widget widget = widgetMatrix[c][r];
          if (widget == null || widget == SPAN) {
            continue;
          }
          if (widget instanceof DecoratorPanel) {
            widget = ((DecoratorPanel) widget).getWidget();
          }

          GridLayoutData layoutData = (GridLayoutData) getLayoutData(widget);

          int flowWidth = getFlowWidth(widget);
          cellWidth = Math.max(cellWidth, (int) Math.ceil((double) flowWidth
              / (double) layoutData.colspan));

          int flowHeight = getFlowHeight(widget);
          cellHeight = Math.max(cellHeight, (int) Math.ceil((double) flowHeight
              / (double) layoutData.rowspan));
        }
        result[0] = Math.max(result[0], cellWidth);
        result[1] = Math.max(result[1], cellHeight);
      }
      result[0] *= cols;
      result[1] *= rows;

      final int[] margins = DOM.getMarginSizes(layoutPanel.getElement());
      result[0] += (margins[1] + margins[3]);
      result[1] += (margins[0] + margins[2]);

      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());
      result[0] += (paddings[1] + paddings[3]);
      result[1] += (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();
      result[0] += ((cols - 1) * spacing);
      result[1] += ((rows - 1) * spacing);
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    layoutPanel.setPreferredSize(result[0], result[1]);

    return result;
  }

  /**
   * Get the number of rows in the grid.
   * 
   * @return the number of rows in the grid
   */
  public final int getRows() {
    return rows;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
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
      width -= ((cols - 1) * spacing);
      height -= ((rows - 1) * spacing);

      final int colWidth = width / cols;
      final int rowHeight = height / rows;

      buildWidgetMatrix(layoutPanel);

      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          Widget widget = widgetMatrix[c][r];
          if (widget == null || widget == SPAN) {
            continue;
          }
          if (widget instanceof DecoratorPanel) {
            widget = ((DecoratorPanel) widget).getWidget();
          }
          final GridLayoutData layoutData = (GridLayoutData) getLayoutData(widget);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int offsetWidth = decPanel.getOffsetWidth()
                - widget.getOffsetWidth();
            final int offsetHeight = decPanel.getOffsetHeight()
                - widget.getOffsetHeight();
            setBounds(layoutPanel, widget, left + (spacing + colWidth) * c, top
                + (spacing + rowHeight) * r, colWidth * layoutData.colspan
                - offsetWidth + spacing * (layoutData.colspan - 1), rowHeight
                * layoutData.rowspan + -offsetHeight + spacing
                * (layoutData.rowspan - 1));
          } else {
            setBounds(layoutPanel, widget, left + (spacing + colWidth) * c, top
                + (spacing + rowHeight) * r, colWidth * layoutData.colspan
                + spacing * (layoutData.colspan - 1), rowHeight
                * layoutData.rowspan + spacing * (layoutData.rowspan - 1));
          }
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
  public void setColumns(int columns) {
    this.cols = Math.max(1, columns);
  }

  /**
   * Sets the number of rows in the grid.
   * 
   * @param rows the new number of rows in the grid
   */
  public void setRows(int rows) {
    this.rows = Math.max(1, rows);
  }

}
