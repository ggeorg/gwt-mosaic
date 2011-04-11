package gwt.mosaic.client.ui;

import com.google.gwt.user.client.ui.HasHTML;

public class HTML extends Label implements HasHTML {

	@Override
	public String getHTML() {
		return htmlDiv.getHTML();
	}

	@Override
	public void setHTML(String html) {
		this.htmlDiv.setHTML(html);
	}

}
