/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.actions.client;

import java.util.EventObject;

/**
 * This event is generated when an {@link Action} on a widget (such as a button}
 * occurs.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ActionEvent extends EventObject {
  private static final long serialVersionUID = 9086901210161090269L;

  private Action action;

  /**
   * Initializes a new instance of {@code ActionEvent} with the specified
   * {@link Action} and source.
   * 
   * @param action the action
   * @param source the event source
   */
  public ActionEvent(Action action, Object source) {
    super(source);
    this.action = action;
  }

  /**
   * Returns the {@link Action} associated with this event.
   * 
   * @return the {@link Action} associated with this event
   */
  public Action getAction() {
    return action;
  }
}
