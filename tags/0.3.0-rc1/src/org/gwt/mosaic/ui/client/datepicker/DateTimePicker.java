/*
 * Copyright 2008 Google Inc.
 * Copyright 2008 Georgios J. Georgopoulos.
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

import org.gwt.mosaic.ui.client.LayoutComposite;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.picker.client.TimePicker;

public class DateTimePicker extends LayoutComposite {
  
  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DateTimePicker";

  private DatePicker datePicker;
  private TimePicker timePicker;
  
  private final ValueChangeHandler<Date> datePickerChangeHandler = new ValueChangeHandler<Date>() {
    public void onValueChange(ValueChangeEvent<Date> event) {
      timePicker.setDate(event.getValue());
    }
  };   

  private final ValueChangeHandler<Date> timePickerChangeHandler = new ValueChangeHandler<Date>() {
    public void onValueChange(ValueChangeEvent<Date> event) {
      datePicker.setValue(event.getValue(), true);
    }
  };

  /**
   * Creates a {@link DateTimePicker} instance using the current date as
   * initial with AM/PM 12h {@link TimePicker}.
   */
  public DateTimePicker() {
    this(false);
  }

  /**
   * Creates a {@link DateTimePicker} instance using the current date as initial
   * value.
   * 
   * @param use24Hours
   */
  public DateTimePicker(boolean use24Hours) {
    this(new DatePicker(), new TimePicker(use24Hours));
  }
  
  /**
   * 
   * @param datePicker the {@link DatePicker} to be used
   * @param timePicker the {@link TimePicker} to be used
   */
  public DateTimePicker(DatePicker datePicker, TimePicker timePicker) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(1);
    
    this.datePicker = datePicker;
    this.timePicker = timePicker;
    
    layoutPanel.add(datePicker, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new WidgetWrapper(timePicker), new BoxLayoutData(FillStyle.HORIZONTAL));
    
    timePicker.addValueChangeHandler(timePickerChangeHandler);
    datePicker.addValueChangeHandler(datePickerChangeHandler);
    
    setStyleName(DEFAULT_STYLENAME);
  }
  
  /**
   * 
   * @return the entered date
   */
  @SuppressWarnings("deprecation")
  public Date getDate() {
    Date d = datePicker.getValue();
    d.setHours(timePicker.getDateTime().getHours());
    d.setMinutes(timePicker.getDateTime().getMinutes());
    d.setSeconds(timePicker.getDateTime().getSeconds());
    return d;
  }
  
  /**
   * This method causes the DatePicker to show the given date.
   */
  public final void showDate(Date date) {
    getDatePicker().setValue(date);
    getTimePicker().setDate(date);
  }
  
  /**
   * 
   * @return the {@link DatePicker}
   */
  public DatePicker getDatePicker() {
    return datePicker;
  }
  
  /**
   * 
   * @return the {@link TimePicker}
   */
  public TimePicker getTimePicker() {
    return timePicker;
  }

}
