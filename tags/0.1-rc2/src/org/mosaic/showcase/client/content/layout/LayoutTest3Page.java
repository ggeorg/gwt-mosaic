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
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;

/**
 * 
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class LayoutTest3Page extends Page {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public LayoutTest3Page(DemoConstants constants) {
    super(constants);
    // TODO Auto-generated constructor stub
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final Grid grid = new Grid(2, 2);
    
    grid.getCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_LEFT,
        HasAlignment.ALIGN_TOP);
    grid.setWidget(0, 0, new Button("Top Left"));
    
    grid.getCellFormatter().setAlignment(0, 1, HasAlignment.ALIGN_RIGHT,
        HasAlignment.ALIGN_TOP);
    grid.setWidget(0, 1, new Button("Top Right"));
    
    grid.getCellFormatter().setAlignment(1, 0, HasAlignment.ALIGN_LEFT,
        HasAlignment.ALIGN_BOTTOM);
    grid.setWidget(1, 0, new Button("Bottom Left"));
    
    grid.getCellFormatter().setAlignment(1, 1, HasAlignment.ALIGN_RIGHT,
        HasAlignment.ALIGN_BOTTOM);
    grid.setWidget(1, 1, new Button("Bottom Right"));
    
    layoutPanel.add(grid);
  }

  @Override
  public String getName() {
    return "Test3";
  }

}