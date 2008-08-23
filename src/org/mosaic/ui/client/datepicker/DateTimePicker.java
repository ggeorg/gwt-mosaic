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
package org.mosaic.ui.client.datepicker;

import java.util.Date;

import org.mosaic.ui.client.WidgetWrapper;
import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.HasLayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;
import com.google.gwt.widgetideas.datepicker.client.TimePicker;

public class DateTimePicker extends Composite implements HasLayoutManager {
  
  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-DateTimePicker";

  private DatePicker datePicker;
  private TimePicker timePicker;

  private final ChangeHandler<Date> datePickerChangeHandler = new ChangeHandler<Date>() {
    public void onChange(ChangeEvent<Date> event) {
      timePicker.setDate(event.getNewValue());
    }
  };

  private final ChangeHandler<Date> timePickerChangeHandler = new ChangeHandler<Date>() {
    public void onChange(ChangeEvent<Date> event) {
      datePicker.setSelectedDate(event.getNewValue(), false);
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
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(1);
    initWidget(layoutPanel);
    
    this.datePicker = datePicker;
    this.timePicker = timePicker;
    
    layoutPanel.add(datePicker, new BoxLayoutData(FillStyle.BOTH));
    layoutPanel.add(new WidgetWrapper(timePicker), new BoxLayoutData(FillStyle.HORIZONTAL));
    
    timePicker.addChangeHandler(timePickerChangeHandler);
    datePicker.addChangeHandler(datePickerChangeHandler);
    
    setStyleName(DEFAULT_STYLENAME);
  }
  
  /**
   * 
   * @return the entered date
   */
  public Date getDate() {
    return timePicker.getDateTime();
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

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    getWidget().layout();
  }

}
