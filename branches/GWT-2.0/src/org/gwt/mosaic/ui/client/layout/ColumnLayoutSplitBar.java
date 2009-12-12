package org.gwt.mosaic.ui.client.layout;

import java.util.List;

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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

class ColumnLayoutSplitBar extends Widget implements HasAllMouseHandlers {
  // private static GlassPanel glassPanel;

  private final SplitBarDragController dragController;

  private final List<Widget> visibleWidgets;
  private final Widget widgetL, widgetR;

  class SplitBarDragController extends AbstractDragController {
    private Widget movablePanel;

    private int draggableOldAbsoluteLeft, draggableOldAbsoluteTop;

    private int boundaryOffsetX, boundaryOffsetY;
    private int dropTargetClientHeight, dropTargetClientWidth;

    public SplitBarDragController(AbsolutePanel boundaryPanel) {
      super(boundaryPanel);
    }

    @Override
    public void dragStart() {
      // if (glassPanel == null) {
      // glassPanel = new GlassPanel(false);
      // glassPanel.addStyleName("mosaic-GlassPanel-invisible");
      // }
      // RootPanel.get().add(glassPanel, 0, 0);

      super.dragStart();

      WidgetLocation currentDraggableLocation = new WidgetLocation(
          context.draggable, context.boundaryPanel);
      movablePanel = context.draggable;
      context.boundaryPanel.add(movablePanel,
          currentDraggableLocation.getLeft(), currentDraggableLocation.getTop());
      movablePanel.addStyleName(getStylePrimaryName() + "-Movable");

      // one time calculation of boundary panel location for efficuency during
      // dragging
      Location widgetLocation = new WidgetLocation(context.boundaryPanel, null);
      final int[] border = DOM.getBorderSizes(context.boundaryPanel.getElement());
      boundaryOffsetX = widgetLocation.getLeft() + border[3];
      boundaryOffsetY = widgetLocation.getTop() + border[0];
      final Dimension box = DOM.getClientSize(context.boundaryPanel.getElement());
      dropTargetClientWidth = box.width;
      dropTargetClientHeight = box.height;

      draggableOldAbsoluteLeft = context.draggable.getAbsoluteLeft();
      draggableOldAbsoluteTop = context.draggable.getAbsoluteTop();
    }

    public void dragMove() {
      int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
      desiredLeft = Math.max(0, Math.min(desiredLeft, dropTargetClientWidth
          - context.draggable.getOffsetWidth()));
      DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
          context.draggable.getAbsoluteTop() - boundaryOffsetY);
    }

    @Override
    public void dragEnd() {
      int delta = context.draggable.getAbsoluteLeft()
          - draggableOldAbsoluteLeft;

      int sizeL = widgetL.getOffsetWidth() + delta;
      if (sizeL < 24) {
        sizeL = 24;
        delta = sizeL - widgetL.getOffsetWidth();
      }

      int sizeR = widgetR.getOffsetWidth() - delta;
      if (sizeR < 24) {
        sizeR = 24;
        delta = widgetR.getOffsetWidth() - sizeR;
        sizeL = widgetL.getOffsetWidth() + delta;
      }

      for (Widget widget : visibleWidgets) {
        ColumnLayoutData layoutData = (ColumnLayoutData) BaseLayout.getLayoutData(widget);
        layoutData.setFlexibility(widget.getOffsetWidth());
      }

      ColumnLayoutData layoutDataL = (ColumnLayoutData) BaseLayout.getLayoutData(widgetL);
      ColumnLayoutData layoutDataR = (ColumnLayoutData) BaseLayout.getLayoutData(widgetR);

      layoutDataL.setFlexibility(sizeL);
      layoutDataR.setFlexibility(sizeR);

      super.dragEnd();

      // glassPanel.removeFromParent();

      WidgetHelper.layout(context.boundaryPanel);

      movablePanel.removeStyleName(getStylePrimaryName() + "-Movable");
    }

    protected BoundaryDropController newBoundaryDropController(
        AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
      if (allowDroppingOnBoundaryPanel) {
        throw new IllegalArgumentException();
      }
      return new BoundaryDropController(boundaryPanel, false);
    }

  }

  public ColumnLayoutSplitBar(AbsolutePanel boundaryPanel,
      List<Widget> visibleWidgets, Widget widgetL, Widget widgetR) {
    setElement(DOM.createSpan());
    setStyleName("SplitBar");
    this.visibleWidgets = visibleWidgets;
    this.widgetL = widgetL;
    this.widgetR = widgetR;

    // sinkEvents(Event.MOUSEEVENTS);

    dragController = new SplitBarDragController(boundaryPanel);

    dragController.setBehaviorConstrainedToBoundaryPanel(true);
    dragController.setBehaviorMultipleSelection(false);

    dragController.makeDraggable(this);
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
    return dragController.getBoundaryPanel();
  }

  public SplitBarDragController getDragController() {
    return dragController;
  }
}