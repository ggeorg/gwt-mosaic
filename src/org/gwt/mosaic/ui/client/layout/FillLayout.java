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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.Viewport;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@code FillLayout} lays out the first visible widget forcing it to completely
 * fill the available space. The widget that {@code FillLayout} lays out can
 * have an associated layout data object, called {@link FillLayoutData}.
 * <p>
 * {@code FillLayout} is the default layout manager for {@link LayoutPanel}. In
 * the following example two {@code Buttons} are placed inside a
 * {@link LayoutPanel}. Notice that only the first {@code Button} is rendered.
 * The associated layout data object declares that the {@code Button} will be
 * decorated (the {@code Button} will be placed inside a
 * {@code com.google.gwt.user.client.ui.DecoratorPanel}):
 * 
 * <table>
 * <tr>
 * <td> <img border="1" src="FillLayout1.jpg"> </td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   LayoutPanel panel = new LayoutPanel();
 *   panel.setPixelSize(320, 200);
 *   panel.setPadding(20);
 *   
 *   panel.add(new Button("Button 1"), new FillLayoutData(true));
 *   
 *   // This button is not rendered!
 *   panel.add(new Button("Button 2"), new FillLayoutData(true));
 *   
 *   RootPanel.get().add(panel);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * In the next example a {@code Button} placed inside a {@link Viewport} is
 * sized to completely fill the browser's content area:
 * 
 * <table>
 * <tr>
 * <td> <img border="1" src="FillLayout2.jpg"> </td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *   
 *   viewport.add(new Button("Button 1"));
 *   
 *   RootPanel.get().add(viewport);
 * }
 * </td>
 * </tr></table>
 * 
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class FillLayout extends BaseLayout {

  /**
   * {@inheritDoc}
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

      for (int i = 0; i < size; i++) {
        Widget child = layoutPanel.getWidget(i);
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null
            || !(layoutDataObject instanceof FillLayoutData)) {
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

      final int[] margins = DOM.getMarginSizes(layoutPanel.getElement());
      result[0] += (margins[1] + margins[3]);
      result[1] += (margins[0] + margins[2]);

      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());
      result[0] += (paddings[1] + paddings[3]);
      result[1] += (paddings[0] + paddings[2]);

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    layoutPanel.setPreferredSize(result[0], result[1]);

    return result;
  }

  /**
   * {@inheritDoc}
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
        if (layoutDataObject == null
            || !(layoutDataObject instanceof FillLayoutData)) {
          layoutDataObject = new FillLayoutData();
          setLayoutData(child, layoutDataObject);
        }
        FillLayoutData layoutData = (FillLayoutData) layoutDataObject;

        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.getDecoratorPanel();
          final int offsetWidth = decPanel.getOffsetWidth()
              - child.getOffsetWidth();
          final int offsetHeight = decPanel.getOffsetHeight()
              - child.getOffsetHeight();
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

    layoutPanel.setPreferredSize(-1, -1);
  }

}
