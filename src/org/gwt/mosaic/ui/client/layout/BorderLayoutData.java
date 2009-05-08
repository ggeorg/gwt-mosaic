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

import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.CollapsedListenerCollection;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.user.client.ui.Widget;

/**
 * Layout data object for {@link BorderLayout}. Each widget controlled by a
 * {@link BorderLayout} can have its initial size and region specified by an
 * instance of this layout data object.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see BorderLayout
 */
public class BorderLayoutData extends LayoutData {

  double preferredSize = -1.0;

  Region region;

  boolean resizable, collapse;

  int minSize = 0, maxSize = -1;

  private CollapsedListenerCollection collapsedListeners;

  /**
   * Creates a new instance of {@code BorderLayoutData}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified to {@link Region#CENTER}. The widget will
   * be stretched both horizontally and vertically to fill any space left over.
   */
  public BorderLayoutData() {
    this(Region.CENTER, false);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The region of the widget
   * added to a {@link LayoutPanel} is specified to {@link Region#CENTER}. The
   * widget will be stretched both horizontally and vertically to fill any space
   * left over.
   * 
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  public BorderLayoutData(boolean decorate) {
    this(Region.CENTER, decorate);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified by the {@code region} parameter. The
   * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
   * horizontally, while the height of the widgets is set to the calculated
   * preferred height; the {@link Region#EAST} and {@link Region#WEST} child
   * widgets are stretched vertically, while the width of the widgets is set to
   * the calculated preferred width; the {@link Region#CENTER} child widget will
   * be stretched both horizontally and vertically to fill any space left over.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   */
  public BorderLayoutData(Region region) {
    this(region, false);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The region of the widget
   * added to a {@link LayoutPanel} is specified by the {@code region}
   * parameter. The {@link Region#NORTH} and {@link Region#SOUTH} child widgets
   * are stretched horizontally, while the height of the widgets is set to the
   * calculated preferred height; the {@link Region#EAST} and
   * {@link Region#WEST} child widgets are stretched vertically, while the width
   * of the widgets is set to the calculated preferred width; the
   * {@link Region#CENTER} child widget will be stretched both horizontally and
   * vertically to fill any space left over.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  public BorderLayoutData(Region region, boolean decorate) {
    super(decorate);
    this.region = region;
  }

  /**
   * Creates a new instance of {@code BorderLayoutData}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified by the {@code region} parameter. The
   * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
   * horizontally, while the height of the widgets is specified by the {@code
   * preferredSize} parameter; the {@link Region#EAST} and {@link Region#WEST}
   * child widgets are stretched vertically, while the width of the widgets is
   * specified by the {@code preferredSize} parameter; the {@link Region#CENTER}
   * child widget will be stretched both horizontally and vertically to fill any
   * space left over.
   * <p>
   * For {@code preferredSize} parameter values > 0 and <= 1 are in ratios of
   * the available client area except paddings, 0 and values > 1 are in pixels,
   * and -1 means the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   */
  @Deprecated
  public BorderLayoutData(Region region, double preferredSize) {
    this(region, preferredSize, false);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified by the {@code region} parameter. The
   * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
   * horizontally, while the height of the widgets is specified by the {@code
   * preferredSize} parameter; the {@link Region#EAST} and {@link Region#WEST}
   * child widgets are stretched vertically, while the width of the widgets is
   * specified by the {@code preferredSize} parameter; the {@link Region#CENTER}
   * child widget will be stretched both horizontally and vertically to fill any
   * space left over.
   * <p>
   * For {@code preferredSize} parameter values > 0 and <= 1 are in ratios of
   * the available client area except paddings, 0 and values > 1 are in pixels,
   * and -1 means the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  @Deprecated
  public BorderLayoutData(Region region, double preferredSize, boolean decorate) {
    super(decorate);
    this.region = region;
    this.preferredSize = preferredSize;
  }

  /**
   * Creates a new instance of {@code BorderLayoutData}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified by the {@code region} parameter. The
   * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
   * horizontally, while the initial height of the widgets is specified by the
   * {@code preferredSize} parameter; the {@link Region#EAST} and
   * {@link Region#WEST} child widgets are stretched vertically, while the
   * initial width of the widgets is specified by the {@code preferredSize}
   * parameter; the {@link Region#CENTER} child widget will be stretched both
   * horizontally and vertically to fill any space left over.
   * <p>
   * The {@code minSize} and {@code maxSize} parameters specify that the height,
   * for widgets placed on the {@link Region#NORTH} and {@link Region#SOUTH}
   * regions, or the width, for widgets placed on the {@link Region#EAST} and
   * {@link Region#WEST} regions, can be changed by the user by dragging a split
   * bar, to the value in the range [minSize, maxSize].
   * <p>
   * For all size parameter values > 0 and <= 1 are in ratios of the available
   * client area except paddings, 0 and values > 1 are in pixels, and -1 means
   * the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   * @param minSize the minimum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param maxSize the maximum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   */
  @Deprecated
  public BorderLayoutData(Region region, double preferredSize, int minSize,
      int maxSize) {
    this(region, preferredSize, minSize, maxSize, false);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The region of the widget
   * added to a {@link LayoutPanel} is specified by the {@code region}
   * parameter. The {@link Region#NORTH} and {@link Region#SOUTH} child widgets
   * are stretched horizontally, while the initial height of the widgets is
   * specified by the {@code preferredSize} parameter; the {@link Region#EAST}
   * and {@link Region#WEST} child widgets are stretched vertically, while the
   * initial width of the widgets is specified by the {@code preferredSize}
   * parameter; the {@link Region#CENTER} child widget will be stretched both
   * horizontally and vertically to fill any space left over.
   * <p>
   * The {@code minSize} and {@code maxSize} parameters specify that the height,
   * for widgets placed on the {@link Region#NORTH} and {@link Region#SOUTH}
   * regions, or the width, for widgets placed on the {@link Region#EAST} and
   * {@link Region#WEST} regions, can be changed by the user by dragging a split
   * bar, to the value in the range [minSize, maxSize].
   * <p>
   * For all size parameter values > 0 and <= 1 are in ratios of the available
   * client area except paddings, 0 and values > 1 are in pixels, and -1 means
   * the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   * @param minSize the minimum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param maxSize the maximum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  @Deprecated
  public BorderLayoutData(Region region, double preferredSize, int minSize,
      int maxSize, boolean decorate) {
    super(decorate);
    this.region = region;
    this.preferredSize = preferredSize;
    this.minSize = Math.max(0, minSize);
    this.maxSize = Math.max(0, maxSize);
    this.resizable = minSize < maxSize;
    // TODO
    // if (this.resizable) {
    // this.preferredSize = (this.preferredSize > this.minSize) ? Math.min(
    // this.preferredSize, this.maxSize) : this.minSize;
    // }
  }

  /**
   * Creates a new instance of {@code BorderLayoutData}. The associated widget
   * should be undecorated. The region of the widget added to a
   * {@link LayoutPanel} is specified by the {@code region} parameter. The
   * {@link Region#NORTH} and {@link Region#SOUTH} child widgets are stretched
   * horizontally, while the initial height of the widgets is set to the
   * calculated preferred height; the {@link Region#EAST} and
   * {@link Region#WEST} child widgets are stretched vertically, while the
   * initial width of the widgets is set to the calculated preferred width; the
   * {@link Region#CENTER} child widget will be stretched both horizontally and
   * vertically to fill any space left over.
   * <p>
   * The {@code minSize} and {@code maxSize} parameters specify that the height,
   * for widgets placed on the {@link Region#NORTH} and {@link Region#SOUTH}
   * regions, or the width, for widgets placed on the {@link Region#EAST} and
   * {@link Region#WEST} regions, can be changed by the user by dragging a split
   * bar, to the value in the range [minSize, maxSize].
   * <p>
   * For all size parameter values > 0 and <= 1 are in ratios of the available
   * client area except paddings, 0 and values > 1 are in pixels, and -1 means
   * the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param minSize the minimum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param maxSize the maximum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   */
  @Deprecated
  public BorderLayoutData(Region region, int minSize, int maxSize) {
    this(region, -1.0, minSize, maxSize, false);
  }

  /**
   * Creates a new instance of {@code BorderLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The region of the widget
   * added to a {@link LayoutPanel} is specified by the {@code region}
   * parameter. The {@link Region#NORTH} and {@link Region#SOUTH} child widgets
   * are stretched horizontally, while the initial height of the widgets is set
   * to the calculated preferred height; the {@link Region#EAST} and
   * {@link Region#WEST} child widgets are stretched vertically, while the
   * initial width of the widgets is set to the calculated preferred width; the
   * {@link Region#CENTER} child widget will be stretched both horizontally and
   * vertically to fill any space left over.
   * <p>
   * The {@code minSize} and {@code maxSize} parameters specify that the height,
   * for widgets placed on the {@link Region#NORTH} and {@link Region#SOUTH}
   * regions, or the width, for widgets placed on the {@link Region#EAST} and
   * {@link Region#WEST} regions, can be changed by the user by dragging a split
   * bar, to the value in the range [minSize, maxSize].
   * <p>
   * For all size parameter values > 0 and <= 1 are in ratios of the available
   * client area except paddings, 0 and values > 1 are in pixels, and -1 means
   * the calculated preferred size.
   * 
   * @param region the region of the widget added to a {@link LayoutPanel} with
   *          a {@code BorderLayout}.
   * @param minSize the minimum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param maxSize the maximum widget size, either width or height, that the
   *          widget can be resized to by the user, by dragging a split bar.
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  @Deprecated
  public BorderLayoutData(Region region, int minSize, int maxSize,
      boolean decorate) {
    this(region, -1.0, minSize, maxSize, decorate);
  }

  protected void addCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners == null) {
      collapsedListeners = new CollapsedListenerCollection();
    }
    collapsedListeners.add(listener);
  }

  protected void fireCollapsedChange(Widget sender) {
    if (collapsedListeners != null) {
      collapsedListeners.fireCollapsedChange(sender);
    }
  }

  /**
   * Gets the maximum widget size, either height for widgets placed on
   * {@link Region#NORTH} and {@link Region#SOUTH} or width for widgets placed
   * on {@link Region#WEST} and {@link Region#EAST}, that the widget can be
   * resized by dragging a split bar. Values > 0 and <= 1 are in ratios of the
   * available client area except paddings, 0 and values > 1 are in pixels, and
   * -1 means the calculated preferred size.
   * 
   * @return the maximum widget size, either width or height, that the widget
   *         can be resized by dragging a split bar.
   */
  @Deprecated
  public int getMaxSize() {
    return maxSize;
  }
  
  /**
   * Gets the minimum widget size, either height for widgets placed on
   * {@link Region#NORTH} and {@link Region#SOUTH} or width for widgets placed
   * on {@link Region#WEST} and {@link Region#EAST}, that the widget can be
   * resized by dragging a split bar. Values > 0 and <= 1 are in ratios of the
   * available client area except paddings, 0 and values > 1 are in pixels, and
   * -1 means the calculated preferred size.
   * 
   * @return the maximum widget size, either width or height, that the widget
   *         can be resized by dragging a split bar.
   */
  @Deprecated
  public int getMinSize() {
    return minSize;
  }

  /**
   * Returns the user specified preferred size of a child widget, either width
   * or height in pixels or ratio depending on the {@link LayoutManager}.
   * Default is -1 which means that the widget's calculated preferred size
   * should be used instead. Values > 0 and <= 1 are ratios, 0 and values > 1
   * are pixels.
   * 
   * @return the preferred size or -1 indicating that the widget's calculated
   *         preferred size should be used instead of this value.
   * 
   * @see BaseLayout#getFlowWidth(com.google.gwt.user.client.ui.Widget)
   * @see BaseLayout#getFlowHeight(com.google.gwt.user.client.ui.Widget)
   */
  @Deprecated
  public double getPreferredSize() {
    return preferredSize;
  }

  public Region getRegion() {
    return region;
  }

  public boolean isResizable() {
    return resizable;
  }

  protected void removeCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners != null) {
      collapsedListeners.remove(listener);
    }
  }

  @Deprecated
  protected void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  @Deprecated
  protected void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  /**
   * Sets the child widget's preferred size, either width or height in pixels or
   * ratio depending on the {@link LayoutManager}. Values > 0 and <= 1 are
   * ratios, 0 and values > 1 are pixels, and -1 means that the widget's
   * calculated preferred size should be used.
   * 
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   */
  @Deprecated
  public void setPreferredSize(double preferredSize) {
    this.preferredSize = preferredSize;
  }

  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }
}
