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

import java.util.Iterator;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
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
public class BorderLayout extends BaseLayout {

  public enum Region {
    NORTH, EAST, SOUTH, WEST, CENTER
  };

  private Widget north, east, south, west, center;

  private BorderLayoutSplitBar northSplitBar, southSplitBar, westSplitBar,
      eastSplitBar;

  private Widget placeHolder;
  
  /**
   * Caches component minimum and preferred sizes. All requests for component
   * sizes shall be directed to the cache.
   */
  private final WidgetSizeCache componentSizeCache;
  
  /**
   * These functional objects are used to measure component sizes. They abstract
   * from horizontal and vertical orientation and so, allow to implement the
   * layout algorithm for both orientations with a single set of methods.
   */
  private final Measure preferredWidthMeasure;
  private final Measure preferredHeightMeasure;
  
  public BorderLayout() {
    this.componentSizeCache = new WidgetSizeCache(0);
    this.preferredWidthMeasure = new PreferredWidthMeasure(componentSizeCache);
    this.preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);
  }

  private BorderLayoutData getBorderLayoutData(Widget child) {
    Object layoutDataObject = getLayoutData(child);
    if (layoutDataObject == null
        || !(layoutDataObject instanceof BorderLayoutData)) {
      layoutDataObject = new BorderLayoutData();
      setLayoutData(child, layoutDataObject);
    }
    return (BorderLayoutData) layoutDataObject;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui
   * .client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension(0, 0);

    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

      int width = (margins[1] + margins[3]) + (paddings[1] + paddings[3])
          + (borders[1] + borders[3]);
      int height = (margins[0] + margins[2]) + (paddings[0] + paddings[2])
          + (borders[0] + borders[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        int northHeight = preferredHeightMeasure.sizeOf(north);

        height += northHeight;
        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.decoratorPanel;
          height += (decPanel.getOffsetHeight() - north.getOffsetHeight());
        }
        height += spacing;
      }

      if (south != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        int southHeight = preferredHeightMeasure.sizeOf(south);;

        height += southHeight;
        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.decoratorPanel;
          height += (decPanel.getOffsetHeight() - south.getOffsetHeight());
        }
        height += spacing;
      }

      Dimension westSize = null;

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        int westWidth = preferredWidthMeasure.sizeOf(west);

        width += (int) Math.round(westWidth);
        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.decoratorPanel;
          width += (decPanel.getOffsetWidth() - west.getOffsetWidth());
        }
        width += spacing;
      }

      Dimension eastSize = null;

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        int eastWidth = preferredWidthMeasure.sizeOf(east);

        width += (int) Math.round(eastWidth);
        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.decoratorPanel;
          width += (decPanel.getOffsetWidth() - east.getOffsetWidth());
        }
        width += spacing;
      }

      Dimension centerSize = new Dimension(preferredWidthMeasure.sizeOf(center), preferredHeightMeasure.sizeOf(center));
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

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.decoratorPanel;
        width += (decPanel.getOffsetWidth() - center.getOffsetWidth());
        height += (decPanel.getOffsetHeight() - center.getOffsetHeight());
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

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    scanForPanels(layoutPanel);

    return initialized = true;
  }

  protected boolean isCollapsed(LayoutPanel layoutPanel, Widget widget) {
    try {
      if (layoutPanel != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(widget);
        return layoutData.collapse;
      }
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
      GWT.log(e.getMessage(), e);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.mosaic.ui.client
   * .LayoutPanel)
   */
  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      final int width = box.width - (paddings[1] + paddings[3]);
      final int height = box.height - (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      int left = paddings[3];
      int right = left + width;

      int top = paddings[0];
      int bottom = top + height;

      if (north != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        if (layoutData.resizable && !layoutData.collapse) {
          if (northSplitBar == null) {
            northSplitBar = new BorderLayoutSplitBar(layoutPanel, north);
            northSplitBar.setStyleName("NorthSplitBar");
            layoutPanel.addImpl(northSplitBar);
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

        if (layoutData.hasDecoratorPanel()) {
          final Dimension decPanelBorderSize = getDecoratorFrameSize(
              layoutData.decoratorPanel, north);
          layoutData.targetWidth -= decPanelBorderSize.width;
          h += decPanelBorderSize.height;
        }

        layoutData.setSourceLeft(layoutData.targetLeft);
        layoutData.setSourceTop(layoutData.targetTop);
        layoutData.setSourceWidth(north.getOffsetWidth());
        layoutData.setSourceHeight(north.getOffsetHeight());

        // split bar
        if (layoutData.resizable && northSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, northSplitBar, left, top + h,
              Math.max(0, right - left), spacing);
        }

        top += (h + spacing);
      }

      if (south != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        if (layoutData.resizable && !layoutData.collapse) {
          if (southSplitBar == null) {
            southSplitBar = new BorderLayoutSplitBar(layoutPanel, south);
            southSplitBar.setStyleName("SouthSplitBar");
            layoutPanel.addImpl(southSplitBar);
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

        if (layoutData.hasDecoratorPanel()) {
          final Dimension decPanelBorderSize = getDecoratorFrameSize(
              layoutData.decoratorPanel, south);
          layoutData.targetWidth -= decPanelBorderSize.width;
          layoutData.targetTop -= decPanelBorderSize.height;
          h += decPanelBorderSize.height;
        }

        layoutData.setSourceLeft(layoutData.targetLeft);
        layoutData.setSourceTop(south.getAbsoluteTop()
            - layoutPanel.getAbsoluteTop() - paddings[2]);
        layoutData.setSourceWidth(south.getOffsetWidth());
        layoutData.setSourceHeight(south.getOffsetHeight());

        // split bar
        if (layoutData.resizable && southSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, southSplitBar, left, Math.max(0,
              bottom - h)
              - spacing, Math.max(0, right - left), spacing);
        }

        bottom -= (h + spacing);
      }

      if (west != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.resizable && !layoutData.collapse) {
          if (westSplitBar == null) {
            westSplitBar = new BorderLayoutSplitBar(layoutPanel, west);
            westSplitBar.setStyleName("WestSplitBar");
            layoutPanel.addImpl(westSplitBar);
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

        if (layoutData.hasDecoratorPanel()) {
          final Dimension decPanelBorderSize = getDecoratorFrameSize(
              layoutData.decoratorPanel, west);
          layoutData.targetHeight -= decPanelBorderSize.height;
          w += decPanelBorderSize.width;
        }

        layoutData.setSourceLeft(west.getAbsoluteLeft()
            - layoutPanel.getAbsoluteLeft() - paddings[3]);
        layoutData.setSourceTop(west.getAbsoluteTop()
            - layoutPanel.getAbsoluteTop() - paddings[0]);
        layoutData.setSourceWidth(west.getOffsetWidth());
        layoutData.setSourceHeight(west.getOffsetHeight());

        // split bar
        if (layoutData.resizable && westSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, westSplitBar, left + w, top,
              spacing, Math.max(0, bottom - top));
        }

        left += (w + spacing);
      }

      if (east != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.resizable && !layoutData.collapse) {
          if (eastSplitBar == null) {
            eastSplitBar = new BorderLayoutSplitBar(layoutPanel, east);
            eastSplitBar.setStyleName("EastSplitBar");
            layoutPanel.addImpl(eastSplitBar);
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

        if (layoutData.hasDecoratorPanel()) {
          final Dimension decPanelBorderSize = getDecoratorFrameSize(
              layoutData.decoratorPanel, east);
          layoutData.targetLeft -= decPanelBorderSize.width;
          layoutData.targetHeight -= decPanelBorderSize.height;
          w += decPanelBorderSize.width;
        }

        layoutData.setSourceLeft(east.getAbsoluteLeft()
            - layoutPanel.getAbsoluteLeft() - paddings[1]);
        layoutData.setSourceTop(east.getAbsoluteTop()
            - layoutPanel.getAbsoluteTop() - paddings[0]);
        layoutData.setSourceWidth(east.getOffsetWidth());
        layoutData.setSourceHeight(east.getOffsetHeight());

        // split bar
        if (layoutData.resizable && eastSplitBar.isAttached()) {
          WidgetHelper.setBounds(layoutPanel, eastSplitBar, Math.max(0, right
              - w)
              - spacing, top, spacing, Math.max(0, bottom - top));
        }

        right -= (w + spacing);
      }

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);

      layoutData.targetLeft = left;
      layoutData.targetTop = top;
      layoutData.targetWidth = Math.max(1, right - left);
      layoutData.targetHeight = Math.max(1, bottom - top);

      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        final Dimension decPanelBorderSize = getDecoratorFrameSize(
            layoutData.decoratorPanel, center);
        layoutData.targetWidth -= decPanelBorderSize.width;
        layoutData.targetHeight -= decPanelBorderSize.height;
      }

      layoutData.setSourceLeft(center.getAbsoluteLeft()
          - layoutPanel.getAbsoluteLeft() - paddings[3]);
      layoutData.setSourceTop(center.getAbsoluteTop()
          - layoutPanel.getAbsoluteTop() - paddings[0]);
      layoutData.setSourceWidth(center.getOffsetWidth());
      layoutData.setSourceHeight(center.getOffsetHeight());

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.getLocalizedMessage());
    }
  }

  private void scanForPanels(LayoutPanel layoutPanel) {
    north = east = south = west = center = null;

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      if (widget == placeHolder) {
        continue;
      } else if (widget instanceof DecoratorPanel) {
        widget = ((DecoratorPanel) widget).getWidget();
      }

      BorderLayoutData layoutData = getBorderLayoutData(widget);

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      }

      if (layoutData.region == Region.NORTH) {
        if (north == null) {
          north = widget;
          visibleChildList.add(widget);
        }
      } else if (layoutData.region == Region.EAST) {
        if (east == null) {
          east = widget;
          visibleChildList.add(widget);
        }
      } else if (layoutData.region == Region.SOUTH) {
        if (south == null) {
          south = widget;
          visibleChildList.add(widget);
        }
      } else if (layoutData.region == Region.WEST) {
        if (west == null) {
          west = widget;
          visibleChildList.add(widget);
        }
      } else if (layoutData.region == Region.CENTER) {
        if (center == null) {
          center = widget;
          visibleChildList.add(widget);
        }
      }

      if (north != null && east != null && south != null && west != null
          && center != null) {
        break;
      }
    }

    if (center == null) {
      if (placeHolder == null) {
        placeHolder = new WidgetWrapper(new SimplePanel());
        layoutPanel.addImpl(placeHolder);
      }
      center = placeHolder;
      visibleChildList.add(center);
    } else if (placeHolder != null && placeHolder != center) {
      layoutPanel.removeImpl(placeHolder);
      placeHolder = null;
    }
  }

  protected void setCollapsed(final LayoutPanel layoutPanel,
      final Widget widget, boolean collapse) {
    try {
      if (layoutPanel != null) {

        scanForPanels(layoutPanel);

        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(widget);

        if (collapse) {
          if (widget == west || widget == east || widget == north
              || widget == south) {
            layoutData.collapse = collapse;
            widget.setVisible(false);
            syncDecoratorVisibility(widget);
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
              imgBtn.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                  layoutPanel.setCollapsed(widget, false);
                  layoutPanel.invalidate();
                  layoutPanel.layout();
                }
              });
              layoutData.collapsedStateWidget = imgBtn;
            } else {
              layoutData.collapsedStateWidget.setVisible(true);
            }
            if (!layoutData.collapsedStateWidget.isAttached()) {
              layoutPanel.add(layoutData.collapsedStateWidget,
                  new BorderLayoutData(layoutData.region));
            }
            layoutData.fireCollapsedChange(widget);
          }
        } else if (layoutData.collapse) {
          layoutData.collapse = collapse;
          layoutData.collapsedStateWidget.setVisible(false);
          widget.setVisible(true);
          syncDecoratorVisibility(widget);
          layoutData.fireCollapsedChange(widget);
        }
      };
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
      GWT.log(e.getMessage(), e);
    }
  }
  
  /**
   * Invalidates the component size caches.
   */
  private void invalidateCaches() {
    componentSizeCache.invalidate();
  }
  
  @Override
  public void flushCache() {
    // widgetSizes.clear();
    invalidateCaches();
    initialized = false;
  }

}
