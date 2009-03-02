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
import org.gwt.mosaic.ui.client.DoubleClickListener;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import org.gwt.mosaic.ui.client.table.ScrollTable.DataGrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
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
  private DataGrid dataTable = null;

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
    for (int i = 0; i < 12; i++) {
      footerTable.setText(0, i, "Col " + i);
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
  }

  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  protected void createScrollTable() {
    // Create the inner tables
    createHeaderTable();
    createFooterTable();
    dataTable = new DataGrid();
    dataTable.setSelectionPolicy(SelectionPolicy.MULTI_ROW);

    // Add the scroll table to the page
    scrollTable = new ScrollTable(dataTable, headerTable);
    scrollTable.setFooterTable(footerTable);
    scrollTable.setContextMenu(createContextMenu());
    
    scrollTable.addDoubleClickListener(new DoubleClickListener() {
      public void onDoubleClick(Widget sender) {
        InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, "DoubleClickListener",
            scrollTable.getDataTable().getSelectedRows().toString());
      }
    });

    setupScrollTable();
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
    //scrollTable.redraw();

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

  @Override
  protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
    GWT.runAsync(new RunAsyncCallback() {

      public void onFailure(Throwable caught) {
        callback.onFailure(caught);
      }

      public void onSuccess() {
        callback.onSuccess(onInitialize());
      }
    });
  }
  
}
