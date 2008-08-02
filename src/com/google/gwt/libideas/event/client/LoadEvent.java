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
  * Represents a LoadEvent event.
  */
public class LoadEvent extends BrowserEvent<LoadHandler> {

  /**
   * A widget that implements this interface is a public source of LoadEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link LoadEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addLoadHandler(LoadHandler handler);
  }

  public static Key<LoadHandler> KEY = new Key<LoadHandler>(
      BrowserEvents.ONLOAD);

  /**
    * Constructs a LoadEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public LoadEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a LoadEvent.
    * @param handler the handler
    */
  protected void fireEvent(LoadHandler handler) {
    handler.onLoad(this);
  }
}