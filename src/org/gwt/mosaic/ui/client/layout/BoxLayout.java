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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A layout manager that allows multiple widgets of a {@link LayoutPanel} to be
 * laid out either vertically or horizontally. The height and width of each
 * widget in a {@code BoxLayout} can be specified by setting a
 * {@link BoxLayoutData} object into the widget using
 * {@link LayoutPanel#add(Widget, LayoutData)} and also by using CSS or a
 * combination of both methods.
 * <p>
 * {@code BoxLayout} aligns all widgets in one row if the {@link Orientation}
 * field is set to {@code HORIZONTAL}, and one column if it is set to {@code
 * VERTICAL}.
 * <p>
 * NOTE: {@code BoxLayout} does not have the ability to wrap.
 * <p>
 * When a {@code BoxLayout} lays out widgets, it tries to size each widget at
 * the widget's preferred size. In the following example a {@link LayoutPanel}
 * is set to use a {@code BoxLayout}. The default orientation of a {@code
 * BoxLayout} is {@code HORIZONTAL}. The {@link LayoutPanel} is added decorated
 * to a {@link org.gwt.mosaic.ui.client.Viewport} so that it fills all browser's
 * content area:
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BoxLayout1.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 * 
 *   LayoutPanel panel = new LayoutPanel(new BoxLayout());
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button(&quot;Button 1&quot;));
 *   panel.add(new Button(&quot;Button 2&quot;));
 *   panel.add(new Button(&quot;Button 3&quot;));
 *   panel.add(new Button(&quot;Button 4&quot;));
 *   viewport.add(panel, true);
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * The next example sets the orientation of the {@link BoxLayout} to
 * {@link Orientation#VERTICAL}:
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BoxLayout2.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 * 
 *   LayoutPanel panel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button(&quot;Button 1&quot;));
 *   panel.add(new Button(&quot;Button 2&quot;));
 *   panel.add(new Button(&quot;Button 3&quot;));
 *   panel.add(new Button(&quot;Button 4&quot;));
 *   viewport.add(panel, true);
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * {@code BoxLayoutData} can specify the size of the widgets. In the next two
 * examples: <em>Button 1</em> will be stretch to fill the available space in
 * both directions (horizontally and vertical); <em>Button 2</em> will be
 * stretched only vertical, <em>Button 3</em> will be stretched only
 * horizontally; <em>Button 4</em> and <em>Button 5</em> will be set to a
 * specific size by giving explicitly the width and height in pixels or ratios
 * (values > 0 and <= 1 are ratios of the {@link LayoutPanel}'s client area
 * except paddings, 0 and values > 1 are pixels, and -1 means preferred size).
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BoxLayout3.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 * 
 *   LayoutPanel panel = new LayoutPanel(new BoxLayout());
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button(&quot;Button 1&quot;), new BoxLayoutData(FillStyle.BOTH));
 *   panel.add(new Button(&quot;Button 2&quot;), new BoxLayoutData(FillStyle.VERTICAL));
 *   panel.add(new Button(&quot;Button 3&quot;), new BoxLayoutData(FillStyle.HORIZONTAL));
 *   panel.add(new Button(&quot;Button 4&quot;), new BoxLayoutData(-1.0, 0.5));
 *   panel.add(new Button(&quot;Button 5&quot;), new BoxLayoutData(-1.0, 150.0));
 *   viewport.add(panel, true);
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="BoxLayout4.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 * 
 *   LayoutPanel panel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
 *   panel.setPadding(10);
 *   panel.setWidgetSpacing(5);
 *   panel.add(new Button(&quot;Button 1&quot;), new BoxLayoutData(FillStyle.BOTH));
 *   panel.add(new Button(&quot;Button 2&quot;), new BoxLayoutData(FillStyle.VERTICAL));
 *   panel.add(new Button(&quot;Button 3&quot;), new BoxLayoutData(FillStyle.HORIZONTAL));
 *   panel.add(new Button(&quot;Button 4&quot;), new BoxLayoutData(0.5, -1.0));
 *   panel.add(new Button(&quot;Button 5&quot;), new BoxLayoutData(150.0, -1.0));
 *   viewport.add(panel, true);
 *   RootPanel.get().add(viewport);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see BoxLayoutData
 */
public class BoxLayout extends BaseLayout {

  public enum Alignment {
    START, CENTER, END
  }

  public enum Orientation {
    HORIZONTAL, VERTICAL
  }

  private Alignment alignment;

  private Orientation orientation;

  boolean leftToRight = true;

  private boolean runTwiceFlag;

