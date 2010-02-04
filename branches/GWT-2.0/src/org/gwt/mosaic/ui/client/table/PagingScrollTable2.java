/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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
 * @author georgopoulos.georgios(at)gmail.com
 * @deprecated use PagingScrollTable instead
 */
@Deprecated
public class PagingScrollTable2<RowType> extends PagingScrollTable<RowType> {

  public PagingScrollTable2(TableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition) {
    super(tableModel, dataTable, headerTable, tableDefinition);
  }

  public PagingScrollTable2(TableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition, ScrollTableResources resources) {
    super(tableModel, dataTable, headerTable, tableDefinition, resources);
  }

  public PagingScrollTable2(TableModel<RowType> tableModel,
      TableDefinition<RowType> tableDefinition) {
    super(tableModel, tableDefinition);
  }

}
