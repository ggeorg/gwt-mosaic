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
    super();
    this.orientation = orientation;
    this.alignment = alignment;
  }

  /**
   * 
   * @return
   */
  public Alignment getAlignment() {
    return alignment;
  }

  /**
   * 
   * @param align
   */
  public void setAlignment(Alignment align) {
    this.alignment = align;
  }

  /**
   * 
   * @param align
   */
  public void setAlignment(String align) {
    align = align.trim().toLowerCase();
    if (align.equals("start".intern())) {
      setAlignment(Alignment.START);
    } else if (align.equals("center".intern())) {
      setAlignment(Alignment.CENTER);
    } else if (align.equals("end".intern())) {
      setAlignment(Alignment.END);
    }
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

  /**
   * Sets the orientation of the child widgets.
   * 
   * @param orient the orientation of the child widgets.
   */
  public void setOrientation(Orientation orient) {
    this.orientation = orient;
  }

  /**
   * Sets the orientation of child widgets used by UiBinder.
   * 
   * @param orient the orientation of child widgets, can be {@code horizontal}
   *          or {@code vertical}
   */
  public void setOrientation(String orient) {
    orient = orient.trim().toLowerCase();
    if (orient.equals("horizontal".intern())) {
      setOrientation(Orientation.HORIZONTAL);
    } else if (orient.equals("vertical".intern())) {
      setOrientation(Orientation.VERTICAL);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    final Dimension result = new Dimension();

    init(layoutPanel);

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

    // adjust for spacing
    if (orientation == Orientation.HORIZONTAL) {
      width += ((size - 1) * spacing);
    } else { // Orientation.VERTICAL
      height += ((size - 1) * spacing);
    }

    int maxWidth = 0;
    int maxHeight = 0;

    for (Widget child : visibleChildList) {
      final BoxLayoutData layoutData = getLayoutData(child);

      Dimension decPanelFrameSize = null;

      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
            borderSizes[0] + borderSizes[0]);
      }

      if (orientation == Orientation.HORIZONTAL) {

        width += preferredWidthMeasure.sizeOf(child);
        layoutData.calcHeight = preferredHeightMeasure.sizeOf(child);

        if (parent instanceof InternalDecoratorPanel) {
          width += decPanelFrameSize.width;
          layoutData.calcHeight += decPanelFrameSize.height;
        }

        maxHeight = Math.max(maxHeight, layoutData.calcHeight);

      } else { // Orientation.VERTICAL

        height += preferredHeightMeasure.sizeOf(child);
        layoutData.calcWidth = preferredWidthMeasure.sizeOf(child);

        if (parent instanceof InternalDecoratorPanel) {
          height += decPanelFrameSize.height;
          layoutData.calcWidth += decPanelFrameSize.width;
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

    return result;
  }

  public void layoutPanel(LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    if (!init(layoutPanel)) {
      return;
    }

    final int size = visibleChildList.size();
    if (size == 0) {
      return;
    }

    final int spacing = layoutPanel.getWidgetSpacing();

    int left = paddingLeftMeasure.sizeOf(layoutPanel);
    int top = paddingTopMeasure.sizeOf(layoutPanel);
    
    final Dimension box = DOM.getClientSize(layoutPanel.getElement());
    final int width = box.width
        - (left + paddingRightMeasure.sizeOf(layoutPanel));
    final int height = box.height
        - (top + paddingBottomMeasure.sizeOf(layoutPanel));

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

    // 1st pass
    for (Widget child : visibleChildList) {
      Dimension decPanelFrameSize = null;

      BoxLayoutData layoutData = getLayoutData(child);

      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
            borderSizes[0] + borderSizes[0]);
      }

      if (orientation == Orientation.HORIZONTAL) {
        if (layoutData.fillWidth) {
          fillingWidth++;
        } else {
          layoutData.calcWidth = preferredWidthMeasure.sizeOf(child);
          if (parent instanceof InternalDecoratorPanel) {
            layoutData.calcWidth += decPanelFrameSize.width;
          }
          fillWidth -= layoutData.calcWidth;
        }
        if (layoutData.fillHeight) {
          layoutData.calcHeight = height;
        } else {
          layoutData.calcHeight = preferredHeightMeasure.sizeOf(child);
          if (parent instanceof InternalDecoratorPanel) {
            layoutData.calcHeight += decPanelFrameSize.height;
          }
        }
      } else { // Orientation.VERTICAL
        if (layoutData.fillHeight) {
          fillingHeight++;
        } else {
          layoutData.calcHeight = preferredHeightMeasure.sizeOf(child);
          if (parent instanceof InternalDecoratorPanel) {
            layoutData.calcHeight += decPanelFrameSize.height;
          }
          fillHeight -= layoutData.calcHeight;
        }
        if (layoutData.fillWidth) {
          layoutData.calcWidth = width;
        } else {
          layoutData.calcWidth = preferredWidthMeasure.sizeOf(child);
          if (parent instanceof InternalDecoratorPanel) {
            layoutData.calcWidth += decPanelFrameSize.width;
          }
        }
      }
    }

    // 2nd pass
    for (Widget child : visibleChildList) {
      Dimension decPanelFrameSize = null;

      final BoxLayoutData layoutData = (BoxLayoutData) child.getLayoutData();
      
      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        decPanelFrameSize = new Dimension(borderSizes[1] + borderSizes[3],
            borderSizes[0] + borderSizes[0]);
      }

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

      if (parent instanceof InternalDecoratorPanel) {
        fw -= decPanelFrameSize.width;
        fh -= decPanelFrameSize.height;
      }

      // Orientation.VERTICAL

      if (orientation == Orientation.VERTICAL) {
        if (alignment == Alignment.START) {
          layoutData.targetLeft = left;
          layoutData.targetTop = top;
          layoutData.targetWidth = fw;
          layoutData.targetHeight = fh;
        } else if (alignment == Alignment.CENTER) {
          layoutData.targetLeft = left + (width / 2) - (w / 2);
          layoutData.targetTop = top;
          layoutData.targetWidth = fw;
          layoutData.targetHeight = fh;
        } else {
          layoutData.targetLeft = left + width - w;
          layoutData.targetTop = top;
          layoutData.targetWidth = fw;
          layoutData.targetHeight = fh;
        }

        top += (h + spacing);
      }

      // Orientation.HORIZONTAL

      else {

        if (isLeftToRight()) { // Left to Right
          if (alignment == Alignment.START) {
            layoutData.targetLeft = left;
            layoutData.targetTop = top;
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          } else if (alignment == Alignment.CENTER) {
            layoutData.targetLeft = left;
            layoutData.targetTop = top + (height / 2) - (h / 2);
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          } else {
            layoutData.targetLeft = left;
            layoutData.targetTop = top + height - h;
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          }
        } else { // Right to Left
          if (alignment == Alignment.START) {
            layoutData.targetLeft = box.width - (left + w);
            layoutData.targetTop = top;
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          } else if (alignment == Alignment.CENTER) {
            layoutData.targetLeft = box.width - (left + w);
            layoutData.targetTop = top + (height / 2) - (h / 2);
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          } else {
            layoutData.targetLeft = box.width - (left + w);
            layoutData.targetTop = top + height - h;
            layoutData.targetWidth = fw;
            layoutData.targetHeight = fh;
          }
        }

        left += (w + spacing);
      }

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

  /**
   * If orientation is {@link Orientation#HORIZONTAL} this method defines
   * whether the child widgets are positioned from left to right, or from right
   * to left.
   * 
   * @param leftToRight {@code true} if the child widgets are positioned from
   *          left to right, {@code false} otherwise.
   */
  public void setLeftToRight(final boolean leftToRight) {
    this.leftToRight = leftToRight;
  }

  /**
   * If orientation is {@link Orientation#HORIZONTAL} this method defines
   * whether the child widgets are positioned from left to right, or from right
   * to left, used by UiBinder .
   * 
   * @param leftToRight {@code true} if the child widgets are positioned from
   *          left to right, {@code false} otherwise.
   */
  public void setLeftToRight(String leftToRight) {
    leftToRight = leftToRight.trim().toLowerCase();
    if (leftToRight.equals("true".intern())) {
      setLeftToRight(true);
    } else if (leftToRight.equals("false".intern())) {
      setLeftToRight(false);
    }
  }

  private BoxLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof BoxLayoutData)) {
      layoutDataObject = new BoxLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (BoxLayoutData) layoutDataObject;
  }

}
