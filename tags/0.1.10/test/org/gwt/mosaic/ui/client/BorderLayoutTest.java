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

import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Alejandro D. Garin - agarin(at)gmail.com
 */
public class BorderLayoutTest extends GWTTestCase {

  public String getModuleName() {
    return "org.gwt.mosaic.Mosaic";
  }
  public void testCreateBorderLayout() {

    final LayoutPanel fillBrowserLayout = new LayoutPanel(new FillLayout());
    fillBrowserLayout.setPixelSize(Window.getClientWidth(), Window.getClientHeight());   
    fillBrowserLayout.setWidgetSpacing(0);
    fillBrowserLayout.setPadding(0);    

    final LayoutPanel boxLayoutPanel = new LayoutPanel(new BorderLayout());
    boxLayoutPanel.setWidgetSpacing(0);
    boxLayoutPanel.setPadding(0);    

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");

    boxLayoutPanel.add(b1, new BorderLayoutData(Region.NORTH, 10, 200));
    boxLayoutPanel.add(b2, new BorderLayoutData(Region.SOUTH, 10, 200));
    boxLayoutPanel.add(b3, new BorderLayoutData(Region.WEST, 10, 200));
    boxLayoutPanel.add(b4, new BorderLayoutData(Region.EAST, 10, 200));
    boxLayoutPanel.add(b5, new BorderLayoutData(Region.CENTER));  

    fillBrowserLayout.add(boxLayoutPanel);
    RootPanel.get().add(fillBrowserLayout);

    assertEquals(BorderLayout.class,boxLayoutPanel.getLayout().getClass());  
    assertEquals(5,boxLayoutPanel.getWidgetCount());

    Timer timer = new Timer() {
      public void run() {
        int clientWidth = Window.getClientWidth();   
        int clientHeight = Window.getClientHeight();
        int spacing = boxLayoutPanel.getWidgetSpacing();
        int padding = boxLayoutPanel.getPadding();

        assertEquals(0,spacing);
        assertEquals(0,padding);

        assertEquals(clientWidth,boxLayoutPanel.getOffsetWidth());
        assertEquals(clientWidth,boxLayoutPanel.getWidget(0).getOffsetWidth());
        assertEquals(clientWidth,boxLayoutPanel.getWidget(1).getOffsetWidth());

        assertEquals(b1.getOffsetHeight(),boxLayoutPanel.getWidget(0).getOffsetHeight());
        assertEquals(b2.getOffsetHeight(),boxLayoutPanel.getWidget(1).getOffsetHeight());
        assertEquals(b3.getOffsetHeight(),boxLayoutPanel.getWidget(2).getOffsetHeight());
        assertEquals(b4.getOffsetHeight(),boxLayoutPanel.getWidget(3).getOffsetHeight());
        assertEquals(b5.getOffsetHeight(),boxLayoutPanel.getWidget(4).getOffsetHeight());

        int northAndSouth = b1.getOffsetHeight() + b2.getOffsetHeight();
        assertEquals(clientHeight - northAndSouth, b3.getOffsetHeight());
        assertEquals(clientHeight - northAndSouth, b4.getOffsetHeight());
        assertEquals(clientHeight - northAndSouth, b5.getOffsetHeight());

        int buttonsHeight = b1.getOffsetHeight() + b2.getOffsetHeight() + b5.getOffsetHeight();
        assertEquals(Window.getClientHeight(),buttonsHeight);
        finishTest();
      }
    };    
    delayTestFinish(500);
    timer.schedule(300);
  }
}
