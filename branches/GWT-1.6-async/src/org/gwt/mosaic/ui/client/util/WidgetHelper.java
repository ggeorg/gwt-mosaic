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
package org.gwt.mosaic.ui.client.util;

import org.gwt.mosaic.ui.client.layout.HasLayoutManager;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class WidgetHelper {

  /**
   * Returns the {@code Widget}'s closest {@link HasLayoutManager} parent.
   * 
   * @param widget the widget
   * @return the {@code Widget}'s closest {@link HasLayoutManager} parent, or
   *         {@code null} if no {@link HasLayoutManager} parent is found
   */
  public static HasLayoutManager getParent(Widget widget) {
    Widget parent = widget.getParent();
    if (parent == null) {
      return null;
    }
    if (parent instanceof HasLayoutManager) {
      return (HasLayoutManager) parent;
    } else {
      return getParent(parent);
    }
  }

}
