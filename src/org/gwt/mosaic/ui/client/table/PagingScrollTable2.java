/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PagingScrollTable2<RowType> extends
    com.google.gwt.gen2.table.client.PagingScrollTable<RowType> implements
    HasLayoutManager {

  public PagingScrollTable2(TableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition) {
    super(tableModel, dataTable, headerTable, tableDefinition);
  }

  public PagingScrollTable2(TableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition, ScrollTableImages images) {
    super(tableModel, dataTable, headerTable, tableDefinition, images);
  }

  public PagingScrollTable2(TableModel<RowType> tableModel,
      TableDefinition<RowType> tableDefinition) {
    super(tableModel, tableDefinition);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public Dimension getPreferredSize() {
    int width = getHeaderTable().getOffsetWidth();
    int height = getHeaderTable().getOffsetHeight()
        + getDataTable().getOffsetHeight();
    if (getFooterTable() != null) {
      height += getFooterTable().getOffsetHeight();
    }
    final int[] m = DOM.getMarginSizes(getElement());
    return new Dimension(width + m[1] + m[3], height + m[0] + m[2]);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate()
   */
  public void invalidate() {
    invalidate(null);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate(com.google.gwt.user.client.ui.Widget)
   */
  public void invalidate(Widget widget) {
    WidgetHelper.invalidate(getParent());
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    redraw();
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#needsLayout()
   */
  public boolean needsLayout() {
    return false;
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    // We don't need this
    ResizableWidgetCollection.get().remove(this);
  }

}
