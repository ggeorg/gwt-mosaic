/*
 * Copyright (c) 2008-2010 GWT Mosaic Johan Rydberg.
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
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.DimensionPolicy;
import org.gwt.mosaic.ui.client.layout.AbsoluteLayout.MarginPolicy;

import com.google.gwt.dom.client.Style.Unit;

/**
 * 
 * @see AbsoluteLayout
 * 
 * @author johan.rydberg(at)gmail.com
 */
public final class AbsoluteLayoutData extends LayoutData {

  MarginPolicy marginPolicy;
  DimensionPolicy dimensionPolicy;

  ParsedSize left, top;

  public AbsoluteLayoutData() {
    this("0px", "0px", null, null, MarginPolicy.NONE, DimensionPolicy.NONE);
  }
  
  public AbsoluteLayoutData(boolean decorated) {
    this("0px", "0px", null, null, MarginPolicy.NONE, DimensionPolicy.NONE, decorated);
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, MarginPolicy margin, DimensionPolicy dimension) {
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

    setLeft(posLeft + "px");
    setTop(posTop + "px");

    this.marginPolicy = margin;
    this.dimensionPolicy = dimension;
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, MarginPolicy margin) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin,
        DimensionPolicy.NONE);
  }

  @Deprecated
  public AbsoluteLayoutData(int posLeft, int posTop, int preferredWidth,
      int preferredHeight, DimensionPolicy dimension) {
    this(posLeft, posTop, preferredWidth, preferredHeight, MarginPolicy.NONE,
        dimension);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, MarginPolicy margin,
      DimensionPolicy dimension) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin, dimension,
        false);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, MarginPolicy margin) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin,
        DimensionPolicy.NONE);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, MarginPolicy margin,
      boolean decorate) {
    this(posLeft, posTop, preferredWidth, preferredHeight, margin,
        DimensionPolicy.NONE, decorate);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, DimensionPolicy dimension) {
    this(posLeft, posTop, preferredWidth, preferredHeight, MarginPolicy.NONE,
        dimension);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, DimensionPolicy dimension,
      boolean decorate) {
    this(posLeft, posTop, preferredWidth, preferredHeight, MarginPolicy.NONE,
        dimension, decorate);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight) {
    this(posLeft, posTop, preferredWidth, preferredHeight, MarginPolicy.NONE,
        DimensionPolicy.NONE);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, boolean decorate) {
    this(posLeft, posTop, preferredWidth, preferredHeight, MarginPolicy.NONE,
        DimensionPolicy.NONE, decorate);
  }

  public AbsoluteLayoutData(String posLeft, String posTop,
      String preferredWidth, String preferredHeight, MarginPolicy margin,
      DimensionPolicy dimension, boolean decorate) {
    super(decorate);

    setPreferredWidth(preferredWidth);
    setPreferredHeight(preferredHeight);

    setLeft(posLeft);
    setTop(posTop);

    this.marginPolicy = margin;
    this.dimensionPolicy = dimension;
  }

  /**
   * @return the marginPolicy
   */
  public MarginPolicy getMarginPolicy() {
    return marginPolicy;
  }

  /**
   * @param marginPolicy the marginPolicy to set
   */
  public void setMarginPolicy(MarginPolicy marginPolicy) {
    this.marginPolicy = marginPolicy;
  }

  /**
   * Used by UiBinder.
   * 
   * @param margin the margin policy to set (accepted values are a string
   *          containing the key words: {@code left}, {@code top}, {@code right}
   *          and {@code bottom})
   */
  public void setMargin(String margin) {
    boolean left = false, top = false, right = false, bottom = false;
    margin = margin.trim().toLowerCase();
    String[] margins = margin.split(" ");
    for (String _margin : margins) {
      left = (!left) ? _margin.equals("left") : true;
      top = (!top) ? _margin.equals("top") : true;
      right = (!right) ? _margin.equals("right") : true;
      bottom = (!bottom) ? _margin.equals("bottom") : true;
    }
    if (!left && !top && !right && !bottom) {
      setMarginPolicy(MarginPolicy.NONE);
    } else if (!left && !top && !right && bottom) {
      setMarginPolicy(MarginPolicy.BOTTOM);
    } else if (!left && !top && right && !bottom) {
      setMarginPolicy(MarginPolicy.RIGHT);
    } else if (!left && !top && right && bottom) {
      setMarginPolicy(MarginPolicy.RIGHT_BOTTOM);
    } else if (!left && top && !right && !bottom) {
      setMarginPolicy(MarginPolicy.TOP);
    } else if (!left && top && !right && bottom) {
      setMarginPolicy(MarginPolicy.TOP_BOTTOM);
    } else if (!left && top && right && !bottom) {
      setMarginPolicy(MarginPolicy.RIGHT_TOP);
    } else if (!left && top && right && bottom) {
      setMarginPolicy(MarginPolicy.RIGHT_TOP_BOTTOM);
    } else if (left && !top && !right && !bottom) {
      setMarginPolicy(MarginPolicy.LEFT);
    } else if (left && !top && !right && bottom) {
      setMarginPolicy(MarginPolicy.LEFT_BOTTOM);
    } else if (left && !top && right && !bottom) {
      setMarginPolicy(MarginPolicy.LEFT_RIGHT);
    } else if (left && !top && right && bottom) {
      setMarginPolicy(MarginPolicy.LEFT_RIGHT_BOTTOM);
    } else if (left && top && !right && !bottom) {
      setMarginPolicy(MarginPolicy.LEFT_TOP);
    } else if (left && top && !right && bottom) {
      setMarginPolicy(MarginPolicy.LEFT_TOP_BOTTOM);
    } else if (left && top && right && !bottom) {
      setMarginPolicy(MarginPolicy.LEFT_TOP_RIGHT);
    } else if (left && top && right && bottom) {
      setMarginPolicy(MarginPolicy.ALL);
    }
  }

  /**
   * @return the dimensionPolicy
   */
  public DimensionPolicy getDimensionPolicy() {
    return dimensionPolicy;
  }

  /**
   * @param dimensionPolicy the dimensionPolicy to set
   */
  public void setDimensionPolicy(DimensionPolicy dimensionPolicy) {
    this.dimensionPolicy = dimensionPolicy;
  }

  /**
   * Used by UiBinder.
   * 
   * @param direction the direction to set (accepted values are a string
   *          containing the key words: {@code width} and {@code height})
   */
  public void setStretch(String direction) {
    boolean width = false, height = false;
    direction = direction.trim().toLowerCase();
    String[] directions = direction.split(" ");
    for (String _direction : directions) {
      width = (!width) ? _direction.equals("width") : true;
      height = (!height) ? _direction.equals("height") : true;
    }
    if (!width && !height) {
      setDimensionPolicy(DimensionPolicy.NONE);
    } else if (!width && height) {
      setDimensionPolicy(DimensionPolicy.WIDTH);
    } else if (width && !height) {
      setDimensionPolicy(DimensionPolicy.HEIGHT);
    } else if (width && height) {
      setDimensionPolicy(DimensionPolicy.BOTH);
    }
  }

  /**
   * @return the left position
   */
  public ParsedSize getLeft() {
    return left;
  }

  /**
   * @param left the left position to set
   */
  public void setLeft(String left) {
    this.left = new ParsedSize(FloatParser.parseFloat(left, 0.0f),
        UnitParser.parseUnit(left, Unit.PX));
  }

  /**
   * @return the left position
   */
  public String getLeftString() {
    return left.getValue();
  }

  /**
   * @return the top position
   */
  public ParsedSize getTop() {
    return top;
  }

  /**
   * @param top the top position to set
   */
  public void setTop(String top) {
    this.top = new ParsedSize(FloatParser.parseFloat(top, 0.0f),
        UnitParser.parseUnit(top, Unit.PX));
  }

  /**
   * @return the top position
   */
  public String getTopString() {
    return top.getValue();
  }

}
