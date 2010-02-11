/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos
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
/*
 * This is derived work from GWT Incubator project:
 * http://code.google.com/p/google-web-toolkit-incubator/
 * 
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
package org.gwt.mosaic.ui.client.table;

import java.util.Date;

import org.gwt.mosaic.ui.client.table.TableDefinition.AbstractCellView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public abstract class DateColumnDefinition<RowType> extends
    AbstractColumnDefinition<RowType, Date> {
  /**
   * The {@link DateCellRenderer} used by the {@link DateColumnDefinition} when
   * the user does not specify one.
   */
  class DateCellRenderer implements CellRenderer<RowType, Date> {
    private final DateTimeFormat dateTimeFormat;

    public DateCellRenderer(DateTimeFormat dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
    }

    public void renderRowValue(RowType rowValue,
        ColumnDefinition<RowType, Date> columnDef,
        AbstractCellView<RowType> view) {
      Date cellValue = columnDef.getCellValue(rowValue);
      if (cellValue == null) {
        view.setHTML("&nbsp;");
      } else {
        if (dateTimeFormat != null) {
          view.setHTML(dateTimeFormat.format(cellValue));
        } else {
          view.setHTML(cellValue.toString());
        }
      }
    }
  }

  /**
   * An {@link InlineCellEditor} that can be used to edit {@link Date Dates}.
   */
  class DateCellEditor extends SimpleInlineCellEditor<Date> {
    /**
     * The date box used in this editor.
     */
    private DateBox dateBox;

    /**
     * Construct a new {@link DateCellEditor} using a {@link DateBox}.
     * 
     * @param dateTimeFormat the {@link DateTimeFormat} used for inline editing
     */
    public DateCellEditor(DateTimeFormat dateTimeFormat) {
      this(new DateBox(), dateTimeFormat);
    }

    /**
     * Construct a new {@link DateCellEditor} using a {@link DateBox}.
     * 
     * @param dateBox the {@link DateBox} used for inline editing
     * @param dateTimeFormat the {@link DateTimeFormat} used for inline editing
     */
    public DateCellEditor(DateBox dateBox, DateTimeFormat dateTimeFormat) {
      super(dateBox);
      
      this.dateBox = dateBox;
      this.dateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
      
      this.dateBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(KeyUpEvent event) {
          int keyCode = event.getNativeKeyCode();
          switch (keyCode) {
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
              DateCellEditor.this.accept();
              break;
            case KeyCodes.KEY_ESCAPE:
              DateCellEditor.this.cancel();
              break;
          }
        }
      });
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, Date cellValue,
        Callback<Date> callback) {
      super.editCell(cellEditInfo, cellValue, callback);
      dateBox.setFocus(true);
    }

    /**
     * @return the date box used in the editor
     */
    protected DateBox getDateBox() {
      return dateBox;
    }

    @Override
    protected Date getValue() {
      return dateBox.getDatePicker().getValue();
    }

    @Override
    protected void setValue(Date cellValue) {
      if (cellValue == null) {
        cellValue = new Date();
      }
      dateBox.getDatePicker().setValue(cellValue);
    }
  }

  /**
   * Resources used.
   */
  public interface DateColumnStyle extends ClientBundle {
  }

  public interface DateColumnMessages extends Messages {
    @DefaultMessage("Shows only dates that are equal")
    String equalTooltip();

    @DefaultMessage("Shows only dates that not equal")
    String unequalTooltip();

    @DefaultMessage("Show only dates before the given date")
    String beforeTooltip();

    @DefaultMessage("Show only dates after the given date")
    String afterTooltip();

    @DefaultMessage("Show only dates between the given dates")
    String betweenTooltip();
  }

  public interface DateColumnResources {
    DateColumnStyle getStyle();

    DateColumnMessages getMessages();
  }

  protected static class DefaultDateColumnResources implements
      DateColumnResources {
    private DateColumnStyle style;
    private DateColumnMessages constants;

    public DateColumnStyle getStyle() {
      if (style == null) {
        style = ((DateColumnStyle) GWT.create(DateColumnStyle.class));
      }
      return style;
    }

    public DateColumnMessages getMessages() {
      if (constants == null) {
        constants = ((DateColumnMessages) GWT.create(DateColumnMessages.class));
      }
      return constants;
    }
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean editingEnabled) {
    this(header, dateTimeFormat, sortingEnabled, editingEnabled,
        new DefaultDateColumnResources());
  }

  public DateColumnDefinition(String header, DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean editingEnabled,
      DateColumnResources resources) {
    this(dateTimeFormat, sortingEnabled, editingEnabled, resources);
    setHeader(0, header);
  }

  /**
   * Column definition used for columns containing {@link Date} objects
   * 
   * @param dateTimeFormat
   * @param sortingEnabled
   * @param editingEnabled
   */
  public DateColumnDefinition(DateTimeFormat dateTimeFormat,
      boolean sortingEnabled, boolean editingEnabled,
      DateColumnResources resources) {
    setColumnSortable(sortingEnabled);
    setCellRenderer(createDateCellRenderer(dateTimeFormat));
    if (editingEnabled) {
      setCellEditor(createDateCellEditor(dateTimeFormat));
    }
  }

  /**
   * Creates the default date renderer implementation. Override this method to
   * provide custom date renderer
   * 
   * @return the created cell renderer suitable for rendering dates
   */
  protected CellRenderer<RowType, Date> createDateCellRenderer(
      DateTimeFormat dateTimeFormat) {
    return new DateCellRenderer(dateTimeFormat);
  }

  /**
   * Creates the default date editor implementation. Override this method to
   * provide custom date editor
   * 
   * @return the created cell editor suitable for editing dates
   */
  protected CellEditor<Date> createDateCellEditor(DateTimeFormat dateTimeFormat) {
    return new DateCellEditor(dateTimeFormat);
  }
}