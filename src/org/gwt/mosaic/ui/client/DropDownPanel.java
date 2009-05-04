/*
 * Copyright 2008 Google Inc.
 * 
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
package org.gwt.mosaic.ui.client;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import org.gwt.mosaic.core.client.Dimension;

/**
 * A popup panel that can position itself relative to another widget.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DropDownPanel extends DecoratedLayoutPopupPanel {

  private static final String DEFAULT_STYLENAME = "mosaic-DropDownPanel gwt-MenuBarPopup";

  // Set of open panels so we can close them on window resize, because resizing
  // the window is equivalent to the user clicking outside the widget.
  private static ArrayList<DropDownPanel> openPanels;
  private ResizeHandler resizeHandler = new ResizeHandler() {
    public void onResize(ResizeEvent event) {
      if (openPanels != null) {
        ArrayList<DropDownPanel> old = openPanels;
        openPanels = null;
        for (DropDownPanel panel : old) {
          assert (panel.isShowing());
          if (panel.currentAnchor != null) {
            panel.showRelativeTo(panel.currentAnchor);
          }
        }
        old.clear();
        openPanels = old;
      }
    }
  };

  private Widget currentAnchor;

  /**
   * Default constructor.
   */
  public DropDownPanel() {
    super(true, false, "menuPopup");
    setStyleName(DEFAULT_STYLENAME);
    // Issue 5 fix (ggeorg)
    // Note: z-index is already set in CSS file (see: .gwt-MenuBarPopup)
    // DOM.setIntStyleAttribute(getElement(), "zIndex", Integer.MAX_VALUE);
  }

  @Override
  public final void hide() {
    hide(false);
  }

  @Override
  public void hide(boolean autohide) {
    if (!isShowing()) {
      return;
    }
    super.hide(autohide);

    // Removes this from the list of open panels.
    if (openPanels != null) {
      openPanels.remove(this);
    }
  }

  @Override
  public void show() {
    if (isShowing()) {
      return;
    }
    // Add this to the set of open panels.
    if (openPanels == null) {
      openPanels = new ArrayList<DropDownPanel>();
      Window.addResizeHandler(resizeHandler);
    }
    openPanels.add(this);
    super.show();
  }

  public void showRelativeTo(Widget anchor) {
    setCurrentAnchor(anchor);
    super.showRelativeTo(anchor);
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    pack();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        // pack();
        layout();
      }
    });
  }

  @Override
  public final void pack() {
    final Dimension size = getLayoutPanel().getPreferredSize();

    int w = getOffsetWidth() - getLayoutPanel().getOffsetWidth();
    int h = getOffsetHeight() - getLayoutPanel().getOffsetHeight();

    setContentSize(new Dimension(Math.min(Math.max(size.width,
        currentAnchor.getOffsetWidth())
        + w, Window.getClientWidth() - getAbsoluteLeft() - w), Math.min(
        size.height + h, Window.getClientHeight() - getAbsoluteTop() - h)));

    // layout(true);
    invalidate();
  }

  private void setCurrentAnchor(Widget anchor) {
    if (currentAnchor != null) {
      this.removeAutoHidePartner(currentAnchor.getElement());
    }
    if (anchor != null) {
      this.addAutoHidePartner(anchor.getElement());
    }
    currentAnchor = anchor;
  }

}
