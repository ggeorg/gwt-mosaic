/*
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
 * @param <T> the data type of the row values
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public interface TableModel<T> {

  /**
   * Returns the number of rows in the model.
   * 
   * @return the number or rows in the model
   */
  public int getRowCount();

  /**
   * Sets the value in the cell at {@code columnIndex} and {@code rowIndex} to
   * {@code value}.
   * 
   * @param value the new value
   * @param rowIndex the row whose value is to be changed
   * @param columnIndex the column whose value is to be changed
   * @see #getValueAt(int, int)
   */
  public void setValueAt(Object value, int rowIndex, int columnIndex);

  /**
   * Returns the value for the cell at {@code columnIndex} and {@code rowIndex}.
   * 
   * @param rowIndex the row whose value is to be queried
   * @param columnIndex the column whose value is to be queried
   * @return the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex);

  /**
   * Adds a listener to the list that is notified each time a change to the data
   * model occurs.
   * 
   * @param listener the {@link TableModelListener}
   */
  public void addTableModelListener(TableModelListener listener);

  /**
   * Removes a listener from the list that is notified each time a change to the
   * data model occurs.
   * 
   * @param listener the {@link TableModelListener}
   */
  public void removeTableModelListener(TableModelListener listener);

}
