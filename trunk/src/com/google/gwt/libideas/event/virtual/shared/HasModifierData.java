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
 * Represents virtual events that have corresponding modifier-key data.
 */
public interface HasModifierData extends EventData {

  /**
   * Gets whether the ALT key was depressed when the given event occurred.
   *
   * @return <code>true</code> if ALT was depressed when the event occurred
   */
  boolean getAltKey();

  /**
   * Gets whether the CTRL key was depressed when the given event occurred.
   *
   * @return <code>true</code> if CTRL was depressed when the event occurred
   */
  boolean getCtrlKey();

  /**
   * Gets whether the META key was depressed when the given event occurred.
   *
   * @return <code>true</code> if META was depressed when the event occurred
   */
  boolean getMetaKey();

  /**
   * Gets whether the shift key was depressed when the given event occurred.
   *
   * @return <code>true</code> if shift was depressed when the event occurred
   */
  boolean getShiftKey();
}
