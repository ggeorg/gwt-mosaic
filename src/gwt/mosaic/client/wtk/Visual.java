package gwt.mosaic.client.wtk;

/**
 * Interface representing a "visual". A visual is an object that can be drawn to
 * an output device.
 */
public interface Visual {

	public int getWidth();

	public int getHeight();

	public int getBaseline();

	public void paint();
}
