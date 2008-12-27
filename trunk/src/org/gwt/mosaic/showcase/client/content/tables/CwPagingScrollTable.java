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

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.DoubleClickListener;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.InfoPanel.InfoPanelType;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.table.PagingOptions;
import org.gwt.mosaic.ui.client.table.PagingScrollTable;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import org.gwt.mosaic.ui.client.table.PagingScrollTable.CellRenderer;
import org.gwt.mosaic.ui.client.table.ScrollTable.DataGrid;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.CachedTableModel;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.widgetideas.table.client.ListCellEditor;
import com.google.gwt.widgetideas.table.client.RadioCellEditor;
import com.google.gwt.widgetideas.table.client.TableBulkRenderer;
import com.google.gwt.widgetideas.table.client.TextCellEditor;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwPagingScrollTable extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicPagingScrollTableDescription();

    String mosaicPagingScrollTableName();
  }

  /**
   * A custom cell renderer.
   */
  private static class CustomBulkRenderer extends
      TableBulkRenderer.CellRenderer {
    @Override
    public void renderCell(final int row, final int column,
        final Object cellData, final StringBuffer accum) {
      if (cellData == null) {
        return;
      }

      switch (column) {
        case 5:
          accum.append("<FONT color=\"" + cellData + "\">" + cellData
              + "</FONT>");
          return;
        default:
          accum.append(cellData + "");
      }
    }
  }

  /**
   * A custom cell renderer.
   */
  private static class CustomCellRenderer implements CellRenderer {
    public void renderCell(DataGrid grid, int row, int column, Object data) {
      if (data == null) {
        grid.clearCell(row, column);
        return;
      }

      switch (column) {
        case 5:
          grid.setHTML(row, column, "<FONT color=\"" + data + "\">" + data
              + "</FONT>");
          break;
        default:
          grid.setHTML(row, column, data + "");
      }
    }
  }

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
   * The <code>CachedTableModel</code> around the main table model.
   */
  @ShowcaseSource
  private CachedTableModel<Serializable> cachedTableModel = null;

  /**
   * The {@link DataSourceTableModel}.
   */
  @ShowcaseSource
  private DataSourceTableModel tableModel = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPagingScrollTable(CwConstants constants) {
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

    // Setup the controller
    tableModel = new DataSourceTableModel();
    cachedTableModel = new CachedTableModel<Serializable>(tableModel);
    cachedTableModel.setPreCachedRowCount(50);
    cachedTableModel.setPostCachedRowCount(50);
    cachedTableModel.setRowCount(1000);

    // Create the scroll table
    scrollTable = new PagingScrollTable<Serializable>(cachedTableModel,
        dataTable, headerTable);
    scrollTable.setContextMenu(createContextMenu());

    scrollTable.addDoubleClickListener(new DoubleClickListener() {
      public void onDoubleClick(Widget sender) {
        InfoPanel.show(InfoPanelType.HUMANIZED_MESSAGE, "DoubleClickListener",
            scrollTable.getDataTable().getSelectedRows().toString());
      }
    });

    PagingScrollTable<Serializable> pagingScrollTable = getPagingScrollTable();
    pagingScrollTable.setCellRenderer(new CustomCellRenderer());
    pagingScrollTable.setPageSize(20);
    setupCellEditors(pagingScrollTable);

    // Setup the bulk renderer
    FixedWidthGridBulkRenderer bulkRenderer = new FixedWidthGridBulkRenderer(
        dataTable, DataSourceTableModel.COLUMN_COUNT);
    bulkRenderer.setCellRenderer(new CustomBulkRenderer());
    pagingScrollTable.setBulkRenderer(bulkRenderer);

    // Setup the scroll table
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
   * Setup the cell editors on the scroll table.
   */
  @ShowcaseSource
  private void setupCellEditors(PagingScrollTable<Serializable> table) {
    // Integer only cell editor for age
    TextBox intOnlyTextBox = new TextBox();
    intOnlyTextBox.setWidth("4em");
    intOnlyTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
      @Override
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
            && (keyCode != (char) KEY_BACKSPACE)
            && (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER)
            && (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
            && (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
            && (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN)) {
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

  /**
   * Get the cached table model.
   * 
   * @return the cached table model
   */
  @ShowcaseSource
  public CachedTableModel<Serializable> getCachedTableModel() {
    return cachedTableModel;
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
    return constants.mosaicPagingScrollTableDescription();
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
    return constants.mosaicPagingScrollTableName();
  }

  /**
   * Get the scroll table.
   * 
   * @return the scroll table.
   */
  @ShowcaseSource
  public PagingScrollTable<Serializable> getPagingScrollTable() {
    return (PagingScrollTable<Serializable>) scrollTable;
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
   * Get the table model.
   * 
   * @return the table model
   */
  @ShowcaseSource
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
  @ShowcaseSource
  public void insertDataRow(int beforeRow) {
    getCachedTableModel().insertRow(beforeRow);
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setPadding(0);

    // Create the tables
    createScrollTable();

    // Create an options panel
    PagingOptions pagingOptions = new PagingOptions(getPagingScrollTable());

    layoutPanel.add(scrollTable, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new WidgetWrapper(pagingOptions), new BoxLayoutData(
        FillStyle.HORIZONTAL, true));

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
