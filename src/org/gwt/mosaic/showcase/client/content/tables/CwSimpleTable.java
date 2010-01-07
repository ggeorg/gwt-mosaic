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

import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.shared.Person;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.Table;
import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.IterableTableModel;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
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
    List<Person> data = new ArrayList<Person>();

    data.add(new Person("Rainer Zufall", "male", true));
    data.add(new Person("Marie Darms", "female", false));
    data.add(new Person("Holger Adams", "male", true));
    data.add(new Person("Juliane Adams", "female", true));

    final Table<Person> table = new Table<Person>(
        new IterableTableModel<Person>(data), createTableDefinition());
    table.setPageSize(2);
    // table.setContextMenu(createContextMenu());
    table.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        Window.alert(event.getSource().getClass().getName());
      }
    });

    return table;
  }

  /**
   * 
   * @return
   */
  @ShowcaseSource
  private TableDefinition<Person> createTableDefinition() {
    DefaultTableDefinition<Person> tableDef = new DefaultTableDefinition<Person>();
    // tableDef.setRowRenderer(new DefaultRowRenderer<Person>(new String[] {
    // "#f00", "#00f" }));

    DefaultColumnDefinition<Person, String> nameColDef = new DefaultColumnDefinition<Person, String>(
        "Name") {
      @Override
      public String getCellValue(Person rowValue) {
        return rowValue.getName();
      }
    };
    nameColDef.setColumnSortable(false);
    tableDef.addColumnDefinition(nameColDef);

    DefaultColumnDefinition<Person, String> genderColDef = new DefaultColumnDefinition<Person, String>(
        "Gender") {
      @Override
      public String getCellValue(Person rowValue) {
        return rowValue.getGender();
      }
    };
    genderColDef.setColumnSortable(false);
    tableDef.addColumnDefinition(genderColDef);

    return tableDef;
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
