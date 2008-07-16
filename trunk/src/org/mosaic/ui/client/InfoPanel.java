package org.mosaic.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.mosaic.core.client.DOM;
import org.mosaic.core.client.util.DelayedRunnable;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Displays information in the bottom region of the browser for a specified
 * amount of time.
 */
public class InfoPanel extends DecoratedPopupPanel implements HasText {

  public enum InfoPanelType {
    HUMANIZED_MESSAGE, TRAY_NOTIFICATION
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-InfoPanel";

  public static final int DEFAULT_DELAY = 3333; // microseconds
  public static final int WIDTH = 224;

  public static final int HEIGHT = 72;

  private static final List<InfoPanel> SLOTS = new ArrayList<InfoPanel>();

  private static int firstAvail() {
    int size = SLOTS.size();
    for (int i = 0; i < size; i++) {
      if (SLOTS.get(i) == null) {
        return i;
      }
    }
    return size;
  }

  private static void show(final InfoPanel infoPanel, final int delayMsec, final int level) {
    final int cw = Window.getClientWidth();
    final int ch = Window.getClientHeight();

    final int left = (cw - WIDTH - 20);
    final int top = ch - HEIGHT - 20 - (level * (HEIGHT + 20));

    if (top < 0) {
      new DelayedRunnable() {
        public void run() {
          InfoPanel.SLOTS.set(level, null);
          InfoPanel.show(infoPanel.caption.getText(), infoPanel.description.getText());
        }
      };
    } else {
      infoPanel.setPopupPosition(left, top);
      infoPanel.show();
      infoPanel.hideTimer.scheduleRepeating(delayMsec / 10);
    }
  }

  public static void show(InfoPanelType type, String caption, String text,
      String... values) {
    if (type == InfoPanelType.TRAY_NOTIFICATION) {
      show(caption, text, values);
    } else {
      // if (text != null && values != null) text = Format.substitute(text,
      // values);

      final InfoPanel infoPanel = new InfoPanel(caption, text, true);
      infoPanel.addPopupListener(new PopupListener() {
        public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
          // Nothing to do here!
        }
      });
      infoPanel.center();
    }
  }

  public static void show(String caption, String text, String... values) {
    final int avail = firstAvail();
    // if (text != null && values != null) text = Format.substitute(text,
    // values);

    InfoPanel infoPanel = new InfoPanel(caption, text);
    infoPanel.addPopupListener(new PopupListener() {
      public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        SLOTS.set(avail, null);
      }
    });
    SLOTS.add(avail, infoPanel);

    show(infoPanel, DEFAULT_DELAY, avail);
  }

  private Label caption, description;

  private final Timer hideTimer = new Timer() {
    private int opacity = 100;

    public void run() {
      updateOpacity();
      if (opacity == 10) {
        hideTimer.cancel();
        InfoPanel.this.hide();
      } else {
        opacity -= 10;
      }
    }

    private void updateOpacity() {
      final Element elem = InfoPanel.this.getElement();
      DOM.setStyleAttribute(elem, "opacity", (new Double(opacity / 100.0)).toString());
      DOM.setStyleAttribute(elem, "filter", "alpha(opacity=" + opacity + ");");
    }
  };

  public InfoPanel() {
    this(null);
  }

  protected InfoPanel(String caption) {
    this(caption, null);
  }

  protected InfoPanel(String caption, String description) {
    this(caption, description, false);
  }

  protected InfoPanel(String caption, String description, boolean autoHide) {
    super(autoHide, false); // modal=false
    ensureDebugId("mosaicInfoPanel-simplePopup");
    if (autoHide) {
      //final int width = Window.getClientWidth();
      //InfoPanel.this.setWidth(Math.max(width / 3, WIDTH) + "px");
      setWidth(WIDTH + "px");
    } else {
      setWidth(WIDTH + "px");
    }
    setAnimationEnabled(true);

    this.caption = new Label(caption);
    this.caption.setStyleName(DEFAULT_STYLENAME + "-caption");

    this.description = new Label(description);
    this.description.setStyleName(DEFAULT_STYLENAME + "-description");

    FlowPanel panel = new FlowPanel();
    panel.setStyleName(DEFAULT_STYLENAME + "-panel");
    panel.setPixelSize(WIDTH, HEIGHT);
    DOM.setStyleAttribute(panel.getElement(), "overflow", "hidden");

    SimplePanel div1 = new SimplePanel();
    div1.add(this.caption);

    SimplePanel div2 = new SimplePanel();
    div2.add(this.description);

    panel.add(div1);
    panel.add(div2);

    setWidget(panel);

    addStyleName(DEFAULT_STYLENAME);
  }

  public String getCaption() {
    return caption.getText();
  }

  public String getText() {
    return description.getText();
  }

  public void setCaption(String caption) {
    this.caption.setText(caption);
  }

  public void setText(String text) {
    this.description.setText(text);
  }

}
