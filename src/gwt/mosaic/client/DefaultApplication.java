package gwt.mosaic.client;

import gwt.mosaic.client.collections.ArrayList;
import gwt.mosaic.client.collections.List;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.ListView;
import gwt.mosaic.client.wtk.Window;

public class DefaultApplication implements Application {
	private Window window;

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();

		ListView listView = new ListView();
		List<String> data = new ArrayList<String>(
				new String[] { "One", "Two", "Three", "Four", "Five", "Six",
						"Seven", "Eight", "Nine", "Ten" });
		listView.setListData(data);

		window.setContent(listView);
		window.setTitle("Hello World!");
		window.setX(10);
		window.setY(10);
		// window.setMaximized(true);

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
