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
package org.gwt.mosaic.ui.client.treetable;

import org.gwt.mosaic.ui.client.ColumnWidget;

import com.google.gwt.user.client.Element;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

/**
 * ScrollTreeTable consists of a fixed header and footer (optional) that remain
 * visible and a scrollable body that contains the data.
 * <p>
 * In order for the columns in the header table and data table to line up, the
 * two table must have the same margin, padding, and border width. You can use
 * CSS style sheets to manipulate the colors and styles of the cell's, but you
 * must keep the actual sizes consistent (especially with respect to the left
 * and right side of the cells).
 * <p>
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li> .mosaic-ScrollTreeTable { applied to the entire widget } </li>
 * <li> .mosaic-ScrollTreeTable .dataTable { applied to the data table }
 * <li> .mosaic-ScrollTreeTable .footerTable { applied to the footer table }
 * <li> .mosaic-ScrollTreeTable .headerWrapper { wrapper around the header table }</li>
 * <li> .mosaic-ScrollTreeTable .dataWrapper { wrapper around the data table }</li>
 * <li> .mosaic-ScrollTreeTable .footerWrapper { wrapper around the footer table }</li>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ScrollTreeTable extends ColumnWidget {

  /**
   * The default style name.
   */
  public static final String DEFAULT_STYLENAME = "mosaic-ScrollTreeTable";

  public ScrollTreeTable(FastTreeTable dataTable,
      FixedWidthFlexTable headerTable) {
    super(dataTable, headerTable);

    setStylePrimaryName(DEFAULT_STYLENAME);
  }

  @Override
  protected void hoverCell(Element cellElem) {
    ((FastTreeTable) getDataTable()).hoverCell(cellElem);
  }

}
