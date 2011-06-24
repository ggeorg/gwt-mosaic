package gwt.mosaic.mobile.client;

import gwt.mosaic.core.client.UserAgent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasTouchEndHandlers;
import com.google.gwt.event.dom.client.HasTouchMoveHandlers;
import com.google.gwt.event.dom.client.HasTouchStartHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class Switch extends Widget {

  private class InnerNode extends Widget implements HasClickHandlers, HasMouseDownHandlers,
      HasTouchStartHandlers, HasTouchMoveHandlers, HasTouchEndHandlers {
    public InnerNode(DivElement elem) {
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

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
      return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
      return addDomHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
      return addDomHandler(handler, TouchStartEvent.getType());
    }

    @Override
    public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
      return addDomHandler(handler, TouchMoveEvent.getType());
    }

    @Override
    public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
      return addDomHandler(handler, TouchEndEvent.getType());
    }
  }

  public static enum Value {
    ON, OFF
  }

  interface MyUiBinder extends UiBinder<DivElement, Switch> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  DivElement innerNode;

  @UiField
  DivElement lNode;

  @UiField
  DivElement lTextNode;

  @UiField
  DivElement rNode;

  @UiField
  DivElement rTextNode;

  @UiField
  DivElement knobNode;

  private Value value;
  private String leftValue = "ON";
  private String rightValue = "OFF";

  private InnerNode _innerNode, _knobNode;

  private int _width = 53, _innerStartX, _touchStartX;
  private boolean _moved;

  public Switch() {
    setElement(uiBinder.createAndBindUi(this));
    setLeftValue(leftValue);
    setRightValue(rightValue);
    setValue(Value.OFF);

    _innerNode = new InnerNode(innerNode);
    _knobNode = new InnerNode(knobNode);

    _knobNode.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (_moved) {
          return;
        }
        setValue(getValue() == Value.ON ? Value.OFF : Value.ON);
      }
    });
    _knobNode.addMouseDownHandler(new MouseDownHandler() {
      @Override
      public void onMouseDown(MouseDownEvent event) {
        _moved = false;
        _innerStartX = innerNode.getOffsetLeft();
        lNode.getStyle().setDisplay(Display.BLOCK);
        rNode.getStyle().setDisplay(Display.BLOCK);
        event.stopPropagation();
      }
    });
    _knobNode.addTouchStartHandler(new TouchStartHandler() {
      @Override
      public void onTouchStart(TouchStartEvent event) {
        _moved = false;
        _innerStartX = innerNode.getOffsetLeft();
        if (event.getTargetTouches() != null) {
          _touchStartX = event.getTargetTouches().get(0).getClientX();
        }
        lNode.getStyle().setDisplay(Display.BLOCK);
        rNode.getStyle().setDisplay(Display.BLOCK);
        event.stopPropagation();
      }
    });
    _innerNode.addTouchMoveHandler(new TouchMoveHandler() {
      @Override
      public void onTouchMove(TouchMoveEvent event) {
        event.preventDefault();
        int dx = 0;
        if (event.getTargetTouches() != null) {
          if (event.getTargetTouches().length() != 1) {
            event.stopPropagation();
            return;
          }
          dx = event.getTargetTouches().get(0).getClientX() - _touchStartX;
        } else {
          dx = event.getNativeEvent().getClientX() - _touchStartX;
        }
        int pos = _innerStartX + dx;
        int d = 10;
        if (pos <= -(_width - d)) {
          pos = -_width;
        }
        if (pos >= -d) {
          pos = 0;
        }
        innerNode.getStyle().setLeft(pos, Unit.PX);
        _moved = true;
      }
    });
    _innerNode.addTouchEndHandler(new TouchEndHandler() {
      @Override
      public void onTouchEnd(TouchEndEvent event) {
        if (_innerStartX == innerNode.getOffsetLeft()) {
          if (UserAgent.isWebkit()) {
            // var ev = dojo.doc.createEvent("MouseEvents");
            // ev.initEvent("click", true, true);
            // this.knob.dispatchEvent(ev);
          }
          return;
        }
        Value newState = (innerNode.getOffsetLeft() < -(_width / 2)) ? Value.OFF : Value.ON;
        setValue(newState);
        if (newState != value) {
          value = newState;
        }
      }
    });
  }

  @Override
  protected void onLoad() {
    _innerNode.onAttach();
    _knobNode.onAttach();
  }

  @Override
  protected void onUnload() {
    _innerNode.onDetach();
    _knobNode.onDetach();
  }

  public Value getValue() {
    return value;
  }

  private final Timer animationTimer = new Timer() {
    @Override
    public void run() {
      if (value == Value.OFF) {
        lNode.getStyle().setDisplay(Display.NONE);
      } else {
        rNode.getStyle().setDisplay(Display.NONE);
      }
      removeStyleName("mblSwitchAnimation");
    }
  };

  public void setValue(Value value) {
    innerNode.getStyle().setProperty("left", "");
    addStyleName("mblSwitchAnimation");
    removeStyleName(this.value == Value.ON ? "mblSwitchOn" : "mblSwitchOff");
    addStyleName((this.value = value) == Value.ON ? "mblSwitchOn" : "mblSwitchOff");
    animationTimer.schedule(333);
  }

  public String getLeftValue() {
    return leftValue;
  }

  public void setLeftValue(String leftValue) {
    lTextNode.setInnerText(this.leftValue = leftValue);
  }

  public String getRightValue() {
    return rightValue;
  }

  public void setRightValue(String rightValue) {
    rTextNode.setInnerText(this.rightValue = rightValue);
  }

}
