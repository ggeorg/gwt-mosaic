/*
 * Copyright 2006-2008 Google Inc.
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
package org.gwt.mosaic.ui.client.table;

import java.util.Vector;

/**
 * 
 * @param <T> the type of the row value
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DefaultTableColumnModel<T> implements TableColumnModel<T> {

  /** Array of TableColumn objects. */
  protected final Vector<TableColumn<?>> tableColumns = new Vector<TableColumn<?>>();

  /**
   * Creates a default table column model.
   */
  public DefaultTableColumnModel() {
    super();
  }

  /**
   * 
   * @param columnNames
   */
  public DefaultTableColumnModel(String[] columnNames) {
    super();
    for (int i = 0; i < columnNames.length; ++i) {
      tableColumns.add(new TableColumn<String>(columnNames[i], 100, new TextCellEditor()));
    }
  }

  public void addColumn(TableColumn<?> column) {
    if (column == null) {
      throw new IllegalArgumentException("Object is null");
    }
    tableColumns.add(column);
  }

  public void removeColumn(TableColumn<?> column) {
    tableColumns.remove(column);
  }

  public int getColumnCount() {
    return tableColumns.size();
  }

  public TableColumn<?> getColumn(int columnIndex) {
    return tableColumns.elementAt(columnIndex);
  }

}
