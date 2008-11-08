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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Region;
import org.gwt.mosaic.ui.client.layout.BaseLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractDecoratedPopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A popup panel that can position itself relative to another widget.
 * 
 * @param <R> relative widget type
 */
public class DropDownPanel extends AbstractDecoratedPopupPanel {

  private static final String DEFAULT_STYLENAME = "mosaic-DropDownPanel gwt-MenuBarPopup";

  private final LayoutPanel panel;

  // private final Timer layoutTimer = new Timer() {
  // public void run() {
  // panel.layout();
  // }
  // };

  private Widget relativeWidget;

  /**
   * Default constructor.
   */
  public DropDownPanel(Widget relativeWidget) {
    super(false, false, "menuPopup");
    this.relativeWidget = relativeWidget;
    panel = new LayoutPanel();
    panel.setPadding(0);
    super.setWidget(panel);
    setStyleName(DEFAULT_STYLENAME);
    // Issue 5 fix (ggeorg)
    DOM.setIntStyleAttribute(getElement(), "zIndex", Integer.MAX_VALUE);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Widget getWidget() {
    if (panel.getWidgetCount() > 0) {
      return panel.getWidget(0);
    } else {
      return null;
    }
  }

  @Override
  public boolean onEventPreview(Event event) {
    final Element target = event.getTarget();

    boolean eventTargetsPopup = (target != null)
        && getElement().isOrHasChild(target);

    boolean eventTargetsAnchor = (target != null) && (relativeWidget != null)
        && relativeWidget.getElement().isOrHasChild(target);

    final int type = DOM.eventGetType(event);
    switch (type) {
      case Event.ONMOUSEDOWN:
        if (!eventTargetsPopup && !eventTargetsAnchor) {
          hide(true);
          return true;
        }
        break;
    }
    return super.onEventPreview(event);
  }

  @Override
  protected void onLoad() {
    final int[] box1 = DOM.getClientSize(relativeWidget.getElement());
    final int[] box2 = DOM.getClientSize(getElement());
    final int[] m = DOM.getMarginSizes(panel.getElement());
    final int widthDelta = getOffsetWidth() - panel.getOffsetWidth();
    final int heightDelta = panel.getOffsetHeight() + m[0] + m[2]
        - BaseLayout.getFlowHeight(panel);
    // FIXME why (+ 1) ?
    setContentSize(box1[0] - widthDelta, box2[1] - heightDelta + 1);
    setSize("auto", "auto");
    panel.layout();
  }

  private void setContentSize(int width, int height) {
    DOM.setContentAreaWidth(panel.getElement(), width);
    DOM.setContentAreaHeight(panel.getElement(), height);
    // layoutTimer.schedule(333);
    panel.layout();
  }

  @Override
  public void setWidget(Widget w) {
    panel.clear();
    panel.add(w);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.PopupPanel#show()
   */
  @Override
  public void show() {
    Region r = DOM.getRegion(relativeWidget.getElement());
    setPopupPosition(r.left, r.bottom);
    super.show();
  }
}
