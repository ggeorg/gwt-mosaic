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
import org.gwt.mosaic.showcase.client.content.tables.StudentColumnDefinition.Group;
import org.gwt.mosaic.showcase.client.content.tables.StudentColumnDefinition.StudentFooterProperty;
import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.table.CachedTableModel;
import org.gwt.mosaic.ui.client.table.CellRenderer;
import org.gwt.mosaic.ui.client.table.ColumnDefinition;
import org.gwt.mosaic.ui.client.table.DefaultTableDefinition;
import org.gwt.mosaic.ui.client.table.FixedWidthGridBulkRenderer;
import org.gwt.mosaic.ui.client.table.ListCellEditor;
import org.gwt.mosaic.ui.client.table.PagingOptions;
import org.gwt.mosaic.ui.client.table.PagingScrollTable2;
import org.gwt.mosaic.ui.client.table.RadioCellEditor;
import org.gwt.mosaic.ui.client.table.TableDefinition;
import org.gwt.mosaic.ui.client.table.TextCellEditor;
import org.gwt.mosaic.ui.client.table.TableDefinition.AbstractCellView;
import org.gwt.mosaic.ui.client.table.property.FooterProperty;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-ScrollTable"})
public class CwPagingScrollTable2 extends ContentWidget {

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
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * The {@link CachedTableModel} around the main table model.
   */
  private CachedTableModel<Student> cachedTableModel = null;

  /**
   * The {@link PagingScrollTable2}.
   */
  private PagingScrollTable2<Student> pagingScrollTable = null;

  /**
   * The {@link DataSourceTableModel}.
   */
  private DataSourceTableModel tableModel = null;

