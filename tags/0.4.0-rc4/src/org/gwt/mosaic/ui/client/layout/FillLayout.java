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
import org.gwt.mosaic.ui.client.Viewport;

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

  private HorizontalAlignmentConstant horizontalAlignment;
  private VerticalAlignmentConstant verticalAlignment;
  
  private Widget child;
  private FillLayoutData layoutData;

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasHorizontalAlignment#getHorizontalAlignment()
   */
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasHorizontalAlignment#setHorizontalAlignment(com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant)
   */
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }

  /**
   * Used by UiBinder parser.
   * 
   * @param align
   */
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

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasVerticalAlignment#getVerticalAlignment()
   */
  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasVerticalAlignment#setVerticalAlignment(com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant)
   */
  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }

  /**
   * Used by UiBinder parser.
   * 
   * @param align
   */
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

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#getPreferredSize(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  public Dimension getPreferredSize(LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    final Dimension result = new Dimension();

    init(layoutPanel);

    if (child != null) {
      result.setSize(preferredWidthMeasure.sizeOf(child),
          preferredHeightMeasure.sizeOf(child));

      final Widget parent = child.getParent();
      if (parent instanceof InternalDecoratorPanel) {
        final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
        final int borderSizes[] = decPanel.getBorderSizes();
        result.width += (borderSizes[1] + borderSizes[3]);
        result.height += (borderSizes[0] + borderSizes[0]);
      }
    }

    result.width += marginLeftMeasure.sizeOf(layoutPanel)
        + marginRightMeasure.sizeOf(layoutPanel)
        + borderLeftMeasure.sizeOf(layoutPanel)
        + borderRightMeasure.sizeOf(layoutPanel)
        + paddingLeftMeasure.sizeOf(layoutPanel)
        + paddingRightMeasure.sizeOf(layoutPanel);

    result.height += marginTopMeasure.sizeOf(layoutPanel)
        + marginBottomMeasure.sizeOf(layoutPanel)
        + borderTopMeasure.sizeOf(layoutPanel)
        + borderBottomMeasure.sizeOf(layoutPanel)
        + paddingTopMeasure.sizeOf(layoutPanel)
        + paddingBottomMeasure.sizeOf(layoutPanel);

    return result;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#layoutPanel(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  public void layoutPanel(final LayoutPanel layoutPanel) {
    assert layoutPanel != null;

    if (!init(layoutPanel)) {
      return;
    }

    final int left = paddingLeftMeasure.sizeOf(layoutPanel);
    final int top = paddingTopMeasure.sizeOf(layoutPanel);

    final Dimension box = DOM.getClientSize(layoutPanel.getElement());
    int width = box.width - (left + paddingRightMeasure.sizeOf(layoutPanel));
    int height = box.height - (top + paddingBottomMeasure.sizeOf(layoutPanel));

    final Widget parent = child.getParent();
    if (parent instanceof InternalDecoratorPanel) {
      final InternalDecoratorPanel decPanel = (InternalDecoratorPanel) parent;
      final int borderSizes[] = decPanel.getBorderSizes();
      width -= (borderSizes[1] + borderSizes[3]);
      height -= (borderSizes[0] + borderSizes[2]);
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

    super.layoutPanel(layoutPanel);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.BaseLayout#init(org.gwt.mosaic.ui.client.layout.LayoutPanel)
   */
  @Override
  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    super.init(layoutPanel);

    if (visibleChildList.size() > 0) {
      child = visibleChildList.get(0);
      layoutData = getLayoutData(child);
      visibleChildList.clear();
      visibleChildList.add(child);
    } else {
      child = null;
      layoutData = null;
    }

    return initialized = (child != null);
  }

  private FillLayoutData getLayoutData(Widget child) {
    Object layoutDataObject = child.getLayoutData();
    if (layoutDataObject == null
        || !(layoutDataObject instanceof FillLayoutData)) {
      layoutDataObject = new FillLayoutData();
      child.setLayoutData(layoutDataObject);
    }
    return (FillLayoutData) layoutDataObject;
  }

}