  private int visibleWidgetCount = 0;

  private Map<Widget, Dimension> widgetSizes = new HashMap<Widget, Dimension>();

  /**
   * Creates a new instance of {@code BoxLayout} with horizontal orientation.
   */
  public BoxLayout() {
    this(Orientation.HORIZONTAL);
  }

  public BoxLayout(Alignment alignment) {
    this(Orientation.HORIZONTAL, alignment);
  }

  /**
   * Creates a new instance of {@code BoxLayout} with the given orientation.
   * 
   * @param orientation the orientation.
   */
  public BoxLayout(Orientation orientation) {
    this(orientation, Alignment.START);
  }

  public BoxLayout(Orientation orientation, Alignment alignment) {
    this.orientation = orientation;
    this.alignment = alignment;
  }

  @Override
  public void flushCache() {
    widgetSizes.clear();
    initialized = false;
  }

  public Alignment getAlignment() {
    return alignment;
  }

  /**
   * Gets the orientation of the child widgets. The default value is
   * {@link Orientation#HORIZONTAL}.
   * 
   * @return the orientation of the child widgets.
   * @deprecated Replaced by {@link #getOrientation()}.
   */
  public Orientation getOrient() {
    return orientation;
  }

  /**
   * Gets the orientation of the child widgets. The default value is
   * {@link Orientation#HORIZONTAL}.
   * 
   * @return the orientation of the child widgets.
   */
  public Orientation getOrientation() {
    return orientation;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui
   * .client.layout.LayoutPanel)
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

      final int size = visibleWidgetCount;
      if (size == 0) {
        result.width = width;
        result.height = height;
        return result;
      }

      final int spacing = layoutPanel.getWidgetSpacing();

      // adjust for spacing
      if (orientation == Orientation.HORIZONTAL) {
        width += ((size - 1) * spacing);
      } else { // Orientation.VERTICAL
        height += ((size - 1) * spacing);
      }

      int maxWidth = 0;
      int maxHeight = 0;

      for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
        Widget child = iter.next();
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null
            || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;
        
        Dimension clientSize = null;

