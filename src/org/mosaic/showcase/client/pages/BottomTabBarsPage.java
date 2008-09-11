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
package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.TabLayoutPanel;
import org.mosaic.ui.client.TabLayoutPanel.TabBarPosition;
import org.mosaic.ui.client.layout.LayoutPanel;

/**
 * 
 */
@ShowcaseStyle({".mosaic-LayoutPanel"})
public class BottomTabBarsPage extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public BottomTabBarsPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    final TabLayoutPanel tabPanel = new TabLayoutPanel(TabBarPosition.BOTTOM, true);
    tabPanel.setPadding(5);

    LayoutPanel panel1 = new LayoutPanel();
    populate1(panel1);

    LayoutPanel panel2 = new LayoutPanel();
    populate2(panel2);

    LayoutPanel panel3 = new LayoutPanel();
    populate3(panel3);

    LayoutPanel panel4 = new LayoutPanel();
    populate4(panel4);

    tabPanel.add(panel1, "BoxLayout");
    tabPanel.add(panel2, "BorderLayout");
    tabPanel.add(panel3, "Nested BorderLayout");
    tabPanel.add(panel4, "Mixed Layout");

    layoutPanel.add(tabPanel);
  }

  @Override
  public String getName() {
    return "TabBar @ Bottom";
  }

}
