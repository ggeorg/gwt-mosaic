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
  * Represents a MouseMoveEvent event.
  */
public class MouseMoveEvent extends MouseEvent<MouseMoveHandler> {

  /**
   * A widget that implements this interface is a public source of MouseMoveEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link MouseMoveEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler);
  }

  public static Key<MouseMoveHandler> KEY = new Key<MouseMoveHandler>(
      BrowserEvents.ONMOUSEMOVE);

  /**
    * Constructs a MouseMoveEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public MouseMoveEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a MouseMoveEvent.
    * @param handler the handler
    */
  protected void fireEvent(MouseMoveHandler handler) {
    handler.onMouseMove(this);
  }
}