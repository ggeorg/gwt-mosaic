package gwt.mosaic.client.ui;

import gwt.mosaic.client.events.HasFillChangeHandlers;
import gwt.mosaic.client.events.HasHorizontalAlignmentChangeHandlers;
import gwt.mosaic.client.events.HasOrientationChangeHandlers;
import gwt.mosaic.client.events.HasSpacingChangeHandlers;
import gwt.mosaic.client.events.HasVerticalAlignmentChangeHandlers;
import gwt.mosaic.client.events.PropertyChangeEvent;
import gwt.mosaic.client.events.PropertyChangeHandler;
import gwt.mosaic.client.style.BoxModel;
import gwt.mosaic.shared.Bean;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Widget;

/**
 * Container that arranges components in a line, either vertically or
 * horizontally.
 */
@Bean
public class Box extends LayoutPanel implements HasOrientationChangeHandlers,
		HasHorizontalAlignmentChangeHandlers,
		HasVerticalAlignmentChangeHandlers, HasSpacingChangeHandlers,
		HasFillChangeHandlers {

	private Orientation orientation;

	// skin --------------------------------------------------------------------
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;

	private int spacing = 4;
	private boolean fill = false;

	// -------------------------------------------------------------------------

	public Box() {
		this(Orientation.HORIZONTAL);
	}

	@Override
	public HandlerRegistration addPropertyChangeHandler(
			PropertyChangeHandler handler) {
		return addHandler(handler, PropertyChangeEvent.getType());
	}

	protected Box(Orientation orientation) {
		this.orientation = orientation;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		if (this.orientation != orientation) {
			this.orientation = orientation;
			PropertyChangeEvent.fire(this, "orientation");
			invalidate();
		}
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		if (this.fill != fill) {
			this.fill = fill;
			invalidate();
		}
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		if (this.spacing != spacing) {
			this.spacing = spacing;
			invalidate();
		}
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		if (this.horizontalAlignment != horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
			invalidate();
		}
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		if (this.verticalAlignment != verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
			invalidate();
		}
	}

	@Override
	public int getPreferredWidth(int clientHeight) {

		if (clientHeight == -1) {
			// First, check if preferredWidth is cached
			if (isValid() && (preferredWidth != -1)) {
				return preferredWidth;
			}
		}
			// then check if there is a fixed preferredWidth hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredWidth() != null) {
				return WidgetHelper.getPreferredWidthImpl(this, clientHeight);
			}
		

		int preferredWidth = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		if (orientation == Orientation.HORIZONTAL) {
			// Include padding in constraint
			if (clientHeight != -1) {
				clientHeight = Math.max(
						clientHeight - (boxModel.getHeightContribution()), 0);
			}

			// Preferred width is the sum of the preferred widths of all widgets
			int j = 0;
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					preferredWidth += WidgetHelper.getPreferredWidth(child,
							fill ? clientHeight : -1);
					++j;
				}

				// Include spacing
				if (j > 1) {
					preferredWidth += spacing * (j - 1);
				}
			}
		} else {
			// Preferred width is the maximum preferred width of all widgets
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					preferredWidth = Math.max(preferredWidth,
							WidgetHelper.getPreferredWidth(child, -1));
				}
			}
		}

		// Include left and right margin+border+padding values
		preferredWidth += boxModel.getWidthContribution();

		return (this.preferredWidth = preferredWidth);
	}

	@Override
	public int getPreferredHeight(int clientWidth) {

		if (clientWidth == -1) {
			// First, check if preferredHeight is cached
			if (isValid() && (preferredHeight != -1)) {
				return preferredHeight;
			}
		}
			// then check if there is a fixed preferredHeight hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredHeight() != null) {
				return WidgetHelper.getPreferredHeightImpl(this, clientWidth);
			}
		

		int preferredHeight = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		if (orientation == Orientation.HORIZONTAL) {
			// Preferred height is the maximum preferred height of all widget
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					preferredHeight = Math.max(preferredHeight,
							WidgetHelper.getPreferredHeight(child, -1));
				}
			}
		} else {
			// Include padding in constraint
			if (clientWidth != -1) {
				clientWidth = Math
						.max(clientWidth
								- boxModel.getPaddingWidthContribution(), 0);
			}

			// Preferred height is the sum of the preferred heights of all
			// widget
			int j = 0;
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					preferredHeight += WidgetHelper.getPreferredHeight(child,
							fill ? clientWidth : -1);
					++j;
				}
			}

			// Include spacing
			if (j > 1) {
				preferredHeight += spacing * (j - 1);
			}
		}

		// Include top and bottom margin+border+padding values
		preferredHeight += boxModel.getHeightContribution();

		return (this.preferredHeight = preferredHeight);
	}

	@Override
	public Dimensions getPreferredSize() {

		// First, check if preferred size is cached
		if (isValid() && (preferredSize != null)) {
			return preferredSize;
		}

		// then check if there is a fixed preferredWidth or preferredHeight hint
		// stored in layoutData
		LayoutData layoutData = WidgetHelper.getLayoutData(this);
		String widthHint = layoutData.getPreferredWidth();
		String heightHint = layoutData.getPreferredHeight();
		if (widthHint != null || heightHint != null) {
			return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
		}

		int preferredWidth = 0;
		int preferredHeight = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		switch (orientation) {
		case HORIZONTAL: {
			// Preferred width is the sum of the preferred widths of all widgets
			int j = 0;
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					Dimensions preferredSize = WidgetHelper
							.getPreferredSize(child);
					preferredWidth += preferredSize.getWidth();
					preferredHeight = Math.max(preferredSize.getHeight(),
							preferredHeight);
					++j;
				}
			}

			// Include spacing
			if (j > 1) {
				preferredWidth += spacing * (j - 1);
			}

			break;
		}

		case VERTICAL: {
			// Preferred height is the sum of the preferred heights of all
			// widgets
			int j = 0;
			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					Dimensions preferredSize = WidgetHelper
							.getPreferredSize(child);
					preferredWidth = Math.max(preferredSize.getWidth(),
							preferredWidth);
					preferredHeight += preferredSize.getHeight();
					++j;
				}
			}

			// Include spacing
			if (j > 1) {
				preferredHeight += spacing * (j - 1);
			}

			break;
		}
		}

		// Include margin+border+padding
		preferredWidth += boxModel.getWidthContribution();
		preferredHeight += boxModel.getHeightContribution();

		return (preferredSize = new Dimensions(preferredWidth, preferredHeight));
	}

	@Override
	public int getBaseline(int width, int height) {
		int baseline = -1;
		int contentHeight = 0;

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		Insets padding = boxModel.getPadding(); // XXX

		switch (orientation) {
		case HORIZONTAL: {
			if (fill) {
				int clientHeight = Math.max(height
						- (padding.top + padding.bottom), 0);

				for (Widget child : getChildren()) {
					if (child.isVisible()) {
						int componentWidth = WidgetHelper.getPreferredWidth(
								child, clientHeight);
						baseline = Math.max(baseline, WidgetHelper.getBaseline(
								child, componentWidth, clientHeight));
					}
				}
			} else {
				contentHeight = 0;
				for (Widget child : getChildren()) {
					if (child.isVisible()) {
						contentHeight = Math.max(contentHeight,
								WidgetHelper.getPreferredHeight(child, -1));
					}
				}

				for (Widget child : getChildren()) {
					if (child.isVisible()) {
						Dimensions size = WidgetHelper.getPreferredSize(child);

						int componentBaseline = WidgetHelper.getBaseline(child,
								size.getWidth(), size.getHeight());

						if (componentBaseline != -1) {
							switch (verticalAlignment) {
							case MIDDLE: {
								componentBaseline += (contentHeight - size
										.getHeight()) / 2;
								break;
							}

							case BOTTOM: {
								componentBaseline += contentHeight
										- size.getHeight();
								break;
							}
							}
						}

						baseline = Math.max(baseline, componentBaseline);
					}
				}
			}

			break;
		}

		case VERTICAL: {
			int clientWidth = Math.max(width - (padding.left + padding.right),
					0);

			for (Widget child : getChildren()) {
				if (child.isVisible()) {
					Dimensions size;
					if (fill) {
						size = new Dimensions(clientWidth,
								WidgetHelper.getPreferredHeight(child,
										clientWidth));
					} else {
						size = WidgetHelper.getPreferredSize(child);
					}

					if (baseline == -1) {
						baseline = WidgetHelper.getBaseline(child,
								size.getWidth(), size.getHeight());
						if (baseline != -1) {
							baseline += contentHeight;
						}
					}

					contentHeight += size.getHeight() + spacing;
				}
			}

			contentHeight -= spacing;

			break;
		}
		}

		if (baseline != -1) {
			if (fill) {
				baseline += padding.top;
			} else {
				switch (verticalAlignment) {
				case TOP: {
					baseline += padding.top;
					break;
				}

				case MIDDLE: {
					baseline += (height - contentHeight) / 2;
					break;
				}

				case BOTTOM: {
					baseline += height - (contentHeight + padding.bottom);
					break;
				}
				}
			}
		}

		return baseline;
	}

	@Override
	protected void doLayout() {
		if (orientation == Orientation.HORIZONTAL) {
			doHorizontalLayout();
		} else {
			doVerticalLayout();
		}
	}

	private void doHorizontalLayout() {
		int n = getWidgetCount();

		int width = getElement().getOffsetWidth();
		int height = getElement().getClientHeight();

		//int preferredWidth = getPreferredWidth(fill ? height : -1);
		//width = Math.max(width, preferredWidth);

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int totalRelativeWeight = 0;
		int totalRelativeWidth = width - boxModel.getPaddingWidthContribution();

		int[] widgetHeights = new int[n];
		int[] widgetWidths = new int[n];

		for (int i = 0; i < n; i++) {
			Widget child = getWidget(i);
			if (child.isVisible()) {
				int weight = WidgetHelper.getWeight(child);
				if (weight > 0) {
					totalRelativeWeight += weight;
				}
				if (fill) {
					int widgetHeight = Math.max(
							height - (boxModel.getPaddingHeightContribution()),
							0);
					widgetHeights[i] = widgetHeight;
					if (weight == 0) {
						widgetWidths[i] = WidgetHelper.getPreferredWidth(child,
								widgetHeight);
						totalRelativeWidth -= widgetWidths[i] + 2 * spacing;
					}
				} else {
					if (weight == 0) {
						Dimensions preferredComponentSize = WidgetHelper
								.getPreferredSize(child);
						widgetWidths[i] = preferredComponentSize.getWidth();
						widgetHeights[i] = preferredComponentSize.getHeight();
						totalRelativeWidth -= widgetWidths[i] + 2 * spacing;
					} else {
						widgetHeights[i] = WidgetHelper.getPreferredHeight(
								child, -1);
					}
				}
			}
		}

		// Determine the starting x-coordinate
		int x = 0;

		if (totalRelativeWeight == 0) {
			switch (horizontalAlignment) {
			case CENTER:
				x = (width - preferredWidth) / 2;
				break;
			case RIGHT:
				x = width - preferredWidth;
				break;
			case START:
				if (LocaleInfo.getCurrentLocale().isRTL()) {
					x = width - preferredWidth;
				}
				break;
			case END:
				if (!LocaleInfo.getCurrentLocale().isRTL()) {
					x = width - preferredWidth;
				}
				break;
			}
		}

		x += boxModel.getPadding().left;

		// Lay out the widgets
		for (int i = 0; i < n; i++) {
			Widget child = getWidget(i);

			if (child.isVisible()) {
				int widgetWidth = 0;
				int widgetHeight = 0;
				int y = 0;

				int weight = WidgetHelper.getWeight(child);
				if (weight == 0) {
					widgetWidth = widgetWidths[i];
				} else {
					widgetWidth = (int) (0.5 + totalRelativeWidth
							* (double) weight / (double) totalRelativeWeight);
				}
				widgetHeight = widgetHeights[i];

				if (!fill) {
					switch (verticalAlignment) {
					case TOP:
						y = boxModel.getPadding().top;
						break;
					case MIDDLE:
						y = (height - widgetHeight) / 2;
						break;
					case BOTTOM:
						y = height - boxModel.getPadding().bottom
								- widgetHeight;
						break;
					}
				}

				// Set the component's size and position
				WidgetHelper.setSize(child, widgetWidth, widgetHeight);
				WidgetHelper.setLocation(child, x, y);

				// Increment the x-coordinate
				x += widgetWidth + spacing;
			}
		}
	}

	private void doVerticalLayout() {
		int n = getWidgetCount();

		int width = getElement().getClientWidth();
		int height = getElement().getClientHeight();

		int preferredHeight = getPreferredHeight(fill ? width : -1);
		height = Math.max(height, preferredHeight);

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int totalRelativeWeight = 0;
		int totalRelativeHeight = height
				- boxModel.getPaddingHeightContribution();

		int[] widgetHeights = new int[n];
		int[] widgetWidths = new int[n];

		for (int i = 0; i < n; i++) {
			Widget child = getWidget(i);
			if (child.isVisible()) {
				int weight = WidgetHelper.getWeight(child);
				if (weight > 0) {
					totalRelativeWeight += weight;
				}
				if (fill) {
					int widgetWidth = Math.max(
							width - boxModel.getPaddingWidthContribution(), 0);
					widgetWidths[i] = widgetWidth;
					if (weight == 0) {
						widgetHeights[i] = WidgetHelper.getPreferredHeight(
								child, widgetWidth);
						totalRelativeHeight -= widgetHeights[i] + 2 * spacing;
					}
				} else {
					if (weight == 0) {
						Dimensions preferredComponentSize = WidgetHelper
								.getPreferredSize(child);
						widgetWidths[i] = preferredComponentSize.getWidth();
						widgetHeights[i] = preferredComponentSize.getHeight();
						totalRelativeHeight -= widgetHeights[i] + 2 * spacing;
					} else {
						widgetWidths[i] = WidgetHelper.getPreferredWidth(child,
								-1);
					}
				}
			}
		}

		// Determine the starting y-coordinate
		int y = 0;

		if (totalRelativeWeight == 0) {
			switch (verticalAlignment) {
			case MIDDLE:
				y = (height - preferredHeight) / 2;
				break;
			case BOTTOM:
				y = height - preferredHeight;
				break;
			}
		}

		y += boxModel.getPadding().top;

		// Lay out the widgets
		for (int i = 0; i < n; i++) {
			Widget child = getWidget(i);

			if (child.isVisible()) {
				int widgetWidth = 0;
				int widgetHeight = 0;
				int x = 0;

				int weight = WidgetHelper.getWeight(child);
				if (weight == 0) {
					widgetHeight = widgetHeights[i];
				} else {
					widgetHeight = (int) (0.5 + totalRelativeHeight
							* (double) weight / (double) totalRelativeWeight);
				}
				widgetWidth = widgetWidths[i];

				if (!fill) {
					switch (horizontalAlignment) {
					case LEFT:
						x = boxModel.getPadding().left;
						break;
					case CENTER:
						x = (width - widgetWidth) / 2;
						break;
					case RIGHT:
						x = width - boxModel.getPadding().right - widgetWidth;
						break;
					case START:
						if (LocaleInfo.getCurrentLocale().isRTL()) {
							x = width - boxModel.getPadding().right
									- widgetWidth;
						} else {
							x = boxModel.getPadding().left;
						}
						break;
					case END:
						if (!LocaleInfo.getCurrentLocale().isRTL()) {
							x = boxModel.getPadding().left;
						} else {
							x = width - boxModel.getPadding().right
									- widgetWidth;
							break;
						}
					}
				}

				// Set the component's size and position
				WidgetHelper.setSize(child, widgetWidth, widgetHeight);
				WidgetHelper.setLocation(child, x, y);

				// Increment the y-coordinate
				y += widgetHeight + spacing;
			}
		}
	}

}
