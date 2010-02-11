/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.core.client.util;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.FontMetrics;

/**
 * This is the default implementation of the {@link UnitConverter} interface. It
 * converts horizontal and vertical dialog base units to pixels.
 * <p>
 * The horizontal base unit is equal to the average width, in pixels, of the
 * characters in the system font; the vertical base uint is equal to the height,
 * in pixels, of the font. Each horizontal base unit is equal to 4 horizontal
 * dialog units; each vertical base unit is equal to 8 vertical dialog units.
 * <p>
 * TODO
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see UnitConverter
 */
public class DefaultUnitConverter extends AbstractUnitConverter implements
    UnitConverter {

  /**
   * Holds the sole instance that will be lazily instantiated.
   */
  private static DefaultUnitConverter instance;

  /**
   * Holds the string that is used to compute the average character width. By
   * default this is just &quot;X&quot;.
   */
  private String averageCharWidthTestString = "X";

  // Cached *****************************************************************

  /**
   * Holds the lazily created cached global dialog base units that are used if a
   * component is not (yet) available - for example in a Border.
   */
  private DialogBaseUnits cachedGlobalDialogBaseUnits = null;

  /**
   * Holds lazily calculated font size value.
   */
  private int cachedFontSize = -1;

  /**
   * Holds lazily calculated x-height value.
   */
  private int cachedXHeight = -1;

  // Instance Creation and Access *******************************************

  /**
   * Constructs a DefaultUnitConverter and registers a listener that handles
   * changes in the look&amp;feel.
   */
  private DefaultUnitConverter() {
    // Nothing to do here!
  }

  /**
   * Lazily instantiates and returns the sole instance.
   * 
   * @return the lazily instantiated sole instance
   */
  public static DefaultUnitConverter getInstance() {
    if (instance == null) {
      instance = new DefaultUnitConverter();
    }
    return instance;
  }

  // Access to Bound Properties *********************************************

  /**
   * Returns the string used to compute the average character width. By default
   * it is initialized to &quot;X&quot;.
   * 
   * @return the test string used to compute the average character width
   */
  public String getAverageCharacterWidthTestString() {
    return averageCharWidthTestString;
  }

  /**
   * Sets a string that will be used to compute the average character width. By
   * default it is initialized to &quot;X&quot;. You can provide other test
   * strings, for example:
   * <ul>
   * <li>&quot;Xximeee&quot;</li>
   * <li>&quot;ABCEDEFHIJKLMNOPQRSTUVWXYZ&quot;</li>
   * <li>&quot;abcdefghijklmnopqrstuvwxyz&quot;</li>
   * </ul>
   * 
   * @param newTestString the test string to be used
   * @throws IllegalArgumentException if the test string is empty
   * @throws NullPointerException if the test string is <code>null</code>
   */
  public void setAverageCharacterWidthTestString(String newTestString) {
    if (newTestString == null)
      throw new NullPointerException("The test string must not be null.");
    if (newTestString.length() == 0)
      throw new IllegalArgumentException("The test string must not be empty.");

    averageCharWidthTestString = newTestString;
  }

  /**
   * Sets the font size that will be used to compute CSS 'em' units.
   * 
   * @param fontSize the font size to set
   */
  public void setFontSize(int fontSize) {
    this.cachedFontSize = fontSize;
  }

  /**
   * Sets the x-height that will be used to compute CSS 'ex' units.
   * 
   * @param xHeight the x-height to set
   */
  public void setXHeight(int xHeight) {
    this.cachedXHeight = xHeight;
  }

  // Implementing Abstract Superclass Behavior ******************************

  /**
   * Returns the cached or computed horizontal dialog base units.
   * 
   * @return the horizontal dialog base units
   */
  @Override
  protected double getDialogBaseUnitsX() {
    return getDialogBaseUnits().x;
  }

  /**
   * Returns the cached or computed vertical dialog base units for the given
   * component.
   * 
   * @param component a Component that provides the font and graphics
   * @return the vertical dialog base units
   */
  @Override
  protected double getDialogBaseUnitsY() {
    return getDialogBaseUnits().y;
  }

  @Override
  protected int getFontSize() {
    if (cachedFontSize == -1) {
      cachedFontSize = DOM.toPixelSize("1em", true);
    }
    return cachedFontSize;
  }

  @Override
  protected int getXHeight() {
    if (cachedXHeight == -1) {
      cachedXHeight = DOM.toPixelSize("1ex", true);
    }
    return cachedFontSize;
  }

  // Compute and Cache Global and Components Dialog Base Units **************

  /**
   * Lazily computes and answer the global dialog base units.
   * 
   * @return a cached DialogBaseUnits object used globally
   */
  private DialogBaseUnits getDialogBaseUnits() {
    if (cachedGlobalDialogBaseUnits == null) {
      cachedGlobalDialogBaseUnits = computeGlobalDialogBaseUnits();
    }
    return cachedGlobalDialogBaseUnits;
  }

  /**
   * Computes the global dialog base units.
   * 
   * @return a DialogBaseUnits object used globally
   */
  private DialogBaseUnits computeGlobalDialogBaseUnits() {
    final FontMetrics metrics = new FontMetrics();
    DOM.setStyleAttribute(metrics.getElement(), "whiteSpace", "nowrap");
    final Dimension boxSize = metrics.stringBoxSize(averageCharWidthTestString);
    return new DialogBaseUnits((double) boxSize.width
        / averageCharWidthTestString.length(), boxSize.height);
  }

  // Helper Code ************************************************************

  /**
   * Describes horizontal and vertical dialog base units.
   */
  private static final class DialogBaseUnits {

    final double x;
    final double y;

    DialogBaseUnits(double dialogBaseUnitsX, double dialogBaseUnitsY) {
      this.x = dialogBaseUnitsX;
      this.y = dialogBaseUnitsY;
    }

    public String toString() {
      return "DBU(x=" + x + "; y=" + y + ")";
    }
  }

}
