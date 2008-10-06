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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ImageButton extends Widget implements SourcesClickEvents {
  
  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ImageButton";

  private ClickListenerCollection clickListeners;

  private Image image;

  public ImageButton() {
    setElement(DOM.createDiv());
    sinkEvents(Event.MOUSEEVENTS);
    setStyleName(DEFAULT_STYLENAME);
  }

  public ImageButton(Image image) {
    this();
    setImage(image);
  }
  
  public ImageButton(AbstractImagePrototype image) {
    this();
    setImage(image.createImage());
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
    DOM.setEventListener(image.getElement(), this);
    getElement().setInnerHTML(image.getElement().getString());
  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
      sinkEvents(Event.ONCLICK);
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
