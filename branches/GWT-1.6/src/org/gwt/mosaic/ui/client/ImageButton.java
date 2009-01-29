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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ListenerWrapper.WrappedClickListener;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ImageButton extends Widget implements SourcesClickEvents, HasClickHandlers {
  
  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ImageButton";

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


  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addHandler(handler, ClickEvent.getType());
  }

  @Deprecated
  public void addClickListener(ClickListener listener) {
    WrappedClickListener.add(this, listener);
  }

  @Override
  public void onBrowserEvent(Event event) {
    DOM.eventPreventDefault(event);
    if (DOM.eventGetType(event) == Event.ONCLICK) {
    	ClickEvent.fireNativeEvent(event, this);
    }
    event.cancelBubble(true);
  }

  @Deprecated
  public void removeClickListener(ClickListener listener) {
	  WrappedClickListener.remove(this, listener);
  }

}
