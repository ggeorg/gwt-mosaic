/*
 * Copyright 2008 Fred Sauer
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
package com.allen_sauer.gwt.dnd.client.util;

import org.mosaic.core.client.DOM;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class to represent a rectangular region of a widget relative to another
 * widget. Also keeps track of the size of the widget borders and its inner
 * width and height.
 */
public class WidgetArea extends AbstractArea {

  /**
   * Determine the area of a widget relative to a panel. The area returned is
   * such that the following are true:
   * <ul>
   * <li><code>parent.add(widget, area.getLeft(), area.getTop())</code>
   * leaves the object in the exact same location on the screen</li>
   * <li><code>area.getRight() = area.getLeft() + widget.getOffsetWidget()</code></li>
   * <li><code>area.getBottom() = area.getTop() + widget.getOffsetHeight()</code></li>
   * </ul>
   * 
   * Note that boundaryPanel need not be the parent node, or even an ancestor of
   * widget. Therefore coordinates returned may be negative or may exceed the
   * dimensions of boundaryPanel.
   * 
   * @param widget the widget whose area we seek
   * @param reference the widget relative to which we seek our area. If
   *            <code>null</code>, then <code>RootPanel().get()</code> is
   *            assumed
   * @param adjustForOffsetParentViewPorts whether or not to account for offset
   *            parent view ports
   */
  public WidgetArea(Widget widget, Widget reference,
      boolean adjustForOffsetParentViewPorts) {
    setLeft(widget.getAbsoluteLeft());
    setTop(widget.getAbsoluteTop());

    if (reference != null) {
      final int[] border = DOM.getBorderSizes(reference.getElement());
      setLeft(getLeft() - reference.getAbsoluteLeft() - border[3]);
      setTop(getTop() - reference.getAbsoluteTop() - border[0]);
    }
    setRight(getLeft() + widget.getOffsetWidth());
    setBottom(getTop() + widget.getOffsetHeight());

    if (adjustForOffsetParentViewPorts) {
      Element elem = widget.getElement().getOffsetParent();
      Element p;
      while (elem != null && (p = elem.getOffsetParent()) != null) {
        int temp;
        final int[] box = DOM.getClientSize((com.google.gwt.user.client.Element) elem);
        if ((temp = getWidth() - box[0]) > 0) {
          setRight(getRight() - temp);
        }
        if ((temp = getHeight() - box[1]) > 0) {
          setBottom(getBottom() - temp);
        }
        setLeft(getLeft() + elem.getScrollLeft());
        setRight(getRight() + elem.getScrollLeft());
        setTop(getTop() + elem.getScrollTop());
        setBottom(getBottom() + elem.getScrollTop());
        elem = p;
      }
    }
  }
}
