/*
 * Copyright 2008 Google Inc.
 * 
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollListenerCollection;
import com.google.gwt.user.client.ui.UIObject;

/**
 * A {@link LayoutPanel} that wraps its contents in a scrollable area.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ScrollLayoutPanel extends LayoutPanel {

  private ScrollListenerCollection scrollListeners;

  /**
   * Creates a new <code>ScrollLayoutPanel</code> with a vertical
   * <code>BoxLayout</code>.
   */
  public ScrollLayoutPanel() {
    this(new BoxLayout(Orientation.VERTICAL));
  }

  /**
   * Creates a new <code>ScrollLayoutPanel</code> with the specified layout
   * manager.
   * 
   * @param layout the <code>LayoutManager</code> to use
   */
  public ScrollLayoutPanel(LayoutManager layout) {
    super(layout);
    setAlwaysShowScrollBars(false);
    sinkEvents(Event.ONSCROLL);

    // Prevent IE standard mode bug when a AbsolutePanel is contained.
    DOM.setStyleAttribute(getElement(), "position", "relative");
  }

  public void addScrollListener(ScrollListener listener) {
    if (scrollListeners == null) {
      scrollListeners = new ScrollListenerCollection();
    }
    scrollListeners.add(listener);
  }

  /**
   * Ensures that the specified item is visible, by adjusting the panel's scroll
   * position.
   * 
   * @param item the item whose visibility is to be ensured
   */
  public void ensureVisible(UIObject item) {
    Element scroll = getElement();
    Element element = item.getElement();
    ensureVisibleImpl(scroll, element);
  }

  private native void ensureVisibleImpl(Element scroll, Element e)
  /*-{
    if (!e)
      return; 
    
    var item = e;
    var realOffset = 0;
    while (item && (item != scroll)) {
      realOffset += item.offsetTop;
      item = item.offsetParent;
    }
    
    scroll.scrollTop = realOffset - scroll.offsetHeight / 2;
  }-*/;

  /**
   * Gets the horizontal scroll position.
   * 
   * @return the horizontal scroll position, in pixels
   */
  public int getHorizontalScrollPosition() {
    return DOM.getElementPropertyInt(getElement(), "scrollLeft");
  }

  /**
   * Gets the vertical scroll position.
   * 
   * @return the vertical scroll position, in pixels
   */
  public int getScrollPosition() {
    return DOM.getElementPropertyInt(getElement(), "scrollTop");
  }

  @Override
  public void onBrowserEvent(Event event) {
    if (DOM.eventGetType(event) == Event.ONSCROLL) {
      if (scrollListeners != null) {
        scrollListeners.fireScroll(this, getHorizontalScrollPosition(),
            getScrollPosition());
      }
    }
  }

  public void removeScrollListener(ScrollListener listener) {
    if (scrollListeners != null) {
      scrollListeners.remove(listener);
    }
  }

  /**
   * Scroll to the bottom of this panel.
   */
  public void scrollToBottom() {
    setScrollPosition(DOM.getElementPropertyInt(getElement(), "scrollHeight"));
  }

  /**
   * Scroll to the far left of this panel.
   */
  public void scrollToLeft() {
    setHorizontalScrollPosition(0);
  }

  /**
   * Scroll to the far right of this panel.
   */
  public void scrollToRight() {
    setHorizontalScrollPosition(DOM.getElementPropertyInt(getElement(),
        "scrollWidth"));
  }

  /**
   * Scroll to the top of this panel.
   */
  public void scrollToTop() {
    setScrollPosition(0);
  }

  /**
   * Sets whether this panel always shows its scroll bars, or only when
   * necessary.
   * 
   * @param alwaysShow <code>true</code> to show scroll bars at all times
   */
  public void setAlwaysShowScrollBars(boolean alwaysShow) {
    DOM.setStyleAttribute(getElement(), "overflow", alwaysShow ? "scroll"
        : "auto");
  }

  /**
   * Sets the horizontal scroll position.
   * 
   * @param position the new horizontal scroll position, in pixels
   */
  public void setHorizontalScrollPosition(int position) {
    DOM.setElementPropertyInt(getElement(), "scrollLeft", position);
  }

  /**
   * Sets the vertical scroll position.
   * 
   * @param position the new vertical scroll position, in pixels
   */
  public void setScrollPosition(int position) {
    DOM.setElementPropertyInt(getElement(), "scrollTop", position);
  }
}