package gwt.mosaic.mobile.client;

import gwt.mosaic.mobile.client.events.AnimationEndEvent;
import gwt.mosaic.mobile.client.events.AnimationEndHandler;
import gwt.mosaic.mobile.client.events.AnimationStartEvent;
import gwt.mosaic.mobile.client.events.AnimationStartHandler;
import gwt.mosaic.mobile.client.events.HasAnimationEndHandlers;
import gwt.mosaic.mobile.client.events.HasAnimationHandlers;
import gwt.mosaic.mobile.client.events.HasAnimationStartHandlers;

import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.Widget;

public class Scrollable implements TouchStartHandler, AnimationEndHandler, AnimationStartHandler {

  public static enum ScrollDir {
    VERTICAL, HORIZONTAL, BOTH, FLIP
  }

  private int fixedHeaderHeight = 0;
  private int fixedFooterHeight = 0;
  // footer is view-local (as opposed to application-wide)
  private boolean isLocalFooter = false;
  // show scroll bar or not
  private boolean scrollBar = true;
  private ScrollDir scrollDir = ScrollDir.VERTICAL;
  // frictional drag
  private double weight = 0.6;
  private boolean fadeScrollBar = false;
  private boolean disableFlashScrollBar = false;

  private Widget domNode;
  private Widget containerNode;

  public Scrollable(Widget domNode, Widget containerNode) {
    assert domNode instanceof HasAnimationHandlers;
    assert containerNode instanceof HasTouchStartHandlers;
    
    this.domNode = domNode;
    this.containerNode = containerNode;

    ((HasTouchStartHandlers) containerNode).addTouchStartHandler(this);
    ((HasAnimationEndHandlers) domNode).addAnimationEndHandler(this);
    ((HasAnimationStartHandlers)domNode).addAnimationStartHandler(this);
    
    // TODO orientation change
    
    resizeView();
    
    // TODO flashScrollBar timer
  }
  
  private void resizeView() {
    
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onAnimationEnd(AnimationEndEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onAnimationStart(AnimationStartEvent event) {
    // TODO Auto-generated method stub
    
  }
}