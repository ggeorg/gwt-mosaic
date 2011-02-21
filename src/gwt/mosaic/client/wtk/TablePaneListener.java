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

import gwt.mosaic.client.collections.Sequence;

/**
 * Table pane listener interface.
 */
public interface TablePaneListener {
    /**
     * Table pane listener adapter.
     */
    public static class Adapter implements TablePaneListener {
        @Override
        public void rowInserted(TableInput tablePane, int index) {
        }

        @Override
        public void rowsRemoved(TableInput tablePane, int index,
            Sequence<TableInput.Row> rows) {
        }

        @Override
        public void rowHeightChanged(TableInput.Row row, int previousHeight,
            boolean previousRelative) {
        }

        @Override
        public void rowHighlightedChanged(TableInput.Row row) {
        }

        @Override
        public void columnInserted(TableInput tablePane, int index) {
        }

        @Override
        public void columnsRemoved(TableInput tablePane, int index,
            Sequence<TableInput.Column> columns) {
        }

        @Override
        public void columnWidthChanged(TableInput.Column column, int previousWidth,
            boolean previousRelative) {
        }

        @Override
        public void columnHighlightedChanged(TableInput.Column column) {
        }

        @Override
        public void cellInserted(TableInput.Row row, int column) {
        }

        @Override
        public void cellsRemoved(TableInput.Row row, int column,
            Sequence<Component> removed) {
        }

        @Override
        public void cellUpdated(TableInput.Row row, int column,
            Component previousComponent) {
        }
    }

    /**
     * Called when a row has been inserted into a table pane.
     *
     * @param tablePane
     * @param index
     */
    public void rowInserted(TableInput tablePane, int index);

    /**
     * Called when rows have been removed from a table pane.
     *
     * @param tablePane
     * @param index
     * @param rows
     */
    public void rowsRemoved(TableInput tablePane, int index,
        Sequence<TableInput.Row> rows);

    /**
     * Called when a row's height has changed.
     *
     * @param row
     * @param previousHeight
     * @param previousRelative
     */
    public void rowHeightChanged(TableInput.Row row, int previousHeight,
        boolean previousRelative);

    /**
     * Called when a row's highlighted state has changed.
     *
     * @param row
     */
    public void rowHighlightedChanged(TableInput.Row row);

    /**
     * Called when a column has been inserted into a table pane.
     *
     * @param tablePane
     * @param index
     */
    public void columnInserted(TableInput tablePane, int index);

    /**
     * Called when column's have been removed from a table pane.
     *
     * @param tablePane
     * @param index
     * @param columns
     */
    public void columnsRemoved(TableInput tablePane, int index,
        Sequence<TableInput.Column> columns);

    /**
     * Called when a column's width has changed.
     *
     * @param column
     * @param previousWidth
     * @param previousRelative
     */
    public void columnWidthChanged(TableInput.Column column, int previousWidth,
        boolean previousRelative);

    /**
     * Called when a column's highlighted state has changed.
     *
     * @param column
     */
    public void columnHighlightedChanged(TableInput.Column column);

    /**
     * Called when a cell has been inserted into a table pane.
     *
     * @param row
     * @param column
     */
    public void cellInserted(TableInput.Row row, int column);

    /**
     * Called when cell's have been removed from a table pane.
     *
     * @param row
     * @param column
     * @param removed
     */
    public void cellsRemoved(TableInput.Row row, int column,
        Sequence<Component> removed);

    /**
     * Called when a cell has been updated in a table pane.
     *
     * @param row
     * @param column
     * @param previousComponent
     */
    public void cellUpdated(TableInput.Row row, int column,
        Component previousComponent);
}
