package gwt.mosaic.mobile.client;

import gwt.mosaic.mobile.client.View.Direction;
import gwt.mosaic.mobile.client.View.Transition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class Heading extends Widget implements HasText, HasClickHandlers {

  interface MyUiBinder extends UiBinder<HeadingElement, Heading> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
  
  private class Btn extends Widget {
    public Btn(DivElement elem) {
      setElement(elem);
    }
    
    @Override
    protected void onAttach() {
      super.onAttach();
    }
    
    @Override
    protected void onDetach() {
      super.onAttach();
    }
  }

  @UiField
  DivElement btn, _btn;

  @UiField
  DivElement head, _head;

  @UiField
  DivElement body, _body;

  @UiField
  SpanElement label;

  private String back;
  private String moveTo;
  private String text;

  private Transition transition = Transition.SLIDE;

  private boolean _started = false;
  
  private Btn _b; 

  public Heading() {
    setElement(uiBinder.createAndBindUi(this));
    
    _b = new Btn(btn);

    addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onButtonClick(event);
      }
    });
  }

  private final Timer btnTimer = new Timer() {
    @Override
    public void run() {
      removeStyleName("mblArrowButtonSelected");
    }
  };

  protected final void onButtonClick(ClickEvent event) {
    addStyleName("mblArrowButtonSelected");
    btnTimer.schedule(333);
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
    if (isAttached() && getParent() instanceof View) {
      View parent = (View) getParent();
      parent.performTransition(moveTo, Direction.REVERSE, transition);
    }
    // }
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    label.setInnerText(this.text = text);
  }

  public String getBack() {
    return back;
  }

  public void setBack(String back) {
    body.setInnerText(this.back = back);
    if (back != null) {
      btn.getStyle().setDisplay(Display.BLOCK);
      btn.getStyle().setWidth(body.getOffsetWidth() + head.getOffsetWidth(), Unit.PX);
      _btn.getStyle().setDisplay(Display.BLOCK);
      _btn.getStyle().setWidth(body.getOffsetWidth() + head.getOffsetWidth(), Unit.PX);
    } else {
      btn.getStyle().setDisplay(Display.NONE);
      _btn.getStyle().setDisplay(Display.NONE);
    }
  }

  public String getMoveTo() {
    return moveTo;
  }

  public void setMoveTo(String moveTo) {
    this.moveTo = moveTo;
  }

  public Transition getTransition() {
    return transition;
  }

  public void setTransition(Transition transition) {
    this.transition = transition;
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return _b.addDomHandler(handler, ClickEvent.getType());
  }

  @Override
  protected void onLoad() {
    _b.onAttach();
    startup();
  }
  
  @Override
  protected void onUnload() {
    _b.onDetach();
  }

  protected void startup() {
    if (_started) {
      return;
    }

    _started = true;

    if (back != null) {
      btn.getStyle().setWidth(body.getOffsetWidth() + head.getOffsetWidth(), Unit.PX);
      _btn.getStyle().setWidth(body.getOffsetWidth() + head.getOffsetWidth(), Unit.PX);
    }
  }
}
