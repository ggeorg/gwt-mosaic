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
 * @author luciano.broussal(at)gmail.com
 */
public class InfoPanel extends DecoratedPopupPanel implements HasText, WindowResizeListener, PopupListener {

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

    private GlassPanel glassPanel;


    private Label caption, description;

    private final Timer hideTimer = new Timer() {
	public void run() {
	    InfoPanel.this.hide();
	}
    };

  private boolean autoHide;

    public InfoPanel() {
	this(null);
    }

    protected InfoPanel(String caption) {
	this(caption, null);
    }

    public InfoPanel(String caption, String description) {
	this(caption, description, false);
    }

    public InfoPanel(String caption, String description, final boolean autoHide) {
	super(autoHide, false); // modal=false
	ensureDebugId("mosaicInfoPanel-simplePopup");

	setAnimationEnabled(true);

    this.autoHide = autoHide;

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
	if (glassPanel != null) {
	    glassPanel.removeFromParent();
	}
    }

    @Override
    public void hide() {
	super.hide();
    }

    @Override
    public void show() {
	super.show();
    if (!autoHide) {
	hideTimer.schedule(DEFAULT_DELAY);
    }
  }

    public void showModal() {
	if (glassPanel == null) {
	    glassPanel = new GlassPanel(false);
	    glassPanel.addStyleName("mosaic-GlassPanel-default");
	}
	RootPanel.get().add(glassPanel, 0, 0);
	center();
	addPopupListener(this);

    }

}
