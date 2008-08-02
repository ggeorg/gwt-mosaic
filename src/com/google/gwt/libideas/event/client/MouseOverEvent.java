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
package com.google.gwt.libideas.event.client;

import com.google.gwt.libideas.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

/**
  * Represents a MouseOverEvent event.
  */
public class MouseOverEvent extends MouseEvent<MouseOverHandler> {

  /**
   * A widget that implements this interface is a public source of MouseOverEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link MouseOverEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addMouseOverHandler(MouseOverHandler handler);
  }

  public static Key<MouseOverHandler> KEY = new Key<MouseOverHandler>(
      BrowserEvents.ONMOUSEOVER);

  /**
    * Constructs a MouseOverEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public MouseOverEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a MouseOverEvent.
    * @param handler the handler
    */
  protected void fireEvent(MouseOverHandler handler) {
    handler.onMouseOver(this);
  }
}