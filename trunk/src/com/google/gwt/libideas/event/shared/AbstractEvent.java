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

/**
 * AbstractEvent is the root of all events.
 * 
 * @param <T> the type of handler used for this event
 */
public abstract class AbstractEvent<T extends EventHandler> {

  /**
   * Key class used to register events with HandlerManager.
   * <p>
   * Key is parameterized by the event handler type in order to make the
   * addHandler method type safe.
   */
  public static class Key<T extends EventHandler> {

    static int EXPECTED_MAX_HANDLERS_PER_WIDGET = 5;

    private static int INDEX_SOURCE;

    private int index;

    {
      index = INDEX_SOURCE;
      INDEX_SOURCE += EXPECTED_MAX_HANDLERS_PER_WIDGET;
    }

    // We override hash code to make it as efficient as possible.
    @Override
    public int hashCode() {
      return index;
    }
  }

  private Object source;

  protected AbstractEvent() {
  }

  /**
   * This is a method used primarily for debugging. It gives a string
   * representation of the event details. This does not override the toString
   * method because the compiler cannot always optimize toString out correctly.
   * Event types should override as desired.
   * 
   * @return a string
   */
  public String getEventDetails() {
    String name = this.getClass().getName();
    name = name.substring(name.lastIndexOf("."));
    return name + ": source = " + source;
  }

  /**
   * Returns the source that generated the event.
   */
  public Object getSource() {
    return source;
  }

  /**
   * Fires event for given handler type.
   * 
   * @param handler of type T
   */
  protected abstract void fireEvent(T handler);

  /**
   * Returns the Key used to register this event.
   */
  protected abstract Key getKey();

  /**
   * Set the source that triggered this event.
   * 
   * @param source An object of type F
   */
  void setSource(Object source) {
    this.source = source;
  }
}
