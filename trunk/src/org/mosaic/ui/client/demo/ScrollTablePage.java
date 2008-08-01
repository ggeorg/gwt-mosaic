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
package org.mosaic.ui.client.demo;

import org.mosaic.ui.client.demo.Annotations.MosaicData;
import org.mosaic.ui.client.demo.Annotations.MosaicSource;
import org.mosaic.ui.client.demo.Annotations.MosaicStyle;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.scrolltable.FixedWidthFlexTable;
import org.mosaic.ui.client.scrolltable.FixedWidthGrid;
import org.mosaic.ui.client.scrolltable.ScrollTable;
import org.mosaic.ui.client.scrolltable.overrides.FlexTable.FlexCellFormatter;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * Test methods in the {@link ScrollTable} class.
 * 
 */
@MosaicStyle( {})
public class ScrollTablePage extends Page {

  /**
   * The constants used in this Page.
   */
  @MosaicSource
  public static interface DemoConstants extends Constants, Page.DemoConstants {
  }

  /**
   * An instance of the constants.
   */
  @MosaicData
  private DemoConstants constants;

  /**
   * The data portion of the {@link ScrollTable}.
   */
  @MosaicData
  protected static FixedWidthGrid dataTable = null;

  /**
   * The footer portion of the {@link ScrollTable}.
   */
  @MosaicData
  protected static FixedWidthFlexTable footerTable = null;

  /**
   * The header portion of the {@link ScrollTable}.
   */
  @MosaicData
  protected static FixedWidthFlexTable headerTable = null;

  /**
   * The scroll table.
   */
  @MosaicSource
  protected static ScrollTable scrollTable = null;

  /**
   * Get the data table.
   * 
   * @return the data table
   */
  @MosaicSource
  public static FixedWidthGrid getDataTable() {
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
  @MosaicSource
  public static FixedWidthFlexTable getFooterTable() {
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
  @MosaicSource
  public static FixedWidthFlexTable getHeaderTable() {
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
  @MosaicSource
  public static ScrollTable getScrollTable() {
    return scrollTable;
  }

  /**
   * Add a row of data cells each consisting of a string that describes the
   * row:column coordinates of the new cell. The number of columns in the new
   * row will match the number of columns in the grid.
   * 
   * @param beforeRow the index to add the new row into
   */
  @MosaicSource
  public static void insertDataRow(int beforeRow) {
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
  @MosaicSource
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
  @MosaicSource
  private void setupScrollTable() {
    // Setup the formatting
    scrollTable.setSize("95%", "50%");
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(1);
    //scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

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

}
