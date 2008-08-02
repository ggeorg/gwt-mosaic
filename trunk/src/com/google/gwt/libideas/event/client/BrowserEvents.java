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
package com.google.gwt.libideas.event.client;

import com.google.gwt.user.client.Event;

/**
 * Enum used to model GWT eventBits and add slightly more type-safety to some
 * constructors.
 */
public enum BrowserEvents {

  ONKEYDOWN(Event.ONKEYDOWN),
  ONKEYUP(Event.ONKEYUP),
  ONKEYPRESSED(Event.ONKEYPRESS),
  ONMOUSEDOWN(Event.ONMOUSEDOWN),
  ONMOUSEUP(Event.ONMOUSEUP),
  ONMOUSEOVER(Event.ONMOUSEOVER),
  ONMOUSEOUT(Event.ONMOUSEOUT),
  ONOUSEMOVE(Event.ONMOUSEMOVE),
  ONCLICK(Event.ONCLICK),
  ONDBLCLICK(Event.ONDBLCLICK),
  ONERROR(Event.ONERROR),
  ONFOCUS(Event.ONFOCUS),
  ONBLUR(Event.ONBLUR),
  ONLOSECAPTURE(Event.ONLOSECAPTURE),
  ONLOAD(Event.ONLOAD),
  ONSCROLL(Event.ONSCROLL),
  ONMOUSEWHEEL(Event.ONMOUSEWHEEL),
  ONCHANGE(Event.ONCHANGE), ONMOUSEMOVE(Event.ONMOUSEMOVE);

  private int eventBits;

  BrowserEvents(int bits) {
    this.eventBits = bits;
  }

  public int getEventBits() {
    return eventBits;
  }
}
