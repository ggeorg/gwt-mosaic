/*
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos.
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
import java.util.Vector;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.Point;
import org.gwt.mosaic.ui.client.WindowPanel.ElementDragHandle;
import org.gwt.mosaic.ui.client.WindowPanel.WindowState;
import org.gwt.mosaic.ui.client.WindowPanel.WindowStateListener;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DesktopPanel extends Composite implements
    OpenHandler<WindowPanel>, CloseHandler<PopupPanel>,
    SelectionHandler<WindowPanel>, WindowStateListener, HasLayoutManager {

  private class MoveDragController extends AbstractDragController {

    private int boundaryOffsetX;

    private int boundaryOffsetY;

    private int dropTargetClientHeight;

    private int dropTargetClientWidth;

    public MoveDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel);
    }

    @Override
    public void dragEnd() {
      final WindowPanel w = (WindowPanel) context.draggable;

      super.dragEnd();
      if (!w.isModal()) {
        w.glassPanel.removeFromParent();
      }
      if (w.isHideContentOnMove() && !w.isCollapsed()) {
        w.hideContent(false);
      }
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
      final WindowPanel w = (WindowPanel) context.draggable;

      if (!w.isActive()) {
        w.toFront();
      }

      if (!w.isCollapsed()) {
        w.hideContent(w.isHideContentOnMove());
      }

      if (!w.isModal()) {
        if (w.glassPanel == null) {
          w.glassPanel = new GlassPanel(false);
        }

        w.glassPanel.addStyleName("mosaic-GlassPanel-invisible");
        DOM.setStyleAttribute(w.glassPanel.getElement(), "zIndex",
            DOM.getComputedStyleAttribute(w.getElement(), "zIndex"));

        add(w.glassPanel);
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

    @Override
    public void makeDraggable(Widget draggable) {
      try {
        super.makeDraggable(draggable);
      } catch (Exception ex) {
        // Ignore!
      }
    }

    @Override
    public void makeNotDraggable(Widget draggable) {
      try {
        super.makeNotDraggable(draggable);
      } catch (Exception ex) {
        // Ignore!
      }
    }
  }

  private class ResizeDragController extends AbstractDragController {

    private static final int MIN_WIDGET_SIZE = 96;

    private int boundaryOffsetX;

    private int boundaryOffsetY;

    private Map<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    private int dropTargetClientHeight;

    private int dropTargetClientWidth;

    public ResizeDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel);
    }

    @Override
    public void dragEnd() {
      final WindowPanel w = (WindowPanel) context.draggable.getParent();

      super.dragEnd();
      if (!w.isModal()) {
        w.glassPanel.removeFromParent();
      }
      w.hideContent(false);
      w.setContentSize(w.getContentSize()); // XXX ?
      w.delayedLayout(CoreConstants.MIN_DELAY_MILLIS);
    }

    public void dragMove() {
      final WindowPanel w = (WindowPanel) context.draggable.getParent();

      int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & DIRECTION_NORTH) != 0) {
        final int delta = getBehaviorConstrainedToBoundaryPanel()
            ? context.draggable.getAbsoluteTop()
                - Math.max(context.desiredDraggableY, boundaryOffsetY)
            : context.draggable.getAbsoluteTop() - context.desiredDraggableY;
        if (delta != 0) {
          int contentHeight = w.getContentHeight();
          int newHeight = Math.max(contentHeight + delta,
              w.getHeader().getOffsetHeight());
          if (newHeight != contentHeight) {
            w.moveBy(0, contentHeight - newHeight);
          }
          w.setContentSize(w.getContentWidth(), newHeight);
          w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
        }
      } else if ((direction & DIRECTION_SOUTH) != 0) {
        final int delta = getBehaviorConstrainedToBoundaryPanel() ? Math.min(
            context.desiredDraggableY, dropTargetClientHeight)
            - context.draggable.getAbsoluteTop() : context.desiredDraggableY
            - context.draggable.getAbsoluteTop();
        if (delta != 0) {
          int contentHeight = w.getContentHeight();
          int newHeight = Math.max(contentHeight + delta,
              w.getHeader().getOffsetHeight());
          w.setContentSize(w.getContentWidth(), newHeight);
          w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
        }
      }
      if ((direction & DIRECTION_WEST) != 0) {
        int delta = getBehaviorConstrainedToBoundaryPanel()
            ? context.draggable.getAbsoluteLeft()
                - Math.max(context.desiredDraggableX, boundaryOffsetX)
            : context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
        if (delta != 0) {
          int contentWidth = w.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          if (newWidth != contentWidth) {
            w.moveBy(contentWidth - newWidth, 0);
          }
          w.setContentSize(newWidth, w.getContentHeight());
          w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
        }
      } else if ((direction & DIRECTION_EAST) != 0) {
        int delta = getBehaviorConstrainedToBoundaryPanel() ? Math.min(
            context.desiredDraggableX, dropTargetClientWidth)
            - context.draggable.getAbsoluteLeft() : context.desiredDraggableX
            - context.draggable.getAbsoluteLeft();
        if (delta != 0) {
          int contentWidth = w.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          w.setContentSize(newWidth, w.getContentHeight());
          w.delayedLayout(CoreConstants.DEFAULT_DELAY_MILLIS);
        }
      }

    }

    @Override
    public void dragStart() {
      final WindowPanel w = (WindowPanel) context.draggable.getParent();

      if (!w.isActive()) {
        w.toFront();
      }

      w.hideContent(true);
      if (!w.isModal()) {
        if (w.glassPanel == null) {
          w.glassPanel = new GlassPanel(false);
        }

        w.glassPanel.addStyleName("mosaic-GlassPanel-invisible");
        DOM.setStyleAttribute(w.glassPanel.getElement(), "zIndex",
            DOM.getComputedStyleAttribute(w.getElement(), "zIndex"));

        getBoundaryPanel().add(w.glassPanel, 0, 0);
      }

      super.dragStart();

      // one time calculations of boundary panel location for efficiency during
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

    @Override
    public void makeDraggable(Widget draggable) {
      try {
        super.makeDraggable(draggable);
      } catch (Exception ex) {
        // Ignore!
      }
    }

    public void makeDraggable(Widget widget, DirectionConstant direction) {
      super.makeDraggable(widget);
      directionMap.put(widget, direction);
    }

    @Override
    public void makeNotDraggable(Widget draggable) {
      try {
        super.makeNotDraggable(draggable);
      } catch (Exception ex) {
        // Ignore!
      }
    }

    private DirectionConstant getDirection(Widget draggable) {
      return directionMap.get(draggable);
    }

    protected BoundaryDropController newBoundaryDropController(
        AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
      if (allowDroppingOnBoundaryPanel) {
        throw new IllegalArgumentException();
      }
      return new BoundaryDropController(boundaryPanel, false);
    }

  }

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

  private static final Map<Element, DesktopPanel> map = new HashMap<Element, DesktopPanel>();

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
  static final DirectionConstant EAST = new DirectionConstant(DIRECTION_EAST,
      "e");

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

  public static DesktopPanel get(Widget widget) {
    assert (widget != null);
    DesktopPanel desktopPanel = map.get(widget.getElement());
    if (desktopPanel == null) {
      desktopPanel = new DesktopPanel(widget.getElement());
    }
    return desktopPanel;
  }

  private WindowPanel active;

  private DesktopManager desktopManager;

  private MoveDragController moveDragController;

  private ResizeDragController resizeDragController;

  private Vector<WindowPanel> windowPanels;

  /**
   * Creates a new {@code DesktopPane}.
   */
  public DesktopPanel() {
    initWidget(new AbsolutePanel());
  }

  /**
   * Creates a {@code DesktopPanel} with the given element. This is protected so
   * that it can be used by a subclass that wants to substitute another element.
   * The element is presumed to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   */
  protected DesktopPanel(Element elem) {
    initWidget(new AbsolutePanel(elem) {
    });
    onAttach();
  }

  public void add(WindowPanel windowPanel) {
    assert (windowPanel != null);
    if (windowPanels == null) {
      windowPanels = new Vector<WindowPanel>();
    }
    if (windowPanels.indexOf(windowPanel) == -1) {
      windowPanels.add(windowPanel);
    }
  }

  public WindowPanel getActive() {
    return active;
  }

  /**
   * Returns the {@code DesktopManger} that handles desktop-specific UI actions.
   * 
   * @return the {@code DesktopManager} used
   */
  public DesktopManager getDesktopManager() {
    if (desktopManager == null) {
      desktopManager = createDefaultDesktopManager();
    }
    return desktopManager;
  }

  public AbstractDragController getMoveDragController() {
    return moveDragController;
  }

  public Point getPopupPosition(WindowPanel windowPanel) {
    final int[] borders = DOM.getBorderSizes(getElement());
    return new Point(windowPanel.getAbsoluteLeft()
        - (getAbsoluteLeft() + borders[3]), windowPanel.getAbsoluteTop()
        - (getAbsoluteTop() + borders[0]));
  }

  public Dimension getPreferredSize() {
    return DOM.getClientSize(getElement());
  }

  public AbstractDragController getResizeDragController() {
    return resizeDragController;
  }

  public WindowPanel getWindowPanel(int index) {
    return (windowPanels == null) ? null : windowPanels.get(index);
  }

  public int getWindowPanelCount() {
    return (windowPanels == null) ? 0 : windowPanels.size();
  }

  public int getWindowPanelIndex(WindowPanel child) {
    return (windowPanels == null) ? -1 : windowPanels.indexOf(child);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate()
   */
  public void invalidate() {
    invalidate(null);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate(com.google.gwt.user.client.ui.Widget)
   */
  public void invalidate(Widget widget) {
    final HasLayoutManager parent = WidgetHelper.getParent(this);
    if (parent != null) {
      parent.invalidate(null);
    }
  }

  /**
   * Returns whether the {@link WindowPanel} is the currently "selected" or
   * active {@link WindowPanel} in this {@code DesktopPanel}.
   * 
   * @param windowPanel the {@link WindowPanel} to check
   * @return {@code true} if the {@code WindowPanel} is currently selected
   *         (active)
   */
  public boolean isActive(WindowPanel windowPanel) {
    assert (windowPanel != null);
    return windowPanel == active;
  }

  public void layout() {
    if (windowPanels == null) {
      return;
    }
    final Dimension box = DOM.getClientSize(getElement());
    for (int i = 0, n = windowPanels.size(); i < n; i++) {
      final WindowPanel w = windowPanels.get(i);
      final Dimension d = WidgetHelper.getOffsetSize(w);

      final Point p = getPopupPosition(w);

      p.x -= Math.max(0, (p.x + w.getOffsetWidth()) - box.width);
      p.y -= Math.max(0, (p.y + w.getOffsetHeight()) - box.height);

      w.setPopupPosition(Math.max(0, p.x), Math.max(0, p.y));

      d.width = d.width > box.width ? box.width : -1;
      d.height = d.height > box.height ? box.height : -1;

      WidgetHelper.setSize(w, d);

      if (w.getWindowState() == WindowState.MAXIMIZED) {
        WidgetHelper.setSize(w, new Dimension(DOM.getClientSize(getElement())));
      }

      w.delayedLayout(CoreConstants.MIN_DELAY_MILLIS);
    }
  }

  public void makeDraggable(WindowPanel w) {
    moveDragController.makeDraggable(w, w.getHeader());
  }

  public void makeNotDraggable(WindowPanel w) {
    moveDragController.makeNotDraggable(w);
  }

  public void makeNotResizable(WindowPanel w) {
    final AbstractDragController c = getResizeDragController();

    c.makeNotDraggable(w.nwResizeHandle);
    w.nwResizeHandle.removeStyleName("Resize-" + NORTH_WEST.directionLetters);

    c.makeNotDraggable(w.nResizeHandle);
    w.nResizeHandle.removeStyleName("Resize-" + NORTH.directionLetters);

    c.makeNotDraggable(w.neResizeHandle);
    w.neResizeHandle.removeStyleName("Resize-" + NORTH_EAST.directionLetters);

    c.makeNotDraggable(w.wResizeHandle);
    w.wResizeHandle.removeStyleName("Resize-" + WEST.directionLetters);

    c.makeNotDraggable(w.eResizeHandle);
    w.eResizeHandle.removeStyleName("Resize-" + EAST.directionLetters);

    c.makeNotDraggable(w.swResizeHandle);
    w.swResizeHandle.removeStyleName("Resize-" + SOUTH_WEST.directionLetters);

    c.makeNotDraggable(w.sResizeHandle);
    w.sResizeHandle.removeStyleName("Resize-" + SOUTH.directionLetters);

    c.makeNotDraggable(w.seResizeHandle);
    w.seResizeHandle.removeStyleName("Resize-" + SOUTH_EAST.directionLetters);
  }

  public void makeResizable(WindowPanel w) {
    if (w.nwResizeHandle == null) {
      w.nwResizeHandle = w.newResizeHandle(0, 0, NORTH_WEST);
    }
    makeElementDradHandleDraggable(w.nwResizeHandle, NORTH_WEST);

    if (w.nResizeHandle == null) {
      w.nResizeHandle = w.newResizeHandle(0, 1, NORTH);
    }
    makeElementDradHandleDraggable(w.nResizeHandle, NORTH);

    if (w.neResizeHandle == null) {
      w.neResizeHandle = w.newResizeHandle(0, 2, NORTH_EAST);
    }
    makeElementDradHandleDraggable(w.neResizeHandle, NORTH_EAST);

    if (w.wResizeHandle == null) {
      w.wResizeHandle = w.newResizeHandle(1, 0, WEST);
    }
    makeElementDradHandleDraggable(w.wResizeHandle, WEST);

    if (w.eResizeHandle == null) {
      w.eResizeHandle = w.newResizeHandle(1, 2, EAST);
    }
    makeElementDradHandleDraggable(w.eResizeHandle, EAST);

    if (w.swResizeHandle == null) {
      w.swResizeHandle = w.newResizeHandle(2, 0, SOUTH_WEST);
    }
    makeElementDradHandleDraggable(w.swResizeHandle, SOUTH_WEST);

    if (w.sResizeHandle == null) {
      w.sResizeHandle = w.newResizeHandle(2, 1, SOUTH);
    }
    makeElementDradHandleDraggable(w.sResizeHandle, SOUTH);

    if (w.seResizeHandle == null) {
      w.seResizeHandle = w.newResizeHandle(2, 2, SOUTH_EAST);
    }
    makeElementDradHandleDraggable(w.seResizeHandle, SOUTH_EAST);
  }

  public void onClose(CloseEvent<PopupPanel> event) {
    getDesktopManager().onClose(event);
  }

  public void onOpen(OpenEvent<WindowPanel> event) {
    getDesktopManager().onOpen(event);
  }

  public void onSelection(SelectionEvent<WindowPanel> event) {
    getDesktopManager().onSelection(event);
  }

  public void onWindowStateChange(WindowPanel sender,
      WindowState oldWindowState, WindowState newWindowState) {
    if (newWindowState == WindowState.NORMAL) {
      getDesktopManager().restore(sender, oldWindowState);
    } else if (newWindowState == WindowState.MAXIMIZED) {
      getDesktopManager().maximize(sender, oldWindowState);
    } else if (newWindowState == WindowState.MINIMIZED) {
      getDesktopManager().minimize(sender, oldWindowState);
    }
  }

  public void remove(int index) {
    if (windowPanels == null) {
      return;
    }
    WindowPanel windowPanel = windowPanels.get(index);
    if (windowPanel != null) {
      windowPanels.remove(index);
    }
  }

  /**
   * Sets the {@link DesktopManager} that will handle desktop-specific UI
   * actions.
   * 
   * @param desktopManager the {@code DesktopManager} to use
   */
  public void setDesktopManager(DesktopManager desktopManager) {
    this.desktopManager = desktopManager;
  }

  public void setPopupPosition(WindowPanel windowPanel, int left, int top) {
    windowPanel.setPopupPosition(left, top);
  }

  public void setPopupPosition(WindowPanel windowPanel, Point position) {
    setPopupPosition(windowPanel, position.x, position.y);
  }

  /**
   * Moves the {@code WindowPanel} to the bottom of the {@code WindowPanel}s in
   * this {@code DesktopPanel} (position 0).
   * 
   * @param windowPanel the {@code WidgetPanel} to move
   * @see #toFront(WindowPanel)
   */
  public void toBack(WindowPanel windowPanel) {
    int size = windowPanels.size();
    if (size <= 1) {
      return;
    }
    if (isActive(windowPanel)) {
      active = null;
    }
    int curIndex = windowPanels.indexOf(windowPanel);
    windowPanels.remove(windowPanel);
    windowPanels.add(0, windowPanel);
    for (; curIndex >= 0; curIndex--) {
      windowPanels.get(curIndex).setZIndex(curIndex);
    }
    windowPanels.get(size - 1).toFront();
  }

  /**
   * Moves the {@code WindowPanel} to the top of the {@code WindowPanel}s in
   * this {@code DesktopPanel}.
   * 
   * @param windowPanel the {@code WidgetPanel} to move
   * @see #toBack(WindowPanel)
   */
  public void toFront(WindowPanel windowPanel) {
    this.active = windowPanel;
    // bring to front
    int size = windowPanels.size();
    int curIndex = windowPanels.indexOf(windowPanel);
    if (curIndex + 1 < size) {
      windowPanels.remove(windowPanel);
      windowPanels.add(windowPanel);
      for (; curIndex < size; curIndex++) {
        windowPanels.get(curIndex).setZIndex(curIndex);
      }
    } else {
      windowPanel.setZIndex(curIndex);
    }
  }

  private void makeElementDradHandleDraggable(ElementDragHandle widget,
      DirectionConstant direction) {
    resizeDragController.makeDraggable(widget, direction);
    widget.addStyleName("Resize-" + direction.directionLetters);
  }

  protected DesktopManager createDefaultDesktopManager() {
    return new DefaultDesktopManager();
  }

  @Override
  protected void initWidget(Widget widget) {
    super.initWidget(widget);

    map.put(widget.getElement(), this);

    moveDragController = new MoveDragController((AbsolutePanel) widget);
    moveDragController.setBehaviorConstrainedToBoundaryPanel(true);
    // moveDragController.setBehaviorDragProxy(true);
    moveDragController.setBehaviorMultipleSelection(false);
    moveDragController.setBehaviorDragStartSensitivity(3);

    resizeDragController = new ResizeDragController((AbsolutePanel) widget);
    resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
    resizeDragController.setBehaviorMultipleSelection(false);
    resizeDragController.setBehaviorDragStartSensitivity(3);
  }

  void add(GlassPanel glassPanel) {
    ((AbsolutePanel) getWidget()).add(glassPanel, 0, 0);
  }

}
