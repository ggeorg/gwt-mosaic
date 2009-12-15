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

import com.google.gwt.user.client.ui.Widget;

/**
 * Defines the interface for classes that know how to lay out a {@code
 * LayoutPanel}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface LayoutManager {

  /**
   * Computes and returns the size of the {@link LayoutPanel} according to this
   * layout.
   * <p>
   * This method computes the preferred size that the client area of the
   * {@link LayoutPanel} must be in order to position all children at their
   * preferred size inside the panel according to the layout algorithm encoded
   * by this layout.
   * 
   * @param layoutPanel a {@link LayoutPanel} widget using this layout.
   * @return an instance of {@link Dimension} containing the computed size.
   */
  Dimension getPreferredSize(LayoutPanel layoutPanel);

  /**
   * Lays out the specified {@link LayoutPanel} according to this layout.
   * <p>
   * This method positions and sizes the children of a {@link LayoutPanel} using
   * the layout algorithm encoded by this layout. Child widgets of the
   * {@link LayoutPanel} are positioned in the client area of the panel. The
   * position of the {@link LayoutPanel} is not altered by this method.
   * 
   * @param layoutPanel the {@link LayoutPanel} to be laid out
   */
  void layoutPanel(LayoutPanel layoutPanel);

  /**
   * Invalidates the layout, indicating that if the layout manager has cached
   * information it should be discarded.
   * 
   * @param widget the {@link Widget} that if the layout manager has cached
   *          information that should be discarded, or {@code null} for all
   *          widgets
   */
  void invalidateLayout(Widget widget);

}
