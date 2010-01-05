/*
 * Copyright (c) 2008-2009 GWT Mosaic Johan Rydberg.
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

/*
 *
 * @see AbsoluteLayout
 * @author johan.rydberg(at)gmail.com
 */
public final class AbsoluteLayoutData extends LayoutData {

  AbsoluteLayout.MarginPolicy marginPolicy;
  AbsoluteLayout.DimensionPolicy dimensionPolicy;

  int posLeft, posTop;

  int widgetWidth;
  int widgetHeight;

  public AbsoluteLayoutData(int posLeft, int posTop, int widgetWidth, 
      int widgetHeight, AbsoluteLayout.MarginPolicy margin, 
      AbsoluteLayout.DimensionPolicy dimension) {
    super(false);
    this.posLeft = posLeft;
    this.posTop = posTop;
    this.widgetWidth = widgetWidth;
    this.widgetHeight = widgetHeight;
    this.marginPolicy = margin;
    this.dimensionPolicy = dimension;
  }

  public AbsoluteLayoutData(int posLeft, int posTop, int widgetWidth, 
      int widgetHeight) {
    this(posLeft, posTop, widgetWidth, widgetHeight, 
        AbsoluteLayout.MarginPolicy.NONE, AbsoluteLayout.DimensionPolicy.NONE);
  }

  public AbsoluteLayoutData(int posLeft, int posTop) {
    this(posLeft, posTop, -1, -1);
  }

  public AbsoluteLayoutData(int posLeft, int posTop, 
      AbsoluteLayout.MarginPolicy margin, AbsoluteLayout.DimensionPolicy dimension) {
    this(posLeft, posTop, -1, -1, margin, dimension);
  }

  public AbsoluteLayoutData(int posLeft, int posTop, int widgetWidth,
      AbsoluteLayout.MarginPolicy margin, AbsoluteLayout.DimensionPolicy dimension) {
    this(posLeft, posTop, widgetWidth, -1, margin, dimension);
  }

  public AbsoluteLayoutData(int posLeft, int posTop, 
      AbsoluteLayout.DimensionPolicy dimension) {
    this(posLeft, posTop, AbsoluteLayout.MarginPolicy.NONE, dimension);
  }

  public AbsoluteLayoutData(int posLeft, int posTop, AbsoluteLayout.MarginPolicy margin) {
    this(posLeft, posTop, margin, AbsoluteLayout.DimensionPolicy.NONE);
  }

  /**
   * Creates a new instance of {@code AbsoluteLayoutData}. The
   * associated widget should be positioned at {@code posLeft} x
   * {@code posTop}, and the initial widget width is {@code
   * widgetWidth}.  {@code margin} specifies how the margins of the
   * widget should be expanded when the layout panel is resized.
   *
   * <p>
   * This constructor is usefull to position fixed-width buttons in an
   * expandable layout panel.
   *
   * <pre>
   * LayoutPanel layoutPanel = new LayoutPanel(new AbsoluteLayout(200, 100));
   * layoutPanel.add(new Button("Cancel"), new AbsoluteLayoutData(5, 100, 
   *     50, MarginPolicy.RIGHT));
   * layoutPanel.add(new Button("OK"), new AbsoluteLayoutData(145, 100, 
   *     50, MarginPolicy.LEFT));
   * </pre>
   *
   * @param posLeft left position in panel
   * @param posTop top position in panel
   * @param widgetWidget initial width of widget
   * @param margin margin expansion policy
   */
  public AbsoluteLayoutData(int posLeft, int posTop, int widgetWidth,
			    AbsoluteLayout.MarginPolicy margin) {
    this(posLeft, posTop, widgetWidth, -1, margin, 
        AbsoluteLayout.DimensionPolicy.NONE);
  }

  /**
   * Creates a new instance of {@code AbsoluteLayoutData}. The
   * associated widget should be positioned at {@code posLeft} x
   * {@code posTop}, and the initial widget dimensions is {@code
   * widgetWidth} times {@code widgetHeight}.  {@code margin}
   * specifies how the margins of the widget should be expanded when
   * the layout panel is resized.
   *
   * @param posLeft left position in panel
   * @param posTop top position in panel
   * @param widgetWidget initial width of widget
   * @param widgetHeight initial height of widget
   * @param margin margin expansion policy
   */
  public AbsoluteLayoutData(int posLeft, int posTop, int widgetWidth,
			    int widgetHeight, AbsoluteLayout.MarginPolicy margin) {
    this(posLeft, posTop, widgetWidth, widgetHeight, margin, 
        AbsoluteLayout.DimensionPolicy.NONE);
  }

}
