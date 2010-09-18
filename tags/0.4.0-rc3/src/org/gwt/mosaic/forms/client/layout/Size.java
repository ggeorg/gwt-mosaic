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

import java.util.List;

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Widget;

/**
 * An interface that describes sizes as used by the {@link FormLayout}:
 * widget measuring sizes, constant sizes with value and unit, and bounded
 * sizes that provide lower and upper bounds for a size.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see Sizes
 * @see ConstantSize
 */
public interface Size {

  /**
   * Computes and returns this Size's maximum pixel size applied to the given
   * list of widgets using the specified measures.
   * <p>
   * Invoked by {@link FormSpec} to determine the size of a column or row. This
   * method is not intended to be called by API users, and it uses API invisible
   * parameter types.
   * 
   * @param layoutPanel the layout panel
   * @param widgets the list of components used to compute the size
   * @param minMeasure the measure that determines the minimum sizes
   * @param prefMeasure the measure that determines the preferred sizes
   * @param defaultMeasure the measure that determines the default sizes
   * @return the maximum size in pixels for the given list of components
   */
  int maximumSize(LayoutPanel layoutPanel, List<Widget> widgets,
      FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure,
      FormLayout.Measure defaultMeasure);

  /**
   * Describes if this Size can be compressed, if layout panel space gets scarce.
   * Used by the FormLayout size computations in <code>#compressedSizes</code>
   * to check whether a column or row can be compressed or not.
   * <p>
   * The ComponentSize <em>default</em> is compressible, as well as BoundedSizes
   * that are based on the <em>default</em> size.
   * 
   * @return <code>true</code> for compressible Sizes
   */
  boolean compressible();

  /**
   * Returns a String respresentation of this Size object that can be parsed by
   * the Forms parser.
   * <p>
   * Implementors should return a non-verbose string.
   * 
   * @return a parseable String representation of this object.
   */
  String encode();

}