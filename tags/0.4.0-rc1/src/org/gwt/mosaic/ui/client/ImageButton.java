/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListenerWrapper;
import com.google.gwt.user.client.ui.SourcesClickEvents;

/**
 * A simple push button with image.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@SuppressWarnings("deprecation")
public class ImageButton extends Composite implements SourcesClickEvents,
    HasClickHandlers, HasAlignment {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ImageButton";

  public ImageButton() {
    this(new Image());
  }

  public ImageButton(AbstractImagePrototype image) {
    this(image.createImage());
  }

  public ImageButton(Image image) {
    initWidget(new WidgetWrapper(image));
    setStyleName(DEFAULT_STYLENAME);
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  @Deprecated
  public void addClickListener(ClickListener listener) {
    ListenerWrapper.WrappedClickListener.add(this, listener);
  }

  public Image getImage() {
    return (Image) ((WidgetWrapper) getWidget()).getWidget().getWidget(0, 0);
  }

  @Deprecated
  public void removeClickListener(ClickListener listener) {
    ListenerWrapper.WrappedClickListener.remove(this, listener);
  }

  public void setImage(Image image) {
    ((WidgetWrapper) getWidget()).getWidget().setWidget(0, 0, image);
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return ((WidgetWrapper) getWidget()).getHorizontalAlignment();
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    ((WidgetWrapper) getWidget()).setHorizontalAlignment(align);
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return ((WidgetWrapper) getWidget()).getVerticalAlignment();
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    ((WidgetWrapper) getWidget()).setVerticalAlignment(align);
  }

}
