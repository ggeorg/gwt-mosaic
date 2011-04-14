package gwt.mosaic.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;

public class Opacity {
	private OpacityImpl impl = GWT.create(OpacityImpl.class);

	private int opacity = -1;

	public Opacity(int opacity) {
		this.opacity = opacity;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	public void applyTo(Element elem) {
		impl.applyTo(elem, opacity);
	}

	public void readFrom(Element elem) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + opacity;
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
		Opacity other = (Opacity) obj;
		if (opacity != other.opacity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return impl.toString(opacity);
	}

}
