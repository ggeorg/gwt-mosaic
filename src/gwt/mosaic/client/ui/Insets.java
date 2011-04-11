package gwt.mosaic.client.ui;

/**
 * Class representing the insets of an object.
 */
public final class Insets {

	public final int top;
	public final int left;
	public final int bottom;
	public final int right;

	/**
	 * Insets whose top, left, bottom, and right values are all zero.
	 */
	public static final Insets NONE = new Insets(0);

	public Insets(int inset) {
		this.top = inset;
		this.left = inset;
		this.bottom = inset;
		this.right = inset;
	}

	public Insets(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public Insets(Insets insets) {
		if (insets == null) {
			throw new IllegalArgumentException("insets is null.");
		}

		this.top = insets.top;
		this.left = insets.left;
		this.bottom = insets.bottom;
		this.right = insets.right;
	}

	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		if (object instanceof Insets) {
			Insets insets = (Insets) object;
			equals = (top == insets.top && left == insets.left
					&& bottom == insets.bottom && right == insets.right);
		}

		return equals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + top;
		result = prime * result + left;
		result = prime * result + bottom;
		result = prime * result + right;
		return result;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [" + top + ", " + left + ", " + bottom
				+ ", " + right + "]";
	}

}