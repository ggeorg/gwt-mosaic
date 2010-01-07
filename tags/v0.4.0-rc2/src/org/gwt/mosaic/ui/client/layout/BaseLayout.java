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
import java.util.List;
import java.util.Map;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Insets;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is the abstract base class for layouts.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class BaseLayout implements LayoutManager {

  protected final List<Widget> visibleChildList = new ArrayList<Widget>();

  protected boolean initialized = false;

  @Deprecated
  protected int[] margins = {0, 0};
  @Deprecated
  protected int[] borders = {0, 0};
  @Deprecated
  protected int[] paddings = {0, 0};

  /**
   * Caches widget minimum and preferred sizes. All requests for component sizes
   * shall be directed to the cache.
   */
  protected final WidgetSizeCache componentSizeCache;

  /**
   * These functional objects are used to measure component sizes. They abstract
   * from horizontal and vertical orientation and so, allow to implement the
   * layout algorithm for both orientations with a single set of methods.
   */
  protected final Measure minimumWidthMeasure;
  protected final Measure minimumHeightMeasure;
  protected final Measure preferredWidthMeasure;
  protected final Measure preferredHeightMeasure;

  public BaseLayout() {
    componentSizeCache = new WidgetSizeCache();
    minimumWidthMeasure = new MinimumWidthMeasure(componentSizeCache);
    minimumHeightMeasure = new MinimumHeightMeasure(componentSizeCache);
    preferredWidthMeasure = new PreferredWidthMeasure(componentSizeCache);
    preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);
  }

  /**
   * Invalidates the component size caches.
   * 
   * @param widget the {@link Widget} that if the layout manager has cached
   *          information that should be discarded, or {@code null} for all
   *          widgets
   */
  protected void invalidateCaches(Widget widget) {
    if (widget != null) {
      componentSizeCache.removeEntry(widget);
    } else {
      componentSizeCache.invalidate();
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.LayoutManager#invalidateLayout(com.google.gwt.user.client.ui.Widget)
   */
  public void invalidateLayout(Widget widget) {
    invalidateCaches(widget);
    initialized = false;
  }

  // -----------------

  protected Dimension getPreferredSize(LayoutPanel layoutPanel, Widget widget,
      LayoutData layoutData) {
    // Ignore FormPanel if getWidget() returns a HasLayoutManager implementation
    if (widget instanceof FormPanel) {
      final Widget child = ((FormPanel) widget).getWidget();
      if (child != null && (child instanceof HasLayoutManager)) {
        widget = child;
      }
    }
    if (widget instanceof HasLayoutManager) {
      final HasLayoutManager lp = (HasLayoutManager) widget;

      if (layoutData.getPreferredWidth() != null
          && layoutData.getPreferredHeight() != null) {
        return new Dimension(layoutPanel.toPixelSize(
            layoutData.getPreferredWidth(), true), layoutPanel.toPixelSize(
            layoutData.getPreferredHeight(), false));
      }

      final Dimension result = lp.getPreferredSize();

      if (layoutData.getPreferredWidth() != null) {
        result.width = layoutPanel.toPixelSize(layoutData.getPreferredWidth(),
            true);
      }

      if (layoutData.getPreferredHeight() != null) {
        result.height = layoutPanel.toPixelSize(
            layoutData.getPreferredHeight(), false);
      }

      return result;

    } else {

      if (layoutData.getPreferredWidth() != null
          && layoutData.getPreferredHeight() != null) {
        return new Dimension(layoutPanel.toPixelSize(
            layoutData.getPreferredWidth(), true), layoutPanel.toPixelSize(
            layoutData.getPreferredHeight(), false));
      }

      final Dimension result = new Dimension();

      final Element parentElem = layoutPanel.getElement();
      final Element clonedElem = widget.getElement().cloneNode(true).cast();

      final Style style = clonedElem.getStyle();
      style.setProperty("position", "static");
      style.setProperty("visibility", "hidden");

      parentElem.appendChild(clonedElem);

      if (layoutData.getPreferredWidth() != null) {
        result.width = layoutPanel.toPixelSize(layoutData.getPreferredWidth(),
            true);
      } else {
        style.setProperty("width", "auto");
        result.width = clonedElem.getOffsetWidth();
      }

      if (layoutData.getPreferredHeight() != null) {
        result.height = layoutPanel.toPixelSize(
            layoutData.getPreferredHeight(), false);
      } else {
        style.setProperty("height", "auto");
        result.height = clonedElem.getOffsetHeight();
      }

      parentElem.removeChild(clonedElem);

      return result;
    }
  }

  protected void syncDecoratorVisibility(Widget child) {
    LayoutData layoutData = (LayoutData) child.getLayoutData();
    if (layoutData != null && layoutData.hasDecoratorPanel()) {
      layoutData.decoratorPanel.setVisible(DOM.isVisible(child.getElement()));
    }
  }

  // DecoratorPanel width calculations *************************************

  protected int getDecoratorFrameWidth(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetWidth() - child.getOffsetWidth();
  }

  protected int getDecoratorFrameHeight(DecoratorPanel decPanel, Widget child) {
    return decPanel.getOffsetHeight() - child.getOffsetHeight();
  }

  protected Dimension getDecoratorFrameSize(DecoratorPanel decPanel,
      Widget child) {
    return new Dimension(decPanel.getOffsetWidth() - child.getOffsetWidth(),
        decPanel.getOffsetHeight() - child.getOffsetHeight());
  }

  protected Insets insets = new Insets(0, 0, 0, 0);

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    final Element layoutPanelElem = layoutPanel.getElement();
    margins = DOM.getMarginSizes(layoutPanelElem);
    borders = DOM.getBorderSizes(layoutPanelElem);
    paddings = DOM.getPaddingSizes(layoutPanelElem);

    insets.top = margins[0] + borders[0] + paddings[0];
    insets.right = margins[1] + borders[1] + paddings[1];
    insets.bottom = margins[2] + borders[2] + paddings[2];
    insets.left = margins[3] + borders[3] + paddings[3];

    visibleChildList.clear();

    return true;
  }

  private void layoutPanelImpl(LayoutPanel layoutPanel) {
    for (Widget child : visibleChildList) {
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      final LayoutData layoutData = (LayoutData) getLayoutData(child);

      WidgetHelper.setBounds(layoutPanel, child, layoutData.targetLeft,
          layoutData.targetTop, layoutData.targetWidth, layoutData.targetHeight);
    }

    layoutPanel.layoutChildren();
  }

  // Layout & Transition animation *****************************************

  private Animation animation = null;

  public void layoutPanel(final LayoutPanel layoutPanel) {
    if (!layoutPanel.isAnimationEnabled()) {
      for (Widget child : visibleChildList) {
        LayoutData layoutData = (LayoutData) child.getLayoutData();
        WidgetHelper.setBounds(layoutPanel, child, layoutData.targetLeft,
            layoutData.targetTop, layoutData.targetWidth,
            layoutData.targetHeight);
      }
      return;
    }

    if (animation != null) {
      animation.cancel();
    }

    animation = new Animation() {

      @Override
      protected void onCancel() {
        onComplete();
      }

      @Override
      protected void onComplete() {
        layoutPanelImpl(layoutPanel);
        if (layoutPanel.animationCallback != null) {
          layoutPanel.animationCallback.onAnimationComplete();
        }

        animation = null;

        for (Widget child : visibleChildList) {
          if (child instanceof DecoratorPanel) {
            child = ((DecoratorPanel) child).getWidget();
          }
          ((LayoutData) getLayoutData(child)).clearSource();
        }
      }

      @Override
      protected void onUpdate(double progress) {
        for (Widget child : visibleChildList) {
          if (child instanceof DecoratorPanel) {
            child = ((DecoratorPanel) child).getWidget();
          }

          final LayoutData layoutData = (LayoutData) getLayoutData(child);

          layoutData.left = (int) (layoutData.getSourceLeft() + (layoutData.targetLeft - layoutData.getSourceLeft())
              * progress);
          layoutData.top = (int) (layoutData.getSourceTop() + (layoutData.targetTop - layoutData.getSourceTop())
              * progress);

          layoutData.width = (int) (layoutData.getSourceWidth() + (layoutData.targetWidth - layoutData.getSourceWidth())
              * progress);
          layoutData.height = (int) (layoutData.getSourceHeight() + (layoutData.targetHeight - layoutData.getSourceHeight())
              * progress);

          WidgetHelper.setBounds(layoutPanel, child, layoutData.left,
              layoutData.top, layoutData.width, layoutData.height);
        }
      }
    };

    animation.run(CoreConstants.DEFAULT_DELAY_MILLIS);
  }

  // Measuring Widget Sizes ************************************************

  public static interface Measure {
    /**
     * Computes and returns the size of the given {@code Widget}.
     * 
     * @param widget the widget to measure
     * @return the widget's size
     */
    int sizeOf(Widget widget);
  }

  private abstract static class CachingMeasure implements Measure {

    /**
     * Holds previously requested widget sizes. Used to minimize size requests
     * to sub-widgets.
     */
    protected final WidgetSizeCache cache;

    private CachingMeasure(WidgetSizeCache cache) {
      this.cache = cache;
    }
  }

  /**
   * Measures a widget by computing its minimum width.
   */
  public static final class MinimumWidthMeasure extends CachingMeasure {
    public MinimumWidthMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMinimumSize(widget).width;
    }
  }

  /**
   * Measures a width by computing its minimum height.
   */
  public static final class MinimumHeightMeasure extends CachingMeasure {
    public MinimumHeightMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMinimumSize(widget).height;
    }
  }

  /**
   * Measures a widget by computing its preferred width.
   */
  public static final class PreferredWidthMeasure extends CachingMeasure {
    public PreferredWidthMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getPreferredSize(c).width;
    }
  }

  /**
   * Measures a widget by computing its preferred height.
   */
  public static final class PreferredHeightMeasure extends CachingMeasure {

    public PreferredHeightMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget c) {
      return cache.getPreferredSize(c).height;
    }
  }

  // Caching Component Sizes ***********************************************

  public final class WidgetSizeCache {

    /** Maps components to their minimum sizes. */
    private final Map<Widget, Dimension> minimumSizes;

    /** Maps components to their preferred sizes. */
    private final Map<Widget, Dimension> preferredSizes;

    /**
     * Constructs a {@code WidgetSizeCache}.
     */
    public WidgetSizeCache() {
      minimumSizes = new HashMap<Widget, Dimension>();
      preferredSizes = new HashMap<Widget, Dimension>();
    }

    /**
     * Invalidates the cache. Clears all stored size information.
     */
    public void invalidate() {
      minimumSizes.clear();
      preferredSizes.clear();
    }

    /**
     * Returns the minimum size for the given widget. Tries to look up the value
     * from the cache; lazy creates the value if it has not been requested
     * before.
     * 
     * @param widget the widget to compute the minimum size
     * @return the widget's minimum size
     */
    Dimension getMinimumSize(Widget widget) {
      Dimension size = minimumSizes.get(widget);
      if (size == null) {
        final String minWidth = DOM.getComputedStyleAttribute(
            widget.getElement(), "minWidth");
        final String minHeight = DOM.getComputedStyleAttribute(
            widget.getElement(), "minHeight");
        size = new Dimension(minWidth == null ? 1 : DOM.toPixelSize(minWidth,
            true), minHeight == null ? 1 : DOM.toPixelSize(minHeight, false));
        minimumSizes.put(widget, size);
      }
      return size;
    }

    /**
     * Returns the preferred size for the given widget. Tries to look up the
     * value from the cache; lazily creates the value if it has not been
     * requested before.
     * 
     * @param widget the widget to compute the preferred size
     * @return the widget's preferred size
     */
    Dimension getPreferredSize(Widget widget) {
      Dimension size = preferredSizes.get(widget);
      if (size == null) {
        LayoutData layoutData = (LayoutData) widget.getLayoutData();
        size = BaseLayout.this.getPreferredSize(
            (LayoutPanel) WidgetHelper.getParent(widget), widget, layoutData);
        // don't cache percentage units
        if (!((layoutData.getPreferredWidth() != null && Unit.PCT == layoutData.getPreferredWidth().getUnit()) || (layoutData.getPreferredHeight() != null && Unit.PCT != layoutData.getPreferredHeight().getUnit()))) {
          preferredSizes.put(widget, size);
        }
      }
      return size;
    }

    public void removeEntry(Widget widget) {
      minimumSizes.remove(widget);
      preferredSizes.remove(widget);
    }
  }

  // LayoutData setter & getter methods ************************************

  /**
   * Gets the panel-defined layout data associated with this widget.
   * 
   * @param widget the widget
   * @return the widget's layout data
   * @deprecated use {@link Widget#getLayoutData()} instead
   */
  @Deprecated
  protected final static Object getLayoutData(Widget widget) {
    return widget.getLayoutData();
  }

  /**
   * Sets the panel-defined layout data associated with this widget. Only the
   * panel that currently contains a widget should ever set this value. It
   * serves as a place to store layout bookkeeping data associated with a
   * widget.
   * 
   * @param widget the widget
   * @param layoutData the widget's layout data
   * @deprecated use {@link Widget#setLayoutData(Object)} instead
   */
  @Deprecated
  protected final static void setLayoutData(Widget widget, Object layoutData) {
    widget.setLayoutData(layoutData);
  }

}
