/*
 * Copyright 2008 Fred Sauer
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
package com.allen_sauer.gwt.dnd.client.drop;

import java.util.ArrayList;

import org.mosaic.core.client.DOM;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link DropController} which allows a draggable widget to be placed at
 * specific (absolute) locations on an
 * {@link com.google.gwt.user.client.ui.AbsolutePanel} drop target.
 */
public class AbsolutePositionDropController extends AbstractPositioningDropController {

  static class Draggable {

    public int desiredX;

    public int desiredY;

    public int relativeX;

    public int relativeY;

    final int offsetHeight;

    final int offsetWidth;

    Widget positioner = null;

    final Widget widget;

    public Draggable(Widget widget) {
      this.widget = widget;
      offsetWidth = widget.getOffsetWidth();
      offsetHeight = widget.getOffsetHeight();
    }
  }

  private static final Label DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT = new Label("x");

  final ArrayList<Draggable> draggableList = new ArrayList<Draggable>();

  final AbsolutePanel dropTarget;

  int dropTargetClientHeight;

  int dropTargetClientWidth;

  int dropTargetOffsetX;

  int dropTargetOffsetY;

  public AbsolutePositionDropController(AbsolutePanel dropTarget) {
    super(dropTarget);
    this.dropTarget = dropTarget;
  }

  /**
   * Programmatically drop a widget on our drop target while obeying the
   * constraints of this controller.
   * 
   * @param widget the widget to be dropped
   * @param left the desired absolute horizontal location relative to our drop
   *            target
   * @param top the desired absolute vertical location relative to our drop
   *            target
   */
  public void drop(Widget widget, int left, int top) {
    left = Math.max(0, Math.min(left, dropTarget.getOffsetWidth()
        - widget.getOffsetWidth()));
    top = Math.max(0, Math.min(top, dropTarget.getOffsetHeight()
        - widget.getOffsetHeight()));
    dropTarget.add(widget, left, top);
  }

  @Override
  public void onDrop(DragContext context) {
    for (Draggable draggable : draggableList) {
      draggable.positioner.removeFromParent();
      dropTarget.add(draggable.widget, draggable.desiredX, draggable.desiredY);
    }
    super.onDrop(context);
  }

  @Override
  public void onEnter(DragContext context) {
    super.onEnter(context);
    assert draggableList.size() == 0;

    final int[] box = DOM.getClientSize(dropTarget.getElement());
    final int[] border = DOM.getBorderSizes(dropTarget.getElement());
    
    dropTargetClientWidth = box[0];
    dropTargetClientHeight = box[1];
    WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
    dropTargetOffsetX = dropTargetLocation.getLeft() + border[3];
    dropTargetOffsetY = dropTargetLocation.getTop() + border[0];

    int draggableAbsoluteLeft = context.draggable.getAbsoluteLeft();
    int draggableAbsoluteTop = context.draggable.getAbsoluteTop();
    for (Widget widget : context.selectedWidgets) {
      Draggable draggable = new Draggable(widget);
      draggable.positioner = makePositioner(widget);
      draggable.relativeX = widget.getAbsoluteLeft() - draggableAbsoluteLeft;
      draggable.relativeY = widget.getAbsoluteTop() - draggableAbsoluteTop;
      draggableList.add(draggable);
    }
  }

  @Override
  public void onLeave(DragContext context) {
    for (Draggable draggable : draggableList) {
      draggable.positioner.removeFromParent();
    }
    draggableList.clear();
    super.onLeave(context);
  }

  @Override
  public void onMove(DragContext context) {
    super.onMove(context);
    for (Draggable draggable : draggableList) {
      draggable.desiredX = context.desiredDraggableX - dropTargetOffsetX
          + draggable.relativeX;
      draggable.desiredY = context.desiredDraggableY - dropTargetOffsetY
          + draggable.relativeY;
      draggable.desiredX = Math.max(0, Math.min(draggable.desiredX, dropTargetClientWidth
          - draggable.offsetWidth));
      draggable.desiredY = Math.max(0, Math.min(draggable.desiredY,
          dropTargetClientHeight - draggable.offsetHeight));
      dropTarget.add(draggable.positioner, draggable.desiredX, draggable.desiredY);
    }
  }

  Widget makePositioner(Widget reference) {
    // Use two widgets so that setPixelSize() consistently affects dimensions
    // excluding positioner border in quirks and strict modes
    SimplePanel outer = new SimplePanel();
    outer.addStyleName(CSS_DRAGDROP_POSITIONER);
    outer.getElement().getStyle().setProperty("margin", "0px");

    // place off screen for border calculation
    RootPanel.get().add(outer, -500, -500);

    // Ensure IE quirks mode returns valid outer.offsetHeight, and thus valid
    // DOMUtil.getVerticalBorders(outer)
    outer.setWidget(DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT);

    final int[] border = DOM.getBorderSizes(outer.getElement());
    
    SimplePanel inner = new SimplePanel();
    inner.getElement().getStyle().setProperty("margin", "0px");
    inner.getElement().getStyle().setProperty("border", "none");
    int offsetWidth = reference.getOffsetWidth() - (border[1] + border[3]);
    int offsetHeight = reference.getOffsetHeight() - (border[0] + border[2]);
    inner.setPixelSize(offsetWidth, offsetHeight);

    outer.setWidget(inner);

    return outer;
  }
}
