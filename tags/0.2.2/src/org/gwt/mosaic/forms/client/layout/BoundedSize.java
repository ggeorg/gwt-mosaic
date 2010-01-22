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

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * Describes sizes that provide lower and upper bounds as used by the JGoodies
 * FormLayout.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see Sizes
 * @see ConstantSize
 */
public final class BoundedSize implements Size, Serializable {
  private static final long serialVersionUID = 2071470989298219314L;

  /**
   * Holds the base size.
   */
  private final Size basis;

  /**
   * Holds an optional lower bound.
   */
  private final Size lowerBound;

  /**
   * Holds an optional upper bound.
   */
  private final Size upperBound;

  // Instance Creation ****************************************************

  /**
   * Constructs a BoundedSize for the given basis using the specified lower and
   * upper bounds.
   * 
   * @param basis the base size
   * @param lowerBound the lower bound size
   * @param upperBound the upper bound size
   * 
   * @throws NullPointerException if the basis is {@code null}
   * 
   * @throws IllegalArgumentException of {@code lowerBound} and {@code
   *           upperBound} is {@code null}
   * 
   * @since 1.1
   */
  public BoundedSize(Size basis, Size lowerBound, Size upperBound) {
    if (basis == null)
      throw new NullPointerException(
          "The basis of a bounded size must not be null.");
    if ((lowerBound == null) && (upperBound == null))
      throw new IllegalArgumentException(
          "A bounded size must have a non-null lower or upper bound.");
    this.basis = basis;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  // Accessors ************************************************************

  /**
   * Returns the base size, which is not-<code>null</code>.
   * 
   * @return the base size
   * 
   * @since 1.1
   */
  public Size getBasis() {
    return basis;
  }

  /**
   * Returns the optional lower bound.
   * 
   * @return the optional lower bound
   * 
   * @since 1.1
   */
  public Size getLowerBound() {
    return lowerBound;
  }

  /**
   * Returns the optional upper bound.
   * 
   * @return the optional upper bound
   * 
   * @since 1.1
   */
  public Size getUpperBound() {
    return upperBound;
  }

  // Implementation of the Size Interface *********************************

  /**
   * Returns this size as pixel size. Neither requires the component list nor
   * the specified measures. Honors the lower and upper bound.
   * <p>
   * 
   * Invoked by <code>FormSpec</code> to determine the size of a column or row.
   * 
   * @param layoutPanel the layout container
   * @param widgets the list of components to measure
   * @param minMeasure the measure used to determine the minimum size
   * @param prefMeasure the measure used to determine the preferred size
   * @param defaultMeasure the measure used to determine the default size
   * @return the maximum size in pixels
   * @see FormSpec#maximumSize(Container, List, FormLayout.Measure,
   *      FormLayout.Measure, FormLayout.Measure)
   */
  public int maximumSize(LayoutPanel layoutPanel, List<Widget> widgets,
      FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure,
      FormLayout.Measure defaultMeasure) {
    int size = basis.maximumSize(layoutPanel, widgets, minMeasure,
        prefMeasure, defaultMeasure);
    if (lowerBound != null) {
      size = Math.max(size, lowerBound.maximumSize(layoutPanel, widgets,
          minMeasure, prefMeasure, defaultMeasure));
    }
    if (upperBound != null) {
      size = Math.min(size, upperBound.maximumSize(layoutPanel, widgets,
          minMeasure, prefMeasure, defaultMeasure));
    }
    return size;
  }

  /**
   * Describes if this Size can be compressed, if container space gets scarce.
   * Used by the FormLayout size computations in <code>#compressedSizes</code>
   * to check whether a column or row can be compressed or not.
   * <p>
   * 
   * BoundedSizes are compressible if the base Size is compressible.
   * 
   * @return <code>true</code> if and only if the basis is compressible
   * 
   * @since 1.1
   */
  public boolean compressible() {
    return getBasis().compressible();
  }

  // Overriding Object Behavior *******************************************

  /**
   * Indicates whether some other BoundedSize is "equal to" this one.
   * 
   * @param object the object with which to compare
   * @return <code>true</code> if this object is the same as the object
   *         argument, <code>false</code> otherwise.
   * @see Object#hashCode()
   * @see java.util.Hashtable
   */
  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (!(object instanceof BoundedSize))
      return false;
    BoundedSize size = (BoundedSize) object;
    return basis.equals(size.basis)
        && ((lowerBound == null && size.lowerBound == null) || (lowerBound != null && lowerBound.equals(size.lowerBound)))
        && ((upperBound == null && size.upperBound == null) || (upperBound != null && upperBound.equals(size.upperBound)));
  }

  /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * @see Object#equals(Object)
   * @see java.util.Hashtable
   */
  public int hashCode() {
    int hashValue = basis.hashCode();
    if (lowerBound != null) {
      hashValue = hashValue * 37 + lowerBound.hashCode();
    }
    if (upperBound != null) {
      hashValue = hashValue * 37 + upperBound.hashCode();
    }
    return hashValue;
  }

  /**
   * Returns a string representation of this size object.
   * <p>
   * 
   * <strong>Note:</strong> This string representation may change at any time.
   * It is intended for debugging purposes. For parsing, use {@link #encode()}
   * instead.
   * 
   * @return a string representation of this bounded size
   */
  public String toString() {
    return encode();
  }

  /**
   * Returns a parseable string representation of this bounded size.
   * 
   * @return a String that can be parsed by the Forms parser
   * 
   * @since 1.2
   */
  public String encode() {
    StringBuffer buffer = new StringBuffer("[");
    if (lowerBound != null) {
      buffer.append(lowerBound.encode());
      buffer.append(',');
    }
    buffer.append(basis.encode());
    if (upperBound != null) {
      buffer.append(',');
      buffer.append(upperBound.encode());
    }
    buffer.append(']');
    return buffer.toString();
  }

}