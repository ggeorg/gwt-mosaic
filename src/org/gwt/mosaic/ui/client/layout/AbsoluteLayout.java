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

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Point;
import org.gwt.mosaic.core.client.util.FloatParser;
import org.gwt.mosaic.core.client.util.UnitParser;
import org.gwt.mosaic.ui.client.layout.LayoutData.ParsedSize;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
    NONE(false, false, false, false), BOTTOM(false, false, false, true), TOP(
        false, false, true, false),

    VCENTER(false, false, true, true), TOP_BOTTOM(false, false, true, true),

    RIGHT(false, true, false, false), RIGHT_BOTTOM(false, true, false, true), RIGHT_TOP(
        false, true, true, false), RIGHT_TOP_BOTTOM(false, true, true, true),

    LEFT(true, false, false, false), LEFT_BOTTOM(true, false, false, true), LEFT_TOP(
        true, false, true, false), LEFT_TOP_BOTTOM(true, false, true, true),

    HCENTER(true, true, false, false), LEFT_RIGHT(true, true, false, false),

    LEFT_RIGHT_BOTTOM(true, true, false, true),

    CENTER(true, true, true, true), ALL(true, true, true, true);

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
  private ParsedSize panelWidth, panelHeight;

  /**
   * Constructor for absolute layout with the specified dimensions.
   * 
   * @param width initial width of the panel
   * @param height initial height of the panel
   */
  public AbsoluteLayout(int width, int height) {
    this(width + "px", height + "px");
  }

  /**
   * Constructor for absolute layout with the specified dimensions.
   * 
   * @param width initial width of the panel
   * @param height initial height of the panel
   */
  public AbsoluteLayout(String width, String height) {
    super();
    setPanelWidth(width);
    setPanelHeight(height);
  }

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

      result.width = layoutPanel.toPixelSize(panelWidth, true);
      result.height = layoutPanel.toPixelSize(panelHeight, false);

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

  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      syncDecoratorVisibility(widget);

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      }

      visibleChildList.add(widget);
    }

    return initialized = true;
  }

  /**
   * Lays out the specified {@link LayoutPanel} using this layout.
   * 
   * @param layoutPanel the panel in which to do the layout
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      final int totalWidth = box.width;
      final int totalHeight = box.height;

      final int panelWidth = layoutPanel.toPixelSize(this.panelWidth, true);
      final int panelHeight = layoutPanel.toPixelSize(this.panelHeight, false);

      final double deltaX = totalWidth - panelWidth;
      final double deltaY = totalHeight - panelHeight;

      Dimension decPanelFrameSize = null;

      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        AbsoluteLayoutData layoutData = getAbsoluteLayoutData(child);

        if (layoutData.hasDecoratorPanel()) {
          decPanelFrameSize = getDecoratorFrameSize(layoutData.decoratorPanel,
              child);
        }

        Dimension clientSize = new Dimension(
            preferredWidthMeasure.sizeOf(child),
            preferredHeightMeasure.sizeOf(child));

        if (clientSize.width == -1) {
          clientSize.width = getPreferredSize(layoutPanel, child, layoutData).width;
        }

        if (clientSize.height == -1) {
          clientSize.height = getPreferredSize(layoutPanel, child, layoutData).height;
        }

        Point point = new Point(layoutPanel.toPixelSize(layoutData.posLeft,
            true), layoutPanel.toPixelSize(layoutData.posTop, false));

        int fw = clientSize.width;
        int fh = clientSize.height;

        if (layoutData.hasDecoratorPanel()) {
          fw -= decPanelFrameSize.width;
          fh -= decPanelFrameSize.height;
        }

        if (layoutData.marginPolicy.left && layoutData.marginPolicy.right) {
          point.x += deltaX / 2;
        } else if (layoutData.marginPolicy.left) {
          point.x += deltaX;
        }

        if (layoutData.marginPolicy.top && layoutData.marginPolicy.bottom) {
          point.y += deltaY / 2;
        } else if (layoutData.marginPolicy.top) {
          point.y += deltaY;
        }

        if (layoutData.dimensionPolicy.width)
          fw += deltaX;
        if (layoutData.dimensionPolicy.height)
          fh += deltaY;

        layoutData.targetLeft = point.x;
        layoutData.targetTop = point.y;
        layoutData.targetWidth = fw;
        layoutData.targetHeight = fh;

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
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

  private AbsoluteLayoutData getAbsoluteLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof AbsoluteLayoutData)) {
      layoutDataObject = new AbsoluteLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (AbsoluteLayoutData) layoutDataObject;
  }

  /**
   * @return the panelWidth
   */
  public ParsedSize getPanelWidth() {
    return panelWidth;
  }

  /**
   * @param panelWidth the panelWidth to set
   */
  public void setPanelWidth(String panelWidth) {
    this.panelWidth = new ParsedSize(FloatParser.parseFloat(panelWidth, 0.0f),
        UnitParser.parseUnit(panelWidth, Unit.PX));
  }

  /**
   * @return the panelHeight
   */
  public ParsedSize getPanelHeight() {
    return panelHeight;
  }

  /**
   * @param panelHeight the panelHeight to set
   */
  public void setPanelHeight(String panelHeight) {
    this.panelHeight = new ParsedSize(
        FloatParser.parseFloat(panelHeight, 0.0f), UnitParser.parseUnit(
            panelHeight, Unit.PX));
  }
}