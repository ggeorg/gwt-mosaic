/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.BaseLayout.Measure;
import org.gwt.mosaic.ui.client.layout.BaseLayout.PreferredHeightMeasure;
import org.gwt.mosaic.ui.client.layout.BaseLayout.PreferredWidthMeasure;
import org.gwt.mosaic.ui.client.layout.BaseLayout.WidgetSizeCache;

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

  private HorizontalAlignmentConstant horizontalAlignment;

  private VerticalAlignmentConstant verticalAlignment;
  
  /**
   * Caches component minimum and preferred sizes. All requests for component
   * sizes shall be directed to the cache.
   */
  private final WidgetSizeCache componentSizeCache;
  
  /**
   * These functional objects are used to measure component sizes. They abstract
   * from horizontal and vertical orientation and so, allow to implement the
   * layout algorithm for both orientations with a single set of methods.
   */
  private final Measure preferredWidthMeasure;
  private final Measure preferredHeightMeasure;

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
    this.componentSizeCache = new WidgetSizeCache(0);
    this.preferredWidthMeasure = new PreferredWidthMeasure(componentSizeCache);
    this.preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);
    setColumns(columns);
    setRows(rows);
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  protected void buildWidgetMatrix(LayoutPanel layoutPanel) {
    int cursorX = 0;
    int cursorY = 0;

    widgetMatrix = new Widget[cols][rows];

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      syncDecoratorVisibility(widget);

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      }

      visibleChildList.add(widget);

      GridLayoutData layoutData = getGridLayoutData(widget);

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

      widgetMatrix[cursorX][cursorY] = widget;

      cursorX += layoutData.colspan;
      if (cursorX >= cols) {
        cursorX = 0;
        if (++cursorY >= rows) {
          break;
        }
      }
    }
  }

  private GridLayoutData getGridLayoutData(Widget widget) {
    Object layoutDataObject = getLayoutData(widget);
    if (layoutDataObject == null
        || !(layoutDataObject instanceof GridLayoutData)) {
      layoutDataObject = new GridLayoutData();
      setLayoutData(widget, layoutDataObject);
    }
    return (GridLayoutData) layoutDataObject;
  }

  @Override
  public void flushCache() {
    initialized = false;
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
   * @see
   * com.google.gwt.user.client.ui.HasHorizontalAlignment#getHorizontalAlignment
   * ()
   */
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
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
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension();

    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

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

          final Dimension dim = new Dimension(preferredWidthMeasure.sizeOf(widget), preferredHeightMeasure.sizeOf(widget));

          int flowWidth, flowHeight;

          flowWidth = dim.getWidth();
          flowHeight = dim.getHeight();

          cellWidth = Math.max(cellWidth, (int) Math.ceil((double) flowWidth
              / (double) layoutData.colspan));

          cellHeight = Math.max(cellHeight, (int) Math.ceil((double) flowHeight
              / (double) layoutData.rowspan));
        }
        result.width = Math.max(result.width, cellWidth);
        result.height = Math.max(result.height, cellHeight);
      }
      result.width *= cols;
      result.height *= rows;

      result.width += (margins[1] + margins[3]) + (paddings[1] + paddings[3])
          + (borders[1] + borders[3]);
      result.height += (margins[0] + margins[2]) + (paddings[0] + paddings[2])
          + (borders[0] + borders[2]);

      final int spacing = layoutPanel.getWidgetSpacing();
      result.width += ((cols - 1) * spacing);
      result.height += ((rows - 1) * spacing);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.getLocalizedMessage());
    }

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

  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    buildWidgetMatrix(layoutPanel);

    return initialized = true;
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
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      int left = paddings[3];
      int top = paddings[0];
      int width = box.width - (paddings[1] + paddings[3]);
      int height = box.height - (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      // adjust for spacing
      width -= ((cols - 1) * spacing);
      height -= ((rows - 1) * spacing);

      final int colWidth = width / cols;
      final int rowHeight = height / rows;

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
            final Dimension decPanelFrameSize = getDecoratorFrameSize(
                layoutData.decoratorPanel, widget);
            cellWidth = colWidth * layoutData.colspan - decPanelFrameSize.width
                + spacing * (layoutData.colspan - 1);
            cellHeight = rowHeight * layoutData.rowspan
                - decPanelFrameSize.height + spacing * (layoutData.rowspan - 1);
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
          
          Dimension prefSize = null;

          if (hAlignment == null) {
            layoutData.targetLeft = left + (spacing + colWidth) * c;
            layoutData.targetWidth = cellWidth;
          } else {
            // (ggeorg) this call to WidgetHelper.getPreferredSize() is
            // required even for ALIGN_LEFT
            prefSize = new Dimension(preferredWidthMeasure.sizeOf(widget), preferredHeightMeasure.sizeOf(widget));

            if (HasHorizontalAlignment.ALIGN_LEFT == hAlignment) {
              layoutData.targetLeft = left + (spacing + colWidth) * c;
            } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
              layoutData.targetLeft = left + (spacing + colWidth) * c + (cellWidth / 2)
                  - prefSize.width / 2;
            } else {
              layoutData.targetLeft = left + (spacing + colWidth) * c + cellWidth
                  - prefSize.width;
            }
            layoutData.targetWidth = prefSize.width;
          }

          VerticalAlignmentConstant vAlignment = layoutData.getVerticalAlignment();
          if (vAlignment == null) {
            vAlignment = getVerticalAlignment();
          }

          if (vAlignment == null) {
            layoutData.targetTop = top + (spacing + rowHeight) * r;
            layoutData.targetHeight = cellHeight;
          } else {
            if (prefSize == null) {
              // (ggeorg) this call to WidgetHelper.getPreferredSize() is
              // required even for ALIGN_TOP
              prefSize = new Dimension(preferredWidthMeasure.sizeOf(widget), preferredHeightMeasure.sizeOf(widget));
            }
            if (HasVerticalAlignment.ALIGN_TOP == vAlignment) {
              layoutData.targetTop = top + (spacing + rowHeight) * r;
            } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
              layoutData.targetTop = top + (spacing + rowHeight) * r + (cellHeight / 2)
                  - prefSize.height / 2;
            } else {
              layoutData.targetTop = top + (spacing + rowHeight) * r + cellHeight
                  - prefSize.height;
            }
            layoutData.targetHeight = prefSize.height;
          }

          layoutData.setSourceLeft(widget.getAbsoluteLeft()
              - layoutPanel.getAbsoluteLeft());
          layoutData.setSourceTop(widget.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop());
          layoutData.setSourceWidth(widget.getOffsetWidth());
          layoutData.setSourceHeight(widget.getOffsetHeight());
        }
      }
      
      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.getLocalizedMessage());
    }

  }

  /**
   * Sets the number of columns in the grid.
   * 
   * @param columns the new number of columns in the grid
   */
  public void setColumns(int columns) {
    this.cols = Math.max(1, columns);
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

  /**
   * Sets the number of rows in the grid.
   * 
   * @param rows the new number of rows in the grid
   */
  public void setRows(int rows) {
    this.rows = Math.max(1, rows);
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
