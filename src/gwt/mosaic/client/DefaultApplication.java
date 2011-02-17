package gwt.mosaic.client;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;

import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.BoxPane;
import gwt.mosaic.client.wtk.Display;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.HorizontalAlignment;
import gwt.mosaic.client.wtk.ImageView;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Label;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.VerticalAlignment;
import gwt.mosaic.client.wtk.Window;
import gwt.mosaic.client.wtk.content.ButtonData;
import gwt.mosaic.client.wtk.media.Picture;
import gwt.mosaic.client.wtk.style.Color;

public class DefaultApplication implements Application {
	private Window window;

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		window = new Window();

		ImageView imageView = new ImageView();
		imageView.setImage(new Picture(
				"http://www.google.gr/images/logos/ps_logo2a_cp.png"));

		PushButton pushButton = new PushButton();
		pushButton.setButtonData(new ButtonData(new Picture(
				"http://upload.wikimedia.org/wikipedia/en/f/f6/Gwt-logo.png"),
				"This is a PushButton"));

		Label label1 = new Label("Hello!");
		label1.getStyles().put("font",
				new Font("Verdana", "32px", FontStyle.NORMAL, FontWeight.BOLD));
		label1.getStyles().put("color", Color.BLUE);
		label1.getStyles().put("padding", new Insets(5));
		label1.getStyles().put("horizontalAlignment",
				HorizontalAlignment.CENTER);
		label1.getStyles().put("verticalAlignment", VerticalAlignment.CENTER);
		label1.setPreferredWidth(200);

		Label label2 = new Label("Hello!");
		label2.getStyles().put("font",
				new Font("Verdana", "32px", FontStyle.NORMAL, FontWeight.BOLD));
		label2.getStyles().put("color", Color.BLUE);
		label2.getStyles().put("padding", new Insets(5));

		BoxPane pane = new BoxPane();
		pane.getStyles().put("padding", new Insets(20));
		pane.getStyles().put("fill", true);
		pane.add(imageView);
		pane.add(label2);

		window.setContent(pushButton);
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
