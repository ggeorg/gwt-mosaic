/*
 * Copyright (c) 2008-2009 GWT Mosaic Johan Rydberg.
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
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The {@code AbsoluteLayout} class is a layout manager that lays out panel's
 * widgets according to absolute positions.
 * <p>
 * The following code lays out two button, next to each other:
 * 
 * <pre>
 * LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout(100, 100));
 * layoutPanel.add(new Button("1"), new AbsoluteLayoutData(5, 5,  90, 40));
 * layoutPanel.add(new Button("2"), new AbsoluteLayoutData(5, 55, 90, 40));
 * </pre>
 * <p>
 * The following code lays out two buttons, just like in the previous example,
 * but the right button ("2") reposition itself when the layout panel is
 * resized. (The margin policy for the second button states that the margin
 * should be expanded to the left of the widget)
 * 
 * <pre>
 * LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout(100, 100));
 * layoutPanel.add(new Button("1"), new AbsoluteLayoutData(5, 5,  40, 25, MarginPolicy.RIGHT));
 * layoutPanel.add(new Button("2"), new AbsoluteLayoutData(55, 5, 40, 25, MarginPolicy.LEFT));
 * </pre>
 * 
 * @see AbsoluteLayoutData
 * @author johan.rydberg(at)gmail.com
 */
public class AbsoluteLayout extends BaseLayout {

  /**
   * Used with layout data {@code AbsoluteLayoutData} to specify how a widget
   * should be repositioned when the layout panel is resized.
   */
  public enum MarginPolicy {
    NONE(false, false, false, false), LEFT(true, false, false, false), RIGHT(
        false, true, false, false), TOP(false, false, true, false), BOTTOM(
        false, false, false, true), VCENTER(false, false, true, true), HCENTER(
        true, true, false, false), CENTER(true, true, true, true);

    final boolean left, right, top, bottom;

    MarginPolicy(boolean l, boolean r, boolean t, boolean b) {
      left = l;
      right = r;
      top = t;
      bottom = b;
    }
  }

  /**
   * Used with layout data {@code AbsoluteLayoutData} to specify how a widget
   * should be redimensioned when the layout panel is resized.
   */
  public enum DimensionPolicy {
    NONE(false, false), WIDTH(true, false), HEIGHT(false, true), BOTH(true,
        true);

    final boolean width, height;

    DimensionPolicy(boolean w, boolean h) {
      width = w;
      height = h;
    }
  }

  /**
   * Dimensions of the panel.
   */
  private int panelWidth, panelHeight;

  /**
   * Constructor for absolute layout with the specified dimensions.
   * 
   * @param width initial width of the panel
   * @param height initial height of the panel
   */
  public AbsoluteLayout(int width, int height) {
    panelWidth = width;
    panelHeight = height;
  }

  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};

    try {
      if (layoutPanel == null) {
        return result;
      }

      result[0] = panelWidth;
      result[1] = panelHeight;
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    return result;
  }

  /**
   * Lays out the specified {@link LayoutPanel} using this layout.
   * 
   * @param layoutPanel the panel in which to do the layout
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null) {
        return;
      }

      final int[] box = DOM.getClientSize(layoutPanel.getElement());
      int totalWidth = box[0];
      int totalHeight = box[1];

      final int deltaX = totalWidth - panelWidth;
      final int deltaY = totalHeight - panelHeight;

      final int size = layoutPanel.getWidgetCount();

      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        AbsoluteLayoutData layoutData = (AbsoluteLayoutData) getLayoutData(child);
        if (layoutData == null) {
          layoutData = new AbsoluteLayoutData(0, 0);
          setLayoutData(child, layoutData);
        }

        if (layoutData.widgetWidth == -1) {
          int flowWidth = getFlowWidth(child);
          layoutData.widgetWidth = flowWidth;
        }

        if (layoutData.widgetHeight == -1) {
          int flowHeight = getFlowHeight(child);
          layoutData.widgetHeight = flowHeight;
        }

        int dw = 0;
        if (layoutData.marginPolicy.left)
          dw++;
        if (layoutData.marginPolicy.right)
          dw++;
        if (layoutData.dimensionPolicy.width)
          dw++;

        int dh = 0;
        if (layoutData.marginPolicy.top)
          dh++;
        if (layoutData.marginPolicy.bottom)
          dh++;
        if (layoutData.dimensionPolicy.height)
          dh++;

        if (layoutData.marginPolicy.left)
          layoutData.posLeft += (deltaX / dw);
        if (layoutData.dimensionPolicy.width)
          layoutData.widgetWidth += (deltaX / dw);

        if (layoutData.marginPolicy.top)
          layoutData.posTop += (deltaY / dh);
        if (layoutData.dimensionPolicy.height)
          layoutData.widgetHeight += (deltaY / dh);

        WidgetHelper.setBounds(layoutPanel, child, layoutData.posLeft, layoutData.posTop,
            layoutData.widgetWidth, layoutData.widgetHeight);

      }

      panelWidth = totalWidth;
      panelHeight = totalHeight;

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }
}