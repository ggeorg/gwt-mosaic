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
  * Represents a MouseOutEvent event.
  */
public class MouseOutEvent extends MouseEvent<MouseOutHandler> {

  /**
   * A widget that implements this interface is a public source of MouseOutEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link MouseOutEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addMouseOutHandler(MouseOutHandler handler);
  }

  public static Key<MouseOutHandler> KEY = new Key<MouseOutHandler>(
      BrowserEvents.ONMOUSEOUT);

  /**
    * Constructs a MouseOutEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public MouseOutEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a MouseOutEvent.
    * @param handler the handler
    */
  protected void fireEvent(MouseOutHandler handler) {
    handler.onMouseOut(this);
  }
}