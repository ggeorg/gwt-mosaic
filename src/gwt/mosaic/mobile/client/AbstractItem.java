package gwt.mosaic.mobile.client;

import gwt.mosaic.mobile.client.View.Direction;
import gwt.mosaic.mobile.client.View.Transition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractItem extends Widget implements HasText, HasClickHandlers {

  private String icon;
  private String iconPos; // top, left, width, height (ex. "0,0,29,29")
  private String href;
  private String hrefTarget;
  private String moveTo;
  private String scene;
  private boolean clickable;
  private String url;
  private String urlTarget; // node id under which a new view is created
  private Transition transition = Transition.SLIDE;
  private Direction transitionDir = Direction.FORWARD;
  private boolean sync;
  private String text;
  private boolean toggle = false;

  private int _duration = 800; // duration of selection, milliseconds

  protected void onItemClick(ClickEvent event) {
    View moveToView = View.findByElementId(moveTo);
    if (moveToView != null) {
      goTo(moveToView);
    } else {
      GWT.log("View '" + moveTo + "' not found!");
    }
  }

  protected void goTo(View moveTo) {
    // TODO if(href) {
    // } else {
    // TODO full mobile app
    if (isAttached()) {
      View.getCurrentView().performTransition(moveTo, transitionDir, transition);
    }
    // }
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public String getMoveTo() {
    return moveTo;
  }

  public void setMoveTo(String moveTo) {
    this.moveTo = moveTo;
  }

}
