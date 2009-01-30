/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.util.DelayedRunnable;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * Displays information in the bottom region of the browser for a specified
 * amount of time.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class InfoPanel extends DecoratedPopupPanel implements HasText,
    WindowResizeListener, PopupListener {

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

  private static void show(final InfoPanel infoPanel, final int delayMsec,
      final int level) {
    final int cw = Window.getClientWidth();
    final int ch = Window.getClientHeight();

    final int left = (cw - WIDTH - 20);
    final int top = ch - HEIGHT - 20 - (level * (HEIGHT + 20));

    if (top < 0) {
      new DelayedRunnable() {
        public void run() {
          InfoPanel.SLOTS.set(level, null);
          InfoPanel.show(infoPanel.caption.getText(),
              infoPanel.description.getText());
        }
      };
    } else {
      infoPanel.setPopupPosition(left, top);
      infoPanel.show();
      infoPanel.hideTimer.scheduleRepeating(delayMsec);
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
      if (infoPanel.glassPanel == null) {
        infoPanel.glassPanel = new GlassPanel(false);
        infoPanel.glassPanel.addStyleName("mosaic-GlassPanel-default");
        DOM.setStyleAttribute(infoPanel.glassPanel.getElement(), "zIndex",
            DOM.getStyleAttribute(infoPanel.getElement(), "zIndex"));
      }
      RootPanel.get().add(infoPanel.glassPanel, 0, 0);
      infoPanel.center();
      infoPanel.addPopupListener(infoPanel);
    }
  }

  private GlassPanel glassPanel;

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
    public void run() {
      InfoPanel.this.hide();
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

  protected InfoPanel(String caption, String description, final boolean autoHide) {
    super(autoHide, false); // modal=false
    ensureDebugId("mosaicInfoPanel-simplePopup");

    setAnimationEnabled(true);

    this.caption = new Label(caption);
    this.caption.setStyleName(DEFAULT_STYLENAME + "-caption");

    this.description = new Label(description);
    this.description.setStyleName(DEFAULT_STYLENAME + "-description");

    final FlowPanel panel = new FlowPanel();
    panel.setStyleName(DEFAULT_STYLENAME + "-panel");
    if (autoHide) {
      final int width = Window.getClientWidth();
      panel.setPixelSize(Math.max(width / 3, WIDTH), HEIGHT);
      Window.addWindowResizeListener(this);
    } else {
      panel.setPixelSize(WIDTH, HEIGHT);
    }
    DOM.setStyleAttribute(panel.getElement(), "overflow", "hidden");

    SimplePanel div1 = new SimplePanel();
    div1.add(this.caption);

    SimplePanel div2 = new SimplePanel();
    div2.add(this.description);

    panel.add(div1);
    panel.add(div2);

    setWidget(panel);

    addStyleName(DEFAULT_STYLENAME);
    DOM.setIntStyleAttribute(getElement(), "zIndex", Integer.MAX_VALUE);
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

  public void onWindowResized(int width, int height) {
    new DelayedRunnable(333) {
      public void run() {
        final int width = Window.getClientWidth();
        getWidget().setPixelSize(Math.max(width / 3, WIDTH), HEIGHT);
        center();
      }
    };
  }

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    Window.removeWindowResizeListener(this);
    glassPanel.removeFromParent();
  }

}
