package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Container that arranges components in a two-dimensional grid, where every
 * cell is the same size.
 */
public class CopyOfGrid extends LayoutPanel {

	public static final class Row extends Layer {

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
	}

	/**
	 * Provides metadata about the grid that the grid use in performing
	 * preferred size calculations and layout.
	 */
	protected final class Metadata {
		public final int visibleRowCount;
		public final int visibleColumnCount;

		private boolean[] visibleRows;
		private boolean[] visibleColumns;

		public Metadata() {
			CopyOfGrid grid = CopyOfGrid.this;

			int columnCount = grid.getColumnCount();
			int rowCount = grid.getWidgetCount();

			visibleRows = new boolean[rowCount];
			visibleColumns = new boolean[columnCount];

			int visibleRowCount = 0;
			int visibleColumnCount = 0;

			for (int i = 0; i < rowCount; i++) {
				CopyOfGrid.Row row = (Row) grid.getWidget(i);

				for (int j = 0, n = row.getWidgetCount(); j < n
						&& j < columnCount; j++) {
					Widget widget = row.getWidget(j);

					if (widget != null && widget.isVisible()) {
						if (!visibleRows[i]) {
							++visibleRowCount;
							visibleRows[i] = true;
						}

						if (!visibleColumns[j]) {
							++visibleColumnCount;
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

	private int columnCount;

	// skin --------------------------------------------------------------------

	private int horizontalSpacing = 0;
	private int verticalSpacing = 0;

	// -------------------------------------------------------------------------

	private transient int cellWidth = 0;
	private transient int cellHeight = 0;

	public CopyOfGrid() {
		this(0);
	}

	public CopyOfGrid(int columnCount) {
		if (columnCount < 0) {
			throw new IllegalArgumentException("columnCount is negative.");
		}

		setColumnCount(columnCount);
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * Gets the widget at the specified cell in this grid pane.
	 * 
	 * @param rowIndex
	 *            The row index of the cell
	 * @param columnIndex
	 *            The column index of the cell
	 * @return The component in the specified cell, or <tt>null</tt> if the cell
	 *         is empty
	 */
	public Widget getCellWidget(int rowIndex, int columnIndex) {
		Row row = (Row) getWidget(rowIndex);

		Widget widget = null;

		if (row.getWidgetCount() > columnIndex) {
			widget = row.getWidget(columnIndex);
		}

		return widget;
	}

	@Override
	public void add(Widget w) {
		if (!(w instanceof CopyOfGrid.Row)) {
			throw new IllegalArgumentException("widget is not a Grid.Row");
		}
		super.add(w);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		if (!(w instanceof CopyOfGrid.Row)) {
			throw new IllegalArgumentException("widget is not a Grid.Row");
		}
		super.insert(w, beforeIndex);
	}

	@Override
	public int getPreferredWidth(int height) {
		LayoutData layoutData = WidgetHelper.getLayoutData(this);
		if (layoutData.getPreferredWidth() != null) {
			return WidgetHelper.getPreferredWidthImpl(this, height);
		}

		int columnCount = getColumnCount();
		int rowCount = getWidgetCount();

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Metadata metadata = new Metadata();

		int cellHeight = getCellHeight(height, metadata);

		int preferredCellWidth = 0;
		for (int i = 0; i < rowCount; i++) {
			CopyOfGrid.Row row = (Row) getWidget(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					preferredCellWidth = Math.max(preferredCellWidth,
							WidgetHelper.getPreferredWidth(widget, cellHeight));
				}
			}
		}

		// The preferred width of the grid pane is the sum of the column widths,
		// plus margin+border+padding and spacing

		int preferredWidth = boxModel.getWidthContribution()
				+ metadata.visibleColumnCount * preferredCellWidth;

		if (metadata.visibleColumnCount > 1) {
			preferredWidth += (metadata.visibleColumnCount - 1)
					* horizontalSpacing;
		}

		return preferredWidth;
	}

	@Override
	public int getPreferredHeight(int width) {
		LayoutData layoutData = WidgetHelper.getLayoutData(this);
		if (layoutData.getPreferredHeight() != null) {
			return WidgetHelper.getPreferredHeightImpl(this, width);
		}

		int columnCount = getColumnCount();
		int rowCount = getWidgetCount();

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Metadata metadata = new Metadata();

		int cellWidth = getCellWidth(width, metadata);

		int preferredCellHeight = 0;
		for (int i = 0; i < rowCount; i++) {
			CopyOfGrid.Row row = (Row) getWidget(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					preferredCellHeight = Math.max(preferredCellHeight,
							WidgetHelper.getPreferredHeight(widget, cellWidth));
				}
			}
		}

		// The preferred height of the grid is the sum of the row heights, plus
		// margin+border+padding and spacing

		int preferredHeight = boxModel.getHeightContribution()
				+ metadata.visibleRowCount * preferredCellHeight;

		if (metadata.visibleRowCount > 1) {
			preferredHeight += (metadata.visibleRowCount - 1) * verticalSpacing;
		}

		return preferredHeight;
	}

	@Override
	public Dimensions getPreferredSize() {
		LayoutData layoutData = WidgetHelper.getLayoutData(this);
		String widthHint = layoutData.getPreferredWidth();
		String heightHint = layoutData.getPreferredHeight();
		if (widthHint != null || heightHint != null) {
			return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
		}

		int columnCount = getColumnCount();
		int rowCount = getWidgetCount();

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Metadata metadata = new Metadata();

		// calculate the maximum preferred cellWidth and cellHeight
		int preferredCellHeight = 0;
		int preferredCellWidth = 0;
		for (int i = 0; i < rowCount; i++) {
			CopyOfGrid.Row row = (Row) getWidget(i);

			for (int j = 0, n = row.getWidgetCount(); j < n && j < columnCount; j++) {
				Widget widget = row.getWidget(j);

				if (widget != null && widget.isVisible()) {
					Dimensions d = WidgetHelper.getPreferredSize(widget);
					preferredCellHeight = Math.max(preferredCellHeight,
							d.height);
					preferredCellWidth = Math.max(preferredCellWidth, d.width);
				}
			}
		}

		// The preferred width of the grid is the sum of the column widths, plus
		// margin+border+padding and spacing

		int preferredWidth = boxModel.getWidthContribution()
				+ metadata.visibleColumnCount * preferredCellWidth;

		if (metadata.visibleColumnCount > 1) {
			preferredWidth += (metadata.visibleColumnCount - 1)
					* horizontalSpacing;
		}

		// The preferred height of the grid is the sum of the row heights, plus
		// margin+border+padding and spacing

		int preferredHeight = boxModel.getHeightContribution()
				+ metadata.visibleRowCount * preferredCellHeight;

		if (metadata.visibleRowCount > 1) {
			preferredHeight += (metadata.visibleRowCount - 1) * verticalSpacing;
		}

		return new Dimensions(preferredWidth, preferredHeight);
	}

	@Override
	public int getBaseline(int width, int height) {
		int columnCount = getColumnCount();
		int rowCount = getWidgetCount();

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Insets padding = boxModel.getPadding(); // XXX

		Metadata metadata = new Metadata();

		int cellWidth = getCellWidth(width, metadata);
		int cellHeight = getCellHeight(height, metadata);

		// Return the first available baseline by traversing cells top left to
		// bottom right

		int baseline = -1;

		int rowY = padding.top;

		for (int i = 0; i < rowCount && baseline == -1; i++) {
			if (metadata.isRowVisible(i)) {
				CopyOfGrid.Row row = (Row) getWidget(i);

				for (int j = 0, n = row.getWidgetCount(); j < n
						&& j < columnCount && baseline == -1; j++) {
					Widget widget = row.getWidget(j);

					if (widget != null && widget.isVisible()) {
						baseline = WidgetHelper.getBaseline(widget, cellWidth,
								cellHeight);

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
	protected void doLayout() {
		int columnCount = getColumnCount();
		int rowCount = getWidgetCount();

		int width = getElement().getClientWidth();
		int height = getElement().getClientHeight();

		Metadata metadata = new Metadata();

		cellWidth = getCellWidth(width, metadata);
		cellHeight = getCellHeight(height, metadata);

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Insets padding = boxModel.getPadding();

		int componentY = padding.top;
		for (int i = 0; i < rowCount; i++) {
			if (metadata.isRowVisible(i)) {
				CopyOfGrid.Row row = (Row) getWidget(i);

				int componentX = padding.left;

				WidgetHelper.setLocation(row, 0, 0);
				WidgetHelper.setSize(row, width, 1);

				for (int j = 0, n = row.getWidgetCount(); j < n
						&& j < columnCount; j++) {
					Widget widget = row.getWidget(j);

					if (widget != null && widget.isVisible()) {
						WidgetHelper
								.setLocation(widget, componentX, componentY);
						WidgetHelper.setSize(widget, cellWidth, cellHeight);
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
	 * Gets the cell width given the specified grid width and metadata.
	 */
	private int getCellWidth(int width, Metadata metadata) {
		int cellWidth = -1;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		if (width != -1) {
			int clientWidth = width - boxModel.getPaddingWidthContribution();

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
	 * Gets the cell height given the specified grid height and metadata.
	 */
	private int getCellHeight(int height, Metadata metadata) {
		int cellHeight = -1;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		if (height != -1) {
			int clientHeight = height - boxModel.getPaddingHeightContribution();

			if (metadata.visibleColumnCount > 1) {
				clientHeight -= (metadata.visibleRowCount - 1)
						* verticalSpacing;
			}

			clientHeight = Math.max(0, clientHeight);

			cellHeight = (metadata.visibleRowCount == 0) ? 0 : clientHeight
					/ metadata.visibleRowCount;
		}

		return cellHeight;
	}

}
