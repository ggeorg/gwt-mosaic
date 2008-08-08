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

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.Caption.CaptionRegion;
import org.mosaic.ui.client.layout.BaseLayout;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasCaption;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class WindowPanel extends DecoratedPopupPanel implements HasCaption {

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

  final class MoveDragController extends PickupDragController {

    public MoveDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel, true);
    }

    @Override
    public void dragEnd() {
      panel.hideBody(false);
      super.dragEnd();
    }

    @Override
    public void dragStart() {
      panel.hideBody(true);
      super.dragStart();
    }
  }

  final class ResizeDragController extends AbstractDragController {

    private static final int MIN_WIDGET_SIZE = 96;

    private Map<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    private WindowPanel windowPanel = null;

    public ResizeDragController(AbsolutePanel boundaryPanel, WindowPanel windowPanel) {
      super(boundaryPanel);
      this.windowPanel = windowPanel;
    }

    // protected Widget newDragProxy(DragContext)

    // @Override
    // public void startDrag() {
    // super.dragStart();
    //      
    // WidgetLocation currentDraggableLocation = new
    // WidgetLocation(context.draggable, context.boundaryPanel);
    //      
    // movablePanel = newDragProxy(context);
    // context.boundaryPanel.add(movablePanel.
    // currentDraggableLocation.getLeft(), currentDraggableLocation.getTop());
    // }

    public void dragMove() {
      int direction = ((ResizeDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & WindowPanel.DIRECTION_NORTH) != 0) {
        final int delta = context.draggable.getAbsoluteTop() - context.desiredDraggableY;
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
        final int delta = context.desiredDraggableY - context.draggable.getAbsoluteTop();
        if (delta != 0) {
          int contentHeight = windowPanel.getContentHeight();
          int newHeight = Math.max(contentHeight + delta,
              windowPanel.panel.getHeader().getOffsetHeight());
          windowPanel.setContentSize(windowPanel.getContentWidth(), newHeight);
        }
      }
      if ((direction & WindowPanel.DIRECTION_WEST) != 0) {
        int delta = context.draggable.getAbsoluteLeft() - context.desiredDraggableX;
        if (delta != 0) {
          int contentWidth = windowPanel.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          if (newWidth != contentWidth) {
            windowPanel.moveBy(contentWidth - newWidth, 0);
          }
          windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
        }
      } else if ((direction & WindowPanel.DIRECTION_EAST) != 0) {
        int delta = context.desiredDraggableX - context.draggable.getAbsoluteLeft();
        if (delta != 0) {
          int contentWidth = windowPanel.getContentWidth();
          int newWidth = Math.max(contentWidth + delta, MIN_WIDGET_SIZE);
          windowPanel.setContentSize(newWidth, windowPanel.getContentHeight());
        }
      }

    }

    private DirectionConstant getDirection(Widget draggable) {
      return directionMap.get(draggable);
    }

    public void makeDraggable(Widget widget, WindowPanel.DirectionConstant direction) {
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

      resizeDragController = new ResizeDragController(boundaryPanel, windowPanel);
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
  static final DirectionConstant EAST = new WindowPanel.DirectionConstant(DIRECTION_EAST,
      "e");

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

  /**
   * Specifies that resizing occur at the north-east edge.
   */
  static final DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH
      | DIRECTION_EAST, "ne");

  /**
   * Specifies that resizing occur at the north-west edge.
   */
  static final DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH
      | DIRECTION_WEST, "nw");

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

  /**
   * Specifies that resizing occur at the south-east edge.
   */
  static final DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH
      | DIRECTION_EAST, "se");

  /**
   * Specifies that resizing occur at the south-west edge.
   */
  static final DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH
      | DIRECTION_WEST, "sw");
  /**
   * Specifies that resizing occur at the west edge.
   */
  static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

  private ElementDragHandle nwResizeHandle, nResizeHandle, neResizeHandle;
  private ElementDragHandle swResizeHandle, sResizeHandle, seResizeHandle;
  private ElementDragHandle wResizeHandle, eResizeHandle;

  private int contentWidth, contentHeight;
  private final WindowController windowController;

  private final TitledLayoutPanel panel;

  private final boolean resizable, modal;

  private boolean initialized;

  private Timer layoutTimer = new Timer() {
    public void run() {
      panel.layout();
    }
  };

  public WindowPanel() {
    this(null);
  }

  protected WindowPanel(AbsolutePanel boundaryPanel, String caption, boolean resizable,
      boolean autoHide, boolean modal) {
    super(autoHide, modal);

    this.resizable = resizable;
    this.modal = modal;

    windowController = new WindowController(boundaryPanel, this);

    if (isResizable()) {
      makeResizable();
    }

    panel = new TitledLayoutPanel(caption);

    ImageButton closeBtn = new ImageButton(CAPTION_IMAGES.windowClose());
    closeBtn.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        hide();
      }
    });
    panel.getHeader().add(closeBtn, CaptionRegion.RIGHT);

    windowController.getMoveDragController().makeDraggable(this, panel.getHeader());

    super.setWidget(panel);

    // addClickListener(new ClickListener() {
    // public void onClick(Widget sender) {
    // // TODO force out panel to the top of our z-index context
    // AbsolutePanel boundaryPanel = windowController.getBoundaryPanel();
    // WidgetLocation location = new WidgetLocation(WindowPanel.this,
    // boundaryPanel);
    // boundaryPanel.add(WindowPanel.this, location.getLeft(),
    // location.getTop());
    // }
    // });

    addStyleName(DEFAULT_STYLENAME);
  }

  public WindowPanel(String caption) {
    this(caption, true, false);
  }

  public WindowPanel(String caption, boolean resizable, boolean modal) {
    this(caption, resizable, false, modal);
  }

  protected WindowPanel(String caption, boolean resizable, boolean autoHide, boolean modal) {
    this(RootPanel.get(), caption, resizable, autoHide, modal);
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

  @Override
  public Widget getWidget() {
    if (panel.getWidgetCount() > 0) {
      return panel.getWidget(0);
    } else {
      return null;
    }
  }

  public boolean isModal() {
    return modal;
  }

  public boolean isResizable() {
    return resizable;
  }

  public void makeResizable() {
    nwResizeHandle = newResizeHandle(0, 0, NORTH_WEST);
    nResizeHandle = newResizeHandle(0, 1, NORTH);
    neResizeHandle = newResizeHandle(0, 2, NORTH_EAST);

    wResizeHandle = newResizeHandle(1, 0, WEST);
    eResizeHandle = newResizeHandle(1, 2, EAST);

    swResizeHandle = newResizeHandle(2, 0, SOUTH_WEST);
    sResizeHandle = newResizeHandle(2, 1, SOUTH);
    seResizeHandle = newResizeHandle(2, 2, SOUTH_EAST);
  }

  public void moveBy(int right, int down) {
    AbsolutePanel parent = (AbsolutePanel) getParent();
    Location location = new WidgetLocation(this, parent);
    int left = location.getLeft() + right;
    int top = location.getTop() + down;
    parent.setWidgetPosition(this, left, top);
  }

  private ElementDragHandle newResizeHandle(int row, int col, DirectionConstant direction) {
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
    if (!initialized) {
      initialized = true;
      final int[] box = DOM.getClientSize(getElement());
      final int[] m = DOM.getMarginSizes(panel.getElement());
      final int delta = panel.getOffsetHeight() + m[0] + m[2]
          - BaseLayout.getFlowHeight(panel);
      setContentSize(box[0], box[1] - delta + 1); // FIXME why (+ 1) ?
      setSize("auto", "auto");
      panel.layout();
    }
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
      if (width != contentWidth) {
        contentWidth = width;
      }
      if (height != contentHeight) {
        contentHeight = height;
      }
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
  public void setWidget(Widget w) {
    panel.clear();
    panel.add(w);
    // maybeUpdateSize();
  }

}
