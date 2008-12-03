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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class CwBorderLayout extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBorderLayout(CwConstants constants) {
    super(constants);
  }
  
  @Override
  public String getDescription() {
    return "BorderLayout description";
  }

  @Override
  public String getName() {
    return "BorderLayout";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());
    
    final Button b1 = new Button("Button 1");
    final Button b2 = new Button("Button 2");
    final Button b3 = new Button("Button 3");
    final Button b4 = new Button("Button 4");
    final Button b5 = new Button("Button 5");
    
    layoutPanel.add(b1, new BorderLayoutData(Region.NORTH, 10, 200));
    layoutPanel.add(b2, new BorderLayoutData(Region.SOUTH, 10, 200));
    layoutPanel.add(b3, new BorderLayoutData(Region.WEST, 10, 200));
    layoutPanel.add(b4, new BorderLayoutData(Region.EAST, 10, 200));
    layoutPanel.add(b5, new BorderLayoutData(Region.CENTER, true));
    
    return layoutPanel;
  }
  
}
