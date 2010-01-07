package org.gwt.mosaic.application.client;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionEvent;
import org.gwt.mosaic.actions.client.ActionMap;
import org.gwt.mosaic.ui.client.Viewport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
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
  public static final String SHOW_HISTORY = "history.token";

  public SingleFrameApplication() {
    super();

    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        final ActionMap actionMap = getContext().getActionMap();
        final Action action = actionMap.get(event.getValue());
        if (action != null) {
          action.actionPerformed(new ActionEvent(action,
              Application.getInstance()));
        }
      }
    });
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
    show(w, false);
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
   * @param decorate
   * 
   * @throws IllegalStateException if {@code w} is {@code null}
   */
  protected void show(Widget w, boolean decorate) {
    showImpl(w, decorate);
    if (w instanceof HasHistoryToken) {
      final String token = ((HasHistoryToken) w).getHistoryToken();
      History.newItem(token);
    }
  }

  protected void showImpl(Widget w, boolean decorate) {
    if (w == null) {
      throw new IllegalArgumentException("null Widget");
    }

    getMainView().setWidget(w, decorate);
  }

  /* Prototype support for the View type */

  private RootPanelView mainView = null;

  public RootPanelView getMainView() {
    if (mainView == null) {
      mainView = new RootPanelView(this);
    }
    return mainView;
  }

  public void startup(String historyToken) {
    if ("".equals(History.getToken())) {
      History.newItem(historyToken);
    } else {
      History.fireCurrentHistoryState();
    }
  }
}
