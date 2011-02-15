package gwt.mosaic.client.wtk;

import gwt.mosaic.client.collections.Dictionary;

import java.io.Serializable;

/**
 * Class representing the location of an object.
 */
public final class Point implements Serializable {
	private static final long serialVersionUID = -4438298400782987456L;

	public final int x;
	public final int y;

	public static final String X_KEY = "x";
	public static final String Y_KEY = "y";

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null.");
		}

		this.x = point.x;
		this.y = point.y;
	}

	public Point(Dictionary<String, ?> point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null.");
		}

		if (point.containsKey(X_KEY)) {
			x = (Integer) point.get(X_KEY);
		} else {
			x = 0;
		}

		if (point.containsKey(Y_KEY)) {
			y = (Integer) point.get(Y_KEY);
		} else {
			y = 0;
		}
	}

	public Point translate(int dx, int dy) {
		return new Point(x + dx, y + dy);
	}

	@Override
	public boolean equals(Object object) {
		boolean equals = false;

		if (object instanceof Point) {
			Point point = (Point) object;
			equals = (x == point.x && y == point.y);
		}

		return equals;
	}

	@Override
	public int hashCode() {
		return 31 * x + y;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + x + ", " + y + "]";
	}

	// TODO public static Point decode(String value) {}
}
