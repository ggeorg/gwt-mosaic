package org.mosaic.ui.client.table;

public interface TableModel {

  public int getRowCount();
  
  public int getColumnCount();
  
  public String getColumnName(int columnIndex);
  
  public boolean isCellEditable(int rowIndex, int columnIndex);
  
  public Object getValueAt(int rowIndex, int columnIndex);
  
  public void setValueAt(Object value, int rowIndex, int columnIndex);
  
  // addTableModelListener(TableModelListener l);
  
  // removeTableModelListener(TableModelListener l); 
}
