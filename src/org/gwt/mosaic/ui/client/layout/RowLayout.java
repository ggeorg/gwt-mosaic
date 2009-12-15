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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class RowLayout extends BaseLayout {

  private List<RowLayoutSplitBar> splitBars = new ArrayList<RowLayoutSplitBar>();

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension();
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

      int width = (margins[1] + margins[3]) + (paddings[1] + paddings[3])
          + (borders[1] + borders[3]);
      int height = (margins[0] + margins[2]) + (paddings[0] + paddings[2])
          + (borders[0] + borders[2]);

      final int size = visibleChildList.size();
      if (size == 0) {
        result.width = width;
        result.height = height;
        return result;
      }

      final int spacing = layoutPanel.getWidgetSpacing();
      height += ((size - 1) * spacing);

      Dimension decPanelFrameSize = null;

      int maxWidth = 0;

      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        RowLayoutData layoutData = getRowLayoutData(child);

        if (layoutData.hasDecoratorPanel()) {
          decPanelFrameSize = getDecoratorFrameSize(layoutData.decoratorPanel,
              child);
        }

        int w = preferredWidthMeasure.sizeOf(child);
        height += preferredHeightMeasure.sizeOf(child);

        if (layoutData.hasDecoratorPanel()) {
          w += decPanelFrameSize.width;
          height += decPanelFrameSize.height;
        }

        maxWidth = Math.max(maxWidth, w);
      }

      result.width = width + maxWidth;
      result.height = height;

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(getClass().getName() + ".getPreferredSize() : "
          + e.getMessage());
    }

    return result;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final int size = visibleChildList.size();
      if (size == 0) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());
      final int spacing = layoutPanel.getWidgetSpacing();

      int width = box.width - (paddings[1] + paddings[3]);
      int height = box.height - (paddings[0] + paddings[2]);
      int left = paddings[3];
      int top = paddings[0];

      int fillHeight = height;

      fillHeight -= ((size - 1) * spacing);

      int fillingHeight = 0;

      Dimension decPanelFrameSize = null;

      // 1st pass
      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        RowLayoutData layoutData = getRowLayoutData(child);

        if (layoutData.hasDecoratorPanel()) {
          decPanelFrameSize = getDecoratorFrameSize(layoutData.decoratorPanel,
              child);
        }

        if (layoutData.getPreferredHeight() == null) {
          fillingHeight += layoutData.getFlexibility();
        } else {
          layoutData.calcHeight = preferredHeightMeasure.sizeOf(child);
          if (layoutData.hasDecoratorPanel()) {
            layoutData.calcHeight += decPanelFrameSize.height;
          }
          fillHeight -= layoutData.calcHeight;
        }
      }

      // 2nd pass
      for (int i = 0, n = visibleChildList.size(); i < n; i++) {
        Widget child = visibleChildList.get(i);

        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        final RowLayoutData layoutData = (RowLayoutData) child.getLayoutData();

        int w = width;
        int h = layoutData.calcHeight;

        if (layoutData.getPreferredHeight() == null) {
          h = (int) (fillHeight * ((double) layoutData.getFlexibility() / fillingHeight));
        }

        int fw = w;
        int fh = h;

        if (layoutData.hasDecoratorPanel()) {
          fw -= decPanelFrameSize.width;
          fh -= decPanelFrameSize.height;
        }

        top = Math.max(0, top);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = fw;
        layoutData.targetHeight = fh;

        if (i < n - 1) {
          RowLayoutSplitBar splitBar = splitBars.get(i);
          if (!splitBar.isAttached()) {
            layoutPanel.addImpl(splitBar);
          }
          WidgetHelper.setBounds(layoutPanel, splitBar, left, top + h, width,
              spacing);
        }

        top += (h + spacing);

        layoutData.setSourceLeft(child.getAbsoluteLeft()
            - layoutPanel.getAbsoluteLeft() - paddings[3]);
        layoutData.setSourceTop(child.getAbsoluteTop()
            - layoutPanel.getAbsoluteTop() - paddings[0]);
        layoutData.setSourceWidth(child.getOffsetWidth());
        layoutData.setSourceHeight(child.getOffsetHeight());
      }

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }
  }

  private RowLayoutData getRowLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof RowLayoutData)) {
      layoutDataObject = new RowLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (RowLayoutData) layoutDataObject;
  }

  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    for (Widget splitBar : splitBars) {
      splitBar.removeFromParent();
    }
    splitBars.clear();

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      }

      visibleChildList.add(widget);
    }

    for (int i = 0, n = visibleChildList.size() - 1; i < n; i++) {
      splitBars.add(new RowLayoutSplitBar(layoutPanel, visibleChildList.get(i),
          visibleChildList.get(i + 1)));
    }

    return initialized = true;
  }

}
