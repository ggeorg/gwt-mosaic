/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.Viewport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
 * decorated (the {@code Button} will be placed inside a {@code
 * com.google.gwt.user.client.ui.DecoratorPanel}):
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="FillLayout1.jpg"></td>
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
 * <td><img border="1" src="FillLayout2.jpg"></td>
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
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see FillLayoutData
 */
public class FillLayout extends BaseLayout implements HasAlignment {

  private Widget child;
  private FillLayoutData layoutData;

  private Map<Widget, Dimension> widgetSizes = new HashMap<Widget, Dimension>();

  private boolean runTwiceFlag;

  private HorizontalAlignmentConstant horizontalAlignment;

  private VerticalAlignmentConstant verticalAlignment;

  @Override
  public void flushCache() {
    widgetSizes.clear();
    initialized = false;
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.
   * mosaic.ui.client.layout.LayoutPanel)
   */
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};

    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

      final Dimension dim = widgetSizes.get(child);
      if (dim == null) {
        result[0] = getFlowWidth(child);
        result[1] = getFlowHeight(child);
      } else {
        result[0] = dim.getWidth();
        result[1] = dim.getHeight();
      }

      if (layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.decoratorPanel;
        result[0] += decPanel.getOffsetWidth() - child.getOffsetWidth();
        result[1] += decPanel.getOffsetHeight() - child.getOffsetHeight();
      }

      result[0] += (margins[1] + margins[3]) + (paddings[1] + paddings[3])
          + (borders[1] + borders[3]);
      result[1] += (margins[0] + margins[2]) + (paddings[0] + paddings[2])
          + (borders[0] + borders[2]);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.getLocalizedMessage());
    }

    return result;
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    final int size = layoutPanel.getWidgetCount();

    for (int i = 0; i < size; i++) {
      child = layoutPanel.getWidget(i);
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
      layoutData = (FillLayoutData) layoutDataObject;

      initialized = true;

      break;
    }
    
    return initialized;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.gwt.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.gwt.mosaic
   * .ui.client.layout.LayoutPanel)
   */
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final int[] box = DOM.getClientSize(layoutPanel.getElement());

      final int left = paddings[3];
      final int top = paddings[0];
      int width = box[0] - (paddings[1] + paddings[3]);
      int height = box[1] - (paddings[0] + paddings[2]);

      if (layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.decoratorPanel;
        final int offsetWidth = decPanel.getOffsetWidth()
            - child.getOffsetWidth();
        final int offsetHeight = decPanel.getOffsetHeight()
            - child.getOffsetHeight();
        width -= offsetWidth;
        height -= offsetHeight;
      }

      HorizontalAlignmentConstant hAlignment = layoutData.getHorizontalAlignment();
      if (hAlignment == null) {
        hAlignment = getHorizontalAlignment();
      }

      int posLeft;
      int widgetWidth;

      runTwiceFlag = false;

      if (hAlignment == null) {
        posLeft = left;
        widgetWidth = width;
      } else if (HasHorizontalAlignment.ALIGN_LEFT == hAlignment) {
        posLeft = left;
        widgetWidth = -1;
      } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
        Dimension dim = widgetSizes.get(child);
        if (dim == null) {
          widgetSizes.put(child, dim = new Dimension(getFlowWidth(child),
              getFlowHeight(child)));
          runTwiceFlag = true;
        }
        posLeft = left + (width / 2) - dim.getWidth() / 2;
        widgetWidth = -1;
      } else {
        Dimension dim = widgetSizes.get(child);
        if (dim == null) {
          widgetSizes.put(child, dim = new Dimension(getFlowWidth(child),
              getFlowHeight(child)));
          runTwiceFlag = true;
        }
        posLeft = left + width - dim.getWidth();
        widgetWidth = -1;
      }

      VerticalAlignmentConstant vAlignment = layoutData.getVerticalAlignment();
      if (vAlignment == null) {
        vAlignment = getVerticalAlignment();
      }

      int posTop;
      int widgetHeight;

      if (vAlignment == null) {
        posTop = top;
        widgetHeight = height;
      } else if (HasVerticalAlignment.ALIGN_TOP == vAlignment) {
        posTop = top;
        widgetHeight = -1;
      } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
        Dimension dim = widgetSizes.get(child);
        if (dim == null) {
          widgetSizes.put(child, dim = new Dimension(getFlowWidth(child),
              getFlowHeight(child)));
          runTwiceFlag = true;
        }
        posTop = top + (height / 2) - dim.getHeight() / 2;
        widgetHeight = -1;
      } else {
        Dimension dim = widgetSizes.get(child);
        if (dim == null) {
          widgetSizes.put(child, dim = new Dimension(getFlowWidth(child),
              getFlowHeight(child)));
          runTwiceFlag = true;
        }
        posTop = top + height - dim.getHeight();
        widgetHeight = -1;
      }

      setBounds(layoutPanel, child, posLeft, posTop, widgetWidth, widgetHeight);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.getLocalizedMessage());
    }

    if (runTwice()) {
      recalculate(widgetSizes);
    }

  }

  @Override
  public boolean runTwice() {
    return runTwiceFlag;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }

}
