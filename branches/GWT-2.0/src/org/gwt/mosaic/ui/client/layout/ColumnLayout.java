package org.gwt.mosaic.ui.client.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class ColumnLayout extends BaseLayout {

  private List<ColumnLayoutSplitBar> splitBars = new ArrayList<ColumnLayoutSplitBar>();

  public ColumnLayout() {
    super();
  }

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

      result.width += ((size - 1) * spacing);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".getPreferredSize() : "
          + e.getMessage());
    }

    return result;
  }

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

      int fillWidth = width;

      fillWidth -= ((size - 1) * spacing);

      int fillingWidth = 0;

      // 1st pass
      for (Widget child : visibleChildList) {
        ColumnLayoutData layoutData = getColumnLayoutData(child);
        //        
        // if (child instanceof Collapsible) {
        // if (((Collapsible)child).isCollapsed()) {
        // fillWidth -= getPreferredSize(layoutPanel, child, layoutData).width;
        // } else {
        // fillingWidth++;
        // }
        // } else {
        fillingWidth += layoutData.getFlexibility();
        // }
      }

      // 2nd pass
      for (int i = 0, n = visibleChildList.size(); i < n; i++) {
        Widget child = visibleChildList.get(i);
        ColumnLayoutData layoutData = getColumnLayoutData(child);

        int w = (int) (fillWidth * ((double) layoutData.getFlexibility() / fillingWidth));

        top = Math.max(0, top);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = w;
        layoutData.targetHeight = height;

        if (i < n - 1) {
          ColumnLayoutSplitBar splitBar = splitBars.get(i);
          if (!splitBar.isAttached()) {
            layoutPanel.addImpl(splitBar);
          }
          WidgetHelper.setBounds(layoutPanel, splitBar, left + w, top, spacing,
              height);
        }

        left += (w + spacing);

        layoutData.sourceLeft = child.getAbsoluteLeft()
            - layoutPanel.getAbsoluteLeft();
        layoutData.sourceTop = child.getAbsoluteTop()
            - layoutPanel.getAbsoluteTop();
        layoutData.sourceWidth = child.getOffsetWidth();
        layoutData.sourceHeight = child.getOffsetHeight();
      }

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }
  }

  private ColumnLayoutData getColumnLayoutData(Widget child) {
    Object layoutDataObject = getLayoutData(child);
    if (layoutDataObject == null
        || !(layoutDataObject instanceof ColumnLayoutData)) {
      layoutDataObject = new ColumnLayoutData();
      setLayoutData(child, layoutDataObject);
    }
    return (ColumnLayoutData) layoutDataObject;
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
      splitBars.add(new ColumnLayoutSplitBar(layoutPanel, visibleChildList,
          visibleChildList.get(i), visibleChildList.get(i + 1)));
    }

    return initialized = true;
  }

}
