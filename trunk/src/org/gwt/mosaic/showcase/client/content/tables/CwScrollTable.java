/*
 * Copyright 2008 Google Inc.
 * 
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
package org.gwt.mosaic.showcase.client.content.tables;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwScrollTable extends ContentWidget {

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
   * The data portion of the <code>ScrollTable</code>
   */
  @ShowcaseData
  private FixedWidthGrid dataTable = null;

  /**
   * The footer portion of the <code>ScrollTable</code>
   */
  @ShowcaseData
  private FixedWidthFlexTable footerTable = null;

  /**
   * The header portion of the <code>ScrollTable</code>
   */
  @ShowcaseData
  private FixedWidthFlexTable headerTable = null;

  /**
   * The scroll table.
   */
  @ShowcaseSource
  private ScrollTable scrollTable = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwScrollTable(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  /**
   * Setup the footer table.
   */
  @ShowcaseSource
  protected void createFooterTable() {
    footerTable = new FixedWidthFlexTable();
    footerTable.setHTML(0, 0, "&nbsp;");
    for (int i = 0; i < 12; i++) {
      footerTable.setText(0, i + 1, "Col " + i);
    }
  }

  /**
   * Setup the header table.
   */
  @ShowcaseSource
  protected void createHeaderTable() {
    headerTable = new FixedWidthFlexTable();

    // Level 1 headers
    FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
    headerTable.setHTML(0, 0, "User Information");
    headerFormatter.setColSpan(0, 0, 13);

    // Create the select all checkbox
    final CheckBox selectAll = new CheckBox();
    selectAll.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (selectAll.isChecked()) {
          dataTable.selectAllRows();
        } else {
          dataTable.deselectAllRows();
        }
      }
    });

    // Level 2 headers
    headerTable.setWidget(1, 0, selectAll);
    headerFormatter.setRowSpan(1, 0, 2);
    headerFormatter.setHorizontalAlignment(1, 0,
        HasHorizontalAlignment.ALIGN_CENTER);
    headerTable.setHTML(1, 1, "First and Last Name");
    headerFormatter.setColSpan(1, 1, 2);
    headerFormatter.setRowSpan(1, 1, 2);
    headerTable.setHTML(1, 2, "General Info");
    headerFormatter.setColSpan(1, 2, 3);
    headerTable.setHTML(1, 3, "Favorite Color");
    headerFormatter.setColSpan(1, 3, 1);
    headerFormatter.setRowSpan(1, 3, 2);
    headerTable.setHTML(1, 4, "Preferred Sport");
    headerFormatter.setColSpan(1, 4, 1);
    headerFormatter.setRowSpan(1, 4, 2);
    headerTable.setHTML(1, 5, "School Info");
    headerFormatter.setColSpan(1, 5, 3);
    headerTable.setHTML(1, 6, "Login Info");
    headerFormatter.setColSpan(1, 6, 2);

    // Level 3 headers
    headerTable.setHTML(2, 0, "Age");
    headerTable.setHTML(2, 1, "Gender");
    headerTable.setHTML(2, 2, "Race");
    headerTable.setHTML(2, 3, "College");
    headerTable.setHTML(2, 4, "Year");
    headerTable.setHTML(2, 5, "GPA");
    headerTable.setHTML(2, 6, "ID");
    headerTable.setHTML(2, 7, "Pin");
  }

  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  protected void createScrollTable() {
    // Create the inner tables
    createHeaderTable();
    createFooterTable();
    dataTable = new FixedWidthGrid();
    //dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);

    // Add the scroll table to the page
    scrollTable = new ScrollTable(dataTable, headerTable);
    scrollTable.setFooterTable(footerTable);

    setupScrollTable();
  }

  /**
   * Get the data table.
   * 
   * @return the data table
   */
  @ShowcaseSource
  public FixedWidthGrid getDataTable() {
    return dataTable;
  }

  @Override
  public String getDescription() {
    return constants.mosaicScrollTableDescription();
  }

  /**
   * Get the footer table.
   * 
   * @return the footer table.
   */
  @ShowcaseSource
  public FixedWidthFlexTable getFooterTable() {
    return footerTable;
  }

  /**
   * Get the header table.
   * 
   * @return the header table
   */
  @ShowcaseSource
  public FixedWidthFlexTable getHeaderTable() {
    return headerTable;
  }

  @Override
  public String getName() {
    return constants.mosaicScrollTableName();
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
    createScrollTable();

    // Add some data the data table
    dataTable.resize(0, Student.NUM_FIELDS);
    for (int i = 0; i < 15; i++) {
      insertDataRow(i);
    }

    // Redraw the scroll table
    scrollTable.redraw();

    layoutPanel.add(scrollTable);

    return layoutPanel;
  }

  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  protected void setupScrollTable() {
    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    // scrollTable.setSize("95%", "50%");
    // scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    // Set column widths
    scrollTable.setColumnWidth(0, 100);
    scrollTable.setColumnWidth(1, 100);
    scrollTable.setColumnWidth(2, 35);
    scrollTable.setColumnWidth(3, 45);
    scrollTable.setColumnWidth(4, 110);
    scrollTable.setColumnWidth(5, 80);
    scrollTable.setColumnWidth(6, 110);
    scrollTable.setColumnWidth(7, 180);
    scrollTable.setColumnWidth(8, 35);
    scrollTable.setColumnWidth(9, 35);
    scrollTable.setColumnWidth(10, 55);
    scrollTable.setColumnWidth(11, 45);
  }

}
