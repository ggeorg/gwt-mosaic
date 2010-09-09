package org.gwt.mosaic2g.client.scene.ui;

import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Show;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Image extends Control implements /* TODO Resizable */LoadHandler, ErrorHandler {

	private String url;
	private boolean urlChanged = false;

	private boolean imageSetup;

	private com.google.gwt.user.client.ui.Image cachedWidget;

	public Image(Show show) {
		this(show, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	public Image(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		assert url != null;
		if (url.equals(this.url)) {
			return;
		}
		if (isSetup()) {
			final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(
					url);
			img.addLoadHandler(new LoadHandler() {
				public void onLoad(LoadEvent event) {
					RootPanel.get().remove(img);
					Image.this.url = url;
					Image.this.urlChanged = true;
				}
			});
			Style imgStyle = img.getElement().getStyle();
			imgStyle.setVisibility(Visibility.HIDDEN);
			RootPanel.get().add(img, Short.MIN_VALUE, Short.MIN_VALUE);
		} else {
			this.imageSetup = false;
			this.url = url;
			// this.urlChanged = true;
		}
		markAsChanged();
	}

	@Override
	protected Widget createWidget() {
		updateWidget(cachedWidget = new com.google.gwt.user.client.ui.Image(),
				true);
		cachedWidget.addLoadHandler(this);
		cachedWidget.addErrorHandler(this);
		return cachedWidget;
	}

	@Override
	protected void updateWidget(Widget w, boolean init) {
		super.updateWidget(w, init);
		if (urlChanged) {
			cachedWidget.setUrl(url);
		}
	}

	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			cachedWidget.setUrl(url);
			if (!cachedWidget.isAttached()) {
				Style widgetStyle = cachedWidget.getElement().getStyle();
				widgetStyle.setVisibility(Visibility.HIDDEN);
				RootPanel.get().add(cachedWidget, Short.MIN_VALUE,
						Short.MIN_VALUE);
			}
		} else {
			imageSetup = false;
		}
	}

	@Override
	public boolean needsMoreSetup() {
		return !imageSetup;
	}

	public void onError(ErrorEvent event) {
		imageSetup = true;
		sendFeatureSetup();
	}

	public void onLoad(LoadEvent event) {
		imageSetup = true;
		sendFeatureSetup();
	}

	@Override
	protected void sendFeatureSetup() {
		RootPanel.get().remove(cachedWidget);
		Style widgetStyle = cachedWidget.getElement().getStyle();
		widgetStyle.setVisibility(Visibility.VISIBLE);
		super.sendFeatureSetup();
	}

}
