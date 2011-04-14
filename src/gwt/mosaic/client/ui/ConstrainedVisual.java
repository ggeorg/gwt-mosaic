package gwt.mosaic.client.ui;

/**
 * Interface representing a visual that is used in layout.
 */
public interface ConstrainedVisual extends Visual {

	public int getPreferredWidth(int clientHeight);

	public int getPreferredHeight(int clientWidth);

	public Dimensions getPreferredSize();

	public int getBaseline(int clientWidth, int clientHeight);

}
