/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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

import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.ListenerWrapper;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link LayoutPanel} that represents a tabbed set of pages, each of which
 * contains another widget. Its child widgets are shown as the user selects the
 * various tabs associated with them.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul class='css'>
 * <li>.mosaic-TabLayoutPanel { the
 * tab layout panel itself }</li>
 * <li>.mosaic-TabLayoutPanelBottom { the bottom
 * section of the tab layout panel (the deck containing the widget) }</li>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class TabLayoutPanel extends LayoutComposite implements TabListener,
    SourcesTabEvents, HasWidgets, /* TODO HasAnimation, */IndexedPanel,
    HasBeforeSelectionHandlers<Integer>, HasSelectionHandlers<Integer> {

  public enum CloseBehaviour {
    SHOW_LEFT_TAB
  }

  public enum TabBarPosition {
    TOP, BOTTOM
  }

  /**
   * Glue code between listeners and handlers.
   * 
   * @param <T> listener type
   * @deprecated will be removed in GWT 2.0 with the handler listeners
   *             themselves
   */
  @Deprecated
  static class WrappedTabListener extends ListenerWrapper<TabListener>
      implements SelectionHandler<Integer>, BeforeSelectionHandler<Integer> {

    public static void add(TabLayoutPanel source, TabListener listener) {
      WrappedTabListener t = new WrappedTabListener(listener);
      source.addBeforeSelectionHandler(t);
      source.addSelectionHandler(t);
    }

    public static void remove(Widget eventSource, TabListener listener) {
      baseRemove(eventSource, listener, SelectionEvent.getType(),
          BeforeSelectionEvent.getType());
    }

    protected WrappedTabListener(TabListener listener) {
      super(listener);
    }

    public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
      if (!getListener().onBeforeTabSelected(
          (SourcesTabEvents) event.getSource(), event.getItem().intValue())) {
        event.cancel();
      }
    }

    public void onSelection(SelectionEvent<Integer> event) {
      getListener().onTabSelected((SourcesTabEvents) event.getSource(),
          event.getSelectedItem().intValue());
    }

  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-TabLayoutPanel";

  private ScrollTabBar tabBar;

  private final DeckLayoutPanel deck = new DeckLayoutPanel();

  public TabLayoutPanel() {
    this(TabBarPosition.TOP, false, false);
  }

  public TabLayoutPanel(boolean decorateBody) {
    this(TabBarPosition.TOP, false, decorateBody);
  }

  public TabLayoutPanel(TabBarPosition region) {
    this(region, false, false);
  }

  public TabLayoutPanel(TabBarPosition region, boolean decorateBody) {
    this(region, false, decorateBody);
  }

  protected TabLayoutPanel(TabBarPosition region, boolean decorate,
      boolean decorateBody) {
    super();

    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BorderLayout());
    layoutPanel.setWidgetSpacing(0);

    if (decorate) {
      if (region == TabBarPosition.TOP) {
        tabBar = new ScrollTabBar(this, true, false);
      } else {
        tabBar = new ScrollTabBar(this, true, true);
      }
    } else {
      tabBar = new ScrollTabBar(this);
    }

    deck.addStyleName(DEFAULT_STYLENAME + "Bottom");
    if (!decorateBody) {
      deck.addStyleName("gwt-TabPanelBottom"); // use GWT's TabPanel style
    }

    if (region == TabBarPosition.TOP) {
      layoutPanel.add(tabBar, new BorderLayoutData(Region.NORTH));
    } else {
      layoutPanel.add(tabBar, new BorderLayoutData(Region.SOUTH));
    }
    layoutPanel.add(deck, new BorderLayoutData(decorateBody));

    tabBar.addTabListener(this);

    setStyleName(DEFAULT_STYLENAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client
   * .ui.Widget)
   */
  public void add(Widget w) {
    throw new UnsupportedOperationException(
        "A tabText parameter must be specified with add().");
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   */
  public void add(Widget w, String tabText) {
    insert(w, tabText, getWidgetCount());
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   * @param asHTML <code>true</code> to treat the specified text as HTML
   */
  public void add(Widget w, String tabText, boolean asHTML) {
    insert(w, tabText, asHTML, getWidgetCount());
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabWidget the widget to be shown in the tab
   */
  public void add(Widget w, Widget tabWidget) {
    insert(w, tabWidget, getWidgetCount());
  }

  public HandlerRegistration addBeforeSelectionHandler(
      BeforeSelectionHandler<Integer> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  public HandlerRegistration addSelectionHandler(
      SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesTabEvents#addTabListener(com.google
   * .gwt.user.client.ui.TabListener)
   */
  @Deprecated
  public void addTabListener(TabListener listener) {
    WrappedTabListener.add(this, listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    while (getWidgetCount() > 0) {
      remove(getWidget(0));
    }
  }

  public int getPadding() {
    return deck.getPadding();
  }

  /**
   * Gets the tab that is currently selected.
   * 
   * @return the selected tab
   */
  public int getSelectedTab() {
    return tabBar.getSelectedTab();
  }

  /**
   * Gets the specified tab's HTML.
   * 
   * @param tabIndex the index of the tab whose HTML is to be retrieved
   * @return the tab's HTML
   */
  public String getTabHTML(int tabIndex) {
    return tabBar.getTabHTML(tabIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return deck.getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return deck.getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt
   * .user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget widget) {
    return deck.getWidgetIndex(widget);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the <code>TabLayoutPanel</code>, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted
   * @param tabText the text to be shown on its tab
   * @param asHTML <code>true</code> to treat the specified text as HTML
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, String tabText, boolean asHTML,
      int beforeIndex) {
    assert (widget != null);

    // Check to see if the tab panel already contains the widget. If so, remove
    // it and see if we need to shift the position to the left.
    int idx = getWidgetIndex(widget);
    if (idx != -1) {
      remove(widget);
      if (idx < beforeIndex) {
        beforeIndex--;
      }
    }

    tabBar.insertTab(tabText, asHTML, beforeIndex);
    deck.insert(widget, beforeIndex);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the <code>TabLayoutPanel</code>, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted
   * @param tabText the text to be shown on its tab
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, String tabText, int beforeIndex) {
    insert(widget, tabText, false, beforeIndex);
  }

  /**
   * Inserts a widget into the tab panel. If the Widget is already attached to
   * the <code>TabLayoutPanel</code>, it will be moved to the requested index.
   * 
   * @param widget the widget to be inserted
   * @param tabWidget the widget to be shown on its tabs
   * @param beforeIndex the index before which it will be inserted
   */
  public void insert(Widget widget, Widget tabWidget, int beforeIndex) {
    assert (widget != null);

    // Check to see if the tab panel already contains the widget. If so, remove
    // it and see if we need to shift the position to the left.
    int idx = getWidgetIndex(widget);
    if (idx != -1) {
      remove(widget);
      if (idx < beforeIndex) {
        beforeIndex--;
      }
    }

    tabBar.insertTab(tabWidget, beforeIndex);
    deck.insert(widget, beforeIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return new Iterator<Widget>() {
      public boolean hasNext() {
        return deck.iterator().hasNext();
      }

      public Widget next() {
        return deck.iterator().next();
      }

      public void remove() {
        throw new UnsupportedOperationException("Use TabLayoutPanel.remove()");
      }
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  @Override
  public void layout() {
    int selection = tabBar.getSelectedTab();
    if (selection == -1) {
      selection = 0;
      selectTab(0);
      return;
    }
    super.layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.TabListener#onBeforeTabSelected(com.google
   * .gwt.user.client.ui.SourcesTabEvents, int)
   */
  @Deprecated
  public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
    // if (tabListeners != null) {
    // return tabListeners.fireBeforeTabSelected(TabLayoutPanel.this, tabIndex);
    // }
    // return true;

    BeforeSelectionEvent<Integer> event = BeforeSelectionEvent.fire(this,
        tabIndex);
    return event == null || !event.isCanceled();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.TabListener#onTabSelected(com.google.gwt.
   * user.client.ui.SourcesTabEvents, int)
   */
  public void onTabSelected(SourcesTabEvents sender, final int tabIndex) {
    deck.showWidget(tabIndex);

    HasLayoutManager lm = WidgetHelper.getParent(this);
    if (lm != null) {
      lm.layout();
    } else {
      layout();
    }

    SelectionEvent.fire(this, tabIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    final Widget widget = deck.getWidget(index);

    if (widget == null) {
      return false;
    }

    return remove(index, widget);
  }

  private boolean remove(int index, Widget widget) {
    tabBar.removeTab(index);
    return deck.remove(widget);
  }

  /**
   * Removes the given widget, and its associated tab.
   * 
   * @param widget the widget to be removed
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget widget) {
    assert (widget != null);

    int index = getWidgetIndex(widget);

    if (index == -1) {
      // tabBar.removeTab will add 1 to the index, ignoring the fact that
      // the widget was not found - it's probably correct to return instead
      return false;
    }

    return remove(index, widget);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.SourcesTabEvents#removeTabListener(com.google
   * .gwt.user.client.ui.TabListener)
   */
  @Deprecated
  public void removeTabListener(TabListener listener) {
    WrappedTabListener.remove(this, listener);
  }

  /**
   * Programmatically selects the specified tab.
   * 
   * @param index the index of the tab to be selected
   */
  public void selectTab(int index) {
    tabBar.selectTab(index);
  }

  public void setPadding(int padding) {
    deck.setPadding(padding);
  }

}
