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
  * Represents a MouseWheelEvent event.
  */
public class MouseWheelEvent extends MouseEvent<MouseWheelHandler> {

  /**
   * A widget that implements this interface is a public source of MouseWheelEvent
   * events.
   */
  public static interface Source {

    /**
     * Adds a {@link MouseWheelEvent} handler.
     *
     * @param handler the handler
     */
    HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler);
  }

  public static Key<MouseWheelHandler> KEY = new Key<MouseWheelHandler>(
      BrowserEvents.ONMOUSEWHEEL);

  /**
    * Constructs a MouseWheelEvent event.
    * @param e An event object, typically from an onBrowserEvent call
    */
  public MouseWheelEvent(Event e) {
    super(KEY, e);
  }

  /**
   * Gets the velocity of the mouse wheel associated with the event along the Y
   * axis. <p> The velocity of the event is an artifical measurement for
   * relative comparisons of wheel activity. It is affected by some non-browser
   * factors, including choice of input hardware and mouse acceleration
   * settings. The sign of the velocity measurement agrees with the screen
   * coordinate system; negative values are towards the origin and positive
   * values are away from the origin. Standard scrolling speed is approximately
   * ten units per event. </p>
   *
   * @return The velocity of the mouse wheel.
   */
  public int getMouseWheelVelocityY() {
    return getBrowserEvent().getMouseWheelVelocityY();
  }

  /**
    * Fires a MouseWheelEvent.
    * @param handler the handler
    */
  protected void fireEvent(MouseWheelHandler handler) {
    handler.onMouseWheel(this);
  }
}