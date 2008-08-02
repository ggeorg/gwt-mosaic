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

import com.google.gwt.user.client.Event;
import com.google.gwt.libideas.event.shared.HandlerRegistration;

/**
  * Represents a KeyDownEvent event.
  */
public class KeyDownEvent extends KeyboardEvent<KeyDownHandler> {

  /**
   * A widget that implements this interface is a public source of KeyDownEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link KeyDownEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addKeyDownHandler(KeyDownHandler handler);
  }

  public static Key<KeyDownHandler> KEY = new Key<KeyDownHandler>(
      BrowserEvents.ONKEYDOWN);

  /**
    * Constructs a KeyDownEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public KeyDownEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a KeyDownEvent.
    * @param handler the handler
    */
  protected void fireEvent(KeyDownHandler handler) {
    handler.onKeyDown(this);
  }
}
