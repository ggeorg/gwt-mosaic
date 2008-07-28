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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mosaic.core.client.event.ChangeEvent;
import org.mosaic.core.client.event.ChangeHandler;
import org.mosaic.core.client.event.EventHandlers;
import org.mosaic.core.client.event.FiresChangeEvents;
import org.mosaic.core.client.event.FiresHighlightEvents;
import org.mosaic.core.client.event.FiresRenderingEvents;
import org.mosaic.core.client.event.HighlightEvent;
import org.mosaic.core.client.event.HighlightHandler;
import org.mosaic.core.client.event.RenderingEvent;
import org.mosaic.core.client.event.RenderingHandler;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Date picker.
 */
public class DatePicker extends Composite implements FiresChangeEvents<Date>,
    FiresHighlightEvents<Date>, FiresRenderingEvents {

  /**
   * Styles used for the DatePicker family of widgets.
   */
  protected static class Styles {
    /**
     * Style for month selector.
     */
    public static final String MONTH_SELECTOR = "month-selector";
    /**
     * The month forward widget.
     */
    public static final String MONTH_FORWARD = "month-forward";
    /**
     * The month backward widget.
     */
    public static final String MONTH_BACKWARD = "month-backward";
    /**
     * The label displaying the current month.
     */
    public static final String MONTH_LABEL = "month-label";
    /**
     * Date picker style name.
     */
    public static final String DEFAULT = "gwt-DatePicker";

    /**
     * The full date cell grid.
     */
    public static final String CALENDAR_VIEW = "calendar-view";
    /**
     * Day of week title i.e. "M/Tu/W/...
     */
    public static final String DAY_TITLE = "day-title";
    /**
     * Weekend modifier.
     */
    public static final String WEEKEND = "weekend";

    /**
     * Active date cell.
     */
    public static final String DATE_CELL = "date-cell";

    /**
     * Filler begin or ending date cell.
     */
    public static final String FILLER_CELL = "filler-cell";
    /**
     * Selected date cell.
     */
    public static final String SELECTED_CELL = "selected-cell";
    /**
     * highlighted date cell.
     */
    public static final String HIGHLIGHTED_CELL = "highlighted-cell";

    /**
     * Disabled date cell.
     */
    public static final String DISABLED_CELL = "disabled-cell";
    /**
     * Today's date cell.
     */
    public static final String TODAY_CELL = "today-cell";
  }

  private class DateStyler {
    private Map<String, String> info = new HashMap<String, String>();

    public String getStyleName(Date d) {
      return info.get(genKey(d));
    }

    public void setStyleName(Date d, String styleName, boolean add) {
      // TODO should use UIObject's code long term. This code therefore is
      // assuming good input and no StyleNames that are proper subsets of each
      // other.
      String key = genKey(d);

      String current = info.get(key);
      if (add) {
        if (current == null) {
          info.put(key, styleName);
        } else if (current.indexOf(styleName) == -1) {
          info.put(key, current + " " + styleName);
        }
      } else {

        if (current == null) {
          throw new IllegalStateException("Removing style " + styleName
              + " from date " + d + " but the stylename wasn't there");
        }
        String newValue = current.replaceAll(styleName, "");
        if (newValue.equals("")) {
          info.remove(key);
        } else {
          info.put(key, newValue);
        }
      }
    }

    private String genKey(Date d) {
      return getModel().createKeyFromDate(d);
    }
  }
  
  private EventHandlers handlers = new EventHandlers();

  private DateStyler styler = new DateStyler();
  private Date highlightedDate;
  private MonthSelector monthSelector;
  private CalendarView calendar;
  private CalendarModel model;
  private Date selectedDate;

  /**
   * Constructor.
   */
  public DatePicker() {
    this(new SimpleMonthSelector(), new SimpleCalendarView(),
        new CalendarModel());
  }

  /**
   * Constructor for use by subtypes.
   * 
   * @param monthSelector the month selector
   * @param calendarView the calendar view
   * @param model the calendar model
   */
  protected DatePicker(MonthSelector monthSelector, CalendarView calendarView,
      CalendarModel model) {
    this.setModel(model);
    this.monthSelector = monthSelector;
    monthSelector.setDatePicker(this);
    this.calendar = calendarView;
    calendar.setDatePicker(this);
    calendar.setup();
    monthSelector.setup();
    this.setup();
    showDate(new Date());
    addGlobalDateStyle(new Date(), Styles.TODAY_CELL);
  }

  public final void addChangeHandler(ChangeHandler<Date> handler) {
    handlers.add(ChangeEvent.class, handler);
  }

  /**
   * Globally adds a style name to a date. i.e. the style name is associated
   * with the date each time it is rendered.
   * 
   * @param date date
   * @param styleName style name
   */
  public void addGlobalDateStyle(Date date, String styleName) {
    styler.setStyleName(date, styleName, true);
    if (isDateVisible(date)) {
      calendar.addDateStyle(date, styleName);
    }
  }

  public final void addHighlightHandler(HighlightHandler<Date> handler) {
    handlers.add(HighlightEvent.class, handler);
  }

  public final void addRenderingHandler(RenderingHandler handler) {
    handlers.add(RenderingEvent.class, handler);

    // Render onto the current display.
    handler.onRendered(new RenderingEvent(this));
  }

  /**
   * Shows the given style name on the specified date. This is only set until
   * the next time the DatePicker is refreshed.
   * 
   * @param date date
   * @param styleName style name
   */
  public final void addVisibleDateStyle(Date date, String styleName) {
    calendar.addDateStyle(date, styleName);
  }

  /**
   * Removes a style name from multiple dates.
   * 
   * @param dates dates that will have the supplied style removed
   * @param styleName style name to remove
   */
  public final void addVisibleDateStyles(Iterable<Date> dates, String styleName) {
    getCalendarView().addDateStyles(dates, styleName);
  }

  /**
   * Gets the currently shown date.
   */
  public Date getDateShown() {
    return getModel().getCurrentMonthAndYear();
  }

  /**
   * Gets the global style name associated with a date.
   */
  public String getGlobalDateStyle(Date d) {
    return styler.getStyleName(d);
  }

  /**
   * Gets the highlighted date, if any.
   */
  public final Date getHighlightedDate() {
    return highlightedDate;
  }

  /**
   * Gets the selected date, if any.
   */
  public final Date getSelectedDate() {
    return selectedDate;
  }

  /**
   * Is the date enabled?
   * 
   * @param date
   * @return is the date enabled?
   */
  public boolean isDateEnabled(Date date) {
    return calendar.isDateEnabled(date);
  }

  /**
   * Is the date currently shown in the date picker?
   * 
   * @param date
   * @return is the date currently shown
   */
  public boolean isDateVisible(Date date) {
    return calendar.isVisible(date);
  }

  public final void removeChangeHandler(ChangeHandler<Date> handler) {
    handlers.remove(ChangeEvent.class, handler);
  }

  /**
   * Globally removes a style from a date.
   * 
   * @param date date
   * @param styleName style name
   */
  public void removeGlobalDateStyle(Date date, String styleName) {
    styler.setStyleName(date, styleName, false);
    if (isDateVisible(date)) {
      calendar.removeStyleName(date, styleName);
    }
  }

  public final void removeHighlightHandler(HighlightHandler<Date> handler) {
    handlers.remove(HighlightEvent.class, handler);
  }

  public final void removeRenderingHandler(RenderingHandler handler) {
    handlers.remove(RenderingEvent.class, handler);
  }

  /**
   * Removes a style name from multiple visible dates.
   * 
   * @param dates dates that will have the supplied style removed
   * @param styleName style name to remove
   */
  public final void removeVisibleDateStyles(Iterator<Date> dates,
      String styleName) {
    for (; dates.hasNext();) {
      Date date = (Date) dates.next();
      calendar.removeStyleName(date, styleName);
    }
  }

  /**
   * Selects the current highlighted date.
   */
  public final void selectHighlightedDate() {
    setSelectedDate(getHighlightedDate());
  }

  /**
   * Restricts the date range this picker can show.
   * <p>
   * (Note: not yet implemented for default case).
   * </p>
   */
  public final void setAllowableDates(Date from, Date to) {
    monthSelector.setAllowableDateRange(from, to);
  }

  /**
   * Sets the selected date.
   */
  public final void setSelectedDate(Date date) {
    setSelectedDate(date, true);
  }

  /**
   * Sets the selected date.
   */
  public final void setSelectedDate(Date date, boolean fireEvents) {
    Date old = selectedDate;
    if (fireEvents && handlers.has(ChangeEvent.class)) {
      ChangeEvent event = new ChangeEvent(this, old, date);
      handlers.fire(event);
    }
    if (old != null) {
      removeGlobalDateStyle(old, Styles.SELECTED_CELL);
    }

    selectedDate = CalendarModel.copy(date);
    addGlobalDateStyle(selectedDate, Styles.SELECTED_CELL);
  }

  /**
   * Sets a visible date to be enabled or disabled. This is only set until the
   * next time the DatePicker is refreshed.
   * 
   * @param date the date
   * @param enabled is enabled
   */
  public final void setVisibleDateEnabled(Date date, boolean enabled) {
    getCalendarView().setDateEnabled(date, enabled);
  }

  /**
   * Sets a group of visible dates to be enabled or disabled. This is only set
   * until the next time the DatePicker is refreshed.
   * 
   * @param dates the dates
   * @param enabled is enabled
   */
  public final void setVisibleEnabledDates(Iterable<Date> dates, boolean enabled) {
    getCalendarView().setEnabledDates(dates, enabled);
  }

  /**
   * This method causes the DatePicker to show the given date.
   */
  public final void showDate(Date date) {
    getModel().setCurrentMonthAndYear(date);
    refresh();
  }

  /**
   * Gets the date view currently associated with this date picker.
   * 
   * @return calendar view
   */
  protected final CalendarView getCalendarView() {
    return calendar;
  }

  /**
   * Gets the model associated with this date picker.
   * 
   * @return the model
   */
  protected final CalendarModel getModel() {
    return model;
  }

  /**
   * Gets the month selector associated with this date picker.
   * 
   * @return the month selector
   */
  protected final Widget getMonthSelector() {
    return monthSelector;
  }

  /**
   * Sets up the date picker.
   */
  protected void setup() {
    FlowPanel panel = new FlowPanel();
    initWidget(panel);
    setStyleName(Styles.DEFAULT);
    panel.add(this.getMonthSelector());
    showDate(new Date());
    panel.add(this.getCalendarView());
  }

  final void refresh() {
    highlightedDate = null;
    calendar.refresh();
    monthSelector.refresh();

    if (handlers.has(RenderingEvent.class)) {
      RenderingEvent event = new RenderingEvent((FiresRenderingEvents) this);
      handlers.fire(event);
    }
  }

  /**
   * Sets the highlighted date. Only {@link CalendarView} can set the
   * highlighted date.
   * 
   * @param highlightedDate selected date
   */
  void setHighlightedDate(Date highlightedDate) {
    if (handlers.has(HighlightEvent.class)) {
      HighlightEvent<Date> event = new HighlightEvent<Date>(this,
          highlightedDate);
      handlers.fire(event);
    }
    this.highlightedDate = highlightedDate;
  }

  final void setModel(CalendarModel model) {
    this.model = model;
  }
}
