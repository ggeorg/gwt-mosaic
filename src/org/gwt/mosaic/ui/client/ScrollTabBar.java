package org.gwt.mosaic.ui.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AbstractDecoratorPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollTabBar extends LayoutComposite implements HasAnimation {

  public static class DecoratedBottomTabBar extends TabBar {
    static String[] TAB_ROW_STYLES = {"tabTop", "tabMiddle", "tabBottom"};

    static final String STYLENAME_DEFAULT = "mosaic-DecoratedBottomTabBar";

    /**
     * Creates an empty {@link DecoratedTabBar}.
     */
    public DecoratedBottomTabBar() {
      super();
      setStylePrimaryName(STYLENAME_DEFAULT);
    }

    @Override
    protected SimplePanel createTabTextWrapper() {
      return new AbstractDecoratorPanel(TAB_ROW_STYLES, 1) {
      };
    }
  }

  /**
   * An {@code Animation} used to scroll the {@code TabBar}.
   */
  private static class ScrollAnimation extends Animation {

    private ScrollTabBar scrollTabBar = null;

    private int scrollOffset = 0;

    @Override
    protected void onComplete() {
      scrollTabBar.updateNavBarState();

      this.scrollTabBar = null;
      this.scrollOffset = 0;
    }

    private void onInstantaneousRun() {
      onUpdate(1.0);
      scrollTabBar.updateNavBarState();
    }

    @Override
    protected void onStart() {
      super.onStart();
    }

    @Override
    protected void onUpdate(double progress) {
      int scrollLeft = scrollTabBar.tabBarWrapper.getElement().getScrollLeft();
      if (scrollOffset > 0) {
        scrollLeft = Math.min(scrollTabBar.tabBar.getOffsetWidth(), scrollLeft
            + (int) (scrollOffset * progress));
      } else {
        scrollLeft = Math.max(0, scrollLeft + (int) (scrollOffset * progress));
      }
      scrollTabBar.tabBarWrapper.getElement().setScrollLeft(scrollLeft);
    }

    public void scrollTabBar(ScrollTabBar scrollTabBar, int scrollOffset,
        boolean animate) {
      // Immediately complete previous animation
      cancel();

      this.scrollTabBar = scrollTabBar;
      this.scrollOffset = scrollOffset;

      // Start the animation
      if (animate) {
        run(CoreConstants.DEFAULT_DELAY_MILLIS);
      } else {
        onInstantaneousRun();
      }
    }

  }

  private List<Widget> tabs = new ArrayList<Widget>();

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ScrollTabBar";

  /**
   * The scroll offset.
   */
  private static final int SCROLL_OFFSET = 32;

  /**
   * The {@code ScrollAnimation} used to scroll the {@code TabBar}.
   */
  private static ScrollAnimation scrollAnimation;

  private final TabBar tabBar;

  private final AbsolutePanel tabBarWrapper;

  private final HorizontalPanel navBar;

  private final Button scrollLeftBtn, scrollRightBtn, tabBarMenuBtn;

  private boolean scrollLeftBtnDown = false;

  private final Timer scrollLeftBtnTimer = new Timer() {
    @Override
    public void run() {
      if (scrollLeftBtn.isEnabled() && scrollLeftBtnDown) {
        scrollLeftBtn.click();
      } else {
        cancel();
      }
    }
  };

  private boolean scrollRightBtnDown = false;

  private final Timer scrollRightBtnTimer = new Timer() {
    @Override
    public void run() {
      if (scrollRightBtn.isEnabled() && scrollRightBtnDown) {
        scrollRightBtn.click();
      } else {
        cancel();
      }
    }
  };

  private boolean isAnimationEnabled = true;

  public ScrollTabBar() {
    this(false, false);
  }

  public ScrollTabBar(boolean decorated) {
    this(decorated, false);
  }

  public ScrollTabBar(boolean decorated, boolean atBottom) {
    super(new BoxLayout(atBottom?Alignment.START:Alignment.END));

    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    if (decorated) {
      if (atBottom) {
        tabBar = new DecoratedBottomTabBar() {
          @Override
          protected void insertTabWidget(Widget widget, int beforeIndex) {
            super.insertTabWidget(widget, beforeIndex);
            tabs.add(beforeIndex, widget);
          }

          @Override
          public void removeTab(int index) {
            super.removeTab(index);
            tabs.remove(index);
          }
        };
      } else {
        tabBar = new DecoratedTabBar() {
          @Override
          protected void insertTabWidget(Widget widget, int beforeIndex) {
            super.insertTabWidget(widget, beforeIndex);
            tabs.add(beforeIndex, widget);
          }

          @Override
          public void removeTab(int index) {
            super.removeTab(index);
            tabs.remove(index);
          }
        };
      }
    } else {
      tabBar = new TabBar() {
        @Override
        protected void insertTabWidget(Widget widget, int beforeIndex) {
          super.insertTabWidget(widget, beforeIndex);
          tabs.add(beforeIndex, widget);
        }

        @Override
        public void removeTab(int index) {
          super.removeTab(index);
          tabs.remove(index);
        }
      };
    }

    tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
      public void onSelection(final SelectionEvent<Integer> event) {
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            invalidate();
            scrollTabIntoView();
          }
        });
      }
    });

    tabBarWrapper = createWrapper("tabBarWrapper");
    tabBarWrapper.add(tabBar);

    navBar = new HorizontalPanel();
    navBar.addStyleName(DEFAULT_STYLENAME + "-NavBar");
    navBar.setVisible(false);
    scrollLeftBtn = new Button(Caption.IMAGES.toolArrowLeft().getHTML(),
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            createScrollAnimation();
            scrollAnimation.scrollTabBar(ScrollTabBar.this, -1 * SCROLL_OFFSET,
                isAnimationEnabled);
          }
        });
    scrollLeftBtn.addMouseDownHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        scrollLeftBtnDown = true;
        scrollLeftBtnTimer.scheduleRepeating(CoreConstants.DEFAULT_DELAY_MILLIS);
      }
    });
    scrollLeftBtn.addMouseUpHandler(new MouseUpHandler() {
      public void onMouseUp(MouseUpEvent event) {
        scrollLeftBtnDown = false;
        scrollLeftBtnTimer.cancel();
      }
    });
    navBar.add(scrollLeftBtn);

    scrollRightBtn = new Button(Caption.IMAGES.toolArrowRight().getHTML(),
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            createScrollAnimation();
            scrollAnimation.scrollTabBar(ScrollTabBar.this, SCROLL_OFFSET,
                isAnimationEnabled);
          }
        });
    scrollRightBtn.addMouseDownHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        scrollRightBtnDown = true;
        scrollRightBtnTimer.scheduleRepeating(CoreConstants.DEFAULT_DELAY_MILLIS);
      }
    });
    scrollRightBtn.addMouseUpHandler(new MouseUpHandler() {
      public void onMouseUp(MouseUpEvent event) {
        scrollRightBtnDown = false;
        scrollRightBtnTimer.cancel();
      }
    });
    navBar.add(scrollRightBtn);

    tabBarMenuBtn = new Button(Caption.IMAGES.toolArrowDown().getHTML(),
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            final PopupMenu menu = new PopupMenu();
            menu.addItem(tabBar.getTabHTML(tabBar.getSelectedTab()), true,
                new Command() {
                  public void execute() {
                    tabBar.selectTab(tabBar.getSelectedTab());
                  }
                });
            menu.addSeparator();
            for (int i = 0, n = tabBar.getTabCount(); i < n; i++) {
              final int index = i;
              if (index != tabBar.getSelectedTab()) {
                menu.addItem(tabBar.getTabHTML(i), true, new Command() {
                  public void execute() {
                    tabBar.selectTab(index);
                  }
                });
              }
            }
            menu.setPopupPositionAndShow(new PositionCallback() {
              public void setPosition(int offsetWidth, int offsetHeight) {
                final Dimension box = WidgetHelper.getOffsetSize(tabBarMenuBtn);
                int left = DOM.getAbsoluteLeft(tabBarMenuBtn.getElement());
                if (left + offsetWidth > Window.getClientWidth()) {
                  left += box.width - offsetWidth;
                }
                final int top = DOM.getAbsoluteTop(tabBarMenuBtn.getElement())
                    + box.height;
                menu.setPopupPosition(left, top);
              }
            });
          }
        });
    navBar.add(tabBarMenuBtn);

    layoutPanel.add(tabBarWrapper, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(navBar);

    DOM.setStyleAttribute(tabBarWrapper.getElement(), "overflow", "hidden");

    addStyleName(DEFAULT_STYLENAME);
  }

  public HandlerRegistration addBeforeSelectionHandler(
      BeforeSelectionHandler<Integer> handler) {
    return tabBar.addBeforeSelectionHandler(handler);
  }

  public HandlerRegistration addSelectionHandler(
      SelectionHandler<Integer> handler) {
    return tabBar.addSelectionHandler(handler);
  }

  @Deprecated
  public void addTabListener(TabListener listener) {
    tabBar.addTabListener(listener);
  }

  @Deprecated
  public void removeTabListener(TabListener listener) {
    tabBar.removeTabListener(listener);
  }

  protected void createScrollAnimation() {
    if (scrollAnimation == null) {
      scrollAnimation = new ScrollAnimation();
    }
  }

  private AbsolutePanel createWrapper(String cssName) {
    final AbsolutePanel wrapper = new AbsolutePanel() {
      @Override
      public void onBrowserEvent(Event event) {
        ScrollTabBar.this.onBrowserEvent(event);
      }
    };
    final Element wrapperElem = wrapper.getElement();
    DOM.setIntStyleAttribute(wrapperElem, "margin", 0);
    DOM.setIntStyleAttribute(wrapperElem, "border", 0);
    DOM.setIntStyleAttribute(wrapperElem, "padding", 0);
    wrapper.setStyleName(cssName);
    return wrapper;
  }

  public int getSelectedTab() {
    return tabBar.getSelectedTab();
  }

  /**
   * Gets the number of tabs present.
   * 
   * @return the tab count
   */
  public int getTabCount() {
    return tabBar.getTabCount();
  }

  public String getTabHTML(int tabIndex) {
    return tabBar.getTabHTML(tabIndex);
  }

  public void insertTab(String tabText, boolean asHTML, int beforeIndex) {
    tabBar.insertTab(tabText, asHTML, beforeIndex);
    invalidate();
  }

  public void insertTab(Widget tabWidget, int beforeIndex) {
    tabBar.insertTab(tabWidget, beforeIndex);
    invalidate();
  }

  public boolean isAnimationEnabled() {
    return isAnimationEnabled;
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.LayoutComposite#layout()
   */
  @Override
  public void layout() {
    super.layout();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (tabBar.getOffsetWidth() > tabBarWrapper.getOffsetWidth()
            + navBar.getOffsetWidth()) {
          if (!navBar.isVisible()) {
            toggleNavBarVisibility(true);
          } else {
            final int scrollLeft = tabBarWrapper.getElement().getScrollLeft();
            if (tabBarWrapper.getOffsetWidth() > tabBar.getOffsetWidth()
                - scrollLeft) {
              tabBarWrapper.getElement().setScrollLeft(
                  Math.min(scrollLeft, tabBar.getOffsetWidth() - scrollLeft));
            }
          }
        } else if (navBar.isVisible()) {
          tabBarWrapper.getElement().setScrollLeft(0);
          toggleNavBarVisibility(false);
        }

        if (navBar.isVisible()) {
          updateNavBarState();
        }
      }
    });
  }

  private void toggleNavBarVisibility(boolean visible) {
    navBar.setVisible(visible);

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        invalidate();
        WidgetHelper.getParent(ScrollTabBar.this).layout();
        scrollTabIntoView();
      }
    });
  }

  public void removeTab(int index) {
    tabBar.removeTab(index);
    invalidate();
  }

  private void scrollTabIntoView() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        tabs.get(tabBar.getSelectedTab()).getElement().scrollIntoView();
      }
    });
  }

  public void selectTab(final int i) {
    tabBar.selectTab(i);
  }

  public void setAnimationEnabled(boolean enable) {
    isAnimationEnabled = enable;
  }

  private void updateNavBarState() {
    final int scrollLeft = tabBarWrapper.getElement().getScrollLeft();
    scrollLeftBtn.setEnabled(scrollLeft > 0);
    scrollRightBtn.setEnabled(scrollLeft < tabBar.getOffsetWidth()
        - tabBarWrapper.getOffsetWidth());
  }
}
