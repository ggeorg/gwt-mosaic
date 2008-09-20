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
package org.gwt.mosaic.showcase.client.content.panels;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.layout.AbstractLayoutPage;
import org.gwt.mosaic.ui.client.DeckLayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle({".mosaic-DockLayoutPanel", ".mosaic-LayoutPanel"})
public class CwDeckLayoutPanel extends AbstractLayoutPage {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDeckLayoutPanel(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "DeckLayoutPanel description";
  }

  @Override
  public String getName() {
    return "DeckLayoutPanel";
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
    layoutPanel.setWidgetSpacing(5);

    // Create a DeckLayoutPanel
    final DeckLayoutPanel deck = new DeckLayoutPanel();
    deck.setPadding(5);

    final LayoutPanel panel1 = new LayoutPanel();
    populate1(panel1);
    deck.add(panel1);

    final LayoutPanel panel2 = new LayoutPanel();
    populate2(panel2);
    deck.add(panel2);

    final LayoutPanel panel3 = new LayoutPanel();
    populate3(panel3);
    deck.add(panel3);

    final LayoutPanel panel4 = new LayoutPanel();
    populate4(panel4);
    deck.add(panel4);

    // Add a drop box with the list types
    final ListBox dropBox = new ListBox(false);
    dropBox.addItem("BoxLayout");
    dropBox.addItem("BorderLayout");
    dropBox.addItem("Nested BorderLayout");
    dropBox.addItem("Mixed Layout");
    dropBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        deck.showWidget(dropBox.getSelectedIndex());
        deck.layout();
      }
    });

    layoutPanel.add(dropBox, new BoxLayoutData(FillStyle.HORIZONTAL));
    layoutPanel.add(deck, new BoxLayoutData(FillStyle.BOTH, true));

    deck.showWidget(dropBox.getSelectedIndex());
    
    return layoutPanel;
  }

}
