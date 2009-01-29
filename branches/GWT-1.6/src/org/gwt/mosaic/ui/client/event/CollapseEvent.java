/*
 * Copyright 2008 Google Inc.
 * Copyright 2008 Cameron Braid.
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
package org.gwt.mosaic.ui.client.event;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * Represents a collapse event.
 * 
 * @param  the type being collapse
 */
public class CollapseEvent extends GwtEvent<CollapseHandler> {

  /**
   * Handler type.
   */
  private static Type<CollapseHandler> TYPE;

  /**
   * Fires a collapse event on all registered handlers in the handler manager. If
   * no such handlers exist, this method will do nothing.
   * 
   * @param  the target type
   * @param source the source of the handlers
   * @param target the target
   */
  public static  void fire(HasCollapseHandlers source, Widget target) {
    if (TYPE != null) {
      CollapseEvent event = new CollapseEvent(target);
	  source.fireEvent(event);
    }
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CollapseHandler> getType() {
    if (TYPE == null) TYPE = new Type<CollapseHandler>();
    return TYPE;
  }

  private final Widget target;

  /**
   * Creates a new collapse event.
   */
  protected CollapseEvent(Widget target) {
    this.target = target;
  }

  /**
   * Gets the target.
   * 
   * @return the target
   */
  public Widget getTarget() {
    return target;
  }


  @Override
  protected void dispatch(CollapseHandler handler) {
    handler.onCollapseChanged(this);
  }

  @Override
  public final Type<CollapseHandler> getAssociatedType() {
    return TYPE;
  }
}
