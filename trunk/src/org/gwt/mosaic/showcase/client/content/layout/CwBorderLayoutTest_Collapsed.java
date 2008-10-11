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
package org.gwt.mosaic.showcase.client.content.layout;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class CwBorderLayoutTest_Collapsed extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBorderLayoutTest_Collapsed(CwConstants constants) {
    super(constants);
  }
  
  @Override
  public String getDescription() {
    return "A BorderLayout test";
  }

  @Override
  public String getName() {
    return "Collapsed";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
    layoutPanel.setPadding(0);
    
    final CaptionLayoutPanel northPanel = new CaptionLayoutPanel("North");
    final ImageButton collapseBtn1 = new ImageButton(
        Caption.IMAGES.toolCollapseUp());
    northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);
    northPanel.add(new WidgetWrapper(new HTML("Height: 20%")));

    collapseBtn1.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        layoutPanel.setCollapsed(northPanel, !layoutPanel.isCollapsed(northPanel));
        layoutPanel.layout();
      }
    });
    
    final CaptionLayoutPanel southPanel = new CaptionLayoutPanel("South");
    final ImageButton collapseBtn2 = new ImageButton(
        Caption.IMAGES.toolCollapseDown());
    southPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);
    southPanel.add(new WidgetWrapper(new HTML("Height: 20%")));

    collapseBtn2.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        layoutPanel.setCollapsed(southPanel, !layoutPanel.isCollapsed(southPanel));
        layoutPanel.layout();
      }
    });

    final CaptionLayoutPanel westPanel = new CaptionLayoutPanel("West");
    final ImageButton collapseBtn3 = new ImageButton(
        Caption.IMAGES.toolCollapseLeft());
    westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
    westPanel.add(new WidgetWrapper(new HTML("Width: 20%")));

    collapseBtn3.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        layoutPanel.setCollapsed(westPanel, !layoutPanel.isCollapsed(westPanel));
        layoutPanel.layout();
      }
    });

    final CaptionLayoutPanel eastPanel = new CaptionLayoutPanel("East");
    final ImageButton collapseBtn4 = new ImageButton(
        Caption.IMAGES.toolCollapseRight());
    eastPanel.getHeader().add(collapseBtn4, CaptionRegion.RIGHT);
    eastPanel.add(new WidgetWrapper(new HTML("Width: 20%")));

    collapseBtn4.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        layoutPanel.setCollapsed(eastPanel, !layoutPanel.isCollapsed(eastPanel));
        layoutPanel.layout();
      }
    });

    final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel("Center");
    centerPanel.getHeader().add(Showcase.IMAGES.gwtLogoThumb().createImage());
    centerPanel.add(new WidgetWrapper(new HTML("<h1>GWT Mosaic</h1>")));
    
    layoutPanel.add(northPanel, new BorderLayoutData(BorderLayoutRegion.NORTH, 0.20, true));
    layoutPanel.setCollapsed(northPanel, true);
    
    layoutPanel.add(southPanel, new BorderLayoutData(BorderLayoutRegion.SOUTH,
        0.20, true));
    layoutPanel.setCollapsed(southPanel, true);
    
    layoutPanel.add(westPanel, new BorderLayoutData(BorderLayoutRegion.WEST, 0.2, true));
    layoutPanel.setCollapsed(westPanel, true);
    
    layoutPanel.add(eastPanel, new BorderLayoutData(BorderLayoutRegion.EAST, 0.2, true));
    layoutPanel.setCollapsed(eastPanel, true);
    
    layoutPanel.add(centerPanel, new BorderLayoutData(true));
    
    return layoutPanel;
  }
  
}
