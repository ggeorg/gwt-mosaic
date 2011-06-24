package gwt.mosaic.showcase.client;

import gwt.mosaic.mobile.client.Viewport;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtMosaic implements EntryPoint {

  interface MyUiBinder extends UiBinder<Viewport, GwtMosaic> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    uiBinder.createAndBindUi(this);
  }
}
