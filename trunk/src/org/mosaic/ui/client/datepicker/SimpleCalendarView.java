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

import java.util.ArrayList;
import java.util.Date;

import org.mosaic.ui.client.datepicker.DatePicker.Styles;
import org.mosaic.ui.client.datepicker.SimpleCalendarView.CellGrid.Cell;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

/**
 * Simple calendar view.
 * 
 */
public class SimpleCalendarView extends CalendarView<DatePicker> {

  /*
   * note the fully-qualified extends classname above: a bug in JDK1.5 (at least
   * 1.5.0_10 & 12) can't find the symbol "Grid" if unqualified, despite the
   * identical import (on line 25 as a write this, but subject to change).
   * --fabbott, 20mar08
   */
  class CellGrid extends Grid {

    class Cell extends UIObject {
      int index;
      Date date = new Date();
      boolean enabled = true;

      Cell(Element td, int column) {
        index = cellList.size();
        cellList.add(this);
        setElement(td);
        cellMap.put(this);
      }

      public Date getDate() {
        return date;
      }

      public boolean isFiller() {
        return !getModel().isInCurrentMonth(date);
      }

      public void onClick(Event event) {
        if (enabled == false) {
          return;
        }

        getDatePicker().setSelectedDate(date);
        if (isFiller()) {
          getDatePicker().showDate(date);
        }
      }

      public void onHover() {
        if (enabled == false) {
          return;
        }
        updateHighlightedDate(date);
      }

      public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
          removeStyleName(Styles.DISABLED_CELL);
        } else {
          addStyleName(Styles.DISABLED_CELL);
        }
      }

      public void update(Date current) {
        enabled = true;
        this.date.setTime(current.getTime());
        String value = getModel().formatDayOfMonth(date);
        setText(value);
        if (isFiller()) {
          setStyleName(Styles.FILLER_CELL);
        } else {
          setStyleName(Styles.DATE_CELL);
        }
        setStyleName(this.getElement(), Styles.FILLER_CELL, isFiller());
        String extraStyle = getDatePicker().getGlobalDateStyle(date);
        if (extraStyle != null) {
          setStyleName(this.getElement(), extraStyle, true);
        }
      }

      private void setText(String value) {
        DOM.setInnerText(getElement(), value);
      }
    }

    private ElementMapper<Cell> cellMap = new ElementMapper<Cell>();

    private ArrayList<Cell> cellList = new ArrayList<Cell>();

    CellGrid() {
      super(CalendarModel.WEEKS_IN_MONTH + 1, CalendarModel.DAYS_IN_WEEK);
      setCellPadding(0);
      setCellSpacing(0);
      sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

    @Override
    public void onBrowserEvent(Event event) {

      switch (DOM.eventGetType(event)) {
        case Event.ONCLICK: {
          Cell cell = getCell(event);
          if (cell != null) {
            cell.onClick(event);
          }
          break;
        }
        case Event.ONMOUSEOUT: {
          Element e = DOM.eventGetFromElement(event);
          if (e != null) {
            Cell cell = cellMap.get(e);
            if (cell != null) {
              cell.removeStyleName(Styles.HIGHLIGHTED_CELL);
              Element target = DOM.eventGetToElement(event);
              if (target != null && cellMap.get(target) == null) {
                updateHighlightedDate(null);
              }
            }
          }
          break;
        }
        case Event.ONMOUSEOVER: {
          Element e = DOM.eventGetToElement(event);
          if (e != null) {
            Cell cell = cellMap.get(e);
            if (cell != null) {
              cell.addStyleName(Styles.HIGHLIGHTED_CELL);
              cell.onHover();
            }
          }
          break;
        }
      }
    }

    public int size() {
      return cellList.size();
    }

    private Cell getCell(Event e) {
      // Find out which cell was actually clicked.
      Element td = getEventTargetCell(e);
      return td != null ? cellMap.get(td) : null;
    }

    private Cell getCell(int i) {
      return cellList.get(i);
    }
  }

  CellGrid grid = new CellGrid();

  private Date firstDisplayed;

  private Date lastDisplayed = new Date();

  public SimpleCalendarView() {
  }

  @Override
  public void addDateStyle(Date date, String styleName) {
    getCell(date).addStyleName(styleName);
  }

  @Override
  public Date getFirstVisibleDate() {
    return firstDisplayed;
  }

  @Override
  public Date getLastVisibleDate() {
    return lastDisplayed;
  }

  @Override
  public boolean isDateEnabled(Date d) {
    return getCell(d).enabled;
  }

  public void refresh() {
    firstDisplayed = getModel().getFirstDayOfCurrentFirstWeek();

    lastDisplayed.setTime(firstDisplayed.getTime());

    for (int i = 0; i < grid.cellList.size(); i++) {
      if (i != 0) {
        CalendarModel.shiftDays(lastDisplayed, 1);
      }
      Cell cell = grid.cellList.get(i);
      cell.update(lastDisplayed);
    }
  }

  @Override
  public void removeStyleName(Date date, String styleName) {
    getCell(date).removeStyleName(styleName);
  }

  @Override
  public void setDateEnabled(Date date, boolean enabled) {
    getCell(date).setEnabled(enabled);
  }

  @Override
  public void setup() {
    CellFormatter formatter = grid.getCellFormatter();
    // Set up title.
    for (int i = 0; i < CalendarModel.DAYS_IN_WEEK; i++) {
      int shift = CalendarModel.getLocaleStartingDayOfWeek();
      int dayIdx = i + shift < CalendarModel.DAYS_IN_WEEK ? i + shift : i
          + shift - CalendarModel.DAYS_IN_WEEK;
      grid.setText(0, i, getModel().formatDayOfWeek(dayIdx));
      formatter.setStyleName(0, i, Styles.DAY_TITLE);
    }

    for (int row = 1; row <= CalendarModel.WEEKS_IN_MONTH; row++) {
      for (int column = 0; column < CalendarModel.DAYS_IN_WEEK; column++) {
        grid.new Cell(formatter.getElement(row, column), column);
      }
    }
    initWidget(grid);
    setStyleName(Styles.CALENDAR_VIEW);
    ColumnFormatter columnFormatter = grid.getColumnFormatter();
    for (int i = 0; i < 7; i++) {
      int shift = CalendarModel.getLocaleStartingDayOfWeek();
      int dayIdx = i + shift < CalendarModel.DAYS_IN_WEEK ? i + shift : i
          + shift - CalendarModel.DAYS_IN_WEEK;
      if (getModel().isWeekend(dayIdx)) {
        columnFormatter.addStyleName(i, Styles.WEEKEND);
      }
    }
  }

  private Cell getCell(Date d) {
    int index = CalendarModel.diffDays(firstDisplayed, d);

    Cell cell = grid.getCell(index);
    if (cell.date.getDate() != d.getDate()) {
      throw new IllegalStateException(d + " cannot be associated with cell "
          + cell + " as it has date " + cell.date);
    }
    return cell;
  }
}
