/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk;

import gwt.mosaic.client.wtk.TableViewHeader.SortMode;

/**
 * Table view header listener interface.
 */
public interface TableViewHeaderListener {
    /**
     * Table view header listener adapter.
     */
    public static class Adapter implements TableViewHeaderListener {
        @Override
        public void tableViewChanged(TableViewHeader tableViewHeader, TableView previousTableView) {
        }

        @Override
        public void sortModeChanged(TableViewHeader tableViewHeader, SortMode previousSortMode) {
        }
    }

    /**
     * Called when a table view header's table view has changed.
     *
     * @param tableViewHeader
     * @param previousTableView
     */
    public void tableViewChanged(TableViewHeader tableViewHeader, TableView previousTableView);

    /**
     * Called when a table view header's sort mode has changed.
     *
     * @param tableViewHeader
     * @param previousSortMode
     */
    public void sortModeChanged(TableViewHeader tableViewHeader, SortMode previousSortMode);
}
