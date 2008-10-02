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

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof FillLayoutData)) {
          layoutDataObject = new FillLayoutData();
          setLayoutData(child, layoutDataObject);
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

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null || !(layoutDataObject instanceof FillLayoutData)) {
          layoutDataObject = new FillLayoutData();
          setLayoutData(child, layoutDataObject);
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
