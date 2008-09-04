/*
 * Copyright 2008 Google Inc.
 * Copyright 2008 Georgios J. Georgopoulos
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
package org.mosaic.showcase.client.pages;

import org.mosaic.showcase.client.Page;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.treetable.FastTreeTable;
import org.mosaic.ui.client.treetable.FastTreeTableItem;
import org.mosaic.ui.client.treetable.ScrollTreeTable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.ScrollTable;

/**
 * 
 */
@ShowcaseStyle({".gwt-FastTreeTable"})
public class BasicTreeTablePage extends Page {
  
  /**
   * The data portion of the {@link ScrollTreeTable}.
   */
  @ShowcaseData
  protected FastTreeTable dataTable = null;
  
  /**
   * The header portion of the {@link ScrollTablePage}.
   */
  @ShowcaseData
  protected FixedWidthFlexTable headerTable = null;
  
  /**
   * The footer portion of the {@link ScrollTable}.
   */
  @ShowcaseData
  protected FixedWidthFlexTable footerTable = null;
  
  /**
   * The scroll tree table.
   */
  protected ScrollTreeTable scrollTreeTable = null;

  public BasicTreeTablePage(DemoConstants constants) {
    super(constants);
    FastTreeTable.addDefaultCSS();
  }
  
  /**
   * The data table.
   * 
   * @return the data table.
   */
  public FastTreeTable getDataTable() {
    if (dataTable == null) {
      dataTable = new FastTreeTable();
    }
    return dataTable;
  }
  
  /**
   * Get the header table.
   * 
   * @return the header table
   */
  public FixedWidthFlexTable getHeaderTable() {
    if (headerTable == null) {
      headerTable = new FixedWidthFlexTable();
    }
    return headerTable;
  }
  
  /**
   * Get the header table.
   * 
   * @return the header table
   */
  public FixedWidthFlexTable getFooterTable() {
    if (footerTable == null) {
      footerTable = new FixedWidthFlexTable();
    }
    return footerTable;
  }
  
  /**
   * Get the scroll tree table.
   * 
   * @return the scroll tree table
   */
  public ScrollTreeTable getScrollTreeTable() {
    return scrollTreeTable;
  }

  /**
   * 
   */
  @ShowcaseSource
  @Override
  protected void onPageLoad(LayoutPanel layoutPanel) {
    // Create the inner tables
    getHeaderTable();
    getFooterTable();
    getDataTable();
    
    // Add the scroll tree table to the page
    scrollTreeTable = new ScrollTreeTable(dataTable, headerTable);
    //scrollTreeTable.setFooterTable(footerTable);
    
    // Setup the header
    setupScrollTreeTable();
    
    // Add some data to the data table
    dataTable.resize(0, 13);
    
    final FastTreeTable t = dataTable;
    final FastTreeTableItem a = t.addItem("A root tree item");
    a.addItem("A child");
    final FastTreeTableItem aXb = a.addItem("Another child");
    aXb.addItem("a grand child");
    final FastTreeTableItem widgetBranch = a.addItem(new CheckBox("A checkbox child"));
    final FastTreeTableItem textBoxParent = widgetBranch.addItem("A TextBox parent");
    textBoxParent.addItem(new TextBox());
    textBoxParent.addItem("and another one...");
    textBoxParent.addItem(new TextArea());
    
    final ListBox lb = new ListBox();
    for (int i = 0; i < 100; i++) {
      lb.addItem(i + "");
    }
    
    widgetBranch.addItem("A ListBox parent").addItem(lb);
    
    ScrollPanel panel = new ScrollPanel();
    layoutPanel.add(panel);
    layoutPanel.setPadding(0);
    panel.add(t);
  }
  
  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  private void setupScrollTreeTable() {
    // Setup the formatting
    scrollTreeTable.setCellPaddind(3);
    scrollTreeTable.setCellSpacing(1);
  }

  @Override
  public String getName() {
    return "Basic TreeTable";
  }

}
