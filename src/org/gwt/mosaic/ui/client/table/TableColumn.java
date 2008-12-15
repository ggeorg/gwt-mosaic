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

/**
 * 
 * @param <T> the data type of the column associated with the editor
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class TableColumn<T> {
  
  /** The label of the column. */
  protected String label;

  /** The width of the column. */
  protected int width;
  
  /** The editor used to edit the data cells of the column. */
  protected TableCellEditor<T> cellEditor;
  
  public TableColumn(String label) {
    this(label, 100, null);
  }
  
  public TableColumn(String label, int width, TableCellEditor<T> cellEditor) {
    this.label = label;
    this.width = width;
    this.cellEditor = cellEditor;
  }

  public String getLabel() {
    return label;
  }

  public int getWidth() {
    return width;
  }

  public TableCellEditor<T> getCellEditor() {
    return cellEditor;
  }
  
}
