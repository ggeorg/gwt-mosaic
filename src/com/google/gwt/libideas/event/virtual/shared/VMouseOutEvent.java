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
package com.google.gwt.libideas.event.virtual.shared;
/**
 * TODO
 */
public class VMouseOutEvent extends VMouseEvent<VMouseOutHandler> {

  public static Key<VMouseOutHandler> KEY = new Key<VMouseOutHandler>();

  public VMouseOutEvent(EventData data) {
    super(KEY, data);
  }

  protected void fireEvent(VMouseOutHandler handler) {
    handler.onMouseOut(this);
  }
}