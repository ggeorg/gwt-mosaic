package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import com.google.gwt.user.client.ui.Widget;

/**
 * Container that behaves like a stack of transparencies, all of which are
 * visible at the same time.
 */
public class Stack extends LayoutPanel {

	@Override
	public int getPreferredWidth(int clientHeight) {

		if (clientHeight == -1) {
			// First, check if preferredWidth is cached
			if (isValid() && (preferredWidth != -1)) {
				return preferredWidth;
			}

			// then check if there is a fixed preferredWidth hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredWidth() != null) {
				return WidgetHelper.getPreferredWidthImpl(this, clientHeight);
			}
		}

		int preferredWidth = 0;

		for (Widget child : getChildren()) {
			if (child.isVisible()) {
				preferredWidth = Math.max(preferredWidth,
						WidgetHelper.getPreferredWidth(child, clientHeight));
			}
		}

		preferredWidth += WidgetHelper.getBoxModel(this).getWidthContribution();

		return (this.preferredWidth = preferredWidth);
	}

	@Override
	public int getPreferredHeight(int clientWidth) {

		if (clientWidth == -1) {
			// First, check if preferredHeight is cached
			if ((clientWidth == -1) && isValid() && (preferredHeight != -1)) {
				return preferredHeight;
			}

			// then check if there is a fixed preferredHeight hint stored in
			// layoutData
			LayoutData layoutData = WidgetHelper.getLayoutData(this);
			if (layoutData.getPreferredHeight() != null) {
				return WidgetHelper.getPreferredHeightImpl(this, clientWidth);
			}
		}

		int preferredHeight = 0;

		for (Widget child : getChildren()) {
			preferredHeight = Math.max(preferredHeight,
					WidgetHelper.getPreferredHeight(child, clientWidth));
		}

		preferredHeight += WidgetHelper.getBoxModel(this)
				.getWidthContribution();

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

		for (Widget child : getChildren()) {
			if (child.isVisible()) {
				Dimensions preferredCardSize = WidgetHelper
						.getPreferredSize(child);

				preferredWidth = Math.max(preferredWidth,
						preferredCardSize.width);

				preferredHeight = Math.max(preferredHeight,
						preferredCardSize.height);
			}
		}

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		preferredWidth += boxModel.getWidthContribution();
		preferredHeight += boxModel.getHeightContribution();

		return (this.preferredSize = new Dimensions(preferredWidth,
				preferredHeight));
	}

	@Override
	public int getBaseline(int width, int height) {
		return -1;
	}

	@Override
	protected void doLayout() {
		// Set the size of all widgets to match the size of the stack pane,
		// minus padding

		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int width = Math.max(
				getElement().getOffsetWidth()
						- boxModel.getPaddingWidthContribution(), 0);
		int height = Math.max(
				getElement().getClientHeight()
						- boxModel.getPaddingHeightContribution(), 0);

		int left = boxModel.getPadding().left;
		int top = boxModel.getPadding().top;
		for (Widget component : getChildren()) {
			if (component.isVisible()) {
				WidgetHelper.setLocation(component, left, top);
				WidgetHelper.setSize(component, width, height);
			}
		}
	}

}
