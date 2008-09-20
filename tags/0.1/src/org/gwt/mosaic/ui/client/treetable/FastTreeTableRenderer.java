/*
 * Copyright 2006-2008 Google Inc. Copyright 2008 Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.treetable;

import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

public interface FastTreeTableRenderer {

  /**
   * Called to render a tree item row.
   * 
   * @param table
   * @param item
   * @param row
   */
  void renderTreeItem(FixedWidthFlexTable table, FastTreeTableItem item, int row);

}
