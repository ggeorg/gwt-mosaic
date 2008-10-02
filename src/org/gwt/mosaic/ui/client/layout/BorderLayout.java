/*
 * Copyright 2008 Georgios J. Georgopoulos.
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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

public class BorderLayout extends BaseLayout {

  public enum BorderLayoutRegion {
    NORTH, EAST, SOUTH, WEST, CENTER
  };

  private Widget north, east, south, west, center;

  private SplitBar northSplitBar, southSplitBar, westSplitBar, eastSplitBar;

  public BorderLayout() {
    // Nothing to do here!
  }

  public Widget getEast() {
    return east;
  }

  public Widget getNorth() {
    return north;
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

      scanForPanels(layoutPanel);

      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      final int spacing = layoutPanel.getWidgetSpacing();

      int width = paddings[1] + paddings[3];
      int height = paddings[0] + paddings[2];

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        if (layoutData.collapse) {
          // TODO collapse
        } else {
          double northHeight = layoutData.preferredSize;
          if (northHeight == -1.0) {
            layoutData.preferredSize = northHeight = getFlowHeight(north);
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
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        if (layoutData.collapse) {
          // TODO collapse
        } else {
          double southHeight = layoutData.preferredSize;
          if (southHeight == -1.0) {
            layoutData.preferredSize = southHeight = getFlowHeight(south);
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
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.collapse) {
          // TODO collapse
        } else {
          double westWidth = layoutData.preferredSize;
          if (westWidth == -1.0) {
            layoutData.preferredSize = westWidth = getFlowWidth(west);
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
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.collapse) {
          // TODO collapse
        } else {
          double eastWidth = layoutData.preferredSize;
          if (eastWidth == -1.0) {
            layoutData.preferredSize = eastWidth = getFlowWidth(east);
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

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
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

      scanForPanels(layoutPanel);

      final int[] box = DOM.getClientSize(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      final int width = box[0] - (paddings[1] + paddings[3]);
      final int height = box[1] - (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      int left = paddings[3];
      int right = left + width;

      int top = paddings[0];
      int bottom = top + height;

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        if (layoutData.resizable) {
          if (northSplitBar == null) {
            northSplitBar = new SplitBar(layoutPanel, north, SplitBar.NORTH);
            northSplitBar.setStyleName("NorthSplitBar");
            layoutPanel.add(northSplitBar);
          }
        } else {
          if (northSplitBar != null) {
            layoutPanel.remove(northSplitBar);
            northSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double northHeight = layoutData.preferredSize;
          if (northHeight == -1.0) {
            layoutData.preferredSize = northHeight = getFlowHeight(north);
          } else if (northHeight > 0 && northHeight <= 1.0) {
            northHeight = height * northHeight;
          }
          // split bar
          h = (int) Math.round(northHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int decPanelBorderWidth = decPanel.getOffsetWidth()
                - north.getOffsetWidth();
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - north.getOffsetHeight();
            setBounds(layoutPanel, north, left, top, Math.max(0, right - left)
                - decPanelBorderWidth, h);
            // increase 'h'
            h += decPanelBorderHeight;
          } else {
            setBounds(layoutPanel, north, left, top, Math.max(0, right - left), h);
          }

          if (layoutData.resizable) {
            setBounds(layoutPanel, northSplitBar, left, top + h,
                Math.max(0, right - left), spacing);
          }
        }

        top += (h + spacing);
      }

      if (south != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        if (layoutData.resizable) {
          if (southSplitBar == null) {
            southSplitBar = new SplitBar(layoutPanel, south, SplitBar.SOUTH);
            southSplitBar.setStyleName("SouthSplitBar");
            layoutPanel.add(southSplitBar);
          }
        } else {
          if (southSplitBar != null) {
            layoutPanel.remove(southSplitBar);
            southSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double southHeight = layoutData.preferredSize;
          if (southHeight == -1.0) {
            layoutData.preferredSize = southHeight = getFlowHeight(south);
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
            setBounds(layoutPanel, south, left, _top, _width, h);
            // increase 'h'
            h += (decPanel.getOffsetHeight() - south.getOffsetHeight());
          } else {
            setBounds(layoutPanel, south, left, Math.max(0, bottom - h), Math.max(0,
                right - left), h);
          }

          if (layoutData.resizable) {
            setBounds(layoutPanel, southSplitBar, left,
                Math.max(0, bottom - h) - spacing, Math.max(0, right - left), spacing);
          }
        }

        bottom -= (h + spacing);
      }

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.resizable) {
          if (westSplitBar == null) {
            westSplitBar = new SplitBar(layoutPanel, west, SplitBar.WEST);
            westSplitBar.setStyleName("WestSplitBar");
            layoutPanel.add(westSplitBar);

          }
        } else {
          if (westSplitBar != null) {
            layoutPanel.remove(westSplitBar);
            westSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double westWidth = layoutData.preferredSize;
          if (westWidth == -1.0) {
            layoutData.preferredSize = westWidth = getFlowWidth(west);
          } else if (westWidth > 0 && westWidth <= 1.0) {
            westWidth = width * westWidth;
          }
          w = (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
            final int _height = Math.max(0, bottom - top)
                - (decPanel.getOffsetHeight() - west.getOffsetHeight());
            setBounds(layoutPanel, west, left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - west.getOffsetWidth());
          } else {
            setBounds(layoutPanel, west, left, top, w, Math.max(0, bottom - top));
          }

          if (layoutData.resizable) {
            setBounds(layoutPanel, westSplitBar, left + w, top, spacing, Math.max(0,
                bottom - top));
          }
        }

        left += (w + spacing);
      }

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.resizable) {
          if (eastSplitBar == null) {
            eastSplitBar = new SplitBar(layoutPanel, east, SplitBar.EAST);
            eastSplitBar.setStyleName("EastSplitBar");
            layoutPanel.add(eastSplitBar);
          }
        } else {
          if (eastSplitBar != null) {
            layoutPanel.remove(eastSplitBar);
            eastSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          // collapse
        } else {
          double eastWidth = layoutData.preferredSize;
          if (eastWidth == -1.0) {
            layoutData.preferredSize = eastWidth = getFlowWidth(east);
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
            setBounds(layoutPanel, east, _left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - east.getOffsetWidth());
          } else {
            setBounds(layoutPanel, east, Math.max(0, right - w), top, w, Math.max(0,
                bottom - top));
          }

          if (layoutData.resizable) {
            setBounds(layoutPanel, eastSplitBar, Math.max(0, right - w) - spacing, top,
                spacing, Math.max(0, bottom - top));
          }
        }

        right -= (w + spacing);
      }

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
      if (layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
        final int decPanelBorderWidth = decPanel.getOffsetWidth()
            - center.getOffsetWidth();
        final int decPanelBorderHeight = decPanel.getOffsetHeight()
            - center.getOffsetHeight();
        final int _width = Math.max(0, right - left) - decPanelBorderWidth;
        final int _height = Math.max(0, bottom - top) - decPanelBorderHeight;
        setBounds(layoutPanel, center, left, top, _width, _height);
      } else {
        setBounds(layoutPanel, center, left, top, Math.max(0, right - left), Math.max(0,
            bottom - top));
      }
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

  private void scanForPanels(LayoutPanel layoutPanel) {
    final int size = layoutPanel.getWidgetCount();

    for (int i = 0; i < size; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      if (!DOM.isVisible(child.getElement())) {
        continue;
      }

      Object layoutDataObject = getLayoutData(child);
      if (layoutDataObject == null || !(layoutDataObject instanceof BorderLayoutData)) {
        layoutDataObject = new BorderLayoutData();
        setLayoutData(child, layoutDataObject);
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
  }

}
