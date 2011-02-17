package gwt.mosaic.client.wtk.skin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class PanelSkin extends ContainerSkin {
	private AbsolutePanel widget = null;

	@Override
	public Widget getWidget() {
		if (widget == null) {
			
			widget = new AbsolutePanel() {
				
				@Override
				public void setWidgetPosition(Widget w, int left, int top) {
					if (!GWT.isProdMode()) {
						super.setWidgetPosition(w, left, top);
					} else {
						// we need to be fast, so no parent check in production
						setWidgetPositionImpl(w, left, top);
					}
				}

				@Override
				protected void setWidgetPositionImpl(Widget w, int left, int top) {
					/*
					 * XXX The AbsolutePanel default implementation says:
					 * Setting a position of (-1, -1) will cause the child
					 * widget to be positioned statically.
					 */
					com.google.gwt.user.client.Element h = w.getElement();
					// if (left == -1 && top == -1) {
					// changeToStaticPositioning(h);
					// } else {
					DOM.setStyleAttribute(h, "position", "absolute");
					DOM.setStyleAttribute(h, "left", left + "px");
					DOM.setStyleAttribute(h, "top", top + "px");
					// }
				}

			};

			String className = getComponent().getClass().getName();
			className = className.substring(className.lastIndexOf('.') + 1);
			widget.addStyleName("m-" + className);
		}

		return widget;
	}
}
