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

import java.util.Iterator;

import org.gwt.mosaic.ui.client.layout.FillLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that displays all of its child widgets in a 'deck', where only one
 * can be visible at a time.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DeckLayoutPanel extends LayoutComposite implements HasWidgets,
    IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DeckLayoutPanel";

  private Widget visibleWidget;

  public DeckLayoutPanel() {
    super();
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Adds the specified widget to the deck.
   * 
   * @param w the widget to be added
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    w.setVisible(false);
    getLayoutPanel().add(w);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    getLayoutPanel().clear();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.layout.LayoutPanel#getPadding()
   */
  public int getPadding() {
    return getLayoutPanel().getPadding();
  }

  /**
   * Gets the index of the currently-visible widget.
   * 
   * @return the visible widget's index
   */
  public int getVisibleWidget() {
    return getWidgetIndex(visibleWidget);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return getLayoutPanel().getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return getLayoutPanel().getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt
   * .user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return getLayoutPanel().getWidgetIndex(child);
  }

  /**
   * Inserts a widget before the specified index.
   * 
   * @param w the widget to be inserted
   * @param beforeIndex the index before which it will be inserted
   * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of
   *           range
   */
  public void insert(Widget w, int beforeIndex) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.insert(w, new FillLayoutData(), beforeIndex);
    w.setVisible(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return getLayoutPanel().remove(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client
   * .ui.Widget)
   */
  public boolean remove(Widget w) {
    return getLayoutPanel().remove(w);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.layout.LayoutPanel#setPadding(int)
   */
  public void setPadding(int padding) {
    getLayoutPanel().setPadding(padding);
  }

  /**
   * Shows the widget at the specified index. This causes the currently visible
   * widget to be hidden.
   * 
   * @param index the index of the widget to be shown
   */
  public void showWidget(int index) {
    Widget oldWidget = visibleWidget;
    visibleWidget = getWidget(index);
    if (oldWidget != visibleWidget) {
      visibleWidget.setVisible(true);
      if (oldWidget != null) {
        oldWidget.setVisible(false);
      }
      getLayoutPanel().invalidate();
    }
  }

}
