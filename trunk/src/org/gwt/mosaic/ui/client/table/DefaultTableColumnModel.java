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
 * @param <R> the type of the row value associated with the editor
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DefaultTableColumnModel<R> implements TableColumnModel<R> {

  /** Array of TableColumn objects. */
  protected final Vector<TableColumn<R>> tableColumns;

  /**
   * Creates a default table column model.
   */
  public DefaultTableColumnModel() {
    super();

    tableColumns = new Vector<TableColumn<R>>();
  }

  public void addColumn(TableColumn<R> column) {
    if (column == null) {
      throw new IllegalArgumentException("Object is null");
    }
    tableColumns.add(column);
  }

  public void removeColumn(TableColumn<R> column) {
    tableColumns.remove(column);
  }

  public int getColumnCount() {
    return tableColumns.size();
  }

  public TableColumn<R> getColumn(int columnIndex) {
    return (TableColumn<R>) tableColumns.elementAt(columnIndex);
  }

}
