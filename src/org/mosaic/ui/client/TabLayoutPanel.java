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

import java.util.HashMap;
import java.util.Map;

import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabListenerCollection;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link LayoutPanel} that represents a tabbed set of pages, each of which
 * contains another widget. Its child widgets are shown as the user selects the
 * various tabs associated with them.
 * 
 * <h3>CSS Style Rules</h3>
 * <ul>
 * <li>.mosaic-TabLayoutPanel { the tab layout panel itself }</li>
 * <li>.mosaic-TabLayoutPanelBottom { the bottom section of the tab layout
 * panel (the deck containing the widget) }</li>
 * </ul>
 */
public class TabLayoutPanel extends LayoutComposite implements SourcesTabEvents {

  public enum TabBarPosition {
    TOP, BOTTOM
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-TabLayoutPanel";

  private TabBar tabBar;

  private final DeckLayoutPanel deck = new DeckLayoutPanel();

  private final Map<Widget, LayoutPanel> panels = new HashMap<Widget, LayoutPanel>();

  private TabListener tabListener = new TabListener() {
    public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
      if (tabListeners != null) {
        if (!tabListeners.fireBeforeTabSelected(TabLayoutPanel.this, tabIndex)) {
          return false;
        }
      }
      return true;
    }

    public void onTabSelected(SourcesTabEvents sender, final int tabIndex) {
      deck.showWidget(tabIndex);
      TabLayoutPanel.this.layout();
      if (tabListeners != null) {
        tabListeners.fireTabSelected(TabLayoutPanel.this, tabIndex);
      }
    }
  };

  private TabListenerCollection tabListeners;

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

    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BorderLayout());
    layoutPanel.setWidgetSpacing(0);

    if (decorate) {
      tabBar = new DecoratedTabBar();
    } else {
      tabBar = new TabBar();
    }

    deck.addStyleName(DEFAULT_STYLENAME + "Bottom");

    if (region == TabBarPosition.TOP) {
      layoutPanel.add(tabBar, new BorderLayoutData(BorderLayoutRegion.NORTH));
    } else {
      layoutPanel.add(tabBar, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    }
    layoutPanel.add(deck, new BorderLayoutData(decorateBody));

    tabBar.addTabListener(tabListener);

    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   */
  public void add(Widget w, String tabText) {
    add(w, tabText, false);
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabText the text to be shown on its tab
   * @param asHTML <code>true</code> to treat the specified text as HTML
   */
  public void add(Widget w, String tabText, boolean asHTML) {
    assert (w != null);
    if (panels.get(w) != null) {
      throw new IllegalArgumentException("Double entry");
    }

    tabBar.addTab(tabText, asHTML);

    final LayoutPanel panel = new LayoutPanel();
    panel.add(w);

    deck.add(panel);

    panels.put(w, panel);
  }

  /**
   * Adds a widget to the tab panel.
   * 
   * @param w the widget to be added
   * @param tabWidget the widget to be shown in the tab
   */
  public void add(Widget w, Widget tabWidget) {
    assert (w != null);
    if (panels.get(w) != null) {
      throw new IllegalArgumentException("Double entry");
    }

    tabBar.addTab(tabWidget);

    final LayoutPanel panel = new LayoutPanel();
    panel.add(w);

    deck.add(panel);

    panels.put(w, panel);
  }

  public void addTabListener(TabListener listener) {
    if (tabListeners == null) {
      tabListeners = new TabListenerCollection();
    }
    tabListeners.add(listener);
  }

  public void clear() {
    // TODO
  }

  public int getPadding() {
    return deck.getPadding();
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
      tabBar.selectTab(0);
    }
    super.layout();
  }

  public void remove(Widget w) {
    assert (w != null);
    if (panels.get(w) == null) {
      return;
    }

    final LayoutPanel panel = panels.get(w);
    tabBar.removeTab(deck.getWidgetIndex(panel));
    deck.remove(panel);

    panel.clear();

    panels.remove(w);
  }

  public void removeTabListener(TabListener listener) {
    if (tabListeners != null) {
      tabListeners.remove(listener);
    }
  }

  public void setPadding(int padding) {
    deck.setPadding(padding);
  }

}
