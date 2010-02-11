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
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A border layout lays out a {@link LayoutPanel}, arranging and resizing its
 * widgets to fit in five regions: {@link Region#NORTH}, {@link Region#SOUTH},
 * {@link Region#EAST}, {@link Region#WEST}, and {@link Region#CENTER}. Each
 * region will render the first visible widget added to that region. The region
 * of the widget added to a {@link LayoutPanel} with a {@code BorderLayout} can
 * be specified by setting a {@link BorderLayoutData} object into the widget
 * using {@link LayoutPanel#add(Widget, LayoutData)}, for example:
 * 
 * <pre>
 * LayoutPanel panel = new LayoutPanel(new BorderLayout());
 * panel.add(new Button("Button 1"), new BorderLayoutData(BorderLayoutRegion.SOUTH));
 * </pre>
 * 
 * <p>
 * As a convenience, {@code BorderLayout} interprets the absence of a
 * {@link Region} specification the same as the {@link Region#CENTER}:
 * 
 * <pre>
 * LayoutPanel panel = new LayoutPanel(new BorderLayout());
 * panel.add(new Button("Button 1"), new BorderLayoutData(true));
 * </pre>
 * 
 * or
 * 
 * <pre>
 * LayoutPanel panel = new LayoutPanel(new BorderLayout());
 * panel.add(new Button("Button 1"));
 * </pre>
 * 
 * <p>
 * The components are laid out according to their preferred sizes or the width
 * and height specified by a {@link BorderLayoutData} object. The
 * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
 * horizontally; the {@link Region#EAST} and {@link Region#WEST} child widgets
 * are stretched vertically; the {@link Region#CENTER} child widget will be
 * stretched both horizontally and vertically to fill any space left over.
 * 
 * <p>
 * Here is an example of five buttons laid out using the {@code BorderLayout}
 * layout manager. The {@link LayoutPanel} is added decorated to a
 * {@link Viewport} so that it fills all browser's content area:
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BorderLayout1.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *
 *   LayoutPanel panel = new LayoutPanel(new BorderLayout());
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button("Button 1"), new BorderLayoutData(Region.NORTH));
 *   panel.add(new Button("Button 2"), new BorderLayoutData(Region.SOUTH));
 *   panel.add(new Button("Button 3"), new BorderLayoutData(Region.WEST));
 *   panel.add(new Button("Button 4"), new BorderLayoutData(Region.EAST));
 *   panel.add(new Button("Button 5"));
 *
 *   viewport.add(panel, true);
 *
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * In the next example the height of <em>Button 1</em> is set to 50 pixels, the
 * height of <em>Button 2</em> is a ratio (30% of the height of
 * {@link LayoutPanel LayoutPanel's} client area except paddings), the width of
 * <em>Button 3</em> is set to 200 pixels but may be changed by the user, by
 * dragging a split bar, to a value in the range [10, 300], and the width of
 * <em>Button 4</em> is set to -1 which means the calculated preferred width for
 * that child. <em>Button 5</em> is placed in a {@code
 * com.google.gwt.user.client.ui.DecoratorPanel}.
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BorderLayout2.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *
 *   LayoutPanel panel = new LayoutPanel(new BorderLayout());
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button("Button 1"), new BorderLayoutData(Region.NORTH, 50));
 *   panel.add(new Button("Button 2"), new BorderLayoutData(Region.SOUTH, 0.3));
 *   panel.add(new Button("Button 3"), new BorderLayoutData(Region.WEST, 200, 10, 300));
 *   panel.add(new Button("Button 4"), new BorderLayoutData(Region.EAST, -1));
 *   panel.add(new Button("Button 5"), new BorderLayoutData(true));
 *
 *   viewport.add(panel, true);
 *
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * In the next example the regions {@link Region#NORTH}, {@link Region#SOUTH},
 * {@link Region#EAST} and {@link Region#WEST} are set to a collapsed state:
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BorderLayout3.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *
 *   final LayoutPanel panel = new LayoutPanel(new BorderLayout());
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *
 *   ClickListener clickListener = new ClickListener() {
 *     public void onClick(Widget sender) {
 *       panel.setCollapsed(sender, !panel.isCollapsed(sender));
 *       panel.layout();
 *     }
 *   };
 *
 *   Button button1 = new Button("Button 1", clickListener);
 *   Button button2 = new Button("Button 2", clickListener);
 *   Button button3 = new Button("Button 3", clickListener);
 *   Button button4 = new Button("Button 4", clickListener);
 *
 *   panel.add(button1, new BorderLayoutData(Region.NORTH));
 *   panel.add(button2, new BorderLayoutData(Region.SOUTH));
 *   panel.add(button3, new BorderLayoutData(Region.WEST));
 *   panel.add(button4, new BorderLayoutData(Region.EAST));
 *  
 *   panel.add(new Button("Button 5"), new BorderLayoutData(true));
 *   
 *   panel.setCollapsed(button1, true);
 *   panel.setCollapsed(button2, true);
 *   panel.setCollapsed(button3, true);
 *   panel.setCollapsed(button4, true);
 *
 *   viewport.add(panel, true);
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
 * @see BorderLayoutData
 */
public class BorderLayout extends BaseLayout implements HasCollapsibleWidgets {

  public enum Region {
    NORTH, EAST, SOUTH, WEST, CENTER
  };

  private Widget north, east, south, west, center;

  private BorderLayoutSplitBar northSplitBar, southSplitBar, westSplitBar,
      eastSplitBar;

  private BorderLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof BorderLayoutData)) {
      layoutDataObject = new BorderLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (BorderLayoutData) layoutDataObject;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension(0, 0);

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

      final int spacing = layoutPanel.getWidgetSpacing();

      if (north != null) {
        height += preferredHeightMeasure.sizeOf(north);
        final Widget parent = north.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          height += (borderSizes[0] + borderSizes[2]);
        }
        height += spacing;
      }

      if (south != null) {
        height += preferredHeightMeasure.sizeOf(south);
        final Widget parent = south.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          height += (borderSizes[0] + borderSizes[2]);
        }
        height += spacing;
      }

      Dimension westSize = null;

      if (west != null) {
        width += preferredWidthMeasure.sizeOf(west);
        final Widget parent = west.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          width += (borderSizes[1] + borderSizes[3]);
        }
        width += spacing;
      }

      Dimension eastSize = null;

      if (east != null) {
        width += preferredWidthMeasure.sizeOf(east);
        final Widget parent = east.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          width += (borderSizes[1] + borderSizes[3]);
        }
        width += spacing;
      }

      Dimension centerSize = new Dimension(
          preferredWidthMeasure.sizeOf(center),
          preferredHeightMeasure.sizeOf(center));
      width += centerSize.width;

      if (west != null && westSize == null) {
        westSize = getPreferredSize(layoutPanel, west,
            (LayoutData) west.getLayoutData());
      }
      if (east != null && eastSize == null) {
        eastSize = getPreferredSize(layoutPanel, east,
            (LayoutData) east.getLayoutData());
      }

      if (west != null && east != null) {
        height += Math.max(Math.max(westSize.height, eastSize.height),
            centerSize.height);
      } else if (west != null) {
        height += Math.max(westSize.height, centerSize.height);
      } else if (east != null) {
        height += Math.max(eastSize.height, centerSize.height);
      } else {
        height += centerSize.height;
      }

      final Widget parent = center.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        width += (borderSizes[1] + borderSizes[3]);
        height += (borderSizes[0] + borderSizes[2]);
      }

      result.width = width;
      result.height = height;

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.getLocalizedMessage());
    }

    return result;
  }

  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    scanForPanels(layoutPanel);

    return initialized = true;
  }

  private void scanForPanels(LayoutPanel layoutPanel) {
    north = east = south = west = center = null;

    for (Widget widget : visibleChildList) {

      if (widget instanceof InternalDecoratorPanel) {
        widget = ((InternalDecoratorPanel) widget).getWidget();
      }

      final BorderLayoutData layoutData = getLayoutData(widget);

      if (layoutData.region == Region.NORTH) {
        if (north == null) {
          north = widget;
        }
      } else if (layoutData.region == Region.EAST) {
        if (east == null) {
          east = widget;
        }
      } else if (layoutData.region == Region.SOUTH) {
        if (south == null) {
          south = widget;
        }
      } else if (layoutData.region == Region.WEST) {
        if (west == null) {
          west = widget;
        }
      } else if (layoutData.region == Region.CENTER) {
        if (center == null) {
          center = widget;
        }
      }

      if (north != null && east != null && south != null && west != null
          && center != null) {
        break;
      }
    }

    visibleChildList.clear();

    if (north != null) {
      visibleChildList.add(north);
    }
    if (east != null) {
      visibleChildList.add(east);
    }
    if (south != null) {
      visibleChildList.add(south);
    }
    if (west != null) {
      visibleChildList.add(west);
    }
    if (center != null) {
      visibleChildList.add(center);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      // ---
      int left = paddingLeftMeasure.sizeOf(layoutPanel);
      int top = paddingTopMeasure.sizeOf(layoutPanel);

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());
      final int width = box.width
          - (left + paddingRightMeasure.sizeOf(layoutPanel));
      final int height = box.height
          - (top + paddingBottomMeasure.sizeOf(layoutPanel));
      // ---

      final int spacing = layoutPanel.getWidgetSpacing();

      int right = left + width;
      int bottom = top + height;

      if (north != null) {
        final BorderLayoutData layoutData = getLayoutData(north);

        if (layoutData.resizable && !layoutData.collapsed) {
          if (northSplitBar == null) {
            northSplitBar = new BorderLayoutSplitBar(layoutPanel, north);
            northSplitBar.setStyleName("NorthSplitBar");
            layoutPanel.add(northSplitBar);
          }
        } else {
          if (northSplitBar != null) {
            layoutPanel.removeImpl(northSplitBar);
            northSplitBar = null;
          }
        }

        int h = preferredHeightMeasure.sizeOf(north);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = Math.max(0, right - left);
        layoutData.targetHeight = h;

        final Widget parent = north.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          final Dimension decPanelBorderSize = new Dimension(borderSizes[1]
              + borderSizes[3], borderSizes[0] + borderSizes[0]);
          layoutData.targetWidth -= decPanelBorderSize.width;
          h += decPanelBorderSize.height;
        }

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(layoutData.targetLeft);
          layoutData.setSourceTop(layoutData.targetTop);
          layoutData.setSourceWidth(north.getOffsetWidth());
          layoutData.setSourceHeight(north.getOffsetHeight());
        }

        // split bar
        if (layoutData.resizable && northSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, northSplitBar, left, top + h,
              Math.max(0, right - left), spacing);
        }

        top += (h + spacing);
      }

      if (south != null) {
        final BorderLayoutData layoutData = getLayoutData(south);

        if (layoutData.resizable && !layoutData.collapsed) {
          if (southSplitBar == null) {
            southSplitBar = new BorderLayoutSplitBar(layoutPanel, south);
            southSplitBar.setStyleName("SouthSplitBar");
            layoutPanel.add(southSplitBar);
          }
        } else {
          if (southSplitBar != null) {
            layoutPanel.removeImpl(southSplitBar);
            southSplitBar = null;
          }
        }

        int h = preferredHeightMeasure.sizeOf(south);

        layoutData.targetLeft = left;
        layoutData.targetTop = Math.max(0, bottom - h);
        layoutData.targetWidth = Math.max(0, right - left);
        layoutData.targetHeight = h;

        final Widget parent = south.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          final Dimension decPanelBorderSize = new Dimension(borderSizes[1]
              + borderSizes[3], borderSizes[0] + borderSizes[0]);
          layoutData.targetWidth -= decPanelBorderSize.width;
          layoutData.targetTop -= decPanelBorderSize.height;
          h += decPanelBorderSize.height;
        }

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(layoutData.targetLeft);
          layoutData.setSourceTop(south.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop()
              - paddingBottomMeasure.sizeOf(layoutPanel));
          layoutData.setSourceWidth(south.getOffsetWidth());
          layoutData.setSourceHeight(south.getOffsetHeight());
        }

        // split bar
        if (layoutData.resizable && southSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, southSplitBar, left, Math.max(0,
              bottom - h)
              - spacing, Math.max(0, right - left), spacing);
        }

        bottom -= (h + spacing);
      }

      if (west != null) {
        final BorderLayoutData layoutData = getLayoutData(west);

        if (layoutData.resizable && !layoutData.collapsed) {
          if (westSplitBar == null) {
            westSplitBar = new BorderLayoutSplitBar(layoutPanel, west);
            westSplitBar.setStyleName("WestSplitBar");
            layoutPanel.add(westSplitBar);
          }
        } else {
          if (westSplitBar != null) {
            layoutPanel.removeImpl(westSplitBar);
            westSplitBar = null;
          }
        }

        int w = preferredWidthMeasure.sizeOf(west);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = w;
        layoutData.targetHeight = Math.max(0, bottom - top);

        final Widget parent = west.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          final Dimension decPanelBorderSize = new Dimension(borderSizes[1]
              + borderSizes[3], borderSizes[0] + borderSizes[0]);
          layoutData.targetHeight -= decPanelBorderSize.height;
          w += decPanelBorderSize.width;
        }

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(west.getAbsoluteLeft()
              - layoutPanel.getAbsoluteLeft()
              - paddingLeftMeasure.sizeOf(layoutPanel));
          layoutData.setSourceTop(west.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop()
              - paddingTopMeasure.sizeOf(layoutPanel));
          layoutData.setSourceWidth(west.getOffsetWidth());
          layoutData.setSourceHeight(west.getOffsetHeight());
        }

        // split bar
        if (layoutData.resizable && westSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, westSplitBar, left + w, top,
              spacing, Math.max(0, bottom - top));
        }

        left += (w + spacing);
      }

      if (east != null) {
        final BorderLayoutData layoutData = getLayoutData(east);

        if (layoutData.resizable && !layoutData.collapsed) {
          if (eastSplitBar == null) {
            eastSplitBar = new BorderLayoutSplitBar(layoutPanel, east);
            eastSplitBar.setStyleName("EastSplitBar");
            layoutPanel.add(eastSplitBar);
          }
        } else {
          if (eastSplitBar != null) {
            layoutPanel.removeImpl(eastSplitBar);
            eastSplitBar = null;
          }
        }

        int w = preferredWidthMeasure.sizeOf(east);

        layoutData.targetLeft = Math.max(0, right - w);
        layoutData.targetTop = top;
        layoutData.targetWidth = w;
        layoutData.targetHeight = Math.max(0, bottom - top);

        final Widget parent = east.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          final Dimension decPanelBorderSize = new Dimension(borderSizes[1]
              + borderSizes[3], borderSizes[0] + borderSizes[0]);
          layoutData.targetLeft -= decPanelBorderSize.width;
          layoutData.targetHeight -= decPanelBorderSize.height;
          w += decPanelBorderSize.width;
        }

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(east.getAbsoluteLeft()
              - layoutPanel.getAbsoluteLeft()
              - paddingRightMeasure.sizeOf(layoutPanel));
          layoutData.setSourceTop(east.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop()
              - paddingTopMeasure.sizeOf(layoutPanel));
          layoutData.setSourceWidth(east.getOffsetWidth());
          layoutData.setSourceHeight(east.getOffsetHeight());
        }

        // split bar
        if (layoutData.resizable && eastSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, eastSplitBar, Math.max(0, right
              - w)
              - spacing, top, spacing, Math.max(0, bottom - top));
        }

        right -= (w + spacing);
      }

      if (center != null) {
        BorderLayoutData layoutData = getLayoutData(center);

        layoutData.targetLeft = left;
        layoutData.targetTop = top;
        layoutData.targetWidth = Math.max(1, right - left);
        layoutData.targetHeight = Math.max(1, bottom - top);

        final Widget parent = center.getParent();
        if (parent instanceof InternalDecoratorPanel) {
          final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
          final int borderSizes[] = decPanel.getBorderSizes();
          final Dimension decPanelBorderSize = new Dimension(borderSizes[1]
              + borderSizes[3], borderSizes[0] + borderSizes[0]);
          layoutData.targetWidth -= decPanelBorderSize.width;
          layoutData.targetHeight -= decPanelBorderSize.height;
        }

        if (layoutPanel.isAnimationEnabled()) {
          layoutData.setSourceLeft(center.getAbsoluteLeft()
              - layoutPanel.getAbsoluteLeft()
              - paddingLeftMeasure.sizeOf(layoutPanel));
          layoutData.setSourceTop(center.getAbsoluteTop()
              - layoutPanel.getAbsoluteTop()
              - paddingTopMeasure.sizeOf(layoutPanel));
          layoutData.setSourceWidth(center.getOffsetWidth());
          layoutData.setSourceHeight(center.getOffsetHeight());
        }
      }

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.getLocalizedMessage());
    }
  }

  public boolean isCollapsed(LayoutPanel layoutPanel, Widget widget) {
    return ((BorderLayoutData) widget.getLayoutData()).collapsed;
  }

  public void setCollapsed(final LayoutPanel layoutPanel, final Widget widget,
      boolean collapse) {
    //scanForPanels(layoutPanel);
    
    init(layoutPanel);

    final BorderLayoutData layoutData = (BorderLayoutData) widget.getLayoutData();

    if (collapse) {
      if (widget == west || widget == east || widget == north
          || widget == south) {
        if (layoutData.collapsedStateWidget == null) {
          ImageButton imgBtn = null;
          if (layoutData.region == Region.NORTH) {
            imgBtn = new ImageButton(Caption.IMAGES.toolCollapseDown());
            imgBtn.addStyleName("NorthCollapsedImageButton");
            imgBtn.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
          } else if (layoutData.region == Region.EAST) {
            imgBtn = new ImageButton(Caption.IMAGES.toolCollapseLeft());
            imgBtn.addStyleName("EastCollapsedImageButton");
            imgBtn.setVerticalAlignment(HasAlignment.ALIGN_TOP);
          } else if (layoutData.region == Region.SOUTH) {
            imgBtn = new ImageButton(Caption.IMAGES.toolCollapseUp());
            imgBtn.addStyleName("SouthCollapsedImageButton");
            imgBtn.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
          } else if (layoutData.region == Region.WEST) {
            imgBtn = new ImageButton(Caption.IMAGES.toolCollapseRight());
            imgBtn.addStyleName("WestCollapsedImageButton");
            imgBtn.setVerticalAlignment(HasAlignment.ALIGN_TOP);
          }
          if (imgBtn == null) {
            return;
          }
          imgBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
              layoutPanel.setCollapsed(widget, false);
              layoutPanel.invalidate(widget);
              layoutPanel.layout();
            }
          });
          layoutData.collapsedStateWidget = imgBtn;
        }
        layoutData.collapsed = collapse;
        widget.setVisible(false);
        syncDecoratorVisibility(widget);
        layoutData.collapsedStateWidget.setVisible(true);
        if (!layoutData.collapsedStateWidget.isAttached()) {
          layoutPanel.add(layoutData.collapsedStateWidget,
              new BorderLayoutData(layoutData.region));
        }
        layoutData.fireCollapsedChange(widget);
      }
    } else if (layoutData.collapsed) {
      layoutData.collapsed = collapse;
      layoutData.collapsedStateWidget.setVisible(false);
      widget.setVisible(true);
      syncDecoratorVisibility(widget);
      layoutData.fireCollapsedChange(widget);
    }
  }

}
