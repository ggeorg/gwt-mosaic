/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.core.client.CoreConstants;
import org.gwt.mosaic.ui.client.ColumnView.ColumnViewItem;

import com.google.gwt.gen2.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.user.client.Timer;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <T>
 */
@SuppressWarnings("deprecation")
public abstract class LiveTableColumnViewItem<T> extends ColumnViewItem<T>
    implements RowSelectionHandler, HasRowSelectionHandlers {
  private final ColumnView<T> columnView;
  private final LiveTable<T> table;

  private Timer timer;

  public LiveTableColumnViewItem(ColumnView<T> columnView, T data) {
    super(data);
    this.columnView = columnView;
    table = new LiveTable<T>(createTableModel(data),
        createTableDefinition(data));
    table.setResizePolicy(ResizePolicy.FILL_WIDTH);
    table.addRowSelectionHandler(this);
    setWidget(table);
  }

  protected abstract TableModel<T> createTableModel(T data);

  protected abstract TableDefinition<T> createTableDefinition(T data);

  /**
   * @return the columnView
   */
  protected ColumnView<T> getColumnView() {
    return columnView;
  }

  /**
   * @return the table
   */
  @Override
  public final LiveTable<T> getWidget() {
    return table;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.gen2.table.event.client.RowSelectionHandler#onRowSelection(com.google.gwt.gen2.table.event.client.RowSelectionEvent)
   */
  public void onRowSelection(RowSelectionEvent event) {
    if (event.getSelectedRows().size() != 1) {

      columnView.deleteColumn(columnView.indexOf(this) + 1);

    } else {

      if (timer != null) {
        timer.cancel();
      }

      timer = new Timer() {
        public void run() {
          final T data = table.getRowValue(table.getSelectedIndex());
          final LiveTableColumnViewItem<T> childItem = new LiveTableColumnViewItem<T>(
              columnView, data) {
            @Override
            protected TableDefinition<T> createTableDefinition(T data) {
              return LiveTableColumnViewItem.this.createTableDefinitionInternal(data);
            }

            @Override
            protected TableModel<T> createTableModel(T data) {
              return LiveTableColumnViewItem.this.createTableModelInternal(data);
            }
          };

          columnView.addColumn(
              columnView.indexOf(LiveTableColumnViewItem.this) + 1, childItem,
              "20em");
          columnView.invalidate(childItem);
          columnView.layout();
        }
      };

      timer.schedule(CoreConstants.DEFAULT_DELAY_MILLIS);
    }
  }

  private TableModel<T> createTableModelInternal(T data) {
    return createTableModel(data);
  }

  private TableDefinition<T> createTableDefinitionInternal(T data) {
    return createTableDefinition(data);
  }

  public HandlerRegistration addRowSelectionHandler(RowSelectionHandler handler) {
    return getWidget().addRowSelectionHandler(handler);
  }
}
