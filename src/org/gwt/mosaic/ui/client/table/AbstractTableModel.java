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

import org.gwt.mosaic.ui.client.event.RowCountChangeEvent;
import org.gwt.mosaic.ui.client.event.RowCountChangeHandler;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * An abstract {@link TableModel} implementation.
 * 
 * @author Derived work from GWT Incubator project
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @param <RowType> the data type of the row values
 */
public abstract class AbstractTableModel<RowType> implements
    TableModel<RowType> {

  /**
   * The manager of events.
   */
  private HandlerManager handlerManager;

  /**
   * The total number of rows available in the model.
   */
  private int rowCount = UNKNOWN_ROW_COUNT;

  public HandlerRegistration addRowCountChangeHandler(
      RowCountChangeHandler handler) {
    return addHandler(handler, RowCountChangeEvent.getType());
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.TableModel#getRowCount()
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.TableModel#setRowCount(int)
   */
  public void setRowCount(int rowCount) {
    if (this.rowCount != rowCount) {
      int oldRowCount = this.rowCount;
      this.rowCount = rowCount;
      fireEvent(new RowCountChangeEvent(oldRowCount, rowCount));
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.table.TableModel#requestRows(org.gwt.mosaic.ui.client.table.TableModelHelper.Request,
   *      org.gwt.mosaic.ui.client.table.TableModel.Callback)
   */
  public abstract void requestRows(Request request, Callback<RowType> callback);

  public void fireEvent(GwtEvent<?> event) {
    if (handlerManager != null) {
      handlerManager.fireEvent(event);
    }
  }

  /**
   * Adds this handler to the widget.
   * 
   * @param key the event key
   * @param handler the handler
   */
  protected final <H extends EventHandler> HandlerRegistration addHandler(
      final H handler, GwtEvent.Type<H> type) {
    return ensureHandlers().addHandler(type, handler);
  }

  /**
   * Ensures the existence of the handler manager.
   * 
   * @return the handler manager
   * */
  HandlerManager ensureHandlers() {
    return handlerManager == null ? handlerManager = new HandlerManager(this)
        : handlerManager;
  }

  /**
   * Returns this widget's {@link HandlerManager} used for event management.
   */
  protected final HandlerManager getHandlerManager() {
    return handlerManager;
  }

  /**
   * Is the event handled by one or more handlers?
   */
  protected final boolean isEventHandled(Type<?> type) {
    return handlerManager.isEventHandled(type);
  }

  /**
   * Gets the number of handlers listening to the event type.
   * 
   * @param type the event type
   * @return the number of registered handlers
   */
  protected int getHandlerCount(Type<?> type) {
    return handlerManager == null ? 0 : handlerManager.getHandlerCount(type);
  }

  /**
   * Removes the given handler from the specified event key. Normally,
   * applications should call {@link HandlerRegistration#removeHandler()}
   * instead. This method is provided primary to support the deprecated
   * listeners api.
   * 
   * @param key the event key
   * @param handler the handler
   */
  protected <H extends EventHandler> void removeHandler(GwtEvent.Type<H> type,
      final H handler) {
    handlerManager.removeHandler(type, handler);
  }
}
