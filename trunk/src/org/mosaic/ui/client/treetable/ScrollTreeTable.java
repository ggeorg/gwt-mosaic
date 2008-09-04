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
package org.mosaic.ui.client.treetable;

import org.mosaic.core.client.DOM;
import org.mosaic.ui.client.LayoutComposite;
import org.mosaic.ui.client.layout.BorderLayout;
import org.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

public class ScrollTreeTable extends LayoutComposite {

  /**
   * The header table.
   */
  private FixedWidthFlexTable headerTable = null;

  /**
   * The data table.
   */
  private FastTreeTable dataTable;

  /**
   * The footer table.
   */
  private FixedWidthFlexTable footerTable = null;

  public ScrollTreeTable(FastTreeTable dataTable,
      FixedWidthFlexTable headerTable) {
    super();
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BorderLayout());
    this.dataTable = dataTable;
    this.headerTable = headerTable;
    
    // Prepare the header and data tables
    prepareTable(dataTable, "dataTable");
    prepareTable(headerTable, "headerTable");
  }

  /**
   * Sets the amount of padding to be added around all cells.
   * 
   * @param padding the cell padding, in pixels
   */
  public void setCellPaddind(int padding) {
    headerTable.setCellPadding(padding);
    dataTable.setCellPadding(padding);
    if (footerTable != null) {
      footerTable.setCellPadding(padding);
    }
  }

  /**
   * Sets the amount of spacing to be added around all cells.
   * 
   * @param spacing the cell spacing, in pixels
   */
  public void setCellSpacing(int spacing) {
    headerTable.setCellSpacing(spacing);
    dataTable.setCellSpacing(spacing);
    if (footerTable != null) {
      footerTable.setCellSpacing(spacing);
    }
  }
  
  /**
   * Prepare a table to be added to the {@link ScrollTreeTable}.
   * 
   * @param table the table to prepare
   * @param cssName the style name added to the base name
   */
  private void prepareTable(Widget table, String cssName) {
    Element tableElem = table.getElement();
    DOM.setStyleAttribute(tableElem, "margin", "0px");
    DOM.setStyleAttribute(tableElem, "border", "0px");
    table.addStyleName(cssName);
  }
}
