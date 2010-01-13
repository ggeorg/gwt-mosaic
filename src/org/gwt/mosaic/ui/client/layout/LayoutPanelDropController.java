package org.gwt.mosaic.ui.client.layout;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractInsertPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LayoutPanelDropController extends
    AbstractInsertPanelDropController {

  /**
   * Label for IE quirks mode workaround.
   */
  private static final Label DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT = new Label(
      "x");

  LayoutPanelDropController(LayoutPanel dropTarget) {
    super(dropTarget);
  }

  @Override
  protected LocationWidgetComparator getLocationWidgetComparator() {
    return LocationWidgetComparator.BOTTOM_HALF_COMPARATOR;
  }

  @Override
  protected Widget newPositioner(DragContext context) {
    // Use two widgets so that setPixelSize() consistently affects dimensions
    // excluding positioner border in quirks and strict modes
    final SimplePanel outer = new SimplePanel();
    outer.addStyleName(DragClientBundle.INSTANCE.css().positioner());

    // place off screen for border calculation
    RootPanel.get().add(outer, -500, -500);

    // Ensure IE quirks mode returns valid outer.offsetHeight, and thus valid
    // DOMUtil.getVerticalBorders(outer)
    outer.setWidget(DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT);

    int width = 0;
    int height = 0;
    for (Widget widget : context.selectedWidgets) {
      width = Math.max(width, widget.getOffsetWidth());
      height += widget.getOffsetHeight();
    }

    final SimplePanel inner = new SimplePanel();
    inner.setPixelSize(width - DOMUtil.getHorizontalBorders(outer), height
        - DOMUtil.getVerticalBorders(outer));

    outer.setWidget(inner);

    return outer;
  }

  @Override
  public void onMove(DragContext context) {
    super.onMove(context);
    timer.schedule(33);
  }

  private Timer timer = new Timer() {
    @Override
    public void run() {
      ((LayoutPanel) dropTarget).invalidate();
      ((LayoutPanel) dropTarget).layout();
    }
  };
}
