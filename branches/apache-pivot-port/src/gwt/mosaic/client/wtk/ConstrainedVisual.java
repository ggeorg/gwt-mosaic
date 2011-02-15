package gwt.mosaic.client.wtk;

/**
 * Interface representing a visual that is used in layout.
 */
public interface ConstrainedVisual extends Visual {

	public void setSize(int width, int height);
	
	public int getPreferredWidth(int height);
	
	public int getPreferredHeight(int width);
	
	public Dimensions getPreferredSize();
	
	public int getBaseline(int width, int height);
}
