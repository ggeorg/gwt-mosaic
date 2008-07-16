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
