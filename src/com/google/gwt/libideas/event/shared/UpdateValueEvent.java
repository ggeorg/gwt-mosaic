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
 * This synthetic event is called after a value has been updated.
 * 
 * @param <Value> the type of value being updated.
 */
public class UpdateValueEvent<Value> extends
    AbstractEvent<UpdateValueHandler<Value>> {
  /**
   * Convenience interface used to supply default add handler method.
   * 
   */
  public interface Source {
    /**
     * Adds a value updated handler.
     * 
     * @param handler handler
     * @return the handler registration
     */
    HandlerRegistration addValueUpdateHandler(UpdateValueHandler handler);
  }

  /**
   * The event key.
   */
  public static Key<UpdateValueHandler> KEY = new Key<UpdateValueHandler>();

  private Value oldValue;

  private Value newValue;

  /**
   * Constructor.
   * 
   * @param oldValue old value
   * @param newValue new value
   */

  public UpdateValueEvent(Value oldValue, Value newValue) {
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  @Override
  public String getEventDetails() {
    return super.getEventDetails() + " old = " + oldValue + " new =" + newValue;
  }

  public Value getNewValue() {
    return newValue;
  }

  public Value getOldValue() {
    return oldValue;
  }

  @Override
  protected void fireEvent(UpdateValueHandler<Value> handler) {
    handler.onUpdateValue(this);
  }

  @Override
  protected Key getKey() {
    return KEY;
  }
}
