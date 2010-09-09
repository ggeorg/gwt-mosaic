package org.gwt.mosaic2g.client.util;

/**
 * The {@code Rectangle} class defines a rectangle with the specified size and
 * location.
 * 
 * @author ggeorg
 */
public class Rectangle {

	public int y;
	public int x;
	public int width;
	public int height;

	public Rectangle() {
		reshape(0, 0, 0, 0);
	}

	public Rectangle(Rectangle r) {
		reshape(r.x, r.y, r.width, r.height);
	}

	public Rectangle(int x, int y, int width, int height) {
		reshape(x, y, width, height);
	}

	public void add(int newx, int newy) {
		if ((width | height) < 0) {
			this.x = newx;
			this.y = newy;
			this.width = this.height = 0;
			return;
		}
		int x1 = this.x;
		int y1 = this.y;
		long x2 = this.width;
		long y2 = this.height;
		x2 += x1;
		y2 += y1;
		if (x1 > newx)
			x1 = newx;
		if (y1 > newy)
			y1 = newy;
		if (x2 < newx)
			x2 = newx;
		if (y2 < newy)
			y2 = newy;
		x2 -= x1;
		y2 -= y1;
		if (x2 > Integer.MAX_VALUE)
			x2 = Integer.MAX_VALUE;
		if (y2 > Integer.MAX_VALUE)
			y2 = Integer.MAX_VALUE;
		reshape(x1, y1, (int) x2, (int) y2);
	}

	public void reshape(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds(Rectangle r) {
		reshape(r.x, r.y, r.width, r.height);
	}

	@Override
	public String toString() {
		return "Rectangle [x=" + x + ", y=" + y + ", width=" + width
				+ ", height=" + height + "]";
	}

	public boolean contains(int X, int Y) {
		int w = this.width;
		int h = this.height;
		if ((w | h) < 0) {
			// At least one of the dimensions is negative...
			return false;
		}
		// Note: if either dimension is zero, tests below must return false...
		int x = this.x;
		int y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		h += y;
		// overflow || intersect
		return ((w < x || w > X) && (h < y || h > Y));
	}

}
