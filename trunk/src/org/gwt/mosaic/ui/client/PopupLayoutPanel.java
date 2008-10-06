package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.BaseLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupLayoutPanel extends PopupPanel {
  
  private final LayoutPanel panel;
  
  public PopupLayoutPanel() {
    this(false, false);
  }
  
  public PopupLayoutPanel(boolean autoHide) {
    this(autoHide, false);
  }
  
  public PopupLayoutPanel(boolean autoHide, boolean modal) {
    super(autoHide, modal);
    panel = new LayoutPanel();
    panel.setPadding(0);
    super.setWidget(panel);
  }
  
  @Override
  public void setWidget(Widget w) {
    panel.clear();
    panel.add(w);
  }

  @Override
  protected void onLoad() {
    final int[] box = DOM.getClientSize(getElement());
    final int[] m = DOM.getMarginSizes(panel.getElement());
    final int widthDelta = getOffsetWidth() - panel.getOffsetWidth();
    final int heightDelta = panel.getOffsetHeight() + m[0] + m[2]
        - BaseLayout.getFlowHeight(panel);
    // FIXME why (+ 1) ?
    setContentSize(box[0] - widthDelta, box[1] - heightDelta + 1);
    setSize("auto", "auto");
    panel.layout();
  }

  private void setContentSize(int width, int height) {
    DOM.setContentAreaWidth(panel.getElement(), width);
    DOM.setContentAreaHeight(panel.getElement(), height);
    // layoutTimer.schedule(333);
    panel.layout();
  }
  
}
