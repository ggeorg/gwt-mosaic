package org.gwt.mosaic.application.client;

import org.gwt.mosaic.ui.client.Viewport;

import com.google.gwt.user.client.ui.Widget;

/**
 * An application base class for simple GUIs with one primary frame.
 * <p>
 * This class takes care of component property injection, exit processing, and
 * saving/restoring session state.
 * <p>
 * To use {@code SimpleFrameApplication}, one need only override
 * {@link #startup()}, create the GUI's main {@link Viewport} and attach it to
 * the {@code RootPanel}.
 * <p>
 * The resources bundle should contain definitions for all of the standard
 * application resources, as well the main frame's title and the label's text.
 * 
 * <pre>
 * # MyApplication.properties
 * Application.id = MyApplication
 * Application.title = My Hello World Application
 * Application.version = 1.0
 * Application.vendor = GWT Mosaic, Inc
 * Application.vendorId = GWTMosaic
 * Application.homepage = http://www.google.com
 * Application.description = An example of SingleFrameApplication
 * Application.lookAndFeel = system
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class SingleFrameApplication extends Application {
  // TODO Logger

  /**
   * Return the {@link Viewport} used to show this application.
   * <p>
   * This method may be called at any time.
   * 
   * @return this application's {@link Viewport}
   */
  public final Viewport getViewport() {
    return getMainView().getViewport();
  }

  /**
   * Sets the {@link Viewport} used to show this application.
   * <p>
   * This method should be called from the startup method by a subclass that
   * wants to construct and initialize the {@link Viewport} itself.
   * <p>
   * If the main frame property was already initialized, either implicitly
   * through a call to {@code #getViewport()} or by explicitly calling this
   * method, an {@code IllegalStateException} is thrown. If {@link Viewport} is
   * {@code null}, an {@code IllegalArgumentException} is thrown.
   * 
   * @param viewport the new value of the viewport property
   * @see #getViewport()
   */
  protected final void setViewport(Viewport viewport) {
    getMainView().setViewport(viewport);
  }

  /**
   * Show the specified widget in the {@link #getViewport() viewport}. Typical
   * applications will call this method after constructing their main GUI panel
   * in the {@link #startup()} method.
   * <p>
   * Before the viewport is attached, the properties of all the widgets in the
   * hierarchy are initialized TODO
   * 
   * @param w the viewport's child
   * 
   * @throws IllegalStateException if {@code w} is {@code null}
   */
  protected void show(Widget w) {
    if (w == null) {
      throw new IllegalArgumentException("null Widget");
    }
    Viewport viewport = getViewport();
    viewport.getLayoutPanel().add(w);
    viewport.attach();
  }

  /* Prototype support for the View type */

  private ViewportView mainView = null;

  public ViewportView getMainView() {
    if (mainView == null) {
      mainView = new ViewportView(this);
    }
    return mainView;
  }
}
