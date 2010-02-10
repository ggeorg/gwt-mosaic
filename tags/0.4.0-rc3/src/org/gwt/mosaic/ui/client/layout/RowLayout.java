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

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class RowLayout extends BaseLayout {

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    final Dimension result = new Dimension();

    if (!init(layoutPanel)) {
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
    height += ((size - 1) * spacing);

    Dimension decPanelFrameSize = null;

    int maxWidth = 0;

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

      int w = preferredWidthMeasure.sizeOf(child);
      height += preferredHeightMeasure.sizeOf(child);

      if (parent instanceof InternalDecoratorPanel) {
        w += decPanelFrameSize.width;
        height += decPanelFrameSize.height;
      }

      maxWidth = Math.max(maxWidth, w);
    }

    result.width = width + maxWidth;
    result.height = height;

    return result;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  public void layoutPanel(LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    if (!init(layoutPanel)) {
      return;
    }

    final int size = visibleChildList.size();
    if (size == 0) {
      return;
    }

    final Dimension box = DOM.getClientSize(layoutPanel.getElement());
    final int spacing = layoutPanel.getWidgetSpacing();

    final int left = paddingLeftMeasure.sizeOf(layoutPanel);
    int top = paddingTopMeasure.sizeOf(layoutPanel);
    int width = box.width - (left + paddingRightMeasure.sizeOf(layoutPanel));
    int height = box.height - (top + paddingBottomMeasure.sizeOf(layoutPanel));

    int fillHeight = height;

    fillHeight -= ((size - 1) * spacing);

    int fillingHeight = 0;

    Dimension decPanelFrameSize = null;

    // 1st pass
    for (Widget child : visibleChildList) {
      if (child instanceof InternalDecoratorPanel) {
        child = ((InternalDecoratorPanel) child).getWidget();
      }

      RowLayoutData layoutData = getLayoutData(child);

      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
            borderSizes[0] + borderSizes[0]);
      }

      if (layoutData.getPreferredHeight() == null) {
        fillingHeight += layoutData.getFlexibility();
      } else {
        layoutData.calcHeight = preferredHeightMeasure.sizeOf(child);
        if (parent instanceof InternalDecoratorPanel) {
          layoutData.calcHeight += decPanelFrameSize.height;
        }
        fillHeight -= layoutData.calcHeight;
      }
    }

    // 2nd pass
    for (int i = 0, n = visibleChildList.size(); i < n; i++) {
      Widget child = visibleChildList.get(i);

      if (child instanceof InternalDecoratorPanel) {
        child = ((InternalDecoratorPanel) child).getWidget();
      }

      final RowLayoutData layoutData = (RowLayoutData) child.getLayoutData();

      int w = width;
      int h = layoutData.calcHeight;

      if (layoutData.getPreferredHeight() == null) {
        h = (int) (fillHeight * ((double) layoutData.getFlexibility() / fillingHeight));
      }

      int fw = w;
      int fh = h;

      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
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
          layoutData.splitBar = new RowLayoutSplitBar(layoutPanel, child,
              visibleChildList.get(i + 1));
          layoutPanel.add(layoutData.splitBar);
        }

        if (!layoutData.splitBar.isAttached()) {
          layoutPanel.add(layoutData.splitBar);
        }

        // update the bottom widget
        layoutData.splitBar.widgetB = visibleChildList.get(i + 1);

        WidgetHelper.setBounds(layoutPanel, layoutData.splitBar, left, top + h,
            width, spacing);
      }

      top += (h + spacing);

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
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#init(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    for (Iterator<Widget> iter = visibleChildList.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      if (widget instanceof RowLayoutSplitBar) {
        iter.remove();
        continue;
      }

    }

    return initialized = true;
  }
  
  private RowLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof RowLayoutData)) {
      layoutDataObject = new RowLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (RowLayoutData) layoutDataObject;
  }

}
