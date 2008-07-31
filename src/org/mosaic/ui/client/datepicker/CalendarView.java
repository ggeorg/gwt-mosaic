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
package org.mosaic.ui.client.datepicker;

import java.util.Date;


/**
 * The CalendarView is a calendar grid that represents the current view of a
 * {@link DatePicker}.
 * 
 * @param <MyDatePicker> the date picker type used with this calendar view.
 * 
 */
public abstract class CalendarView<MyDatePicker extends DatePicker>
    extends DatePickerComponent<MyDatePicker> {

  public CalendarView() {
  }

  /**
   * Adds a stylename to the cell of the supplied date. TEMP
   * 
   * @param date date that will have the supplied style added
   * @param styleName style name to add
   */
  public abstract void addDateStyle(Date date, String styleName);

  public void addDateStyles(Iterable<Date> dates, String styleName) {
    for (Date date : dates) {
      addDateStyle(date, styleName);
    }
  }

  /**
   * Returns the first date that is currently shown by the calendar.
   */
  public abstract Date getFirstVisibleDate();

  /**
   * Returns the last date that is currently shown by the calendar.
   */
  public abstract Date getLastVisibleDate();

  public abstract boolean isDateEnabled(Date d);

  public boolean isVisible(Date date) {
    Date first = getFirstVisibleDate();
    Date last = getLastVisibleDate();
    return (date != null && (first.equals(date) || last.equals(date) || (first.before(date) && last.after(date))));
  }

  public abstract void refresh();

  /**
   * Removes a stylename from the cell of the supplied date.
   * 
   * @param date date that will have the supplied style added
   * @param styleName style name to remove
   */
  public abstract void removeStyleName(Date date, String styleName);

  /**
   * Enables or Disables a particular date. by default all valid dates are
   * enabled after a rendering event. Disabled dates cannot be selected.
   * 
   * @param date date to enable or disable
   * 
   * @param enabled true for enabled, false for disabled
   */
  public abstract void setDateEnabled(Date date, boolean enabled);

  /**
   * Enables or disables multiple dates.
   * 
   * @param dates dates to [en|dis]able
   * @param enabled true to enable, false to disable
   */
  public void setEnabledDates(Iterable<Date> dates, boolean enabled) {
    for (Date date : dates) {
      setDateEnabled(date, enabled);
    }
  }

  protected final void updateHighlightedDate(Date date) {
    getDatePicker().setHighlightedDate(date);
  }
}