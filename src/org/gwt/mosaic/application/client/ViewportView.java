package org.gwt.mosaic.application.client;

import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Window;

public class ViewportView extends View {

  // TODO logger
  private Viewport viewport = null;

  public ViewportView(Application application) {
    super(application);
  }

  /**
   * Return the {@link Viewport} used to show this view.
   * 
   * @return this application's viewport
   */
  public Viewport getViewport() {
    if (viewport == null) {
      viewport = new Viewport(new BorderLayout());
      Window.setTitle(getContext().getConstants().applicationTitle());
    }
    return viewport;
  }

  /**
   * Sets the {@link Viewport} use to show this {@code View}.
   * <p>
   * This method should be called from the startup method by a subclass that
   * wants to construct and initialize the viewport itself. TODO ...
   * 
   * @param viewport
   */
  public void setViewport(Viewport viewport) {
    if (viewport == null) {
      throw new IllegalArgumentException("null Viewport");
    }
    if (this.viewport != null) {
      throw new IllegalStateException("viewport already set");
    }
    this.viewport = viewport;
    firePropertyChange("viewport", null, this.viewport);
  }

  @Override
  public LayoutPanel getLayoutPanel() {
    return getViewport().getLayoutPanel();
  }

}
