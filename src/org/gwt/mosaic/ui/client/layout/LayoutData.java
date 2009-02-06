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
package org.gwt.mosaic.ui.client.layout;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.DecoratorPanel;

/**
 * Base class for all layout data objects.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class LayoutData implements HasHandlers {

  final transient DecoratorPanel decoratorPanel;

  protected HandlerManager handlerManager;
  
  public void fireEvent(GwtEvent<?> event) {
	  if (handlerManager != null) handlerManager.fireEvent(event);		
  }
  
  /**
   * Creates a new instance of {@code LayoutData} by specifying that the
   * associated widget should be decorated.
   * 
   * @param decorate specifies whether the associated widget will be decorated
   *          or not.
   */
  protected LayoutData(final boolean decorate) {
    if (decorate) {
      decoratorPanel = new DecoratorPanel();
    } else {
      decoratorPanel = null;
    }
  }

  /**
   * If the child widget is decorated (if the child widget is placed in a
   * {@code com.google.gwt.user.client.ui.DecoratorPanel}), this method returns
   * {@code true}, if not this method will return {@code false}.
   * 
   * @return {@code true} if the child widget is placed in a
   *         {@code com.google.gwt.user.client.ui.DecoratorPanel},
   *         {@code false} otherwise.
   */
  public final boolean hasDecoratorPanel() {
    return decoratorPanel != null;
  }


  /**
   * Adds this handler to the widget.
   * 
   * @param <H> the type of handler to add
   * @param type the event type
   * @param handler the handler
   * @return {@link HandlerRegistration} used to remove the handler
   */
  protected final <H extends EventHandler> HandlerRegistration addHandler(
      final H handler, GwtEvent.Type<H> type) {
    return ensureHandlers().addHandler(type, handler);
  }

  /**
   * Ensures the existence of the handler manager.
   * 
   * @return the handler manager
   * */
  HandlerManager ensureHandlers() {
    return handlerManager == null ? handlerManager = new HandlerManager(this)
        : handlerManager;
  }

}
