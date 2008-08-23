/*
 * Copyright 2008 Google Inc.
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

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;

/**
 * Test methods in the {@link ScrollTable} class.
 * 
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class ScrollTablePage extends Page {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private DemoConstants constants;

  /**
   * The data portion of the {@link ScrollTable}.
   */
  @ShowcaseData
  protected FixedWidthGrid dataTable = null;

  /**
   * The footer portion of the {@link ScrollTable}.
   */
  @ShowcaseData
  protected FixedWidthFlexTable footerTable = null;

  /**
   * The header portion of the {@link ScrollTable}.
   */
  @ShowcaseData
  protected FixedWidthFlexTable headerTable = null;

  /**
   * The scroll table.
   */
  @ShowcaseSource
  protected ScrollTable scrollTable = null;

  /**
   * Get the data table.
   * 
   * @return the data table
   */
  @ShowcaseSource
  public FixedWidthGrid getDataTable() {
    if (dataTable == null) {
      dataTable = new FixedWidthGrid();
    }
    return dataTable;
  }

  /**
   * Get the footer table.
   * 
   * @return the footer table.
   */
  @ShowcaseSource
  public FixedWidthFlexTable getFooterTable() {
    if (footerTable == null) {
      footerTable = new FixedWidthFlexTable();
    }
    return footerTable;
  }

  /**
   * Get the header table.
   * 
   * @return the header table.
   */
  @ShowcaseSource
  public FixedWidthFlexTable getHeaderTable() {
    if (headerTable == null) {
      headerTable = new FixedWidthFlexTable();
    }
    return headerTable;
  }

  /**
   * Get the scroll table.
   * 
   * @return the scroll table.
   */
  @ShowcaseSource
  public ScrollTable getScrollTable() {
    return scrollTable;
  }

  /**
   * Add a row of data cells each consisting of a string that describes the
   * row:column coordinates of the new cell. The number of columns in the new
   * row will match the number of columns in the grid.
   * 
   * @param beforeRow the index to add the new row into
   */
  @ShowcaseSource
  public void insertDataRow(int beforeRow) {
    // Insert the new row
    beforeRow = dataTable.insertRow(beforeRow);

    // Set the data in the new row
    int numColumns = dataTable.getColumnCount();
    for (int column = 0; column < numColumns; column++) {
      String label = beforeRow + ":" + column;
      if (column == 0) {
        dataTable.setWidget(beforeRow, column, new CheckBox(label));
      } else if (column == 2) {
        int rand = (int) (Math.random() * 100000);
        dataTable.setHTML(beforeRow, column, rand + "");
      } else {
        dataTable.setHTML(beforeRow, column, label);
      }
    }
  }

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public ScrollTablePage(DemoConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Load this example.
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    // Create the inner tables
    getHeaderTable();
    getFooterTable();
    getDataTable();

    // Add the scroll table to the page
    scrollTable = new ScrollTable(dataTable, headerTable);
    scrollTable.setFooterTable(footerTable);

    // Setup the header
    setupScrollTable();

    // Add some data the data table
    dataTable.resize(0, 13);
    for (int i = 0; i < 15; i++) {
      insertDataRow(i);
    }

    // Add some data to the footer table
    for (int i = 0; i < 13; i++) {
      footerTable.setText(0, i, "Col " + i);
    }

    // Redraw the scroll table
    scrollTable.redraw();

    layoutPanel.add(scrollTable);
  }

  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  private void setupScrollTable() {
    // Setup the formatting
    scrollTable.setSize("95%", "50%");
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(1);
    // scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

    // Level 1 headers
    FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
    headerTable.setHTML(0, 0, "Info Table");
    headerFormatter.setColSpan(0, 0, 13);

    // Level 2 headers
    headerTable.setHTML(1, 0, "Group Header 0<BR>Multiline");
    headerFormatter.setColSpan(1, 0, 2);
    headerFormatter.setRowSpan(1, 0, 2);
    headerTable.setHTML(1, 1, "Group Header 1");
    headerFormatter.setColSpan(1, 1, 3);
    headerTable.setHTML(1, 2, "Group Header 2");
    headerFormatter.setColSpan(1, 2, 1);
    headerFormatter.setRowSpan(1, 2, 2);
    headerTable.setHTML(1, 3, "Group Header 3");
    headerFormatter.setColSpan(1, 3, 1);
    headerFormatter.setRowSpan(1, 3, 2);
    headerTable.setHTML(1, 4, "Group Header 4");
    headerFormatter.setColSpan(1, 4, 3);
    headerTable.setHTML(1, 5, "Group Header 5");
    headerFormatter.setColSpan(1, 5, 3);

    // Level 3 headers
    for (int cell = 0; cell < 9; cell++) {
      headerTable.setHTML(2, cell, "Header " + cell);
    }
  }

  @Override
  public String getName() {
    return "Scroll Table";
  }

}
