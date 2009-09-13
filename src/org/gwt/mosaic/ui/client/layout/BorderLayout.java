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
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

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
  private Widget northCollapsed, eastCollapsed, southCollapsed, westCollapsed;

  private SplitBar northSplitBar, southSplitBar, westSplitBar, eastSplitBar;

  abstract class MyTimer extends Timer {
    protected LayoutPanel layoutPanel;

    public void schedule(LayoutPanel layoutPanel, int delayMillis) {
      this.layoutPanel = layoutPanel;
      super.schedule(delayMillis);
    }
  };

  private ImageButton northCollapsedImageButton;
  private ImageButton southCollapsedImageButton;
  private ImageButton westCollapsedImageButton;
  private ImageButton eastCollapsedImageButton;

  private GlassPanel northGlassPanel;
  private GlassPanel southGlassPanel;
  private GlassPanel westGlassPanel;
  private GlassPanel eastGlassPanel;

  private Widget placeHolder;

  // private Map<Widget, Dimension> widgetSizes = new HashMap<Widget,
  // Dimension>();

  @Override
  public void flushCache() {
    north = null;
    east = null;
    south = null;
    west = null;
    center = null;
    // widgetSizes.clear();
    initialized = false;
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

        if (layoutData.collapse) {
          height += WidgetHelper.getPreferredSize(northCollapsedImageButton).height;
        } else {
          int northHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            northHeight = WidgetHelper.getPreferredSize(north).height;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            northHeight = (int) (height * layoutData.preferredSize);
          }
          height += northHeight;
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            height += (decPanel.getOffsetHeight() - north.getOffsetHeight());
          }
        }

        height += spacing;
      }

      if (south != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        if (layoutData.collapse) {
          height += WidgetHelper.getPreferredSize(southCollapsedImageButton).height;
        } else {
          int southHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            southHeight = WidgetHelper.getPreferredSize(south).height;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            southHeight = (int) (height * layoutData.preferredSize);
          }
          height += southHeight;
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            height += (decPanel.getOffsetHeight() - south.getOffsetHeight());
          }
        }

        height += spacing;
      }

      Dimension westSize = null;

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.collapse) {
          width += WidgetHelper.getPreferredSize(westCollapsedImageButton).width;
        } else {
          int westWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            westWidth = WidgetHelper.getPreferredSize(west).width;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            westWidth = (int) (width * layoutData.preferredSize);
          }
          width += (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            width += (decPanel.getOffsetWidth() - west.getOffsetWidth());
          }
        }

        width += spacing;
      }

      Dimension eastSize = null;

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.collapse) {
          width += WidgetHelper.getPreferredSize(eastCollapsedImageButton).width;
        } else {
          int eastWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            eastWidth = WidgetHelper.getPreferredSize(east).width;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            eastWidth = (int) (width * layoutData.preferredSize);
          }
          width += (int) Math.round(eastWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            width += (decPanel.getOffsetWidth() - east.getOffsetWidth());
          }
        }

        width += spacing;
      }

      Dimension centerSize = WidgetHelper.getPreferredSize(center);
      width += centerSize.width;

      if (west != null && westSize == null) {
        westSize = WidgetHelper.getPreferredSize(west);
      }
      if (east != null && eastSize == null) {
        eastSize = WidgetHelper.getPreferredSize(east);
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
            northSplitBar = new SplitBar(layoutPanel, north, SplitBar.NORTH);
            northSplitBar.setStyleName("NorthSplitBar");
            layoutPanel.addImpl(northSplitBar);
          }
        } else {
          if (northSplitBar != null) {
            layoutPanel.removeImpl(northSplitBar);
            northSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          if (northCollapsedImageButton == null) {
            northCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseDown());
            northCollapsedImageButton.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            northCollapsedImageButton.addStyleName("NorthCollapsedImageButton");
            northCollapsedImageButton.addClickHandler(new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (!Element.is(event.getNativeEvent().getEventTarget())) {
                  return;
                }
                final Element elem = event.getNativeEvent().getEventTarget().cast();
                if ("TD".equalsIgnoreCase(elem.getTagName())) {
                  final Element collapsedElem;
                  if (layoutData.hasDecoratorPanel()) {
                    collapsedElem = layoutData.decoratorPanel.getElement();
                  } else {
                    collapsedElem = northCollapsed.getElement();
                  }

                  // layoutData.floatting = true;
                  if (northGlassPanel == null) {
                    northGlassPanel = createGlassPanelForACollapsedElement(
                        layoutPanel, northCollapsed, collapsedElem);
                  }
                  RootPanel.get().add(northGlassPanel, 0, 0);

                  DOM.setStyleAttribute(collapsedElem, "zIndex", ""
                      + Integer.MAX_VALUE);

                  layoutPanel.setCollapsed(northCollapsed, false);
                  // layoutPanel.removeImpl(westCollapsedImageButton);
                  // westCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  northCollapsed.setVisible(true);

                  layoutPanel.layout();

                } else {
                  layoutPanel.setCollapsed(northCollapsed, false);
                  layoutPanel.removeImpl(northCollapsedImageButton);
                  northCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  northCollapsed.setVisible(true);
                  layoutPanel.layout();
                  return;
                }
              }
            });
            layoutPanel.addImpl(northCollapsedImageButton);
          }

          if (layoutData.hasDecoratorPanel()) {
            layoutData.decoratorPanel.setVisible(false);
          }
          northCollapsed = north;
          north.setVisible(false);

          h = WidgetHelper.getPreferredSize(northCollapsedImageButton).height;
          WidgetHelper.setBounds(layoutPanel, northCollapsedImageButton, left,
              top, Math.max(0, right - left), h);
        } else {

          if (layoutData.preferredSize == -1.0) {
            h = WidgetHelper.getPreferredSize(north).height;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            h = (int) (height * layoutData.preferredSize);
          } else {
            h = (int) layoutData.preferredSize;
          }

          int _width = Math.max(0, right - left);
          int _height = -1;
          if (layoutData.preferredSize != -1.0) {
            _height = h;
          }

          if (layoutData.hasDecoratorPanel()) {
            final Dimension decPanelBorderSize = getDecoratorFrameSize(
                layoutData.decoratorPanel, north);

            _width -= decPanelBorderSize.width;

            // increase 'h'
            h += decPanelBorderSize.height;
          }
          WidgetHelper.setBounds(layoutPanel, north, left, top, _width, _height);
          if (northGlassPanel == null || !northGlassPanel.isAttached()) {
            // split bar
            if (layoutData.resizable && northSplitBar.isAttached()) {
              WidgetHelper.setBounds(layoutPanel, northSplitBar, left, top + h,
                  Math.max(0, right - left), spacing);
            }
          } else {
            return;
            // h =
            // WidgetHelper.getPreferredSize(northCollapsedImageButton).height;
          }
        }
        top += (h + spacing);
      }

      if (south != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(south);

        if (layoutData.resizable && !layoutData.collapse) {
          if (southSplitBar == null) {
            southSplitBar = new SplitBar(layoutPanel, south, SplitBar.SOUTH);
            southSplitBar.setStyleName("SouthSplitBar");
            layoutPanel.addImpl(southSplitBar);
          }
        } else {
          if (southSplitBar != null) {
            layoutPanel.removeImpl(southSplitBar);
            southSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          if (southCollapsedImageButton == null) {
            southCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseUp());
            southCollapsedImageButton.addStyleName("SouthCollapsedImageButton");
            southCollapsedImageButton.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            southCollapsedImageButton.addClickHandler(new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (!Element.is(event.getNativeEvent().getEventTarget())) {
                  return;
                }
                final Element elem = event.getNativeEvent().getEventTarget().cast();
                if ("TD".equalsIgnoreCase(elem.getTagName())) {
                  final Element collapsedElem;
                  if (layoutData.hasDecoratorPanel()) {
                    collapsedElem = layoutData.decoratorPanel.getElement();
                  } else {
                    collapsedElem = southCollapsed.getElement();
                  }

                  // layoutData.floatting = true;
                  if (southGlassPanel == null) {
                    southGlassPanel = createGlassPanelForACollapsedElement(
                        layoutPanel, southCollapsed, collapsedElem);
                  }
                  RootPanel.get().add(southGlassPanel, 0, 0);

                  DOM.setStyleAttribute(collapsedElem, "zIndex", ""
                      + Integer.MAX_VALUE);

                  layoutPanel.setCollapsed(southCollapsed, false);
                  // layoutPanel.removeImpl(westCollapsedImageButton);
                  // westCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  southCollapsed.setVisible(true);

                  layoutPanel.layout();

                } else {
                  layoutPanel.setCollapsed(southCollapsed, false);
                  layoutPanel.removeImpl(southCollapsedImageButton);
                  southCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  southCollapsed.setVisible(true);
                  layoutPanel.layout();
                  return;
                }
              }
            });
            layoutPanel.addImpl(southCollapsedImageButton);
          }

          if (layoutData.hasDecoratorPanel()) {
            layoutData.decoratorPanel.setVisible(false);
          }
          southCollapsed = south;
          south.setVisible(false);

          h = WidgetHelper.getPreferredSize(southCollapsedImageButton).height;
          WidgetHelper.setBounds(layoutPanel, southCollapsedImageButton, left,
              Math.max(0, bottom - h), Math.max(0, right - left), h);
        } else {
          if (layoutData.preferredSize == -1.0) {
            h = WidgetHelper.getPreferredSize(south).height;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            h = (int) (height * layoutData.preferredSize);
          } else {
            h = (int) layoutData.preferredSize;
          }

          int _width = Math.max(0, right - left);
          int _top = Math.max(0, bottom - h);
          int _height = -1;
          if (layoutData.preferredSize != -1.0) {
            _height = h;
          }

          if (layoutData.hasDecoratorPanel()) {
            final Dimension decPanelBorderSize = getDecoratorFrameSize(
                layoutData.decoratorPanel, south);

            _width -= decPanelBorderSize.width;
            _top -= decPanelBorderSize.height;

            // increase 'h'
            h += decPanelBorderSize.height;
          }

          WidgetHelper.setBounds(layoutPanel, south, left, _top, _width,
              _height);
          if (southGlassPanel == null || !southGlassPanel.isAttached()) {
            // split bar
            if (layoutData.resizable && southSplitBar.isAttached()) {
              WidgetHelper.setBounds(layoutPanel, southSplitBar, left,
                  Math.max(0, bottom - h) - spacing, Math.max(0, right - left),
                  spacing);
            }
          } else {
            return;
            // h =
            // WidgetHelper.getPreferredSize(southCollapsedImageButton).height;
          }
        }
        bottom -= (h + spacing);
      }

      if (west != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.resizable && !layoutData.collapse) {
          if (westSplitBar == null) {
            westSplitBar = new SplitBar(layoutPanel, west, SplitBar.WEST);
            westSplitBar.setStyleName("WestSplitBar");
            layoutPanel.addImpl(westSplitBar);
          }
        } else {
          if (westSplitBar != null) {
            layoutPanel.removeImpl(westSplitBar);
            westSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          if (westCollapsedImageButton == null) {
            westCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseRight());
            westCollapsedImageButton.addStyleName("WestCollapsedImageButton");
            westCollapsedImageButton.setVerticalAlignment(HasAlignment.ALIGN_TOP);
            westCollapsedImageButton.addClickHandler(new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (!Element.is(event.getNativeEvent().getEventTarget())) {
                  return;
                }
                final Element elem = event.getNativeEvent().getEventTarget().cast();
                if ("TD".equalsIgnoreCase(elem.getTagName())) {
                  final Element collapsedElem;
                  if (layoutData.hasDecoratorPanel()) {
                    collapsedElem = layoutData.decoratorPanel.getElement();
                  } else {
                    collapsedElem = westCollapsed.getElement();
                  }

                  // layoutData.floatting = true;
                  if (westGlassPanel == null) {
                    westGlassPanel = createGlassPanelForACollapsedElement(
                        layoutPanel, westCollapsed, collapsedElem);
                  }
                  RootPanel.get().add(westGlassPanel, 0, 0);

                  DOM.setStyleAttribute(collapsedElem, "zIndex", ""
                      + Integer.MAX_VALUE);

                  layoutPanel.setCollapsed(westCollapsed, false);
                  // layoutPanel.removeImpl(westCollapsedImageButton);
                  // westCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  westCollapsed.setVisible(true);

                  layoutPanel.layout();

                } else {
                  layoutPanel.setCollapsed(westCollapsed, false);
                  layoutPanel.removeImpl(westCollapsedImageButton);
                  westCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  westCollapsed.setVisible(true);
                  layoutPanel.layout();
                  return;
                }
              }
            });
            layoutPanel.addImpl(westCollapsedImageButton);
          }

          if (layoutData.hasDecoratorPanel()) {
            layoutData.decoratorPanel.setVisible(false);
          }
          westCollapsed = west;
          west.setVisible(false);

          w = WidgetHelper.getPreferredSize(westCollapsedImageButton).width;
          WidgetHelper.setBounds(layoutPanel, westCollapsedImageButton, left,
              top, w, Math.max(0, bottom - top));
        } else {
          if (layoutData.preferredSize == -1.0) {
            w = WidgetHelper.getPreferredSize(west).width;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            w = (int) (width * layoutData.preferredSize);
          } else {
            w = (int) layoutData.preferredSize;
          }

          int _width = -1;
          if (layoutData.preferredSize != -1.0) {
            _width = w;
          }
          int _height = Math.max(0, bottom - top);

          if (layoutData.hasDecoratorPanel()) {
            final Dimension decPanelBorderSize = getDecoratorFrameSize(
                layoutData.decoratorPanel, west);

            _height -= decPanelBorderSize.height;

            // increase 'w'
            w += decPanelBorderSize.width;
          }

          WidgetHelper.setBounds(layoutPanel, west, left, top, _width, _height);
          if (westGlassPanel == null || !westGlassPanel.isAttached()) {
            // split bar
            if (layoutData.resizable && westSplitBar.isAttached()) {
              WidgetHelper.setBounds(layoutPanel, westSplitBar, left + w, top,
                  spacing, Math.max(0, bottom - top));
            }
          } else {
            return;
            // w =
            // WidgetHelper.getPreferredSize(westCollapsedImageButton).width;
          }
        }
        left += (w + spacing);
      }

      if (east != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.resizable && !layoutData.collapse) {
          if (eastSplitBar == null) {
            eastSplitBar = new SplitBar(layoutPanel, east, SplitBar.EAST);
            eastSplitBar.setStyleName("EastSplitBar");
            layoutPanel.addImpl(eastSplitBar);
          }
        } else {
          if (eastSplitBar != null) {
            layoutPanel.removeImpl(eastSplitBar);
            eastSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          if (eastCollapsedImageButton == null) {
            eastCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseLeft());
            eastCollapsedImageButton.addStyleName("EastCollapsedImageButton");
            eastCollapsedImageButton.setVerticalAlignment(HasAlignment.ALIGN_TOP);
            eastCollapsedImageButton.addClickHandler(new ClickHandler() {
              public void onClick(ClickEvent event) {
                if (!Element.is(event.getNativeEvent().getEventTarget())) {
                  return;
                }
                final Element elem = event.getNativeEvent().getEventTarget().cast();
                if ("TD".equalsIgnoreCase(elem.getTagName())) {
                  final Element collapsedElem;
                  if (layoutData.hasDecoratorPanel()) {
                    collapsedElem = layoutData.decoratorPanel.getElement();
                  } else {
                    collapsedElem = eastCollapsed.getElement();
                  }

                  // layoutData.floatting = true;
                  if (eastGlassPanel == null) {
                    eastGlassPanel = createGlassPanelForACollapsedElement(
                        layoutPanel, eastCollapsed, collapsedElem);
                  }
                  RootPanel.get().add(eastGlassPanel, 0, 0);

                  DOM.setStyleAttribute(collapsedElem, "zIndex", ""
                      + Integer.MAX_VALUE);

                  layoutPanel.setCollapsed(eastCollapsed, false);
                  // layoutPanel.removeImpl(eastCollapsedImageButton);
                  // westCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  eastCollapsed.setVisible(true);

                  layoutPanel.layout();

                } else {
                  layoutPanel.setCollapsed(eastCollapsed, false);
                  layoutPanel.removeImpl(eastCollapsedImageButton);
                  eastCollapsedImageButton = null;
                  if (layoutData.hasDecoratorPanel()) {
                    layoutData.decoratorPanel.setVisible(true);
                  }
                  eastCollapsed.setVisible(true);
                  layoutPanel.layout();
                  return;
                }
              }
            });
            layoutPanel.addImpl(eastCollapsedImageButton);
          }

          if (layoutData.hasDecoratorPanel()) {
            layoutData.decoratorPanel.setVisible(false);
          }
          eastCollapsed = east;
          east.setVisible(false);

          w = WidgetHelper.getPreferredSize(eastCollapsedImageButton).width;
          WidgetHelper.setBounds(layoutPanel, eastCollapsedImageButton,
              Math.max(0, right - w), top, w, Math.max(0, bottom - top));
        } else {
          if (layoutData.preferredSize == -1.0) {
            w = WidgetHelper.getPreferredSize(east).width;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            w = (int) (width * layoutData.preferredSize);
          } else {
            w = (int) layoutData.preferredSize;
          }

          int _left = Math.max(0, right - w);
          int _width = -1;
          if (layoutData.preferredSize != -1.0) {
            _width = w;
          }
          int _height = Math.max(0, bottom - top);

          if (layoutData.hasDecoratorPanel()) {
            final Dimension decPanelBorderSize = getDecoratorFrameSize(
                layoutData.decoratorPanel, east);

            _left -= decPanelBorderSize.width;
            _height -= decPanelBorderSize.height;

            // increase 'h'
            w += decPanelBorderSize.width;
          }
          WidgetHelper.setBounds(layoutPanel, east, _left, top, _width, _height);
          if (eastGlassPanel == null || !eastGlassPanel.isAttached()) {
            // split bar
            if (layoutData.resizable && eastSplitBar.isAttached()) {
              WidgetHelper.setBounds(layoutPanel, eastSplitBar, Math.max(0,
                  right - w)
                  - spacing, top, spacing, Math.max(0, bottom - top));
            }
          } else {
            return;
            // w =
            // WidgetHelper.getPreferredSize(eastCollapsedImageButton).width;
          }
        }
        right -= (w + spacing);
      }

      int _width = Math.max(0, right - left);
      int _height = Math.max(0, bottom - top);

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        final Dimension decPanelBorderSize = getDecoratorFrameSize(
            layoutData.decoratorPanel, center);
        _width -= decPanelBorderSize.width;
        _height -= decPanelBorderSize.height;
      }

      WidgetHelper.setBounds(layoutPanel, center, left, top, _width, _height);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);
      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.getLocalizedMessage());
    }
  }

  private GlassPanel createGlassPanelForACollapsedElement(
      final LayoutPanel layoutPanel, final Widget widget, final Element elem) {
    GlassPanel glassPanel = new GlassPanel(true) {
      @Override
      protected void onUnload() {
        if (widget.isAttached()) {
          DOM.setStyleAttribute(elem, "zIndex", "");
          layoutPanel.setCollapsed(widget, true);
          layoutPanel.layout();
        }
        super.onUnload();
      }
    };
    glassPanel.addStyleName("mosaic-GlassPanel-invisible");
    return glassPanel;
  }

  private void scanForPanels(LayoutPanel layoutPanel) {

    north = null;
    south = null;
    west = null;
    south = null;
    center = null;

    final int size = layoutPanel.getWidgetCount();
    for (int i = 0; i < size; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child == placeHolder) {
        continue;
      } else if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      BorderLayoutData layoutData = getBorderLayoutData(child);

      if (!DOM.isVisible(child.getElement()) && !layoutData.collapse) {
        continue;
      }

      if (layoutData.region == Region.NORTH) {
        if (north == null) {
          north = child;
        }
      } else if (layoutData.region == Region.EAST) {
        if (east == null) {
          east = child;
        }
      } else if (layoutData.region == Region.SOUTH) {
        if (south == null) {
          south = child;
        }
      } else if (layoutData.region == Region.WEST) {
        if (west == null) {
          west = child;
        }
      } else if (layoutData.region == Region.CENTER) {
        if (center == null) {
          center = child;
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
    } else if (placeHolder != null && placeHolder != center) {
      layoutPanel.removeImpl(placeHolder);
      placeHolder = null;
    }
  }

  protected void setCollapsed(LayoutPanel layoutPanel, Widget widget,
      boolean collapse) {
    try {
      if (layoutPanel != null) {
        scanForPanels(layoutPanel);
        if (widget == west || widget == east || widget == north
            || widget == south) {
          final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(widget);
          layoutData.collapse = collapse;
          layoutData.fireCollapsedChange(widget);
        }
        if (collapse) {
          if (northGlassPanel != null && northGlassPanel.isAttached()) {
            northGlassPanel.removeFromParent();
          }
          if (southGlassPanel != null && southGlassPanel.isAttached()) {
            southGlassPanel.removeFromParent();
          }
          if (westGlassPanel != null && westGlassPanel.isAttached()) {
            westGlassPanel.removeFromParent();
          }
          if (eastGlassPanel != null && eastGlassPanel.isAttached()) {
            eastGlassPanel.removeFromParent();
          }
        }
      };
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

}
