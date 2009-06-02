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
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.table.ScrollTable2;

import com.google.gwt.gen2.table.client.AbstractScrollTable;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwScrollTable2 extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicScrollTableDescription();

    String mosaicScrollTableName();
  }

  /**
   * The source of the data.
   */
  @ShowcaseSource
  public static final StudentGenerator STUDENT_DATA = new StudentGenerator() {
    @Override
    public int getRandomInt(int max) {
      return Random.nextInt(max);
    }
  };

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * The scroll table.
   */
  @ShowcaseSource
  private ScrollTable2 scrollTable = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwScrollTable2(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * @return the newly created data table.
   */
  @ShowcaseSource
  private FixedWidthGrid createDataTable() {
    FixedWidthGrid dataTable = new FixedWidthGrid();
    dataTable.setSelectionPolicy(SelectionPolicy.MULTI_ROW);
    return dataTable;
  }

  /**
   * @return the new footer table
   */
  @ShowcaseSource
  private FixedWidthFlexTable createFooterTable() {
    FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
    for (int i = 0; i < 12; i++) {
      footerTable.setText(0, i, "Col " + i);
    }
    return footerTable;
  }

  /**
   * @return the new header table.
   */
  @ShowcaseSource
  private FixedWidthFlexTable createHeaderTable() {
    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

    // Level 1 headers
    FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
    headerTable.setHTML(0, 0, "User Information");
    headerFormatter.setColSpan(0, 0, 13);

    // Level 2 headers
    headerTable.setHTML(1, 0, "First and Last Name");
    headerFormatter.setColSpan(1, 0, 2);
    headerFormatter.setRowSpan(1, 0, 2);
    headerTable.setHTML(1, 1, "General Info");
    headerFormatter.setColSpan(1, 1, 3);
    headerTable.setHTML(1, 2, "Favorite Color");
    headerFormatter.setColSpan(1, 2, 1);
    headerFormatter.setRowSpan(1, 2, 2);
    headerTable.setHTML(1, 3, "Preferred Sport");
    headerFormatter.setColSpan(1, 3, 1);
    headerFormatter.setRowSpan(1, 3, 2);
    headerTable.setHTML(1, 4, "School Info");
    headerFormatter.setColSpan(1, 4, 3);
    headerTable.setHTML(1, 5, "Login Info");
    headerFormatter.setColSpan(1, 5, 2);

    // Level 3 headers
    headerTable.setHTML(2, 0, "Age");
    headerTable.setHTML(2, 1, "Gender");
    headerTable.setHTML(2, 2, "Race");
    headerTable.setHTML(2, 3, "College");
    headerTable.setHTML(2, 4, "Year");
    headerTable.setHTML(2, 5, "GPA");
    headerTable.setHTML(2, 6, "ID");
    headerTable.setHTML(2, 7, "Pin");

    return headerTable;
  }
  
  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  protected ScrollTable2 createScrollTable() {
    // Create the three component tables
    FixedWidthFlexTable headerTable = createHeaderTable();
    FixedWidthFlexTable footerTable = createFooterTable();
    FixedWidthGrid dataTable = createDataTable();

    // Create the scroll table
    ScrollTable2 scrollTable = new ScrollTable2(dataTable, headerTable);
    scrollTable.setFooterTable(footerTable);

    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    // first name
    scrollTable.setMinimumColumnWidth(0, 50);
    scrollTable.setPreferredColumnWidth(0, 100);
    scrollTable.setColumnTruncatable(0, false);

    // last name
    scrollTable.setMinimumColumnWidth(1, 50);
    scrollTable.setPreferredColumnWidth(1, 100);
    scrollTable.setColumnTruncatable(1, false);

    // age
    scrollTable.setMinimumColumnWidth(2, 35);
    scrollTable.setPreferredColumnWidth(2, 35);
    scrollTable.setMaximumColumnWidth(2, 35);

    // gender
    scrollTable.setMinimumColumnWidth(3, 45);
    scrollTable.setPreferredColumnWidth(3, 45);
    scrollTable.setMaximumColumnWidth(3, 45);

    // race
    scrollTable.setMinimumColumnWidth(4, 45);
    scrollTable.setPreferredColumnWidth(4, 45);
    scrollTable.setMaximumColumnWidth(4, 45);

    // color
    scrollTable.setPreferredColumnWidth(5, 80);

    // sport
    scrollTable.setMinimumColumnWidth(6, 40);
    scrollTable.setPreferredColumnWidth(6, 110);

    // college
    scrollTable.setMinimumColumnWidth(7, 50);
    scrollTable.setPreferredColumnWidth(7, 180);
    scrollTable.setMaximumColumnWidth(7, 250);

    // year
    scrollTable.setPreferredColumnWidth(8, 25);
    scrollTable.setColumnTruncatable(8, false);

    // gpa
    scrollTable.setPreferredColumnWidth(9, 35);
    scrollTable.setColumnTruncatable(9, false);

    // id
    scrollTable.setPreferredColumnWidth(10, 55);
    scrollTable.setColumnTruncatable(10, false);

    // pin
    scrollTable.setPreferredColumnWidth(11, 45);
    scrollTable.setColumnTruncatable(11, false);

    return scrollTable;
  }
  
  /**
   * @return the data table.
   */
  public FixedWidthGrid getDataTable() {
    return getScrollTable().getDataTable();
  }

  @Override
  public String getDescription() {
    return constants.mosaicScrollTableDescription();
  }
  
  /**
   * @return the footer table.
   */
  public FixedWidthFlexTable getFooterTable() {
    return getScrollTable().getFooterTable();
  }
  
  /**
   * @return the header table.
   */
  public FixedWidthFlexTable getHeaderTable() {
    return getScrollTable().getHeaderTable();
  }

  @Override
  public String getName() {
    return constants.mosaicScrollTableName() + "2";
  }
  
  /**
   * @return the scroll table.
   */
  public AbstractScrollTable getScrollTable() {
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
    FixedWidthGrid dataTable = getDataTable();
    beforeRow = dataTable.insertRow(beforeRow);

    // Set the data in the new row
    Student student = STUDENT_DATA.generateStudent();
    dataTable.setText(beforeRow, 0, student.getFirstName());
    dataTable.setText(beforeRow, 1, student.getLastName());
    dataTable.setText(beforeRow, 2, student.getAge() + "");
    dataTable.setText(beforeRow, 3, student.isMale() ? "male" : "female");
    dataTable.setText(beforeRow, 4, student.getRace());
    String color = student.getFavoriteColor();
    String colorHtml = "<FONT color=\"" + color + "\">" + color + "</FONT>";
    dataTable.setHTML(beforeRow, 5, colorHtml);
    dataTable.setText(beforeRow, 6, student.getFavoriteSport());
    dataTable.setText(beforeRow, 7, student.getCollege());
    dataTable.setText(beforeRow, 8, student.getGraduationYear() + "");
    String gpa = student.getGpa() + "";
    if (gpa.length() > 4) {
      gpa = gpa.substring(0, 4);
    }
    dataTable.setText(beforeRow, 9, gpa);
    dataTable.setText(beforeRow, 10, student.getId() + "");
    dataTable.setText(beforeRow, 11, student.getPin() + "");
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();

    // Create the tables
    scrollTable = createScrollTable();

    // Add some data to the data table
    getDataTable().resize(0, Student.NUM_FIELDS);
    for (int i = 0; i < 15; i++) {
      insertDataRow(i);
    }

    layoutPanel.add(scrollTable);

    return layoutPanel;
  }
  
}
