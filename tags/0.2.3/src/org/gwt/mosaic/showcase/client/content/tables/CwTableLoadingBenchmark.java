/*
 * Copyright 2008 Google Inc.
 * 
 * Copyright (C) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.ClientTableModel;
import com.google.gwt.widgetideas.table.client.FlexTableBulkRenderer;
import com.google.gwt.widgetideas.table.client.PreloadedTable;
import com.google.gwt.widgetideas.table.client.RendererCallback;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable;
import com.google.gwt.widgetideas.table.client.overrides.Grid;
import com.google.gwt.widgetideas.table.client.overrides.HTMLTable;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwTableLoadingBenchmark extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
  }

  int numColumns = 15;
  int numRows = 120;

  HTMLTable curTable;

  ClientTableModel<Object> tableModel = new ClientTableModel<Object>() {

    @Override
    public Object getCell(int rowNum, int cellNum) {
      if (rowNum >= numRows | cellNum >= numColumns) {
        return null;
      }
      return "cell " + rowNum + ", " + cellNum;
    }

    @Override
    protected boolean onRowInserted(int beforeRow) {
      return false;
    }

    @Override
    protected boolean onRowRemoved(int row) {
      return false;
    }

    @Override
    protected boolean onSetData(int row, int cell, Object data) {
      return false;
    }

  };

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwTableLoadingBenchmark(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return "Loading Benchmark description";
  }

  @Override
  public String getName() {
    return "Loading Benchmark";
  }

  private ScrollPanel center = new ScrollPanel();

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BorderLayout());

    HTML caption = new HTML(
        "<b>A very boring demo showing the speed difference of using bulk loading tables.</b>");
    layoutPanel.add(caption, new BorderLayoutData(Region.NORTH));

    LayoutPanel panel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));

    panel.add(new Label("Number of rows:"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    final TextBox rows = new TextBox();
    panel.add(rows, new BoxLayoutData(FillStyle.HORIZONTAL));
    rows.setText(numRows + "");
    rows.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        numRows = Integer.parseInt(rows.getText().trim());
      }
    });

    panel.add(new Label("Number of columns"), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    final TextBox columns = new TextBox();
    panel.add(columns, new BoxLayoutData(FillStyle.HORIZONTAL));
    columns.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
        numColumns = Integer.parseInt(columns.getText());
      }
    });
    columns.setText(numColumns + "");

    Button clearBtn = new Button("Clear Table now", new ClickHandler() {
      public void onClick(ClickEvent event) {
        clearTable();      }
    });
    panel.add(clearBtn, new BoxLayoutData(FillStyle.HORIZONTAL));
    panel.add(new HTML(
        "<em><small>Clearing will also happen if the buttons are clicked below</small></em><hr>"));

    Button flexTableAPI = new Button("Use the traditional FlexTable API",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            clearTable();
            long milli = System.currentTimeMillis();
            FlexTable newTable = new FlexTable();
            usingFlexTableAPI(newTable);
            finishTable("Traditional FlexTable API", newTable, milli);
          }
        });
    panel.add(flexTableAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button gridAPI = new Button("Use the traditional Grid API",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            clearTable();
            long milli = System.currentTimeMillis();
            Grid newTable = new Grid();
            usingGridAPI(newTable);
            finishTable("Traditional Grid API", newTable, milli);
          }

        });
    panel.add(gridAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button detachedGridAPI = new Button("Use the attached Grid API",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            clearTable();
            long milli = System.currentTimeMillis();
            Grid table = new Grid();
            usingGridAPI(table);
            finishTable("Attached Grid API", table, milli);          }
        });
    panel.add(detachedGridAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button asyncAPI = new Button("Use Async BulkLoadedTable API",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            clearTable();
            long milli = System.currentTimeMillis();
            FlexTable table = new FlexTable();
            usingBulkLoadedTableAPI(table, milli);
          }
        });
    panel.add(asyncAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    Button pendingAPI = new Button("Using the preloadedTable  API",
        new ClickHandler() {
          public void onClick(ClickEvent event) {
            clearTable();
            long milli = System.currentTimeMillis();
            PreloadedTable table = new PreloadedTable();
            usingPreloadedTableAPI(table);
            finishTable("PreloadedTable  API", table, milli);
          }
        });
    panel.add(pendingAPI, new BoxLayoutData(FillStyle.HORIZONTAL));

    layoutPanel.add(panel, new BorderLayoutData(Region.WEST, 250,
        true));
    layoutPanel.add(center, new BorderLayoutData(true));

    return layoutPanel;
  }

  private void clearTable() {
    if (curTable != null) {
      curTable.removeFromParent();
      curTable = null;
    }
  }

  private void finishTable(final String caption, final HTMLTable table,
      final long milli) {

    // In order to compare apples-to-apples for rendering time letting event cue
    // flush once.

    curTable = table;
    table.setBorderWidth(2);
    center.add(table);

    table.setWidget(0, 3, new Button("A widget"));

    InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, caption, "Finished in "
        + (System.currentTimeMillis() - milli) + " milliseconds");
  }

  private void usingBulkLoadedTableAPI(final FlexTable table, final long milli) {
    RendererCallback callback = new RendererCallback() {
      public void onRendered() {
        finishTable("Async BulkLoadedTable API", table, milli);
      }
    };

    FlexTableBulkRenderer renderer = new FlexTableBulkRenderer(table);
    renderer.renderRows(tableModel, callback);
  }

  private void usingFlexTableAPI(FlexTable table) {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }

  private void usingGridAPI(Grid table) {
    table.resize(numRows, numColumns);
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }

  private void usingPreloadedTableAPI(PreloadedTable table) {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numColumns; j++) {
        table.setPendingHTML(i, j, "cell " + i + ", " + j);
      }
    }
  }
  
}
