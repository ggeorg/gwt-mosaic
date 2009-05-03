/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopolos.
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
package org.gwt.mosaic.forms.client.layout;

import java.io.Serializable;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.FontMetrics;
import org.gwt.mosaic.core.client.util.DefaultUnitConverter;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link Size} implementation that computes its width and height by a
 * prototype String.
 * <p>
 * 
 * <strong>Examples:</strong>
 * 
 * <pre>
 * new PrototypeSize("123-456-789");
 * new FormLayout("p, 2dlu, 'MMMM'");
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see Size
 * @see Sizes
 */
public final class PrototypeSize implements Size, Serializable {
  private static final long serialVersionUID = -7711209479604414919L;

  // Fields ***************************************************************

  private final String prototype;

  // Instance Creation ****************************************************

  /**
   * Constructs a PrototypeSize for the given String.
   * 
   * @param prototype the String used to compute the width and height.
   * 
   * @throws NullPointerException if {@code prototype} is {@code null}.
   */
  public PrototypeSize(String prototype) {
    this.prototype = prototype;
  }

  // Accessors ************************************************************

  /**
   * Returns this size's prototype string.
   * 
   * @return the prototype string
   */
  public String getPrototype() {
    return prototype;
  }

  // Implementing the Size Interface **************************************

  /**
   * Computes and returns the width of this Size's prototype in pixel. Ignores
   * the component list and measures. Obtains the FontMetrics from the given
   * layout {@code container} for the default dialog font provided by
   * {@link DefaultUnitConverter#getDefaultDialogFont()}.
   * <p>
   * 
   * Invoked by {@link FormSpec} to determine the size of a column or row.
   * 
   * @param layoutPanel the layout container
   * @param widgets the list of components used to compute the size
   * @param minMeasure the measure that determines the minimum sizes
   * @param prefMeasure the measure that determines the preferred sizes
   * @param defaultMeasure the measure that determines the default sizes
   * 
   * @return the {@code stringWidth} for this size's prototype string computed
   *         by the {@code container}'s FontMetrics for the {@code
   *         DefaultUnitConverter}'s default dialog font
   */
  public int maximumSize(LayoutPanel layoutPanel, List<Widget> widgets,
      FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure,
      FormLayout.Measure defaultMeasure) {
    final FontMetrics metrics = new FontMetrics();
    DOM.setStyleAttribute(metrics.getElement(), "whiteSpace", "nowrap");
    return metrics.stringBoxSize(prototype).width;
  }

  /**
   * Describes if this Size can be compressed, if container space gets scarce.
   * Used by the FormLayout size computations in <code>#compressedSizes</code>
   * to check whether a column or row can be compressed or not.
   * <p>
   * 
   * PrototypeSizes are incompressible.
   * 
   * @return {@code false}
   */
  public boolean compressible() {
    return false;
  }

  /**
   * Returns a parseable string representation of this prototype size.
   * 
   * @return a String that can be parsed by the Forms parser
   */
  public String encode() {
    return "'" + prototype + "'";
  }

  // Overriding Object Behavior *******************************************

  /**
   * Indicates whether some other ConstantSize is "equal to" this one.
   * 
   * @param o the Object with which to compare
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see java.lang.Object#hashCode()
   * @see java.util.Hashtable
   */
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PrototypeSize))
      return false;
    PrototypeSize size = (PrototypeSize) o;
    return prototype.equals(size.prototype);
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by {@code java.util.Hashtable}
   * .
   * 
   * @return a hash code value for this object.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @see java.util.Hashtable
   */
  public int hashCode() {
    return prototype.hashCode();
  }

  /**
   * Returns a string representation of this size object.
   * 
   * <strong>Note:</strong> This string representation may change at any time.
   * It is intended for debugging purposes. For parsing, use {@link #encode()}
   * instead.
   * 
   * @return a string representation of the constant size
   */
  public String toString() {
    return encode();
  }

}
