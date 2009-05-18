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
package org.gwt.mosaic.ui.client.datepicker;

import java.util.Date;

import org.gwt.mosaic.ui.client.ComboBoxBase;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class DateTimeComboBox extends ComboBoxBase<DateTimePicker> {

  private static final String DEFAULT_STYLENAME = "mosaic-DateTimeBox";

  private final DateTimePicker dateTimePicker;

  private DateTimeFormat formatter = DateTimeFormat.getMediumDateTimeFormat();

  /**
   * Default constructor.
   */
  public DateTimeComboBox() {
    this(new DateTimePicker());
  }

  public DateTimeComboBox(final DateTimePicker dateTimePicker) {
    super();

    this.dateTimePicker = dateTimePicker;

    final ChangeHandler<Date> changeHandler = new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        setText(dateTimePicker.getDate());
        hidePopup();
      }
    };
    dateTimePicker.getDatePicker().addChangeHandler(changeHandler);
    // dateTimePicker.getTimePicker().addChangeHandler(changeHandler);

    addStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Gets the current date time formatter.
   * 
   * @return the current date time formatter
   */
  public DateTimeFormat getDateTimeFormater() {
    return formatter;
  }

  /**
   * Gets the date time picker.
   * 
   * @return the date time picker
   */
  public DateTimePicker getDateTimePicker() {
    return dateTimePicker;
  }

  @Override
  protected boolean onHidePopup() {
    return true;
  }

  @Override
  protected DateTimePicker onShowPopup() {
    Date current = null;

    String value = super.getText().trim();
    if (value.length() != 0) {
      try {
        showDate(current = formatter.parse(value));
      } catch (IllegalArgumentException e) {
        // Ignore!
      }
    }

    if (current == null) {
      current = new Date();
    }
    dateTimePicker.showDate(current);

    return dateTimePicker;
  }

  /**
   * Sets the date time format to the given format. If date box is not empty,
   * contents of date time box will be replaced with current date time in new
   * format.
   * 
   * @param formatter
   */
  public void setDateTimeFormat(DateTimeFormat formatter) {
    if (formatter != this.formatter) {
      this.formatter = formatter;
      String current = super.getText();
      if (current != null && current.length() != 0) {
        try {
          super.setText(this.formatter.format(dateTimePicker.getDate()));
        } catch (IllegalArgumentException e) {
          super.setText(""); // TODO use EMPTY_STRING
        }
      }
    }
  }

  protected void setText(Date value) {
    super.setText(formatter.format(value));
  }

  /**
   * Show the given date in the date picker.
   * 
   * @param date picker
   */
  public void showDate(Date date) {
    dateTimePicker.getDatePicker().setSelectedDate(date, false);
    dateTimePicker.getDatePicker().showDate(date);
    dateTimePicker.getTimePicker().setDateTime(date);
    setText(date);
  }

}
