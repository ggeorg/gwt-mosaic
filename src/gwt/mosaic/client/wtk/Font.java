package gwt.mosaic.client.wtk;

import java.io.Serializable;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.Element;

public class Font implements Serializable {
	private static final long serialVersionUID = -5221372237798800324L;

	private final String family;
	private final String size;
	private final Style.FontStyle style;
	private final Style.FontWeight weight;

	public Font() {
		this("arial", "1em", FontStyle.NORMAL, FontWeight.NORMAL);
	}

	public Font(String family, String size, Style.FontStyle style,
			Style.FontWeight weight) {
		this.family = family;
		this.size = size;
		this.style = style;
		this.weight = weight;
	}

	public String getFamily() {
		return family;
	}

	public String getSize() {
		return size;
	}

	public Style.FontStyle getStyle() {
		return style;
	}

	public Style.FontWeight getWeight() {
		return weight;
	}

	public void applyTo(Element elem) {
		elem.getStyle().setProperty("fontFamily", family);
		elem.getStyle().setProperty("fontSize", size);
		elem.getStyle().setFontStyle(style);
		elem.getStyle().setFontWeight(weight);
	}

	public void readFrom(Element elem) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Font other = (Font) obj;
		if (family == null) {
			if (other.family != null)
				return false;
		} else if (!family.equals(other.family))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (style != other.style)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return style.toString() + " " + weight.toString() + " " + size + " "
				+ family;
	}
}
