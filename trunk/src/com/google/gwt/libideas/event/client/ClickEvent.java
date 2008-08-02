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
  * Represents a ClickEvent event.
  */
public class ClickEvent extends MouseEvent<ClickHandler> {

  /**
   * A widget that implements this interface is a public source of ClickEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link ClickEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addClickHandler(ClickHandler handler);
  }

  public static Key<ClickHandler> KEY = new Key<ClickHandler>(
      BrowserEvents.ONCLICK);

  /**
    * Constructs a ClickEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public ClickEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a ClickEvent.
    * @param handler the handler
    */
  protected void fireEvent(ClickHandler handler) {
    handler.onClick(this);
  }
}