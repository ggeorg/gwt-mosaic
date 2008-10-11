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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class BoxLayout extends BaseLayout {

  public enum Orientation {
    HORIZONTAL, VERTICAL
  };

  private Orientation orient;

  boolean leftToRight = true;

  private boolean runTwiceFlag;

  public BoxLayout() {
    this(Orientation.HORIZONTAL);
  }

  public BoxLayout(Orientation orient) {
    this.orient = orient;
  }

  public Orientation getOrient() {
    return orient;
  }

  @Override
  public boolean runTwice() {
    return runTwiceFlag;
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

      final int size = getVisibleWidgetCount(layoutPanel);
      if (size == 0) {
        return result;
      }

      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      int width = paddings[1] + paddings[3];
      int height = paddings[0] + paddings[2];

      final int spacing = layoutPanel.getWidgetSpacing();

      // adjust for spacing
      if (orient == Orientation.HORIZONTAL) {
        width += ((size - 1) * spacing);
      } else {
        height += ((size - 1) * spacing);
      }

      int maxWidth = 0;
      int maxHeight = 0;

      for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
        Widget child = iter.next();
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null
            || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;

        if (orient == Orientation.HORIZONTAL) {
          if (layoutData.width != -1) {
            if (layoutData.width > 0 && layoutData.width <= 1.0) {
              layoutData.calcWidth = 1; // FIXME
            } else {
              layoutData.calcWidth = (int) layoutData.width;
            }
            width += layoutData.calcWidth;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            width += flowWidth;
            layoutData.calcWidth = /* layoutData.width = */flowWidth;
          }
          if (layoutData.height != -1) {
            if (layoutData.height > 0 && layoutData.height <= 1.0) {
              layoutData.calcHeight = 1; // FIXME
            } else {
              layoutData.calcHeight = (int) layoutData.height;
            }
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            layoutData.calcHeight = /* layoutData.height = */flowHeight;
          }
          maxHeight = Math.max(maxHeight, (int) layoutData.calcHeight);
        } else {
          if (layoutData.height != -1) {
            if (layoutData.height > 0 && layoutData.height <= 1.0) {
              layoutData.calcHeight = 1; // FIXME
            } else {
              layoutData.calcHeight = (int) layoutData.height;
            }
            height += layoutData.calcHeight;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            height += flowHeight;
            layoutData.calcHeight = /* layoutData.height = */flowHeight;
          }
          if (layoutData.width != -1) {
            if (layoutData.width > 0 && layoutData.width <= 1.0) {
              layoutData.calcWidth = 1; // FIXME
            } else {
              layoutData.calcWidth = (int) layoutData.width;
            }
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            layoutData.calcWidth = /* layoutData.width = */flowWidth;
          }
          maxWidth = Math.max(maxWidth, (int) layoutData.calcWidth);
        }
      }

      if (orient == Orientation.HORIZONTAL) {
        result[0] = width;
        result[1] = height + maxHeight;
      } else {
        result[0] = width + maxWidth;
        result[1] = height;
      }

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ".getPreferredSize() : "
          + e.getMessage());
    }

    return result;
  }

  public int getVisibleWidgetCount(LayoutPanel layoutPanel) {
    int result = 0;
    for (int i = 0, n = layoutPanel.getWidgetCount(); i < n; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }
      if (!DOM.isVisible(child.getElement())) {
        continue;
      }
      ++result;
    }
    return result;
  }

  public boolean isLeftToRight() {
    return leftToRight;
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

      final int size = getVisibleWidgetCount(layoutPanel);
      if (size == 0) {
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
      if (orient == Orientation.HORIZONTAL) {
        width -= ((size - 1) * spacing);
      } else {
        height -= ((size - 1) * spacing);
      }

      int fillWidth = width;
      int fillHeight = height;

      int fillingWidth = 0;
      int fillingHeight = 0;

      runTwiceFlag = false;

      final List<Widget> visibleChildList = new ArrayList<Widget>();

      // 1st pass
      for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
        Widget child = iter.next();
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        visibleChildList.add(child);

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null
            || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;

        if (orient == Orientation.HORIZONTAL) {
          if (layoutData.fillWidth) {
            fillingWidth++;
          } else if (layoutData.width != -1) {
            if (layoutData.width > 0 && layoutData.width <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.width * width);
            } else {
              layoutData.calcWidth = (int) layoutData.width;
            }
            fillWidth -= layoutData.calcWidth;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            fillWidth -= flowWidth;
            layoutData.calcWidth = /* layoutData.width = */flowWidth;
            runTwiceFlag = true;
          }
          if (layoutData.fillHeight) {
            layoutData.calcHeight = height;
          } else if (layoutData.height != -1) {
            if (layoutData.height > 0 && layoutData.height <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.height * height);
            } else {
              layoutData.calcHeight = (int) layoutData.height;
            }
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            layoutData.calcHeight = /* layoutData.height = */flowHeight;
            runTwiceFlag = true;
          }
        } else {
          if (layoutData.fillHeight) {
            fillingHeight++;
          } else if (layoutData.height != -1) {
            if (layoutData.height > 0 && layoutData.height <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.height * height);
            } else {
              layoutData.calcHeight = (int) layoutData.height;
            }
            fillHeight -= layoutData.calcHeight;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            fillHeight -= flowHeight;
            layoutData.calcHeight = /* layoutData.height = */flowHeight;
            runTwiceFlag = true;
          }
          if (layoutData.fillWidth) {
            layoutData.calcWidth = width;
          } else if (layoutData.width != -1) {
            if (layoutData.width > 0 && layoutData.width <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.width * width);
            } else {
              layoutData.calcWidth = (int) layoutData.width;
            }
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            layoutData.calcWidth = /* layoutData.width = */flowWidth;
            runTwiceFlag = true;
          }
        }
      }

      // 2nd pass
      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        BoxLayoutData layoutData = (BoxLayoutData) getLayoutData(child);

        int w = (int) layoutData.calcWidth;
        int h = (int) layoutData.calcHeight;

        if (orient == Orientation.VERTICAL) {
          if (layoutData.fillHeight) {
            h = fillHeight / fillingHeight;
          }
        } else {
          if (layoutData.fillWidth) {
            w = fillWidth / fillingWidth;
          }
        }

        top = Math.max(0, top);

        // do not set size for normal flow
        // boolean normalFlow = !layoutData.fillWidth && !layoutData.fillHeight
        // && layoutData.width == -1 && layoutData.height == -1;
        // int fw = normalFlow ? -1 : w;
        // int fh = normalFlow ? -1 : h;
        int fw = w;
        int fh = h;
        // TODO to check again

        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
          final int decPanelBorderWidth = decPanel.getOffsetWidth()
              - child.getOffsetWidth();

          if (fw != -1) {
            fw -= decPanelBorderWidth;
          }
          if (fh != -1) {
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - child.getOffsetHeight();
            fh -= decPanelBorderHeight;
          }

          if (orient == Orientation.VERTICAL || isLeftToRight()) {
            setBounds(layoutPanel, child, left, top, fw, fh);
          } else {
            setBounds(layoutPanel, child, box[0] - decPanelBorderWidth
                - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())), top,
                fw, fh);
          }
        } else {
          if (orient == Orientation.VERTICAL || isLeftToRight()) {
            setBounds(layoutPanel, child, left, top, fw, fh);
          } else {
            setBounds(layoutPanel, child, box[0]
                - (left + (fw != -1 ? fw : child.getOffsetWidth())), top, fw,
                fh);
          }
        }

        if (orient == Orientation.VERTICAL) {
          top += h + spacing;
        } else {
          left += w + spacing;
        }
      }
    } catch (Exception e) {
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }
  }

  public void setLeftToRight(boolean leftToRight) {
    this.leftToRight = leftToRight;
  }

  public void setOrient(Orientation orient) {
    this.orient = orient;
  }

}
