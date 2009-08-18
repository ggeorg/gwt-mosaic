/*
 * Copyright 2008 Google Inc. Copyright 2008 Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client.datepicker;

import java.util.Date;

import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Widget;

/**
 * Date picker.
 */
public class DatePicker extends
    com.google.gwt.widgetideas.datepicker.client.DatePicker implements
    HasLayoutManager {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DatePicker";

  /**
   * Sets up the date picker.
   */
  protected void setup() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(0);
    initWidget(layoutPanel);

    layoutPanel.add(this.getMonthSelector(), new BoxLayoutData(
        FillStyle.HORIZONTAL));
    showDate(new Date());
    layoutPanel.add(this.getCalendarView(), new BoxLayoutData(FillStyle.BOTH));

    setStyleName(DEFAULT_STYLENAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public Dimension getPreferredSize() {
    return ((LayoutPanel) getWidget()).getPreferredSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    ((LayoutPanel) getWidget()).layout();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate()
   */
  public void invalidate() {
    ((LayoutPanel) getWidget()).invalidate();
  }

}