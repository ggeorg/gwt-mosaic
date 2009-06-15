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

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * A layout panel that stacks its children vertically, displaying only one at a
 * time, with a header for each child which the user can click to display.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.mosaic-StackLayoutPanel { the panel itself }</li>
 * <li>.mosaic-StackLayoutPanel .mosaic-StackLayoutPanelItem { unselected items }</li>
 * <li>.mosaic-StackLayoutPanel .mosaic-StackLayoutPanelItem-selected {
 * selected items }</li>
 * <li>.mosaic-StackLayoutPanel .mosaic-StackLayoutPanelContent { the wrapper
 * around the contents of the item }</li>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class StackLayoutPanel extends LayoutComposite {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-StackLayoutPanel";

  /**
   * The default style name for items.
   */
  private static final String DEFAULT_ITEM_STYLENAME = DEFAULT_STYLENAME
      + "Item";

  /**
   * The default style name for content.
   */
  private static final String DEFAULT_CONTENT_STYLENAME = DEFAULT_STYLENAME
      + "Content";

  private int visibleStack = -1;

  private Map<Widget, LayoutPanel> panels = new HashMap<Widget, LayoutPanel>();

  private ClickHandler clickHandler = new ClickHandler() {
    public void onClick(ClickEvent event) {
      Widget w = (Widget) event.getSource();
      if (w instanceof Caption) {
        showStack(getLayoutPanel().getWidgetIndex(w) >> 1);
        layout();
      }
    }
  };

  public StackLayoutPanel() {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(0);

    setStyleName(DEFAULT_STYLENAME);
  }
  
  /**
   * Adds a new child with the given widget and header.
   * 
   * @param w the widget to be added
   * @param stackText the header text associated with this widget
   */
  public void add(Widget w, String stackText) {
    add(w, stackText, false);
  }

  /**
   * Adds a new child with the given widget and header, optionally interpreting
   * the header as HTML.
   * 
   * @param w the widget to be added
   * @param stackText the header text associated with this widget
   * @param asHTML <code>true</code> to treat the specified text as HTML
   */
  public void add(Widget w, String stackText, boolean asHTML) {
    final Caption caption = new Caption(stackText, asHTML);
    final LayoutPanel content = new LayoutPanel();
    final LayoutPanel layoutPanel = getLayoutPanel();
    caption.addStyleName(DEFAULT_ITEM_STYLENAME);
    caption.addClickHandler(clickHandler);
    content.addStyleName(DEFAULT_CONTENT_STYLENAME);
    content.add(w);
    panels.put(w, content);
    layoutPanel.add(caption, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(content, new BoxLayoutData(FillStyle.BOTH));
    if (visibleStack == -1) {
      showStack(0);
    } else {
      setStackVisible(visibleStack, false);
      visibleStack = getLayoutPanel().getWidgetCount() - 2;
      setStackVisible(visibleStack, true);
    }
  }

  /**
   * Gets the currently selected child index.
   * 
   * @return selected child
   */
  public int getSelectedIndex() {
    return visibleStack >> 1;
  }

  public boolean remove(Widget child) {
    return remove(child, panels.get(child));
  }

  private boolean remove(Widget child, LayoutPanel captionLayoutPanel) {
    int index = getLayoutPanel().getWidgetIndex(captionLayoutPanel);
    boolean removed = getLayoutPanel().remove(index--);
    if (removed) {
      panels.remove(child);
      removed = getLayoutPanel().remove(index);
    }
    if (removed) {
      // Correct visible stack for new location.
      if (visibleStack == index) {
        visibleStack = -1;
      } else if (visibleStack > index) {
        visibleStack -= 2;
      }
    }
    return removed;
  }

  private void setStackVisible(int index, boolean visible) {
    final Caption caption = (Caption) getLayoutPanel().getWidget(index);
    final LayoutPanel content = (LayoutPanel) getLayoutPanel().getWidget(++index);
    if (visible) {
      caption.addStyleName(DEFAULT_ITEM_STYLENAME + "-selected");
    } else {
      caption.removeStyleName(DEFAULT_ITEM_STYLENAME + "-selected");
    }
    content.setVisible(visible);
  }
  
  public Caption getCaption(int index) {
    index <<= 1;
    if ((index >= getLayoutPanel().getWidgetCount()) || (index < 0)) {
      return null;
    }
    return (Caption) getLayoutPanel().getWidget(index);
  }

  /**
   * Shows the widget at the specified child index.
   * 
   * @param index the index of the child to be shown
   */
  public void showStack(int index) {
    index <<= 1;
    if ((index >= getLayoutPanel().getWidgetCount()) || (index < 0)
        || (index == visibleStack)) {
      return;
    }

    if (visibleStack >= 0) {
      setStackVisible(visibleStack, false);
    }

    visibleStack = index;
    setStackVisible(visibleStack, true);
  }

}
