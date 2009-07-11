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
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class ScrollTable2 extends com.google.gwt.gen2.table.client.ScrollTable
    implements HasLayoutManager {

  public static class DataGrid extends FixedWidthGrid {

    public DataGrid() {
      // Nothing to do here!
    }

  }

  /**
   * Construct a new {@link ScrollTable2}.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   */
  public ScrollTable2(FixedWidthGrid dataTable, FixedWidthFlexTable headerTable) {
    super(dataTable, headerTable);
  }

  /**
   * Construct a new {@link ScrollTable2}.
   * 
   * @param dataTable the data table
   * @param headerTable the header table
   * @param images the images to use in the table
   */
  public ScrollTable2(FixedWidthGrid dataTable,
      FixedWidthFlexTable headerTable, ScrollTableImages images) {
    super(dataTable, headerTable, images);
  }

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

  public void invalidate() {
    WidgetHelper.invalidate(getParent());
  }

  public void layout() {
    redraw();
  }

  @Override
  protected void onLoad() {
    super.onLoad();

    // We don't need this
    ResizableWidgetCollection.get().remove(this);
  }

}
