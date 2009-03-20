package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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

public class ScrollTabBar extends LayoutComposite implements HasAnimation {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ScrollTabBar";

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
        run(ANIMATION_DURATION);
      } else {
        onInstantaneousRun();
      }
    }

  }

  /**
   * The duration of the animation.
   */
  private static final int ANIMATION_DURATION = 333;

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
    super();

    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout(Alignment.END));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    if (decorated) {
      if (atBottom) {
        tabBar = new DecoratedBottomTabBar();
      } else {
        tabBar = new DecoratedTabBar();
      }
    } else {
      tabBar = new TabBar();
    }
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
        scrollLeftBtnTimer.scheduleRepeating(333);
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
        scrollRightBtnTimer.scheduleRepeating(333);
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
                final int[] box = DOM.getBoxSize(tabBarMenuBtn.getElement());
                int left = DOM.getAbsoluteLeft(tabBarMenuBtn.getElement());
                if (left + offsetWidth > Window.getClientWidth()) {
                  left += box[0] - offsetWidth;
                }
                final int top = DOM.getAbsoluteTop(tabBarMenuBtn.getElement())
                    + box[1];
                menu.setPopupPosition(left, top);
              }
            });
          }
        });

    layoutPanel.add(tabBarWrapper, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(navBar);
    layoutPanel.add(tabBarMenuBtn);

    DOM.setStyleAttribute(tabBarWrapper.getElement(), "overflow", "hidden");

    addStyleName(DEFAULT_STYLENAME);
  }

  public void addTabListener(TabListener listener) {
    tabBar.addTabListener(listener);
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

  public String getTabHTML(int tabIndex) {
    return tabBar.getTabHTML(tabIndex);
  }

  public void insertTab(String tabText, boolean asHTML, int beforeIndex) {
    tabBar.insertTab(tabText, asHTML, beforeIndex);
  }

  public void insertTab(Widget tabWidget, int beforeIndex) {
    tabBar.insertTab(tabWidget, beforeIndex);
  }

  public boolean isAnimationEnabled() {
    return isAnimationEnabled;
  }

  @Override
  public void layout() {
    layout(false);
  }

  @Override
  public void layout(boolean invalidate) {
    super.layout(invalidate);

    if (tabBar.getOffsetWidth() > tabBarWrapper.getOffsetWidth()) {
      if (!navBar.isVisible()) {
        navBar.setVisible(true);
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            layout(true);
          }
        });
      } else {
        final int scrollLeft = tabBarWrapper.getElement().getScrollLeft();
        if (tabBarWrapper.getOffsetWidth() > tabBar.getOffsetWidth()
            - scrollLeft) {
          tabBarWrapper.getElement().setScrollLeft(
              Math.min(scrollLeft, tabBar.getOffsetWidth() - scrollLeft + navBar.getOffsetWidth()));
          DeferredCommand.addCommand(new Command() {
            public void execute() {
              layout(true);
            }
          });
        }
      }
    } else if (tabBar.getOffsetWidth() <= tabBarWrapper.getOffsetWidth()
        + navBar.getOffsetWidth()
        && navBar.isVisible()) {
      tabBarWrapper.getElement().setScrollLeft(0);
      navBar.setVisible(false);
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          layout(true);
        }
      });
    }

    if (navBar.isVisible()) {
      updateNavBarState();
    }
  }

  public void removeTab(int index) {
    tabBar.removeTab(index);
  }

  public void selectTab(int i) {
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
