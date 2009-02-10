/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.Dimension;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface HasLayoutManager {
  /**
   * If the receiver has a {@link LayoutManager}, asks the layout to
   * <em>lay out</em> (that is, set the size and location of) the receiver's
   * children. If the receiver does not have a layout, do nothing.
   * <p>
   * This method should be invoked when this receiver's children are modified
   * (added or removed from the receiver, or layout-related information changed)
   * after the receiver has been displayed.
   * 
   * @see #layout(boolean)
   */
  void layout();
  
  /**
   * 
   * @param invalidate
   */
  void layout(boolean invalidate);
  
  /**
   * 
   */
  void invalidate(boolean layout);

  /**
   * Returns the preferred size of the receiver.
   * <p>
   * The <em>preferred size</em> of a receiver is the size that it would be best
   * be displayed at.
   * <p>
   * Note: some implementations may cache the value returned from the
   * {@link LayoutManager}. Implementations that cache need not invoke
   * {@link LayoutManager#getPreferredSize(LayoutPanel)} every time this method
   * is invoked, rather the {@link LayoutManager} will only be queried after
   * receiver becomes invalid.
   * 
   * @return an instance of {@link Dimension} that represents the preferred size
   *         of this receiver.
   */
  int[] getPreferredSize(); // TODO Dimension getPreferredSize();
}
