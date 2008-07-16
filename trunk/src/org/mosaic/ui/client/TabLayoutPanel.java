package org.mosaic.ui.client;

import java.util.HashMap;
import java.util.Map;

import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.HasLayout;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabListenerCollection;
import com.google.gwt.user.client.ui.Widget;

public class TabLayoutPanel extends Composite implements HasLayout, SourcesTabEvents {

  public enum TabBarPosition {
    TOP, BOTTOM
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-TabLayoutPanel";

  private final BorderLayout layout = new BorderLayout();

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

  protected TabLayoutPanel(TabBarPosition region, boolean decorate, boolean decorateBody) {
    final LayoutPanel layoutPanel = new LayoutPanel(layout);
    layout.setSpacing(0);

    if (decorate) {
      tabBar = new DecoratedTabBar();
    } else {
      tabBar = new TabBar();
    }

    deck.addStyleName("Body");

    if (region == TabBarPosition.TOP) {
      layoutPanel.add(tabBar, new BorderLayoutData(BorderLayoutRegion.NORTH));
    } else {
      layoutPanel.add(tabBar, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    }
    layoutPanel.add(deck, new BorderLayoutData(decorateBody));

    tabBar.addTabListener(tabListener);

    initWidget(layoutPanel);

    setStyleName(DEFAULT_STYLENAME);
  }

  public void add(String caption, Widget w) {
    assert (w != null);
    if (panels.get(w) != null) {
      throw new IllegalArgumentException("Double entry");
    }

    tabBar.addTab(caption);

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

  public int getMargin() {
    return layout.getMargin();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  public void layout() {
    int selection = tabBar.getSelectedTab();
    if (selection == -1) {
      selection = 0;
      tabBar.selectTab(0);
    }
    getWidget().layout();
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

  public void setMargin(int margin) {
    layout.setMargin(margin);
  }

}
