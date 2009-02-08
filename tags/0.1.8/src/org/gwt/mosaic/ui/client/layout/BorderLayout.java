/*
 *  Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.WidgetWrapper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
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
 * In the next example the height of <em>Button 1</em> is set to 50 pixels,
 * the height of <em>Button 2</em> is a ratio (30% of the height of
 * {@link LayoutPanel LayoutPanel's} client area except paddings), the width of
 * <em>Button 3</em> is set to 200 pixels but may be changed by the user, by
 * dragging a split bar, to a value in the range [10, 300], and the width of
 * <em>Button 4</em> is set to -1 which means the calculated preferred width
 * for that child. <em>Button 5</em> is placed in a
 * {@code com.google.gwt.user.client.ui.DecoratorPanel}.
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

  private SplitBar northSplitBar, southSplitBar, westSplitBar, eastSplitBar;

  private ImageButton northCollapsedImageButton;
  private ImageButton southCollapsedImageButton;
  private ImageButton westCollapsedImageButton;
  private ImageButton eastCollapsedImageButton;

  private Widget placeHolder;

  private boolean runTwiceFlag;

  public BorderLayout() {
    // Nothing to do here!
  }

  @Override
  public boolean runTwice() {
    return runTwiceFlag;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui.client.layout.LayoutPanel)
   */
  public int[] getPreferredSize(LayoutPanel layoutPanel) {
    int[] result = {0, 0};
    try {
      if (layoutPanel == null) {
        return result;
      }

      scanForPanels(layoutPanel);

      final int[] margins = DOM.getMarginSizes(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());
      int width = (margins[1] + margins[3]) + (paddings[1] + paddings[3]);
      int height = (margins[0] + margins[2]) + (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      if (north != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        if (layoutData.collapse) {
          height += getFlowHeight(northCollapsedImageButton);
        } else {
          int northHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            northHeight = getFlowHeight(north);
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
          height += getFlowHeight(southCollapsedImageButton);
        } else {
          int southHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            southHeight = getFlowHeight(south);
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

      if (west != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(west);

        if (layoutData.collapse) {
          width += getFlowWidth(westCollapsedImageButton);
        } else {
          int westWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            westWidth = getFlowWidth(west);
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

      if (east != null) {
        BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(east);

        if (layoutData.collapse) {
          width += getFlowWidth(eastCollapsedImageButton);
        } else {
          int eastWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            eastWidth = getFlowWidth(east);
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

      width += getFlowWidth(center);

      if (west != null && east != null) {
        height += Math.max(Math.max(getFlowHeight(west), getFlowHeight(east)),
            getFlowHeight(center));
      } else if (west != null) {
        height += Math.max(getFlowHeight(west), getFlowHeight(center));
      } else if (east != null) {
        height += Math.max(getFlowHeight(east), getFlowHeight(center));
      } else {
        height += getFlowHeight(center);
      }

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.decoratorPanel;
        width += (decPanel.getOffsetWidth() - center.getOffsetWidth());
        height += (decPanel.getOffsetHeight() - center.getOffsetHeight());
      }

      result[0] = width;
      result[1] = height;

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }

    return result;
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
   * @see org.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.mosaic.ui.client.LayoutPanel)
   */
  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null) {
        return;
      }

      scanForPanels(layoutPanel);

      final int[] box = DOM.getClientSize(layoutPanel.getElement());
      final int[] paddings = DOM.getPaddingSizes(layoutPanel.getElement());

      final int width = box[0] - (paddings[1] + paddings[3]);
      final int height = box[1] - (paddings[0] + paddings[2]);

      final int spacing = layoutPanel.getWidgetSpacing();

      int left = paddings[3];
      int right = left + width;

      int top = paddings[0];
      int bottom = top + height;

      runTwiceFlag = false;

      if (north != null) {
        final BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(north);

        if (layoutData.resizable && !layoutData.collapse) {
          if (northSplitBar == null) {
            northSplitBar = new SplitBar(layoutPanel, north, SplitBar.NORTH);
            northSplitBar.setStyleName("NorthSplitBar");
            layoutPanel.add(northSplitBar);
          }
        } else {
          if (northSplitBar != null) {
            layoutPanel.remove(northSplitBar);
            northSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          if (northCollapsedImageButton == null) {
            northCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseDown());
            northCollapsedImageButton.addStyleName("NorthCollapsedImageButton");
            northCollapsedImageButton.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                layoutPanel.setCollapsed(north, false);
                layoutPanel.remove(northCollapsedImageButton);
                northCollapsedImageButton = null;
                if (layoutData.hasDecoratorPanel()) {
                  layoutData.decoratorPanel.setVisible(true);
                }
                north.setVisible(true);
                layoutPanel.layout();
                return;
              }
            });
            layoutPanel.add(northCollapsedImageButton);
            if (layoutData.hasDecoratorPanel()) {
              layoutData.decoratorPanel.setVisible(false);
            }
            north.setVisible(false);
          }
          h = getFlowHeight(northCollapsedImageButton);
          setBounds(layoutPanel, northCollapsedImageButton, left, top,
              Math.max(0, right - left), h);
        } else {
          int northHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            northHeight = getFlowHeight(north);
            runTwiceFlag = true;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            northHeight = (int) (height * layoutData.preferredSize);
          }

          h = northHeight;
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int decPanelBorderWidth = decPanel.getOffsetWidth()
                - north.getOffsetWidth();
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - north.getOffsetHeight();
            setBounds(layoutPanel, north, left, top, Math.max(0, right - left)
                - decPanelBorderWidth, h);
            // increase 'h'
            h += decPanelBorderHeight;
          } else {
            setBounds(layoutPanel, north, left, top, Math.max(0, right - left),
                h);
          }

          // split bar
          if (layoutData.resizable) {
            setBounds(layoutPanel, northSplitBar, left, top + h, Math.max(0,
                right - left), spacing);
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
            layoutPanel.add(southSplitBar);
          }
        } else {
          if (southSplitBar != null) {
            layoutPanel.remove(southSplitBar);
            southSplitBar = null;
          }
        }

        int h = 0;

        if (layoutData.collapse) {
          if (southCollapsedImageButton == null) {
            southCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseUp());
            southCollapsedImageButton.addStyleName("SouthCollapsedImageButton");
            southCollapsedImageButton.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                layoutPanel.setCollapsed(south, false);
                layoutPanel.remove(southCollapsedImageButton);
                southCollapsedImageButton = null;
                if (layoutData.hasDecoratorPanel()) {
                  layoutData.decoratorPanel.setVisible(true);
                }
                south.setVisible(true);
                layoutPanel.layout();
                return;
              }
            });
            layoutPanel.add(southCollapsedImageButton);
            if (layoutData.hasDecoratorPanel()) {
              layoutData.decoratorPanel.setVisible(false);
            }
            south.setVisible(false);
          }
          h = getFlowHeight(southCollapsedImageButton);
          setBounds(layoutPanel, southCollapsedImageButton, left, Math.max(0,
              bottom - h), Math.max(0, right - left), h);
        } else {
          int southHeight = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            southHeight = getFlowHeight(south);
            runTwiceFlag = true;
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            southHeight = (int) (height * layoutData.preferredSize);
          }

          h = (int) Math.round(southHeight);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int _width = Math.max(0, right - left)
                - (decPanel.getOffsetWidth() - south.getOffsetWidth());
            final int _top = Math.max(0, bottom - h)
                - (decPanel.getOffsetHeight() - south.getOffsetHeight());
            setBounds(layoutPanel, south, left, _top, _width, h);
            // increase 'h'
            h += (decPanel.getOffsetHeight() - south.getOffsetHeight());
          } else {
            setBounds(layoutPanel, south, left, Math.max(0, bottom - h),
                Math.max(0, right - left), h);
          }

          // split bar
          if (layoutData.resizable) {
            setBounds(layoutPanel, southSplitBar, left, Math.max(0, bottom - h)
                - spacing, Math.max(0, right - left), spacing);
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
            layoutPanel.add(westSplitBar);
          }
        } else {
          if (westSplitBar != null) {
            layoutPanel.remove(westSplitBar);
            westSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          if (westCollapsedImageButton == null) {
            westCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseRight());
            westCollapsedImageButton.addStyleName("WestCollapsedImageButton");
            westCollapsedImageButton.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                layoutPanel.setCollapsed(west, false);
                layoutPanel.remove(westCollapsedImageButton);
                westCollapsedImageButton = null;
                if (layoutData.hasDecoratorPanel()) {
                  layoutData.decoratorPanel.setVisible(true);
                }
                west.setVisible(true);
                layoutPanel.layout();
                return;
              }
            });
            layoutPanel.add(westCollapsedImageButton);
            if (layoutData.hasDecoratorPanel()) {
              layoutData.decoratorPanel.setVisible(false);
            }
            west.setVisible(false);
          }
          w = getFlowWidth(westCollapsedImageButton);
          setBounds(layoutPanel, westCollapsedImageButton, left, top, w,
              Math.max(0, bottom - top));
        } else {
          int westWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            westWidth = getFlowWidth(west);
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            westWidth = (int) (width * layoutData.preferredSize);
          }

          w = (int) Math.round(westWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int _height = Math.max(0, bottom - top)
                - (decPanel.getOffsetHeight() - west.getOffsetHeight());
            setBounds(layoutPanel, west, left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - west.getOffsetWidth());
          } else {
            setBounds(layoutPanel, west, left, top, w,
                Math.max(0, bottom - top));
          }

          // split bar
          if (layoutData.resizable) {
            setBounds(layoutPanel, westSplitBar, left + w, top, spacing,
                Math.max(0, bottom - top));
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
            layoutPanel.add(eastSplitBar);
          }
        } else {
          if (eastSplitBar != null) {
            layoutPanel.remove(eastSplitBar);
            eastSplitBar = null;
          }
        }

        int w = 0;

        if (layoutData.collapse) {
          if (eastCollapsedImageButton == null) {
            eastCollapsedImageButton = new ImageButton(
                Caption.IMAGES.toolCollapseLeft());
            eastCollapsedImageButton.addStyleName("EastCollapsedImageButton");
            eastCollapsedImageButton.addClickListener(new ClickListener() {
              public void onClick(Widget sender) {
                layoutPanel.setCollapsed(east, false);
                layoutPanel.remove(eastCollapsedImageButton);
                eastCollapsedImageButton = null;
                if (layoutData.hasDecoratorPanel()) {
                  layoutData.decoratorPanel.setVisible(true);
                }
                east.setVisible(true);
                layoutPanel.layout();
                return;
              }
            });
            layoutPanel.add(eastCollapsedImageButton);
            if (layoutData.hasDecoratorPanel()) {
              layoutData.decoratorPanel.setVisible(false);
            }
            east.setVisible(false);
          }
          w = getFlowWidth(eastCollapsedImageButton);
          setBounds(layoutPanel, eastCollapsedImageButton, Math.max(0, right
              - w), top, w, Math.max(0, bottom - top));
        } else {
          int eastWidth = (int) layoutData.preferredSize;
          if (layoutData.preferredSize == -1.0) {
            eastWidth = getFlowWidth(east);
          } else if (layoutData.preferredSize > 0.0
              && layoutData.preferredSize <= 1.0) {
            eastWidth = (int) (width * layoutData.preferredSize);
          }

          w = (int) Math.round(eastWidth);
          if (layoutData.hasDecoratorPanel()) {
            final DecoratorPanel decPanel = layoutData.decoratorPanel;
            final int decPanelBorderWidth = decPanel.getOffsetWidth()
                - east.getOffsetWidth();
            final int decPanelBorderHeight = decPanel.getOffsetHeight()
                - east.getOffsetHeight();
            final int _left = Math.max(0, right - w) - decPanelBorderWidth;
            final int _height = Math.max(0, bottom - top)
                - decPanelBorderHeight;
            setBounds(layoutPanel, east, _left, top, w, _height);
            // increase 'h'
            w += (decPanel.getOffsetWidth() - east.getOffsetWidth());
          } else {
            setBounds(layoutPanel, east, Math.max(0, right - w), top, w,
                Math.max(0, bottom - top));
          }

          // split bar
          if (layoutData.resizable) {
            setBounds(layoutPanel, eastSplitBar, Math.max(0, right - w)
                - spacing, top, spacing, Math.max(0, bottom - top));
          }
        }
        right -= (w + spacing);
      }

      BorderLayoutData layoutData = (BorderLayoutData) getLayoutData(center);
      if (layoutData != null && layoutData.hasDecoratorPanel()) {
        final DecoratorPanel decPanel = layoutData.decoratorPanel;
        final int decPanelBorderWidth = decPanel.getOffsetWidth()
            - center.getOffsetWidth();
        final int decPanelBorderHeight = decPanel.getOffsetHeight()
            - center.getOffsetHeight();
        final int _width = Math.max(0, right - left) - decPanelBorderWidth;
        final int _height = Math.max(0, bottom - top) - decPanelBorderHeight;
        setBounds(layoutPanel, center, left, top, _width, _height);
      } else {
        setBounds(layoutPanel, center, left, top, Math.max(0, right - left),
            Math.max(0, bottom - top));
      }
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

  private void scanForPanels(LayoutPanel layoutPanel) {
    final int size = layoutPanel.getWidgetCount();

    north = null;
    south = null;
    west = null;
    east = null;
    center = null;

    for (int i = 0; i < size; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child == placeHolder) {
        continue;
      } else if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      Object layoutDataObject = getLayoutData(child);
      if (layoutDataObject == null
          || !(layoutDataObject instanceof BorderLayoutData)) {
        layoutDataObject = new BorderLayoutData();
        setLayoutData(child, layoutDataObject);
      }
      BorderLayoutData layoutData = (BorderLayoutData) layoutDataObject;

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
        placeHolder = new WidgetWrapper(new HTML("CENTER"));
        layoutPanel.add(placeHolder);
      }
      center = placeHolder;
    } else if (placeHolder != null && placeHolder != center) {
      layoutPanel.remove(placeHolder);
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
      };
    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ": " + e.getMessage());
    }
  }

}
