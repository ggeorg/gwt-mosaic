package gwt.mosaic.client.ui;

import gwt.mosaic.client.style.BoxModel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * A container that arranges field widgets in a form layout. Each field has an
 * optional text label associated with it and may be flagged as requiring
 * attension using one of several flag types and an optional flag message (for
 * use during form validation, for example).
 */
public class Form extends LayoutPanel {

	/**
	 * Class representing a form section. A section is a grouping of field
	 * widgets within a form.
	 */
	public static class Section extends Layer {
		private String heading = null;

		final Separator separator = new Separator();

		public Section() {
			Element elem = getElement();
			DOM.setStyleAttribute(elem, "margin", "0px");
			DOM.setStyleAttribute(elem, "border", "0px none");
			DOM.setStyleAttribute(elem, "padding", "0px");

			// !!! Very important in order to support rowSpan
			DOM.setStyleAttribute(elem, "overflow", "visible");

			super.add(separator);
		}

		public String getHeading() {
			return heading;
		}

		public void setHeading(String heading) {
			String previousHeading = this.heading;
			if (previousHeading != heading) {
				separator.setHeading(heading == null ? "" : heading);
				this.heading = heading;
				// if (form != null) {
				// fire heading changed event
				// }
			}
		}

		@Override
		public Widget getWidget(int index) {
			return getChildren().get(index + 1);
		}

		@Override
		public int getWidgetCount() {
			return getChildren().size() - 1;
		}

		@Override
		public boolean remove(Widget w) {
			if (w == separator) {
				throw new UnsupportedOperationException(
						"section's separator widget can't be removed");
			}
			return super.remove(w);
		}

		@Override
		public void add(Widget w) {
			if (!(w instanceof Form.Field)) {
				throw new IllegalArgumentException("widget is not a Form.Field");
			}
			super.add(w);
		}

		@Override
		public void insert(Widget w, int beforeIndex) {
			throw new UnsupportedOperationException(
					"use Section.add(Widget w) instead");
		}
	}

	/**
	 * Field represents a form's field element.
	 */
	public static class Field extends Layer {
		private String label = null;

		final Label labelWidget = new Label();

		public Field() {
			Element elem = getElement();
			DOM.setStyleAttribute(elem, "margin", "0px");
			DOM.setStyleAttribute(elem, "border", "0px none");
			DOM.setStyleAttribute(elem, "padding", "0px");

			// !!! Very important in order to support rowSpan
			DOM.setStyleAttribute(elem, "overflow", "visible");

			super.add(labelWidget);
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			String previousLabel = this.label;
			if (previousLabel != label) {
				labelWidget.setText(label == null ? "" : label);
				this.label = label;
				// if (form != null) {
				// fire heading changed event
				// }
			}
		}

		@Override
		public Widget getWidget(int index) {
			return getChildren().get(index + 1);
		}

		@Override
		public int getWidgetCount() {
			return getChildren().size() - 1;
		}

		@Override
		public boolean remove(Widget w) {
			if (w == labelWidget) {
				throw new UnsupportedOperationException(
						"field's label widget can't be removed");
			}
			return super.remove(w);
		}

		@Override
		public void add(Widget w) {
			if (getWidgetCount() == 1) {
				throw new UnsupportedOperationException(
						"only one widget can be added to field");
			}
			super.add(w);
		}

		@Override
		public void insert(Widget w, int beforeIndex) {
			throw new UnsupportedOperationException(
					"use Field.add(Widget w) instead");
		}
	}

	private int horizontalSpacing = 8;
	private int verticalSpacing = 8;
	private boolean fill;
	private boolean leftAlignLabels;