  /**
   * The {@link DefaultTableDefinition}.
   */
  private DefaultTableDefinition<Student> tableDefinition = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwPagingScrollTable2(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  protected void createScrollTable() {
    // Setup the controller
    tableModel = new DataSourceTableModel();
    cachedTableModel = new CachedTableModel<Student>(tableModel);
    cachedTableModel.setPreCachedRowCount(50);
    cachedTableModel.setPostCachedRowCount(50);
    cachedTableModel.setRowCount(1000);

    // Create a TableCellRenderer
    TableDefinition<Student> tableDef = createTableDefinition();

    // Create the scroll table
    pagingScrollTable = new PagingScrollTable2<Student>(cachedTableModel,
        tableDef);
    pagingScrollTable.setPageSize(50);
    pagingScrollTable.setEmptyTableWidget(new HTML(
        "There is no data to display"));

    // Setup the bulk renderer
    FixedWidthGridBulkRenderer<Student> bulkRenderer = new FixedWidthGridBulkRenderer<Student>(
        pagingScrollTable.getDataTable(), pagingScrollTable);
    pagingScrollTable.setBulkRenderer(bulkRenderer);

    // Setup the formatting
    pagingScrollTable.setCellPadding(3);
    pagingScrollTable.setCellSpacing(0);
    pagingScrollTable.setResizePolicy(PagingScrollTable2.ResizePolicy.UNCONSTRAINED);
  }

  /**
   * @return the {@link TableDefinition} with all ColumnDefinitions defined.
   */
  private TableDefinition<Student> createTableDefinition() {
    // Define some cell renderers
    CellRenderer<Student, Integer> intCellRenderer = new CellRenderer<Student, Integer>() {
      public void renderRowValue(Student rowValue,
          ColumnDefinition<Student, Integer> columnDef,
          AbstractCellView<Student> view) {
        view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        view.setHTML(columnDef.getCellValue(rowValue).toString());
      }
    };

    // Create the table definition
    tableDefinition = new DefaultTableDefinition<Student>();

    // Set the row renderer
//    String[] rowColors = new String[] {"#FFFFDD", "#EEEEEE"};
//    tableDefinition.setRowRenderer(new DefaultRowRenderer<Student>(rowColors));

    // First name
    {
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "First Name", Group.GENERAL) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getFirstName();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setFirstName(cellValue);
        }
      };
      columnDef.setMinimumColumnWidth(50);
      columnDef.setPreferredColumnWidth(100);
      columnDef.setColumnSortable(true);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // Last name
    {
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "Last Name", Group.GENERAL) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getLastName();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setLastName(cellValue);
        }
      };
      columnDef.setMinimumColumnWidth(50);
      columnDef.setPreferredColumnWidth(100);
      columnDef.setColumnSortable(true);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // Age
    {
      StudentColumnDefinition<Integer> columnDef = new StudentColumnDefinition<Integer>(
          "Age", Group.GENERAL) {
        @Override
        public Integer getCellValue(Student rowValue) {
          return rowValue.getAge();
        }

        @Override
        public void setCellValue(Student rowValue, Integer cellValue) {
          rowValue.setAge(cellValue);
        }
      };

      // Dynamic footer provides range of ages
      StudentFooterProperty prop = new StudentFooterProperty() {
        @Override
        public Object getFooter(int row, int column) {
          if (row == 1) {
            int min = -1;
            int max = -1;
            int rowCount = pagingScrollTable.getDataTable().getRowCount();
            for (int i = 0; i < rowCount; i++) {
              int age = pagingScrollTable.getRowValue(i).getAge();
              if (min == -1) {
                min = age;
                max = age;
              } else {
                min = Math.min(min, age);
                max = Math.max(max, age);
              }
            }
            return min + "-" + max;
          }
          return super.getFooter(row, column);
        }
      };
      prop.setFooterCount(2);
      prop.setDynamic(true);
      columnDef.setColumnProperty(FooterProperty.TYPE, prop);

      columnDef.setCellRenderer(intCellRenderer);
      columnDef.setMinimumColumnWidth(35);
      columnDef.setPreferredColumnWidth(35);
      columnDef.setMaximumColumnWidth(35);
      columnDef.setColumnSortable(true);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // Gender
    {
      StudentColumnDefinition<Boolean> columnDef = new StudentColumnDefinition<Boolean>(
          "Gender", Group.GENERAL) {
        @Override
        public Boolean getCellValue(Student rowValue) {
          return rowValue.isMale();
        }

        @Override
        public void setCellValue(Student rowValue, Boolean cellValue) {
          rowValue.setMale(cellValue);
        }
      };
      columnDef.setCellRenderer(new CellRenderer<Student, Boolean>() {
        public void renderRowValue(Student rowValue,
            ColumnDefinition<Student, Boolean> columnDef,
            AbstractCellView<Student> view) {
          if (rowValue.isMale()) {
            view.setHTML("male");
          } else {
            view.setHTML("female");
          }
        }
      });
      columnDef.setMinimumColumnWidth(45);
      columnDef.setPreferredColumnWidth(45);
      columnDef.setMaximumColumnWidth(45);
      columnDef.setColumnSortable(true);
      tableDefinition.addColumnDefinition(columnDef);

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
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "Race", Group.GENERAL) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getRace();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setRace(cellValue);
        }
      };
      columnDef.setMinimumColumnWidth(45);
      columnDef.setPreferredColumnWidth(55);
      columnDef.setMaximumColumnWidth(70);
      columnDef.setColumnSortable(true);
      tableDefinition.addColumnDefinition(columnDef);

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
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "Favorite Color", null) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getFavoriteColor();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setFavoriteColor(cellValue);
        }
      };
      columnDef.setCellRenderer(new CellRenderer<Student, String>() {
        public void renderRowValue(Student rowValue,
            ColumnDefinition<Student, String> columnDef,
            AbstractCellView<Student> view) {
          String color = rowValue.getFavoriteColor();
          view.setStyleAttribute("color", color);
          view.setHTML(color);
        }
      });
      columnDef.setPreferredColumnWidth(80);
      columnDef.setColumnSortable(true);
      columnDef.setHeaderTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);

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
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "Preferred Sport", null) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getFavoriteSport();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setFavoriteSport(cellValue);
        }
      };
      columnDef.setPreferredColumnWidth(110);
      columnDef.setColumnSortable(true);
      tableDefinition.addColumnDefinition(columnDef);

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
      StudentColumnDefinition<String> columnDef = new StudentColumnDefinition<String>(
          "College", Group.SCHOOL) {
        @Override
        public String getCellValue(Student rowValue) {
          return rowValue.getCollege();
        }

        @Override
        public void setCellValue(Student rowValue, String cellValue) {
          rowValue.setCollege(cellValue);
        }
      };
      columnDef.setMinimumColumnWidth(50);
      columnDef.setPreferredColumnWidth(180);
      columnDef.setMaximumColumnWidth(250);
      columnDef.setColumnSortable(true);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);

      // Setup the cell editor
      TextCellEditor cellEditor = new TextCellEditor() {
        @Override
        public boolean onAccept() {
          if (getValue().equals("")) {
            Window.alert("You must enter a school");
            return false;
          }
          return true;
        }

        @Override
        protected int getOffsetLeft() {
          return -8;
        }

        @Override
        protected int getOffsetTop() {
          return -10;
        }
      };
      columnDef.setCellEditor(cellEditor);
    }

    // Graduation year
    {
      StudentColumnDefinition<Integer> columnDef = new StudentColumnDefinition<Integer>(
          "Year", Group.SCHOOL) {
        @Override
        public Integer getCellValue(Student rowValue) {
          return rowValue.getGraduationYear();
        }

        @Override
        public void setCellValue(Student rowValue, Integer cellValue) {
          rowValue.setGraduationYear(cellValue);
        }
      };

      // Dynamic footer provides range of ages
      StudentFooterProperty prop = new StudentFooterProperty() {
        @Override
        public Object getFooter(int row, int column) {
          if (row == 1) {
            int min = -1;
            int max = -1;
            int rowCount = pagingScrollTable.getDataTable().getRowCount();
            for (int i = 0; i < rowCount; i++) {
              int year = pagingScrollTable.getRowValue(i).getGraduationYear();
              if (min == -1) {
                min = year;
                max = year;
              } else {
                min = Math.min(min, year);
                max = Math.max(max, year);
              }
            }
            return min + "-" + max;
          }
          return super.getFooter(row, column);
        }
      };
      prop.setFooterCount(2);
      prop.setDynamic(true);
      columnDef.setColumnProperty(FooterProperty.TYPE, prop);

      columnDef.setCellRenderer(intCellRenderer);
      columnDef.setPreferredColumnWidth(35);
      columnDef.setMinimumColumnWidth(35);
      columnDef.setMaximumColumnWidth(35);
      columnDef.setColumnSortable(true);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // GPA
    {
      StudentColumnDefinition<Double> columnDef = new StudentColumnDefinition<Double>(
          "GPA", Group.SCHOOL) {
        @Override
        public Double getCellValue(Student rowValue) {
          return rowValue.getGpa();
        }

        @Override
        public void setCellValue(Student rowValue, Double cellValue) {
          rowValue.setGpa(cellValue);
        }
      };

      // Dynamic footer provides average GPA
      StudentFooterProperty prop = new StudentFooterProperty() {
        @Override
        public Object getFooter(int row, int column) {
          if (row == 1) {
            double avg = 0;
            int rowCount = pagingScrollTable.getDataTable().getRowCount();
            for (int i = 0; i < rowCount; i++) {
              avg += pagingScrollTable.getRowValue(i).getGpa();
            }
            avg /= rowCount;
            return gpaToString(avg);
          }
          return super.getFooter(row, column);
        }
      };
      prop.setFooterCount(2);
      prop.setDynamic(true);
      columnDef.setColumnProperty(FooterProperty.TYPE, prop);

      // Custom renderer uses background colors based on GPA
      columnDef.setCellRenderer(new CellRenderer<Student, Double>() {
        public void renderRowValue(Student rowValue,
            ColumnDefinition<Student, Double> columnDef,
            AbstractCellView<Student> view) {
          view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
          double gpa = rowValue.getGpa();
          if (gpa < 2) {
            view.setStyleName("badGPA");
          } else if (gpa < 3) {
            view.setStyleName("goodGPA");
          } else {
            view.setStyleName("greatGPA");
          }
          view.setHTML(gpaToString(gpa));
        }
      });
      columnDef.setPreferredColumnWidth(35);
      columnDef.setMinimumColumnWidth(35);
      columnDef.setMaximumColumnWidth(35);
      columnDef.setColumnSortable(true);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // ID
    {
      StudentColumnDefinition<Integer> columnDef = new StudentColumnDefinition<Integer>(
          "ID", Group.LOGIN) {
        @Override
        public Integer getCellValue(Student rowValue) {
          return rowValue.getId();
        }

        @Override
        public void setCellValue(Student rowValue, Integer cellValue) {
          rowValue.setId(cellValue);
        }
      };
      columnDef.setCellRenderer(intCellRenderer);
      columnDef.setPreferredColumnWidth(55);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    // Pin
    {
      StudentColumnDefinition<Integer> columnDef = new StudentColumnDefinition<Integer>(
          "Pin", Group.LOGIN) {
        @Override
        public Integer getCellValue(Student rowValue) {
          return rowValue.getPin();
        }

        @Override
        public void setCellValue(Student rowValue, Integer cellValue) {
          rowValue.setPin(cellValue);
        }
      };
      columnDef.setCellRenderer(intCellRenderer);
      columnDef.setPreferredColumnWidth(45);
      columnDef.setColumnTruncatable(false);
      tableDefinition.addColumnDefinition(columnDef);
    }

    return tableDefinition;
  }

  @Override
  public String getDescription() {
    return constants.mosaicPagingScrollTableDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicPagingScrollTableName() + "2";
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
    PagingOptions pagingOptions = new PagingOptions(pagingScrollTable);

    layoutPanel.add(pagingScrollTable, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new WidgetWrapper(pagingOptions), new BoxLayoutData(
        FillStyle.HORIZONTAL, true));

    return layoutPanel;
  }

  /**
   * Called when initialization has completed and the widget has been added to
   * the page.
   */
  protected void onInitializeComplete() {
    super.onInitializeComplete();

    pagingScrollTable.gotoFirstPage();
  }

}
