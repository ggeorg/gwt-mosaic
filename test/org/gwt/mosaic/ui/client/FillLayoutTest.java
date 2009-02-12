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

import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author Alejandro D. Garin - agarin(at)gmail.com
 */
public class FillLayoutTest extends GWTTestCase {

  public String getModuleName() {
    return "org.gwt.mosaic.Mosaic";
  }
  public void testSimpleAdd() {
    final LayoutPanel fillLayout = new LayoutPanel();
    fillLayout.setPixelSize(200,150);
    fillLayout.setPadding(0);
    fillLayout.setWidgetSpacing(0);
    final Button button = new Button();
    fillLayout.add(button);
   
    RootPanel.get().add(fillLayout);
   
    assertEquals(FillLayout.class,fillLayout.getLayout().getClass());
    assertEquals(1,fillLayout.getWidgetCount());
    assertEquals(0,fillLayout.getWidgetSpacing());
    assertEquals(0,fillLayout.getPadding());
   
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        assertEquals(200,((Button)fillLayout.getWidget(0)).getOffsetWidth());
        assertEquals(150,((Button)fillLayout.getWidget(0)).getOffsetHeight());
        assertEquals(button,fillLayout.getWidget(0));
      }
    });    
  }
  public void testBounds() {
    final LayoutPanel fillLayout = new LayoutPanel();
    final Button button = new Button();
    fillLayout.add(button);
    TextArea textArea = new TextArea();
    fillLayout.add(textArea);
   
    assertEquals(2,fillLayout.getWidgetCount());
    assertEquals(button,fillLayout.getWidget(0));
    assertEquals(textArea,fillLayout.getWidget(1));
    assertEquals(0,fillLayout.getWidgetIndex(button));
    assertEquals(1,fillLayout.getWidgetIndex(textArea));
    try {
      fillLayout.getWidget(3);
      fail("should throw an IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      assertTrue(true);
    }
  }

}
