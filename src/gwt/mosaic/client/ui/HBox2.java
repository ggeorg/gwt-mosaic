package gwt.mosaic.client.ui;

import gwt.mosaic.client.util.ImmutableIterator;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class HBox2 extends Composite implements HasWidgets, RequiresResize,
		ProvidesResize {

	class Cell extends SimplePanel implements RequiresResize, ProvidesResize {
		public Cell(Widget w) {
			super();
			setWidget(w);
			setStyleName(style.cell());
		}

		@Override
		public void onResize() {
			int index = widgets.indexOf(getWidget());
			Element elem = getElement();
			Element col = colgroup.getChild(index).cast();
			if (WidgetHelper.getWeight(getWidget()) > 0) {
				WidgetHelper.setSize(getWidget(), col.getClientWidth(),
						elem.getClientHeight());
			} else {
				WidgetHelper.setSize(getWidget(), -1, elem.getClientHeight());
			}
		}
	}

	interface MyUiBinder extends UiBinder<Widget, HBox2> {
	}

	interface MyStyle extends CssResource {
		String cell();
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	MyStyle style;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	HorizontalPanel hPanel;

	private final ArrayList<Widget> widgets = new ArrayList<Widget>();

	private final Element colgroup;

	// skin --------------------------------------------------------------------
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	private boolean horizontalAlignmentChanged = true;

	private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
	private boolean verticalAlignmentChanged = true;

	private boolean fill = false;
	private int spacing;

	// -------------------------------------------------------------------------

	public HBox2() {
		initWidget(uiBinder.createAndBindUi(this));
		hPanel.setBorderWidth(1);

		TableElement table = hPanel.getElement().cast();
		table.insertFirst(colgroup = DOM.createElement("colgroup"));

		setSpacing(4);
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		if (this.horizontalAlignment != horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
			this.horizontalAlignmentChanged = true;
			layout();
		}
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		if (this.verticalAlignment != verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
			this.verticalAlignmentChanged = true;
			layout();
		}
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
		layout();
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		if (spacing < 0) {
			throw new IllegalArgumentException("spacing can't be negative");
		}
		if (this.spacing != spacing) {
			hPanel.setSpacing(this.spacing = spacing);
			layout();
		}
	}

	@Override
	public void add(Widget w) {
		widgets.add(w);
		hPanel.add(new Cell(w));

		Element col = DOM.createElement("col");
		colgroup.appendChild(col);
	}

	@Override
	public void clear() {
		widgets.clear();
		hPanel.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return new ImmutableIterator<Widget>(widgets.iterator());
	}

	@Override
	public boolean remove(Widget w) {
		if (hPanel.remove(w)) {
			if (!widgets.remove(w)) {
				throw new IllegalStateException();
			}
			return true;
		}
		return false;
	}

	@Override
	public void onResize() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				layout();
			}
		});
	}

	@Override
	public void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				layout();
			}
		});
	}
	int totalRelativeWeight ;
	private void layout() {
		try {
			Element elem = scrollPanel.getElement();
			WidgetHelper.setSize(hPanel, -1, elem.getClientHeight());

			if (fill) {
				for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
					Cell cell = (Cell) hPanel.getWidget(i);
					WidgetHelper.setSize(cell, -1, elem.getClientHeight()
							- (2 + 2 * spacing));
				}
			} else if (verticalAlignmentChanged) {
				switch (verticalAlignment) {
				case TOP:
					for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
						Cell cell = (Cell) hPanel.getWidget(i);
						hPanel.setCellVerticalAlignment(cell,
								HasVerticalAlignment.ALIGN_TOP);
					}
					break;
				case MIDDLE:
					for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
						Cell cell = (Cell) hPanel.getWidget(i);
						hPanel.setCellVerticalAlignment(cell,
								HasVerticalAlignment.ALIGN_MIDDLE);
					}
					break;
				case BOTTOM:
					for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
						Cell cell = (Cell) hPanel.getWidget(i);
						hPanel.setCellVerticalAlignment(cell,
								HasVerticalAlignment.ALIGN_BOTTOM);
					}
					break;
				}
				this.verticalAlignmentChanged = false;
			}

			int[] relativeWeights = new int[widgets.size()];
			 totalRelativeWeight = 0;
			for (int i = 0, n = widgets.size(); i < n; i++) {
				Widget w = widgets.get(i);
				relativeWeights[i] = WidgetHelper.getWeight(w);
				totalRelativeWeight += relativeWeights[i];
			}

			if (totalRelativeWeight > 0) {

				hPanel.setWidth("100%");
				for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
					Cell cell = (Cell) hPanel.getWidget(i);
					Element col = colgroup.getChild(i).cast();
					if (relativeWeights[i] == 0) {
						DOM.setStyleAttribute(
								col,
								"width",
								WidgetHelper.getPreferredWidth(
										cell.getWidget(), -1) + "px");
						hPanel.setCellWidth(
								cell,
								WidgetHelper.getPreferredWidth(
										cell.getWidget(), -1) + "px");
						WidgetHelper.setSize(cell.getWidget(), col.getClientWidth(), -1);
					} else {
						int width = (int) (100.0 * relativeWeights[i] / totalRelativeWeight);
						DOM.setStyleAttribute(col, "width", width + "px");
						hPanel.setCellWidth(cell, width + "%");
						WidgetHelper.setSize(cell.getWidget(), col.getClientWidth(), -1);
					}
				}

			} else if (horizontalAlignmentChanged) {

				hPanel.setWidth("auto");
				for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
					Cell cell = (Cell) hPanel.getWidget(i);
					hPanel.setCellWidth(cell, "auto");
				}

				switch (horizontalAlignment) {
				case LEFT:
					DOM.setElementProperty(scrollPanel.getElement(), "align",
							"left");
					break;
				case CENTER:
					DOM.setElementProperty(scrollPanel.getElement(), "align",
							"center");
					break;
				case RIGHT:
					DOM.setElementProperty(scrollPanel.getElement(), "align",
							"right");
					break;
				case START:
					if (LocaleInfo.getCurrentLocale().isRTL()) {
						DOM.setElementProperty(scrollPanel.getElement(),
								"align", "right");
					} else {
						DOM.setElementProperty(scrollPanel.getElement(),
								"align", "left");
					}
					break;
				case END:
					if (!LocaleInfo.getCurrentLocale().isRTL()) {
						DOM.setElementProperty(scrollPanel.getElement(),
								"align", "right");
					} else {
						DOM.setElementProperty(scrollPanel.getElement(),
								"align", "left");
					}
					break;
				}
			}

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
			if (fill || (totalRelativeWeight > 0)) {
				for (int i = 0, n = hPanel.getWidgetCount(); i < n; i++) {
					Cell cell = (Cell) hPanel.getWidget(i);
					cell.getWidget().setSize("auto", "auto");
					cell.onResize();
				}
			}}});

		} catch (Exception e) {
			Window.alert(e.toString());
		}
	}
}
