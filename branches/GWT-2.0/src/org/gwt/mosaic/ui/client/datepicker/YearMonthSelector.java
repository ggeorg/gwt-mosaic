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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.MonthSelector;

/**
 * A simple {@link MonthSelector} used for the default date picker. Not
 * extensible as we wish to evolve it freely over time.
 */
public class YearMonthSelector extends MonthSelector {

  private PushButton backwards;
  private PushButton forwards;
  private PushButton backwardsYear;
  private PushButton forwardsYear;
  private Grid grid;

  /**
   * Constructor.
   */
  public YearMonthSelector() {
  }
 
  @Override
  protected void refresh() {
    String formattedMonth = getModel().formatCurrentMonth();
    grid.setText(0, 2, formattedMonth);
  }

  @Override
  protected void setup() {
    // Set up backwards.
    backwards = new PushButton();
    backwards.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addMonths(-1);
      }
    });

    backwards.getUpFace().setHTML("<");
    backwards.setStyleName("datePickerPreviousButton");

    // Set up backwardsYear.
    backwardsYear = new PushButton();

    backwardsYear.getUpFace().setHTML("&laquo;");
    backwardsYear.setStyleName("datePickerPreviousButton");
    backwardsYear.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addMonths(-12);
      }
    });

    forwards = new PushButton();
    forwards.getUpFace().setHTML(">");
    forwards.setStyleName("datePickerNextButton");
    forwards.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
          addMonths(+1);
      }
    });

    forwardsYear = new PushButton();
    forwardsYear.getUpFace().setHTML("&raquo;");
    forwardsYear.setStyleName("datePickerNextButton");
    forwardsYear.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addMonths(+12);
      }
    });

    // Set up grid.
    grid = new Grid(1, 5);
    grid.setWidget(0, 0, backwardsYear);
    grid.setWidget(0, 1, backwards);
    grid.setWidget(0, 3, forwards);
    grid.setWidget(0, 4, forwardsYear);

    CellFormatter formatter = grid.getCellFormatter();
    formatter.setStyleName(0, 2, "datePickerMonth");
    formatter.setWidth(0, 0, "1");
    formatter.setWidth(0, 1, "1");
    formatter.setWidth(0, 2, "100%");
    formatter.setWidth(0, 3, "1");
    formatter.setWidth(0, 4, "1");
    grid.setStyleName("datePickerMonthSelector");
    initWidget(grid);
  }

}