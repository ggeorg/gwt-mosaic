/*
 * Copyright 2007 Google Inc.
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
package org.mosaic.ui.client.scrolltable;

import org.mosaic.ui.client.scrolltable.overrides.FlexTable;

/**
 * A helper class to enable bulk loading of {@link FlexTable}. Once we have 1.5
 * support it will extend TableBulkLoader<FlexTable>.
 * <p>
 * Important note: Must use {@link FlexTable} in overrides package NOT the
 * standard 1.4 FlexTable.
 */
public class FlexTableBulkRenderer extends TableBulkRenderer {

  /**
   * Constructor.
   * 
   * @param table the table to be bulk loaded
   * 
   */
  public FlexTableBulkRenderer(FlexTable table) {
    super(table);
  }
  
  /**
   * Constructor.
   * 
   * @param table the table to be bulk loaded
   * 
   */
  public FlexTableBulkRenderer(FlexTable table, CellRenderer renderer) {
    super(table, renderer);
  }
}
