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
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The {@code GridLayout} class is a layout manager that lays out a panel's
 * widgets in a rectangular grid. The panel is divided into equal-sized
 * rectangles, with each widget occupying one or more cells.
 * <p>
 * {@code GridLayout} has a number of configurable fields, and the control it
 * lays out can have an associated layout data object, called
 * {@link GridLayoutData}.
 * <p>
 * The following code lays out six buttons into three columns and two rows:
 * 
 * <pre>
 * LayoutPanel layoutPanel = new LayoutPanel(new GridLayout(3, 2));
 * layoutPanel.add(new Button("1"));
 * layoutPanel.add(new Button("2"));
 * layoutPanel.add(new Button("3"));
 * layoutPanel.add(new Button("4"));
 * layoutPanel.add(new Button("5"));
 * layoutPanel.add(new Button("6"));
 * </pre>
 * 
 * <p>
 * The {@code columns} and {@code rows} parameters are the most important
 * {@code GridLayout} properties. Widgets are laid out in columns from left to
 * right, and rows from top to bottom.
 * 
 * @see GridLayoutData
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class GridLayout extends BaseLayout implements HasAlignment {

  protected static final Widget SPAN = new SimplePanel();

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
  protected Widget[][] widgetMatrix;

  /**
   * Creates a grid layout with a default of one column per component, in a
   * single row.
   */
  public GridLayout() {
    this(1, 1);
  }

  /**
   * Constructor for grid layout with the specified number of rows and columns.
   * 
   * @param columns number of columns in the grid
   * @param rows number of rows in the grid
   */
  public GridLayout(int columns, int rows) {
    this(columns, rows, null, null);
  }

  /**
   * Constructor for grid layout with the specified number of rows and columns
   * TODO
   * 
   * @param columns number of columns in the grid
   * @param rows number of rows in the grid
   * @param horizontalAlignment TODO
   * @param verticalAlignment TODO
   */
  public GridLayout(int columns, int rows,
      HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment) {
    setColumns(columns);
    setRows(rows);
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  protected void buildWidgetMatrix(LayoutPanel layoutPanel) {
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

  /**
   * Determines the preferred size of the panel argument using this grid layout.
   * <p>
   * The preferred width of a grid layout is the largest preferred width of all
   * of the widgets in the panel times the number of columns, plus the
   * horizontal padding times the number of columns minus one, plus the left and
   * right margins.
   * <p>
   * The preferred height of a grid layout is the largest preferred height of
   * all of the widgets in the panel times the number of rows, plus the vertical
   * padding times the number of rows minus one, plus the top and bottom
   * margins.
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

  /**
   * Lays out the specified {@link LayoutPanel} using this layout.
   * <p>
   * The grid layout manager determines the size of individual widgets by
   * dividing the free space in the panel into equal-sized portions according to
   * the number of rows and columns in the layout. The container's free space
   * equals the container's size minus any margins and any specified horizontal
   * or vertical gap.
   * 
   * @param layoutPanel the panel in which to do the layout
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

          int cellWidth;
          int cellHeight;

          final GridLayoutData layoutData = (GridLayoutData) getLayoutData(widget);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int offsetWidth = decPanel.getOffsetWidth()
                - widget.getOffsetWidth();
            final int offsetHeight = decPanel.getOffsetHeight()
                - widget.getOffsetHeight();

            cellWidth = colWidth * layoutData.colspan - offsetWidth + spacing
                * (layoutData.colspan - 1);
            cellHeight = rowHeight * layoutData.rowspan + -offsetHeight
                + spacing * (layoutData.rowspan - 1);
          } else {
            cellWidth = colWidth * layoutData.colspan + spacing
                * (layoutData.colspan - 1);
            cellHeight = rowHeight * layoutData.rowspan + spacing
                * (layoutData.rowspan - 1);
          }

          HorizontalAlignmentConstant hAlignment = layoutData.getHorizontalAlignment();
          if (hAlignment == null) {
            hAlignment = getHorizontalAlignment();
          }

          int posLeft;
          int widgetWidth;

          if (hAlignment == null) {
            posLeft = left + (spacing + colWidth) * c;
            widgetWidth = cellWidth;
          } else if (HasHorizontalAlignment.ALIGN_LEFT == hAlignment) {
            posLeft = left + (spacing + colWidth) * c;
            widgetWidth = -1;
          } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
            posLeft = left + (spacing + colWidth) * c + (cellWidth / 2)
                - getFlowWidth(widget) / 2;
            widgetWidth = -1;
          } else {
            posLeft = left + (spacing + colWidth) * c + cellWidth
                - getFlowWidth(widget);
            widgetWidth = -1;
          }

          VerticalAlignmentConstant vAlignment = layoutData.getVerticalAlignment();
          if (vAlignment == null) {
            vAlignment = getVerticalAlignment();
          }

          int posTop;
          int widgetHeight;

          if (vAlignment == null) {
            posTop = top + (spacing + rowHeight) * r;
            widgetHeight = cellHeight;
          } else if (HasVerticalAlignment.ALIGN_TOP == vAlignment) {
            posTop = top + (spacing + rowHeight) * r;
            widgetHeight = -1;
          } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
            posTop = top + (spacing + rowHeight) * r + (cellHeight / 2)
                - getFlowHeight(widget) / 2;
            widgetHeight = -1;
          } else {
            posTop = top + (spacing + rowHeight) * r + cellHeight
                - getFlowHeight(widget);
            widgetHeight = -1;
          }

          setBounds(layoutPanel, widget, posLeft, posTop, widgetWidth,
              widgetHeight);

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

  private HorizontalAlignmentConstant horizontalAlignment;
  private VerticalAlignmentConstant verticalAlignment;

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasHorizontalAlignment#getHorizontalAlignment
   * ()
   */
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasHorizontalAlignment#setHorizontalAlignment
   * (com.google.gwt.user.client.ui.HasHorizontalAlignment.
   * HorizontalAlignmentConstant)
   */
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasVerticalAlignment#getVerticalAlignment()
   */
  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasVerticalAlignment#setVerticalAlignment
   * (com.
   * google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant)
   */
  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }

}
