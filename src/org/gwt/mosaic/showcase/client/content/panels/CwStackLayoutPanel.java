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
package org.gwt.mosaic.showcase.client.content.panels;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.layout.AbstractLayoutPage;
import org.gwt.mosaic.ui.client.StackLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle({".mosaic-StackLayoutPanel", ".mosaic-LayoutPanel"})
public class CwStackLayoutPanel extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwStackLayoutPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "StackLayoutPanel description";
  }

  @Override
  public String getName() {
    return "StackLayoutPanel";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(5);

    // Create a StackLayoutPanel
    final StackLayoutPanel stack = new StackLayoutPanel();
    stack.setAnimationEnabled(true);

    final LayoutPanel panel1 = new LayoutPanel();
    populate1(panel1);
    stack.add(panel1, "BoxLayout");

    final LayoutPanel panel2 = new LayoutPanel();
    populate2(panel2);
    stack.add(panel2, "BorderLayout");

    final LayoutPanel panel3 = new LayoutPanel();
    populate3(panel3);
    stack.add(panel3, "Nested BorderLayout");

    final LayoutPanel panel4 = new LayoutPanel();
    populate4(panel4);
    stack.add(panel4, "Mixed Layout");

    layoutPanel.add(stack, new BoxLayoutData(FillStyle.BOTH, true));

    stack.showStack(0);
    
    return layoutPanel;
  }
  
}
