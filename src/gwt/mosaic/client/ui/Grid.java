package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Container that arranges widgets in a two-dimensional grid, optionally
 * spanning multiple rows and columns, much like an HTML <tt>&lt;table&gt;</tt>
 * element.
 * <p>
 * Note that unlike a HTML <tt>&lt;table&gt;</tt>, widgets that span multiple
 * rows or columns will not "push" other widgets out of their way. Instead, the
 * spanning widgets will simply overlay the cells into which they span. This
 * means that application developers may have to use filler cells in the cells
 * that are spanned.
 */
public class Grid extends LayoutPanel {

	/**
	 * Represents a table row.
	 */
	public static class Row extends Layer {
		public Row() {
			Element elem = getElement();
			DOM.setStyleAttribute(elem, "margin", "0px");
			DOM.setStyleAttribute(elem, "border", "0px none");
			DOM.setStyleAttribute(elem, "padding", "0px");

			// !!! Very important in order to support rowSpan
			DOM.setStyleAttribute(elem, "overflow", "visible");
		}

		@Override
		protected void setWidgetPositionImpl(Widget w, int left, int top) {
			Element h = w.getElement();
			// if (left == -1 && top == -1) {
			// changeToStaticPositioning(h);
			// } else {
			DOM.setStyleAttribute(h, "position", "absolute");
			DOM.setStyleAttribute(h, "left", left + "px");
			DOM.setStyleAttribute(h, "top", top + "px");
			// }
		}

		public void setPreferredHeight(String preferredHeight) {
			WidgetHelper.setPreferredHeight(this, preferredHeight);
		}

		public int getWeight() {
			return WidgetHelper.getWeight(this);
		}

		public void setWeight(int weight) {
			WidgetHelper.setWeight(this, weight);
		}

		public boolean isRelative() {
			return WidgetHelper.getWeight(this) > 0;
		}
	}

	public static class Column extends Widget {
		private boolean highlighted;

		public Column() {
			setElement(DOM.createDiv());
		}

		public void setPreferredWidth(String preferredWidth) {
			WidgetHelper.setPreferredWidth(this, preferredWidth);
		}

		public int getWeight() {
			return WidgetHelper.getWeight(this);
		}

		public void setWeight(int weight) {
			WidgetHelper.setWeight(this, weight);
		}

		public boolean isRelative() {
			return WidgetHelper.getWeight(this) > 0;
		}

		public boolean isHighlighted() {
			return highlighted;
		}

		public void setHighlighted(boolean highlighted) {
			this.highlighted = highlighted;
		}
	}

	private final ArrayList<Grid.Row> rows = new ArrayList<Grid.Row>();
	private final ArrayList<Grid.Column> columns = new ArrayList<Grid.Column>();

	// skin --------------------------------------------------------------------

	private int horizontalSpacing = 0;
	private int verticalSpacing = 0;

	// -------------------------------------------------------------------------

	private transient int[] columnWidths = null;
	private transient int[] rowHeights = null;

	public Grid() {
		// do nothing
	}

	/**
	 * Gets the widget at the specified cell in this grid pane.
	 * 
	 * @param rowIndex
	 *            The row index of the cell
	 * @param columnIndex
	 *            The column index of the cell
	 * @return The widget in the specified cell, or <tt>null</tt> if the cell is
	 *         empty
	 */
	public Widget getCellWidget(int rowIndex, int columnIndex) {
		Grid.Row row = (Grid.Row) getWidget(rowIndex);

		Widget widget = null;

		if (row.getWidgetCount() > columnIndex) {
			widget = row.getWidget(columnIndex);
		}

		return widget;
	}

	@Override
	public void add(Widget w) {
		if (!(w instanceof Grid.Row || w instanceof Grid.Column)) {
			throw new IllegalArgumentException(
					"widget is not a Table.Row or Table.Column");
		}
		if (w instanceof Grid.Column) {
			w.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			columns.add((Grid.Column) w);
		} else {
			rows.add((Grid.Row) w);
		}
		super.add(w);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		throw new UnsupportedOperationException(
				"use Table.add(Widget w) instead");
	}

	@Override
	public int getPreferredWidth(int clientHeight) {

		if (clientHeight == -1) {
			// First, check if preferredWidth is cached
			if (isValid() && (preferredWidth != -1)) {
				return preferredWidth;
			}

			// then check if there is a fixed preferredWidth hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredWidth() != null) {
				return WidgetHelper.getPreferredWidthImpl(this, clientHeight);
			}
		}

