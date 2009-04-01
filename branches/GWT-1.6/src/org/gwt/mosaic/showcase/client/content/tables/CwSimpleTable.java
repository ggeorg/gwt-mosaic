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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.Table;
import org.gwt.mosaic.ui.client.Table.PagingOptions;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.table.DefaultTableColumnModel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwSimpleTable extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwSimpleTable(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Simple Table description.";
  }

  @Override
  public String getName() {
    return "Simple Table";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final LayoutPanel vBox = new LayoutPanel(
        new BoxLayout(Orientation.VERTICAL));
    vBox.setPadding(0);
    //vBox.setWidgetSpacing(0);

    String[] columnNames = {
        "First Name", "Last name", "Sport", "# of Years", "Vegetarian"};

    Object[][] data = {
        {"Mary", "Campione", "Snowboarding", new Integer(5), new Boolean(false)},
        {"Alison", "Huml", "Rowing", new Integer(3), new Boolean(true)},
        {"Kathy", "Walrath", "Knitting", new Integer(2), new Boolean(false)},
        {
            "Sharon", "Zakhour", "Speed reading", new Integer(20),
            new Boolean(true)},
        {"Philip", "Milne", "Pool", new Integer(10), new Boolean(false)}};

    final Table<String[]> table = new Table<String[]>(data,
        new DefaultTableColumnModel<String[]>(columnNames));
    table.setPageSize(2);
    // table.setContextMenu(createContextMenu());

    // Create an options panel
    PagingOptions pagingOptions = new PagingOptions(table);

    // vBox.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
    vBox.add(table, new BoxLayoutData(FillStyle.BOTH));
    vBox.add(pagingOptions);

    return vBox;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private PopupMenu createContextMenu() {
    Command cmd = new Command() {
      public void execute() {
        InfoPanel.show("Menu Button", "You selected a menu item!");
      }
    };

    PopupMenu contextMenu = new PopupMenu();

    contextMenu.addItem("MenuItem 1", cmd);
    contextMenu.addItem("MenuItem 2", cmd);

    contextMenu.addSeparator();

    contextMenu.addItem("MenuItem 3", cmd);
    contextMenu.addItem("MenuItem 4", cmd);

    return contextMenu;
  }
  
}
