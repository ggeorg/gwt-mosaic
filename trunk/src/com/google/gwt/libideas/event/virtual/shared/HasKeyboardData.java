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

/**
 * Represents virtual events that have corresponding keyboard data.
 */
public interface HasKeyboardData extends HasModifierData {

  /**
   * Gets the key code associated with this event.
   *
   * <p> For {@link Event#ONKEYPRESS}, this method returns the Unicode value
   * of the character generated. For {@link Event#ONKEYDOWN} and {@link
   * Event#ONKEYUP}, it returns the code associated with the physical key.
   * </p>
   *
   * @return the Unicode character or key code.
   * @see com.google.gwt.user.client.ui.KeyboardListener
   */
  int getKeyCode();

  /**
   * Gets the key-repeat state of this event.
   *
   * @return <code>true</code> if this key event was an auto-repeat
   */
  boolean getRepeat();
}