		int rowCount = rows.size();
		int columnCount = columns.size();

		int[] columnWidths = new int[columnCount];
		int[] relativeWeights = new int[columnCount];
		boolean[] defaultWidthColumns = new boolean[columnCount];

		int totalRelativeWeight = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		// First, we calculate the base widths of the columns, giving relative
		// columns their preferred width

		for (int i = 0; i < columnCount; i++) {
			Grid.Column column = columns.get(i);
			boolean isRelative = column.isRelative();

			LayoutData layoutData = WidgetHelper.getLayoutData(column);
			String widthHint = layoutData.getPreferredWidth();

			defaultWidthColumns[i] = (widthHint == null);

			if (isRelative) {
				relativeWeights[i] = column.getWeight();
				totalRelativeWeight += relativeWeights[i];
			}

			int columnWidth;

			if (widthHint == null || isRelative) {
				columnWidth = getPreferredColumnWidth(i);
			} else {
				columnWidth = WidgetHelper.getPreferredWidth(column, -1);
			}

			columnWidths[i] = columnWidth;
		}

		// Next, we adjust the widths of the relative columns upwards where
		// necessary to reconcile their widths relative to one another while
		// ensuring that they still get at least their preferred width

		if (totalRelativeWeight > 0) {
			int totalRelativeWidth = 0;

			// Calculate the total relative width after the required upward
			// adjustments

			for (int i = 0; i < columnCount; i++) {
				int columnWidth = columnWidths[i];
				int relativeWeight = relativeWeights[i];

				if (relativeWeight > 0) {
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					totalRelativeWidth = Math.max(totalRelativeWidth,
							(int) (columnWidth / weightPercentage));
				}
			}

			// Perform the upward adjustments using the total relative width

			for (int i = 0; i < columnCount; i++) {
				int relativeWeight = relativeWeights[i];

				if (relativeWeight > 0) {
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					columnWidths[i] = (int) (weightPercentage * totalRelativeWidth);
				}
			}
		}

		// Finally, we account for spanning cells, which have been ignored thus
		// far. If any spanned cell is default-width (including relative width
		// columns), then we ensure that the sum of the widths of the spanned
		// cells is enough to satisfy the preferred width of the spanning
		// content

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					int columnSpan = WidgetHelper.getColumnSpan(widget);

