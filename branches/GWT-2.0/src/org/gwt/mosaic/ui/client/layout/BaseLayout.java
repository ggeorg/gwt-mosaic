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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

  protected final Measure marginTopMeasure;
  protected final Measure marginRightMeasure;
  protected final Measure marginBottomMeasure;
  protected final Measure marginLeftMeasure;

  protected final Measure borderTopMeasure;
  protected final Measure borderRightMeasure;
  protected final Measure borderBottomMeasure;
  protected final Measure borderLeftMeasure;

  protected final Measure paddingTopMeasure;
  protected final Measure paddingRightMeasure;
  protected final Measure paddingBottomMeasure;
  protected final Measure paddingLeftMeasure;

  public BaseLayout() {
    componentSizeCache = new WidgetSizeCache();
    minimumWidthMeasure = new MinimumWidthMeasure(componentSizeCache);
    minimumHeightMeasure = new MinimumHeightMeasure(componentSizeCache);
    preferredWidthMeasure = new PreferredWidthMeasure(componentSizeCache);
    preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);

    marginTopMeasure = new MarginTopMeasure(componentSizeCache);
    marginRightMeasure = new MarginRightMeasure(componentSizeCache);
    marginBottomMeasure = new MarginBottomMeasure(componentSizeCache);
    marginLeftMeasure = new MarginLeftMeasure(componentSizeCache);

    borderTopMeasure = new BorderTopMeasure(componentSizeCache);
    borderRightMeasure = new BorderRightMeasure(componentSizeCache);
    borderBottomMeasure = new BorderBottomMeasure(componentSizeCache);
    borderLeftMeasure = new BorderLeftMeasure(componentSizeCache);

    paddingTopMeasure = new PaddingTopMeasure(componentSizeCache);
    paddingRightMeasure = new PaddingTopMeasure(componentSizeCache);
    paddingBottomMeasure = new PaddingTopMeasure(componentSizeCache);
    paddingLeftMeasure = new PaddingTopMeasure(componentSizeCache);
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

  public int[] getMarginSize(Widget widget) {
    return new int[] {
        marginTopMeasure.sizeOf(widget), marginRightMeasure.sizeOf(widget),
        marginBottomMeasure.sizeOf(widget), marginLeftMeasure.sizeOf(widget)};
  }

  public int[] getBorderSize(Widget widget) {
    return new int[] {
        borderTopMeasure.sizeOf(widget), borderRightMeasure.sizeOf(widget),
        borderBottomMeasure.sizeOf(widget), borderLeftMeasure.sizeOf(widget)};
  }

  public int[] getPaddingSize(Widget widget) {
    return new int[] {
        paddingTopMeasure.sizeOf(widget), paddingRightMeasure.sizeOf(widget),
        paddingBottomMeasure.sizeOf(widget), paddingLeftMeasure.sizeOf(widget)};
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
    final Widget parent = child.getParent();
    if (parent instanceof InternalDecoratorPanel) {
      //
      // parent.setVisible(DOM.isVisible(child.getElement()));
      //
      // replaced by:
      parent.setVisible(child.isVisible());
    }
  }

  // -----------------------------------------------------------------------

  protected boolean init(LayoutPanel layoutPanel) {
    if (initialized) {
      return true;
    }

    visibleChildList.clear();

    for (Iterator<Widget> iter = layoutPanel.iterator(); iter.hasNext();) {
      Widget widget = iter.next();

      syncDecoratorVisibility(widget);

      //
      // if (!DOM.isVisible(widget.getElement()))
      //
      // replaced by:
      if (!widget.isVisible()) {
        continue;
      }

      visibleChildList.add(widget);
    }

    return true;
  }

  private void layoutPanelImpl(LayoutPanel layoutPanel) {
    for (Widget child : visibleChildList) {
      if (child instanceof DecoratorPanel) {
        child = ((DecoratorPanel) child).getWidget();
      }

      final LayoutData layoutData = (LayoutData) child.getLayoutData();

      WidgetHelper.setBounds(layoutPanel, child, layoutData.targetLeft,
          layoutData.targetTop, layoutData.targetWidth,
          layoutData.targetHeight, getMarginSize(child), getBorderSize(child),
          getPaddingSize(child));
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
            layoutData.targetHeight, getMarginSize(child),
            getBorderSize(child), getPaddingSize(child));
      }
      return;
    }

    // Cancel the old animation, if there is one.
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
          ((LayoutData) child.getLayoutData()).clearSource();
        }
      }

      @Override
      protected void onUpdate(double progress) {
        for (Widget child : visibleChildList) {

          if (child instanceof DecoratorPanel) {
            child = ((DecoratorPanel) child).getWidget();
          }

          final LayoutData layoutData = (LayoutData) child.getLayoutData();

          layoutData.left = (int) (layoutData.getSourceLeft() + (layoutData.targetLeft - layoutData.getSourceLeft())
              * progress);
          layoutData.top = (int) (layoutData.getSourceTop() + (layoutData.targetTop - layoutData.getSourceTop())
              * progress);

          layoutData.width = (int) (layoutData.getSourceWidth() + (layoutData.targetWidth - layoutData.getSourceWidth())
              * progress);
          layoutData.height = (int) (layoutData.getSourceHeight() + (layoutData.targetHeight - layoutData.getSourceHeight())
              * progress);

          WidgetHelper.setBounds(layoutPanel, child, layoutData.left,
              layoutData.top, layoutData.width, layoutData.height,
              getMarginSize(child), getBorderSize(child), getPaddingSize(child));
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

  // margin ----------------------------------------------------------------

  /**
   * Measures a widget by computing its margin top.
   */
  public static final class MarginTopMeasure extends CachingMeasure {

    public MarginTopMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMarginSize(widget).top;
    }
  }

  /**
   * Measures a widget by computing its margin right.
   */
  public static final class MarginRightMeasure extends CachingMeasure {

    public MarginRightMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMarginSize(widget).right;
    }
  }

  /**
   * Measures a widget by computing its margin bottom.
   */
  public static final class MarginBottomMeasure extends CachingMeasure {

    public MarginBottomMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMarginSize(widget).bottom;
    }
  }

  /**
   * Measures a widget by computing its margin left.
   */
  public static final class MarginLeftMeasure extends CachingMeasure {

    public MarginLeftMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getMarginSize(widget).bottom;
    }
  }

  // border ----------------------------------------------------------------

  /**
   * Measures a widget by computing its border top.
   */
  public static final class BorderTopMeasure extends CachingMeasure {

    public BorderTopMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getBorderSize(widget).top;
    }
  }

  /**
   * Measures a widget by computing its border right.
   */
  public static final class BorderRightMeasure extends CachingMeasure {

    public BorderRightMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getBorderSize(widget).right;
    }
  }

  /**
   * Measures a widget by computing its border bottom.
   */
  public static final class BorderBottomMeasure extends CachingMeasure {

    public BorderBottomMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getBorderSize(widget).bottom;
    }
  }

  /**
   * Measures a widget by computing its border left.
   */
  public static final class BorderLeftMeasure extends CachingMeasure {

    public BorderLeftMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getBorderSize(widget).bottom;
    }
  }

  // padding ----------------------------------------------------------------

  /**
   * Measures a widget by computing its padding top.
   */
  public static final class PaddingTopMeasure extends CachingMeasure {

    public PaddingTopMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getPaddingSize(widget).top;
    }
  }

  /**
   * Measures a widget by computing its padding right.
   */
  public static final class PaddingRightMeasure extends CachingMeasure {

    public PaddingRightMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getPaddingSize(widget).right;
    }
  }

  /**
   * Measures a widget by computing its padding bottom.
   */
  public static final class PaddingBottomMeasure extends CachingMeasure {

    public PaddingBottomMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getPaddingSize(widget).bottom;
    }
  }

  /**
   * Measures a widget by computing its padding left.
   */
  public static final class PaddingLeftMeasure extends CachingMeasure {

    public PaddingLeftMeasure(WidgetSizeCache cache) {
      super(cache);
    }

    public int sizeOf(Widget widget) {
      return cache.getPaddingSize(widget).bottom;
    }
  }

  // Caching Component Sizes ***********************************************

  public final class WidgetSizeCache {

    /** Maps components to their minimum sizes. */
    private final Map<Widget, Dimension> minimumSizes;

    /** Maps components to their preferred sizes. */
    private final Map<Widget, Dimension> preferredSizes;

    /** Maps components to their margin sizes. */
    private final Map<Widget, Insets> marginSizes;

    /** Maps components to their border sizes. */
    private final Map<Widget, Insets> borderSizes;

    /** Maps components to their padding sizes. */
    private final Map<Widget, Insets> paddingSizes;

    /**
     * Constructs a {@code WidgetSizeCache}.
     */
    public WidgetSizeCache() {
      minimumSizes = new HashMap<Widget, Dimension>();
      preferredSizes = new HashMap<Widget, Dimension>();
      marginSizes = new HashMap<Widget, Insets>();
      borderSizes = new HashMap<Widget, Insets>();
      paddingSizes = new HashMap<Widget, Insets>();
    }

    /**
     * Invalidates the cache. Clears all stored size information.
     */
    public void invalidate() {
      minimumSizes.clear();
      preferredSizes.clear();
      marginSizes.clear();
      borderSizes.clear();
      paddingSizes.clear();
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

    /**
     * Returns the margin size for the given widget. Tries to look up the value
     * from the cache; lazily creates the value if it has not been requested
     * before.
     * 
     * @param widget the widget to compute the margin size
     * @return the widget's margin size
     */
    Insets getMarginSize(Widget widget) {
      Insets insets = marginSizes.get(widget);
      if (insets == null) {
        insets = new Insets(DOM.getMarginSizes(widget.getElement()));
        marginSizes.put(widget, insets);
      }
      return insets;
    }

    /**
     * Returns the border size for the given widget. Tries to look up the value
     * from the cache; lazily creates the value if it has not been requested
     * before.
     * 
     * @param widget the widget to compute the border size
     * @return the widget's border size
     */
    Insets getBorderSize(Widget widget) {
      Insets insets = borderSizes.get(widget);
      if (insets == null) {
        insets = new Insets(DOM.getBorderSizes(widget.getElement()));
        borderSizes.put(widget, insets);
      }
      return insets;
    }

    /**
     * Returns the padding size for the given widget. Tries to look up the value
     * from the cache; lazily creates the value if it has not been requested
     * before.
     * 
     * @param widget the widget to compute the padding size
     * @return the widget's border size
     */
    Insets getPaddingSize(Widget widget) {
      Insets insets = paddingSizes.get(widget);
      if (insets == null) {
        insets = new Insets(DOM.getPaddingSizes(widget.getElement()));
        paddingSizes.put(widget, insets);
      }
      return insets;
    }

    public void removeEntry(Widget widget) {
      minimumSizes.remove(widget);
      preferredSizes.remove(widget);
      marginSizes.remove(widget);
      borderSizes.remove(widget);
      paddingSizes.remove(widget);
    }
  }

}
