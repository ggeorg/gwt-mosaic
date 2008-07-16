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
            final int[] p = DOM.getPaddingSizes(north.getElement());
            layoutData.preferredSize = north.getOffsetHeight() + p[0] + p[2];
            northHeight = layoutData.preferredSize;
          } else if (northHeight > 0 && northHeight <= 1.0) {
            northHeight = height * northHeight;
          }
          // split bar
          h = (int) Math.round(northHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _width = Math.max(0, right - left) + 2
                * (decPanel.getAbsoluteLeft() - north.getAbsoluteLeft());
            setBounds(layoutPanel, decPanel, left, top, _width, h);
            // increase 'h'
            h += 2 * (north.getAbsoluteTop() - decPanel.getAbsoluteTop());
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
            final int[] p = DOM.getPaddingSizes(south.getElement());
            layoutData.preferredSize = south.getOffsetHeight() + p[0] + p[1];
            southHeight = layoutData.preferredSize;
          } else if (southHeight > 0 && southHeight <= 1.0) {
            southHeight = height * southHeight;
          }
          // split bar
          h = (int) Math.round(southHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _width = Math.max(0, right - left) + 2
                * (decPanel.getAbsoluteLeft() - north.getAbsoluteLeft());
            final int _top = Math.max(0, bottom - h) + 2
                * (decPanel.getAbsoluteTop() - south.getAbsoluteTop());
            setBounds(layoutPanel, decPanel, left, _top, _width, h);
            // increase 'h'
            h += 2 * (south.getAbsoluteTop() - decPanel.getAbsoluteTop());
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
            final int[] p = DOM.getPaddingSizes(west.getElement());
            layoutData.preferredSize = west.getOffsetWidth() + p[1] + p[3];
            westWidth = layoutData.preferredSize;
          } else if (westWidth > 0 && westWidth <= 1.0) {
            westWidth = width * westWidth;
          }
          // split bar
          w = (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _height = Math.max(0, bottom - top) + 2
                * (decPanel.getAbsoluteTop() - west.getAbsoluteTop());
            setBounds(layoutPanel, decPanel, left, top, w, _height);
            // increase 'h'
            w += 2 * (west.getAbsoluteLeft() - decPanel.getAbsoluteLeft());
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
            final int[] p = DOM.getPaddingSizes(east.getElement());
            layoutData.preferredSize = east.getOffsetWidth() + p[1] + p[3];
            eastWidth = layoutData.preferredSize;
          } else if (eastWidth > 0 && eastWidth <= 1.0) {
            eastWidth = width * eastWidth;
          }
          // split bar
          w = (int) Math.round(eastWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int decPanelBorderWidth = decPanel.getOffsetWidth() - east.getOffsetWidth();
            final int decPanelBorderHeight = decPanel.getOffsetHeight() - east.getOffsetHeight();
            final int _left = Math.max(0, right - w) - decPanelBorderWidth;
            final int _height = Math.max(0, bottom - top) - decPanelBorderHeight;
            setBounds(layoutPanel, decPanel, _left, top, w, _height);
            // increase 'h'
            w += 2 * (east.getAbsoluteLeft() - decPanel.getAbsoluteLeft());
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
        final int decPanelBorderWidth = decPanel.getOffsetWidth() - center.getOffsetWidth();
        final int decPanelBorderHeight = decPanel.getOffsetHeight() - center.getOffsetHeight();
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
}
