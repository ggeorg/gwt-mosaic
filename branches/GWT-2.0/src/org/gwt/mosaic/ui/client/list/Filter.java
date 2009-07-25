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
package org.gwt.mosaic.ui.client.list;

/**
 * A {@code ModelFilter} is used by a filter model to extract a subset of
 * elements provided by its content provider.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public interface Filter<T1, T2> {

  /**
   * Returns whether the given element makes it through this filter.
   * 
   * @param element the element
   * @param filter the filter element
   * @return {@code true} if the element is included in the filtered set, and
   *         {@code false} if excluded
   */
  boolean select(T1 element, T2 filter);
}
