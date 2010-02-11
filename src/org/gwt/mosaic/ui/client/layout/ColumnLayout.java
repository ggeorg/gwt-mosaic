/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ColumnLayout extends BaseLayout {

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

      int width = marginLeftMeasure.sizeOf(layoutPanel)
          + marginRightMeasure.sizeOf(layoutPanel)
          + borderLeftMeasure.sizeOf(layoutPanel)
          + borderRightMeasure.sizeOf(layoutPanel)
          + paddingLeftMeasure.sizeOf(layoutPanel)
          + paddingRightMeasure.sizeOf(layoutPanel);

      int height = marginTopMeasure.sizeOf(layoutPanel)
          + marginBottomMeasure.sizeOf(layoutPanel)
          + borderTopMeasure.sizeOf(layoutPanel)
          + borderBottomMeasure.sizeOf(layoutPanel)
          + paddingTopMeasure.sizeOf(layoutPanel)
          + paddingBottomMeasure.sizeOf(layoutPanel);

      final int size = visibleChildList.size();
      if (size == 0) {
        result.width = width;
        result.height = height;
        return result;
      }

      final int spacing = layoutPanel.getWidgetSpacing();
      width += ((size - 1) * spacing);

      Dimension decPanelFrameSize = null;

      int maxHeight = 0;

      for (Widget child : visibleChildList) {
        if (child instanceof InternalDecoratorPanel) {
          child = ((InternalDecoratorPanel) child).getWidget();
        }

        final Widget parent = child.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
              borderSizes[0] + borderSizes[0]);
        }

        width += preferredWidthMeasure.sizeOf(child);
        int h = preferredHeightMeasure.sizeOf(child);

        if (parent instanceof InternalDecoratorPanel) {
          width += decPanelFrameSize.width;
          h += decPanelFrameSize.height;
        }

        maxHeight = Math.max(maxHeight, h);
      }

      result.width = width;
      result.height = height + maxHeight;

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

      int left = paddingLeftMeasure.sizeOf(layoutPanel);
      int top = paddingTopMeasure.sizeOf(layoutPanel);
      final int width = box.width
          - (left + paddingRightMeasure.sizeOf(layoutPanel));
      final int height = box.height
          - (top + paddingBottomMeasure.sizeOf(layoutPanel));

      int fillWidth = width;

      fillWidth -= ((size - 1) * spacing);

      int fillingWidth = 0;

      Dimension decPanelFrameSize = null;

      // 1st pass
      for (Widget child : visibleChildList) {
        if (child instanceof InternalDecoratorPanel) {
          child = ((InternalDecoratorPanel) child).getWidget();
        }

        ColumnLayoutData layoutData = getLayoutData(child);

        final Widget parent = child.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
              borderSizes[0] + borderSizes[0]);
        }

        if (layoutData.getPreferredWidth() == null) {
          fillingWidth += layoutData.getFlexibility();
        } else {
          layoutData.calcWidth = preferredWidthMeasure.sizeOf(child);
          if (parent instanceof InternalDecoratorPanel) {
            layoutData.calcWidth += decPanelFrameSize.width;
          }
          fillWidth -= layoutData.calcWidth;
        }
      }

      // 2nd pass
      for (int i = 0, n = visibleChildList.size(); i < n; i++) {
        Widget child = visibleChildList.get(i);

        if (child instanceof InternalDecoratorPanel) {
          child = ((InternalDecoratorPanel) child).getWidget();
        }

        final ColumnLayoutData layoutData = (ColumnLayoutData) child.getLayoutData();

        int w = layoutData.calcWidth;
        int h = height;

        if (layoutData.getPreferredWidth() == null) {
          w = (int) (fillWidth * ((double) layoutData.getFlexibility() / fillingWidth));
        }

        int fw = w;
        int fh = h;

        final Widget parent = child.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          fw -= decPanelFrameSize.width;
          fh -= decPanelFrameSize.height;
        }

        top = Math.max(0, top);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = fw;
        layoutData.targetHeight = fh;

        if (i < n - 1) {
          if (layoutData.splitBar == null
              || layoutPanel != layoutData.splitBar.getBoundaryPanel()) {
            layoutData.splitBar = new ColumnLayoutSplitBar(layoutPanel, child,
                visibleChildList.get(i + 1));
            layoutPanel.add(layoutData.splitBar);
          }

          if (!layoutData.splitBar.isAttached()) {
            layoutPanel.add(layoutData.splitBar);
          }

          // update the right widget
          layoutData.splitBar.widgetR = visibleChildList.get(i + 1);

          WidgetHelper.setBounds(layoutPanel, layoutData.splitBar, left + w,
              top, spacing, height);
        }

        left += (w + spacing);

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(child.getAbsoluteLeft()
              - layoutPanel.getAbsoluteLeft()
              - paddingLeftMeasure.sizeOf(layoutPanel));
          layoutData.setSourceTop(child.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop()
              - paddingTopMeasure.sizeOf(layoutPanel));
          layoutData.setSourceWidth(child.getOffsetWidth());
          layoutData.setSourceHeight(child.getOffsetHeight());
        }
      }

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }
  }

  private ColumnLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof ColumnLayoutData)) {
      layoutDataObject = new ColumnLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (ColumnLayoutData) layoutDataObject;
  }

  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    for (Iterator<Widget> iter = visibleChildList.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      if (widget instanceof ColumnLayoutSplitBar) {
        iter.remove();
        continue;
      }

    }

    return initialized = true;
  }

}