					if (columnSpan > 1) {
						// We might need to adjust column widths to accommodate
						// this spanning cell. First, we find out if any of the
						// spanned cells are default width and how much space
						// we've allocated thus far for those cells

						int spannedDefaultWidthCellCount = 0;
						int spannedRelativeWeight = 0;
						int spannedWidth = 0;

						for (int k = 0; k < columnSpan && j + k < columnCount; k++) {
							if (defaultWidthColumns[j + k]) {
								spannedDefaultWidthCellCount++;
							}

							spannedRelativeWeight += relativeWeights[j + k];
							spannedWidth += columnWidths[j + k];
						}

						if (spannedRelativeWeight > 0
								|| spannedDefaultWidthCellCount > 0) {
							int rowHeight = row.isRelative() ? -1
									: WidgetHelper.getPreferredHeight(row, -1);
							int widgetPreferredWidth = WidgetHelper
									.getPreferredWidth(widget, rowHeight);

							if (widgetPreferredWidth > spannedWidth) {
								// The widget's preferred width is larger than
								// the width we've allocated thus far, so an
								// adjustment is necessary
								int adjustment = widgetPreferredWidth
										- spannedWidth;

								if (spannedRelativeWeight > 0) {
									// We'll distribute the adjustment across
									// the spanned relative columns and adjust
									// other relative column widths to keep all
									// relative column widths reconciled
									float unitAdjustment = adjustment
											/ (float) spannedRelativeWeight;

									for (int k = 0; k < columnCount; k++) {
										int relativeWeight = relativeWeights[k];

										if (relativeWeight > 0) {
											int columnAdjustment = Math
													.round(unitAdjustment
															* relativeWeight);

											columnWidths[k] += columnAdjustment;
										}
									}
								} else {
									// We'll distribute the adjustment evenly
									// among the default-width columns
									for (int k = 0; k < columnSpan
											&& j + k < columnCount; k++) {
										if (defaultWidthColumns[j + k]) {
											int columnAdjustment = adjustment
													/ spannedDefaultWidthCellCount;

											columnWidths[j + k] += columnAdjustment;

											// Adjust these to avoid rounding
											// errors
											adjustment -= columnAdjustment;
											spannedDefaultWidthCellCount--;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// The preferred width of the table is the sum of the column widths,
		// plus margin+border+padding and spacing

		boolean[][] occupiedCells = getOccupiedCells();
		int visibleColumnCount = 0;

		int preferredWidth = boxModel.getWidthContribution();

		for (int j = 0; j < columnCount; j++) {
			boolean columnVisible = false;

			for (int i = 0; i < rowCount; i++) {
				if (occupiedCells[i][j]) {
					columnVisible = true;
					break;
				}
			}

			if (columnVisible) {
				preferredWidth += columnWidths[j];
				visibleColumnCount++;
			}
		}

		if (visibleColumnCount > 1) {
			preferredWidth += (visibleColumnCount - 1) * horizontalSpacing;
		}

		return (this.preferredWidth = preferredWidth);
	}

	@Override
	public int getPreferredHeight(int clientWidth) {

		if (clientWidth == -1) {
			// First, check if preferredHeight is cached
			if ((clientWidth == -1) && isValid() && (preferredHeight != -1)) {
				return preferredHeight;
			}

			// then check if there is a fixed preferredHeight hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredHeight() != null) {
				return WidgetHelper.getPreferredHeightImpl(this, clientWidth);
			}
		}

		int rowCount = rows.size();
		int columnCount = columns.size();

		int[] rowHeights = new int[rowCount];
		int[] relativeWeights = new int[rowCount];
		boolean[] defaultHeightRows = new boolean[rowCount];

		int totalRelativeWeight = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		if (clientWidth < 0) {
			clientWidth = getPreferredWidth(-1);
		}

		int[] columnWidths = getColumnWidths(clientWidth);

		// First, we calculate the base heights of the rows, giving relative
		// rows their preferred height

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);
			boolean isRelative = row.isRelative();

			LayoutData layoutData = WidgetHelper.getLayoutData(row);
			String heightHint = layoutData.getPreferredHeight();

			defaultHeightRows[i] = (heightHint == null);

			if (isRelative) {
				relativeWeights[i] = row.getWeight();
				totalRelativeWeight += relativeWeights[i];
			}

			int rowHeight;

			if (layoutData.getPreferredHeight() == null || isRelative) {
				rowHeight = getPreferredRowHeight(i, columnWidths);
			} else {
				rowHeight = WidgetHelper.getPreferredHeight(row, -1);
			}

			rowHeights[i] = rowHeight;
		}

		// Next, we adjust the heights of the relative rows upwards where
		// necessary to reconcile their heights relative to one another while
		// ensuring that they still get at least their preferred height

		if (totalRelativeWeight > 0) {
			int totalRelativeHeight = 0;

			// Calculate the total relative height after the required upward
			// adjustments

			for (int i = 0; i < rowCount; i++) {
				int rowHeight = rowHeights[i];
				int relativeWeight = relativeWeights[i];

				if (relativeWeight > 0) {
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					totalRelativeHeight = Math.max(totalRelativeHeight,
							(int) (rowHeight / weightPercentage));
				}
			}

			// Perform the upward adjustments using the total relative height

			for (int i = 0; i < rowCount; i++) {
				int relativeWeight = relativeWeights[i];

				if (relativeWeight > 0) {
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					rowHeights[i] = (int) (weightPercentage * totalRelativeHeight);
				}
			}
		}

		// Finally, we account for spanning cells, which have been ignored thus
		// far. If any spanned cell is default-height (including relative height
		// rows), then we ensure that the sum of the heights of the spanned
		// cells is enough to satisfy the preferred height of the spanning
		// content

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					int rowSpan = WidgetHelper.getRowSpan(widget);

					if (rowSpan > 1) {
						// We might need to adjust row heights to accomodate
						// this spanning cell. First, we find out if any of the
						// spanned cells are default height and how much space
						// we've allocated thus far for those cells

						int spannedDefaultHeightCellCount = 0;
						int spannedRelativeWeight = 0;
						int spannedHeight = 0;

						for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
							if (defaultHeightRows[i + k]) {
								spannedDefaultHeightCellCount++;
							}

							spannedRelativeWeight += relativeWeights[i + k];
							spannedHeight += rowHeights[i + k];
						}

						if (spannedRelativeWeight > 0
								|| spannedDefaultHeightCellCount > 0) {
							int widgetPreferredHeight = WidgetHelper
									.getPreferredHeight(widget, columnWidths[j]);

							if (widgetPreferredHeight > spannedHeight) {
								// The widget's preferred height is larger
								// than the height we've allocated thus far, so
								// an adjustment is necessary
								int adjustment = widgetPreferredHeight
										- spannedHeight;

								if (spannedRelativeWeight > 0) {
									// We'll distribute the adjustment across
									// the spanned relative rows and adjust
									// other relative row heights to keep all
									// relative row heights reconciled
									float unitAdjustment = adjustment
											/ (float) spannedRelativeWeight;

									for (int k = 0; k < rowCount; k++) {
										int relativeWeight = relativeWeights[k];

										if (relativeWeight > 0) {
											int rowAdjustment = Math
													.round(unitAdjustment
															* relativeWeight);

											rowHeights[k] += rowAdjustment;
										}
									}
								} else {
									// We'll distribute the adjustment evenly
									// among the default-height rows
									for (int k = 0; k < rowSpan
											&& i + k < rowCount; k++) {
										if (defaultHeightRows[i + k]) {
											int rowAdjustment = adjustment
													/ spannedDefaultHeightCellCount;

											rowHeights[i + k] += rowAdjustment;

											// Adjust these to avoid rounding
											// errors
											adjustment -= rowAdjustment;
											spannedDefaultHeightCellCount--;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// The preferred height of the table pane is the sum of the row
		// heights, plus padding and spacing

		boolean[][] occupiedCells = getOccupiedCells();
		int visibleRowCount = 0;

		int preferredHeight = boxModel.getHeightContribution();

		for (int i = 0; i < rowCount; i++) {
			boolean rowVisible = false;

			for (int j = 0; j < columnCount; j++) {
				if (occupiedCells[i][j]) {
					rowVisible = true;
					break;
				}
			}

			if (rowVisible) {
				preferredHeight += rowHeights[i];
				visibleRowCount++;
			}
		}

		if (visibleRowCount > 1) {
			preferredHeight += (visibleRowCount - 1) * verticalSpacing;
		}

		return (this.preferredHeight = preferredHeight);
	}

	@Override
	public Dimensions getPreferredSize() {

		// Check if preferred size is cached
		if (isValid() && (preferredSize != null)) {
			return preferredSize;
		}

		// TODO Optimize by performing calculations here
		int preferredWidth = getPreferredWidth(-1);
		int preferredHeight = getPreferredHeight(preferredWidth);
		return (this.preferredSize = new Dimensions(preferredWidth,
				preferredHeight));
	}

	@Override
	public int getBaseline(int width, int height) {
		int rowCount = rows.size();
		int columnCount = columns.size();

		int[] columnWidths = getColumnWidths(width);
		int[] rowHeights = getRowHeights(height, columnWidths);
		boolean[][] occupiedCells = getOccupiedCells();

		int baseline = -1;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int rowY = boxModel.getPadding().top; // XXX

		for (int i = 0; i < rowCount && baseline == -1; i++) {
			Grid.Row row = rows.get(i);
			boolean rowVisible = false;

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount
					&& baseline == -1; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					int columnSpan = Math
							.min(WidgetHelper.getColumnSpan(widget),
									columnCount - j);
					int widgetWidth = (columnSpan - 1) * horizontalSpacing;
					for (int k = 0; k < columnSpan && j + k < columnCount; k++) {
						widgetWidth += columnWidths[j + k];
					}

					int rowSpan = Math.min(WidgetHelper.getRowSpan(widget),
							rowCount - i);
					int widgetHeight = (rowSpan - 1) * verticalSpacing;
					for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
						widgetHeight += rowHeights[i + k];
					}

					baseline = WidgetHelper
							.getBaseline(widget, Math.max(widgetWidth, 0),
									Math.max(widgetHeight, 0));

					if (baseline != -1) {
						baseline += rowY;
					}
				}

				rowVisible |= occupiedCells[i][j];
			}

			if (rowVisible) {
				rowY += (rowHeights[i] + verticalSpacing);
			}
		}

		return baseline;
	}

	@Override
	protected void doLayout() {
		int rowCount = rows.size();
		int columnCount = columns.size();

		int width = getElement().getClientWidth();
		int height = getElement().getClientHeight();

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		// NOTE We cache column widths and row heights to make getColumnAt()
		// and getRowAt() more efficient
		columnWidths = getColumnWidths(width);
		rowHeights = getRowHeights(height, columnWidths);

		// Determine which rows and column should be visible so we know which
		// ones should be collapsed
		boolean[] visibleRows = new boolean[rowCount];
		boolean[] visibleColumns = new boolean[columnCount];

		boolean[][] occupiedCells = getOccupiedCells();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				if (occupiedCells[i][j]) {
					visibleRows[i] = true;
					visibleColumns[j] = true;
				}
			}
		}

		int widgetY = boxModel.getPadding().top;
		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			int widgetX = boxModel.getPadding().left;

			WidgetHelper.setLocation(row, 0, 0);
			WidgetHelper.setSize(row, width, 1);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget child = row.getWidget(j);

				if (child != null && child.isVisible()) {
					WidgetHelper.setLocation(child, widgetX, widgetY);

					int columnSpan = WidgetHelper.getColumnSpan(child);
					columnSpan = Math.min(columnSpan, columnCount - j);
					int childWidth = (columnSpan - 1) * horizontalSpacing;
					for (int k = 0; k < columnSpan && j + k < columnCount; k++) {
						childWidth += columnWidths[j + k];
					}

					int rowSpan = WidgetHelper.getRowSpan(child);
					rowSpan = Math.min(rowSpan, rowCount - i);
					int childHeight = (rowSpan - 1) * verticalSpacing;
					for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
						childHeight += rowHeights[i + k];
					}

					// Set the component's size
					WidgetHelper.setSize(child, Math.max(childWidth, 0),
							Math.max(childHeight, 0));
				}

				if (visibleColumns[j]) {
					widgetX += (columnWidths[j] + horizontalSpacing);
				}
			}

			if (visibleRows[i]) {
				widgetY += (rowHeights[i] + verticalSpacing);
			}
		}
	}

	/**
	 * Returns a grid indicating which cells are occupied. A widget is said to
	 * occupy a cell if it is visible and either lives in the cell directly or
	 * spans the cell. Conversely, vacant cells do not have visible widgets
	 * within them or spanning them.
	 * 
	 * @return A grid of booleans, where occupied cells are denoted by
	 *         <tt>true</tt>, and vacant cells are denoted by <tt>false</tt>
	 */
	private boolean[][] getOccupiedCells() {
		int rowCount = rows.size();
		int columnCount = columns.size();

		boolean[][] occupiedCells = new boolean[rowCount][columnCount];

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {

					int rowSpan = WidgetHelper.getRowSpan(widget);
					int columnSpan = WidgetHelper.getColumnSpan(widget);

					for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
						for (int l = 0; l < columnSpan && j + l < columnCount; l++) {
							occupiedCells[i + k][j + l] = true;
						}
					}
				}
			}
		}

		return occupiedCells;
	}

	/**
	 * Gets the preferred width of a table column, which is defined as the
	 * maximum preferred width of the column's visible widgets.
	 * <p>
	 * Because their preferred width relates to the preferred widths of other
	 * columns, widgets that span multiple columns will not considered in this
	 * calculation (even if they live in the column directly). It is up to the
	 * caller to factor such widgets into the column widths calculation.
	 * 
	 * @param columnIndex
	 *            The index of the column whose preferred width we're
	 *            calculating
	 * @return The width of a table column defined as the maximum preferred
	 *         width of the column's visible widgets
	 */
	private int getPreferredColumnWidth(int columnIndex) {
		int preferredWidth = 0;

		for (int i = 0, n = rows.size(); i < n; i++) {
			Grid.Row row = rows.get(i);

			if (row.getWidgetCount() > columnIndex) {
				Widget widget = row.getWidget(columnIndex);

				if (widget != null && widget.isVisible()
						&& WidgetHelper.getColumnSpan(widget) == 1) {
					preferredWidth = Math.max(preferredWidth,
							WidgetHelper.getPreferredWidth(widget, -1));
				}
			}
		}

		return preferredWidth;
	}

	/**
	 * Tells whether or not the specified column is visible. A column is visible
	 * if and only if one or more visible components occupies it. A component is
	 * said to occupy a cell if it either lives in the cell directly or spans
	 * the cell.
	 * 
	 * @param columnIndex
	 *            The index of the column within the table pane
	 * 
	 * @return <tt>true</tt> if the column is visible; <tt>false</tt> otherwise
	 */
	private boolean isColumnVisible(int columnIndex) {
		boolean visible = false;

		boolean[][] occupiedCells = getOccupiedCells();

		for (int i = 0; i < occupiedCells.length; i++) {
			if (occupiedCells[i][columnIndex]) {
				visible = true;
				break;
			}
		}

		return visible;
	}

	/**
	 * Gets the preferred height of a table pane row, which is defined as the
	 * maximum preferred height of the row's visible components. The preferred
	 * height of each constituent component will be constrained by the width of
	 * the column that the component occupies (as specified in the array of
	 * column widths).
	 * <p>
	 * Because their preferred height relates to the preferred heights of other
	 * rows, components that span multiple rows will not be considered in this
	 * calculation (even if they live in the column directly). It is up to the
	 * caller to factor such components into the row heights calculation.
	 * 
	 * @param rowIndex
	 *            The index of the row whose preferred height we're calculating
	 * 
	 * @param columnWidths
	 *            An array of column width values corresponding to the columns
	 *            of the table pane
	 */
	private int getPreferredRowHeight(int rowIndex, int[] columnWidths) {
		if (columnWidths == null) {
			throw new IllegalArgumentException("columnWidths is null");
		}

		Grid.Row row = rows.get(rowIndex);

		int preferredHeight = 0;

		for (int j = 0, n = row.getWidgetCount(), m = columns.size(); j < n
				&& j < m; j++) {
			Widget widget = row.getWidget(j);

			if (widget != null && widget.isVisible()
					&& WidgetHelper.getRowSpan(widget) == 1) {
				preferredHeight = Math.max(preferredHeight, WidgetHelper
						.getPreferredHeight(widget, columnWidths[j]));
			}
		}

		return preferredHeight;
	}

	/**
	 * Tells whether or not the specified row is visible. A row is visible if
	 * and only if one or more visible components occupies it. A component is
	 * said to occupy a cell if it either lives in the cell directly or spans
	 * the cell.
	 * 
	 * @param rowIndex
	 *            The index of the row within the table pane
	 * 
	 * @return <tt>true</tt> if the row is visible; <tt>false</tt> otherwise
	 */
	private boolean isRowVisible(int rowIndex) {
		boolean visible = false;

		boolean[][] occupiedCells = getOccupiedCells();

		for (int j = 0; j < occupiedCells[rowIndex].length; j++) {
			if (occupiedCells[rowIndex][j]) {
				visible = true;
				break;
			}
		}

		return visible;
	}

	/**
	 * Gets the width of each table pane column given the specified table pane
	 * width.
	 * 
	 * @param width
	 *            The width constraint of the table pane
	 * 
	 * @return An array containing the width of each column in the table pane
	 *         given the specified constraint
	 */
	private int[] getColumnWidths(int width) {
		int rowCount = rows.size();
		int columnCount = columns.size();

		int[] columnWidths = new int[columnCount];

		boolean[] defaultWidthColumns = new boolean[columnCount];
		int totalRelativeWeight = 0;
		int visibleColumnCount = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int reservedWidth = boxModel.getPaddingWidthContribution();

		// First, we allocate the widths of non-relative columns. We store the
		// widths of relative columns as negative values for later processing

		for (int j = 0; j < columnCount; j++) {
			if (isColumnVisible(j)) {
				Grid.Column column = columns.get(j);

				LayoutData layoutData = WidgetHelper.getLayoutData(column);
				String widthHint = layoutData.getPreferredWidth();

				if (column.isRelative()) {
					// XXX columnWidths[j] = -columnWidth;
					columnWidths[j] = -column.getWeight();
					totalRelativeWeight += column.getWeight();
				} else {
					int columnWidth;

					if (widthHint == null) {
						// Default width column; we must calculate the width
						columnWidth = getPreferredColumnWidth(j);
						defaultWidthColumns[j] = true;
					} else {
						columnWidth = WidgetHelper
								.getPreferredWidth(column, -1);
					}

					columnWidths[j] = columnWidth;
					reservedWidth += columnWidth;
				}

				visibleColumnCount++;
			} else {
				columnWidths[j] = 0;
			}
		}

		if (visibleColumnCount > 1) {
			reservedWidth += (visibleColumnCount - 1) * horizontalSpacing;
		}

		// Next, we we account for default-width columns containing spanning
		// cells, which have been ignored thus far. We ensure that the sum of
		// the widths of the spanned cells is enough to satisfy the preferred
		// width of the spanning content.

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget component = row.getWidget(j);

				if (component != null && component.isVisible()) {
					int columnSpan = WidgetHelper.getColumnSpan(component);

					if (columnSpan > 1) {
						// We might need to adjust column widths to accommodate
						// this spanning cell. First, we find out if any of the
						// spanned cells are default width and how much space
						// we've allocated thus far for those cells

						boolean adjustCells = true;
						int spannedDefaultWidthCellCount = 0;
						int spannedWidth = 0;

						for (int k = 0; k < columnSpan && j + k < columnCount; k++) {
							if (columnWidths[j + k] < 0) {
								adjustCells = false;
								break;
							}

							if (defaultWidthColumns[j + k]) {
								spannedDefaultWidthCellCount++;
							}

							spannedWidth += columnWidths[j + k];
						}

						// If we span any relative-width columns, we assume
						// that we'll achieve the desired spanning width when
						// we dive up the remaining space, so there's no need
						// to make an adjustment here. This assumption is safe
						// because our preferred width policy is to *either*
						// divide the adjustment among the relative-width
						// columns *or* among the default-width columns if we
						// don't span any relative-width columns

						if (adjustCells && spannedDefaultWidthCellCount > 0) {
							int componentPreferredWidth = WidgetHelper
									.getPreferredWidth(component, -1);

							if (componentPreferredWidth > spannedWidth) {
								// The component's preferred width is larger
								// than the width we've allocated thus far, so
								// an adjustment is necessary
								int adjustment = componentPreferredWidth
										- spannedWidth;

								// We'll distribute the adjustment evenly
								// among the default-width columns
								for (int k = 0; k < columnSpan
										&& j + k < columnCount; k++) {
									if (defaultWidthColumns[j + k]) {
										int columnAdjustment = adjustment
												/ spannedDefaultWidthCellCount;

										columnWidths[j + k] += columnAdjustment;
										reservedWidth += columnAdjustment;

										// Adjust these to avoid rounding errors
										adjustment -= columnAdjustment;
										spannedDefaultWidthCellCount--;
									}
								}
							}
						}
					}
				}
			}
		}

		// Finally, we allocate the widths of the relative columns by divvying
		// up the remaining width

		int remainingWidth = Math.max(width - reservedWidth, 0);
		if (totalRelativeWeight > 0 && remainingWidth > 0) {
			for (int j = 0; j < columnCount; j++) {
				if (columnWidths[j] < 0) {
					int relativeWeight = -columnWidths[j];
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					int columnWidth = (int) (remainingWidth * weightPercentage);

					columnWidths[j] = columnWidth;

					// NOTE we adjust remainingWidth and totalRelativeWeight as
					// we go to avoid potential rounding errors in the
					// columnWidth calculation
					remainingWidth -= columnWidth;
					totalRelativeWeight -= relativeWeight;
				}
			}
		}

		return columnWidths;
	}

	/**
	 * Gets the height of each row of a table pane given the specified
	 * constraints.
	 * 
	 * @param height
	 *            The height constraint of the table pane
	 * 
	 * @param columnWidths
	 *            The widths of the table pane's columns, which will be used as
	 *            width constraints to the row heights when necessary, or
	 *            <tt>null</tt> if the column widths are not yet known (the row
	 *            heights will be unconstrained)
	 * 
	 * @return An array containing the height of each row in the table pane
	 *         given the specified constraints
	 */
	private int[] getRowHeights(int height, int[] columnWidths) {
		if (columnWidths == null) {
			throw new IllegalArgumentException("columnWidths is null");
		}

		int rowCount = rows.size();
		int columnCount = columns.size();

		int rowHeights[] = new int[rowCount];

		boolean[] defaultHeightRows = new boolean[rowCount];
		int totalRelativeWeight = 0;
		int visibleRowCount = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int reservedHeight = boxModel.getPaddingHeightContribution();

		// First, we allocate the heights of non-relative rows. We store the
		// heights of relative rows as negative values for later processing

		for (int i = 0; i < rowCount; i++) {
			if (isRowVisible(i)) {
				Grid.Row row = rows.get(i);

				LayoutData layoutData = WidgetHelper.getLayoutData(row);
				String heightHint = layoutData.getPreferredHeight();

				if (row.isRelative()) {
					// XXX rowHeights[i] = -rowHeight;
					rowHeights[i] = -row.getWeight();
					totalRelativeWeight += row.getWeight();
				} else {
					int rowHeight;

					if (heightHint == null) {
						// Default height row; we must calculate the height
						rowHeight = getPreferredRowHeight(i, columnWidths);
						defaultHeightRows[i] = true;
					} else {
						rowHeight = WidgetHelper.getPreferredHeight(row, -1);
					}

					rowHeights[i] = rowHeight;
					reservedHeight += rowHeight;
				}

				visibleRowCount++;
			} else {
				rowHeights[i] = 0;
			}
		}

		if (visibleRowCount > 1) {
			reservedHeight += (visibleRowCount - 1) * verticalSpacing;
		}

		// Next, we we account for default-width columns containing spanning
		// cells, which have been ignored thus far. We ensure that the sum of
		// the widths of the spanned cells is enough to satisfy the preferred
		// width of the spanning content.

		for (int i = 0; i < rowCount; i++) {
			Grid.Row row = rows.get(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget component = row.getWidget(j);

				if (component != null && component.isVisible()) {
					int rowSpan = WidgetHelper.getRowSpan(component);

					if (rowSpan > 1) {
						// We might need to adjust row heights to accomodate
						// this spanning cell. First, we find out if any of the
						// spanned cells are default height and how much space
						// we've allocated thus far for those cells

						boolean adjustCells = true;
						int spannedDefaultHeightCellCount = 0;
						int spannedHeight = 0;

						for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
							if (rowHeights[i + k] < 0) {
								adjustCells = false;
								break;
							}

							if (defaultHeightRows[i + k]) {
								spannedDefaultHeightCellCount++;
							}

							spannedHeight += rowHeights[i + k];
						}

						// If we span any relative-height rows, we assume
						// that we'll achieve the desired spanning height when
						// we divvy up the remaining space, so there's no need
						// to make an adjustment here. This assumption is safe
						// because our preferred height policy is to *either*
						// divide the adjustment among the relative-height
						// rows *or* among the default-height rows if we
						// don't span any relative-height rows

						if (adjustCells && spannedDefaultHeightCellCount > 0) {
							int componentPreferredHeight = WidgetHelper
									.getPreferredHeight(component,
											columnWidths[j]);

							if (componentPreferredHeight > spannedHeight) {
								// The component's preferred height is larger
								// than the height we've allocated thus far, so
								// an adjustment is necessary
								int adjustment = componentPreferredHeight
										- spannedHeight;

								// We'll distribute the adjustment evenly
								// among the default-height rows
								for (int k = 0; k < rowSpan && i + k < rowCount; k++) {
									if (defaultHeightRows[i + k]) {
										int rowAdjustment = adjustment
												/ spannedDefaultHeightCellCount;

										rowHeights[i + k] += rowAdjustment;
										reservedHeight += rowAdjustment;

										// Adjust these to avoid rounding errors
										adjustment -= rowAdjustment;
										spannedDefaultHeightCellCount--;
									}
								}
							}
						}
					}
				}
			}
		}

		// Finally, we allocate the heights of the relative rows by diving
		// up the remaining height

		int remainingHeight = Math.max(height - reservedHeight, 0);
		if (totalRelativeWeight > 0 && remainingHeight > 0) {
			for (int i = 0; i < rowCount; i++) {
				if (rowHeights[i] < 0) {
					int relativeWeight = -rowHeights[i];
					float weightPercentage = relativeWeight
							/ (float) totalRelativeWeight;
					int rowHeight = (int) (remainingHeight * weightPercentage);

					rowHeights[i] = rowHeight;

					// NOTE we adjust remainingHeight and totalRelativeWeight as
					// we go to avoid potential rounding errors in the rowHeight
					// calculation
					remainingHeight -= rowHeight;
					totalRelativeWeight -= relativeWeight;
				}
			}
		}

		return rowHeights;
	}

}
