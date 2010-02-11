/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.PopupMenu;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 *
 */
public class DataTable extends FixedWidthGrid {

  private PopupMenu contextMenu;

  public DataTable() {
    super();

    sinkEvents(Event.ONDBLCLICK | Event.ONCONTEXTMENU);
  }

  public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
    return addHandler(handler, DoubleClickEvent.getType());
  }

  @Override
  public void onBrowserEvent(final Event event) {
    switch (event.getTypeInt()) {
      case Event.ONCONTEXTMENU:
        Element targetRow = null;
        Element targetCell = null;

        // Get the target row
        targetCell = getEventTargetCell(event);
        if (targetCell == null) {
          return;
        }
        targetRow = DOM.getParent(targetCell);
        int targetRowIndex = getRowIndex(targetRow);
        if (!isRowSelected(targetRowIndex)) {
          onMouseClick(event);
        }

        DOM.eventPreventDefault(event);
        showContextMenu(event);
        break;
      case Event.ONDBLCLICK:
        DomEvent.fireNativeEvent(event, this, this.getElement());
      default:
        super.onBrowserEvent(event);
    }
  }

  public PopupMenu getContextMenu() {
    return contextMenu;
  }

  public void setContextMenu(PopupMenu contextMenu) {
    this.contextMenu = contextMenu;
    if (this.contextMenu != null) {
      sinkEvents(Event.ONCONTEXTMENU);
    }
  }

  private void showContextMenu(final Event event) {
    if (contextMenu != null) {
      contextMenu.setPopupPositionAndShow(new PositionCallback() {
        public void setPosition(int offsetWidth, int offsetHeight) {
          contextMenu.setPopupPosition(event.getClientX(), event.getClientY());
        }
      });
    }
  }
}
