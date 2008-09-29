/*
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

import org.gwt.mosaic.ui.client.ComboBox;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

public class DateComboBox extends ComboBox<DatePicker> {

  private static final String DEFAULT_STYLENAME = "mosaic-DateBox";

  private final DatePicker datePicker;

  private DateTimeFormat formatter = DateTimeFormat.getMediumDateFormat();

  private Timer updateTimer = new Timer() {
    public void run() {
      if (!isPopupVisible()) {
        showPopup();
      } else {
        DateComboBox.this.onShowPopup();
      }
    }
  };

  /**
   * Default constructor.
   */
  public DateComboBox() {
    this(new DatePicker());
  }

  public DateComboBox(final DatePicker datePicker) {
    super();

    this.datePicker = datePicker;

    super.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // Nothing to do here!
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        updateTimer.schedule(333);
      }
    });

    datePicker.addChangeHandler(new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        setText(event.getNewValue());
        hidePopup();
      }
    });

    addStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Gets the current date formatter.
   * 
   * @return the current date formatter
   */
  public DateTimeFormat getDateFormater() {
    return formatter;
  }

  /**
   * Gets the date picker.
   * 
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    return datePicker;
  }

  /**
   * Sets the date format to the given format. If date box is not empty,
   * contents of date box will be replaced with current date in new format.
   * 
   * @param formatter
   */
  public void setDateFormat(DateTimeFormat formatter) {
    if (formatter != this.formatter) {
      this.formatter = formatter;
      String current = super.getText();
      if (current != null && current.length() != 0) {
        try {
          super.setText(this.formatter.format(datePicker.getSelectedDate()));
        } catch (IllegalArgumentException e) {
          super.setText(""); // TODO use EMPTY_STRING
        }
      }
    }
  }

  /**
   * Show the given date in the date picker.
   * 
   * @param date picker
   */
  public void showDate(Date date) {
    datePicker.setSelectedDate(date, false);
    datePicker.showDate(date);
    setText(date);
  }

  protected void setText(Date value) {
    super.setText(formatter.format(value));
  }

  @Override
  protected DatePicker onShowPopup() {
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
    datePicker.showDate(current);

    return datePicker;
  }

  @Override
  protected boolean onHidePopup() {
    return true;
  }

}
