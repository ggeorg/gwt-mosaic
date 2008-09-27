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
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.gen2.table.client.CachedTableModel;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.ListCellEditor;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.RadioCellEditor;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TextCellEditor;
import com.google.gwt.gen2.table.client.TableDefinition.HTMLCellView;
import com.google.gwt.gen2.table.client.TableDefinition.TableCellView;
import com.google.gwt.gen2.table.client.overrides.FlexTable.FlexCellFormatter;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
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
   * A <code>ColumnDefinition</code> applied to Integer columns in
   * {@link Student} row values.
   */
  @ShowcaseSource
  private abstract static class IntegerColumnDefinition extends
      StudentColumnDefinition<Integer> {
    @Override
    public void renderCellValue(Student rowValue, Integer cellValue,
        HTMLCellView<Student> view) {
      view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      super.renderCellValue(rowValue, cellValue, view);
    }

    @Override
    public void renderCellValue(Student rowValue, Integer cellValue,
        TableCellView<Student> view) {
      view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      super.renderCellValue(rowValue, cellValue, view);
    }
  }

  /**
   * A <code>ColumnDefinition</code> applied to {@link Student} row values.
   * 
   * @param <ColType> the data type of the column
   */
  @ShowcaseSource
  private abstract static class StudentColumnDefinition<ColType> extends
      ColumnDefinition<Student, ColType> {
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
   * The <code>CachedTableModel</code> around the main table model.
   */
  @ShowcaseSource
  private CachedTableModel<Student> cachedTableModel = null;

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
   * Setup the scroll table.
   */
  @ShowcaseSource
  protected void createScrollTable() {
    // Create the inner tables
    createHeaderTable();
    createFooterTable();
    dataTable = new FixedWidthGrid();

    // Setup the controller
    tableModel = new DataSourceTableModel();
    cachedTableModel = new CachedTableModel<Student>(tableModel);
    cachedTableModel.setPreCachedRowCount(50);
    cachedTableModel.setPostCachedRowCount(50);
    cachedTableModel.setRowCount(1000);

    // Create a TableCellRenderer
    TableDefinition<Student> tcr = createTableCellRenderer();

    // Create the scroll table
    scrollTable = new PagingScrollTable<Student>(cachedTableModel, dataTable,
        headerTable, tcr);
    getPagingScrollTable().setPageSize(50);
    getPagingScrollTable().setEmptyTableWidget(
        new HTML("There is no data to display"));
    scrollTable.setFooterTable(getFooterTable());

    // Setup the bulk renderer
    FixedWidthGridBulkRenderer<Student> bulkRenderer = new FixedWidthGridBulkRenderer<Student>(
        dataTable, tcr);
    getPagingScrollTable().setBulkRenderer(bulkRenderer);

    // Setup the scroll table
    setupScrollTable();
  }

  /**
   * @return the {@link TableDefinition} with all {@link ColumnDefinition}.
   */
  @ShowcaseSource
  private TableDefinition<Student> createTableCellRenderer() {
    TableDefinition<Student> tcr = new TableDefinition<Student>();
    // First name
    tcr.addColumnDefinition(new StudentColumnDefinition<String>() {
      @Override
      public String getCellValue(Student rowValue) {
        return rowValue.getFirstName();
      }

      @Override
      public void setCellValue(Student rowValue, String cellValue) {
        rowValue.setFirstName(cellValue);
      }

    });

    // Last name
    tcr.addColumnDefinition(new StudentColumnDefinition<String>() {
      @Override
      public String getCellValue(Student rowValue) {
        return rowValue.getLastName();
      }

      @Override
      public void setCellValue(Student rowValue, String cellValue) {
        rowValue.setLastName(cellValue);
      }
    });

    // Age
    tcr.addColumnDefinition(new IntegerColumnDefinition() {
      @Override
      public Integer getCellValue(Student rowValue) {
        return rowValue.getAge();
      }

      @Override
      public void setCellValue(Student rowValue, Integer cellValue) {
        rowValue.setAge(cellValue);
      }
    });

    // Gender
    {
      StudentColumnDefinition<Boolean> columnDef = new StudentColumnDefinition<Boolean>() {
        @Override
        public Boolean getCellValue(Student rowValue) {
          return rowValue.isMale();
        }

        @Override
        public void renderCellValue(Student rowValue, Boolean cellValue,
            HTMLCellView<Student> view) {
          if (cellValue) {
            view.addHTML("male");
          } else {
            view.addHTML("female");
          }
        }

        @Override
        public void renderCellValue(Student rowValue, Boolean cellValue,
            TableCellView<Student> view) {
          if (cellValue) {
            view.setHTML("male");
          } else {
            view.setHTML("female");
          }
        }

        @Override
        public void setCellValue(Student rowValue, Boolean cellValue) {
          rowValue.setMale(cellValue);
        }
      };
      tcr.addColumnDefinition(columnDef);

      // Setup the cellEditor
      RadioCellEditor<Boolean> cellEditor = new RadioCellEditor<Boolean>();
      cellEditor.setLabel("Select a gender:");
      cellEditor.addRadioButton(new RadioButton("editorGender", "male"), true);
      cellEditor.addRadioButton(new RadioButton("editorGender", "female"),
          false);
      columnDef.setCellEditor(cellEditor);
    }

    // Race
    {
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>() {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getRace();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setRace(cellValue);
        }
      };
      tcr.addColumnDefinition(columnDef);

      // Setup the cell editor
      ListCellEditor<String> cellEditor = new ListCellEditor<String>();
      for (int i = 0; i < StudentGenerator.races.length; i++) {
        String race = StudentGenerator.races[i];
        cellEditor.addItem(race, race);
      }
      columnDef.setCellEditor(cellEditor);
    }

    // Favorite color
    {
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>() {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getFavoriteColor();
        }

        @Override
        public void renderCellValue(Student rowValue, String cellValue,
            HTMLCellView<Student> view) {
          view.setStyleAttribute("color", cellValue);
          view.addHTML(cellValue);
        }

        @Override
        public void renderCellValue(Student rowValue, String cellValue,
            TableCellView<Student> view) {
          view.setStyleAttribute("color", cellValue);
          view.setHTML(cellValue);
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setFavoriteColor(cellValue);
        }
      };
      tcr.addColumnDefinition(columnDef);

      // Setup the cell editor
      RadioCellEditor<String> cellEditor = new RadioCellEditor<String>();
      cellEditor.setLabel("Select a color:");
      for (int i = 0; i < StudentGenerator.colors.length; i++) {
        String color = StudentGenerator.colors[i];
        String text = "<FONT color=\"" + color + "\">" + color + "</FONT>";
        cellEditor.addRadioButton(new RadioButton("editorColor", text, true),
            color);
      }
      columnDef.setCellEditor(cellEditor);
    }

    // Favorite Sport
    {
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>() {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getFavoriteSport();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setFavoriteSport(cellValue);
        }
      };
      tcr.addColumnDefinition(columnDef);

      // Setup the cell editor
      ListCellEditor<String> cellEditor = new ListCellEditor<String>();
      cellEditor.setLabel("Select a sport:");
      for (int i = 0; i < StudentGenerator.sports.length; i++) {
        String sport = StudentGenerator.sports[i];
        cellEditor.addItem(sport, sport);
      }
      columnDef.setCellEditor(cellEditor);
    }

    // College
    {
      StudentColumnDefinition<String> colDef = new StudentColumnDefinition<String>() {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getCollege();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setCollege(cellValue);
        }
      };
      tcr.addColumnDefinition(colDef);

      // Setup the cell editor
      TextCellEditor cellEditor = new TextCellEditor() {
        @Override
        protected int getOffsetLeft() {
          return -8;
        }

        @Override
        protected int getOffsetTop() {
          return -10;
        }

        @Override
        public boolean onAccept() {
          if (getValue().equals("")) {
            Window.alert("You must enter a school");
            return false;
          }
          return true;
        }
      };
      colDef.setCellEditor(cellEditor);
    }

    // Graduation year
    tcr.addColumnDefinition(new IntegerColumnDefinition() {
      @Override
      public Integer getCellValue(Student rowValue) {
        return rowValue.getGraduationYear();
      }

      @Override
      public void setCellValue(Student rowValue, Integer cellValue) {
        rowValue.setGraduationYear(cellValue);
      }
    });

    // GPA
    tcr.addColumnDefinition(new StudentColumnDefinition<Double>() {
      @Override
      public Double getCellValue(Student rowValue) {
        return rowValue.getGpa();
      }

      /**
       * Convert a double to human readable format with a max of 2 significant
       * digits.
       * 
       * @param gpa the GPA as a double
       * @return a more readable format of the GPA
       */
      private String gpaToString(Double gpa) {
        String gpaString = gpa.toString();
        if (gpaString.length() > 4) {
          gpaString = gpaString.substring(0, 4);
        }
        return gpaString;
      }

      @Override
      public void renderCellValue(Student rowValue, Double cellValue,
          HTMLCellView<Student> view) {
        view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        if (cellValue < 2) {
          view.setStyleName("badGPA");
        } else if (cellValue < 3) {
          view.setStyleName("goodGPA");
        } else {
          view.setStyleName("greatGPA");
        }
        view.addHTML(gpaToString(cellValue));
      }

      @Override
      public void renderCellValue(Student rowValue, Double cellValue,
          TableCellView<Student> view) {
        view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        if (cellValue < 2) {
          view.setStyleName("badGPA");
        } else if (cellValue < 3) {
          view.setStyleName("goodGPA");
        } else {
          view.setStyleName("greatGPA");
        }
        view.setHTML(gpaToString(cellValue));
      }

      @Override
      public void setCellValue(Student rowValue, Double cellValue) {
        rowValue.setGpa(cellValue);
      }
    });

    // ID
    tcr.addColumnDefinition(new IntegerColumnDefinition() {
      @Override
      public Integer getCellValue(Student rowValue) {
        return rowValue.getId();
      }

      @Override
      public void setCellValue(Student rowValue, Integer cellValue) {
        rowValue.setId(cellValue);
      }
    });

    // Pin
    tcr.addColumnDefinition(new IntegerColumnDefinition() {
      @Override
      public Integer getCellValue(Student rowValue) {
        return rowValue.getPin();
      }

      @Override
      public void setCellValue(Student rowValue, Integer cellValue) {
        rowValue.setPin(cellValue);
      }
    });
    return tcr;
  }

  /**
   * Get the cached table model.
   * 
   * @return the cached table model
   */
  @ShowcaseSource
  public CachedTableModel<Student> getCachedTableModel() {
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
  public PagingScrollTable<Student> getPagingScrollTable() {
    return (PagingScrollTable<Student>) scrollTable;
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
    //scrollTable.setSize("95%", "50%");
    //scrollTable.setResizePolicy(ScrollTable.ResizePolicy.FILL_WIDTH);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

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
  }

}
