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
package org.gwt.mosaic.showcase.client.content.widgets;

import java.util.Date;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.datepicker.DatePicker;
import org.gwt.mosaic.ui.client.datepicker.DateTimePicker;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

/**
 * Example file.
 */
@ShowcaseStyle( {".mosaic-DatePicker"})
public class CwDatePicker extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {

  }

  /**
   * An instance of the constants.
   */
  @ShowcaseData
  private CwConstants constants;

  /**
   * Constructor.
   * 
   * @param constants the constants
   */
  public CwDatePicker(CwConstants constants) {
    super(constants);
    this.constants = constants;
  }

  @Override
  public String getDescription() {
    return "DatePicker description";
  }

  @Override
  public String getName() {
    return "DatePicker";
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    //
    // DatePicker
    //

    final CaptionLayoutPanel vPanel1 = new CaptionLayoutPanel("DatePicker");
    layoutPanel.add(vPanel1, new BoxLayoutData(FillStyle.BOTH, true));

    final DatePicker datePicker = new DatePicker();
    final Date d = new Date();
    d.setMonth(2);
    d.setDate(1);
    datePicker.setSelectedDate(d);
    vPanel1.add(datePicker, new BoxLayoutData(FillStyle.BOTH));

    // Log select events.
    final ChangeHandler<Date> changeHandler = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        InfoPanel.show("DatePicker ChangeHandler", event.getOldValue()
            + " --> " + event.getNewValue());
      }
    };
    datePicker.addChangeHandler(changeHandler);

    //
    // DateTimePicker
    //

    final CaptionLayoutPanel vPanel2 = new CaptionLayoutPanel("DateTimePicker");
    layoutPanel.add(vPanel2, new BoxLayoutData(FillStyle.BOTH, true));

    final DateTimePicker dateTimePicker = new DateTimePicker();
    // final Date d = new Date();
    // d.setMonth(2);
    // d.setDate(1);
    dateTimePicker.getDatePicker().setSelectedDate(d);
    vPanel2.add(dateTimePicker, new BoxLayoutData(FillStyle.BOTH));

    // Log select events.
    final ChangeHandler<Date> changeHandler2 = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        InfoPanel.show("DateTimePicker ChangeHandler",
            dateTimePicker.getDate().toString());
      }
    };
    dateTimePicker.getDatePicker().addChangeHandler(changeHandler2);
    dateTimePicker.getTimePicker().addChangeHandler(changeHandler2);

    return layoutPanel;
  }

}
