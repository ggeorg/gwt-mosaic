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
import org.gwt.mosaic.ui.client.Viewport;

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
 *   viewport.getLayoutPanel().add(new Button("Button 1"));
 *   
 *   viewport.attach();
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * In the next example a {@code Button} placed inside a {@link Viewport} is
 * using size and alignment hints:
 * 
 * <table>
 * <tr>
 * <td><img border="0" src="FillLayout3.png"></td>
 * <td>
 * 
 * <pre>
 * public void onModuleLoad() {
 *   Viewport viewport = new Viewport();
 *   
 *   FillLayoutData layoutData = new FillLayoutData();
 *   layoutData.setHorizontalAlignment(FillLayoutData.ALIGN_RIGHT);
 *   layoutData.setVerticalAlignment(FillLayoutData.ALIGN_TOP);
 *   layoutData.xsetPreferredWidth("10em");
 *   layoutData.xsetPreferredHeight("50%");
 *
 *   LayoutPanel layoutPanel = viewport.getLayoutPanel();
 *   layoutPanel.add(new Button("Button 1"), layoutData);
 *   
 *   viewport.attach();
 * }
 * </pre>
 * 
 * </td>
 * </tr>
 * </table>
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

      result.setSize(preferredWidthMeasure.sizeOf(child),
          preferredHeightMeasure.sizeOf(child));

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

  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    child = null;

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      syncDecoratorVisibility(widget);

      if (!DOM.isVisible(widget.getElement())) {
        continue;
      } else if (child == null) {
        child = widget;
        layoutData = getFillLayoutData(child);
        visibleChildList.add(child);
      }
    }

    return initialized = child != null;
  }

  private FillLayoutData getFillLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof FillLayoutData)) {
      layoutDataObject = new FillLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (FillLayoutData) layoutDataObject;
  }

  public void layoutPanel(final LayoutPanel layoutPanel) {
    try {
      if (layoutPanel == null || !init(layoutPanel)) {
        return;
      }

      if (!child.isVisible()) {
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

      Dimension prefSize = null;

      if (hAlignment == null) {
        layoutData.targetLeft = left;
        layoutData.targetWidth = width;
      } else {
        // (ggeorg) this call to WidgetHelper.getPreferredSize() is
        // required even for ALIGN_LEFT
        prefSize = new Dimension(preferredWidthMeasure.sizeOf(child),
            preferredHeightMeasure.sizeOf(child));

        if (HasHorizontalAlignment.ALIGN_LEFT == hAlignment) {
          layoutData.targetLeft = left;
        } else if (HasHorizontalAlignment.ALIGN_CENTER == hAlignment) {
          layoutData.targetLeft = left + (width - prefSize.width) / 2;
        } else {
          layoutData.targetLeft = left + width - prefSize.width;
        }
        layoutData.targetWidth = prefSize.width;
      }

      VerticalAlignmentConstant vAlignment = layoutData.getVerticalAlignment();
      if (vAlignment == null) {
        vAlignment = getVerticalAlignment();
      }

      if (vAlignment == null) {
        layoutData.targetTop = top;
        layoutData.targetHeight = height;
      } else {
        if (prefSize == null) {
          // (ggeorg) this call to WidgetHelper.getPreferredSize() is
          // required even for ALIGN_TOP
          prefSize = new Dimension(preferredWidthMeasure.sizeOf(child),
              preferredHeightMeasure.sizeOf(child));
        }

        if (HasVerticalAlignment.ALIGN_TOP == vAlignment) {
          layoutData.targetTop = top;
        } else if (HasVerticalAlignment.ALIGN_MIDDLE == vAlignment) {
          layoutData.targetTop = top + (height - prefSize.height) / 2;
        } else {
          layoutData.targetTop = top + height - prefSize.height;
        }
        layoutData.targetHeight = prefSize.height;
      }

      layoutData.setSourceLeft(child.getAbsoluteLeft()
          - layoutPanel.getAbsoluteLeft() - paddings[3]);
      layoutData.setSourceTop(child.getAbsoluteTop()
          - layoutPanel.getAbsoluteTop() - paddings[0]);
      layoutData.setSourceWidth(child.getOffsetWidth());
      layoutData.setSourceHeight(child.getOffsetHeight());

      super.layoutPanel(layoutPanel);

    } catch (Exception e) {
      GWT.log(e.getMessage(), e);

      Window.alert(this.getClass().getName() + ".layoutPanel(): "
          + e.toString());
    }

  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }
  
  public void setHorizontalAlignment(String align) {
    align = align.trim().toLowerCase();
    if (align.equals("left".intern())) {
      setHorizontalAlignment(ALIGN_LEFT);
    } else if (align.equals("center".intern())) {
      setHorizontalAlignment(ALIGN_CENTER);
    } else if (align.equals("right".intern())) {
      setHorizontalAlignment(ALIGN_RIGHT);
    } else if (align.equals("default".intern())) {
      setHorizontalAlignment(ALIGN_DEFAULT);
    }
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }
  
  public void setVerticalAlignment(String align) {
    align = align.trim().toLowerCase();
    if (align.equals("top".intern())) {
      setVerticalAlignment(ALIGN_TOP);
    } else if (align.equals("middle".intern())) {
      setVerticalAlignment(ALIGN_MIDDLE);
    } else if (align.equals("bottom".intern())) {
      setVerticalAlignment(ALIGN_BOTTOM);
    }
  }

}
