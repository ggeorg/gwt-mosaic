package gwt.mosaic.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public abstract class LayoutPanel extends AbsolutePanel implements
		ConstrainedVisual, RequiresResize, ProvidesResize {
	private static final int DEFAULT_DELAY_MILLIS = 33;

	private int invalidateTimerDelayMillis = DEFAULT_DELAY_MILLIS;

	private final transient Timer validateTimer = new Timer() {
		@Override
		public void run() {
			validate();
		}
	};
	
	// Calculated preferred width value
	protected transient int preferredWidth = -1;

	// Calculated preferred height value
	protected transient int preferredHeight = -1;

	// Calculated preferred size value
	protected transient Dimensions preferredSize = null;

	// The panel's valid state
	private transient boolean valid = false;

	protected LayoutPanel() {
		super();

		String cn = getClass().getName();
		cn = cn.substring(cn.lastIndexOf('.') + 1);

		setStyleName("m-" + cn);
	}

	@Override
	protected void setWidgetPositionImpl(Widget w, int left, int top) {
		Element h = w.getElement();
		// if (left == -1 && top == -1) {
		// changeToStaticPositioning(h);
		// } else {
		DOM.setStyleAttribute(h, "position", "absolute");
		DOM.setStyleAttribute(h, "left", left + "px");
		DOM.setStyleAttribute(h, "top", top + "px");
		// }
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * Flags the panel's hierarchy as invalid, and clears any cached preferred
	 * size.
	 */
	public void invalidate() {
		valid = false;

		Widget parent = getParent();
		if (parent != null) {
			if (parent instanceof LayoutPanel) {
				((LayoutPanel) parent).invalidate();
			} else {
				if (GWT.isProdMode()) {
					validateTimer.schedule(invalidateTimerDelayMillis);
				} else {
					validateTimer.schedule(2 * invalidateTimerDelayMillis);
				}
			}
		}
	}

	public final void validate() {
		try {
			if (!valid && isVisible()) {
				layout();
				valid = true;
			}
		} catch (Exception e) {
			GWT.log("Error: ", e);
			Window.alert(e.toString());
		}
	}

	/**
	 * Called to layout the component.
	 */
	private void layout() {
		doLayout();

		for (Widget child : getChildren()) {
			if (child instanceof LayoutPanel) {
				((LayoutPanel) child).validate();
			} else if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}

	protected abstract void doLayout();

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				invalidate();
			}
		});
	}

	@Override
	public final void onResize() {
		invalidate();
	}

	@Override
	public int getBaseline() {
		return getBaseline(getElement().getClientWidth(), getElement()
				.getClientHeight());
	}

	//
	// UiBinder related layout hints
	//

	@Override
	public void setPreferredWidth(String preferredWidth) {
		WidgetHelper.setPreferredWidth(this, preferredWidth);
	}

	@Override
	public void setPreferredHeight(String preferredHeight) {
		WidgetHelper.setPreferredHeight(this, preferredHeight);
	}

	@Override
	public void setColumnSpan(int columnSpan) {
		WidgetHelper.setColumnSpan(this, columnSpan);
	}

	@Override
	public void setRowSpan(int rowSpan) {
		WidgetHelper.setRowSpan(this, rowSpan);
	}

	@Override
	public void setWeight(int weight) {
		WidgetHelper.setWeight(this, weight);
	}
}
