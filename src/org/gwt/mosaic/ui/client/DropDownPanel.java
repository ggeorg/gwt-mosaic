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
package org.gwt.mosaic.ui.client;

import java.util.ArrayList;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * A popup panel that can position itself relative to another widget.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DropDownPanel extends DecoratedLayoutPopupPanel {

  private static final String DEFAULT_STYLENAME = "mosaic-DropDownPanel gwt-MenuBarPopup";

  private Widget relativeWidget;

  // Set of open panels so we can close them on window resize, because resizing
  // the window is equivalent to the clicking outside the widget
  private static ArrayList<DropDownPanel> openPanels;

  private static WindowResizeListener resizeListener = new WindowResizeListener() {
    public void onWindowResized(int width, int height) {
      for (DropDownPanel panel : openPanels) {
        panel.hide();
      }
    }
  };

  /**
   * Default constructor.
   */
  public DropDownPanel(Widget relativeWidget) {
    super(false, false, "menuPopup");
    this.relativeWidget = relativeWidget;
    setStyleName(DEFAULT_STYLENAME);
    // Issue 5 fix (ggeorg)
    // Note: z-index is already set in CSS file (see: .gwt-MenuBarPopup)
    // DOM.setIntStyleAttribute(getElement(), "zIndex", Integer.MAX_VALUE);
  }

  @Override
  public boolean onEventPreview(Event event) {
    final Element target = event.getTarget();

    boolean eventTargetsPopup = (target != null)
        && getElement().isOrHasChild(target);

    boolean eventTargetsAnchor = (target != null) && (relativeWidget != null)
        && relativeWidget.getElement().isOrHasChild(target);

    final int type = DOM.eventGetType(event);
    switch (type) {
      case Event.ONMOUSEDOWN:
        if (!eventTargetsPopup && !eventTargetsAnchor) {
          hide(true);
          return true;
        }
        break;
    }
    return super.onEventPreview(event);
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

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.PopupPanel#show()
   */
  @Override
  public void show() {
    setPopupPosition(relativeWidget.getAbsoluteLeft(),
        relativeWidget.getAbsoluteTop() + relativeWidget.getOffsetHeight());

    super.show();

    // Add this to the set of open panels.
    if (openPanels == null) {
      openPanels = new ArrayList<DropDownPanel>();
      Window.addWindowResizeListener(resizeListener);
    }
    openPanels.add(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.PopupPanel#hide(boolean)
   */
  @Override
  public void hide(boolean autoClosed) {
    super.hide(autoClosed);

    // Removes this from the list of open panels.
    if (openPanels != null) {
      openPanels.remove(this);
    }
  }
}
