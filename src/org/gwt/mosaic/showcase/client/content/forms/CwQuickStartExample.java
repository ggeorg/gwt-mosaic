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

import org.gwt.mosaic.forms.client.builder.PanelBuilder;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwQuickStartExample extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwQuickStartExample(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Demonstrates the typical steps how to work with the FormLayout.";
  }

  @Override
  public String getName() {
    return "Quick Start";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new ScrollLayoutPanel();

    // Create a FormLayout instance on the given column and row specs.
    // For almost all forms you specify the columns; sometimes rows are
    // created dynamically. In this case the labels are right aligned.
    FormLayout layout = new FormLayout(
        "right:pref, 3dlu, pref, 7dlu, right:pref, 3dlu, pref", // cols
        "p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 3dlu, p"); // rows

    // Specify that columns 1 & 5 as well as 3 & 7 have equal widths.
    layout.setColumnGroups(new int[][] { {1, 5}, {3, 7}});

    // Create a builder that assists in adding components to the container.
    // Wrap the panel with a standardized border.
    PanelBuilder builder = new PanelBuilder(layout);
    // builder.setDefaultDialogBorder();

    // Fill the grid with components; the builder offers to create
    // frequently used components, e.g. separators and labels.

    builder.addSeparator("General", CellConstraints.xyw(1, 1, 7));

    builder.addLabel("Company", CellConstraints.xy(1, 3));
    builder.add(new TextBox(), CellConstraints.xyw(3, 3, 5));
    builder.addLabel("Contact", CellConstraints.xy(1, 5));
    builder.add(new TextBox(), CellConstraints.xyw(3, 5, 5));

    builder.addSeparator("Proppeler", CellConstraints.xyw(1, 7, 7));

    builder.addLabel("PTI/kW", CellConstraints.xy(1, 9));
    builder.add(new TextBox(), CellConstraints.xy(3, 9));
    builder.addLabel("Power/kW", CellConstraints.xy(5, 9));
    builder.add(new TextBox(), CellConstraints.xy(7, 9));
    builder.addLabel("R/mm", CellConstraints.xy(1, 11));
    builder.add(new TextBox(), CellConstraints.xy(3, 11));
    builder.addLabel("D/mm", CellConstraints.xy(5, 11));
    builder.add(new TextBox(), CellConstraints.xy(7, 11));

    // The builder holds the layout container that we now return.
    layoutPanel.add(builder.getPanel());

    return layoutPanel;
  }

}
