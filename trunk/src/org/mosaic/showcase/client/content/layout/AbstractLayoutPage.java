/*
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client.content.layout;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.BorderLayoutData;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;

public abstract class AbstractLayoutPage extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public AbstractLayoutPage(DemoConstants constants) {
    super(constants);
  }

  protected void populate1(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(5);

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BoxLayout());
    layoutPanel1.setWidgetSpacing(2);

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    final BoxLayoutData blData = new BoxLayoutData(FillStyle.VERTICAL);
    blData.setWidth(100);
    layoutPanel1.add(b11, blData);
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b13);
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.BOTH));

    layoutPanel.add(b1, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(layoutPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel.add(b2, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(b3);
    layoutPanel.add(b4, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

  /**
   * 
   */
  @ShowcaseSource
  protected void populate2(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(b5, new BorderLayoutData(BorderLayoutRegion.CENTER, true));
  }

  /**
   * 
   */
  @ShowcaseSource
  protected void populate3(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BorderLayout());

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(layoutPanel1, new BorderLayoutData(BorderLayoutRegion.CENTER, true));

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");
    final Button b15 = new Button("Button 15");

    layoutPanel1.add(b11, new BorderLayoutData(BorderLayoutRegion.NORTH));
    layoutPanel1.add(b12, new BorderLayoutData(BorderLayoutRegion.SOUTH));
    layoutPanel1.add(b13, new BorderLayoutData(BorderLayoutRegion.WEST));
    layoutPanel1.add(b14, new BorderLayoutData(BorderLayoutRegion.EAST));
    layoutPanel1.add(b15, new BorderLayoutData(BorderLayoutRegion.CENTER));
  }

  /**
   * 
   */
  @ShowcaseSource
  protected void populate4(LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BorderLayout());

    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");

    final LayoutPanel layoutPanel1 = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    layoutPanel.add(b1, new BorderLayoutData(BorderLayoutRegion.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(BorderLayoutRegion.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(BorderLayoutRegion.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(BorderLayoutRegion.EAST, 10, 200));
    layoutPanel.add(layoutPanel1, new BorderLayoutData(BorderLayoutRegion.CENTER, true));

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");
    final Button b15 = new Button("Button 15");

    layoutPanel1.add(b11, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b13, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel1.add(b15, new BoxLayoutData(FillStyle.HORIZONTAL));
  }

}
