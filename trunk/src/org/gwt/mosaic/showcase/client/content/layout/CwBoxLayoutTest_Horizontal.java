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
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Alignment;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class CwBoxLayoutTest_Horizontal extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBoxLayoutTest_Horizontal(CwConstants constants) {
    super(constants);
  }
  

  @Override
  public String getDescription() {
    return "Horizontal BoxLayout Test (with scrollbar)";
  }

  @Override
  public String getName() {
    return "Horizontal";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel(new BoxLayout());
    ((BoxLayout)layoutPanel.getLayout()).setAlignment(Alignment.CENTER);

    final Button b1 = new Button("Width: float<br>Height: float");
    final Button b2 = new Button("Width: float<br>Height: 25%");
    final Button b3 = new Button("Width: float<br>Height: 50%");
    final Button b4 = new Button("Width: float<br>Height: 75%");
    final Button b5 = new Button("Width: 50%<br>Height: 50%");
    final Button b6 = new Button("Width: 100px<br>Height: 100px");
    final Button b7 = new Button("Width: float<br>Height: fill");
    
    layoutPanel.add(b1);
    layoutPanel.add(b2, new BoxLayoutData(-1.0, 0.25));
    layoutPanel.add(b3, new BoxLayoutData(-1.0, 0.5));
    layoutPanel.add(b4, new BoxLayoutData(-1.0, 0.75));
    layoutPanel.add(b5, new BoxLayoutData(0.5, 0.5));
    layoutPanel.add(b6, new BoxLayoutData(100.0, 100.0));
    layoutPanel.add(b7, new BoxLayoutData(FillStyle.VERTICAL));
    
    return layoutPanel;
  }

}
