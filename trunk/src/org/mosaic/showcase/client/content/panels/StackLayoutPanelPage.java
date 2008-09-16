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
package org.mosaic.showcase.client.content.panels;

import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.showcase.client.content.layout.AbstractLayoutPage;
import org.mosaic.ui.client.StackLayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

/**
 * Example file.
 */
@ShowcaseStyle({".mosaic-DockLayoutPanel", ".mosaic-LayoutPanel"})
public class StackLayoutPanelPage extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public StackLayoutPanelPage(DemoConstants constants) {
    super(constants);
  }

  /**
   * <code>DeckLayoutPanel</code> example code.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(final LayoutPanel layoutPanel) {
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(5);

    // Create a DeckLayoutPanel
    final StackLayoutPanel stack = new StackLayoutPanel();

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
  }

  @Override
  public String getName() {
    return "StackLayoutPanel";
  }

}
