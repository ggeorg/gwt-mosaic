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
package org.mosaic.core.client.event;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A single event handler for all types of events.
 */
public class EventHandlers {
  private HashMap<Class, ArrayList<EventHandler>> map = null;

  /**
   * Add an event handler
   * 
   * @param eventType event type handled
   * @param handler handler
   */
  public void add(Class eventType, EventHandler handler) {
    if (map == null) {
      map = new HashMap<Class, ArrayList<EventHandler>>();
    }
    ArrayList<EventHandler> list = map.get(eventType);
    if (list == null) {
      list = new ArrayList<EventHandler>();
      map.put(eventType, list);
    }
    list.add(handler);
  }

  /**
   * Fires the event.
   * 
   * @param event the event
   * 
   */
  public void fire(AbstractEvent event) {
    if (map != null) {
      ArrayList<EventHandler> list = map.get(event.getEventClass());
      if (list != null) {
        for (EventHandler e : list) {
          event.tryFire(e);
          if (event instanceof HasCancel && ((HasCancel) event).isCanceled()) {
            return;
          }
        }
      }
    }
    return;
  }

  /**
   * Does this {@link EventHandlers} contain handlers for the specified event
   * type?
   * 
   * @param eventClass the event class
   * @return whether the event has a handler
   */
  public boolean has(Class eventClass) {
    return map != null && map.get(eventClass) != null;
  }

  /**
   * Remove the handler from the map. The return boolean should primarily be
   * used for unit test support.
   * 
   * 
   * @param eventType event type
   * @param handler the handler
   * @return whether the handler was removed
   */
  public boolean remove(Class eventType, EventHandler handler) {
    if (map == null) {
      return false;
    }
    ArrayList<EventHandler> list = map.get(eventType);
    if (list == null) {
      return false;
    } else {
      return list.remove(handler);
    }
  }
}
