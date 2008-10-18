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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.GridLayout;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwSimpleForm extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSimpleForm(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Simple Form description.";
  }

  @Override
  public String getName() {
    return "Simple Form";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel();
    
    final LayoutPanel formLayout = new LayoutPanel(new GridLayout(7, 3));
    
    formLayout.add(new Label("Label1"));
    formLayout.add(new TextBox(), new GridLayoutData(6, 1));
    
    formLayout.add(new Label("Label2"));
    formLayout.add(new TextBox(), new GridLayoutData(5, 1));
    formLayout.add(new Label(""));
    
    formLayout.add(new Label("Label3"));
    formLayout.add(new TextBox(), new GridLayoutData(5, 1));
    formLayout.add(new Button("..."));
    
    layoutPanel.add(formLayout, new BoxLayoutData(FillStyle.HORIZONTAL));

    return layoutPanel;
  }

}
