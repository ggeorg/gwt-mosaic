package gwt.mosaic.client.wtk;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.json.JSONSerializer;
import gwt.mosaic.shared.serialization.SerializationException;

import java.io.Serializable;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;

public class Font implements Serializable {
	private static final long serialVersionUID = -5221372237798800324L;

	private Style.FontStyle style;
	private Style.FontWeight weight;
	private int size;
	private String family;

	public static final String STYLE_KEY = "style";
	public static final String WEIGHT_KEY = "weight";
	public static final String SIZE_KEY = "size";
	public static final String FAMILY_KEY = "family";

	public Font() {
		this(FontStyle.NORMAL, FontWeight.NORMAL, 11, "Verdana");
	}

	public Font(Style.FontStyle style, Style.FontWeight weight, int size,
			String family) {
		this.style = style;
		this.weight = weight;
		this.size = size;
		this.family = family;
	}

	public Font(Font font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		this.style = font.style;
		this.weight = font.weight;
		this.size = font.size;
		this.family = font.family;
	}

	public Font(Dictionary<String, ?> font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		if (font.containsKey(STYLE_KEY)) {
			style = FontStyle.valueOf(font.get(STYLE_KEY).toString()
					.toUpperCase());
		} else {
			style = FontStyle.NORMAL;
		}

		if (font.containsKey(WEIGHT_KEY)) {
			weight = FontWeight.valueOf(font.get(WEIGHT_KEY).toString()
					.toUpperCase());
		} else {
			weight = FontWeight.NORMAL;
		}

		if (font.containsKey(SIZE_KEY)) {
			size = ((Number) font.get(SIZE_KEY)).intValue();
		} else {
			size = 11;
		}

		if (font.containsKey(FAMILY_KEY)) {
			family = font.get(FAMILY_KEY).toString();
		} else {
			family = "Verdana";
		}
	}

	public String getFamily() {
		return family;
	}

	public int getSize() {
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
		elem.getStyle().setFontSize(size, Unit.PX);
		elem.getStyle().setFontStyle(style);
		elem.getStyle().setFontWeight(weight);
	}

	public void readFrom(Element elem) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		result = prime * result + size;
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
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
		if (size != other.size)
			return false;
		if (style != other.style)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return style.toString() + " " + weight.toString() + " " + size + "px "
				+ family;
	}

	public static Font decode(String value) {
		if (value == null || !value.startsWith("{")) {
			throw new IllegalArgumentException();
		}

		Font font;
		try {
			font = new Font(JSONSerializer.parseMap(value));
		} catch (SerializationException exception) {
			throw new IllegalArgumentException(exception);
		}

		return font;
	}
}
