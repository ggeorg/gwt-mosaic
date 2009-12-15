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

/**
 * An interface that describes how to convert general sizes to pixel sizes. For
 * example, <i>dialog units</i> require a conversion that honors the font and
 * resolution.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see AbstractUnitConverter
 * @see DefaultUnitConverter
 */
public interface UnitConverter {

  /**
   * Converts Inches and returns pixels.
   * 
   * @param in the Inches
   * @return the given Inches as pixels
   */
  int inchAsPixel(double in);

  /**
   * Converts Millimeters and returns pixels.
   * 
   * @param mm Millimeters
   * @return the given Millimeters as pixels
   */
  int millimeterAsPixel(double mm);

  /**
   * Converts Centimeters and returns pixels.
   * 
   * @param cm Centimeters
   * @return the given Centimeters as pixels
   */
  int centimeterAsPixel(double cm);

  /**
   * Converts DTP Points and returns pixels.
   * 
   * @param pt DTP Points
   * @return the given Points as pixels
   */
  int pointAsPixel(int pt);

  /**
   * Converts horizontal dialog units and returns pixels. Honors the resolution,
   * dialog font size, platform and look&amp;feel.
   * 
   * @param dluX the horizontal dialog units
   * @return the given horizontal dialog units as pixels
   */
  int dialogUnitXAsPixel(int dluX);

  /**
   * Converts vertical dialog units and returns pixels. Honors the resolution,
   * dialog font size, platform and look&amp;feel.
   * 
   * @param dluY the vertical dialog units
   * @return the given vertical dialog units as pixels
   */
  int dialogUnitYAsPixel(int dluY);
  
  // Additional CSS only units *********************************************
  
  /**
   * Converts font size and returns pixels.
   * 
   * @param em the font size
   * @return the given font size as pixels
   */
  int fontSizeAsPixel(double em);
  
  /**
   * Converts the x-height and returns pixels.
   * 
   * @param ex the x-height
   * @return the given x-height as pixels
   */
  int xHeightAsPixel(double ex);
  
  /**
   * Converts the DTP PICA and returns pixels (1pc is the same as 12points).
   * 
   * @param pc the DTP PICA
   * @return the given DTP PICA as pixels
   */
  int picaAsPixel(double pc);

}
