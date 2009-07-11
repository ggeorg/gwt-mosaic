/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class CwBoxLayoutTest1 extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwBoxLayoutTest1(CwConstants constants) {
    super(constants);
  }
  

  @Override
  public String getDescription() {
    return "Test1 description";
  }

  @Override
  public String getName() {
    return "Test1";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(5);

    final Button b11 = new Button("Button 11");
    final Button b12 = new Button("Button 12");
    final Button b13 = new Button("Button 13");
    final Button b14 = new Button("Button 14");

    final BoxLayout boxLayout1 = new BoxLayout(); // default is horizontal
    final LayoutPanel layoutPanel1 = new LayoutPanel(boxLayout1);

    layoutPanel1.add(b11, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b12, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b13, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel1.add(b14, new BoxLayoutData(FillStyle.VERTICAL));

    final Button b21 = new Button("Button 21");
    final Button b22 = new Button("Button 22");
    final Button b23 = new Button("Button 23");
    final Button b24 = new Button("Button 24");

    final BoxLayout boxLayout2 = new BoxLayout(); // default is horizontal
    boxLayout2.setLeftToRight(false);
    final LayoutPanel layoutPanel2 = new LayoutPanel(boxLayout2);

    layoutPanel2.add(b21, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b22, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b23, new BoxLayoutData(FillStyle.VERTICAL));
    layoutPanel2.add(b24, new BoxLayoutData(FillStyle.VERTICAL));

    layoutPanel.add(layoutPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    layoutPanel.add(layoutPanel2, new BoxLayoutData(FillStyle.BOTH, true));
    
    return layoutPanel;
  }
}
