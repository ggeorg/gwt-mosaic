/*
 * Copyright 2008 Google Inc.
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
package org.gwt.mosaic.ui.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DoubleClickListenerCollection extends
    ArrayList<DoubleClickListener> {
  private static final long serialVersionUID = 5439952176064306231L;

  /**
   * Fires a double click to all listeners.
   * 
   * @param sender the widget sending the event
   */
  public void fireDblClick(Widget sender) {
    for (DoubleClickListener listener : this) {
      listener.onDoubleClick(sender);
    }
  }

}
