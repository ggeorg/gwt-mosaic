/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.core.client.util.DelayedRunnable;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * Displays a loading message and adds a gray overlay.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class LoadingPanel extends PopupPanel implements ResizeHandler,
    CloseHandler<PopupPanel> {

  public static LoadingPanel show(String text) {
    return show(text, false);
  }

  public static LoadingPanel show(String text, boolean asHTML) {
    if (asHTML) {
      return show(new HTML(text));
    } else {
      return show(new Label(text));
    }
  }

  public static LoadingPanel show(Widget w) {
    return show(null, w);
  }

  public static LoadingPanel show(Widget targetWidget, String text) {
    return show(targetWidget, text, false);
  }

  public static LoadingPanel show(Widget targetWidget, String text,
      boolean asHTML) {
    if (asHTML) {
      return show(targetWidget, new HTML(text));
    } else {
      return show(targetWidget, new Label(text));
    }
  }

  private GlassPanel glassPanel;

  private final Widget targetWidget;

  private final AbsolutePanel glassPanelParent;

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-LoadingPanel";

  public static LoadingPanel show(Widget targetWidget, Widget w) {
    final LoadingPanel loadingPanel = new LoadingPanel(targetWidget);
    loadingPanel.setWidget(w);
    if (loadingPanel.glassPanel == null) {
      loadingPanel.glassPanel = new GlassPanel(false);
      loadingPanel.glassPanel.addStyleName("mosaic-GlassPanel-loading");
      DOM.setStyleAttribute(loadingPanel.glassPanel.getElement(), "zIndex",
          DOM.getComputedStyleAttribute(loadingPanel.getElement(), "zIndex"));
    }
    if (loadingPanel.glassPanelParent == null) {
      RootPanel.get().add(loadingPanel.glassPanel, 0, 0);
    } else {
      RootPanel.get().add(loadingPanel.glassPanelParent);
      loadingPanel.glassPanelParent.add(loadingPanel.glassPanel, 0, 0);
      loadingPanel.adjustGlassPanelBounds();
    }
    loadingPanel.center();
    loadingPanel.addCloseHandler(loadingPanel);
    return loadingPanel;
  }

  private HandlerRegistration resizeHandlerRegistration = null;

  protected LoadingPanel(Widget targetWidget) {
    super(false, false);
    ensureDebugId("mosaicInfoPanel-simplePopup");

    this.targetWidget = (RootPanel.get() != targetWidget) ? targetWidget : null;
    glassPanelParent = (this.targetWidget != null) ? new AbsolutePanel() : null;

    setAnimationEnabled(true);

    resizeHandlerRegistration = Window.addResizeHandler(this);

    addStyleName(DEFAULT_STYLENAME);
    DOM.setIntStyleAttribute(getElement(), "zIndex", Integer.MAX_VALUE);
  }

  protected void adjustGlassPanelBounds() {
    if (glassPanelParent == null) {
      return;
    }
    final Dimension size = WidgetHelper.getOffsetSize(targetWidget);
    RootPanel.get().setWidgetPosition(glassPanelParent,
        targetWidget.getAbsoluteLeft(), targetWidget.getAbsoluteTop());
    glassPanelParent.setPixelSize(size.width, size.height);
    glassPanel.removeFromParent();
    glassPanelParent.add(glassPanel, 0, 0);
  }

  /**
   * Centers the popup in the browser window and shows it. If the popup was
   * already showing, then the popup is centered.
   */
  public void center() {
    setPopupPositionAndShow(new PositionCallback() {
      public void setPosition(int offsetWidth, int offsetHeight) {
        if (glassPanelParent == null) {
          int left = (Window.getClientWidth() - offsetWidth) >> 1;
          int top = (Window.getClientHeight() - offsetHeight) >> 1;
          setPopupPosition(Window.getScrollLeft() + left, Window.getScrollTop()
              + top);
        } else {
          int left = (glassPanelParent.getOffsetWidth() - offsetWidth) >> 1;
          int top = (glassPanelParent.getOffsetHeight() - offsetHeight) >> 1;
          setPopupPosition(glassPanelParent.getAbsoluteLeft()
              + glassPanelParent.getElement().getScrollLeft() + left,
              glassPanelParent.getAbsoluteTop()
                  + glassPanelParent.getElement().getScrollTop() + top);
        }
      }
    });
  }

  public Widget getBoudaryWidget() {
    return targetWidget;
  }

  public void onClose(CloseEvent<PopupPanel> event) {
    resizeHandlerRegistration.removeHandler();
    if (glassPanelParent != null) {
      glassPanelParent.removeFromParent();
      glassPanelParent.setSize("", "");
    }
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        glassPanel.removeFromParent();
      }
    });
  }

  public void onResize(ResizeEvent event) {
    new DelayedRunnable() {
      public void run() {
        center();
        adjustGlassPanelBounds();
      }
    };
  }

}
