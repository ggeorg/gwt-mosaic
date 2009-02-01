/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Copyright (c) 2009 GWT Mosaic Georgopoulos J. Georgios. All Rights Reserved.
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

/**
 * An abstract implementation of the {@link UnitConverter} interface that
 * minimizes the effort to convert font-dependent sizes to pixels.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see DefaultUnitConverter
 * @see com.jgoodies.forms.layout.Size
 * @see com.jgoodies.forms.layout.Sizes
 */
public abstract class AbstractUnitConverter implements UnitConverter {

  /**
   * Converts Inches and returns pixels.
   * 
   * @param in the Inches
   * @return the given Inches as pixels
   */
  public int inchAsPixel(final double in) {
    return inchAsPixel(in, getDPI());
  }

  /**
   * Converts Millimeters and returns pixels.
   * 
   * @param mm Millimeters
   * @return the given Millimeters as pixels
   */
  public int millimeterAsPixel(double mm) {
    return millimeterAsPixel(mm, getDPMM());
  }

  /**
   * Converts Centimeters and returns pixels.
   * 
   * @param cm Centimeters
   * @return the given Centimeters as pixels
   */
  public int centimeterAsPixel(double cm) {
    return centimeterAsPixel(cm, getDPCM());
  }

  /**
   * Converts DTP Points and returns pixels.
   * 
   * @param pt DTP Points
   * @return the given Points as pixels
   */
  public int pointAsPixel(int pt) {
    return pointAsPixel(pt, getDPPT());
  }

  /**
   * Converts horizontal dialog units and returns pixels.
   * 
   * @param dluX the horizontal dialog units return the given horizontal dialog
   *          units per pixels
   */
  public int dialogUnitXAsPixel(int dluX) {
    return dialogUnitXAsPixel(dluX, getDialogBaseUnitsX() / 4);
  }

  /**
   * Converts vertical dialog units and returns pixels.
   * 
   * @param dluY the vertical dialog units
   * @return the given vertical dialog units as pixels
   */
  public int dialogUnitYAsPixel(int dluY) {
    return dialogUnitYAsPixel(dluY, getDialogBaseUnitsY() / 8);
  }

  // Abstract Behavior *****************************************************

  /**
   * Gets and returns the horizontal dialog base units. Implementations are
   * encouraged to cache previously computed dialog base units.
   * 
   * @return the horizontal dialog base units
   */
  protected abstract double getDialogBaseUnitsX();

  /**
   * Gets and returns the vertical dialog base units. Implementations are
   * encouraged to cache previously computed dialog base units.
   * 
   * @return the vertical dialog base units
   */
  protected abstract double getDialogBaseUnitsY();

  // Convenience Methods ***************************************************

  /**
   * Converts Inches and returns pixels.
   * 
   * @param in the Inches
   * @param dpi the resolution
   * @return the given Inches as pixels
   */
  protected final int inchAsPixel(double in, int dpi) {
    return (int) Math.round(dpi * in);
  }

  /**
   * Converts Millimeters and returns pixels.
   * 
   * @param mm Millimeters
   * @param dpmm the resolution
   * @return the given Millimeters as pixels
   */
  protected final int millimeterAsPixel(double mm, int dpmm) {
    return (int) Math.round(dpmm * mm);
  }

  /**
   * Converts Centimeters and returns pixels.
   * 
   * @param cm Centimeters
   * @param dpcm the resolution
   * @return the given Centimeters as pixels
   */
  protected final int centimeterAsPixel(double cm, int dpcm) {
    return (int) Math.round(dpcm * cm);
  }

  /**
   * Converts DTP Points and returns pixels.
   * 
   * @param pt DTP Points
   * @param dppt the resolution in dpi
   * @return the given Points as pixels
   */
  protected final int pointAsPixel(int pt, int dppt) {
    return Math.round(dppt * pt);
  }

  /**
   * Converts horizontal dialog units and returns pixels.
   * 
   * @param dluX the horizontal dialog units
   * @param dialogBaseUnitsX the horizontal dialog base units
   * @return the given dialog base units as pixels
   */
  protected int dialogUnitXAsPixel(int dluX, double dialogBaseUnitsX) {
    return (int) Math.round(dluX * dialogBaseUnitsX);
  }

  /**
   * Converts vertical dialog units and returns pixels.
   * 
   * @param dluY the vertical dialog units
   * @param dialogBaseUnitsY the vertical dialog base units
   * @return the given dialog base units as pixels
   */
  protected int dialogUnitYAsPixel(int dluY, double dialogBaseUnitsY) {
    return (int) Math.round(dluY * dialogBaseUnitsY);
  }

  // Helper Code ************************************************************

  private static int dpi = -1;

  /**
   * Returns the screen resolution in dots-per-inch.
   * 
   * @return the screen resolution, in dots-per-inch
   */
  private int getDPI() {
    if (dpi == -1) {
      dpi = DOM.getScreenResolution();
    }
    return dpi;
  }

  private static int dpmm = -1;

  /**
   * Returns the screen resolution in dots-per-millimeter.
   * 
   * @return the screen resolution, in dots-per-millimeter
   */
  private static int getDPMM() {
    if (dpmm == -1) {
      dpmm = DOM.toPixelSize("1mm");
    }
    return dpmm;
  }

  private static int dpcm = -1;

  /**
   * Returns the screen resolution in dots-per-centimeter.
   * 
   * @return the screen resolution, in dots-per-centimeter
   */
  private static int getDPCM() {
    if (dpcm == -1) {
      dpcm = DOM.toPixelSize("1cm");
    }
    return dpcm;
  }

  private static int dppt = -1;

  /**
   * Returns the screen resolution in dots-per-point.
   * 
   * @return the screen resolution, in dots-per-point
   */
  private static int getDPPT() {
    if (dppt == -1) {
      dppt = DOM.toPixelSize("1pt");
    }
    return dppt;
  }
}
