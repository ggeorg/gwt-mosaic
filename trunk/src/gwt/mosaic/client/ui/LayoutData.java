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

	public boolean setPreferredWidth(String preferredWidth) {
		if (isChanged(this.preferredWidth, preferredWidth)) {
			this.preferredWidth = preferredWidth;
			return true;
		} else {
			return false;
		}
	}

	public String getPreferredHeight() {
		return preferredHeight;
	}

	public boolean setPreferredHeight(String preferredHeight) {
		if (isChanged(this.preferredHeight, preferredHeight)) {
			this.preferredHeight = preferredHeight;
			return true;
		} else {
			return false;
		}
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

	private static boolean isChanged(Object o1, Object o2) {
		if (o1 == o2) {
			return false;
		} else if (o1 != null && o1.equals(o2)) {
			return false;
		} else {
			return true;
		}
	}

}
