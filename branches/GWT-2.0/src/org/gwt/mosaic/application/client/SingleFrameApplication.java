package org.gwt.mosaic.application.client;

import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionEvent;
import org.gwt.mosaic.actions.client.ActionMap;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
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
  
  private SingleFrameView mainView = null;

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
   * Show the specified widget. Typical applications will call this method in
   * the {@link #startup()} method.
   * 
   * @param w the widget to show in the application's main panel
   */
  protected void show(Widget w) {
    show(w, false);
  }

  /**
   * Show the specified widget. Typical applications will call this method in
   * the {@link #startup()} method.
   * 
   * @param w the widget to show in the application's main panel
   * @param decorate {@code true} if the widget to show should be decorated
   */
  protected void show(Widget w, boolean decorate) {
    showImpl(w, decorate);
    if (w instanceof HasHistoryToken) {
      final String token = ((HasHistoryToken) w).getHistoryToken();
      History.newItem(token);
    }
  }
  
  public void startup(String historyToken) {
    if ("".equals(History.getToken())) {
      History.newItem(historyToken);
    } else {
      History.fireCurrentHistoryState();
    }
  }

  private void showImpl(Widget w, boolean decorate) {
    assert w != null;

    getMainView().setWidget(w, decorate);
  }

  public View getMainView() {
    if (mainView == null) {
      mainView = new SingleFrameView(this);
    }
    return mainView;
  }
}
