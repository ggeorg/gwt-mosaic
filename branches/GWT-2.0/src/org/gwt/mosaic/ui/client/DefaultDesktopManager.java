/*
 * Copyright (c) 2010 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Rectangle;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * {@code DesktopManager} objects are owned by a {@link DesktopPanel}.
 * <p>
 * This delegation allows each L&F to provide custom behaviors for for
 * desktop-specific actions (For example, how and where the internal frame's
 * icon would appear).
 * 
 * @see DesktopPanel
 * @see DesktopPanel.InternalWindowPanel
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DefaultDesktopManager implements DesktopManager {
  
  public void beginDragging(WindowPanel w) {
    // TODO Auto-generated method stub

  }

  public void dragMove(WindowPanel w, int newX, int newY) {
    // TODO Auto-generated method stub

  }

  public void endDragging(WindowPanel w) {
    // TODO Auto-generated method stub

  }

  /**
   * Resizes the {@link WindowPanel} to fill its parents bounds.
   * 
   * @param w the {@link WindowPanel} to be resized
   */
  public void maximize(WindowPanel w, WindowState oldWindowState) {
    if (!w.isResizable()) {
      return;
    }

    final DesktopPanel d = w.getDesktopPanel();
    final int[] borders = DOM.getBorderSizes(d.getElement());

    final Rectangle r = w.getNormalBounds();

    if (w.isCollapsed()) {
      r.x = w.getAbsoluteLeft() - borders[3] - d.getAbsoluteLeft();
      r.y = w.getAbsoluteTop() - borders[0] - d.getAbsoluteTop();
      w.setNormalBounds(r);

      w.setPopupPosition(0, 0);
      WidgetHelper.setSize(w, DOM.getClientSize(d.getElement()).width, -1);

    } else {

      if (oldWindowState != WindowState.MINIMIZED) {
        r.x = w.getAbsoluteLeft() - borders[3] - d.getAbsoluteLeft();
        r.y = w.getAbsoluteTop() - borders[0] - d.getAbsoluteTop();
        r.width = w.getContentWidth();
        r.height = w.getContentHeight();
        w.setNormalBounds(r);
      }

      w.setPopupPosition(0, 0);
      WidgetHelper.setSize(w, new Dimension(DOM.getClientSize(d.getElement())));

      d.makeNotResizable(w);
    }

    if (!w.isActive()) {
      w.toFront();
    }

    w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
  }

  /**
   * Minimizes the {@link WindowPanel}.
   * 
   * @param w the {@link WindowPanel} to be minimized
   */
  public void minimize(WindowPanel w, WindowState oldWindowState) {
    if (!w.isModal()) {
      w.setNormalWindowState(oldWindowState);
      w.setVisible(false);
    }
  }

  /**
   * Removes the {@link WindowPanel} from its {@link DesktopPanel}.
   * 
   * @param the {@code CloseEvent}
   */
  public void onClose(CloseEvent<PopupPanel> event) {
    WindowPanel w = (WindowPanel) event.getTarget();
    DesktopPanel d = w.getDesktopPanel();
    d.remove(d.getWindowPanelIndex(w));
    int size = d.getWindowPanelCount();
    if (size > 0) {
      d.getWindowPanel(size - 1).toFront();
    }
  }

  /**
   * Adds the {@link WindowPanel} to its {@link DesktopPanel}.
   */
  public void onOpen(OpenEvent<WindowPanel> event) {
    WindowPanel w = (WindowPanel) event.getTarget();
    DesktopPanel d = w.getDesktopPanel();
    d.add(w);
  }

  /**
   * This will activate the selected {@link WindowPanel} moving it to the front.
   * 
   * @param the {@code SelectionEvent}
   */
  public void onSelection(SelectionEvent<WindowPanel> event) {
    final DesktopPanel d = event.getSelectedItem().getDesktopPanel();
    d.toFront(event.getSelectedItem());
  }

  /**
   * Restores the {@link WindowPanel}.
   * 
   * @param w the {@link WindowPanel} to be restored
   */
  public void restore(WindowPanel w, WindowState oldWindowState) {
    if (w.isResizable() && oldWindowState == WindowState.MAXIMIZED) {
      final DesktopPanel d = w.getDesktopPanel();
      final Rectangle r = w.getNormalBounds();
      if (w.isCollapsed()) {
        w.setPopupPosition(r.x, r.y);
        w.setContentSize(r.width, -1);
      } else {
        w.setPopupPosition(r.x, r.y);
        w.setContentSize(r.width, r.height);
        d.makeResizable(w);
      }
      d.makeDraggable(w);
      w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
    } else if (!w.isModal() && oldWindowState == WindowState.MINIMIZED) {
      w.setVisible(true);
      if (w.getWindowState() == WindowState.MAXIMIZED) {
        // TODO windowResizeHandler.onResize(null);
      }
    }
  }

}
