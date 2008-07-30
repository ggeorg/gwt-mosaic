package org.mosaic.ui.client.layout;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.LayoutManagerHelper;
import com.google.gwt.user.client.ui.Widget;

public class FillLayout extends BaseLayout {

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui.client.layout.LayoutPanel)
   */
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = { 0, 0 };

    try {
      if (layoutPanel == null) {
        return result;
      }

      final int size = layoutPanel.getWidgetCount();

      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof FillLayoutData)) {
          layoutDataObject = new FillLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        FillLayoutData layoutData = (FillLayoutData) layoutDataObject;

        result[0] = getFlowWidth(child);
        result[1] = getFlowHeight(child);
        
        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
          result[0] += decPanel.getOffsetWidth() - child.getOffsetWidth();
          result[1] += decPanel.getOffsetHeight() - child.getOffsetHeight();
        }

        break;
      }

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
    
    return result;
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
      
      final int left = paddings[3];
      final int top = paddings[0];
      int width = box[0] - (paddings[1] + paddings[3]);
      int height = box[1] - (paddings[0] + paddings[2]);

      final int size = layoutPanel.getWidgetCount();

      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = LayoutManagerHelper.getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof FillLayoutData)) {
          layoutDataObject = new FillLayoutData();
          LayoutManagerHelper.setLayoutData(child, layoutDataObject);
        }
        FillLayoutData layoutData = (FillLayoutData) layoutDataObject;

        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
          final int offsetWidth = decPanel.getOffsetWidth() - child.getOffsetWidth();
          final int offsetHeight = decPanel.getOffsetHeight() - child.getOffsetHeight();
          width -= offsetWidth;
          height -= offsetHeight;
          setBounds(layoutPanel, child, left, top, width, height);
        } else {
          setBounds(layoutPanel, child, left, top, width, height);
        }

        break;
      }

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

}
