package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class WidgetWrapper extends Composite {

  private static final String DEFAULT_STYLE_NAME = "mosaic-WidgetWrapper";

  public WidgetWrapper(Widget widget) {
    Grid grid = new Grid(1, 1);
    grid.setBorderWidth(0);
    grid.setCellPadding(0);
    grid.setCellSpacing(0);

    grid.setWidget(0, 0, widget);

    initWidget(grid);

    addStyleName(DEFAULT_STYLE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  protected Grid getWidget() {
    return (Grid) super.getWidget();
  }

}
