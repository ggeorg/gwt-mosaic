package gwt.mosaic.mobile.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class Button extends Widget implements HasText, HasClickHandlers {

  public static enum ButtonClass {
    BLUE {
      @Override
      public String getButtonClass() {
        return "mblBlueButton";
      }
    };

    public abstract String getButtonClass();
  }

  private ButtonClass buttonClass;
  private int duration = 1000; // duration of selection, milliseconds

  private String text = null;

  public Button() {
    setElement(Document.get().createPushButtonElement());
    setStyleName("mblButton");
    setButtonClass(ButtonClass.BLUE);

    addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        Button.this.onClick(event);
      }
    });
  }

  private Timer btnTimer = new Timer() {
    @Override
    public void run() {
      Button.this.removeStyleName("bmlButtonSelected");
      Button.this.removeStyleName(Button.this.buttonClass.getButtonClass() + "Selected");
    }
  };

  protected final void onClick(ClickEvent event) {
    addStyleName("bmlButtonSelected");
    addStyleName(Button.this.buttonClass.getButtonClass() + "Selected");
    btnTimer.schedule(duration);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    getElement().setInnerText(this.text = text);
  }

  public ButtonClass getButtonClass() {
    return buttonClass;
  }

  public void setButtonClass(ButtonClass buttonClass) {
    if (this.buttonClass == buttonClass) {
      return;
    }
    if (this.buttonClass != null) {
      removeStyleName(this.buttonClass.getButtonClass());
    }
    addStyleName((this.buttonClass = buttonClass).getButtonClass());
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

}
