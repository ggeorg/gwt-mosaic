package org.gwt.mosaic.ui.client.layout;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

class LayoutPanelDragController extends PickupDragController {

  LayoutPanelDragController(LayoutPanel boundaryPanel,
      boolean allowDroppingOnBoundaryPanel) {
    super(boundaryPanel, allowDroppingOnBoundaryPanel);
  }

  @Override
  public void dragEnd() {
    super.dragEnd();

    ((LayoutPanel) getBoundaryPanel()).layout();
  }

}
