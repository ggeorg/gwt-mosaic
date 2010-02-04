/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.GlassPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
final class BorderLayoutSplitBar extends Widget implements HasAllMouseHandlers {
  private static GlassPanel glassPanel;
  private final SplitBarDragController dragController;

  final class SplitBarDragController extends AbstractDragController {

    private Widget widget = null;

    private int minWidth, maxWidth, minHeight, maxHeight;

    private Widget movablePanel;

    private int draggableOldAbsoluteLeft, draggableOldAbsoluteTop;

    private BorderLayoutData layoutData = null;

    private int boundaryOffsetX, boundaryOffsetY;
    private int dropTargetClientHeight, dropTargetClientWidth;

    public SplitBarDragController(AbsolutePanel boundaryPanel, Widget widget) {
      super(boundaryPanel);
      this.widget = widget;
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
      final Dimension box = DOM.getClientSize(context.boundaryPanel.getElement());
      dropTargetClientWidth = box.width;
      dropTargetClientHeight = box.height;

      layoutData = (BorderLayoutData) widget.getLayoutData();

      final LayoutPanel layoutPanel = (LayoutPanel) context.boundaryPanel;

      int minSize = 0;
      int maxSize = 0;

      if (layoutData.minSize != null && layoutData.maxSize != null) {
        if (layoutData.region == Region.NORTH
            || layoutData.region == Region.SOUTH) {
          minSize = layoutPanel.toPixelSize(layoutData.minSize, false);
          maxSize = layoutPanel.toPixelSize(layoutData.maxSize, false);
        }
        if (layoutData.region == Region.WEST
            || layoutData.region == Region.EAST) {
          minSize = layoutPanel.toPixelSize(layoutData.minSize, true);
          maxSize = layoutPanel.toPixelSize(layoutData.maxSize, true);
        }
      }

      if (maxSize > minSize) {
        if (layoutData.region == Region.NORTH
            || layoutData.region == Region.SOUTH) {
          minWidth = 0;
          maxWidth = Integer.MAX_VALUE;
          minHeight = minSize;
          maxHeight = maxSize;
        }
        if (layoutData.region == Region.WEST
            || layoutData.region == Region.EAST) {
          minWidth = minSize;
          maxWidth = maxSize;
          minHeight = 0;
          maxHeight = Integer.MAX_VALUE;
        }
      } else {
        minWidth = layoutPanel.toPixelSize(DOM.getComputedStyleAttribute(
            widget.getElement(), "minWidth"), true);
        maxWidth = layoutPanel.toPixelSize(DOM.getComputedStyleAttribute(
            widget.getElement(), "maxWidth"), true);
        maxWidth = maxWidth <= minWidth ? Integer.MAX_VALUE : maxWidth;

        minHeight = layoutPanel.toPixelSize(DOM.getComputedStyleAttribute(
            widget.getElement(), "minHeight"), false);
        maxHeight = layoutPanel.toPixelSize(DOM.getComputedStyleAttribute(
            widget.getElement(), "maxHeight"), false);
        maxHeight = maxHeight <= minHeight ? Integer.MAX_VALUE : maxHeight;
      }

      draggableOldAbsoluteLeft = context.draggable.getAbsoluteLeft();
      draggableOldAbsoluteTop = context.draggable.getAbsoluteTop();
    }
    
    public void dragMove() {
      Region region = layoutData.getRegion();
      if (region == Region.NORTH) {
        int desiredTop = context.desiredDraggableY - boundaryOffsetY;
        desiredTop = Math.max(0, Math.min(desiredTop, dropTargetClientHeight
            - context.draggable.getOffsetHeight()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(),
            context.draggable.getAbsoluteLeft() - boundaryOffsetX, desiredTop);
      } else if (region == Region.SOUTH) {
        int desiredTop = context.desiredDraggableY - boundaryOffsetY;
        desiredTop = Math.max(0, Math.min(desiredTop, dropTargetClientHeight
            - context.draggable.getOffsetHeight()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(),
            context.draggable.getAbsoluteLeft() - boundaryOffsetX, desiredTop);
      }
      if (region == Region.WEST) {
        int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
        desiredLeft = Math.max(0, Math.min(desiredLeft, dropTargetClientWidth
            - context.draggable.getOffsetWidth()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
            context.draggable.getAbsoluteTop() - boundaryOffsetY);
      } else if (region == Region.EAST) {
        int desiredLeft = context.desiredDraggableX - boundaryOffsetX;
        desiredLeft = Math.max(0, Math.min(desiredLeft, dropTargetClientWidth
            - context.draggable.getOffsetWidth()));
        DOMUtil.fastSetElementPosition(movablePanel.getElement(), desiredLeft,
            context.draggable.getAbsoluteTop() - boundaryOffsetY);
      }
    }

    @Override
    public void dragEnd() {
      try {
        Region region = layoutData.getRegion();
        if (region == Region.NORTH) {
          int delta = context.draggable.getAbsoluteTop()
              - draggableOldAbsoluteTop;
          int height = widget.getOffsetHeight() + delta;
          height = Math.min(Math.max(height, minHeight), maxHeight);
          layoutData.setPreferredHeight(height + "px");
        } else if (region == Region.SOUTH) {
          int delta = draggableOldAbsoluteTop
              - context.draggable.getAbsoluteTop();
          int height = widget.getOffsetHeight() + delta;
          height = Math.min(Math.max(height, minHeight), maxHeight);
          layoutData.setPreferredHeight(height + "px");
        }
        if (region == Region.WEST) {
          int delta = context.draggable.getAbsoluteLeft()
              - draggableOldAbsoluteLeft;
          int width = widget.getOffsetWidth() + delta;
          width = Math.min(Math.max(width, minWidth), maxWidth);
          layoutData.setPreferredWidth(width + "px");
        } else if (region == Region.EAST) {
          int delta = draggableOldAbsoluteLeft
              - context.draggable.getAbsoluteLeft();
          int width = widget.getOffsetWidth() + delta;
          width = Math.min(Math.max(width, minWidth), maxWidth);
          layoutData.setPreferredWidth(width + "px");
        }

        super.dragEnd();

        glassPanel.removeFromParent();

        WidgetHelper.invalidate(context.boundaryPanel);
        WidgetHelper.layout(context.boundaryPanel);

        movablePanel.removeStyleName(getStylePrimaryName() + "-Movable");
      } catch (Exception ex) {
        Window.alert(ex.toString());
      }
    }

    public void makeDraggable(Widget widget) {
      super.makeDraggable(widget);
    }

    protected BoundaryDropController newBoundaryDropController(
        AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
      if (allowDroppingOnBoundaryPanel) {
        throw new IllegalArgumentException();
      }
      return new BoundaryDropController(boundaryPanel, false);
    }

  }

  public BorderLayoutSplitBar(AbsolutePanel boundaryPanel, Widget widget) {
    setElement(DOM.createSpan());
    sinkEvents(Event.MOUSEEVENTS);

    dragController = new SplitBarDragController(boundaryPanel, widget);
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