	public Form() {
		// do nothing
	}

	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}

	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public boolean isLeftAlignLabels() {
		return leftAlignLabels;
	}

	public void setLeftAlignLabels(boolean leftAlignLabels) {
		this.leftAlignLabels = leftAlignLabels;
	}

	@Override
	public void add(Widget w) {
		if (!(w instanceof Form.Section)) {
			throw new IllegalArgumentException("widget is not a Form.Section");
		}
		super.add(w);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		if (!(w instanceof Form.Section)) {
			throw new IllegalArgumentException("widget is not a Form.Section");
		}
		super.insert(w, beforeIndex);
	}

	@Override
	public int getPreferredWidth(int height) {
		int preferredWidth = 0;

		int maximumLabelWidth = 0;
		int maximumFieldWidth = 0;
		int maximumSeparatorWidth = 0;
		
		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		for (int sectionIndex = 0, sectionCount = getWidgetCount(); sectionIndex < sectionCount; sectionIndex++) {
			Form.Section section = (Form.Section) getWidget(sectionIndex);

			if (sectionIndex > 0 || section.getHeading() != null) {
				Separator separator = section.separator;
				maximumSeparatorWidth = Math.max(maximumSeparatorWidth,
						WidgetHelper.getPreferredWidth(separator, -1));
			}

			for (int fieldIndex = 0, fieldCount = section.getWidgetCount(); fieldIndex < fieldCount; fieldIndex++) {
				Form.Field fieldWidget = (Form.Field) section
						.getWidget(fieldIndex);
				Widget field = fieldWidget.getWidget(0);

				if (fieldWidget.isVisible()) {
					Label label = fieldWidget.labelWidget;
					maximumLabelWidth = Math.max(maximumLabelWidth,
							WidgetHelper.getPreferredWidth(label, -1));

					int fieldWidth = WidgetHelper.getPreferredWidth(field, -1);

					// TODO if(showFlagMessagesInline) {}

					maximumFieldWidth = Math.max(maximumFieldWidth, fieldWidth);
				}
			}
		}

		preferredWidth = maximumLabelWidth + horizontalSpacing
				+ maximumFieldWidth;

		// TODO if(showFlagIcons) {}

		preferredWidth = Math.max(maximumSeparatorWidth, preferredWidth
				+ boxModel.getWidthContribution());

		return preferredWidth;
	}

	@Override
	public int getPreferredHeight(int width) {
		int preferredHeight = 0;
		
		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		// Determine the field width constraint
		int fieldWidth = (fill && width != -1) ? getFieldWidth(width) : -1;

		for (int sectionIndex = 0, sectionCount = getWidgetCount(); sectionIndex < sectionCount; sectionIndex++) {
			Form.Section section = (Form.Section) getWidget(sectionIndex);

			if (sectionIndex > 0 || section.getHeading() != null) {
				Separator separator = section.separator;
				preferredHeight += WidgetHelper.getPreferredHeight(separator,
						width);
				preferredHeight += verticalSpacing;
			}

			for (int fieldIndex = 0, fieldCount = section.getWidgetCount(); fieldIndex < fieldCount; fieldIndex++) {
				Form.Field fieldWidget = (Form.Field) section
						.getWidget(fieldIndex);
				Widget field = fieldWidget.getWidget(0);

				if (fieldWidget.isVisible()) {
					// Determine the label size and baseline
					Label label = fieldWidget.labelWidget;
					Dimensions labelSize = WidgetHelper.getPreferredSize(label);
					int labelAscent = WidgetHelper.getBaseline(label,
							labelSize.width, labelSize.height);
					int labelDescent = labelSize.height - labelAscent;

					// Determine the field size and baseline
					Dimensions fieldSize;
					if (fill && fieldWidth != -1) {
						fieldSize = new Dimensions(fieldWidth,
								WidgetHelper.getPreferredHeight(field,
										fieldWidth));
					} else {
						fieldSize = WidgetHelper.getPreferredSize(field);
					}

					int fieldAscent = WidgetHelper.getBaseline(field,
							fieldSize.width, fieldSize.height);
					if (fieldAscent == -1) {
						fieldAscent = fieldSize.height;
					}

					int fieldDescent = fieldSize.height - fieldAscent;

					// Determine the row height
					int maximumAscent = Math.max(labelAscent, fieldAscent);
					int maximumDescent = Math.max(labelDescent, fieldDescent);
					int rowHeight = maximumAscent + maximumDescent;

					preferredHeight += rowHeight;

					if (fieldIndex > 0) {
						preferredHeight += verticalSpacing;
					}
				}
			}
		}

		preferredHeight += boxModel.getHeightContribution();

		return preferredHeight;
	}

	@Override
	public Dimensions getPreferredSize() {
		// TODO Optimizations
		return new Dimensions(getPreferredWidth(-1), getPreferredHeight(-1));
	}

	@Override
	public int getBaseline(int width, int height) {
		int baseline = -1;

		// Determine the field width constraint
		int fieldWidth = (fill) ? getFieldWidth(width) : -1;

		int sectionCount = getWidgetCount();
		int sectionIndex = 0;
		
		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		int rowY = 0;
		while (sectionIndex < sectionCount && baseline == -1) {
			Form.Section section = (Section) getWidget(sectionIndex);

			if (sectionIndex > 0 || section.getHeading() != null) {
				Separator separator = section.separator;
				rowY += WidgetHelper.getPreferredHeight(separator, width);
				rowY += verticalSpacing;
			}

			int fieldCount = section.getWidgetCount();
			int fieldIndex = 0;

			while (fieldIndex < fieldCount && baseline == -1) {
				Form.Field fieldWidget = (Form.Field) section
						.getWidget(fieldIndex);
				Widget field = fieldWidget.getWidget(0);

				if (fieldWidget.isVisible()) {
					// Determine the label size and baseline
					Label label = fieldWidget.labelWidget;
					Dimensions labelSize = label.getPreferredSize();
					int labelAscent = label.getBaseline(labelSize.width,
							labelSize.height);

					// Determine the field size and baseline
					Dimensions fieldSize;
					if (fill && fieldWidth != -1) {
						fieldSize = new Dimensions(fieldWidth,
								WidgetHelper.getPreferredHeight(field,
										fieldWidth));
					} else {
						fieldSize = WidgetHelper.getPreferredSize(field);
					}

					int fieldAscent = WidgetHelper.getBaseline(field,
							fieldSize.width, fieldSize.height);
					if (fieldAscent == -1) {
						fieldAscent = labelAscent;
					}

					// Determine the baseline
					int maximumAscent = Math.max(labelAscent, fieldAscent);
					baseline = rowY + maximumAscent;
				}

				fieldIndex++;
			}

			sectionIndex++;
		}

		baseline += boxModel.getPadding().top;

		return baseline;
	}

	private int getFieldWidth(int width) {
		int maximumLabelWidth = 0;
		int maximumFlagMessageWidth = 0;
		
		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		for (int sectionIndex = 0, sectionCount = getWidgetCount(); sectionIndex < sectionCount; sectionIndex++) {
			Form.Section section = (Form.Section) getWidget(sectionIndex);

			for (int fieldIndex = 0, fieldCount = section.getWidgetCount(); fieldIndex < fieldCount; fieldIndex++) {
				Form.Field fieldWidget = (Form.Field) section
						.getWidget(fieldIndex);
				// Widget field = fieldWidget.getWidget(0);

				if (fieldWidget.isVisible()) {
					Label label = fieldWidget.labelWidget;
					maximumLabelWidth = Math.max(maximumLabelWidth,
							WidgetHelper.getPreferredWidth(label, -1));

					// TODO if (showFlagMessagesInline) {}
				}
			}
		}

		int fieldWidth = Math.max(0, width
				- (maximumLabelWidth + horizontalSpacing));

		// TODO if (showFlagIcons) {}

		// TODO if (showFlagMessagesInline) {}

		fieldWidth = Math.max(0,
				fieldWidth - boxModel.getPaddingWidthContribution());

		return fieldWidth;
	}

	@Override
	protected void doLayout() {
		// Determine the maximum label and flag message width
		int maximumLabelWidth = 0;
		int maximumFlagMessageWidth = 0;
		
		BoxModel boxModel = WidgetHelper.getBoxModel(this);

		for (int sectionIndex = 0, sectionCount = getWidgetCount(); sectionIndex < sectionCount; sectionIndex++) {
			Form.Section section = (Form.Section) getWidget(sectionIndex);

			for (int fieldIndex = 0, fieldCount = section.getWidgetCount(); fieldIndex < fieldCount; fieldIndex++) {
				Form.Field fieldWidget = (Form.Field) section
						.getWidget(fieldIndex);
				// Widget field = fieldWidget.getWidget(0);

				if (fieldWidget.isVisible()) {
					Label label = fieldWidget.labelWidget;
					maximumLabelWidth = Math.max(maximumLabelWidth,
							WidgetHelper.getPreferredWidth(label, -1));

					// TODO if (showFlagMessagesInline) {}
				}
			}
		}

		// Determine the field width
		int width = getElement().getClientWidth();
		int fieldWidth = Math.max(0, width
				- (maximumLabelWidth + horizontalSpacing));

		// TODO if (showFlagIcons) {}

		// TODO if (showFlagMessagesInline) {}

		fieldWidth = Math.max(0,
				fieldWidth - boxModel.getPaddingWidthContribution());

		// Lay out the components
		int rowY = boxModel.getPadding().top;

		for (int sectionIndex = 0, sectionCount = getWidgetCount(); sectionIndex < sectionCount; sectionIndex++) {
			Form.Section section = (Form.Section) getWidget(sectionIndex);

			WidgetHelper.setSize(section, width, 1);
			WidgetHelper.setLocation(section, 0, 0);

			Separator separator = section.separator;
			if (sectionIndex > 0 || section.getHeading() != null) {
				int separatorWidth = Math.max(
						width - boxModel.getPaddingWidthContribution(), 0);
				separator.setVisible(true);
				int separatorHeight = WidgetHelper.getPreferredHeight(
						separator, separatorWidth);
				WidgetHelper
						.setSize(separator, separatorWidth, separatorHeight);
				WidgetHelper.setLocation(separator, boxModel.getPadding().left,
						rowY);
				rowY += separatorHeight;
			} else {
				separator.setVisible(false);
			}

			for (int fieldIndex = 0, fieldCount = section.getWidgetCount(); fieldIndex < fieldCount; fieldIndex++) {
				Form.Field fieldWidget = (Form.Field) section.getWidget(fieldIndex);
				Widget field = fieldWidget.getWidget(0);

				Label label = fieldWidget.labelWidget;

				WidgetHelper.setSize(fieldWidget, width, 1);
				WidgetHelper.setLocation(fieldWidget, 0, 0);

				if (fieldWidget.isVisible()) {
					// Show the label
					label.setVisible(true);

					// Determine the label size and baseline
					Dimensions labelSize = label.getPreferredSize();

					WidgetHelper.setSize(label, labelSize.width,
							labelSize.height);
					int labelAscent = label.getBaseline(labelSize.width,
							labelSize.height);
					int labelDescent = labelSize.height - labelAscent;

					// Determine the field size and baseline
					Dimensions fieldSize;
					if (fill) {
						fieldSize = new Dimensions(fieldWidth,
								WidgetHelper.getPreferredHeight(field,
										fieldWidth));
					} else {
						fieldSize = WidgetHelper.getPreferredSize(field);
					}

					WidgetHelper.setSize(field, fieldSize.width,
							fieldSize.height);

					int fieldAscent = WidgetHelper.getBaseline(field,
							fieldSize.width, fieldSize.height);
					if (fieldAscent == -1) {
						fieldAscent = labelAscent;
					}

					int fieldDescent = fieldSize.height - fieldAscent;

					// Determine the baseline and row height
					int maximumAscent = Math.max(labelAscent, fieldAscent);
					int maximumDescent = Math.max(labelDescent, fieldDescent);

					int baseline = maximumAscent;
					int rowHeight = maximumAscent + maximumDescent;

					// Position the label
					int labelX = boxModel.getPadding().left;
					if (!leftAlignLabels) {
						labelX += maximumLabelWidth - labelSize.width;
					}

					// TODO if (showFlagIcons) {}

					int labelY = rowY + (baseline - labelAscent);
					WidgetHelper.setLocation(label, labelX, labelY);

					// Position the field
					int fieldX = boxModel.getPadding().left + maximumLabelWidth
							+ horizontalSpacing;
					// TODO if (showFlagIcons) {}

					int fieldY = rowY + (baseline - fieldAscent);
					WidgetHelper.setLocation(field, fieldX, fieldY);

					// Update the row y-coordinate
					rowY += rowHeight + verticalSpacing;
				}
			}
		}
	}

}
