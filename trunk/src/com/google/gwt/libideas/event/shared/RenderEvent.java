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
 * This synthetic event is called whenever a widget needs to render itself again
 * because its content has changed.
 */
public class RenderEvent extends AbstractEvent<RenderHandler> {

  /**
   * Convenience interface used to supply default add handler method.
   * 
   */
  public interface Source {
    /**
     * Adds a render handler. A widget calls its render handler right before it
     * needs to re-render its DOM contents.
     * 
     * @param handler handler
     * @return the handler registration
     */
    HandlerRegistration addRenderHandler(RenderHandler handler);
  }

  /**
   * The event key.
   */
  public static Key<RenderHandler> KEY = new Key<RenderHandler>();

  @Override
  protected Key getKey() {
    return KEY;
  }

  @Override
  protected void fireEvent(RenderHandler handler) {
    handler.onRender(this);
  }

}
