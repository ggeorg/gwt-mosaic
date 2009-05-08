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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

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

  private boolean runTwiceFlag;

  private HorizontalAlignmentConstant horizontalAlignment;

  private VerticalAlignmentConstant verticalAlignment;

  @Override
  public void flushCache() {
    initialized = false;
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension();

    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

      result.setSize(WidgetHelper.getPreferredSize(child));

      if (layoutData.hasDecoratorPanel()) {
        final Dimension d = getDecoratorFrameSize(layoutData.decoratorPanel,
            child);
        result.width += d.width;
        result.height += d.height;
      }

      result.width += (margins[1] + margins[3]) + (paddings[1] + paddings[3])
          + (borders[1] + borders[3]);
      result.height += (margins[0] + margins[2]) + (paddings[0] + paddings[2])
          + (borders[0] + borders[2]);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);

      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.toString());
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

    for (int i = 0, size = layoutPanel.getWidgetCount(); i < size; i++) {
      child = layoutPanel.getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      if (!DOM.isVisible(child.getElement())) {
        continue;
      }

      layoutData = getFillLayoutData(child);

      initialized = true;

      break;
    }

    return initialized;
  }

  private FillLayoutData getFillLayoutData(Widget child) {
    Object layoutDataObject = getLayoutData(child);
    if (layoutDataObject == null
        || !(layoutDataObject instanceof FillLayoutData)) {
      layoutDataObject = new FillLayoutData();
      setLayoutData(child, layoutDataObject);
    }
    return (FillLayoutData) layoutDataObject;
  }

  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      final int left = paddings[3];
      final int top = paddings[0];
      int width = box.width - (paddings[1] + paddings[3]);
      int height = box.height - (paddings[0] + paddings[2]);

      if (layoutData.hasDecoratorPanel()) {
        final Dimension d = getDecoratorFrameSize(layoutData.decoratorPanel,
            child);
        width -= d.width;
        height -= d.height;
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
        runTwiceFlag = true;
      } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
        posLeft = left + (width / 2)
            - WidgetHelper.getPreferredSize(child).width / 2;
        widgetWidth = -1;
        runTwiceFlag = true;
      } else {
        posLeft = left + width - WidgetHelper.getPreferredSize(child).width;
        widgetWidth = -1;
        runTwiceFlag = true;
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
        runTwiceFlag = true;
      } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
        posTop = top + (height / 2)
            - WidgetHelper.getPreferredSize(child).height / 2;
        widgetHeight = -1;
        runTwiceFlag = true;
      } else {
        posTop = top + height - WidgetHelper.getPreferredSize(child).height;
        widgetHeight = -1;
        runTwiceFlag = true;
      }

      WidgetHelper.setBounds(layoutPanel, child, posLeft, posTop, widgetWidth,
          widgetHeight);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);

      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.toString());
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
