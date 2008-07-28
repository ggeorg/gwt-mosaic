package org.mosaic.ui.client.layout;

import java.util.ArrayList;
import java.util.List;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

public class BoxLayout extends BaseLayout {

  public enum Orientation {
    HORIZONTAL, VERTICAL
  };

  private Orientation orient;

  private int spacing = 4;

  boolean leftToRight = true;

  public BoxLayout() {
    this(Orientation.HORIZONTAL);
  }

  public BoxLayout(Orientation orient) {
    this.orient = orient;
  }

  public Orientation getOrient() {
    return orient;
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
      
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      int width = paddings[1] + paddings[3];
      int height = paddings[0] + paddings[2];

      // adjust for spacing
      if (orient == Orientation.HORIZONTAL) {
        width += ((size - 1) * spacing);
      } else {
        height += ((size - 1) * spacing);
      }

      int maxWidth = 0;
      int maxHeight = 0;

      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;

        if (orient == Orientation.HORIZONTAL) {
          if (layoutData.width != -1) {
            width += layoutData.width;
            layoutData.calcWidth = layoutData.width;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            width += flowWidth;
            layoutData.calcWidth = layoutData.width = flowWidth;
          }
          if (layoutData.height != -1) {
            layoutData.calcHeight = layoutData.height;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            layoutData.calcHeight = layoutData.height = flowHeight;
          }
          maxHeight = Math.max(maxHeight, (int) layoutData.height);
        } else {
          if (layoutData.height != -1) {
            height += layoutData.height;
            layoutData.calcHeight = layoutData.height;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            height += flowHeight;
            layoutData.calcHeight = layoutData.height = flowHeight;
          }
          if (layoutData.width != -1) {
            layoutData.calcWidth = layoutData.width;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            layoutData.calcWidth = layoutData.width = flowWidth;
          }
          maxWidth = Math.max(maxWidth, (int) layoutData.width);
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
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
    
    return result;
  }

  public int getSpacing() {
    return spacing;
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

      final int[] box = DOM.getClientSize(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      final int size = layoutPanel.getWidgetCount();

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

      final List<Widget> visibleChildList = new ArrayList<Widget>();

      // 1st pass
      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        visibleChildList.add(child);

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;

        if (orient == Orientation.HORIZONTAL) {
          if (layoutData.fillWidth) {
            fillingWidth++;
          } else if (layoutData.width != -1) {
            fillWidth -= layoutData.width;
            layoutData.calcWidth = layoutData.width;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            fillWidth -= flowWidth;
            layoutData.calcWidth = layoutData.width = flowWidth;
          }
          if (layoutData.fillHeight) {
            layoutData.calcHeight = height;
          } else if (layoutData.height != -1) {
            layoutData.calcHeight = layoutData.height;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            layoutData.calcHeight = layoutData.height = flowHeight;
          }
        } else {
          if (layoutData.fillHeight) {
            fillingHeight++;
          } else if (layoutData.height != -1) {
            fillHeight -= layoutData.height;
            layoutData.calcHeight = layoutData.height;
          } else {
            int flowHeight = getFlowHeight(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              flowHeight += decPanelBorderHeight;
            }
            fillHeight -= flowHeight;
            layoutData.calcHeight = layoutData.height = flowHeight;
          }
          if (layoutData.fillWidth) {
            layoutData.calcWidth = width;
          } else if (layoutData.width != -1) {
            layoutData.calcWidth = layoutData.width;
          } else {
            int flowWidth = getFlowWidth(child);
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              flowWidth += decPanelBorderWidth;
            }
            layoutData.calcWidth = layoutData.width = flowWidth;
          }
        }
      }

      // 2nd pass
      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        BoxLayoutData layoutData = (BoxLayoutData) LayoutManagerHelper.getLayoutData(child);

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
        int fw = (!layoutData.fillWidth && layoutData.width == -1) ? -1 : w;
        int fh = (!layoutData.fillHeight && layoutData.height == -1) ? -1 : h;

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
            setBounds(layoutPanel, decPanel, left, top, fw, fh);
          } else {
            setBounds(layoutPanel, decPanel, box[0] - decPanelBorderWidth
                - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())), top, fw, fh);
          }
        } else {
          if (orient == Orientation.VERTICAL || isLeftToRight()) {
            setBounds(layoutPanel, child, left, top, fw, fh);
          } else {
            setBounds(layoutPanel, child, box[0]
                - (left + (fw != -1 ? fw : child.getOffsetWidth())), top, fw, fh);
          }
        }

        if (orient == Orientation.VERTICAL) {
          top += h + spacing;
        } else {
          left += w + spacing;
        }
      }
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

  public void setLeftToRight(boolean leftToRight) {
    this.leftToRight = leftToRight;
  }

  public void setOrient(Orientation orient) {
    this.orient = orient;
  }

  public void setSpacing(int spacing) {
    this.spacing = spacing;
  }

}
