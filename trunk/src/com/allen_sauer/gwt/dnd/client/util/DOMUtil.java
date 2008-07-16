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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.util.impl.DOMUtilImpl;

/**
 * Provides DOM utility methods.
 */
public class DOMUtil {

  public static final boolean DEBUG = false;

  private static DOMUtilImpl impl;

  static {
    impl = (DOMUtilImpl) GWT.create(DOMUtilImpl.class);
  }

  /**
   * Adjust line breaks within in the provided title for
   * optimal readability and display length for the current
   * user agent.
   *
   * @param title the desired raw text
   * @return formatted and escaped text
   */
  public static String adjustTitleForBrowser(String title) {
    return impl.adjustTitleForBrowser(title).replaceAll("</?code>", "`");
  }

  /**
   * Cancel all currently selected region(s) on the current page.
   */
  public static void cancelAllDocumentSelections() {
    impl.cancelAllDocumentSelections();
  }

  /**
   * Set an element's location as fast as possible, avoiding some of the overhead in
   * {@link com.google.gwt.user.client.ui.AbsolutePanel#setWidgetPosition(Widget, int, int)}.
   *
   * @param elem the element's whose position is to be modified
   * @param left the left pixel offset
   * @param top the top pixel offset
   */
  public static void fastSetElementPosition(Element elem, int left, int top) {
    elem.getStyle().setPropertyPx("left", left);
    elem.getStyle().setPropertyPx("top", top);
  }

  /**
   * TODO Handle LTR case once Bidi support is part of GWT.
   */
  public static int findIntersect(IndexedPanel parent, Location location,
      LocationWidgetComparator comparator) {
    int widgetCount = parent.getWidgetCount();

    // short circuit in case dropTarget has no children
    if (widgetCount == 0) {
      return 0;
    }

    if (DEBUG) {
      for (int i = 0; i < widgetCount; i++) {
        debugWidgetWithColor(parent, i, "white");
      }
    }

    // binary search over range of widgets to find intersection
    int low = 0;
    int high = widgetCount;

    while (true) {
      int mid = (low + high) / 2;
      assert mid >= low;
      assert mid < high;
      Widget widget = parent.getWidget(mid);
      WidgetArea midArea = new WidgetArea(widget, null, false);
      if (mid == low) {
        if (mid == 0) {
          if (comparator.locationIndicatesIndexFollowingWidget(midArea, location)) {
            debugWidgetWithColor(parent, high, "green");
            return high;
          } else {
            debugWidgetWithColor(parent, mid, "green");
            return mid;
          }
        } else {
          debugWidgetWithColor(parent, high, "green");
          return high;
        }
      }
      if (midArea.getBottom() < location.getTop()) {
        debugWidgetWithColor(parent, mid, "blue");
        low = mid;
      } else if (midArea.getTop() > location.getTop()) {
        debugWidgetWithColor(parent, mid, "red");
        high = mid;
      } else if (midArea.getRight() < location.getLeft()) {
        debugWidgetWithColor(parent, mid, "blue");
        low = mid;
      } else if (midArea.getLeft() > location.getLeft()) {
        debugWidgetWithColor(parent, mid, "red");
        high = mid;
      } else {
        if (comparator.locationIndicatesIndexFollowingWidget(midArea, location)) {
          debugWidgetWithColor(parent, mid + 1, "green");
          return mid + 1;
        } else {
          debugWidgetWithColor(parent, mid, "green");
          return mid;
        }
      }
    }
  } 

  /**
   * Determine an element's node name via the <code>nodeName</code> property.
   *
   * @param elem the element whose node name is to be determined
   * @return the element's node name
   */
  public static String getNodeName(Element elem) {
    return elem.getNodeName();
  }


  /**
   * Set the browser's status bar text, if supported and enabled in the client browser.
   *
   * @param text the message to use as the window status
   */
  public static void setStatus(String text) {
    Window.setStatus(text);
  }

  private static void debugWidgetWithColor(IndexedPanel parent, int index, String color) {
    if (DEBUG) {
      if (index >= parent.getWidgetCount()) {
        parent.getWidget(parent.getWidgetCount() - 1).getElement().getStyle().setProperty("border",
            "2px dashed " + color);
      } else {
        parent.getWidget(index).getElement().getStyle().setProperty("border", "2px solid " + color);
      }
    }
  }
}
