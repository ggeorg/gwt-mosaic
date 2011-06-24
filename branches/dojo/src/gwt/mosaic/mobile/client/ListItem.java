package gwt.mosaic.mobile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;

public class ListItem extends AbstractItem {

  interface MyUiBinder extends UiBinder<LIElement, ListItem> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  ImageElement iconNode;

  @UiField
  AnchorElement anchorNode;

  @UiField
  DivElement textBoxNode;

  @UiField
  DivElement rightTextBoxNode;

  @UiField
  DivElement arrowNode;

  private boolean _started = false;

  private String rightText;
  private boolean selected;

  private boolean anchorLabel = false;

  public ListItem() {
    setElement(uiBinder.createAndBindUi(this));

    textBoxNode.getStyle().setCursor(Cursor.POINTER);
    textBoxNode.getStyle().setDisplay(Display.INLINE);

    addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        onItemClick(event);
      }
    });
  }

  private final Timer anchorTimer = new Timer() {
    @Override
    public void run() {
      textBoxNode.removeClassName("mblListItemTextBoxSelected");
    }
  };

  private final Timer itemTimer = new Timer() {
    @Override
    public void run() {
      removeStyleName("mblItemSelected");
    }
  };

  protected final void onItemClick(ClickEvent event) {
    if (getStyleName().contains(" mblItemSelected ")) {
      // already selected
      return;
    }

    EventTarget target = event.getNativeEvent().getEventTarget();
    if (isAnchorLabel(target)) {
      textBoxNode.addClassName("mblListItemTextBoxSelected");
      anchorTimer.schedule(333);
      return;
    }

    addStyleName("mblItemSelected");
    itemTimer.schedule(333);

    super.onItemClick(event);
  }

  protected boolean isAnchorLabel(EventTarget target) {
    if (anchorLabel && target != null) {
      Node targetNode = target.cast();
      return anchorNode.isOrHasChild(targetNode);
    } else {
      return false;
    }
  }

  @Override
  protected void onLoad() {
    startup();
  }

  protected void startup() {
    if (_started) {
      return;
    }

    _started = true;
  }

  @Override
  public void setText(String text) {
    super.setText(text);
    textBoxNode.setInnerText(text);
    if (text != null) {
      textBoxNode.getStyle().setDisplay(Display.BLOCK);
    } else {
      textBoxNode.getStyle().setDisplay(Display.NONE);
    }
  }

  public String getRightText() {
    return rightText;
  }

  public void setRightText(String rightText) {
    rightTextBoxNode.setInnerText(this.rightText = rightText);
    if (rightText != null) {
      rightTextBoxNode.getStyle().setDisplay(Display.BLOCK);
    } else {
      rightTextBoxNode.getStyle().setDisplay(Display.NONE);
    }
  }

  @Override
  public void setMoveTo(String moveTo) {
    super.setMoveTo(moveTo);
    if (getMoveTo() != null) {
      arrowNode.getStyle().setDisplay(Display.BLOCK);
    } else {
      arrowNode.getStyle().setDisplay(Display.NONE);
    }
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
    if (selected) {
      addStyleName("mblItemSelected");
    } else {
      removeStyleName("mblItemSelected");
    }
  }

  @Override
  public void setIcon(String icon) {
    super.setIcon(icon);
    if (icon != null) {
      this.iconNode.setSrc(getIcon());
      anchorNode.removeClassName("mblListItemAnchorNoIcon");
      iconNode.getStyle().setProperty("display", "");
    } else {
      anchorNode.addClassName("mblListItemAnchorNoIcon");
      iconNode.getStyle().setDisplay(Display.NONE);
    }
  }

  public void setIconResource(ImageResource res) {
    setIcon(res.getURL());
  }

}
