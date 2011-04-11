package gwt.mosaic.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockLayoutPanel.Direction;

public class SplitPanel extends Composite implements HasWidgets,
		RequiresResize, ProvidesResize {

	static class MySplitLayoutPanel extends SplitLayoutPanel {
	}

	class Wrapper extends com.google.gwt.user.client.ui.LayoutPanel {
		private final Stack innerPanel;

		public Wrapper(Widget w) {
			add(innerPanel = new Stack());
			innerPanel.add(w);
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			if (splitPanel.getWidgetDirection(this) != Direction.CENTER) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (orientation == Orientation.HORIZONTAL) {
							int width = WidgetHelper.getPreferredWidth(
									innerPanel, -1);
							splitPanel.setWidgetSize(Wrapper.this, width);
						} else {
							int height = WidgetHelper.getPreferredHeight(
									innerPanel, -1);
							splitPanel.setWidgetSize(Wrapper.this, height);
						}
					}
				});
			}
		}
	}

	interface MyUiBinder extends UiBinder<Widget, SplitPanel> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	SplitLayoutPanel splitPanel;

	/**
	 * Enumeration defining split pane regions.
	 */
	public enum Region {
		TOP_LEFT, BOTTOM_RIGHT
	}

	private final ArrayList<Wrapper> widgets = new ArrayList<Wrapper>();

	private Orientation orientation = null;
	private Region primaryRegion;

	public SplitPanel() {
		this(Orientation.HORIZONTAL);
	}

	protected SplitPanel(Orientation orientation) {
		initWidget(uiBinder.createAndBindUi(this));
		this.orientation = orientation;

		if (orientation == Orientation.HORIZONTAL) {
			primaryRegion = LocaleInfo.getCurrentLocale().isRTL() ? Region.TOP_LEFT
					: Region.BOTTOM_RIGHT;
		} else {
			primaryRegion = Region.BOTTOM_RIGHT;
		}
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		if (this.orientation != orientation) {
			this.orientation = orientation;
			restructure();
		}
	}

	public Region getPrimaryRegion() {
		return primaryRegion;
	}

	public void setPrimaryRegion(Region primaryRegion) {
		if (this.primaryRegion != primaryRegion) {
			this.primaryRegion = primaryRegion;
			restructure();
		}
	}

	private void restructure() {
		int n = widgets.size();
		if (n == 0) {
			return;
		}

		splitPanel.clear();

		if (primaryRegion == Region.TOP_LEFT) {
			for (int i = n - 1; i >= 0; i--) {
				Wrapper wrapper = widgets.get(i);
				if (i > 0) {
					if (orientation == Orientation.HORIZONTAL) {
						splitPanel.addWest(wrapper, 0);
					} else {
						splitPanel.addNorth(wrapper, 0);
					}
				} else {
					splitPanel.add(wrapper);
				}
			}
		} else {
			for (int i = 0; i < n; i++) {
				Wrapper wrapper = widgets.get(i);
				if (i < n - 1) {
					if (orientation == Orientation.HORIZONTAL) {
						splitPanel.addWest(wrapper, 0);
					} else {
						splitPanel.addNorth(wrapper, 0);
					}
				} else {
					splitPanel.add(wrapper);
				}
			}
		}

		WidgetHelper.invalidate(this);
	}

	@Override
	public void add(Widget w) {
		if (w == null) {
			throw new IllegalArgumentException("widget is null");
		}
		if (widgets.indexOf(w) != -1) {
			throw new IllegalStateException("widgets already added");
		}
		widgets.add(new Wrapper(w));
		restructure();
	}

	@Override
	public void clear() {

	}

	@Override
	public Iterator<Widget> iterator() {
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		return false;
	}

	@Override
	public void onResize() {
		splitPanel.onResize();
	}

}
