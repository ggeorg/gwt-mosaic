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
package gwt.mosaic.client.wtk.content;

import gwt.mosaic.client.json.JSON;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.TableView;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.style.Color;

/**
 * Default table cell renderer. Renders cell contents as a string.
 */
public class TableViewCellRenderer extends Label
    implements TableView.CellRenderer {
    public TableViewCellRenderer() {
        getStyles().put("verticalAlignment", VerticalAlignment.CENTER);
        getStyles().put("padding", new Insets(2));
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        // Since this component doesn't have a parent, it won't be validated
        // via layout; ensure that it is valid here
        validate();
    }

    @Override
    public void render(Object row, int rowIndex, int columnIndex,
        TableView tableView, String columnName,
        boolean selected, boolean highlighted, boolean disabled) {
        renderStyles(tableView, selected, disabled);

        String text = null;
        if (row != null
            && columnName != null) {
            text = toString(row, columnName);
        }

        setText(text);
    }

    protected void renderStyles(TableView tableView, boolean rowSelected, boolean rowDisabled) {
        Component.StyleDictionary tableViewStyles = tableView.getStyles();
        Component.StyleDictionary styles = getStyles();

        Font font = (Font)tableViewStyles.get("font");
        styles.put("font", font);

        Color color;
        if (tableView.isEnabled() && !rowDisabled) {
            if (rowSelected) {
                if (tableView.isFocused()) {
                    color = (Color)tableViewStyles.get("selectionColor");
                } else {
                    color = (Color)tableViewStyles.get("inactiveSelectionColor");
                }
            } else {
                color = (Color)tableViewStyles.get("color");
            }
        } else {
            color = (Color)tableViewStyles.get("disabledColor");
        }

        styles.put("color", color);
    }

    @Override
    public String toString(Object row, String columnName) {
        Object cellData = JSON.get(row, columnName);
        return (cellData == null) ? null : cellData.toString();
    }
}
