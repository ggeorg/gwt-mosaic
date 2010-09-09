package org.gwt.mosaic2g.client.scene.ui;

import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Show;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class Box extends Control {

	public Box(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}
	
	@Override
	protected Widget createWidget() {
		final Widget result = new BoxWidget();
		updateWidget(result, true);
		return result;
	}
	
	// -------------------------
	private class BoxWidget extends Widget {
		public BoxWidget() {
			setElement(Document.get().createDivElement());
		}
	}

}
