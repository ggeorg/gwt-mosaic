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
package org.gwt.mosaic.showcase.client.content.panels;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.ColumnView;
import org.gwt.mosaic.ui.client.ColumnView.ColumnViewItem;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-ColumnView"})
public class CwColumnView extends ContentWidget {

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwColumnView(CwConstants constants) {
    super(constants);
  }

  @Override
  public String getDescription() {
    return "Column View (Miller Columns) are a browsing/visualization "
        + "technique that can be applied to tree structures. The columns "
        + "allow multiple levels of hierarchy to be open at once, and "
        + "provide a visual representation of the current location.";
  }

  @Override
  public String getName() {
    return "Column View";
  }

  /**
   * The ColumnView.
   */
  @ShowcaseData
  private ColumnView<String> columnView = new ColumnView<String>();

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    ColumnViewItem<String> item = new ColumnViewItem<String>("Column #");
    columnView.addColumn(0, item, "33em");
    item.setWidget(createButton(item));
    columnView.invalidate(item);
    columnView.layout();

    return columnView;
  }

  /**
   * 
   */
  @ShowcaseSource
  private Widget createButton(final ColumnViewItem<String> parent) {
    return new Button(parent.getData() + columnView.size()
        + "<br/><small>[Click me to expand]</small>", new ClickHandler() {
      public void onClick(ClickEvent event) {
        ColumnViewItem<String> item = new ColumnViewItem<String>("Column #");
        columnView.addColumn(columnView.indexOf(parent) + 1, item, "20em");
        item.setWidget(createButton(item));
        columnView.invalidate(item);
        columnView.layout();
      }
    });
  }
}
