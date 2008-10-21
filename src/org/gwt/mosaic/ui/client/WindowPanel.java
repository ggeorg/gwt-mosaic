/*
 * Copyright 2008 Google Inc.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasCaption;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class WindowPanel extends DecoratedPopupPanel implements HasCaption,
    HasLayoutManager {

  /**
   * WindowPanel direction constant, used in {@link ResizeDragController}.
   */
  static final class DirectionConstant {

    final int directionBits;

    final String directionLetters;

    private DirectionConstant(int directionBits, String directionLetters) {
      this.directionBits = directionBits;
      this.directionLetters = directionLetters;
    }
  }

  class ElementDragHandle extends Widget implements SourcesMouseEvents {
    private MouseListenerCollection mouseListeners;

    public ElementDragHandle(Element elem) {
      setElement(elem);
      sinkEvents(Event.MOUSEEVENTS);
    }

    public void addMouseListener(MouseListener listener) {
      if (mouseListeners == null) {
        mouseListeners = new MouseListenerCollection();
      }
      mouseListeners.add(listener);
    }

    protected void onAttach() {
      super.onAttach();
    }

    @Override
    public void onBrowserEvent(Event event) {
      switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
          if (!isActive()) {
            bringToFront();
          }
        case Event.ONMOUSEUP:
        case Event.ONMOUSEMOVE:
        case Event.ONMOUSEOVER:
        case Event.ONMOUSEOUT:
          if (mouseListeners != null) {
            mouseListeners.fireMouseEvent(this, event);
          }
          break;
      }
    }

    protected void onDetach() {
      super.onDetach();
    }

    public void removeMouseListener(MouseListener listener) {
      if (mouseListeners != null) {
        mouseListeners.remove(listener);
      }
    }
  }

  final class MoveDragController extends AbstractDragController {

    private int boundaryOffsetX;

    private int boundaryOffsetY;

    private int dropTargetClientHeight;

    private int dropTargetClientWidth;

    public MoveDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel);
    }

    @Override
    public void dragEnd() {
      super.dragEnd();
      if (!modal) {
        glassPanel.removeFromParent();
      }
      panel.hideContents(false);
    }

    public void dragMove() {
      int desiredLeft = context.desiredDraggableX;
      int desiredTop = context.desiredDraggableY;
      if (getBehaviorConstrainedToBoundaryPanel()) {
        desiredLeft = Math.max(boundaryOffsetX, Math.min(desiredLeft,
            dropTargetClientWidth));
        desiredTop = Math.max(boundaryOffsetY, Math.min(desiredTop,
            dropTargetClientHeight));
      }

      DOMUtil.fastSetElementPosition(context.draggable.getElement(),
          desiredLeft, desiredTop);
    }

    @Override
    public void dragStart() {
      if (!isActive()) {
        bringToFront();
      }
      panel.hideContents(true);
      if (!modal) {
        if (glassPanel == null) {
          glassPanel = new GlassPanel(false);
          glassPanel.addStyleName("mosaic-GlassPanel-invisible");
          DOM.setStyleAttribute(glassPanel.getElement(), "zIndex",
              DOM.getStyleAttribute(WindowPanel.this.getElement(), "zIndex"));
        }
        getBoundaryPanel().add(glassPanel, 0, 0);
      }
      super.dragStart();

      // one time calculation of boundary panel location for efficiency during
      // dragging
      Location widgetLocation = new WidgetLocation(context.boundaryPanel, null);
      boundaryOffsetX = widgetLocation.getLeft()
          + DOMUtil.getBorderLeft(context.boundaryPanel.getElement());
      boundaryOffsetY = widgetLocation.getTop()
          + DOMUtil.getBorderTop(context.boundaryPanel.getElement());

      dropTargetClientWidth = boundaryOffsetX
          + DOMUtil.getClientWidth(context.boundaryPanel.getElement())
          - context.draggable.getOffsetWidth();
      dropTargetClientHeight = boundaryOffsetY
          + DOMUtil.getClientHeight(context.boundaryPanel.getElement())
          - context.draggable.getOffsetHeight();
    }
  }

  final class ResizeDragController extends AbstractDragController {

    private static final int MIN_WIDGET_SIZE = 96;

    private Map<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    private WindowPanel windowPanel = null;

    private int boundaryOffsetX;

    private int boundaryOffsetY;

    private int dropTargetClientHeight;

    private int dropTargetClientWidth;

    public ResizeDragController(AbsolutePanel boundaryPanel,
        WindowPanel windowPanel) {
      super(boundaryPanel);
      this.windowPanel = windowPanel;
    }

    @Override
    public void dragEnd() {
      super.dragEnd();
      if (!modal) {
        glassPanel.removeFromParent();
      }
      panel.hideContents(false);
      setContentSize(contentWidth, contentHeight);
    }

    public void dragMove() {
      int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & WindowPanel.DIRECTION_NORTH) != 0) {
        final int delta = getBehaviorConstrainedToBoundaryPanel()
            ? context.draggable.getAbsoluteTop()
                - Math.max(context.desiredDraggableY, boundaryOffsetY)
            : context.draggable.getAbsoluteTop() - context.desiredDraggableY;
        if (delta != 0) {
          int contentHeight = windowPanel.getContentHeight();
          int newHeight = Math.max(contentHeight + delta,
              windowPanel.panel.getHeader().getOffsetHeight());
          if (newHeight != contentHeight) {
            windowPanel.moveBy(0, contentHeight - newHeight);
          }
          windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
        }
      } else if ((direction & WindowPanel.DIRECTION_SOUTH) != 0) {
        final int delta = getBehaviorConstrainedToBoundaryPanel() ? Math.min(
            context.desiredDraggableY, dropTargetClientHeight)
            - context.draggable.getAbsoluteTop() : context.desiredDraggableY
            - context.draggable.getAbsoluteTop();
        if (delta != 0) {
          int contentHeight = windowPanel.getContentHeight();
          int newHeight = Math.max(contentHeight + delta,
              windowPanel.panel.getHeader().getOffsetHeight());
          windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
        }
      }
      if ((direction & WindowPanel.DIRECTION_WEST) != 0) {
        int delta = getBehaviorConstrainedToBoundaryPanel()
            ? context.draggable.getAbsoluteLeft()
                - Math.max(context.desiredDraggableX, boundaryOffsetX)
            : context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
        if (delta != 0) {
          int contentWidth = windowPanel.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          if (newWidth != contentWidth) {
            windowPanel.moveBy(contentWidth - newWidth, 0);
          }
          windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
        }
      } else if ((direction & WindowPanel.DIRECTION_EAST) != 0) {
        int delta = getBehaviorConstrainedToBoundaryPanel() ? Math.min(
            context.desiredDraggableX, dropTargetClientWidth)
            - context.draggable.getAbsoluteLeft() : context.desiredDraggableX
            - context.draggable.getAbsoluteLeft();
        if (delta != 0) {
          int contentWidth = windowPanel.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
        }
      }

    }

    @Override
    public void dragStart() {
      panel.hideContents(true);
      if (!modal) {
        if (glassPanel == null) {
          glassPanel = new GlassPanel(false);
          glassPanel.addStyleName("mosaic-GlassPanel-invisible");
          final int zIndex = DOM.getIntStyleAttribute(
              WindowPanel.this.getElement(), "zIndex");
          DOM.setIntStyleAttribute(glassPanel.getElement(), "zIndex",
              zIndex - 1);
        }
        getBoundaryPanel().add(glassPanel, 0, 0);
      }
      super.dragStart();

      // one timecalculation of boundary panel location for efficiency during
      // dragging
      Location widgetLocation = new WidgetLocation(context.boundaryPanel, null);
      boundaryOffsetX = widgetLocation.getLeft()
          + DOMUtil.getBorderLeft(context.boundaryPanel.getElement());
      boundaryOffsetY = widgetLocation.getTop()
          + DOMUtil.getBorderTop(context.boundaryPanel.getElement());

      dropTargetClientWidth = boundaryOffsetX
          + DOMUtil.getClientWidth(context.boundaryPanel.getElement())
          - context.draggable.getOffsetWidth();
      dropTargetClientHeight = boundaryOffsetY
          + DOMUtil.getClientHeight(context.boundaryPanel.getElement())
          - context.draggable.getOffsetHeight();
    }

    private DirectionConstant getDirection(Widget draggable) {
      return directionMap.get(draggable);
    }

    public void makeDraggable(Widget widget,
        WindowPanel.DirectionConstant direction) {
      super.makeDraggable(widget);
      directionMap.put(widget, direction);
    }

    protected BoundaryDropController newBoundaryDropController(
        AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
      if (allowDroppingOnBoundaryPanel) {
        throw new IllegalArgumentException();
      }
      return new BoundaryDropController(boundaryPanel, false);
    }

  }

  final class WindowController {

    private final AbsolutePanel boundaryPanel;

    private final MoveDragController moveDragController;

    private final ResizeDragController resizeDragController;

    WindowController(AbsolutePanel boundaryPanel, WindowPanel windowPanel) {
      this.boundaryPanel = boundaryPanel;

      moveDragController = new MoveDragController(boundaryPanel);
      moveDragController.setBehaviorConstrainedToBoundaryPanel(true);
      // moveDragController.setBehaviorDragProxy(true);
      moveDragController.setBehaviorMultipleSelection(false);

      resizeDragController = new ResizeDragController(boundaryPanel,
          windowPanel);
      resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
      resizeDragController.setBehaviorMultipleSelection(false);
    }

    AbsolutePanel getBoundaryPanel() {
      return boundaryPanel;
    }

    public MoveDragController getMoveDragController() {
      return moveDragController;
    }

    public ResizeDragController getResizeDragController() {
      return resizeDragController;
    }

  }

  /**
   * The caption images to use.
   */
  public static final CaptionImages CAPTION_IMAGES = (CaptionImages) GWT.create(CaptionImages.class);

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-WindowPanel";

  /**
   * Specifies that resizing occur at the east edge.
   */
  static final int DIRECTION_EAST = 0x0001;

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final int DIRECTION_NORTH = 0x0002;

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final int DIRECTION_SOUTH = 0x0004;

  /**
   * Specifies that resizing occur at the west edge.
   */
  static final int DIRECTION_WEST = 0x0008;

  /**
   * Specifies that resizing occur at the east edge.
   */
  static final DirectionConstant EAST = new WindowPanel.DirectionConstant(
      DIRECTION_EAST, "e");

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH,
      "n");

  /**
   * Specifies that resizing occur at the north-east edge.
   */
  static final DirectionConstant NORTH_EAST = new DirectionConstant(
      DIRECTION_NORTH | DIRECTION_EAST, "ne");

  /**
   * Specifies that resizing occur at the north-west edge.
   */
  static final DirectionConstant NORTH_WEST = new DirectionConstant(
      DIRECTION_NORTH | DIRECTION_WEST, "nw");

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH,
      "s");

  /**
   * Specifies that resizing occur at the south-east edge.
   */
  static final DirectionConstant SOUTH_EAST = new DirectionConstant(
      DIRECTION_SOUTH | DIRECTION_EAST, "se");
  /**
   * Specifies that resizing occur at the south-west edge.
   */
  static final DirectionConstant SOUTH_WEST = new DirectionConstant(
      DIRECTION_SOUTH | DIRECTION_WEST, "sw");

  /**
   * Specifies that resizing occur at the west edge.
   */
  static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST,
      "w");

  private static final int Z_INDEX_BASE = 10000;
  private static final int Z_INDEX_MODAL_OFFSET = 1000;
  private static Vector<WindowPanel> windowPanelOrder = new Vector<WindowPanel>();

  private List<WindowCloseListener> closingListeners;
  private List<WindowResizeListener> resizeListeners;

  private ElementDragHandle nwResizeHandle, nResizeHandle, neResizeHandle;

  private ElementDragHandle swResizeHandle, sResizeHandle, seResizeHandle;

  private ElementDragHandle wResizeHandle, eResizeHandle;

  private int contentWidth, contentHeight;

  private final WindowController windowController;

  private final CaptionLayoutPanel panel;

  private final boolean resizable, modal;

  private boolean initialized;

  private final Timer layoutTimer = new Timer() {
    public void run() {
      layout();
      fireResizedImpl();
    }
  };

  private GlassPanel glassPanel;

  private String height = null;

  private String width = null;

  public WindowPanel() {
    this(null);
  }

  protected WindowPanel(AbsolutePanel boundaryPanel, String caption,
      boolean resizable, boolean autoHide, boolean modal) {
    super(autoHide, modal);

    this.resizable = resizable;
    this.modal = modal;

    final int order = windowPanelOrder.size();
    setWindowOrder(order);
    windowPanelOrder.add(this);

    windowController = new WindowController(boundaryPanel, this);

    if (isResizable()) {
      makeResizable();
    }

    panel = new CaptionLayoutPanel(caption);

    final ImageButton closeBtn = new ImageButton(CAPTION_IMAGES.windowClose());
    closeBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        hide();
      }
    });
    panel.getHeader().add(closeBtn, CaptionRegion.RIGHT);
    
    panel.getHeader().addDoubleClickListener(new DoubleClickListener() {
      public void onDoubleClick(Widget sender) {
        setCollapsed(!isCollapsed());
      }
    });

    windowController.getMoveDragController().makeDraggable(this,
        panel.getHeader());

    super.setWidget(panel);

    addStyleName(DEFAULT_STYLENAME);
  }

  public WindowPanel(String caption) {
    this(caption, true, false);
  }

  public WindowPanel(String caption, boolean resizable, boolean modal) {
    this(caption, resizable, false, modal);
  }

  protected WindowPanel(String caption, boolean resizable, boolean autoHide,
      boolean modal) {
    this(RootPanel.get(), caption, resizable, autoHide, modal);
  }

  /**
   * Adds a listener to receive window closing events.
   * 
   * @param listener the listener to be informed when the window panel is
   *          closing
   */
  public void addWindowCloseListener(WindowCloseListener listener) {
    if (closingListeners == null) {
      closingListeners = new ArrayList<WindowCloseListener>();
    }
    closingListeners.add(listener);
  }

  /**
   * Adds a listener to receive window resize events.
   * 
   * @param listener the listener to be informed when the window panel is
   *          resized
   */
  public void addWindowResizeListener(WindowResizeListener listener) {
    if (resizeListeners == null) {
      resizeListeners = new ArrayList<WindowResizeListener>();
    }
    resizeListeners.add(listener);
  }

  public void bringToFront() {
    int curIndex = windowPanelOrder.indexOf(this);
    if (curIndex + 1 < windowPanelOrder.size()) {
      windowPanelOrder.remove(this);
      windowPanelOrder.add(this);
      for (; curIndex < windowPanelOrder.size(); curIndex++) {
        windowPanelOrder.get(curIndex).setWindowOrder(curIndex);
      }
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();

    // See comment in doDetachChildren for an explanation of this call
    if (isResizable()) {
      nResizeHandle.onAttach();
      sResizeHandle.onAttach();
      wResizeHandle.onAttach();
      eResizeHandle.onAttach();
      nwResizeHandle.onAttach();
      neResizeHandle.onAttach();
      swResizeHandle.onAttach();
      seResizeHandle.onAttach();
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();

    // We need to detach the caption specifically because it is not part of the
    // iterator of Widgets that the {@link SimplePanel} super class returns.
    // This is similar to a {@link ComplexPanel}, but we do not want to expose
    // the caption widget, as its just an internal implementation.
    if (isResizable()) {
      nResizeHandle.onDetach();
      sResizeHandle.onDetach();
      wResizeHandle.onDetach();
      eResizeHandle.onDetach();
      nwResizeHandle.onDetach();
      neResizeHandle.onDetach();
      swResizeHandle.onDetach();
      seResizeHandle.onDetach();
    }
  }

  private void fireClosedImpl() {
    if (closingListeners != null) {
      for (WindowCloseListener listener : closingListeners) {
        listener.onWindowClosed();
      }
    }
  }

  private String fireClosingImpl() {
    String ret = null;
    if (closingListeners != null) {
      for (WindowCloseListener listener : closingListeners) {
        // If any listener wants to suppress the window closing event, then do
        // so.
        String msg = listener.onWindowClosing();
        if (ret == null) {
          ret = msg;
        }
      }
    }

    return ret;
  }

  private void fireResizedImpl() {
    if (resizeListeners != null) {
      for (WindowResizeListener listener : resizeListeners) {
        listener.onWindowResized(contentWidth, contentHeight);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasCaption#getCaption()
   */
  public String getCaption() {
    return panel.getHeader().getText();
  }

  public int getContentHeight() {
    return contentHeight;
  }

  public int getContentWidth() {
    return contentWidth;
  }

  public Widget getFooter() {
    return panel.getFooter();
  }

  public Caption getHeader() {
    return panel.getHeader();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return panel.getPreferredSize();
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
   * Hides the popup. This has no effect if it is not currently visible.
   * 
   * @param autoClosed the value that will be passed to
   *          {@link PopupListener#onPopupClosed(PopupPanel, boolean)} when the
   *          popup is closed
   */
  @Override
  public void hide(boolean autoHide) {
    if (fireClosingImpl() == null) {
      super.hide(autoHide);
      if (modal && glassPanel != null) {
        glassPanel.removeFromParent();
      }
      fireClosedImpl();
    } else {
      // TODO
    }
  }

  public boolean isActive() {
    return windowPanelOrder.lastElement().equals(this);
  }

  public boolean isModal() {
    return modal;
  }

  public boolean isResizable() {
    return resizable;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    panel.layout();
  }

  public void makeResizable() {
    if (nwResizeHandle == null) {
      nwResizeHandle = newResizeHandle(0, 0, NORTH_WEST);
    } else {
      windowController.getResizeDragController().makeDraggable(nwResizeHandle,
          NORTH_WEST);
      nwResizeHandle.addStyleName("Resize-" + NORTH_WEST.directionLetters);
    }

    if (nResizeHandle == null) {
      nResizeHandle = newResizeHandle(0, 1, NORTH);
    } else {
      windowController.getResizeDragController().makeDraggable(nResizeHandle,
          NORTH);
      nResizeHandle.addStyleName("Resize-" + NORTH.directionLetters);
    }

    if (neResizeHandle == null) {
      neResizeHandle = newResizeHandle(0, 2, NORTH_EAST);
    } else {
      windowController.getResizeDragController().makeDraggable(neResizeHandle,
          NORTH_EAST);
      neResizeHandle.addStyleName("Resize-" + NORTH_EAST.directionLetters);
    }

    if (wResizeHandle == null) {
      wResizeHandle = newResizeHandle(1, 0, WEST);
    } else {
      windowController.getResizeDragController().makeDraggable(wResizeHandle,
          WEST);
      wResizeHandle.addStyleName("Resize-" + WEST.directionLetters);
    }

    if (eResizeHandle == null) {
      eResizeHandle = newResizeHandle(1, 2, EAST);
    } else {
      windowController.getResizeDragController().makeDraggable(eResizeHandle,
          EAST);
      eResizeHandle.addStyleName("Resize-" + EAST.directionLetters);
    }

    if (swResizeHandle == null) {
      swResizeHandle = newResizeHandle(2, 0, SOUTH_WEST);
    } else {
      windowController.getResizeDragController().makeDraggable(swResizeHandle,
          SOUTH_WEST);
      swResizeHandle.addStyleName("Resize-" + SOUTH_WEST.directionLetters);
    }

    if (sResizeHandle == null) {
      sResizeHandle = newResizeHandle(2, 1, SOUTH);
    } else {
      windowController.getResizeDragController().makeDraggable(sResizeHandle,
          SOUTH);
      sResizeHandle.addStyleName("Resize-" + SOUTH.directionLetters);
    }

    if (seResizeHandle == null) {
      seResizeHandle = newResizeHandle(2, 2, SOUTH_EAST);
    } else {
      windowController.getResizeDragController().makeDraggable(seResizeHandle,
          SOUTH_EAST);
      seResizeHandle.addStyleName("Resize-" + SOUTH_EAST.directionLetters);
    }
  }

  public void moveBy(int right, int down) {
    AbsolutePanel parent = (AbsolutePanel) getParent();
    Location location = new WidgetLocation(this, parent);
    int left = location.getLeft() + right;
    int top = location.getTop() + down;
    parent.setWidgetPosition(this, left, top);
  }

  private ElementDragHandle newResizeHandle(int row, int col,
      DirectionConstant direction) {
    final Element td = getCellElement(row, col).getParentElement().cast();
    final ElementDragHandle widget = new ElementDragHandle(td);
    adopt(widget);
    windowController.getResizeDragController().makeDraggable(widget, direction);
    widget.addStyleName("Resize-" + direction.directionLetters);
    return widget;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Panel#onLoad()
   */
  protected void onLoad() {
    super.onLoad();
    if (!initialized) {
      initialized = true;

      resizeToFitContent();

      if (windowState == WindowState.MAXIMIZED) {
        maximize();
      } else if (windowState == WindowState.MINIMIZED) {
      } else {

        if (width != null && height != null) {
          panel.setSize("0px", "0px");

          final int[] box = DOM.getClientSize(getElement());
          WindowPanel.super.setSize("auto", "auto");
          final int[] box2 = DOM.getClientSize(getCellElement(0, 0));
          final int[] box3 = DOM.getClientSize(getCellElement(2, 0));
          setContentSize(box[0] - box2[0] - box3[0], box[1] - box2[1] - box3[1]);
          layout();
        } else if (width != null) {
          panel.setSize("0px", "0px");

          final int[] box = DOM.getClientSize(getElement());
          WindowPanel.super.setWidth("auto");
          final int[] box2 = DOM.getClientSize(getCellElement(0, 0));
          final int[] box3 = DOM.getClientSize(getCellElement(2, 0));
          final int[] size = panel.getPreferredSize();
          setContentSize(box[0] - box2[0] - box3[0], size[1]);
          layout();
        } else if (height != null) {
          panel.setSize("0px", "0px");

          final int[] box = DOM.getClientSize(getElement());
          WindowPanel.super.setHeight("auto");
          final int[] box2 = DOM.getClientSize(getCellElement(0, 0));
          final int[] box3 = DOM.getClientSize(getCellElement(2, 0));
          final int[] size = panel.getPreferredSize();
          setContentSize(size[0], box[1] - box2[1] - box3[1]);
          layout();
        } else {
          resizeToFitContent();
        }
      }

      // final int[] _box = DOM.getClientSize(getElement());
      // System.out.println("\t" + _box[0] + "x" + _box[1]);
    }
  }

  /**
   * Removes a window closing listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeWindowCloseListener(WindowCloseListener listener) {
    if (closingListeners != null) {
      closingListeners.remove(listener);
    }
  }

  /**
   * Removes a window panel resize listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeWindowResizeListener(WindowResizeListener listener) {
    if (resizeListeners != null) {
      resizeListeners.remove(listener);
    }
  }

  public void resizeToFitContent() {
    panel.setSize("0px", "0px");
    final int[] size = panel.getPreferredSize();
    setContentSize(size[0], size[1]);
    layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasCaption#setCaption(java.lang.String)
   */
  public void setCaption(final String text) {
    panel.getHeader().setText(text);
  }

  public void setContentSize(int width, int height) {
    if (isResizable()) {
      contentWidth = width;
      contentHeight = height;
    }

    DOM.setContentAreaWidth(panel.getElement(), width);
    DOM.setContentAreaHeight(panel.getElement(), height);

    layoutTimer.schedule(333);
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

  @Override
  public void setHeight(String height) {
    super.setHeight(height);
    this.height = height;
  }

  @Override
  public void setWidget(Widget w) {
    panel.clear();
    panel.add(w);
    // maybeUpdateSize();
  }

  @Override
  public void setWidth(String width) {
    super.setWidth(width);
    this.width = width;
  }

  private void setWindowOrder(int order) {
    int zIndex = (order + Z_INDEX_BASE);
    if (modal) {
      zIndex += Z_INDEX_MODAL_OFFSET;
    }
    DOM.setStyleAttribute(getElement(), "zIndex", Integer.toString(zIndex));
  }

  /**
   * Shows the popup. It must have a child widget before this method is called.
   */
  @Override
  public void show() {
    if (windowState == WindowState.MINIMIZED) {
      return;
    }
    if (modal) {
      if (glassPanel == null) {
        glassPanel = new GlassPanel(false);
        glassPanel.addStyleName("mosaic-GlassPanel-default");
        DOM.setStyleAttribute(glassPanel.getElement(), "zIndex",
            DOM.getStyleAttribute(WindowPanel.this.getElement(), "zIndex"));
      }
      windowController.getBoundaryPanel().add(glassPanel, 0, 0);
    }
    super.show();

    if (!isActive()) {
      bringToFront();
    }
  }

  public enum WindowState {
    NORMAL, MINIMIZED, MAXIMIZED
  }

  private WindowState windowState = WindowState.NORMAL;

  private int restoredLeft;
  private int restoredTop;
  private int restoredWidth;
  private int restoredHeight;

  public void setWindowState(WindowState windowState) {
    if (this.windowState != windowState) {
      WindowState oldState = this.windowState;
      this.windowState = windowState;

      if (isAttached()) {
        if (windowState == WindowState.NORMAL) {
          restore(oldState);
        } else if (windowState == WindowState.MAXIMIZED) {
          maximize();
        } else if (windowState == WindowState.MINIMIZED) {
          minimize();
        }
      }

      fireWindowStateChangeImpl();
    }
  }

  public void setCollapsed(boolean collapsed) {
    if (collapsed == isCollapsed()) {
      return;
    }

    if (collapsed) {
      if (getWindowState() != WindowState.MAXIMIZED) {
        final int[] box = DOM.getClientSize(getElement());
        final int[] boxTL = DOM.getClientSize(getCellElement(0, 0));
        final int[] boxTR = DOM.getClientSize(getCellElement(0, 2));
        final int[] boxBL = DOM.getClientSize(getCellElement(2, 0));
        restoredWidth = box[0] - (boxTL[0] + boxTR[0]);
        restoredHeight = box[1] - (boxTL[1] + boxBL[1]);
      }
      panel.setCollapsed(true);
      final int[] size = panel.getPreferredSize();
      setContentSize(size[0], size[1]);
      if (isResizable()) {
        makeNotResizable();
      }
    } else {
      panel.setCollapsed(false);
      if (getWindowState() != WindowState.MAXIMIZED) {
        setContentSize(restoredWidth, restoredHeight);
      } else {
        final int[] size = DOM.getClientSize(windowController.getBoundaryPanel().getElement());
        final int[] boxTL = DOM.getClientSize(getCellElement(0, 0));
        final int[] boxTR = DOM.getClientSize(getCellElement(0, 2));
        final int[] boxBL = DOM.getClientSize(getCellElement(2, 0));
        setContentSize(size[0] - (boxTL[0] + boxTR[0]), size[1]
            - (boxTL[1] + boxBL[1]));
      }
      if (isResizable()) {
        makeResizable();
      }
    }

    layout();
  }

  public boolean isCollapsed() {
    return panel.isCollapsed();
  }

  protected void restore(WindowState oldState) {
    if (isResizable() && oldState == WindowState.MAXIMIZED) {
      if (isCollapsed()) {
        setPopupPosition(restoredLeft, restoredTop);
        panel.setSize("0px", "0px");
        setContentSize(restoredWidth, panel.getPreferredSize()[1]);
        makeResizable();
      } else {
        setPopupPosition(restoredLeft, restoredTop);
        panel.setSize("0px", "0px");
        setContentSize(restoredWidth, restoredHeight);
        makeResizable();
      }
      windowController.getMoveDragController().makeDraggable(this,
          panel.getHeader());

      layout();
    } else if (!isModal() && oldState == WindowState.MINIMIZED) {
      show();
    }
  }

  protected void maximize() {
    if (isResizable()) {
      if (isCollapsed()) {
        restoredLeft = getAbsoluteLeft();
        restoredTop = getAbsoluteTop();
        final int[] size = DOM.getClientSize(windowController.getBoundaryPanel().getElement());
        final int[] boxTL = DOM.getClientSize(getCellElement(0, 0));
        final int[] boxTR = DOM.getClientSize(getCellElement(0, 2));
        setPopupPosition(0, 0);
        setContentSize(size[0] - (boxTL[0] + boxTR[0]),
            panel.getPreferredSize()[1]);
      } else {
        restoredLeft = getAbsoluteLeft();
        restoredTop = getAbsoluteTop();
        restoredWidth = contentWidth;
        restoredHeight = contentHeight;
        final int[] size = DOM.getClientSize(windowController.getBoundaryPanel().getElement());
        final int[] boxTL = DOM.getClientSize(getCellElement(0, 0));
        final int[] boxTR = DOM.getClientSize(getCellElement(0, 2));
        final int[] boxBL = DOM.getClientSize(getCellElement(2, 0));
        setPopupPosition(0, 0);
        setContentSize(size[0] - (boxTL[0] + boxTR[0]), size[1]
            - (boxTL[1] + boxBL[1]));
        makeNotResizable();
      }
      windowController.getMoveDragController().makeNotDraggable(this);

      layout();
    }
  }

  public void makeNotResizable() {
    windowController.getResizeDragController().makeNotDraggable(nwResizeHandle);
    nwResizeHandle.removeStyleName("Resize-" + NORTH_WEST.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(nResizeHandle);
    nResizeHandle.removeStyleName("Resize-" + NORTH.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(neResizeHandle);
    neResizeHandle.removeStyleName("Resize-" + NORTH_EAST.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(wResizeHandle);
    wResizeHandle.removeStyleName("Resize-" + WEST.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(eResizeHandle);
    eResizeHandle.removeStyleName("Resize-" + EAST.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(swResizeHandle);
    swResizeHandle.removeStyleName("Resize-" + SOUTH_WEST.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(sResizeHandle);
    sResizeHandle.removeStyleName("Resize-" + SOUTH.directionLetters);

    windowController.getResizeDragController().makeNotDraggable(seResizeHandle);
    seResizeHandle.removeStyleName("Resize-" + SOUTH_EAST.directionLetters);
  }

  protected void minimize() {
    if (!isModal()) {
      super.hide(false);
    }
  }

  public WindowState getWindowState() {
    return windowState;
  }

  private List<WindowStateListener> windowStateListeners;

  public void addWindowStateListener(WindowStateListener listener) {
    if (windowStateListeners == null) {
      windowStateListeners = new ArrayList<WindowStateListener>();
    }
    windowStateListeners.add(listener);
  }

  public void removeWindowStateListener(WindowStateListener listener) {
    if (windowStateListeners != null) {
      windowStateListeners.remove(listener);
    }
  }

  private void fireWindowStateChangeImpl() {
    if (windowStateListeners != null) {
      for (WindowStateListener listener : windowStateListeners) {
        listener.onWindowStateChange(this);
      }
    }
  }

  /**
   * Event handler for a change in the window state.
   */
  public interface WindowStateListener {
    void onWindowStateChange(WindowPanel sender);
  }

}
