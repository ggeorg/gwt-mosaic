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

/**
 * Defines the interface for classes that know how to lay out a
 * <code>LayoutPanel</code>.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface LayoutManager {

  /**
   * Lays out the specified {@link LayoutPanel} according to this layout.
   * <p>
   * This method positions and sizes the children of a {@link LayoutPanel} using
   * the layout algorithm encoded by this layout. Child widgets of the
   * {@link LayoutPanel} are positioned in the client area of the panel. The
   * position of the {@link LayoutPanel} is not altered by this method.
   * <p>
   * TODO: caching of child preferred sizes
   * 
   * @param layoutPanel the {@link LayoutPanel} to be laid out
   */
  void layoutPanel(LayoutPanel layoutPanel);

  /**
   * Computes and returns the size of the {@link LayoutPanel} according to this
   * layout.
   * <p>
   * This method computes the preferred size that the client area of the
   * {@link LayoutPanel} must be in order to position all children at their
   * preferred size inside the panel according to the layout algorithm encoded
   * by this layout.
   * <p>
   * TODO: caching of child preferred sizes
   * 
   * @param layoutPanel a {@link LayoutPanel} widget using this layout.
   * @return an array ({@code {width, height}}) containing the computed size.
   */
  int[] getPreferredSize(LayoutPanel layoutPanel);

  /**
   * Indicates whether {@link #layoutPanel(LayoutPanel)} has to run twice. This
   * method is called by a {@link LayoutPanel#layout()} internally to check if
   * {@link #layoutPanel(LayoutPanel)} should be run again because e.g.: text
   * wrapping.
   * 
   * @return {@code true} if {@link #layoutPanel(LayoutPanel)} has to run twice,
   *         {@code false} otherwise.
   */
  boolean runTwice();

}
