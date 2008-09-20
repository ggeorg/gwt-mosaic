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
package org.gwt.mosaic.showcase.client.content.tables;

import java.io.Serializable;

import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.CachedTableModel;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.widgetideas.table.client.ListCellEditor;
import com.google.gwt.widgetideas.table.client.PagingOptions;
import com.google.gwt.widgetideas.table.client.PagingScrollTable;
import com.google.gwt.widgetideas.table.client.RadioCellEditor;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.TableBulkRenderer;
import com.google.gwt.widgetideas.table.client.TextCellEditor;
import com.google.gwt.widgetideas.table.client.PagingScrollTable.CellRenderer;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;

/**
 * Example file.
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwPagingScrollTable extends CwScrollTable {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants, CwScrollTable.CwConstants {
  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * A custom cell renderer.
   */
  @ShowcaseSource
  private static class CustomBulkRenderer extends TableBulkRenderer.CellRenderer {

    @Override
    public void renderCell(int row, int column, Object cellData, StringBuffer accum) {
      if (cellData == null) {
        return;
      }

      switch (column) {
        case 5:
          accum.append("<font color=\"").append(cellData).append("\">");
          accum.append(cellData);
          accum.append("</font>");
          break;
        default:
          accum.append(cellData.toString());
      }
    }
  }

  /**
   * A custom cell renderer.
   */
  private static class CustomCellRenderer implements CellRenderer {
    public void renderCell(FixedWidthGrid grid, int row, int column, Object data) {
      if (data == null) {
        grid.clearCell(row, column);
        return;
      }

      switch (column) {
        case 5:
          grid.setHTML(row, column, "<font color=\"" + data + "\">" + data + "</font>");
          break;
        default:
          grid.setHTML(row, column, data.toString());
      }
    }
  }

  /**
   * The {@link CachedTableModel} around the main table model.
   */
  protected CachedTableModel<Serializable> cachedTableModel = null;

  /**
   * The {@link DataSourceTableModel}.
   */
  protected DataSourceTableModel tableModel = null;

  /**
   * Get the cached table model.
   * 
   * @return the cached table model
   */
  public CachedTableModel<Serializable> getCachedTableModel() {
    return cachedTableModel;
  }

  /**
   * Get the scroll table.
   * 
   * @return the scroll table.
   */
  public PagingScrollTable<Serializable> getPagingScrollTable() {
    return (PagingScrollTable<Serializable>) scrollTable;
  }

  /**
   * Get the table model.
   * 
   * @return the table model
   */
  public DataSourceTableModel getTableModel() {
    return tableModel;
  }

  /**
   * Add a row of data cells each consisting of a string that describes the
   * row:column coordinates of the new cell. The number of columns in the new
   * row will match the number of columns in the grid.
   * 
   * @param beforeRow the index to add the new row into
   */
  public void insertDataRow(int beforeRow) {
    getCachedTableModel().insertRow(beforeRow);
  }

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPagingScrollTable(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }
  
  @Override
  public String getDescription() {
    return "Paging Scroll Table description";
  }

  @Override
  public String getName() {
    return "Paging Scroll Table";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();
    
    // Create the inner tables
    getHeaderTable();
    getFooterTable();

    // Setup the controller
    tableModel = new DataSourceTableModel();
    cachedTableModel = new CachedTableModel<Serializable>(tableModel);
    cachedTableModel.setPreCachedRowCount(20);
    cachedTableModel.setPostCachedRowCount(20);
    cachedTableModel.setRowCount(1000);

    // Setup the view
    dataTable = new FixedWidthGrid();

    // Create the scroll table
    scrollTable = new PagingScrollTable<Serializable>(cachedTableModel, dataTable,
        headerTable);
    getPagingScrollTable().setCellRenderer(new CustomCellRenderer());
    getPagingScrollTable().setPageSize(20);
    scrollTable.setFooterTable(getFooterTable());
    setupCellEditors(getPagingScrollTable());

    // Setup the bulk renderer
    FixedWidthGridBulkRenderer bulkRenderer = new FixedWidthGridBulkRenderer(dataTable,
        DataSourceTableModel.COLUMN_COUNT);
    bulkRenderer.setCellRenderer(new CustomBulkRenderer());
    getPagingScrollTable().setBulkRenderer(bulkRenderer);

    // Add some data to the footer table
    for (int i = 0; i < 12; i++) {
      footerTable.setText(0, i, "Col " + i);
    }
    scrollTable.setFooterTable(footerTable);

    // Setup the scroll table
    setupScrollTable();

    // Create an options panel
    PagingOptions pagingOptions = new PagingOptions(getPagingScrollTable());

    BoxLayout boxLayout = new BoxLayout(Orientation.VERTICAL);
    boxLayout.setLeftToRight(true);
    layoutPanel.setLayout(boxLayout);
    //layoutPanel.setWidgetSpacing(0);
    layoutPanel.add(scrollTable, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new WidgetWrapper(pagingOptions), new BoxLayoutData(FillStyle.HORIZONTAL, true));
    
    return layoutPanel;
  }

  /**
   * Setup the scroll table.
   */
  private void setupScrollTable() {
    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    scrollTable.setSize("95%", "50%");
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);

    // Set column widths
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

    // Level 1 headers
    FlexCellFormatter headerFormatter = headerTable.getFlexCellFormatter();
    headerTable.setHTML(0, 0, "User Information");
    headerFormatter.setColSpan(0, 0, 12);

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
   * Setup the cell editors on the scroll table.
   */
  private void setupCellEditors(PagingScrollTable<Serializable> table) {
    // Integer only cell editor for age
    TextBox intOnlyTextBox = new TextBox();
    intOnlyTextBox.setWidth("4em");
    intOnlyTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
      @Override
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
            && (keyCode != (char) KEY_BACKSPACE) && (keyCode != (char) KEY_DELETE)
            && (keyCode != (char) KEY_ENTER) && (keyCode != (char) KEY_HOME)
            && (keyCode != (char) KEY_END) && (keyCode != (char) KEY_LEFT)
            && (keyCode != (char) KEY_UP) && (keyCode != (char) KEY_RIGHT)
            && (keyCode != (char) KEY_DOWN)) {
          ((TextBox) sender).cancelKey();
        }
      }
    });
    table.setCellEditor(2, new TextCellEditor<Serializable>(intOnlyTextBox));

    // Gender cell editor
    RadioCellEditor<Serializable> genderEditor = new RadioCellEditor<Serializable>();
    genderEditor.setLabel("Select a gender:");
    genderEditor.addRadioButton(new RadioButton("editorGender", "male"));
    genderEditor.addRadioButton(new RadioButton("editorGender", "female"));
    table.setCellEditor(3, genderEditor);

    // Race cell editor
    ListCellEditor<Serializable> raceEditor = new ListCellEditor<Serializable>();
    ListBox raceBox = raceEditor.getListBox();
    for (int i = 0; i < DataSourceData.races.length; i++) {
      raceBox.addItem(DataSourceData.races[i]);
    }
    table.setCellEditor(4, raceEditor);

    // Color cell editor
    RadioCellEditor<Serializable> colorEditor = new RadioCellEditor<Serializable>() {
      /**
       * An element used to string the HTML portion of the color.
       */
      private HTML html = new HTML();

      @Override
      protected void setValue(Object value) {
        html.setHTML(value.toString());
        super.setValue(html.getText());
      }
    };
    colorEditor.setLabel("Select a color:");
    for (int i = 0; i < DataSourceData.colors.length; i++) {
      String color = DataSourceData.colors[i];
      colorEditor.addRadioButton(new RadioButton("editorColor", color));
    }
    table.setCellEditor(5, colorEditor);

    // Sport cell editor
    ListCellEditor<Serializable> sportEditor = new ListCellEditor<Serializable>();
    sportEditor.setLabel("Select a sport:");
    ListBox sportBox = sportEditor.getListBox();
    for (int i = 0; i < DataSourceData.sports.length; i++) {
      sportBox.addItem(DataSourceData.sports[i]);
    }
    table.setCellEditor(6, sportEditor);

    // College cell editor
    TextCellEditor<Serializable> collegeEditor = new TextCellEditor<Serializable>() {
      @Override
      protected Object getValue() {
        return "University of " + super.getValue();
      }

      @Override
      public boolean onAccept() {
        if (getValue().equals("")) {
          Window.alert("You must enter a school");
          return false;
        }
        return true;
      }

      @Override
      protected void setValue(Object value) {
        super.setValue(value.toString().substring(14));
      }
    };
    collegeEditor.setLabel("University of");
    table.setCellEditor(7, collegeEditor);
  }

}
