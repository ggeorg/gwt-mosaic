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
package org.gwt.mosaic.ui.client.list;

import java.util.EventObject;

/**
 * An event that contains information about a modification to the content of a list.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ListDataEvent extends EventObject {
  private static final long serialVersionUID = -8373032744475643155L;

  /** The event type. */
  public enum Type {
    /** An event type indicating that the list content has been modified. */
    CONTENTS_CHANGED,
    /** An event type indicating that an interval has been added to the list. */
    INTERVAL_ADDED,
    /** An event type indicating that an interval has been removed from the list. */
    INTERVAL_REMOVED
  }

  private Type type;
  private int index0;
  private int index1;

  /**
   * Creates a {@code ListDataEvent} object. If {@code index0} is >
   * {@code index1}, {@code index0} and {@code index1} will be swapped such
   * that {@code index0} will always be <= {@code index1}.
   * 
   * @param source the source of the event
   * @param type the type of the event
   * @param index0 the index for one end of the modified range of list elements
   * @param index1 the index for the other end of the modified range of list
   *          elements
   */
  public ListDataEvent(Object source, Type type, int index0, int index1) {
    super(source);
    this.type = type;
    this.index0 = Math.min(index0, index1);
    this.index1 = Math.max(index0, index1);
  }

  /**
   * Returns the index of the first item in the range of modified items.
   * 
   * @return the index of the first item in the range of modified list items.
   */
  public int getIndex0() {
    return index0;
  }

  /**
   * Returns the index of the last item in the range of modified list items.
   * 
   * @return The index of the last item in the range of modified list items.
   */
  public int getIndex1() {
    return index1;
  }

  /**
   * Returns a code representing the type of this event.
   * 
   * @return the event type
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns a string representing the state of this event.
   * 
   * @return a string representation of this {@code ListDataEvent}
   */
  @Override
  public String toString() {
    return getClass().getName() + "[type=" + type + ",index0=" + index0
        + ",index1=" + index1 + "]";
  }

}
