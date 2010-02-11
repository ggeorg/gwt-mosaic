/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import java.util.List;

/**
 * A {@link ColumnDefinition} that can be used when the row type is a simple
 * {@link List}.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <ColType> the data type of the column
 */
public class ListColumnDefinition<ColType> extends
    AbstractColumnDefinition<List<ColType>, ColType> {
  /**
   * The index of this column in the list.
   */
  private int index;

  /**
   * Construct a new {@link ListColumnDefinition}.
   * 
   * @param index the index of this column in the list
   */
  public ListColumnDefinition(int index) {
    this.index = index;
  }

  @Override
  public ColType getCellValue(List<ColType> rowValue) {
    if (rowValue.size() > index) {
      return rowValue.get(index);
    }
    return null;
  }

  @Override
  public void setCellValue(List<ColType> rowValue, ColType cellValue) {
    while (rowValue.size() <= index) {
      rowValue.add(null);
    }
    rowValue.set(index, cellValue);
  }
}
