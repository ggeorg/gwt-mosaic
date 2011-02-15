package gwt.mosaic.client;

import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.ImageView;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.media.Picture;

public class DefaultApplication implements Application {
	private Window window;

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();

		ImageView imageView = new ImageView();
		imageView.setImage(new Picture("http://www.google.gr/images/logos/ps_logo2a_cp.png"));
		
//		PushButton pushButton = new PushButton();
//		pushButton.setButtonData(new ButtonData("Hello"));

		window.setContent(imageView);
		window.setTitle("Hello World!");
		window.setMaximized(true);

		window.open(display);
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub

	}

}
