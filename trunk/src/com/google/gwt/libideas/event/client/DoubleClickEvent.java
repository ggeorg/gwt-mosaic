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
  * Represents a DoubleClickEvent event.
  */
public class DoubleClickEvent extends MouseEvent<DoubleClickHandler> {

  /**
   * A widget that implements this interface is a public source of DoubleClickEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link DoubleClickHandler} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler);
  }

  public static Key<DoubleClickHandler> KEY = new Key<DoubleClickHandler>(
      BrowserEvents.ONDBLCLICK);

  public DoubleClickEvent(Event e) {
    super(KEY, e);
  }

  /**
    * Fires a DoubleClickEvent.
    * @param handler the handler
    */
  protected void fireEvent(DoubleClickHandler handler) {
    handler.onDoubleClick(this);
  }
}