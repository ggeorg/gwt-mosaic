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

import org.gwt.mosaic.core.client.util.FloatParser;
import org.gwt.mosaic.core.client.util.UnitParser;

import com.google.gwt.dom.client.Style.Unit;

/*
 * 
 * @see AbsoluteLayout
 * 
 * @author johan.rydberg(at)gmail.com
 */
public final class AbsoluteLayoutData extends LayoutData {

  AbsoluteLayout.MarginPolicy marginPolicy;
  AbsoluteLayout.DimensionPolicy dimensionPolicy;

  ParsedSize posLeft, posTop;

  public AbsoluteLayoutData() {
    this("0px", "0px", null, null, AbsoluteLayout.MarginPolicy.NONE,
        AbsoluteLayout.DimensionPolicy.NONE);
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, AbsoluteLayout.MarginPolicy margin,
      AbsoluteLayout.DimensionPolicy dimension) {
    super(false);

    if (preferredWidth < 0) {
      setPreferredWidth(null);
    } else {
      setPreferredWidth(preferredWidth + "px");
    }

    if (preferredHeight < 0) {
      setPreferredHeight(null);
    } else {
      setPreferredHeight(preferredHeight + "px");
    }

    setPosLeft(posLeft + "px");
    setPosTop(posTop + "px");

    this.marginPolicy = margin;
    this.dimensionPolicy = dimension;
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, AbsoluteLayout.MarginPolicy margin) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin,
        AbsoluteLayout.DimensionPolicy.NONE);
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, AbsoluteLayout.DimensionPolicy dimension) {
    this(posLeft, posTop, preferredWidth, preferredHeight,
        AbsoluteLayout.MarginPolicy.NONE, dimension);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight,
      AbsoluteLayout.MarginPolicy margin,
      AbsoluteLayout.DimensionPolicy dimension) {
    super(false);

    setPreferredWidth(preferredWidth);
    setPreferredHeight(preferredHeight);

    setPosLeft(posLeft);
    setPosTop(posTop);

    this.marginPolicy = margin;
    this.dimensionPolicy = dimension;
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight,
      AbsoluteLayout.MarginPolicy margin) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin,
        AbsoluteLayout.DimensionPolicy.NONE);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight,
      AbsoluteLayout.DimensionPolicy dimension) {
    this(posLeft, posTop, preferredWidth, preferredHeight,
        AbsoluteLayout.MarginPolicy.NONE, dimension);
  }

  /**
   * @return the marginPolicy
   */
  public AbsoluteLayout.MarginPolicy getMarginPolicy() {
    return marginPolicy;
  }

  /**
   * @param marginPolicy the marginPolicy to set
   */
  public void setMarginPolicy(AbsoluteLayout.MarginPolicy marginPolicy) {
    this.marginPolicy = marginPolicy;
  }

  /**
   * @return the dimensionPolicy
   */
  public AbsoluteLayout.DimensionPolicy getDimensionPolicy() {
    return dimensionPolicy;
  }

  /**
   * @param dimensionPolicy the dimensionPolicy to set
   */
  public void setDimensionPolicy(AbsoluteLayout.DimensionPolicy dimensionPolicy) {
    this.dimensionPolicy = dimensionPolicy;
  }

  /**
   * @return the posLeft
   */
  public ParsedSize getPosLeft() {
    return posLeft;
  }

  /**
   * @param posLeft the posLeft to set
   */
  public void setPosLeft(String posLeft) {
    this.posLeft = new ParsedSize(FloatParser.parseFloat(posLeft, 0.0f),
        UnitParser.parseUnit(posLeft, Unit.PX));
  }

  /**
   * @return the posLeft
   */
  public String getPosLeftString() {
    return posLeft.getValue();
  }

  /**
   * @return the posTop
   */
  public ParsedSize getPosTop() {
    return posTop;
  }

  /**
   * @param posTop the posTop to set
   */
  public void setPosTop(String posTop) {
    this.posTop = new ParsedSize(FloatParser.parseFloat(posTop, 0.0f),
        UnitParser.parseUnit(posTop, Unit.PX));
  }

  /**
   * @return the posTop
   */
  public String getPosTopString() {
    return posTop.getValue();
  }

}
