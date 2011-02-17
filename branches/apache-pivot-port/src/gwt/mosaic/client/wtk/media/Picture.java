package gwt.mosaic.client.wtk.media;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * Image representing a bitmapped picture.
 */
public class Picture extends Image {

	private com.google.gwt.user.client.ui.Image img;

	private int baseline = -1;

	public Picture(String location) {
		com.google.gwt.user.client.ui.Image.prefetch(location);
		initWidget(this.img = new com.google.gwt.user.client.ui.Image(location));
		this.img.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				imageListeners.onLoad(Picture.this);
				imageListeners.sizeChanged(Picture.this, 0, getWidth());
			}
		});
		this.img.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				imageListeners.onError(Picture.this);
			}
		});
	}

	@Override
	public int getWidth() {
		return img.getWidth();
	}

	@Override
	public int getHeight() {
		return img.getHeight();
	}

	@Override
	public int getBaseline() {
		return baseline;
	}

	public void setBaseline(int baseline) {
		int previousBaseline = this.baseline;

		if (baseline != previousBaseline) {
			this.baseline = baseline;
			imageListeners.baselineChanged(this, previousBaseline);
		}
	}

	@Override
	public void paint(Widget context) {
		// No-op
	}
}
