package gwt.mosaic.client.wtk.media;

import com.google.gwt.user.client.ui.Composite;

import gwt.mosaic.client.util.ListenerList;
import gwt.mosaic.client.wtk.Dimensions;
import gwt.mosaic.client.wtk.Visual;

/**
 * Abstract base class for images. An image is either a bitmapped "picture" or a
 * vector "drawing".
 */
public abstract class Image extends Composite implements Visual {
	/**
	 * Image listener list.
	 */
	protected static class ImageListenerList extends
			ListenerList<ImageListener> implements ImageListener {

		@Override
		public void sizeChanged(Image image, int previousWidth,
				int previousHeight) {
			for (ImageListener listener : this) {
				listener.sizeChanged(image, previousWidth, previousHeight);
			}
		}

		@Override
		public void baselineChanged(Image image, int previousBaseline) {
			for (ImageListener listener : this) {
				listener.baselineChanged(image, previousBaseline);
			}
		}

		@Override
		public void onLoad(Image image) {
			for (ImageListener listener : this) {
				listener.onLoad(image);
			}
		}

		@Override
		public void onError(Image image) {
			for (ImageListener listener : this) {
				listener.onError(image);
			}
		}
	}

	protected ImageListenerList imageListeners = new ImageListenerList();

	@Override
	public int getBaseline() {
		return -1;
	}

	public Dimensions getSize() {
		return new Dimensions(getWidth(), getHeight());
	}

	public ListenerList<ImageListener> getImageListeners() {
		return imageListeners;
	}
}
