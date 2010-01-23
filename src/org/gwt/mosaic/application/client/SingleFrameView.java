package org.gwt.mosaic.application.client;

import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

public class SingleFrameView extends View {
  private final Viewport viewport = new Viewport(new BorderLayout());

  public SingleFrameView(Application application) {
    super(application);
    viewport.attach();
  }

  @Override
  protected LayoutPanel getLayoutPanel() {
    return viewport.getLayoutPanel();
  }
}
