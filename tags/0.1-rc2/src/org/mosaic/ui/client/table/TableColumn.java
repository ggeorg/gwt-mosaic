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

import org.mosaic.core.client.model.ModelElement;

public class TableColumn extends ModelElement {
  private static final long serialVersionUID = 6679711167414903261L;

  public static final String MODEL_INDEX = "modelIndex";

  public static final String WIDTH = "width";

  public static final String PREFERRED_WIDTH = "preferredWidth";

  public static final String MIN_WIDTH = "minWidth";

  public static final String MAX_WIDTH = "maxWidth";

  public static final String HEADER_VALUE = "headerValue";

  public static final String RESIZABLE = "resizable";

  public TableColumn() {
    this(0);
  }

  public TableColumn(int modelIndex) {
    this(modelIndex, 0);
  }

  public TableColumn(int modelIndex, int width) {
    super();
    
    setModelIndex(modelIndex);
    width = Math.max(width, 0);
    setWidth(width);
    setPreferredWidth(width);
    
    setMinWidth(Math.min(15, width));
    setMaxWidth(Integer.MAX_VALUE);
    
    setResizable(true);

    setHeaderValue(null);
  }

  /**
   * The header value of the column.
   */
  public Object getHeaderValue() {
    return getProperty(HEADER_VALUE);
  }

  /**
   * The maximum width of the column.
   */
  public int getMaxWidth() {
    return (Integer) getProperty(MAX_WIDTH);
  }

  /**
   * The minimum width of the column.
   */
  public int getMinWidth() {
    return (Integer) getProperty(WIDTH);
  }

  /**
   * The index of the column in the model.
   */
  public int getModelIndex() {
    return (Integer) getProperty(MODEL_INDEX);
  }

  /**
   * Returns the preferred width of the <code>TableColumn</code>. The default
   * preferred width is 75.
   * 
   * @return the <code>preferredWidth</code> property
   * @see #setPreferredWidth
   */
  public int getPreferredWidth() {
    return (Integer) getProperty(PREFERRED_WIDTH);
  }

  /**
   * The width of the column.
   */
  public int getWidth() {
    return (Integer) getProperty(WIDTH);
  }

  /**
   * If <code>true</code>, the user is allowed to resize the column; the
   * default is <code>true</code>.
   */
  public boolean isResizable() {
    return (Boolean) getProperty(RESIZABLE);
  }

  public void setHeaderValue(Object headerValue) {
    setProperty(HEADER_VALUE, headerValue);
  }

  public void setMaxWidth(int maxWidth) {
    setProperty(MAX_WIDTH, maxWidth);
  }

  public void setMinWidth(int minWidth) {
    setProperty(MIN_WIDTH, minWidth);
  }

  public void setModelIndex(int modelIndex) {
    setProperty(MODEL_INDEX, modelIndex);
  }

  /**
   * Sets this column's preferred width to <code>preferredWidth</code>. If
   * <code>preferredWidth</code> exceeds the minimum or maximum width, it is
   * adjusted to the appropriate limiting value.
   * 
   * @param preferredWidth the new preferred width
   * @see #getPreferredWidth
   */
  public void setPreferredWidth(int preferredWidth) {
    setProperty(PREFERRED_WIDTH, preferredWidth);
  }

  public void setResizable(boolean resizable) {
    setProperty(RESIZABLE, resizable);
  }

  public void setWidth(int width) {
    setProperty(WIDTH, width);
  }

}
