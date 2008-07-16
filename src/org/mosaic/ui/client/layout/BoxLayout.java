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
    this.setMargin(5);
  }

  public Orientation getOrient() {
    return orient;
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

      final int size = layoutPanel.getWidgetCount();

      int width = box[0] - 2 * getMargin();
      int height = box[1] - 2 * getMargin();
      int left = getMargin();
      int top = getMargin();

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
            int[] p = DOM.getPaddingSizes(child.getElement());
            int flowWidth = child.getOffsetWidth() + p[1] + p[3];
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              // flowWidth += 2 * (child.getAbsoluteLeft() -
              // decPanel.getAbsoluteLeft());
              flowWidth += decPanelBorderWidth;
            }
            fillWidth -= flowWidth;
            layoutData.calcWidth = flowWidth;
            // for next time
            layoutData.width = flowWidth;
          }
          if (layoutData.fillHeight) {
            layoutData.calcHeight = height;
          } else if (layoutData.height != -1) {
            layoutData.calcHeight = layoutData.height;
          } else {
            final int[] p = DOM.getPaddingSizes(child.getElement());
            layoutData.calcHeight = child.getOffsetHeight() + p[0] + p[2];
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              // layoutData.calcHeight += 2 * (child.getAbsoluteTop() -
              // decPanel.getAbsoluteTop());
              layoutData.calcHeight += decPanelBorderHeight;
            }
            // for next time
            layoutData.height = layoutData.calcHeight;
          }
        } else {
          if (layoutData.fillHeight) {
            fillingHeight++;
          } else if (layoutData.height != -1) {
            fillHeight -= layoutData.height;
            layoutData.calcHeight = layoutData.height;
          } else {
            final int[] p = DOM.getPaddingSizes(child.getElement());
            int flowHeight = child.getOffsetHeight() + p[0] + p[2];
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              // flowHeight += 2 * (child.getAbsoluteTop() -
              // decPanel.getAbsoluteTop());
              flowHeight += decPanelBorderHeight;
            }
            fillHeight -= flowHeight;
            layoutData.calcHeight = flowHeight;
            // for next time
            layoutData.height = flowHeight;
          }
          if (layoutData.fillWidth) {
            layoutData.calcWidth = width;
          } else if (layoutData.width != -1) {
            layoutData.calcWidth = layoutData.width;
          } else {
            final int[] p = DOM.getPaddingSizes(child.getElement());
            layoutData.calcWidth = child.getOffsetWidth() + p[1] + p[3];
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              // layoutData.calcWidth += 2 * (child.getAbsoluteLeft() -
              // decPanel.getAbsoluteLeft());
              layoutData.calcWidth += decPanelBorderWidth;
            }
            // for next time
            layoutData.width = layoutData.calcWidth;
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
          if (fw != -1) {
            final int decPanelBorderWidth = decPanel.getOffsetWidth()
                - child.getOffsetWidth();
            // fw -= 2 * (child.getAbsoluteLeft() - decPanel.getAbsoluteLeft());
            fw -= decPanelBorderWidth;
          }
          if (fh != -1) {
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - child.getOffsetHeight();
            // fh -= 2 * (child.getAbsoluteTop() - decPanel.getAbsoluteTop());
            fh -= decPanelBorderHeight;
          }

          if (orient == Orientation.VERTICAL || isLeftToRight()) {
            setBounds(layoutPanel, decPanel, left, top, fw, fh);
          } else {
            setBounds(layoutPanel, decPanel, box[0]
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