        if (orientation == Orientation.HORIZONTAL) {
          if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              // Ignore: width += 0;
            } else {
              width += (int) layoutData.preferredWidth;
            }
          } else {
            final Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              if (clientSize == null) {
                clientSize = WidgetHelper.getPreferredSize(child);
              }
              width += clientSize.width;
            } else {
              width += dim.getWidth();
            }
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              width += decPanelBorderWidth;
            }
          }
          if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight >= 0
                && layoutData.preferredHeight <= 1.0) {
              layoutData.calcHeight = 0;
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
          } else {
            final Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              if (clientSize == null) {
                clientSize = WidgetHelper.getPreferredSize(child);
              }
              layoutData.calcHeight = clientSize.height;
            } else {
              layoutData.calcHeight = dim.getHeight();
            }
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
          }
          maxHeight = Math.max(maxHeight, layoutData.calcHeight);
        } else { // Orientation.VERTICAL
          if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              // Ignore: height += 0;
            } else {
              height += (int) layoutData.preferredHeight;
            }
          } else {
            final Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              if (clientSize == null) {
                clientSize = WidgetHelper.getPreferredSize(child);
              }
              height += clientSize.height;
            } else {
              height += dim.getHeight();
            }
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              height += decPanelBorderHeight;
            }
          }
          if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              layoutData.calcWidth = 0;
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
          } else {
            final Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              if (clientSize == null) {
                clientSize = WidgetHelper.getPreferredSize(child);
              }
              layoutData.calcWidth = clientSize.width;
            } else {
              layoutData.calcWidth = dim.getWidth();
            }
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
          }
          maxWidth = Math.max(maxWidth, layoutData.calcWidth);
        }
      }

      if (orientation == Orientation.HORIZONTAL) {
        result.width = width;
        result.height = height + maxHeight;
      } else { // Orientation.VERTICAL
        result.width = width + maxWidth;
        result.height = height;
      }

    } catch (Exception e) {
      Window.alert(this.getClass().getName() + ".getPreferredSize() : "
          + e.getMessage());
    }

    return result;
  }

  private int getVisibleWidgetCount(LayoutPanel layoutPanel) {
    int result = 0;
    for (int i = 0, n = layoutPanel.getWidgetCount(); i < n; i++) {
      Widget child = layoutPanel.getWidget(i);
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }
      if (!DOM.isVisible(child.getElement())) {
        continue;
      }
      ++result;
    }
    return result;
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    visibleWidgetCount = getVisibleWidgetCount(layoutPanel);

    return initialized = true;
  }

  /**
   * If orientation is {@link Orientation#HORIZONTAL} this method returns
   * {@code true} if the child widgets are positioned from left to right,
   * {@code false} otherwise. Default is {@code true}.
   * 
   * @return {@code true} if the child widgets are positioned from left to
   *         right, {@code false} otherwise.
   */
  public boolean isLeftToRight() {
    return leftToRight;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.mosaic.ui.client.layout.LayoutManager#layoutPanel(org.mosaic.ui.client
   * .LayoutPanel)
   */
  public void layoutPanel(LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final int size = visibleWidgetCount;
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
      int fillHeight = height;

      // adjust for spacing
      if (orientation == Orientation.HORIZONTAL) {
        fillWidth -= ((size - 1) * spacing);
      } else { // Orientation.VERTICAL
        fillHeight -= ((size - 1) * spacing);
      }

      int fillingWidth = 0;
      int fillingHeight = 0;

      runTwiceFlag = false;

      final List<Widget> visibleChildList = new ArrayList<Widget>();

      // 1st pass
      for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
        Widget child = iter.next();
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        if (!DOM.isVisible(child.getElement())) {
          continue;
        }

        visibleChildList.add(child);

        Object layoutDataObject = getLayoutData(child);
        if (layoutDataObject == null
            || !(layoutDataObject instanceof BoxLayoutData)) {
          layoutDataObject = new BoxLayoutData();
          setLayoutData(child, layoutDataObject);
        }
        BoxLayoutData layoutData = (BoxLayoutData) layoutDataObject;
        
        if (orientation == Orientation.HORIZONTAL) {
          if (layoutData.fillWidth) {
            fillingWidth++;
          } else if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.preferredWidth * width);
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
            fillWidth -= layoutData.calcWidth;
          } else {
            Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              widgetSizes.put(child, dim = WidgetHelper.getPreferredSize(child));
              runTwiceFlag = true;
            }
            layoutData.calcWidth = dim.getWidth();
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
            fillWidth -= layoutData.calcWidth;
          }
          if (layoutData.fillHeight) {
            layoutData.calcHeight = height;
          } else if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.preferredHeight * height);
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
          } else {
            Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              widgetSizes.put(child, dim = WidgetHelper.getPreferredSize(child));
              runTwiceFlag = true;
            }
            layoutData.calcHeight = dim.getHeight();
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
          }
        } else { // Orientation.VERTICAL
          if (layoutData.fillHeight) {
            fillingHeight++;
          } else if (layoutData.preferredHeight != -1) {
            if (layoutData.preferredHeight > 0
                && layoutData.preferredHeight <= 1.0) {
              layoutData.calcHeight = (int) (layoutData.preferredHeight * height);
            } else {
              layoutData.calcHeight = (int) layoutData.preferredHeight;
            }
            fillHeight -= layoutData.calcHeight;
          } else {
            Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              widgetSizes.put(child, dim = WidgetHelper.getPreferredSize(child));
              runTwiceFlag = true;
            }
            layoutData.calcHeight = dim.getHeight();
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderHeight = decPanel.getOffsetHeight()
                  - child.getOffsetHeight();
              layoutData.calcHeight += decPanelBorderHeight;
            }
            fillHeight -= layoutData.calcHeight;
          }
          if (layoutData.fillWidth) {
            layoutData.calcWidth = width;
          } else if (layoutData.preferredWidth != -1) {
            if (layoutData.preferredWidth > 0
                && layoutData.preferredWidth <= 1.0) {
              layoutData.calcWidth = (int) (layoutData.preferredWidth * width);
            } else {
              layoutData.calcWidth = (int) layoutData.preferredWidth;
            }
          } else {
            Dimension dim = widgetSizes.get(child);
            if (dim == null) {
              widgetSizes.put(child, dim = WidgetHelper.getPreferredSize(child));
              runTwiceFlag = true;
            }
            layoutData.calcWidth = dim.getWidth();
            if (layoutData.hasDecoratorPanel()) {
              final DecoratorPanel decPanel = layoutData.decoratorPanel;
              final int decPanelBorderWidth = decPanel.getOffsetWidth()
                  - child.getOffsetWidth();
              layoutData.calcWidth += decPanelBorderWidth;
            }
          }
        }
      }

      // 2nd pass
      for (Widget child : visibleChildList) {
        if (child instanceof DecoratorPanel) {
          child = ((DecoratorPanel) child).getWidget();
        }

        final BoxLayoutData layoutData = (BoxLayoutData) getLayoutData(child);

        int w = layoutData.calcWidth;
        int h = layoutData.calcHeight;

        if (orientation == Orientation.HORIZONTAL) {
          if (layoutData.fillWidth) {
            w = fillWidth / fillingWidth;
          }
        } else { // Orientation.VERTICAL
          if (layoutData.fillHeight) {
            h = fillHeight / fillingHeight;
          }
        }

        top = Math.max(0, top);

        int fw = w;
        int fh = h;

        if (layoutData.hasDecoratorPanel()) {
          final DecoratorPanel decPanel = layoutData.decoratorPanel;
          final int decPanelBorderWidth = decPanel.getOffsetWidth()
              - child.getOffsetWidth();
          final int decPanelBorderHeight = decPanel.getOffsetHeight()
              - child.getOffsetHeight();

          if (fw != -1) {
            fw -= decPanelBorderWidth;
          }
          if (fh != -1) {
            fh -= decPanelBorderHeight;
          }

          if (orientation == Orientation.VERTICAL) {
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, left, top, fw, fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child,
                  ((width - decPanelBorderWidth) / 2) - (fw / 2) + left, top,
                  fw, fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child,
                  (width - decPanelBorderWidth) - fw + left, top, fw, fh);
            }
          } else if (isLeftToRight()) {
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, left, top, fw, fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child, left,
                  ((height - decPanelBorderHeight) / 2) - (fh / 2) + top, fw,
                  fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child, left,
                  (height - decPanelBorderHeight) - fh + top, fw, fh);
            }
          } else { // !isLeftToRight()
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, box.width +
                  - decPanelBorderWidth
                  - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())), top,
                  fw, fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child, box.width
                  - decPanelBorderWidth
                  - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())),
                  ((height - decPanelBorderHeight) / 2) - (fh / 2) + top, fw,
                  fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child, box.width
                  - decPanelBorderWidth
                  - (left + (fw != -1 ? fw : decPanel.getOffsetWidth())),
                  (height - decPanelBorderHeight) - fh + top, fw, fh);
            }
          }
        } else {
          if (orientation == Orientation.VERTICAL) {
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, left, top, fw, fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child, (width / 2) - (fw / 2)
                  + left, top, fw, fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child, width - fw + left,
                  top, fw, fh);
            }
          } else if (isLeftToRight()) {
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, left, top, fw, fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child, left, (height / 2)
                  - (fh / 2) + top, fw, fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child, left, height - fh
                  + top, fw, fh);
            }
          } else { // !isLeftToRight()
            if (alignment == Alignment.START) {
              WidgetHelper.setBounds(layoutPanel, child, box.width
                  - (left + (fw != -1 ? fw : child.getOffsetWidth())), top, fw,
                  fh);
            } else if (alignment == Alignment.CENTER) {
              WidgetHelper.setBounds(layoutPanel, child, box.width
                  - (left + (fw != -1 ? fw : child.getOffsetWidth())),
                  (height / 2) - (fh / 2) + top, fw, fh);
            } else {
              WidgetHelper.setBounds(layoutPanel, child, box.width
                  - (left + (fw != -1 ? fw : child.getOffsetWidth())), height
                  - fh + top, fw, fh);
            }
          }
        }

        if (orientation == Orientation.HORIZONTAL) {
          left += (w + spacing);
        } else { // Orientation.VERTICAL
          top += (h + spacing);
        }
      }
    } catch (Exception e) {
      Window.alert(getClass().getName() + ".layoutPanel() : " + e.getMessage());
    }

    if (runTwice()) {
      recalculate(widgetSizes);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#runTwice()
   */
  @Override
  public boolean runTwice() {
    return runTwiceFlag;
  }

  public void setAlignment(Alignment align) {
    this.alignment = align;
  }

  /**
   * If orientation is {@link Orientation#HORIZONTAL} this method defines
   * whether the child widgets are positioned from left to right, or from right
   * to left.
   * 
   * @param leftToRight {@code true} if the child widgets are positioned from
   *          left to right, {@code false} otherwise.
   */
  public void setLeftToRight(boolean leftToRight) {
    this.leftToRight = leftToRight;
  }

  /**
   * Sets the orientation of the child widgets.
   * 
   * @param orient the orientation of the child widgets.
   * @deprecated Replaced by {@link #setOrientation(Orientation)}.
   */
  public void setOrient(Orientation orient) {
    this.orientation = orient;
  }

  /**
   * Sets the orientation of the child widgets.
   * 
   * @param orient the orientation of the child widgets.
   */
  public void setOrientation(Orientation orient) {
    this.orientation = orient;
  }

}
