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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class FlowLayout extends BaseLayout {

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
      int width = box.width - (left + paddingRightMeasure.sizeOf(layoutPanel));
      int height = box.height
          - (top + paddingBottomMeasure.sizeOf(layoutPanel));

      Dimension decPanelFrameSize = null;

      int maxHeight = 0;

      for (Widget child : visibleChildList) {

        if (child instanceof InternalDecoratorPanel) {
          child = ((InternalDecoratorPanel) child).getWidget();
        }

        final FlowLayoutData layoutData = getLayoutData(child);

        final Widget parent = child.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
              borderSizes[0] + borderSizes[0]);
        }

        int w = preferredWidthMeasure.sizeOf(child);
        int h = preferredHeightMeasure.sizeOf(child);

        int fw = w;
        int fh = h;

        if (parent instanceof InternalDecoratorPanel) {
          fw -= decPanelFrameSize.width;
          fh -= decPanelFrameSize.height;
        }

        if (left + w > width) {
          left = paddingLeftMeasure.sizeOf(layoutPanel);
          top += (maxHeight + spacing);
          maxHeight = h;
        } else {
          maxHeight = Math.max(maxHeight, h);
        }

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = fw;
        layoutData.targetHeight = fh;

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

  private FlowLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof FlowLayoutData)) {
      layoutDataObject = new FlowLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (FlowLayoutData) layoutDataObject;
  }
}
