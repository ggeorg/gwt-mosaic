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

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.constants.DateTimeConstants;

import java.util.Date;

/**
 * Model used to get calendar information.
 */
@SuppressWarnings( {"deprecation"})
public class CalendarModel {

  public static final int WEEKS_IN_MONTH = 6;
  public static final int DAYS_IN_WEEK = 7;

  private static final DateTimeConstants intlConstants = (DateTimeConstants) GWT.create(DateTimeConstants.class);

  /**
   * dayOfWeekNames is kept as strings because used only once for initial
   * drawing.
   */
  private static final String[] dayOfWeekNames = new String[7];
  private static final DateTimeFormat dayOfMonthFormatter = DateTimeFormat.getFormat("d");
  private static final DateTimeFormat yearFormatter = DateTimeFormat.getFormat("yyyy");
  private static final DateTimeFormat dayOfWeekFormatter = DateTimeFormat.getFormat("ccccc");
  private static final DateTimeFormat monthAndYearFormatter = DateTimeFormat.getFormat("MMM yyyy");
  private static final DateTimeFormat dateFormatter = DateTimeFormat.getShortDateFormat();

  /**
   * Is the year before the month?
   * 
   * @return is the year before the month
   */
  public static boolean computeYearBeforeMonth() {
    // Finding whether year is before month
    String[] dateFormats = intlConstants.dateFormats();
    String dateLongFormat = dateFormats[3];

    int yIndex = dateLongFormat.indexOf("y");
    int mIndex = dateLongFormat.indexOf("M");

    return (yIndex < mIndex);
  }

  /**
   * Copies the given date.
   * 
   * @param date the date
   * @return the copy
   */
  public static Date copy(Date date) {
    Date newDate = new Date();
    newDate.setTime(date.getTime());
    return newDate;
  }

  /**
   * Returns the number of days between the two dates. Time is ignored.
   * 
   * @param start starting date
   * @param finish ending date
   * @return the different
   */
  public static int diffDays(Date start, Date finish) {
    if (hasTime(start)) {
      start = copy(start);
      resetTime(start);
    }

    if (hasTime(finish)) {
      finish = copy(finish);
      resetTime(finish);
    }

    long aTime = start.getTime();
    long bTime = finish.getTime();

    long adjust = 60 * 60 * 1000;
    adjust = (bTime > aTime) ? adjust : -adjust;

    return (int) ((bTime - aTime + adjust) / (24 * 60 * 60 * 1000));
  }

  /**
   * Returns the day of the week on which week starts as per the locale. The
   * range between 0 for Sunday and 6 for Saturday.
   * 
   * @return the day of the week on which week starts as per the locale.
   */
  public static int getLocaleStartingDayOfWeek() {
    return Integer.parseInt(intlConstants.firstDayOfTheWeek()) - 1;
  }

  /**
   * Shift the date by the given number of days.
   * 
   * @param date the date
   * @param days number of days
   */
  public static void shiftDays(Date date, int days) {
    date.setDate(date.getDate() + days);
  }

  /**
   * Shift the date by the given number of months.
   * 
   * @param date the date
   * @param months number of months
   */
  public static void shiftMonths(Date date, int months) {
    if (months != 0) {
      int month = date.getMonth();
      int year = date.getYear();

      int resultMonthCount = year * 12 + month + months;
      int resultYear = resultMonthCount / 12;
      int resultMonth = resultMonthCount - resultYear * 12;

      date.setMonth(resultMonth);
      date.setYear(resultYear);
    }
  }

  /**
   * Return the Date object with time set to 00:00:00. Keeping a fixed the time
   * of day intended to make it easier to find day differences of dates that are
   * initiated to different times of the day.
   * 
   * @return new Date object
   */
  private static Date createDateWithoutTime() {
    Date date = new Date();
    resetTime(date);
    return date;
  }

  /**
   * Return the Date object with date set to 1 and time set to 00:00:00.
   * 
   * @return new Date object
   */
  private static Date createFirstDayOfMonth() {
    Date date = createDateWithoutTime();
    date.setDate(1);
    return date;
  }

  private static boolean hasTime(Date start) {
    return start.getHours() != 0 || start.getMinutes() != 0
        || start.getSeconds() != 0;
  }

  /**
   * Resets the date to have no time modifiers.
   * 
   * @param date the date
   */
  private static void resetTime(Date date) {
    long msec = date.getTime();
    msec = (msec / 1000) * 1000;
    date.setTime(msec);

    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
  }

  private final Date curMonthAndYear = createFirstDayOfMonth();

  private int firstDayOfWeekend;

  private int lastDayOfWeekend;

  private String[] dayOfMonthNames = new String[32];

  private int numDaysInMonth;

  /**
   * Constructor.
   */
  public CalendarModel() {
    // Finding day of week names
    Date date = new Date();
    for (int i = 1; i <= 7; i++) {
      date.setDate(i);
      int dayOfWeek = date.getDay();
      dayOfWeekNames[dayOfWeek] = dayOfWeekFormatter.format(date);
    }

    // Finding day of month names
    date.setMonth(0);

    for (int i = 1; i < 32; ++i) {
      date.setDate(i);
      dayOfMonthNames[i] = dayOfMonthFormatter.format(date);
    }

    // Finding the start and end of weekend
    firstDayOfWeekend = Integer.parseInt(intlConstants.weekendRange()[0]) - 1;
    lastDayOfWeekend = Integer.parseInt(intlConstants.weekendRange()[1]) - 1;
  }

  /**
   * Create a date in the current month at the given day.
   * 
   * @param dayOfMonth the day in the month
   * @return the new date
   */
  public Date createDate(int dayOfMonth) {
    Date d = new Date(curMonthAndYear.getTime());
    d.setDate(dayOfMonth);
    return d;
  }

