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
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@code FillLayout} lays out the first visible widget forcing it to completely
 * fill the available space. The widget that {@code FillLayout} lays out can
 * have an associated layout data object, called {@link FillLayoutData}.
 * <p>
 * {@code FillLayout} is the default layout manager for {@link LayoutPanel}. In
 * the following example two {@code Buttons} are placed inside a
 * {@link LayoutPanel}. Notice that only the first {@code Button} is rendered.
 * The associated layout data object declares that the {@code Button} will be
 * decorated (the {@code Button} will be placed inside a {@code
 * com.google.gwt.user.client.ui.DecoratorPanel}):
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="FillLayout1.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   LayoutPanel panel = new LayoutPanel();
 *   panel.setPixelSize(320, 200);
 *   panel.setPadding(20);
 *   
 *   panel.add(new Button("Button 1"), new FillLayoutData(true));
 *   
 *   // This button is not rendered!
 *   panel.add(new Button("Button 2"), new FillLayoutData(true));
 *   
 *   RootPanel.get().add(panel);
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * In the next example a {@code Button} placed inside a {@link Viewport} is
 * sized to completely fill the browser's content area:
 * 
 * <table>
 * <tr>
 * <td><img border="1" src="FillLayout2.jpg"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *   
 *   viewport.add(new Button("Button 1"));
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
 * @see FillLayoutData
 */
public class FillLayout extends BaseLayout implements HasAlignment {

  private Widget child;
  private FillLayoutData layoutData;

  private HorizontalAlignmentConstant horizontalAlignment;

  private VerticalAlignmentConstant verticalAlignment;

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    final Dimension result = new Dimension();

    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return result;
      }

      result.setSize(getPreferredSize(layoutPanel, child, layoutData));

      if (layoutData.hasDecoratorPanel()) {
        final Dimension d = getDecoratorFrameSize(layoutData.decoratorPanel,
            child);
        result.width += d.width;
        result.height += d.height;
      }

      result.width += insets.left + insets.right;
      result.height += insets.top + insets.bottom;

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);

      Window.alert(this.getClass().getName() + ".getPreferredSize(): "
          + e.toString());
    }

    return result;
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    child = null;

    for (int i = 0, size = layoutPanel.getWidgetCount(); i < size; i++) {
      final Widget widget = layoutPanel.getWidget(i);

      syncDecoratorVisibility(widget);

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      } else if (child == null) {
        child = widget;
        layoutData = getFillLayoutData(child);
      }
    }

    return initialized = child != null;
  }

  private FillLayoutData getFillLayoutData(Widget child) {
    Object layoutDataObject = getLayoutData(child);
    if (layoutDataObject == null
        || !(layoutDataObject instanceof FillLayoutData)) {
      layoutDataObject = new FillLayoutData();
      setLayoutData(child, layoutDataObject);
    }
    return (FillLayoutData) layoutDataObject;
  }

  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      final Dimension box = DOM.getClientSize(layoutPanel.getElement());

      final int left = paddings[3];
      final int top = paddings[0];
      int width = box.width - (paddings[1] + paddings[3]);
      int height = box.height - (paddings[0] + paddings[2]);

      if (layoutData.hasDecoratorPanel()) {
        final Dimension d = getDecoratorFrameSize(layoutData.decoratorPanel,
            child);
        width -= d.width;
        height -= d.height;
      }

      HorizontalAlignmentConstant hAlignment = layoutData.getHorizontalAlignment();
      if (hAlignment == null) {
        hAlignment = getHorizontalAlignment();
      }

      final int posLeft;
      final int widgetWidth;

      Dimension prefSize = null;

      if (hAlignment == null) {
        posLeft = left;
        widgetWidth = width;
        layoutData.setTargetLeft = layoutData.setTargetWidth = true;
      } else {
        // (ggeorg) this call to WidgetHelper.getPreferredSize() is
        // required even for ALIGN_LEFT
        prefSize = getPreferredSize(layoutPanel, child, layoutData);

        if (HasHorizontalAlignment.ALIGN_LEFT == hAlignment) {
          posLeft = left;
          widgetWidth = prefSize.width;// -1;
        } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
          posLeft = left + (width - prefSize.width) / 2;
          widgetWidth = prefSize.width;// -1;
        } else {
          posLeft = left + width - prefSize.width;
          widgetWidth = prefSize.width;// -1;
        }

        layoutData.setTargetLeft = true;
      }

      VerticalAlignmentConstant vAlignment = layoutData.getVerticalAlignment();
      if (vAlignment == null) {
        vAlignment = getVerticalAlignment();
      }

      final int posTop;
      final int widgetHeight;

      if (vAlignment == null) {
        posTop = top;
        widgetHeight = height;
        layoutData.setTargetTop = layoutData.setTargetHeight = true;
      } else {
        if (prefSize == null) {
          // (ggeorg) this call to WidgetHelper.getPreferredSize() is
          // required even for ALIGN_TOP
          prefSize = getPreferredSize(layoutPanel, child, layoutData);
        }

        if (HasVerticalAlignment.ALIGN_TOP == vAlignment) {
          posTop = top;
          widgetHeight = prefSize.height;// -1;
        } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
          posTop = top + (height - prefSize.height) / 2;
          widgetHeight = prefSize.height;// -1;
        } else {
          posTop = top + height - prefSize.height;
          widgetHeight = prefSize.height;// -1;
        }

        layoutData.setTargetTop = true;
      }

      if (layoutPanel.isAnimationEnabled()) {

        if (animation != null) {
          animation.cancel();
        }

        animation = new Animation() {

          @Override
          protected void onStart() {
            layoutData.sourceLeft = child.getAbsoluteLeft()
                - layoutPanel.getAbsoluteLeft();
            layoutData.sourceTop = child.getAbsoluteTop()
                - layoutPanel.getAbsoluteTop();
            layoutData.sourceWidth = child.getOffsetWidth();
            layoutData.sourceHeight = child.getOffsetHeight();
            super.onStart();
          }

          @Override
          protected void onCancel() {
            onComplete();
          }

          @Override
          protected void onComplete() {
            WidgetHelper.setBounds(layoutPanel, child, posLeft, posTop,
                widgetWidth, widgetHeight);

            // if (callback != null) {
            // callback.onAnimationComplete();
            // }
            animation = null;
            // layoutData.setTargetLeft = layoutData.setTargetWidth =
            // layoutData.setTargetTop = layoutData.setTargetHeight = false;
          }

          @Override
          protected void onUpdate(double progress) {
            layoutData.left = (int) (layoutData.sourceLeft + (posLeft - layoutData.sourceLeft)
                * progress);
            layoutData.top = (int) (layoutData.sourceTop + (posTop - layoutData.sourceTop)
                * progress);
            layoutData.width = (int) (layoutData.sourceWidth + (widgetWidth - layoutData.sourceWidth)
                * progress);
            layoutData.height = (int) (layoutData.sourceHeight + (widgetHeight - layoutData.sourceHeight)
                * progress);

            WidgetHelper.setBounds(layoutPanel, child, layoutData.left,
                layoutData.top, layoutData.width, layoutData.height);
          }
        };

        animation.run(333);

      } else {

        WidgetHelper.setBounds(layoutPanel, child, posLeft, posTop,
            widgetWidth, widgetHeight);

      }

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);

      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.toString());
    }

  }

  private Animation animation = null;

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }

}
