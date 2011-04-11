package gwt.mosaic.client.ui;

/**
 * Interface representing a visual that is used in layout.
 */
public interface ConstrainedVisual extends Visual {

	public int getPreferredWidth(int height);

	public int getPreferredHeight(int width);

	public Dimensions getPreferredSize();

	public int getBaseline(int width, int height);

}
