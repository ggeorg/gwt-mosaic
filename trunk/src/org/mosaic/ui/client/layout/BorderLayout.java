package org.mosaic.ui.client.layout;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

public class BorderLayout extends BaseLayout {

  public enum BorderLayoutRegion {
    NORTH, EAST, SOUTH, WEST, CENTER
  };

  private int spacing = 4;

  private Widget north, east, south, west, center;

  public BorderLayout() {
    setMargin(5);
  }

  public int getSpacing() {
    return spacing;
  }

  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

  public Widget getNorth() {
    return north;
  }

  public Widget getEast() {
    return east;
  }

  public Widget getSouth() {
    return south;
  }

  public Widget getWest() {
    return west;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.mosaic.ui.client.LayoutPanel)
   */
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null) {
        return;
      }

      final int[] box = DOM.getClientSize(layoutPanel.getElement());

      final int size = layoutPanel.getWidgetCount();

      // 1st pass
      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof BorderLayoutData)) {
          layoutDataObject = new BorderLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        BorderLayoutData layoutData = (BorderLayoutData) layoutDataObject;

        if (layoutData.region == BorderLayoutRegion.NORTH) {
          if (north == null) {
            north = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.EAST) {
          if (east == null) {
            east = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.SOUTH) {
          if (south == null) {
            south = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.WEST) {
          if (west == null) {
            west = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.CENTER) {
          if (center == null) {
            center = child;
          }
        }

        if (north != null && east != null && south != null && west != null
            && center != null) {
          break;
        }
      }

      if (center == null) {
        throw new RuntimeException("BorderLayout requires a widget in the center region.");
      }

      final int width = box[0];
      final int height = box[1];

      int left = getMargin();
      int right = left + width - (2 * getMargin());

      int top = getMargin();
      int bottom = top + height - (2 * getMargin());

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(north);

        if (layoutData.resizable) {
          // init split bar
        } else {
          // remove split bar
        }

        int h = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double northHeight = layoutData.preferredSize;
          if (northHeight == -1.0) {
            northHeight = getFlowHeight(north);
          } else if (northHeight > 0 && northHeight <= 1.0) {
            northHeight = height * northHeight;
          }
          // split bar
          h = (int) Math.round(northHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _width = Math.max(0, right - left)
                - (decPanel.getOffsetWidth() - north.getOffsetWidth());
            setBounds(layoutPanel, decPanel, left, top, _width, h);
            // increase 'h'
            h += (decPanel.getOffsetHeight() - north.getOffsetHeight());
          } else {
            setBounds(layoutPanel, north, left, top, Math.max(0, right - left), h);
          }
        }

        top += (h + spacing);
      }

      if (south != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(south);

        if (layoutData.resizable) {
          // init split bar
        } else {
          // remove split bar
        }

        int h = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double southHeight = layoutData.preferredSize;
          if (southHeight == -1.0) {
            southHeight = getFlowHeight(south);
          } else if (southHeight > 0 && southHeight <= 1.0) {
            southHeight = height * southHeight;
          }
          // split bar
          h = (int) Math.round(southHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _width = Math.max(0, right - left)
                - (decPanel.getOffsetWidth() - south.getOffsetWidth());
            final int _top = Math.max(0, bottom - h)
                - (decPanel.getOffsetHeight() - south.getOffsetHeight());
            setBounds(layoutPanel, decPanel, left, _top, _width, h);
            // increase 'h'
            h += (decPanel.getOffsetHeight() - south.getOffsetHeight());
          } else {
            setBounds(layoutPanel, south, left, Math.max(0, bottom - h), Math.max(0,
                right - left), h);
          }
        }

        bottom -= (h + spacing);
      }

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(west);

        if (layoutData.resizable) {
          // init split bar
        } else {
          // remove split bar
        }

        int w = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double westWidth = layoutData.preferredSize;
          if (westWidth == -1.0) {
            westWidth = getFlowWidth(west);
          } else if (westWidth > 0 && westWidth <= 1.0) {
            westWidth = width * westWidth;
          }
          // split bar
          w = (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _height = Math.max(0, bottom - top)
                - (decPanel.getOffsetHeight() - west.getOffsetHeight());
            setBounds(layoutPanel, decPanel, left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - west.getOffsetWidth());
          } else {
            setBounds(layoutPanel, west, left, top, w, Math.max(0, bottom - top));
          }
        }

        left += (w + spacing);
      }

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(east);

        if (layoutData.resizable) {
          // init split bar
        } else {
          // remove split bar
        }

        int w = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double eastWidth = layoutData.preferredSize;
          if (eastWidth == -1.0) {
            eastWidth = getFlowWidth(east);
          } else if (eastWidth > 0 && eastWidth <= 1.0) {
            eastWidth = width * eastWidth;
          }
          // split bar
          w = (int) Math.round(eastWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int decPanelBorderWidth = decPanel.getOffsetWidth()
                - east.getOffsetWidth();
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - east.getOffsetHeight();
            final int _left = Math.max(0, right - w) - decPanelBorderWidth;
            final int _height = Math.max(0, bottom - top) - decPanelBorderHeight;
            setBounds(layoutPanel, decPanel, _left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - east.getOffsetWidth());
          } else {
            setBounds(layoutPanel, east, Math.max(0, right - w), top, w, Math.max(0,
                bottom - top));
          }
        }

        right -= (w + spacing);
      }

      BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(center);
      if (layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
        final int decPanelBorderWidth = decPanel.getOffsetWidth()
            - center.getOffsetWidth();
        final int decPanelBorderHeight = decPanel.getOffsetHeight()
            - center.getOffsetHeight();
        final int _width = Math.max(0, right - left) - decPanelBorderWidth;
        final int _height = Math.max(0, bottom - top) - decPanelBorderHeight;
        setBounds(layoutPanel, decPanel, left, top, _width, _height);
      } else {
        setBounds(layoutPanel, center, left, top, Math.max(0, right - left), Math.max(0,
            bottom - top));
      }
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui.client.layout.LayoutPanel)
   */
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};

    try {
      if (layoutPanel == null) {
        return result;
      }

      final int size = layoutPanel.getWidgetCount();

      // 1st pass
      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof BorderLayoutData)) {
          layoutDataObject = new BorderLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        BorderLayoutData layoutData = (BorderLayoutData) layoutDataObject;

        if (layoutData.region == BorderLayoutRegion.NORTH) {
          if (north == null) {
            north = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.EAST) {
          if (east == null) {
            east = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.SOUTH) {
          if (south == null) {
            south = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.WEST) {
          if (west == null) {
            west = child;
          }
        } else if (layoutData.region == BorderLayoutRegion.CENTER) {
          if (center == null) {
            center = child;
          }
        }

        if (north != null && east != null && south != null && west != null
            && center != null) {
          break;
        }
      }

      if (center == null) {
        throw new RuntimeException("BorderLayout requires a widget in the center region.");
      }

      int width = 2 * getMargin();
      int height = 2 * getMargin();

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(north);

        if (layoutData.collapse) {
          // collapse
        } else {
          double northHeight = layoutData.preferredSize;
          if (northHeight == -1.0) {
            northHeight = getFlowHeight(north);
          } else if (northHeight > 0 && northHeight <= 1.0) {
            northHeight = height * northHeight;
          }
          // split bar
          height += (int) Math.round(northHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            height += (decPanel.getOffsetHeight() - north.getOffsetHeight());
          }
        }

        height += spacing;
      }

      if (south != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(south);

        if (layoutData.collapse) {
          // collapse
        } else {
          double southHeight = layoutData.preferredSize;
          if (southHeight == -1.0) {
            southHeight = getFlowHeight(south);
          } else if (southHeight > 0 && southHeight <= 1.0) {
            southHeight = height * southHeight;
          }
          // split bar
          height += (int) Math.round(southHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            height += (decPanel.getOffsetHeight() - south.getOffsetHeight());
          }
        }

        height += spacing;
      }

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(west);

        if (layoutData.collapse) {
          // collapse
        } else {
          double westWidth = layoutData.preferredSize;
          if (westWidth == -1.0) {
            westWidth = getFlowWidth(west);
          } else if (westWidth > 0 && westWidth <= 1.0) {
            westWidth = width * westWidth;
          }
          // split bar
          width += (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            width += (decPanel.getOffsetWidth() - west.getOffsetWidth());
          }
        }

        width += spacing;
      }

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(east);

        if (layoutData.collapse) {
          // collapse
        } else {
          double eastWidth = layoutData.preferredSize;
          if (eastWidth == -1.0) {
            eastWidth = getFlowWidth(east);
          } else if (eastWidth > 0 && eastWidth <= 1.0) {
            eastWidth = width * eastWidth;
          }
          // split bar
          width += (int) Math.round(eastWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            width += (decPanel.getOffsetWidth() - east.getOffsetWidth());
          }
        }

        width += spacing;
      }
      
      width += getFlowWidth(center);
      height += getFlowHeight(center);
      
      BorderLayoutData layoutData = (BorderLayoutData) LayoutManagerHelper.getLayoutData(center);
      if (layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
        width += decPanel.getOffsetWidth() - center.getOffsetWidth();
        height += decPanel.getOffsetHeight() - center.getOffsetHeight();
      }

      result[0] = width;
      result[1] = height;

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    return result;
  }

}
