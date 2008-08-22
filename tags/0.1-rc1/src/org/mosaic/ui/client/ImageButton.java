/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client;

import org.mosaic.core.client.DOM;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

public class ImageButton extends Widget implements SourcesClickEvents {

  private ClickListenerCollection clickListeners;

  private AbstractImagePrototype image;

  public ImageButton() {
    this(null);
  }

  public ImageButton(AbstractImagePrototype image) {
    setElement(image.createElement());
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);

//    if (image != null) {
//      this.image = image;
//      getElement().setInnerHTML(image.getHTML());
//    }

    setStyleName("mosaic-ImageButton");
  }

  public AbstractImagePrototype getImage() {
    return image;
  }

//  public void setImage(AbstractImagePrototype image) {
//    this.image = image;
//  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
    }
    clickListeners.add(listener);
  }

  @Override
  public void onBrowserEvent(Event event) {
    DOM.eventPreventDefault(event);
    if (DOM.eventGetType(event) == Event.ONCLICK) {
      if (clickListeners != null) {
        clickListeners.fireClick(this);
      }
    }
    event.cancelBubble(true);
  }

  public void removeClickListener(ClickListener listener) {
    if (clickListeners != null) {
      clickListeners.remove(listener);
    }
  }

}
