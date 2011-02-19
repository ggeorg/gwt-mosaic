package gwt.mosaic.client.wtk;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class Dimensions implements Serializable {
	protected int width;
	protected int height;

	public static final String WIDTH_KEY = "width";
	public static final String HEIGHT_KEY = "height";
	
	public Dimensions() {
		this(0, 0);
	}

	public Dimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dimensions(Dimensions dimensions) {
		if (dimensions == null) {
			throw new IllegalArgumentException("dimensions is null");
		}
		this.width = dimensions.width;
		this.height = dimensions.height;
	}

	// TODO public Dimensions(Dictionary<String, ?> dimensions) {}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		if (object instanceof Dimensions) {
			Dimensions dimensions = (Dimensions) object;
			equals = (width == dimensions.width && height == dimensions.height);
		}

		return equals;
	}

	@Override
	public int hashCode() {
		return 31 * width + height;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [" + width + "x" + height + "]";
	}

	// TODO public static Dimensions decode(String value) {}
}
