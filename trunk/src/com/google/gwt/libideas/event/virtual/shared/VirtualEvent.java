/*
 * Copyright 2007 Google Inc.
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

import com.google.gwt.libideas.event.shared.AbstractEvent;
import com.google.gwt.libideas.event.shared.EventHandler;
/**
 * TODO
 * @param <T> event handler type
 */
public abstract class VirtualEvent<T extends EventHandler>
    extends AbstractEvent<T> {
  /**
   * TODO
   */
  public static class Key<T extends EventHandler> extends AbstractEvent.Key<T> {
  }

  private EventData eventData;

  private Key<T> key;

  public VirtualEvent(Key<T> key, EventData eventData) {
    this.key = key;
    this.eventData = eventData;
  }

  public EventData getEventData() {
    return eventData;
  }

  public AbstractEvent.Key<T> getKey() {
    return key;
  }
}
