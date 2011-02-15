/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.mosaic.client.wtk.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

public class Color {
	private ColorImpl impl = GWT.create(ColorImpl.class);

	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color LIGHT_GRAY = new Color(192, 192, 192);
	public static final Color GRAY = new Color(128, 128, 128);
	public static final Color DARK_GRAY = new Color(64, 64, 64);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color PINK = new Color(255, 175, 175);
	public static final Color ORANGE = new Color(255, 200, 0);
	public final static Color YELLOW = new Color(255, 255, 0);
	public final static Color GREEN = new Color(0, 255, 0);
	public final static Color MAGENTA = new Color(255, 0, 255);
	public final static Color CYAN = new Color(0, 255, 255);
	public final static Color BLUE = new Color(0, 0, 255);

	private int r, g, b;
	private double alpha;

	public Color(int rgb) {
		this(rgb, false);
	}

	public Color(int rgba, boolean hasalpha) {
		if (!hasalpha) {
			rgba = 0xff000000 | rgba;
		}
		r = (rgba >> 16) & 0xFF;
		g = (rgba >> 8) & 0xFF;
		b = (rgba >> 0) & 0xFF;
		alpha = ((rgba >> 24) & 0xff) / 255.0;
	}

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, double alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}

	public int getRed() {
		return r;
	}

	public void setRed(int r) {
		this.r = r & 0xFF;
	}

	public int getGreen() {
		return g;
	}

	public void setGreen(int g) {
		this.g = g & 0xFF;
	}

	public int getBlue() {
		return b;
	}

	public void setBlue(int b) {
		this.b = b & 0xFF;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		assert alpha >= 0 && alpha <= 1.0;
		this.alpha = alpha;
	}

	public int getRGB() {
		return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}

	public int getRGBA() {
		int a = (int) (alpha * 255);
		return ((a) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8)
				| ((b & 0xFF) << 0);
	}

	public Color brighter(double factor) {
		if (factor < 0 || factor > 0) {
			return this;
		}

		int R = r;
		int G = g;
		int B = b;

		int i = (int) (1.0 / (1.0 - factor));
		if (R == 0 && G == 0 && B == 0) {
			return new Color(i, i, i);
		}
		if (R > 0 && R < i)
			R = i;
		if (G > 0 && G < i)
			G = i;
		if (B > 0 && B < i)
			B = i;

		return new Color(Math.min((int) (R / factor), 255), Math.min(
				(int) (G / factor), 255), Math.min((int) (B / factor), 255));
	}

	public void applyTo(Element elem, boolean backgroundColor) {
		final Style style = elem.getStyle();
		if (backgroundColor) {
			style.setBackgroundColor(toString());
		} else {
			style.setColor(toString());
		}
	}

	public void readFrom(Element elem, boolean backgroundColor) {
		Color c;
		if (backgroundColor) {
			c = impl.parse(ComputedStyle.getBackgroundColor(elem));
		} else {
			c = impl.parse(ComputedStyle.getColor(elem));
		}
		r = c.r;
		g = c.g;
		b = c.b;
		alpha = c.alpha;
	}

	public Color darker(double factor) {
		return new Color(Math.max((int) (getRed() * factor), 0), Math.max(
				(int) (getGreen() * factor), 0), Math.max(
				(int) (getBlue() * factor), 0));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color other = (Color) obj;
		if (alpha != other.alpha)
			return false;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (alpha * 255);
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public String toString() {
		return impl.toString(r, g, b, alpha);
	}

}
