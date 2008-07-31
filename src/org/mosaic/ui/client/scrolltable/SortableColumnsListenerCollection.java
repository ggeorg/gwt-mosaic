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
package org.mosaic.ui.client.scrolltable;

import java.util.ArrayList;

import org.mosaic.ui.client.scrolltable.TableModel.ColumnSortList;

/**
 * A helper class for implementers of the {@link SourceSortableColumnsEvents}
 * interface. This subclass of {@link ArrayList} assumes that all objects added
 * to it will be of type {@link SortableColumnsListener}.
 */
public class SortableColumnsListenerCollection extends
    ArrayList<SortableColumnsListener> {
  private static final long serialVersionUID = -4412778421382569723L;

  /**
   * Fired when the currently sorted column changes.
   * 
   * @param sortList the list of sorted columns
   */
  public void fireColumnSorted(ColumnSortList sortList) {
    for (SortableColumnsListener listener : this) {
      listener.onColumnSorted(sortList);
    }
  }
}
