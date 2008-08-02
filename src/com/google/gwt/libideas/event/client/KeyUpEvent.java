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
  * Represents a KeyUpEvent event.
  */
public class KeyUpEvent extends KeyboardEvent<KeyUpHandler> {

  /**
   * A widget that implements this interface is a public source of KeyUpEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link KeyUpEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addKeyUpHandler(KeyUpHandler handler);
  }

  public static Key<KeyUpHandler> KEY = new Key<KeyUpHandler>(
      BrowserEvents.ONKEYUP);

  /**
    * Constructs a KeyUpEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public KeyUpEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a KeyUpEvent.
    * @param handler the handler
    */
  protected void fireEvent(KeyUpHandler handler) {
    handler.onKeyUp(this);
  }
}