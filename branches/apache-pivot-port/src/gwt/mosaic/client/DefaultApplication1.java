package gwt.mosaic.client;

import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.BoxPane;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.ImageView;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Orientation;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.content.ButtonData;
import gwt.mosaic.client.wtk.media.Picture;

public class DefaultApplication1 implements Application {
	private Window window;

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();

		ImageView imageView = new ImageView();
		imageView.setImage(new Picture(
				"http://www.google.gr/images/logos/ps_logo2a_cp.png"));

		PushButton pushButton1 = new PushButton();
		Picture pic1 = new Picture(
				"http://upload.wikimedia.org/wikipedia/en/f/f6/Gwt-logo.png");
		pic1.setPixelSize(96, 96);
		pushButton1.setButtonData(new ButtonData(pic1, "PushButton #1"));

		PushButton pushButton2 = new PushButton();
		Picture pic2 = new Picture(
				"http://upload.wikimedia.org/wikipedia/en/f/f6/Gwt-logo.png");
		pic2.setPixelSize(96, 96);
		pushButton2.setButtonData(new ButtonData(pic2, "PushButton #21"));

		PushButton pushButton3 = new PushButton();
		Picture pic3 = new Picture(
				"http://upload.wikimedia.org/wikipedia/en/f/f6/Gwt-logo.png");
		pic3.setPixelSize(96, 96);
		pushButton3.setButtonData(new ButtonData(pic3, "PushButton #311"));

		// Checkbox checkbox = new Checkbox();
		// checkbox.setButtonData(new ButtonData(new Picture(
		// "http://upload.wikimedia.org/wikipedia/en/f/f6/Gwt-logo.png"),
		// "This is a PushButton"));

		// Label label1 = new Label("Hello!");
		// label1.getStyles().put("font",
		// new Font(FontStyle.NORMAL, FontWeight.BOLD, 32, "Verdana"));
		// label1.getStyles().put("backgroundColor", Color.BLUE);
		// label1.getStyles().put("padding", new Insets(5));
		// label1.getStyles().put("horizontalAlignment",
		// HorizontalAlignment.CENTER);
		// label1.getStyles().put("verticalAlignment",
		// VerticalAlignment.CENTER);
		// label1.setPreferredWidth(200);
		//
		// Label label2 = new Label("Hello!");
		// label2.getStyles().put("font",
		// new Font(FontStyle.NORMAL, FontWeight.BOLD, 32, "Verdana"));
		// label2.getStyles().put("color", Color.BLUE);
		// label2.getStyles().put("padding", new Insets(5));
		//

		BoxPane pane = new BoxPane();
		pane.setOrientation(Orientation.VERTICAL);
		pane.getStyles().put("fill", true);
		pane.add(pushButton1);
		pane.add(pushButton2);
		pane.add(pushButton3);

		window.setContent(pane);
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
