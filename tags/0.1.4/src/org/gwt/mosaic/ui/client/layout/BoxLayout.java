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

      final int[] margins = DOM.getMarginSizes(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());
      int width = (margins[1] + margins[3]) + (paddings[1] + paddings[3]);
      int height = (margins[0] + margins[2]) + (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      // adjust for spacing
      if (orient == Orientation.HORIZONTAL) {
        width += ((size - 1) * spacing);
      } else { // Orientation.VERTICAL
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
          if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              // FIXME
            } else {
              width += (int) layoutData.preferredWidth;
            }
          } else {
            width += getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              width += decPanelBorderWidth;
            }
          }
          if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              // FIXME
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
          } else {
            layoutData.calcHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
          }
          maxHeight = Math.max(maxHeight, layoutData.calcHeight);
        } else { // Orientation.VERTICAL
          if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              // FIXME
            } else {
              height += (int) layoutData.preferredHeight;
            }
          } else {
            height += getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              height += decPanelBorderHeight;
            }
          }
          if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              // FIXME
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
          } else {
            layoutData.calcWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
          }
          maxWidth = Math.max(maxWidth, layoutData.calcWidth);
        }
      }

      if (orient == Orientation.HORIZONTAL) {
        result[0] = width;
        result[1] = height + maxHeight;
      } else { // Orientation.VERTICAL
        result[0] = width + maxWidth;
        result[1] = height;
      }

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ".getPreferredSize() : "
          + e.getMessage());
    }
    
    layoutPanel.setPreferredSize(result[0], result[1]);
    
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
      } else { // Orientation.VERTICAL
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
          } else if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.preferredWidth * width);
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
            fillWidth -= layoutData.calcWidth;
          } else {
            layoutData.calcWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
            fillWidth -= layoutData.calcWidth;
            runTwiceFlag = true;
          }
          if (layoutData.fillHeight) {
            layoutData.calcHeight = height;
          } else if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.preferredHeight * height);
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
          } else {
            layoutData.calcHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
            runTwiceFlag = true;
          }
        } else { // Orientation.VERTICAL
          if (layoutData.fillHeight) {
            fillingHeight++;
          } else if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.preferredHeight * height);
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
            fillHeight -= layoutData.calcHeight;
          } else {
            layoutData.calcHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
            fillHeight -= layoutData.calcHeight;
            runTwiceFlag = true;
          }
          if (layoutData.fillWidth) {
            layoutData.calcWidth = width;
          } else if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.preferredWidth * width);
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
          } else {
            layoutData.calcWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
            runTwiceFlag = true;
          }
        }
      }

      // 2nd pass
      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        final BoxLayoutData layoutData = (BoxLayoutData) getLayoutData(child);

        int w = layoutData.calcWidth;
        int h = layoutData.calcHeight;

        if (orient == Orientation.HORIZONTAL) {
          if (layoutData.fillWidth) {
            w = fillWidth / fillingWidth;
          }
        } else { // Orientation.VERTICAL
          if (layoutData.fillHeight) {
            h = fillHeight / fillingHeight;
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
          } else { // !isLeftToRight()
            setBounds(layoutPanel, child, box[0] - decPanelBorderWidth
                - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())), top,
                fw, fh);
          }
        } else {
          if (orient == Orientation.VERTICAL || isLeftToRight()) {
            setBounds(layoutPanel, child, left, top, fw, fh);
          } else { // !isLeftToRight()
            setBounds(layoutPanel, child, box[0]
                - (left + (fw != -1 ? fw : child.getOffsetWidth())), top, fw,
                fh);
          }
        }

        if (orient == Orientation.HORIZONTAL) {
          left += (w + spacing);
        } else { // Orientation.VERTICAL
          top += (h + spacing);
        }
      }
    } catch (Exception e) {
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }
    
    layoutPanel.setPreferredSize(-1, -1);
  }

  public void setLeftToRight(boolean leftToRight) {
    this.leftToRight = leftToRight;
  }

  public void setOrient(Orientation orient) {
    this.orient = orient;
  }

}