  /**
   * Create a unique string key for the given date.
   * 
   * @param date the date
   * @return the key
   */
  public String createKeyFromDate(Date date) {
    return date.getYear() + "/" + date.getMonth() + "/" + date.getDate();
  }

  /**
   * Format the date using this model's formatter.
   * 
   * @param date date
   * @return string
   */
  public String format(Date date) {
    if (date == null) {
      return "";
    } else {
      return dateFormatter.format(date);
    }
  }

  /**
   * Formats the month in year.
   * 
   * @return the formatted month in year
   */
  public String formatCurrentMonth() {
    return monthAndYearFormatter.format(curMonthAndYear);
  }

  /**
   * Formats the year.
   * 
   * @return the formatted year
   */
  public String formatCurrentYear() {
    return yearFormatter.format(curMonthAndYear);
  }

  /**
   * Format the date's day of month.
   * 
   * @param date the date
   * @return the formated day of month
   */
  public String formatDayOfMonth(Date date) {
    return dayOfMonthNames[date.getDate()];
  }

  /**
   * Formats the date's day of month.
   * 
   * @param dayOfMonth day of month
   * @return the formatted day of month
   */
  public String formatDayOfMonth(int dayOfMonth) {
    return dayOfMonthNames[dayOfMonth];
  }

  /**
   * Format the day in the week.
   * 
   * @param dayInWeek the day in week
   * @return the formatted day in week
   */
  public String formatDayOfWeek(int dayInWeek) {
    return dayOfWeekNames[dayInWeek];
  }

  /**
   * Get the current month.
   * 
   * @return the current month
   */
  public int getCurrentMonth() {
    return curMonthAndYear.getMonth();
  }

  /**
   * Gets the current month and year.
   * 
   * @return the month and year
   */
  public Date getCurrentMonthAndYear() {
    return curMonthAndYear;
  }

  /**
   * Returns size of the current month.
   * 
   * @return number of days in the current month
   */
  public int getCurrentNumberOfDaysInMonth() {
    return numDaysInMonth;
  }

  /**
   * Gets the current year.
   * 
   * @return the current year
   */
  public int getCurrentYear() {
    return curMonthAndYear.getYear();
  }

  /**
   * Gets the first day of the first week in the current month and year.
   * 
   * @return the first day
   */
  public Date getFirstDayOfCurrentFirstWeek() {
    int wkDayOfMonth1st = curMonthAndYear.getDay();
    if (wkDayOfMonth1st == getLocaleStartingDayOfWeek()) {
      return curMonthAndYear;
    } else {
      Date d = new Date(curMonthAndYear.getTime());
      int offset = wkDayOfMonth1st - getLocaleStartingDayOfWeek() > 0
          ? wkDayOfMonth1st - getLocaleStartingDayOfWeek() : DAYS_IN_WEEK
              - (getLocaleStartingDayOfWeek() - wkDayOfMonth1st);
      shiftDays(d, -offset);
      return d;
    }
  }

  /**
   * Is the date in the current month.
   * 
   * @param date date
   * @return date
   */
  public boolean isInCurrentMonth(Date date) {
    return curMonthAndYear.getMonth() == date.getMonth();
  }

  /**
   * Is the day of the week a weekend?
   * 
   * @param dayOfWeek day of week
   * @return is the day of week a weekend?
   */
  public boolean isWeekend(int dayOfWeek) {
    return dayOfWeek == firstDayOfWeekend || dayOfWeek == lastDayOfWeekend;
  }

  /**
   * Default formatter for date parsing.
   * 
   * @param text date to parse
   * @return resulting Date object
   */
  public Date parseDate(String text) {
    return dateFormatter.parse(text);
  }

  /**
   * Sets the current month and year
   * 
   * @param currentMonthAndYear the current month and year
   */
  public void setCurrentMonthAndYear(Date currentMonthAndYear) {
    this.curMonthAndYear.setYear(currentMonthAndYear.getYear());
    this.curMonthAndYear.setMonth(currentMonthAndYear.getMonth());
    refresh();
  }

  /**
   * Shifts the current month by the given number of months. The day of the
   * month will be pinned to the original value as far as possible.
   * 
   * @param deltaMonths - number of months to be added to the current date
   */
  public void shiftCurrentMonth(int deltaMonths) {
    shiftMonths(curMonthAndYear, deltaMonths);
    refresh();
  }

  /**
   * Private method getNumberOfDaysInMonth() gets number of days in the month
   * indicated.
   * 
   * @param deltaMonth - the position of the month with respect to current month
   * 
   * @return number of days in the month
   */

  private int computeDaysInMonth(int deltaMonth) {
    int month = curMonthAndYear.getMonth();
    int year = curMonthAndYear.getYear();

    Date a = createDate(1);
    Date b = createDate(1);

    int resultMonthCount = year * 12 + month + deltaMonth;

    int aYear = resultMonthCount / 12;
    int aMonth = resultMonthCount - aYear * 12;

    resultMonthCount++;

    int bYear = resultMonthCount / 12;
    int bMonth = resultMonthCount - bYear * 12;

    a.setMonth(aMonth);
    a.setYear(aYear);

    b.setMonth(bMonth);
    b.setYear(bYear);

    int diff = diffDays(a, b);

    return (diff >= 0) ? diff : -diff;
  }

  private void refresh() {
    // int weekStart = getLocaleStartingDayOfWeek();
    // int dayOfWeek = curMonthAndYear.getDay();
    // int date = curMonthAndYear.getDate();
    // offset from Sunday == 0; +70 to make number +ve
    // int offset = (dayOfWeek - date + 1 - weekStart + 70) % 7;
    numDaysInMonth = computeDaysInMonth(0);
  }

}
