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
package org.gwt.mosaic.showcase.client.content.widgets;

import java.util.Date;

import org.gwt.mosaic.showcase.client.ContentWidget;
import org.gwt.mosaic.showcase.client.Showcase;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseData;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseSource;
import org.gwt.mosaic.showcase.client.ShowcaseAnnotations.ShowcaseStyle;
import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.datepicker.DatePicker;
import org.gwt.mosaic.ui.client.datepicker.DateTimePicker;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

/**
 * Example file.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ShowcaseStyle( {".mosaic-DatePicker"})
public class CwDatePicker extends ContentWidget {

  /**
   * The constants used in this Page.
   */
  @ShowcaseSource
  public static interface CwConstants extends Constants,
      ContentWidget.CwConstants {
    String mosaicDatePickerDescription();

    String mosaicDatePickerName();
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
    return constants.mosaicDatePickerDescription();
  }

  @Override
  public String getName() {
    return constants.mosaicDatePickerName();
  }

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  @Override
  protected Widget onInitialize() {
    // Create a layout panel to align the widgets
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    final Date d = new Date();
    d.setMonth(2);
    d.setDate(1);

    //
    // DatePicker
    //

    final DatePicker datePicker = new DatePicker();
    datePicker.setSelectedDate(d);

    final CaptionLayoutPanel vPanel1 = new CaptionLayoutPanel(
        datePicker.getSelectedDate().toString());
    layoutPanel.add(vPanel1, new BoxLayoutData(FillStyle.BOTH, true));
    vPanel1.getHeader().add(Showcase.IMAGES.calendar().createImage());
    vPanel1.add(datePicker, new BoxLayoutData(FillStyle.BOTH));

    // Log select events.
    final ChangeHandler<Date> changeHandler = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        vPanel1.getHeader().setText(event.getNewValue().toString());
        vPanel1.layout();
      }
    };
    datePicker.addChangeHandler(changeHandler);

    //
    // DateTimePicker
    //

    final DateTimePicker dateTimePicker = new DateTimePicker();
    dateTimePicker.getDatePicker().setSelectedDate(d);

    final CaptionLayoutPanel vPanel2 = new CaptionLayoutPanel(
        dateTimePicker.getDate().toString());
    layoutPanel.add(vPanel2, new BoxLayoutData(FillStyle.HORIZONTAL, true));
    final ImageButton collapseBtn = new ImageButton(Caption.IMAGES.toolMinus());
    vPanel2.getHeader().add(Showcase.IMAGES.calendar().createImage());
    vPanel2.getHeader().add(collapseBtn, CaptionRegion.RIGHT);
    vPanel2.add(dateTimePicker, new BoxLayoutData(FillStyle.BOTH));

    collapseBtn.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        vPanel2.setCollapsed(!vPanel2.isCollapsed());
        final Image image = vPanel2.isCollapsed()
            ? Caption.IMAGES.toolPlus().createImage()
            : Caption.IMAGES.toolMinus().createImage();
        collapseBtn.setImage(image);
        layoutPanel.layout(true);
      }
    });

    // Log select events.
    final ChangeHandler<Date> changeHandler2 = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        vPanel2.getHeader().setText(dateTimePicker.getDate().toString());
        vPanel2.layout();
      }
    };
    dateTimePicker.getDatePicker().addChangeHandler(changeHandler2);
    dateTimePicker.getTimePicker().addChangeHandler(changeHandler2);

    return layoutPanel;
  }

}
