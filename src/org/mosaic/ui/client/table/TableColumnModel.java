/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client.table;

import java.util.Iterator;

import org.mosaic.ui.client.event.TableColumnModelListener;

/**
 * Defines the requirements for a table column model object suitable for use
 * with {@link Table}.
 */
public interface TableColumnModel {

  public void addColumn(TableColumn column);
  
  public void removeColumn(TableColumn column);
  
  public void moveColumn(int columnIndex, int newIndex);
  
  public int getColumnCount();

  public Iterator<TableColumn> getColumns();
  
  public TableColumn getColumn(int columnIndex);
  
  public int getColumnIndex(TableColumn column);
  
  public void setColumnSelectionAllowed(boolean flag);
  
  public boolean getColumnSelectionAllowed();
  
  public int[] getSelectedColumns();
  
  public int getSelectedColumnCount();
  
  // public void setSelectionModel(ListSelectionModel newModel);
  
  // public ListSelectionModel getSelectionModel();
  
  public void addColumnModelListener(TableColumnModelListener listener);
  
  public void removeColumnModelListener(TableColumnModelListener listener);
}
