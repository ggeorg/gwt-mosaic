/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.showcase.client.content.uibinder.tables;

import java.util.List;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.shared.Student;
import org.gwt.mosaic.showcase.client.content.tables.shared.StudentGenerator;
import org.gwt.mosaic.ui.client.table.DefaultTableModel;
import org.gwt.mosaic.ui.client.table.PagingScrollTable;
import org.gwt.mosaic.ui.client.table.DefaultTableModel.ColumnComparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-LayoutPanel"})
public class CwUiBinderPagingScrollTable extends ContentWidget {
  interface Binder extends UiBinder<Widget, CwUiBinderPagingScrollTable> {
  }

  private static final Binder binder = GWT.create(Binder.class);

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwUiBinderPagingScrollTable(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "UiBinder PagingScrollTable description";
  }

  @Override
  public String getName() {
    return "PagingScrollTable";
  }

  @Override
  public boolean hasUiBinderSource() {
    return true;
  }

  /**
   * Initialized by UiBinder.
   */
  @ShowcaseData
  @UiField
  PagingScrollTable<Student> table;

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    binder.createAndBindUi(this);

    List<Student> students = StudentGenerator.generate(100);
    DefaultTableModel<Student> tableModel = new DefaultTableModel<Student>(
        students);
    tableModel.setColumnComparator(new ColumnComparator<Student>() {
      public int compare(Student t1, Student t2, int column) {
        switch (column) {
          case 0:
            return t1.getFirstName().compareTo(t2.getFirstName());
          case 1:
            return t1.getLastName().compareTo(t2.getLastName());
          case 2:
            return ((Integer) t1.getAge()).compareTo(t2.getAge());
        }
        return 0;
      }
    });

    table.setTableModel(tableModel);
    table.gotoFirstPage();

    return table;
  }

}
