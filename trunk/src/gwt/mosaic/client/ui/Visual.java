package gwt.mosaic.client.ui;

/**
 * Interface representing a "visual". A visual is an object that can be drawn to
 * an output device.
 */
public interface Visual {

	// public int getWidth();

	// public int getHeight();

	public int getBaseline();

	// public int paint();
	
	//
	// UiBinder related layout hints
	//
	
	public void setPreferredWidth(String preferredWidth);
	public void setPreferredHeight(String preferredHeight);
	public void setColumnSpan(int columnSpan);
	public void setRowSpan(int rowSpan);
	public void setWeight(int weight);
}
