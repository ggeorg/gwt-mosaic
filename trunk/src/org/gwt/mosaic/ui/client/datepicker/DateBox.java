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

import org.gwt.mosaic.core.client.util.DelayedRunnable;
import org.gwt.mosaic.ui.client.ComboBox;
import org.gwt.mosaic.ui.client.DropDownPanel;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;

public class DateBox extends ComboBox {

  private static final String DEFAULT_STYLENAME = "mosaic-DateBox";

  private final DatePicker datePicker;
  private final DropDownPanel<DateBox> popup;

  private DateTimeFormat formatter = DateTimeFormat.getMediumDateFormat();

  /**
   * Default constructor.
   */
  public DateBox() {
    this(new DatePicker());
  }

  public DateBox(final DatePicker datePicker) {
    super();
    
    this.datePicker = datePicker;
    
    popup = new DropDownPanel<DateBox>(this);
    popup.setWidget(datePicker);

    datePicker.addChangeHandler(new ChangeHandler<Date>() {
      public void onChange(ChangeEvent<Date> event) {
        setText(event.getNewValue());
        hideDatePicker();
        input.setFocus(true);
      }
    });

    input.addKeyboardListener(new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {

        switch (keyCode) {
          case KEY_ENTER:
          case KEY_TAB:
          case KEY_ESCAPE:
          case KEY_DOWN:
            updateDateFromTextBox();
            popup.hide();
            break;
          default:
            // dirtyText = false;
        }
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // TODO Auto-generated method stub
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
   * Hide the date picker.
   */
  public void hideDatePicker() {
    popup.hide();
  }

  public boolean isAnimationEnabled() {
    return popup.isAnimationEnabled();
  }

  public void setAnimationEnabled(boolean enable) {
    popup.setAnimationEnabled(enable);
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
    // dirtyText = false;
  }

  public void showDatePicker() {
    Date current = null;

    String value = super.getText().trim();
    if (value.length() != 0) {
      try {
        current = formatter.parse(value);
      } catch (IllegalArgumentException e) {
        // Log.info("cannot parse as date: " + value);
      }
    }

    if (current == null) {
      current = new Date();
    }
    datePicker.showDate(current);
    popup.show();
  }

  private void setText(Date value) {
    super.setText(formatter.format(value));
    // dirtyText = false;
  }

  private void updateDateFromTextBox() {
    String text = super.getText().trim();
    if (text.length() == 0) {
      return;
    }
    try {
      Date d = formatter.parse(text);
      showDate(d);
    } catch (IllegalArgumentException exception) {
      // TODO handleIllegalInput(text);
    }
    // dirtyText = false;
  }

  @Override
  protected void onButtonClick() {
    if (popup.isAttached()) {
      hideDatePicker();
    } else {
      showDatePicker();
    }
  }
}
