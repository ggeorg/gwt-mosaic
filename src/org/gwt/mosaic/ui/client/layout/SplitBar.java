/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.layout;

import java.util.HashMap;
import java.util.Map;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.allen_sauer.gwt.dnd.client.AbstractDragController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
final class SplitBar extends Widget implements HasAllMouseHandlers {

  static final class DirectionConstant {

    final int directionBits;

    final String directionLetters;

    private DirectionConstant(int directionBits, String directionLetters) {
      this.directionBits = directionBits;
      this.directionLetters = directionLetters;
    }
  }

  /**
   * @author ggeorg
   * 
   */
  final class SplitBarDragController extends AbstractDragController {

    private Map<Widget, DirectionConstant> directionMap = new HashMap<Widget, DirectionConstant>();

    private Widget widget = null;

    private Widget movablePanel;

    private int draggableOldAbsoluteLeft, draggableOldAbsoluteTop;

    private BorderLayoutData layoutData = null;

    private int boundaryOffsetX, boundaryOffsetY;
    private int dropTargetClientHeight, dropTargetClientWidth;
    private int minValue = 0;
    private int maxValue = -1;

    public SplitBarDragController(AbsolutePanel boundaryPanel, Widget widget) {
      super(boundaryPanel);
      this.widget = widget;
    }

    @Override
    public void dragEnd() {
      int direction = ((SplitBarDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & SplitBar.DIRECTION_NORTH) != 0) {
        int delta = context.draggable.getAbsoluteTop()
            - draggableOldAbsoluteTop;
//        layoutData.preferredSize = widget.getOffsetHeight() + delta;
        WidgetHelper.setSize(widget, new Dimension(-1, widget.getOffsetHeight() + delta));
        layoutData.preferredSize = widget.getOffsetHeight();
      } else if ((direction & SplitBar.DIRECTION_SOUTH) != 0) {
        int delta = draggableOldAbsoluteTop
            - context.draggable.getAbsoluteTop();
//        layoutData.preferredSize = widget.getOffsetHeight() + delta;
        WidgetHelper.setSize(widget, new Dimension(-1, widget.getOffsetHeight() + delta));
        layoutData.preferredSize = widget.getOffsetHeight();
      }
      if ((direction & SplitBar.DIRECTION_WEST) != 0) {
        int delta = context.draggable.getAbsoluteLeft()
            - draggableOldAbsoluteLeft;
//        layoutData.preferredSize = widget.getOffsetWidth() + delta;
        WidgetHelper.setSize(widget, new Dimension(widget.getOffsetWidth() + delta, -1));
        layoutData.preferredSize = widget.getOffsetWidth();
      } else if ((direction & SplitBar.DIRECTION_EAST) != 0) {
        int delta = draggableOldAbsoluteLeft
            - context.draggable.getAbsoluteLeft();
//        layoutData.preferredSize = widget.getOffsetWidth() + delta;
        WidgetHelper.setSize(widget, new Dimension(widget.getOffsetWidth() + delta, -1));
        layoutData.preferredSize = widget.getOffsetWidth();
      }

//      layoutData.preferredSize = Math.max((int) layoutData.preferredSize,
//          layoutData.minSize);
//      layoutData.preferredSize = Math.min((int) layoutData.preferredSize,
//          layoutData.maxSize);

      super.dragEnd();

      glassPanel.removeFromParent();

      WidgetHelper.layout(context.boundaryPanel);
      movablePanel.removeStyleName(getStylePrimaryName() + "-Movable");
    }

