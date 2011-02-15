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
package org.gwt.mosaic.ui.client.treetable;

import org.gwt.mosaic.ui.client.table.AbstractTableModel;
import org.gwt.mosaic.ui.client.table.TableModelHelper.Request;

/**
 * A tree table version of the {@link TableModel}.
 * 
 * @param <R> the data type of the row values
 */
public abstract class TreeTableModel<R extends TreeTableItem> extends AbstractTableModel<R> {
  @Override
  public void requestRows(Request request, Callback<R> callback) {
    requestTreeItems((TreeRequest)request, callback);
  }

  public abstract void requestTreeItems(TreeRequest request,
      Callback<R> callback);
}