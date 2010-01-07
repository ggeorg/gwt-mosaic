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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.ui.client.WindowPanel.WindowState;

import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * {@code DesktopManager} objects are owned by a {@link DesktopPanel}.
 * <p>
 * This delegation allows each L&F to provide custom behaviors for for
 * desktop-specific actions (For example, how and where the internal frame's
 * icon would appera).
 * 
 * @see DesktopPanel
 * @see DesktopPanel.InternalWindowPanel
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public interface DesktopManager extends CloseHandler<PopupPanel>,
    OpenHandler<WindowPanel>, SelectionHandler<WindowPanel> {

  void beginDragging(WindowPanel w);

  void dragMove(WindowPanel w, int newX, int newY);

  void endDragging(WindowPanel w);

  void maximize(WindowPanel w, WindowState oldWindowState);

  void minimize(WindowPanel w, WindowState oldWindowState);

  void restore(WindowPanel w, WindowState oldWindowState);

}
