package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import com.google.gwt.user.client.ui.Widget;

class LayoutData {
	private final BoxModel boxModel;

	private String preferredWidth;
	private String preferredHeight;

	private int columnSpan = 1;
	private int rowSpan = 1;

	private int weight = 0;

	LayoutData(Widget w) {
		this.boxModel = new BoxModel(w);
	}

	public BoxModel getBoxModel() {
		return boxModel;
	}

	public String getPreferredWidth() {
		return preferredWidth;
	}

	public void setPreferredWidth(String preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	public String getPreferredHeight() {
		return preferredHeight;
	}

	public void setPreferredHeight(String preferredHeight) {
		this.preferredHeight = preferredHeight;
	}

	public int getColumnSpan() {
		return columnSpan;
	}

	public void setColumnSpan(int columnSpan) {
		if (columnSpan < 1) {
			throw new IllegalArgumentException("column span is less than 1");
		}
		this.columnSpan = columnSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		if (rowSpan < 1) {
			throw new IllegalArgumentException("row span is less than 1");
		}
		this.rowSpan = rowSpan;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		if (weight < 0) {
			throw new IllegalArgumentException("weight is negative");
		}
		this.weight = weight;
	}

	private int dx = 0;
	private int dy = 0;
	
	public void translate(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

}
