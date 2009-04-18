/*
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
package org.gwt.mosaic.showcase.client.content.treetables;

import java.io.Serializable;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.showcase.client.content.tables.CwScrollTable;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.treetable.FastTreeTable;
import org.gwt.mosaic.ui.client.treetable.FastTreeTableItem;
import org.gwt.mosaic.ui.client.treetable.HasFastTreeTableItems;
import org.gwt.mosaic.ui.client.treetable.ScrollTreeTable;
import org.gwt.mosaic.ui.client.treetable.TreeTableLabelProvider;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".gwt-FastTreeTable"})
public class CwLazyTreeTable extends ContentWidget {

  /**
   * User object.
   */
  @ShowcaseSource
  private class Foo implements Serializable {
    private static final long serialVersionUID = 1120309810337478764L;

    int[] values = new int[5];

    public Foo() {
      for (int i = 0; i < values.length; i++) {
        values[i] = Random.nextInt();
      }
    }
  }

  /**
   * The data portion of the {@link ScrollTreeTable}.
   */
  @ShowcaseData
  protected FastTreeTable dataTable = null;

  /**
   * The header portion of the {@link CwScrollTable}.
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
  @ShowcaseData
  protected ScrollTreeTable scrollTreeTable = null;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwLazyTreeTable(CwConstants constants) {
    super(constants);

    // FastTreeTable.addDefaultCSS();
  }

  /**
   * The data table.
   * 
   * @return the data table.
   */
  @ShowcaseSource
  public FastTreeTable getDataTable() {
    if (dataTable == null) {
      dataTable = new FastTreeTable();
    }
    return dataTable;
  }

  @Override
  public String getDescription() {
    return "Lazy TreeTable demo";
  }

  /**
   * Get the header table.
   * 
   * @return the header table
   */
  @ShowcaseSource
  public FixedWidthFlexTable getFooterTable() {
    if (footerTable == null) {
      footerTable = new FixedWidthFlexTable();
    }
    return footerTable;
  }

  /**
   * Get the header table.
   * 
   * @return the header table
   */
  @ShowcaseSource
  public FixedWidthFlexTable getHeaderTable() {
    if (headerTable == null) {
      headerTable = new FixedWidthFlexTable();
    }
    return headerTable;
  }

  @Override
  public String getName() {
    return "Lazy TreeTable";
  }

  /**
   * 
   * @param parent
   * @param index
   * @param children
   */
  @ShowcaseSource
  private void lazyCreateChild(final HasFastTreeTableItems parent,
      final int index, final int children) {
    final FastTreeTableItem item = new FastTreeTableItem("child" + index + " ("
        + children + " children)") {

      private Timer t = new Timer() {
        public void run() {
          lazyCreateChilds();
        }
      };

      public void ensureChildren() {
        addStyleName("gwt-FastTreeTableItem-loading");
        t.schedule(3333);
      }

      @Override
      public Object getUserObject() {
        if (super.getUserObject() == null) {
          setUserObject(new Foo());
        }
        return super.getUserObject();
      }

      private void lazyCreateChilds() {
        try {
          for (int i = 0; i < children; i++) {
            lazyCreateChild(this, i, children + (i * 10));
          }
        } finally {
          removeStyleName("gwt-FastTreeTableItem-loading");
        }
      }
    };
    item.becomeInteriorNode();
    parent.addItem(item);
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel();

    // Add the scroll tree table to the page
    scrollTreeTable = new ScrollTreeTable(getDataTable(), getHeaderTable());
    scrollTreeTable.setFooterTable(getFooterTable());

    layoutPanel.add(scrollTreeTable);

    // Setup the header
    setupScrollTreeTable();

    // Add some data to the data table
    dataTable.resize(0, 5);
    dataTable.setSelectionPolicy(SelectionPolicy.MULTI_ROW);
    lazyCreateChild(dataTable, 0, 50);

    return layoutPanel;
  }

  /**
   * Setup the scroll table.
   */
  @ShowcaseSource
  private void setupScrollTreeTable() {
    // Setup the formatting
    scrollTreeTable.setCellPadding(3);
    scrollTreeTable.setCellSpacing(0);
    scrollTreeTable.setResizePolicy(ScrollTreeTable.ResizePolicy.UNCONSTRAINED);

    // header & footer data
    for (int cell = 0; cell < 5; cell++) {
      headerTable.setHTML(0, cell, "Header " + cell);
      footerTable.setText(0, cell, "Col " + cell);
    }

    // Setup some default widths
    scrollTreeTable.setColumnWidth(0, 250);
    scrollTreeTable.setColumnWidth(1, 100);
    scrollTreeTable.setColumnWidth(2, 100);
    scrollTreeTable.setColumnWidth(3, 100);
    scrollTreeTable.setColumnWidth(4, 100);

    // data table
    dataTable.addTreeTableLabelProvider(new TreeTableLabelProvider() {
      public Object getItemLabel(FastTreeTableItem item, int col) {
        Foo userObject = (Foo) item.getUserObject();
        switch (col) {
          case 1:
            return userObject.values[1];
          case 2:
            return userObject.values[2];
          case 3:
            return userObject.values[3];
          case 4:
            return userObject.values[4];
          default:
            return null;
        }
      }
    });
  }
  
}
