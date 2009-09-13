/*
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

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Point;
import org.gwt.mosaic.core.client.Rectangle;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.DesktopPanel.DirectionConstant;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.AbstractWindowClosingEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasCaption;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * A {@code WindowPanel} is a {@code DecoratedPopupPanel} that has a caption
 * area at the top and can be dragged and resized by the user. The default
 * layout for a {@code WindowPanel} is {@link FillLayout}.
 * <p>
 * Example:
 * 
 * <pre>
 * WindowPanel windowPanel = new WindowPanel(&quot;CaptionText&quot;);
 * windowPanel.add(new Button(&quot;Click me!&quot;));
 * windowPanel.show();
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
@SuppressWarnings("deprecation")
public class WindowPanel extends DecoratedLayoutPopupPanel implements
    HasBeforeSelectionHandlers<WindowPanel>, HasSelectionHandlers<WindowPanel>,
    HasOpenHandlers<WindowPanel>, HasCaption, CoreConstants {

  /**
   * Double click caption action.
   */
  public enum CaptionAction {
    COLLAPSE, MAXIMIZE, NONE
  }

  /**
   * Represents how the window panel appears on the page.
   */
  public enum WindowState {
    MAXIMIZED, MINIMIZED, NORMAL
  }

  /**
   * Event listener for a change in the window state.
   */
  public interface WindowStateListener {
    void onWindowStateChange(WindowPanel sender, WindowState oldWindowState,
        WindowState newWindowState);
  }

  private class WindowHandlers extends HandlerManager implements
      HasResizeHandlers, HasHandlers {

    public WindowHandlers() {
      super(null);
    }

    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
      return addHandler(ResizeEvent.getType(), handler);
    }

    public HandlerManager getHandler() {
      return this;
    }
  }

  final class ElementDragHandle extends Widget implements HasAllMouseHandlers,
      HasClickHandlers {

    public ElementDragHandle(Element elem) {
      setElement(elem);
      onAttach();
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
      return addDomHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
      return addDomHandler(handler, MouseDownEvent.getType());
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
      return addDomHandler(handler, MouseMoveEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
      return addDomHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
      return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
      return addDomHandler(handler, MouseUpEvent.getType());
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
      return addDomHandler(handler, MouseWheelEvent.getType());
    }
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-WindowPanel";

  // (ggeorg) Issue 51
  private static final Image image = Caption.IMAGES.windowClose().createImage();

  private static final int Z_INDEX_BASE = 10000;

  private static final int Z_INDEX_MODAL_OFFSET = 1000;

  private Point beforeHidePopupPosition;

  private ClickHandler bringToFontClickHandler = new ClickHandler() {
    public void onClick(ClickEvent event) {
      if (isShowing() && !isActive()) {
        toFront();
      }
    }
  };

  /**
   * Double click caption action.
   */
  private CaptionAction captionAction = CaptionAction.COLLAPSE;

  private CollapsedListenerCollection collapsedListeners;

  private int contentWidth, contentHeight;

  private DesktopPanel desktopPanel;

  private HandlerRegistration desktopPanelCloseHandler;

  private HandlerRegistration desktopPanelOpenHandler;

  private HandlerRegistration desktopPanelSelectionHandler;

  private boolean fireWindowCloseEvents = true;

  private WindowHandlers handlers;

  private boolean hideContentsOnMove = true;

  private final Timer layoutTimer = new Timer() {
    public void run() {
      layout();
      fireResizedImpl();
    }
  };

  final private Timer maximizeTimer = new Timer() {
    public void run() {
      setWindowState(WindowState.MAXIMIZED);
    }
  };

  final private Timer minimizeTimer = new Timer() {
    public void run() {
      setWindowState(WindowState.MINIMIZED);
    }
  };

  private boolean modal;

  private Rectangle normalBounds = new Rectangle();

  private WindowState normalWindowState;

  private CaptionLayoutPanel panel;

  private boolean resizable;

  private WindowState windowState = WindowState.NORMAL;

  private List<WindowStateListener> windowStateListeners;

  GlassPanel glassPanel;

  ElementDragHandle nwResizeHandle, nResizeHandle, neResizeHandle;

  ElementDragHandle swResizeHandle, sResizeHandle, seResizeHandle;

  ElementDragHandle wResizeHandle, eResizeHandle;

  /**
   * Creates a new empty window with default layout.
   */
  public WindowPanel() {
    this(null);
  }
  
  /**
   * Creates a new empty window with the specified caption and default layout.
   * 
   * @param caption the caption of the window
   */
  public WindowPanel(String caption) {
    this(caption, true, false);
  }
  
  /**
   * Creates a new empty window with the specified caption and default layout.
   *  
   * @param caption the caption of the window
   * @param resizable
   */
  public WindowPanel(String caption, boolean resizable) {
    this(caption, resizable, false);
  }
  
  /**
   * Creates a new empty window with default layout.
   * 
   * @param caption the caption of the window
   * @param resizable
   * @param autoHide
   */
  protected WindowPanel(String caption, boolean resizable, boolean autoHide) {
    this(RootPanel.get(), caption, resizable, autoHide);
  }

  /**
   * Creates a new empty window with default layout.
   * 
   * @param boundaryPanel
   * @param caption the caption of the window
   * @param resizable
   * @param autoHide
   */
  protected WindowPanel(AbsolutePanel boundaryPanel, String caption,
      boolean resizable, boolean autoHide) {
    this(DesktopPanel.get(boundaryPanel == null ? RootPanel.get()
        : boundaryPanel), caption, resizable, autoHide);
  }

  public WindowPanel(DesktopPanel desktopPanel, String caption,
      boolean resizable, boolean autoHide) {
    super(autoHide);

    this.resizable = resizable;

    this.desktopPanel = desktopPanel;

    panel = new CaptionLayoutPanel(caption);
    panel.addCollapsedListener(new CollapsedListener() {
      public void onCollapsedChange(Widget sender) {
        fireCollapsedChange(WindowPanel.this);
      }
    });

    // (ggeorg) Issue 51
    final ImageButton closeBtn = new ImageButton(new Image(image.getUrl(),
        image.getOriginLeft(), image.getOriginTop(), image.getWidth(),
        image.getHeight()));
    closeBtn.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        hide();
      }
    });
    panel.getHeader().add(closeBtn, CaptionRegion.RIGHT);

    panel.getHeader().addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        if (captionAction == CaptionAction.COLLAPSE) {
          setCollapsed(!isCollapsed());
        } else if (captionAction == CaptionAction.MAXIMIZE) {
          if (getWindowState() == WindowState.MAXIMIZED) {
            setWindowState(WindowState.NORMAL);
          } else {
            setWindowState(WindowState.MAXIMIZED);
          }
        }
      }
    });

    panel.getHeader().addClickHandler(bringToFontClickHandler);

    super.setWidget(panel);

    addStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Adds a {@code BeforeSelectionHandler} handler.
   * <p>
   * This handler is invoked before a {@code WindowPanel} is selected
   * (activated).
   * 
   * @param handler the handler
   * @return the handler registration
   */
  public HandlerRegistration addBeforeSelectionHandler(
      BeforeSelectionHandler<WindowPanel> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  public void addCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners == null) {
      collapsedListeners = new CollapsedListenerCollection();
    }
    collapsedListeners.add(listener);
  }

  public HandlerRegistration addOpenHandler(OpenHandler<WindowPanel> handler) {
    return addHandler(handler, OpenEvent.getType());
  }

  /**
   * Adds a {@link ResizeEvent} handler.
   * 
   * @param handler the handler
   * @return {@link HandlerRegistration} used to remove the handler
   */
  public HandlerRegistration addResizeHandler(ResizeHandler handler) {
    return addHandler(ResizeEvent.getType(), handler);
  }

  /**
   * Adds a {@code SelectionHandler} handler.
   * 
   * <p>
   * This handler is invoked when a {@code WindowPanel} is selected (activated).
   * 
   * @param handler the handler
   * @return the handler registration
   */
  public HandlerRegistration addSelectionHandler(
      SelectionHandler<WindowPanel> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  /**
   * Adds a listener to receive window closing events.
   * 
   * @param listener the listener to be informed when the window panel is
   *          closing
   */
  @Deprecated
  public void addWindowCloseListener(WindowCloseListener listener) {
    ListenerWrapper.WrapWindowPanelClose.add(this, listener);
  }

  /**
   * Adds a {@code Window.ClosingEvent} handler.
   * 
   * @param handler the handler
   * @return the handler registration
   */
  public HandlerRegistration addWindowClosingHandler(ClosingHandler handler) {
    return addHandler(handler, AbstractWindowClosingEvent.getType());
  }

  /**
   * Adds a listener to receive window resize events.
   * 
   * @param listener the listener to be informed when the window panel is
   *          resized
   * @deprecated use {@link #addResizeHandler(ResizeHandler)} instead
   */
  @Deprecated
  public void addWindowResizeListener(WindowResizeListener listener) {
    ListenerWrapper.WrapWindowPanelResize.add(this, listener);
  }

  public void addWindowStateListener(WindowStateListener listener) {
    if (windowStateListeners == null) {
      windowStateListeners = new ArrayList<WindowStateListener>();
    }
    windowStateListeners.add(listener);
  }

  /**
   * Centers the {@code WindowPanel} in the browser window and shows it (
   * centers the popup in the browser window by adding it to the {@code
   * RootPanel}). The {@link #layout()} is called after the {@code WindowPanel}
   * is attached (in {@link #onLoad()}). If the {@code WindowPanel} was already
   * showing, then the {@code WindowPanel} is centered. If the {@code
   * WindowPanel} is set to not visible by calling {@link #setVisible(boolean)}
   * before {@link #show()} the {@code WindowPanel} will be attached and visible
   * (not like {@link #show()}).
   * 
   * @see #show()
   */
  public void center() {
    try {
      fireWindowCloseEvents = false;
      super.center();
    } finally {
      fireWindowCloseEvents = true;
    }
  }

  public void delayedLayout(int delayMillis) {
    layoutTimer.schedule(delayMillis);
  }

  /**
   * Gets the caption of the {@code WindowPanel}. The caption is displayed in
   * the {@code WindowPanel}'s frame.
   * 
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasCaption#getCaption()
   * @see #setCaption(String)
   */
  public String getCaption() {
    return panel.getHeader().getText();
  }

  public CaptionAction getCaptionAction() {
    return captionAction;
  }

  public int getContentHeight() {
    return contentHeight;
  }

  public Dimension getContentSize() {
    return new Dimension(contentWidth, contentHeight);
  }

  public int getContentWidth() {
    return contentWidth;
  }

  /**
   * Convenience method to get the {@link DesktopPanel} this {@code WindowPanel}
   * belongs to.
   * 
   * @return the the {@link DesktopPanel} this {@code WindowPanel} belongs to
   */
  public DesktopPanel getDesktopPanel() {
    return desktopPanel;
  }

  public Widget getFooter() {
    return panel.getFooter();
  }

  public Caption getHeader() {
    return panel.getHeader();
  }

  public Rectangle getNormalBounds() {
    return normalBounds;
  }

  public WindowState getNormalWindowState() {
    return normalWindowState;
  }

  /**
   * Gets the popup's left position relative to the browser's client area.
   * 
   * @return the popup's left position
   */
  @Override
  public int getPopupLeft() {
    final DesktopPanel boundaryPanel = getDesktopPanel();
    if (boundaryPanel == null) {
      return DOM.getAbsoluteLeft(getElement());
    } else {
      return boundaryPanel.getPopupPosition(this).x;
    }
  }

  /**
   * Gets the popup's top position relative to the browser's client area.
   * 
   * @return the popup's top position
   */
  @Override
  public int getPopupTop() {
    final DesktopPanel boundaryPanel = getDesktopPanel();
    if (boundaryPanel == null) {
      return DOM.getAbsoluteTop(getElement());
    } else {
      return boundaryPanel.getPopupPosition(this).y;
    }
  }

  @Override
  public Widget getWidget() {
    if (panel.getWidgetCount() > 0) {
      return panel.getWidget(0);
    } else {
      return null;
    }
  }

  /**
   * Gets the flag that determines the state of the window panel. Default is
   * 'NORMAL' (ie not MINIMIZED or MAXIMIZED).
   */
  public WindowState getWindowState() {
    return windowState;
  }

  /**
   * Hides the {@code WindowPanel} (hides the popup by removing it from the
   * {@code RootPanel}). This has no effect if it is not currently visible.
   * 
   * @param autoHide the value that will be passed to
   *          {@link PopupListener#onPopupClosed(PopupPanel, boolean)} when the
   *          popup is closed
   * 
   * @see #hide()
   */
  @Override
  public void hide(boolean autoHide) {
    if (!fireWindowCloseEvents) {
      super.hide(autoHide);
      return;
    }
    final String msg = fireClosingImpl();
    if (msg != null && !Window.confirm(msg)) {
      return;
    }

    if (isResizable()) {
      getDesktopPanel().makeNotResizable(this);
    }

    getDesktopPanel().makeNotDraggable(this);

    // restore popup position
    beforeHidePopupPosition = getDesktopPanel().getPopupPosition(this);

    try {
      super.hide(autoHide);
    } finally {
      removeDesktopPanelHandlers();
      if (modal && glassPanel != null) {
        glassPanel.removeFromParent();
      }
      if (modal) {
        modal = false;
      }
    }
  }

  /**
   * Returns whether the {@code WindowPanel} is the currently "selected" or
   * active {@code WindowPanel}.
   * 
   * @return {@code true} if this {@code WindowPanel} is currently selected
   *         (active)
   * @see #toFront()
   * @see #toBack()
   */
  public boolean isActive() {
    return getDesktopPanel().isActive(this);
  }

  public boolean isCollapsed() {
    return panel.isCollapsed();
  }

  public boolean isHideContentOnMove() {
    return hideContentsOnMove;
  }

  /**
   * Indicates whether the {@code WindowPanel} is modal.
   * 
   * @return {@code true} if this {@code WindowPanel} is modal; {@code false}
   *         otherwise
   * 
   * @see #showModal()
   */
  public boolean isModal() {
    return modal;
  }

  /**
   * Indicates whether this {@code WindowPanel} is resizable by the user. By
   * default, all {@code WindowPanels} are initially resizable.
   * 
   * @return {@code true} if the user can resize this {@code WindowPanel};
   *         {@code false} otherwise.
   * @see #setResizable(boolean)
   */
  public boolean isResizable() {
    return resizable;
  }

  public void moveBy(int right, int down) {
    AbsolutePanel parent = (AbsolutePanel) getParent();
    Location location = new WidgetLocation(this, parent);
    int left = location.getLeft() + right;
    int top = location.getTop() + down;
    parent.setWidgetPosition(this, left, top);
  }

  public void removeCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners != null) {
      collapsedListeners.remove(listener);
    }
  }

  /**
   * Removes a window closing listener.
   * 
   * @param listener the listener to be removed
   */
  @Deprecated
  public void removeWindowCloseListener(WindowCloseListener listener) {
    ListenerWrapper.WrapWindowPanelClose.remove(handlers, listener);
  }

  /**
   * Removes a window panel resize listener.
   * 
   * @param listener the listener to be removed
   */
  @Deprecated
  public void removeWindowResizeListener(WindowResizeListener listener) {
    ListenerWrapper.WrapWindowPanelResize.remove(getHandlers(), listener);
  }

  public void removeWindowStateListener(WindowStateListener listener) {
    if (windowStateListeners != null) {
      windowStateListeners.remove(listener);
    }
  }

  /**
   * Sets the caption for this {@code WindowPanel} to the specified string.
   * 
   * {@inheritDoc}
   * 
   * @param text the caption to be displayed in the {@code WindowPanel}'s
   *          border. A {@code null} value is treated as an empty string, "".
   * 
   * @see com.google.gwt.user.client.ui.HasCaption#setCaption(java.lang.String)
   * @see #getCaption()
   */
  public void setCaption(final String text) {
    panel.getHeader().setText(text);
  }

  public void setCaptionAction(CaptionAction captionAction) {
    this.captionAction = captionAction;
  }

  public void setCollapsed(boolean collapsed) {
    if (collapsed == isCollapsed()) {
      return;
    }

    if (!isAttached()) {
      panel.setCollapsed(true);
      return;
    }

    if (collapsed) {
      if (getWindowState() != WindowState.MAXIMIZED) {
        final Dimension box = DOM.getClientSize(getElement());
        final Dimension size2 = WidgetHelper.getOffsetSize(WindowPanel.this);
        final Dimension size3 = WidgetHelper.getOffsetSize(getLayoutPanel());
        normalBounds.width = box.width - (size2.width - size3.width);
        normalBounds.height = box.height - (size2.height - size3.height);
      }
      panel.setCollapsed(true);

      final int width = getLayoutPanel().getOffsetWidth();
      setContentSize(width, getLayoutPanel().getPreferredSize().height);
      if (isResizable() && windowState != WindowState.MAXIMIZED) {
        getDesktopPanel().makeNotResizable(this);
      }
    } else {
      panel.setCollapsed(false);

      if (getWindowState() != WindowState.MAXIMIZED) {
        setContentSize(normalBounds.width, normalBounds.height);
      } else {
        WidgetHelper.setSize(this,
            DOM.getClientSize(getDesktopPanel().getElement()));
      }

      if (isResizable() && windowState != WindowState.MAXIMIZED) {
        getDesktopPanel().makeResizable(this);
      }
    }

    layout();
  }

  @Override
  public void setContentSize(Dimension d) {
    if (isResizable()) {
      if (d.width >= 0) {
        contentWidth = d.width;
      }
      if (d.height >= 0) {
        contentHeight = d.height;
      }
    }
    super.setContentSize(d);
  }

  public void setFooter(Widget footer) {
    if (getFooter() != null) {
      getFooter().removeStyleName("Footer");
    }
    panel.setFooter(footer);
    if (getFooter() != null) {
      getFooter().addStyleName("Footer");
    }
  }

  public void setHideContentOnMove(boolean hideContents) {
    this.hideContentsOnMove = hideContents;
  }

  /**
   * Sets the normal bounds for this {@code WindowPanel}, the bounds that this
   * {@code WindowPanel} would be restored to from its maximized state.
   * <p>
   * This method is intended for use only by desktop managers.
   * 
   * @param r the bounds that this internal frame should be restored to
   */
  public void setNormalBounds(Rectangle r) {
    assert (r != null);
    normalBounds = r;
  }

  public void setNormalWindowState(WindowState windowState) {
    normalWindowState = windowState;
  }

  @Override
  public void setPopupPosition(int left, int top) {
    final DesktopPanel boundaryPanel = getDesktopPanel();
    if (boundaryPanel == null) {
      super.setPopupPosition(left, top);
    } else {
      final int[] borders = DOM.getBorderSizes(boundaryPanel.getElement());
      super.setPopupPosition(left
          + (boundaryPanel.getAbsoluteLeft() + borders[3]), top
          + (boundaryPanel.getAbsoluteTop() + borders[0]));
    }
  }

  /**
   * Sets whether this {@code WindowPanel} is resizable by the user.
   * 
   * @param resizable {@code true} if this {@code WindowPanel} is resizable;
   *          {@code false} otherwise.
   * @see #isResizable()
   */
  public void setResizable(boolean resizable) {
    if (this.resizable == resizable) {
      return;
    }
    this.resizable = resizable;
    if (isShowing()) {
      if (resizable) {
        getDesktopPanel().makeResizable(this);
      } else {
        getDesktopPanel().makeNotResizable(this);
      }
    }
  }

  @Override
  public void setWidget(Widget w) {
    panel.clear();
    panel.add(w);
  }

  /**
   * Sets the flag to determine the state of the window panel. Default is
   * 'NORMAL' (ie not MINIMIZED or MAXIMIZED).
   * 
   * @param windowState the flag to determine the state of the window panel
   */
  public void setWindowState(WindowState windowState) {
    if (this.windowState != windowState) {
      WindowState oldWindowState = this.windowState;
      this.windowState = windowState;

      if (isAttached()) {
        if (oldWindowState == WindowState.MINIMIZED) {
          this.windowState = normalWindowState;
        }

        // if (this.windowState == WindowState.MAXIMIZED) {
        // windowResizeHandler.addResizeHandler();
        // } else {
        // windowResizeHandler.removeResizeHandler();
        // }
      }

      fireWindowStateChangeImpl(oldWindowState, windowState);
    }
  }

  /**
   * Makes the {@code WindowPanel} visible (shows the popup by adding it to the
   * {@code RootPanel}). The {@link #layout()} is called after the {@code
   * WindowPanel} is attached (in {@link #onLoad()}). If the {@code WindowPanel}
   * is already attached, this will bring the {@code WindowPanel} to the front.
   * If the {@code WindowPanel} is set to not visible by calling
   * {@link #setVisible(boolean)} before {@link #show()} the {@code WindowPanel}
   * will be attached but not visible.
   * 
   * @see #center()
   * @see #pack()
   * @see #setPopupPositionAndShow(com.google.gwt.user.client.ui.PopupPanel.PositionCallback)
   * @see #showModal()
   */
  @Override
  public void show() {
    if (isAttached()) {
      return;
    }

    addDesktopPanelHandlers();

    OpenEvent.fire(this, this);

    if (isResizable()) {
      getDesktopPanel().makeResizable(this);
    }

    getDesktopPanel().makeDraggable(this);

    if (modal) {
      if (glassPanel == null) {
        glassPanel = new GlassPanel(false);
        glassPanel.addStyleName("mosaic-GlassPanel-default");
        String zIndex = DOM.getComputedStyleAttribute(
            WindowPanel.this.getElement(), "zIndex");
        if (zIndex != null) {
          DOM.setStyleAttribute(glassPanel.getElement(), "zIndex", zIndex);
        }
      }
      getDesktopPanel().add(glassPanel);

      // new DelayedRunnable() {
      // @Override
      // public void run() {
      // WindowPanel.super.show();
      // }
      // };
    }

    if (beforeHidePopupPosition != null) {
      super.setPopupPosition(beforeHidePopupPosition.x,
          beforeHidePopupPosition.y);
      beforeHidePopupPosition = null;
    }

    super.show();

    toFront();
  }

  public void showModal() {
    showModal(true);
  }

  /**
   * Centers the {@code WindowPanel} in the browser window and shows it modal
   * (centers the popup in the browser window by adding it to the {@code
   * RootPanel} and displays a {@code
   * com.google.gwt.widgetideas.client.GlassPanel} under it). The
   * {@link #layout()} is called after the {@code WindowPanel} is attached (in
   * {@link #onLoad()}). If the {@code WindowPanel} is already attached, then
   * the {@code WindowPanel} is centered. If the {@code WindowPanel} is set to
   * not visible by calling {@link #setVisible(boolean)} before {@link #show()}
   * the {@code WindowPanel} will be attached and visible (not like
   * {@link #show()}).
   * 
   * @see #center()
   * @see #isModal()
   */
  public void showModal(boolean doPack) {
    modal = true;
    if (doPack) {
      pack();
    }
    // DeferredCommand.addCommand(new Command() {
    // public void execute() {
    center();
    toFront();
    // }
    // });
  }

  /**
   * If this {@code WindowPanel} is visible, sends this {@code WindowPanel} to
   * the back.
   * <p>
   * Places this {@code WindowPanel} at the bottom of the stacking order and
   * shows it behind any other {@code WindowPanels}.
   * 
   * @see #toFront()
   */
  public void toBack() {
    getDesktopPanel().toBack(this);
  }

  /**
   * If this {@code WindowPanel} is visible, brings this {@code WindowPanel} to
   * the front.
   * <p>
   * Places this {@code WindowPanel} at the top of the stacking order and shows
   * it in front of any other {@code WindowPanels}.
   * 
   * @see #toBack()
   */
  public void toFront() {
    if (isActive()) {
      return;
    }

    BeforeSelectionEvent<WindowPanel> event = BeforeSelectionEvent.fire(this,
        this);
    if (event != null && event.isCanceled()) {
      return;
    }

    SelectionEvent.fire(this, this);
  }

  /**
   * Adds this handler to the window panel.
   * 
   * @param <H> the type of handler to add
   * @param type the event type
   * @param handler the handler
   * @return {@link HandlerRegistration} used to remove the handler
   */
  private <H extends EventHandler> HandlerRegistration addHandler(
      GwtEvent.Type<H> type, final H handler) {
    return getHandlers().addHandler(type, handler);
  }

  private String fireClosingImpl() {
    // if (closeHandlerInitialized) {
    ClosingEvent event = new ClosingEvent();
    fireEvent(event);
    return event.getMessage();
    // }
    // return null;
  }

  private void fireCollapsedChange(Widget sender) {
    if (collapsedListeners != null) {
      collapsedListeners.fireCollapsedChange(sender);
    }
  }

  private void fireResizedImpl() {
    ResizeEvent.fire(getHandlers(), contentWidth, contentHeight);
  }

  private void fireWindowStateChangeImpl(WindowState oldWindowState,
      WindowState newWindowState) {
    if (windowStateListeners != null) {
      for (WindowStateListener listener : windowStateListeners) {
        listener.onWindowStateChange(this, oldWindowState, newWindowState);
      }
    }
  }

  private WindowHandlers getHandlers() {
    if (handlers == null) {
      handlers = new WindowHandlers();
    }
    return handlers;
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (windowState == WindowState.MAXIMIZED) {
          maximizeTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
        } else if (windowState == WindowState.MINIMIZED) {
          minimizeTimer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
        }
      }
    });
  }
  void addDesktopPanelHandlers() {
    desktopPanelOpenHandler = addOpenHandler(desktopPanel);
    desktopPanelCloseHandler = addCloseHandler(desktopPanel);
    desktopPanelSelectionHandler = addSelectionHandler(desktopPanel);
    addWindowStateListener(desktopPanel);
  }
  void hideContent(boolean hideContent) {
    panel.hideContent(hideContent);
  }

  ElementDragHandle newResizeHandle(int row, int col,
      DirectionConstant direction) {
    final Element td = getCellElement(row, col).getParentElement().cast();
    final ElementDragHandle widget = new ElementDragHandle(td);

    adopt(widget);

    widget.addClickHandler(bringToFontClickHandler);

    return widget;
  }

  void removeDesktopPanelHandlers() {
    desktopPanelOpenHandler.removeHandler();
    desktopPanelCloseHandler.removeHandler();
    desktopPanelSelectionHandler.removeHandler();
    removeWindowStateListener(desktopPanel);
  }

  void setZIndex(int zIndexOffset) {
    int zIndex = (zIndexOffset + Z_INDEX_BASE);
    if (modal) {
      zIndex += Z_INDEX_MODAL_OFFSET;
    }
    DOM.setStyleAttribute(getElement(), "zIndex", Integer.toString(zIndex));
    if (glassPanel != null && glassPanel.isAttached()) {
      DOM.setStyleAttribute(glassPanel.getElement(), "zIndex",
          Integer.toString(zIndex));
    }
  }

}
