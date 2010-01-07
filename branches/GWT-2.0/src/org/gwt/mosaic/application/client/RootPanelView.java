package org.gwt.mosaic.application.client;

import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

public class RootPanelView extends View {

  private Viewport viewport = new Viewport(new BorderLayout());

  public RootPanelView(Application application) {
    super(application);
    viewport.attach();
  }

  @Override
  public LayoutPanel getLayoutPanel() {
    return viewport.getLayoutPanel();
  }

}
