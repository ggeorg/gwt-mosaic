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
package gwt.mosaic.client.wtk.skin;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.wtk.Bounds;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.GridPane;
import gwt.mosaic.client.wtk.GridPaneListener;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.style.Color;

/**
 * Grid pane skin.
 */
public class GridPaneSkin extends PanelSkin implements GridPane.Skin,
		GridPaneListener {
	/**
	 * Provides metadata about the grid pane that skins use in performing
	 * preferred size calculations and layout.
	 */
	protected final class Metadata {
		public final int visibleRowCount;
		public final int visibleColumnCount;

		private boolean[] visibleRows;
		private boolean[] visibleColumns;

		public Metadata() {
			GridPane gridPane = (GridPane) getComponent();

			GridPane.RowSequence rows = gridPane.getRows();

			int columnCount = gridPane.getColumnCount();
			int rowCount = rows.getLength();

			visibleRows = new boolean[rowCount];
			visibleColumns = new boolean[columnCount];

			int visibleRowCount = 0;
			int visibleColumnCount = 0;

			for (int i = 0; i < rowCount; i++) {
				GridPane.Row row = rows.get(i);

				for (int j = 0, n = row.getLength(); j < n && j < columnCount; j++) {
					Component component = row.get(j);

					if (component != null && component.isVisible()) {
						if (!visibleRows[i]) {
							visibleRowCount++;
							visibleRows[i] = true;
						}

						if (!visibleColumns[j]) {
							visibleColumnCount++;
							visibleColumns[j] = true;
						}
					}
				}
			}

			this.visibleRowCount = visibleRowCount;
			this.visibleColumnCount = visibleColumnCount;
		}

		public boolean isRowVisible(int rowIndex) {
			return visibleRows[rowIndex];
		}

		public boolean isColumnVisible(int columnIndex) {
			return visibleColumns[columnIndex];
		}
	}

	private Insets padding = Insets.NONE;
	private int horizontalSpacing = 0;
	private int verticalSpacing = 0;
	private boolean showHorizontalGridLines = false;
	private boolean showVerticalGridLines = false;
	private Color horizontalGridColor = Color.BLACK;
	private Color verticalGridColor = Color.BLACK;

	private int cellWidth = 0;
	private int cellHeight = 0;

	@Override
	public void install(Component component) {
		super.install(component);

		GridPane gridPane = (GridPane) component;
		gridPane.getGridPaneListeners().add(this);
	}

	@Override
	public int getPreferredWidth(int height) {
		GridPane gridPane = (GridPane) getComponent();

		GridPane.RowSequence rows = gridPane.getRows();

		int columnCount = gridPane.getColumnCount();
		int rowCount = rows.getLength();

		Metadata metadata = new Metadata();

		int cellHeight = getCellHeight(height, metadata);

		int preferredCellWidth = 0;
		for (int i = 0; i < rowCount; i++) {
			GridPane.Row row = rows.get(i);

			for (int j = 0, n = row.getLength(); j < n && j < columnCount; j++) {
				Component component = row.get(j);

				if (component != null && component.isVisible()) {
					preferredCellWidth = Math.max(preferredCellWidth,
							component.getPreferredWidth(cellHeight));
				}
			}
		}

		// The preferred width of the grid pane is the sum of the column
		// widths, plus padding and spacing

		int preferredWidth = padding.left + padding.right
				+ metadata.visibleColumnCount * preferredCellWidth;

		if (metadata.visibleColumnCount > 1) {
			preferredWidth += (metadata.visibleColumnCount - 1)
					* horizontalSpacing;
		}

		return preferredWidth;
	}

	@Override
	public int getPreferredHeight(int width) {
		GridPane gridPane = (GridPane) getComponent();

		GridPane.RowSequence rows = gridPane.getRows();

		int columnCount = gridPane.getColumnCount();
		int rowCount = rows.getLength();

		Metadata metadata = new Metadata();

		int cellWidth = getCellWidth(width, metadata);

		int preferredCellHeight = 0;
		for (int i = 0; i < rowCount; i++) {
			GridPane.Row row = rows.get(i);

			for (int j = 0, n = row.getLength(); j < n && j < columnCount; j++) {
				Component component = row.get(j);

				if (component != null && component.isVisible()) {
					preferredCellHeight = Math.max(preferredCellHeight,
							component.getPreferredHeight(cellWidth));
				}
			}
		}

		// The preferred height of the grid pane is the sum of the row
		// heights, plus padding and spacing

		int preferredHeight = padding.top + padding.bottom
				+ metadata.visibleRowCount * preferredCellHeight;

		if (metadata.visibleRowCount > 1) {
			preferredHeight += (metadata.visibleRowCount - 1) * verticalSpacing;
		}

		return preferredHeight;
	}

	@Override
	public Dimensions getPreferredSize() {
		GridPane gridPane = (GridPane) getComponent();

		GridPane.RowSequence rows = gridPane.getRows();

		int columnCount = gridPane.getColumnCount();
		int rowCount = rows.getLength();

		Metadata metadata = new Metadata();

		// calculate the maximum preferred cellWidth and cellHeight
		int preferredCellHeight = 0;
		int preferredCellWidth = 0;
		for (int i = 0; i < rowCount; i++) {
			GridPane.Row row = rows.get(i);

			for (int j = 0, n = row.getLength(); j < n && j < columnCount; j++) {
				Component component = row.get(j);

				if (component != null && component.isVisible()) {
					Dimensions d = component.getPreferredSize();
					preferredCellHeight = Math.max(preferredCellHeight,
							d.height);
					preferredCellWidth = Math.max(preferredCellWidth, d.width);
				}
			}
		}

		// The preferred width of the grid pane is the sum of the column
		// widths, plus padding and spacing

		int preferredWidth = padding.left + padding.right
				+ metadata.visibleColumnCount * preferredCellWidth;

		if (metadata.visibleColumnCount > 1) {
			preferredWidth += (metadata.visibleColumnCount - 1)
					* horizontalSpacing;
		}

		// The preferred height of the grid pane is the sum of the row
		// heights, plus padding and spacing

		int preferredHeight = padding.top + padding.bottom
				+ metadata.visibleRowCount * preferredCellHeight;

		if (metadata.visibleRowCount > 1) {
			preferredHeight += (metadata.visibleRowCount - 1) * verticalSpacing;
		}

		return new Dimensions(preferredWidth, preferredHeight);
	}

	@Override
	public int getBaseline(int width, int height) {
		GridPane gridPane = (GridPane) getComponent();

		GridPane.RowSequence rows = gridPane.getRows();

		int columnCount = gridPane.getColumnCount();
		int rowCount = rows.getLength();

		Metadata metadata = new Metadata();

		int cellWidth = getCellWidth(width, metadata);
		int cellHeight = getCellHeight(height, metadata);

		// Return the first available baseline by traversing cells top left to
		// bottom right

		int baseline = -1;

		int rowY = padding.top;

		for (int i = 0; i < rowCount && baseline == -1; i++) {
			if (metadata.isRowVisible(i)) {
				GridPane.Row row = rows.get(i);

				for (int j = 0, n = row.getLength(); j < n && j < columnCount
						&& baseline == -1; j++) {
					Component component = row.get(j);

					if (component != null && component.isVisible()) {
						baseline = component.getBaseline(cellWidth, cellHeight);

						if (baseline != -1) {
							baseline += rowY;
						}
					}
				}

				rowY += (cellHeight + verticalSpacing);
			}
		}

		return baseline;
	}

	@Override
	public void layout() {
		super.layout();
		
		GridPane gridPane = (GridPane) getComponent();

		GridPane.RowSequence rows = gridPane.getRows();

		int columnCount = gridPane.getColumnCount();
		int rowCount = rows.getLength();

		int width = getWidth();
		int height = getHeight();

		Metadata metadata = new Metadata();

		cellWidth = getCellWidth(width, metadata);
		cellHeight = getCellHeight(height, metadata);

		int componentY = padding.top;
		for (int i = 0; i < rowCount; i++) {
			if (metadata.isRowVisible(i)) {
				GridPane.Row row = rows.get(i);

				int componentX = padding.left;
				for (int j = 0, n = row.getLength(); j < n && j < columnCount; j++) {
					Component component = row.get(j);

					if (component != null && component.isVisible()) {
						component.setLocation(componentX, componentY);
						component.setSize(cellWidth, cellHeight);
					}

					if (metadata.isColumnVisible(j)) {
						componentX += (cellWidth + horizontalSpacing);
					}
				}

				componentY += (cellHeight + verticalSpacing);
			}
		}
	}

	/**
	 * Gets the cell width given the specified grid pane width and metadata.
	 */
	private int getCellWidth(int width, Metadata metadata) {
		int cellWidth = -1;

		if (width != -1) {
			int clientWidth = width - padding.left - padding.right;

			if (metadata.visibleColumnCount > 1) {
				clientWidth -= (metadata.visibleColumnCount - 1)
						* horizontalSpacing;
			}

			clientWidth = Math.max(0, clientWidth);

			cellWidth = (metadata.visibleColumnCount == 0) ? 0 : clientWidth
					/ metadata.visibleColumnCount;
		}

		return cellWidth;
	}

	/**
	 * Gets the cell height given the specified grid pane height and metadata.
	 */
	private int getCellHeight(int height, Metadata metadata) {
		int cellHeight = -1;

		if (height != -1) {
			int clientHeight = height - padding.top - padding.bottom;

			if (metadata.visibleRowCount > 1) {
				clientHeight -= (metadata.visibleRowCount - 1)
						* verticalSpacing;
			}

			clientHeight = Math.max(0, clientHeight);

			cellHeight = (metadata.visibleRowCount == 0) ? 0 : clientHeight
					/ metadata.visibleRowCount;
		}

		return cellHeight;
	}

	/**
	 * Gets the padding that will be reserved around the grid pane during
	 * layout.
	 */
	public Insets getPadding() {
		return padding;
	}

	/**
	 * Sets the padding that will be reserved around the grid pane during
	 * layout.
	 */
	public void setPadding(Insets padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		this.padding = padding;
		invalidateComponent();
	}

	/**
	 * Sets the padding that will be reserved around the grid pane during
	 * layout.
	 */
	public final void setPadding(int padding) {
		setPadding(new Insets(padding));
	}

	/**
	 * Sets the padding that will be reserved around the grid pane during
	 * layout.
	 */
	public final void setPadding(Dictionary<String, ?> padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		setPadding(new Insets(padding));
	}

	/**
	 * Sets the padding that will be reserved around the grid pane during
	 * layout.
	 */
	public final void setPadding(String padding) {
		if (padding == null) {
			throw new IllegalArgumentException("padding is null.");
		}

		setPadding(Insets.decode(padding));
	}

	/**
	 * Gets the spacing that will be applied in between the grid pane's columns
	 * during layout.
	 */
	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	/**
	 * Sets the spacing that will be applied in between the grid pane's columns
	 * during layout.
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		if (horizontalSpacing < 0) {
			throw new IllegalArgumentException("horizontalSpacing is negative");
		}

		this.horizontalSpacing = horizontalSpacing;
		invalidateComponent();
	}

	/**
	 * Gets the spacing that will be applied in between the grid pane's rows
	 * during layout.
	 */
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	/**
	 * Sets the spacing that will be applied in between the grid pane's rows
	 * during layout.
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		if (verticalSpacing < 0) {
			throw new IllegalArgumentException("verticalSpacing is negative");
		}

		this.verticalSpacing = verticalSpacing;
		invalidateComponent();
	}

	/**
	 * Tells whether or not horizontal grid lines will be painted in between the
	 * grid pane's rows.
	 */
	public boolean getShowHorizontalGridLines() {
		return showHorizontalGridLines;
	}

	/**
	 * Sets whether or not horizontal grid lines will be painted in between the
	 * grid pane's rows.
	 */
	public void setShowHorizontalGridLines(boolean showHorizontalGridLines) {
		this.showHorizontalGridLines = showHorizontalGridLines;
		repaintComponent();
	}

	/**
	 * Tells whether or not vertical grid lines will be painted in between the
	 * grid pane's columns.
	 */
	public boolean getShowVerticalGridLines() {
		return showVerticalGridLines;
	}

	/**
	 * Sets whether or not vertical grid lines will be painted in between the
	 * grid pane's columns.
	 */
	public void setShowVerticalGridLines(boolean showVerticalGridLines) {
		this.showVerticalGridLines = showVerticalGridLines;
		repaintComponent();
	}

	/**
	 * Gets the color used to paint the grid pane's horizontal grid lines.
	 */
	public Color getHorizontalGridColor() {
		return horizontalGridColor;
	}

	/**
	 * Sets the color used to paint the grid pane's horizontal grid lines.
	 */
	public void setHorizontalGridColor(Color horizontalGridColor) {
		if (horizontalGridColor == null) {
			throw new IllegalArgumentException("horizontalGridColor is null.");
		}

		this.horizontalGridColor = horizontalGridColor;

		if (showHorizontalGridLines || showVerticalGridLines) {
			repaintComponent();
		}
	}

	/**
	 * Sets the color used to paint the grid pane's horizontal grid lines.
	 */
	public final void setHorizontalGridColor(String horizontalGridColor) {
		if (horizontalGridColor == null) {
			throw new IllegalArgumentException("horizontalGridColor is null.");
		}

		throw new UnsupportedOperationException();
		// setHorizontalGridColor(GraphicsUtilities.decodeColor(horizontalGridColor));
	}

	/**
	 * Gets the color used to paint the grid pane's vertical grid lines.
	 */
	public Color getVerticalGridColor() {
		return verticalGridColor;
	}

	/**
	 * Sets the color used to paint the grid pane's vertical grid lines.
	 */
	public void setVerticalGridColor(Color verticalGridColor) {
		if (verticalGridColor == null) {
			throw new IllegalArgumentException("verticalGridColor is null.");
		}

		this.verticalGridColor = verticalGridColor;

		if (showHorizontalGridLines || showVerticalGridLines) {
			repaintComponent();
		}
	}

	/**
	 * Sets the color used to paint the grid pane's vertical grid lines.
	 */
	public final void setVerticalGridColor(String verticalGridColor) {
		if (verticalGridColor == null) {
			throw new IllegalArgumentException("verticalGridColor is null.");
		}

		throw new UnsupportedOperationException();
		// setVerticalGridColor(GraphicsUtilities.decodeColor(verticalGridColor));
	}

	// GridPane.Skin methods

	@Override
	public int getRowAt(int y) {
		GridPane gridPane = (GridPane) getComponent();
		GridPane.RowSequence rows = gridPane.getRows();

		int rowCount = rows.getLength();

		int rowIndex = -1;
		int rowY = padding.top;

		for (int i = 0; rowY <= y && i < rowCount; i++) {

			if (y < rowY + cellHeight) {
				rowIndex = i;
				break;
			}

			rowY += cellHeight + verticalSpacing;
		}

		return rowIndex;
	}

	@Override
	public Bounds getRowBounds(int row) {
		GridPane gridPane = (GridPane) getComponent();
		GridPane.RowSequence rows = gridPane.getRows();

		int rowCount = rows.getLength();

		if (row < 0 || row >= rowCount) {
			throw new IndexOutOfBoundsException(String.valueOf(row));
		}

		int rowY = padding.top;

		for (int i = 0; i < row; i++) {
			rowY += cellHeight + verticalSpacing;
		}

		return new Bounds(0, rowY, getWidth(), cellHeight);
	}

	@Override
	public int getColumnAt(int x) {
		GridPane gridPane = (GridPane) getComponent();

		int columnCount = gridPane.getColumnCount();
		int columnIndex = -1;

		for (int j = 0, columnX = padding.left; columnX <= x && j < columnCount; j++) {

			if (x < columnX + cellWidth) {
				columnIndex = j;
				break;
			}

			columnX += cellWidth + horizontalSpacing;
		}

		return columnIndex;
	}

	@Override
	public Bounds getColumnBounds(int column) {
		GridPane gridPane = (GridPane) getComponent();
		int columnCount = gridPane.getColumnCount();

		if (column < 0 || column >= columnCount) {
			throw new IndexOutOfBoundsException(String.valueOf(column));
		}

		int columnX = padding.left;

		for (int j = 0; j < column; j++) {
			columnX += cellWidth + horizontalSpacing;
		}

		return new Bounds(columnX, 0, cellWidth, getHeight());
	}

	// GridPaneListener methods
	@Override
	public void columnCountChanged(GridPane gridPane, int previousColumnCount) {
		invalidateComponent();
	}

	@Override
	public void rowInserted(GridPane gridPane, int index) {
		invalidateComponent();
	}

	@Override
	public void rowsRemoved(GridPane gridPane, int index,
			Sequence<GridPane.Row> rows) {
		invalidateComponent();
	}

	@Override
	public void cellInserted(GridPane.Row row, int column) {
		invalidateComponent();
	}

	@Override
	public void cellsRemoved(GridPane.Row row, int column,
			Sequence<Component> removed) {
		invalidateComponent();
	}

	@Override
	public void cellUpdated(GridPane.Row row, int column,
			Component previousComponent) {
		invalidateComponent();
	}

}
