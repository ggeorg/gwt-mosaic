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
package org.gwt.mosaic.showcase.client.content.forms;

import org.gwt.mosaic.forms.client.builder.FormLayoutPanel;
import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwComplexForm extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwComplexForm(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Complex Form description.";
  }

  @Override
  public String getName() {
    return "Complex Form";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel();

    final LayoutPanel formLayout = new LayoutPanel(new GridLayout(7, 6));
    
    // Create and configure a Builder
    final PanelBuilder builder = new PanelBuilder(new FormLayoutPanel() {
      public void add(Widget widget, GridLayoutData layoutData) {
        formLayout.add(widget, layoutData);
      }
    });
    builder.setHorizontalAlignment(PanelBuilder.ALIGN_RIGHT);
    builder.setDebug(true);

    // Fill the grid with components; the builder can create frequently used
    // components, e.g. separators and labels.
    
    // Add a titled separator to cell (1, 1) that spans 7 columns.
    builder.addSeparator("General", 7);
    
    builder.addLabel("Company");
    builder.add(new TextBox(), 6);
    builder.addLabel("Contact");
    builder.add(new TextBox(), 6);
    
    builder.addSeparator("Propeller", 7);
    
    builder.addLabel("PTI [kW]");
    builder.add(new TextBox(), 2);
    builder.addGap();
    builder.addLabel("Power [kW]");
    builder.add(new TextBox(), 2);
    
    builder.addLabel("R [mm]");
    builder.add(new TextBox(), 2);
    builder.addGap();
    builder.addLabel("D [mm]");
    builder.add(new TextBox(), 2);
    
    layoutPanel.add(formLayout);

    return layoutPanel;
  }

}
