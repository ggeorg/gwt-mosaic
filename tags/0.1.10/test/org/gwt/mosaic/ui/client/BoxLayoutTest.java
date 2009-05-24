/*
 * Copyright (c) 2009 GWT Mosaic Alejandro D. Garin.
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

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Alejandro D. Garin - agarin(at)gmail.com
 */
public class BoxLayoutTest extends GWTTestCase {

  public String getModuleName() {
    return "org.gwt.mosaic.Mosaic";
  }

  public void testCreateBoxLayout() {
    final LayoutPanel fillBrowserLayout = new LayoutPanel(new FillLayout());
    fillBrowserLayout.setPixelSize(Window.getClientWidth(), Window.getClientHeight());   
    fillBrowserLayout.setWidgetSpacing(0);
    fillBrowserLayout.setPadding(0); 

    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));

    layoutPanel.setWidgetSpacing(0);
    layoutPanel.setPadding(0);     

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    b3.setWidth("70px");
    final Button b4 = new Button("Button 4");

    final LayoutPanel horizontalPanel = new LayoutPanel(new BoxLayout());
    horizontalPanel.setWidgetSpacing(0);
    horizontalPanel.setPadding(0); 

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    final BoxLayoutData blData = new BoxLayoutData(FillStyle.VERTICAL);
    blData.setPreferredWidth(100);
    horizontalPanel.add(b11, blData);
    horizontalPanel.add(b12, new BoxLayoutData(FillStyle.VERTICAL));
    horizontalPanel.add(b13);
    horizontalPanel.add(b14, new BoxLayoutData(FillStyle.BOTH));

    layoutPanel.add(b1, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(horizontalPanel, new BoxLayoutData(FillStyle.BOTH, false));
    layoutPanel.add(b2, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(b3);
    layoutPanel.add(b4, new BoxLayoutData(FillStyle.HORIZONTAL));

    fillBrowserLayout.add(layoutPanel);

    RootPanel.get().add(fillBrowserLayout);

    Timer timer = new Timer() {
      public void run() {
        int clientWidth = Window.getClientWidth();

        assertEquals(0, layoutPanel.getWidgetSpacing());

        assertEquals(clientWidth, b1.getOffsetWidth());
        assertEquals(clientWidth, b2.getOffsetWidth());
        assertEquals("Button11 prefered width", 100, b11.getOffsetWidth());
        assertEquals("Button3 width", 70, b3.getOffsetWidth());
        int b14width = clientWidth - b11.getOffsetWidth() - b12.getOffsetWidth() - b13.getOffsetWidth();
        assertEquals(
            "Button14 should be the diferente between client width and the other buttons",
            b14width, b14.getOffsetWidth());

        assertEquals(horizontalPanel.getOffsetHeight(), b2.getOffsetHeight());

        finishTest();
      }
    };

    delayTestFinish(500);
    timer.schedule(300);      
  }
}
