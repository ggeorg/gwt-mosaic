package gwt.mosaic.client;

import gwt.mosaic.client.beans.BeanAdapterFactory;
import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.immutable.ImmutableMap;
import gwt.mosaic.client.wtk.Application;
import gwt.mosaic.client.wtk.ApplicationContext;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtMosaic extends ApplicationContext implements EntryPoint,
		ClosingHandler, CloseHandler<Window>, ResizeHandler {
	
	static {
		GWT.create(BeanAdapterFactory.class);
	}

	private DisplayHost displayHost = null;
	private Application application = null;

	private HashMap<String, String> startupProperties = new HashMap<String, String>();

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		// Create the display host
		displayHost = new DisplayHost(RootPanel.get());
		displayHost.getDisplay().setSize(Window.getClientWidth(),
				Window.getClientHeight());

		// Load the application
		try {
			application = GWT.create(Application.class);
		} catch (Throwable throwable) {
			GWT.log("No application", throwable);
			application = new DefaultApplication3();
		}

		Window.addWindowClosingHandler(this);
		Window.addCloseHandler(this);
		Window.addResizeHandler(this);

		StartCallback startCallback = new StartCallback();
		Scheduler.get().scheduleDeferred(startCallback);
	}

	@Override
	public void onClose(CloseEvent<Window> event) {

	}

	@Override
	public void onWindowClosing(ClosingEvent event) {

	}

	@Override
	public void onResize(ResizeEvent event) {
		displayHost.getDisplay().setSize(event.getWidth(), event.getHeight());
	}

	public Application getApplication() {
		return application;
	}

	private void displayException(Exception exception) {
		GWT.log(exception.getMessage(), exception);

		String message = exception.getClass().getName();
		String body = exception.getMessage();

		Window.alert(message + "\n\n" + body);
	}

	private class StartCallback implements Command {
		@Override
		public void execute() {
			// Start the application
			if (application != null) {
				try {
					application
							.startup(displayHost.getDisplay(),
									new ImmutableMap<String, String>(
											startupProperties));
				} catch (Exception exception) {
					displayException(exception);
				}
			}
		}
	}
}
