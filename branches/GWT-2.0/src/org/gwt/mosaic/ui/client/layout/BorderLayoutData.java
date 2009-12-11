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

import com.google.gwt.dom.client.Style.Unit;
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

  Region region;

  boolean resizable;
  
  @Deprecated
  boolean collapse;
  
  Widget collapsedStateWidget = null;

  String minSize = null, maxSize = null;

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

  public BorderLayoutData(Region region, String preferredSize) {
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

    if (region == Region.NORTH || region == Region.SOUTH) {
      if (preferredSize > 1.0) {
        setPreferredHeight(((int) preferredSize) + "px");
      } else if (preferredSize > 0.0) {
        setPreferredHeight(((int) (preferredSize * 100.0)) + "%");
      } else {
        setPreferredHeight(null);
      }
    } else if (region == Region.WEST || region == Region.EAST) {
      if (preferredSize > 1.0) {
        setPreferredWidth(((int) preferredSize) + "px");
      } else if (preferredSize > 0.0) {
        setPreferredWidth(((int) (preferredSize * 100.0)) + "%");
      } else {
        setPreferredWidth(null);
      }
    }
  }

  public BorderLayoutData(Region region, String preferredSize, boolean decorate) {
    super(decorate);
    setRegion(region);
    if (region == Region.NORTH || region == Region.SOUTH) {
      setPreferredHeight(preferredSize);
    } else if (region == Region.WEST || region == Region.EAST) {
      setPreferredWidth(preferredSize);
    }
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

  public BorderLayoutData(Region region, String preferredSize, String minSize,
      String maxSize) {
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

    if (region == Region.NORTH || region == Region.SOUTH) {
      if (preferredSize > 1.0) {
        setPreferredHeight(((int) preferredSize) + "px");
      } else if (preferredSize > 0.0) {
        setPreferredHeight(((int) (preferredSize * 100.0)) + "%");
      } else {
        setPreferredHeight(null);
      }
    } else if (region == Region.WEST || region == Region.EAST) {
      if (preferredSize > 1.0) {
        setPreferredWidth(((int) preferredSize) + "px");
      } else if (preferredSize > 0.0) {
        setPreferredWidth(((int) (preferredSize * 100.0)) + "%");
      } else {
        setPreferredWidth(null);
      }
    }

    this.minSize = Math.max(0, minSize) + Unit.PX.getType();
    this.maxSize = Math.max(0, maxSize) + Unit.PX.getType();
    this.resizable = minSize < maxSize;
  }

  public BorderLayoutData(Region region, String preferredSize, String minSize,
      String maxSize, boolean decorate) {
    super(decorate);
    setRegion(region);
    if (region == Region.NORTH || region == Region.SOUTH) {
      setPreferredHeight(preferredSize);
    } else if (region == Region.WEST || region == Region.EAST) {
      setPreferredWidth(preferredSize);
    }
    setMinSize(minSize);
    setMaxSize(maxSize);
    if (minSize != null && maxSize != null) {
      setResizable(true);
    }
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

  public BorderLayoutData(Region region, String minSize, String maxSize) {
    this(region, null, minSize, maxSize, false);
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

  public BorderLayoutData(Region region, String minSize, String maxSize,
      boolean decorate) {
    this(region, null, minSize, maxSize, decorate);
  }

  /**
   * @return the region
   */
  public Region getRegion() {
    return region;
  }

  /**
   * @param region the region to set
   */
  public void setRegion(Region region) {
    this.region = region;
  }

  /**
   * @return the minSize
   */
  public String getMinSize() {
    return minSize;
  }

  /**
   * @param minSize the minSize to set
   */
  public void setMinSize(String minSize) {
    this.minSize = minSize;
  }

  /**
   * @return the maxSize
   */
  public String getMaxSize() {
    return maxSize;
  }

  /**
   * @param maxSize the maxSize to set
   */
  public void setMaxSize(String maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * @return the resizable
   */
  public boolean isResizable() {
    return resizable;
  }

  /**
   * @param resizable the resizable to set
   */
  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  /**
   * @return the collapsedStateWidget
   */
  public Widget getCollapsedStateWidget() {
    return collapsedStateWidget;
  }

  /**
   * @param collapsedStateWidget the collapsedStateWidget to set
   */
  public void setCollapsedStateWidget(Widget collapsedStateWidget) {
    this.collapsedStateWidget = collapsedStateWidget;
  }

  protected void addCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners == null) {
      collapsedListeners = new CollapsedListenerCollection();
    }
    collapsedListeners.add(listener);
  }

  protected void removeCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners != null) {
      collapsedListeners.remove(listener);
    }
  }

  protected void fireCollapsedChange(Widget sender) {
    if (collapsedListeners != null) {
      collapsedListeners.fireCollapsedChange(sender);
    }
  }
}
