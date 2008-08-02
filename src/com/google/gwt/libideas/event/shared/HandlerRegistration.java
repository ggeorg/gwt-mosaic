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
package com.google.gwt.libideas.event.shared;

import com.google.gwt.libideas.event.shared.AbstractEvent.Key;

/**
 * HandlerRegistration instances are returned by HandlerManager.addEventHandler()
 * and used to remove event handler registrations. Should be as vanilla as
 * possible, as we want it optimized out in the common case.
 */
public class HandlerRegistration {

  private HandlerManager target;

  private EventHandler handler;

  private Key key;

  HandlerRegistration(HandlerManager target, Key key, EventHandler handler) {
    this.target = target;
    this.handler = handler;
    this.key = key;
  }

  public void removeHandler() {
    target.removeHandler(key, handler);
  }
}