    public void dragMove() {
      int direction = ((SplitBarDragController) context.dragController).getDirection(context.draggable).directionBits;
      if ((direction & SplitBar.DIRECTION_NORTH) != 0) {
        int desiredTop = context.desiredDraggableY - boundaryOffsetY;
        desiredTop = Math.max(0, Math.min(desiredTop, dropTargetClientHeight
            - context.draggable.getOffsetHeight()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(),
            context.draggable.getAbsoluteLeft() - boundaryOffsetX, desiredTop);
      } else if ((direction & SplitBar.DIRECTION_SOUTH) != 0) {
        int desiredTop = context.desiredDraggableY - boundaryOffsetY;
        desiredTop = Math.max(0, Math.min(desiredTop, dropTargetClientHeight
            - context.draggable.getOffsetHeight()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(),
            context.draggable.getAbsoluteLeft() - boundaryOffsetX, desiredTop);
      }
      if ((direction & SplitBar.DIRECTION_WEST) != 0) {
        int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
        desiredLeft = Math.max(0, Math.min(desiredLeft, dropTargetClientWidth
            - context.draggable.getOffsetWidth()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
            context.draggable.getAbsoluteTop() - boundaryOffsetY);
      } else if ((direction & SplitBar.DIRECTION_EAST) != 0) {
        int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
        desiredLeft = Math.max(0, Math.min(desiredLeft, dropTargetClientWidth
            - context.draggable.getOffsetWidth()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
            context.draggable.getAbsoluteTop() - boundaryOffsetY);
      }
    }

    @Override
    public void dragStart() {
      if (glassPanel == null) {
        glassPanel = new GlassPanel(false);
        glassPanel.addStyleName("mosaic-GlassPanel-invisible");
      }
      RootPanel.get().add(glassPanel, 0, 0);

      super.dragStart();

      WidgetLocation currentDraggableLocation = new WidgetLocation(
          context.draggable, context.boundaryPanel);
      movablePanel = context.draggable;
      context.boundaryPanel.add(movablePanel,
          currentDraggableLocation.getLeft(), currentDraggableLocation.getTop());
      movablePanel.addStyleName(getStylePrimaryName() + "-Movable");

      // one time calculation of boundary panel location for efficiency during
      // dragging
      Location widgetLocation = new WidgetLocation(context.boundaryPanel, null);
      final int[] border = DOM.getBorderSizes(context.boundaryPanel.getElement());
      boundaryOffsetX = widgetLocation.getLeft() + border[3];
      boundaryOffsetY = widgetLocation.getTop() + border[0];
      final Dimension box = DOM.getClientSize(boundaryPanel.getElement());
      dropTargetClientWidth = box.width;
      dropTargetClientHeight = box.height;

      layoutData = (BorderLayoutData) BaseLayout.getLayoutData(widget);
      draggableOldAbsoluteLeft = context.draggable.getAbsoluteLeft();
      draggableOldAbsoluteTop = context.draggable.getAbsoluteTop();
    }

    public DirectionConstant getDirection(Widget draggable) {
      return directionMap.get(draggable);
    }

    public int getMaxValue() {
      return maxValue;
    }

    public int getMinValue() {
      return minValue;
    }

    public void makeDraggable(Widget widget,
        SplitBar.DirectionConstant direction) {
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

    public void setMaxValue(int maxValue) {
      this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
      this.minValue = minValue;
    }

  }

  private static GlassPanel glassPanel;

  /**
   * Specifies that resizing occur at the east edge.
   */
  static final int DIRECTION_EAST = 0x0001;

  /**
   * Specifies that resizing occur at the both edge.
   */
  static final int DIRECTION_NORTH = 0x0002;

  /**
   * Specifies that resizing occur at the west edge.
   */
  static final int DIRECTION_WEST = 0x0008;

  /**
   * Specifies that resizing occur at the south edge.
   */
  static final int DIRECTION_SOUTH = 0x0004;

  /**
   * Specifies that resizing occur at the both edge.
   */
  public static final DirectionConstant NORTH = new DirectionConstant(
      DIRECTION_NORTH, "n");

  /**
   * Specifies that resizing occur at the south edge.
   */
  public static final DirectionConstant SOUTH = new DirectionConstant(
      DIRECTION_SOUTH, "s");

  /**
   * Specifies that resizing occur at the east edge.
   */
  public static final DirectionConstant EAST = new DirectionConstant(
      DIRECTION_EAST, "e");

  /**
   * Specifies that resizing occur at the west edge.
   */
  public static final DirectionConstant WEST = new DirectionConstant(
      DIRECTION_WEST, "w");

  private final AbsolutePanel boundaryPanel;

  private final SplitBarDragController dragController;

  public SplitBar(AbsolutePanel boundaryPanel, Widget widget,
      DirectionConstant direction) {
    setElement(DOM.createSpan());
    sinkEvents(Event.MOUSEEVENTS);

    this.boundaryPanel = boundaryPanel;

    dragController = new SplitBarDragController(boundaryPanel, widget);
    dragController.setBehaviorConstrainedToBoundaryPanel(true);
    dragController.setBehaviorMultipleSelection(false);

    dragController.makeDraggable(this, direction);
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

  public AbsolutePanel getBoundaryPanel() {
    return boundaryPanel;
  }

  public SplitBarDragController getDragController() {
    return dragController;
  }

}
