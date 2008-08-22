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

import org.mosaic.ui.client.layout.FillLayoutData;
import org.mosaic.ui.client.layout.HasLayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that displays all of its child widgets in a 'deck', where only one
 * can be visible at a time.
 */
public class DeckLayoutPanel extends Composite implements HasLayoutManager, IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DeckLayoutPanel";

  private Widget visibleWidget;

  public DeckLayoutPanel() {
    final LayoutPanel layoutPanel = new LayoutPanel();

    initWidget(layoutPanel);

    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Adds the specified widget to the deck.
   * 
   * @param w the widget to be added
   */
  public void add(Widget w) {
    w.setVisible(false);
    getWidget().add(w);
  }

  /**
   * Gets the index of the currently-visible widget.
   * 
   * @return the visible widget's index
   */
  public int getVisibleWidget() {
    return getWidgetIndex(visibleWidget);
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
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return getWidget().getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return getWidget().getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return getWidget().getWidgetIndex(child);
  }

  /**
   * Inserts a widget before the specified index.
   * 
   * @param w the widget to be inserted
   * @param beforeIndex the index before which it will be inserted
   * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of
   *             range
   */
  public void insert(Widget w, int beforeIndex) {
    insert(w, beforeIndex, false);
  }

  /**
   * 
   * @param w
   * @param beforeIndex
   * @param decorate
   */
  public void insert(Widget w, int beforeIndex, boolean decorate) {
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.insert(w, new FillLayoutData(decorate), beforeIndex);
    w.setVisible(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  public void layout() {
    getWidget().layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return getWidget().remove(index);
  }

  /**
   * Removes the given widget.
   * 
   * @param w the widget to be removed
   */
  public void remove(Widget w) {
    getWidget().remove(w);
  }

  public int getPadding() {
    return getWidget().getPadding();
  }

  public void setPadding(int padding) {
    getWidget().setPadding(padding);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

}
