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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.LiveTable;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.table.DefaultColumnDefinition;
import org.gwt.mosaic.ui.client.table.DefaultTableDefinition;
import org.gwt.mosaic.ui.client.table.IterableTableModel;
import org.gwt.mosaic.ui.client.table.SerializableResponse;
import org.gwt.mosaic.ui.client.table.TableDefinition;
import org.gwt.mosaic.ui.client.table.TableModel;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwLiveTable extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwLiveTable(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Live Table description.";
  }

  @Override
  public String getName() {
    return "Live Table";
  }

  /**
   * User object.
   */
  @ShowcaseSource
  class Foo implements Serializable {
    private static final long serialVersionUID = 1120309810337478764L;

    int[] values = new int[5];

    public Foo(int row) {
      values[0] = row;
      for (int i = 1; i < values.length; i++) {
        values[i] = Random.nextInt();
      }
    }
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    final List<Foo> data = new ArrayList<Foo>();

    for (int i = 0; i < 510; i++) {
      data.add(new Foo(i));
    }

    TableModel<Foo> tableModel = new IterableTableModel<Foo>(data) {
      @Override
      public int getRowCount() {
        return data.size();
      }

      @Override
      public void requestRows(Request request, Callback<Foo> callback) {
        int numRows = Math.min(request.getNumRows(), data.size()
            - request.getStartRow());
        List<Foo> list = new ArrayList<Foo>();
        for (int i = 0, n = numRows; i < n; i++) {
          list.add(data.get(request.getStartRow() + i));
        }
        SerializableResponse<Foo> response = new SerializableResponse<Foo>(list);
        callback.onRowsReady(request, response);
      }
    };

    final LiveTable<Foo> table = new LiveTable<Foo>(tableModel,
        createTableDefinition());
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
  private TableDefinition<Foo> createTableDefinition() {
    DefaultTableDefinition<Foo> tableDef = new DefaultTableDefinition<Foo>();
    // tableDef.setRowRenderer(new DefaultRowRenderer<Person>(new String[] {
    // "#f00", "#00f" }));

    DefaultColumnDefinition<Foo, Integer> colDef0 = new DefaultColumnDefinition<Foo, Integer>(
        "#") {
      @Override
      public Integer getCellValue(Foo rowValue) {
        return rowValue.values[0];
      }
    };
    colDef0.setColumnSortable(false);
    tableDef.addColumnDefinition(colDef0);

    DefaultColumnDefinition<Foo, Integer> colDef1 = new DefaultColumnDefinition<Foo, Integer>(
        "Col 1") {
      @Override
      public Integer getCellValue(Foo rowValue) {
        return rowValue.values[1];
      }
    };
    colDef1.setColumnSortable(false);
    tableDef.addColumnDefinition(colDef1);

    DefaultColumnDefinition<Foo, Integer> colDef2 = new DefaultColumnDefinition<Foo, Integer>(
        "Col 2") {
      @Override
      public Integer getCellValue(Foo rowValue) {
        return rowValue.values[1];
      }
    };
    colDef2.setColumnSortable(false);
    tableDef.addColumnDefinition(colDef2);

    DefaultColumnDefinition<Foo, Integer> colDef3 = new DefaultColumnDefinition<Foo, Integer>(
        "Col 3") {
      @Override
      public Integer getCellValue(Foo rowValue) {
        return rowValue.values[2];
      }
    };
    colDef3.setColumnSortable(false);
    tableDef.addColumnDefinition(colDef3);

    DefaultColumnDefinition<Foo, Integer> colDef4 = new DefaultColumnDefinition<Foo, Integer>(
        "Col 4") {
      @Override
      public Integer getCellValue(Foo rowValue) {
        return rowValue.values[2];
      }
    };
    colDef4.setColumnSortable(false);
    tableDef.addColumnDefinition(colDef4);

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
