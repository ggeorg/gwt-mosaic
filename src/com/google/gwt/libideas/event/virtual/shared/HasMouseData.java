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
package com.google.gwt.libideas.event.virtual.shared;

import com.google.gwt.dom.client.Element;

/**
 * Represents Virtual events which have corresponding mouse event data.
 */
public interface HasMouseData extends HasModifierData {

  /**
   * Gets the mouse buttons that were depressed when the given event occurred.
   *
   * @return a bit-field, defined by {@link Event#BUTTON_LEFT}, {@link
   *         Event#BUTTON_MIDDLE}, and {@link Event#BUTTON_RIGHT}
   */
  int getButton();

  /**
   * Gets the mouse x-position within the browser window's client area.
   *
   * @return the mouse x-position
   */
  int getClientX();

  /**
   * Gets the mouse y-position within the browser window's client area.
   *
   * @return the mouse y-position
   */
  int getClientY();

  /**
   * Gets the element from which the mouse pointer was moved (only valid for
   * {@link Event#ONMOUSEOVER}).
   *
   * @return the element from which the mouse pointer was moved
   */
  Element getFromElement();

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
  int getMouseWheelVelocityY();

  /**
   * Gets the mouse x-position on the user's display.
   *
   * @return the mouse x-position
   */
  int getScreenX();

  /**
   * Gets the mouse y-position on the user's display.
   *
   * @return the mouse y-position
   */
  int getScreenY();
}
