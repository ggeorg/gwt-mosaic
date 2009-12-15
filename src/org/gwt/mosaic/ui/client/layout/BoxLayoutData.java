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

/**
 * Layout data object for {@link BoxLayout}. Each widget controlled by a
 * {@link BoxLayout} can have its initial width and height specified by an
 * instance of this layout data object.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see BoxLayout
 */
public final class BoxLayoutData extends LayoutData {

  public enum FillStyle {
    BOTH, HORIZONTAL, VERTICAL
  }

  boolean fillWidth;
  boolean fillHeight;

  int calcWidth;
  int calcHeight;

  /**
   * Creates a new instance of {@code BoxLayoutData}. The associated widget
   * should be undecorated. The width and height of the widget should be the
   * calculated preferred width and height returned by
   * {@link BaseLayout#getFlowWidth(Widget)} and
   * {@link BaseLayout#getFlowHeight(Widget)}.
   */
  public BoxLayoutData() {
    super(false);
  }

  /**
   * Creates a new instance of {@code BoxLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The width and height of the
   * widget should be the calculated preferred width and height returned by
   * {@link BaseLayout#getFlowWidth(Widget)} and
   * {@link BaseLayout#getFlowHeight(Widget)}.
   * 
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  public BoxLayoutData(final boolean decorate) {
    super(decorate);
  }

  /**
   * Creates a new instance of {@code BoxLayoutData}. The associated widget
   * should be undecorated. The width and height of the widget is specified by
   * the {@code width} and {@code height} parameters. Values > 0 and <= 1 are in
   * ratios of the available client area except paddings, 0 and values > 1 are
   * in pixels, and -1 means the calculated preferred size.
   * 
   * @param width the preferred width or -1 indicating that the widget's
   *          calculated preferred width should be used instead of this value.
   * @param height the preferred height or -1 indicating that the widget's
   *          calculated preferred height should be used instead of this value.
   */
  @Deprecated
  public BoxLayoutData(final double width, final double height) {
    this(width, height, false);
  }

  public BoxLayoutData(final String width, final String height) {
    this(width, height, false);
  }

  /**
   * Creates a new instance of {@code BoxLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The width and height of the
   * widget is specified by the {@code width} and {@code height} parameters.
   * Values > 0 and <= 1 are in ratios of the available client area except
   * paddings, 0 and values > 1 are in pixels, and -1 means preferred size.
   * 
   * @param width the preferred width or -1 indicating that the widget's
   *          calculated preferred width should be used instead of this value.
   * @param height the preferred height or -1 indicating that the widget's
   *          calculated preferred height should be used instead of this value.
   * @param decorate specifies whether the associated widget will be decorated
   *          or not.
   */
  @Deprecated
  public BoxLayoutData(final double width, final double height,
      final boolean decorate) {
    super(decorate);

    if (width > 1.0) {
      setPreferredWidth(((int) width) + "px");
    } else if (width > 0.0) {
      setPreferredWidth(((int) (width * 100.0)) + "%");
    } else {
      setPreferredWidth(null);
    }

    if (height > 1.0) {
      setPreferredHeight(((int) height) + "px");
    } else if (height > 0.0) {
      setPreferredHeight(((int) (height * 100.0)) + "%");
    } else {
      setPreferredHeight(null);
    }
  }

  public BoxLayoutData(final String width, final String height,
      final boolean decorate) {
    super(decorate);
    setPreferredSize(width, height);
  }

  /**
   * Creates a new instance of {@code BoxLayoutData}. The associated widget
   * should be undecorated. The width and height of the widget depends on the
   * {@code fillStyle} parameter:
   * <ul>
   * <li>for {@link FillStyle#BOTH} the child widget will be stretched to fill
   * the available space in both directions</li>
   * <li>for {@link FillStyle#HORIZONTAL} the child widget will be stretched
   * only horizontally, while in the vertical direction the widget's height
   * should be the calculated preferred height returned by
   * {@link BaseLayout#getFlowHeight(Widget)}</li>
   * <li>for {@link FillStyle#VERTICAL} the child widget will be stretched only
   * vertically, while in the horizontal direction the widget's width should be
   * the calculated preferred width returned by
   * {@link BaseLayout#getFlowWidth(Widget)}</li>
   * </ul>
   * 
   * @param fillStyle specifies whether the child widget will be stretched to
   *          fill the available space in both directions, or only horizontally
   *          or only vertically.
   */
  public BoxLayoutData(final FillStyle fillStyle) {
    this(fillStyle, false);
  }

  /**
   * Creates a new instance of {@code BoxLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}. The width and height of the
   * widget depends on the {@code fillStyle} parameter:
   * <ul>
   * <li>for {@link FillStyle#BOTH} the child widget will be stretched to fill
   * the available space in both directions</li>
   * <li>for {@link FillStyle#HORIZONTAL} the child widget will be stretched
   * only horizontally, while in the vertical direction the widget's height
   * should be the calculated preferred height returned by
   * {@link BaseLayout#getFlowHeight(Widget)}</li>
   * <li>for {@link FillStyle#VERTICAL} the child widget will be stretched only
   * vertically, while in the horizontal direction the widget's width should be
   * the calculated preferred width returned by
   * {@link BaseLayout#getFlowWidth(Widget)}</li>
   * </ul>
   * 
   * @param fillStyle specifies whether the child widget will be stretched to
   *          fill the available space in both directions, or only horizontally
   *          or only vertically.
   * @param decorate decorate specifies whether the associated widget will be
   *          decorated or not.
   */
  public BoxLayoutData(final FillStyle fillStyle, final boolean decorate) {
    super(decorate);
    if (fillStyle == FillStyle.BOTH) {
      fillWidth = true;
      fillHeight = true;
    } else if (fillStyle == FillStyle.HORIZONTAL) {
      fillWidth = true;
    } else if (fillStyle == FillStyle.VERTICAL) {
      fillHeight = true;
    }
  }

  /**
   * Returns {@code true} the child widget will be stretched vertically, {@code
   * false} otherwise.
   * 
   * @return {@code true} the child widget will be stretched vertically, {@code
   *         false} otherwise.
   */
  public boolean isFillHeight() {
    return fillHeight;
  }

  /**
   * Sets whether the child widget will be stretched vertically or not.
   * 
   * @param fillHeight {@code true} if the child widget should be stretched
   *          vertically or {@code false} otherwise.
   */
  public void setFillHeight(final boolean fillHeight) {
    this.fillHeight = fillHeight;
  }

  /**
   * Returns {@code true} the child widget will be stretched horizontally,
   * {@code false} otherwise.
   * 
   * @return {@code true} the child widget will be stretched horizontally,
   *         {@code false} otherwise.
   */
  public boolean isFillWidth() {
    return fillWidth;
  }

  /**
   * Sets whether the child widget will be stretched horizontally or not.
   * 
   * @param fillWidth {@code true} if the child widget should be stretched
   *          horizontally or {@code false} otherwise.
   */
  public void setFillWidth(final boolean fillWidth) {
    this.fillWidth = fillWidth;
  }

}
