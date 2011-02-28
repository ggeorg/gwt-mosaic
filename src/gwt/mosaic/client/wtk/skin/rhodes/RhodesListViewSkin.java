/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk.skin.rhodes;

import gwt.mosaic.client.collections.ArrayList;
import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.collections.List;
import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.util.Filter;
import gwt.mosaic.client.wtk.Bounds;
import gwt.mosaic.client.wtk.Component;
import gwt.mosaic.client.wtk.Font;
import gwt.mosaic.client.wtk.Insets;
import gwt.mosaic.client.wtk.Keyboard;
import gwt.mosaic.client.wtk.Keyboard.KeyCode;
import gwt.mosaic.client.wtk.Keyboard.Modifier;
import gwt.mosaic.client.wtk.ListView;
import gwt.mosaic.client.wtk.ListView.SelectMode;
import gwt.mosaic.client.wtk.ListViewItemListener;
import gwt.mosaic.client.wtk.ListViewItemStateListener;
import gwt.mosaic.client.wtk.ListViewListener;
import gwt.mosaic.client.wtk.ListViewSelectionListener;
import gwt.mosaic.client.wtk.Mouse;
import gwt.mosaic.client.wtk.Span;
import gwt.mosaic.client.wtk.Theme;
import gwt.mosaic.client.wtk.skin.ComponentSkin;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * List view skin.
 */
public class RhodesListViewSkin extends ComponentSkin implements ListView.Skin,
		ListViewListener, ListViewItemListener, ListViewItemStateListener,
		ListViewSelectionListener {

	public interface UI extends IsWidget {
		void setPresender(Component presender);

		void setInnerHTML(String string);
	}

	private Font font;
	private Color color;
	private Color disabledColor;
	private Color backgroundColor;
	private Color selectionColor;
	private Color selectionBackgroundColor;
	private Color inactiveSelectionColor;
	private Color inactiveSelectionBackgroundColor;
	private Color highlightBackgroundColor;
	private Color alternateItemBackgroundColor;
	private boolean showHighlight;
	private boolean variableItemHeight;
	private Insets checkboxPadding = new Insets(2, 2, 2, 0);

	private int highlightIndex = -1;
	private int selectIndex = -1;

	private ArrayList<Integer> itemBoundaries = null;
	private int fixedItemHeight;

	private boolean validateSelection = false;

	// private static final Checkbox CHECKBOX = new Checkbox();

	// static {
	// CHECKBOX.setSize(CHECKBOX.getPreferredSize());
	// }

	private UI ui = null;

	public RhodesListViewSkin() {
		Theme theme = Theme.getTheme();
		font = theme.getFont();
		color = theme.getColor(Theme.WINDOW_TEXT_COLOR);
		disabledColor = theme.getColor(7);
		backgroundColor = theme.getColor(4);
		selectionColor = theme.getColor(4);
		selectionBackgroundColor = theme.getColor(14);
		inactiveSelectionColor = theme.getColor(1);
		inactiveSelectionBackgroundColor = theme.getColor(9);
		highlightBackgroundColor = theme.getColor(10);
		alternateItemBackgroundColor = null;
		showHighlight = true;
	}

	@Override
	public void install(Component component) {
		super.install(component);

		ListView listView = (ListView) component;
		listView.getListViewListeners().add(this);
		listView.getListViewItemListeners().add(this);
		listView.getListViewItemStateListeners().add(this);
		listView.getListViewSelectionListeners().add(this);
	}

	@Override
	public Widget getWidget() {
		if (ui == null) {
			ui = GWT.create(UI.class);
			ui.setPresender(getComponent());
			ui.asWidget().addStyleName("m-ListView");
		}
		return ui.asWidget();
	}

	@Override
	@SuppressWarnings("unchecked")
	public int getPreferredWidth(int height) {
		int preferredWidth = 0;

		ListView listView = (ListView) getComponent();
		List<Object> listData = (List<Object>) listView.getListData();

		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();

		int index = 0;
		for (Object item : listData) {
			itemRenderer.render(item, index++, listView, false, false, false,
					false);
			preferredWidth = Math.max(preferredWidth,
					itemRenderer.getPreferredWidth(-1));
		}

		if (listView.getCheckmarksEnabled()) {
			throw new UnsupportedOperationException();
			// preferredWidth += CHECKBOX.getWidth() + (checkboxPadding.left
			// + checkboxPadding.right);
		}

		return preferredWidth;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int getPreferredHeight(int width) {
		int preferredHeight = 0;

		ListView listView = (ListView) getComponent();
		List<Object> listData = (List<Object>) listView.getListData();
		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();

		if (variableItemHeight) {
			int clientWidth = width;
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// clientWidth = Math
				// .max(clientWidth
				// - (CHECKBOX.getWidth() + (checkboxPadding.left +
				// checkboxPadding.right)),
				// 0);
			}

			int index = 0;
			for (Object item : listData) {
				itemRenderer.render(item, index++, listView, false, false,
						false, false);
				preferredHeight += itemRenderer.getPreferredHeight(clientWidth);
			}
		} else {
			itemRenderer.render(null, -1, listView, false, false, false, false);

			int fixedItemHeight = itemRenderer.getPreferredHeight(-1);
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// fixedItemHeight = Math.max(CHECKBOX.getHeight()
				// + (checkboxPadding.top + checkboxPadding.bottom),
				// fixedItemHeight);
			}

			preferredHeight = listData.getLength() * fixedItemHeight;
		}

		return preferredHeight;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int getBaseline(int width, int height) {
		ListView listView = (ListView) getComponent();

		int baseline = -1;

		int clientWidth = width;
		if (listView.getCheckmarksEnabled()) {
			throw new UnsupportedOperationException();
			// clientWidth = Math
			// .max(clientWidth
			// - (CHECKBOX.getWidth() + (checkboxPadding.left +
			// checkboxPadding.right)),
			// 0);
		}

		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();
		List<Object> listData = (List<Object>) listView.getListData();
		if (variableItemHeight && listData.getLength() > 0) {
			itemRenderer.render(listData.get(0), 0, listView, false, false,
					false, false);
			int itemHeight = itemRenderer.getPreferredHeight(clientWidth);
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// itemHeight = Math.max(CHECKBOX.getHeight()
				// + (checkboxPadding.top + checkboxPadding.bottom),
				// itemHeight);
			}

			baseline = itemRenderer.getBaseline(clientWidth, itemHeight);
		} else {
			itemRenderer.render(null, -1, listView, false, false, false, false);

			int fixedItemHeight = itemRenderer.getPreferredHeight(-1);
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// fixedItemHeight = Math.max(CHECKBOX.getHeight()
				// + (checkboxPadding.top + checkboxPadding.bottom),
				// fixedItemHeight);
			}

			baseline = itemRenderer.getBaseline(clientWidth, fixedItemHeight);
		}

		return baseline;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void layout() {
		ListView listView = (ListView) getComponent();
		List<Object> listData = (List<Object>) listView.getListData();
		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();

		if (variableItemHeight) {
			int width = getWidth();

			int checkboxHeight = 0;
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// checkboxHeight = CHECKBOX.getHeight()
				// + (checkboxPadding.top + checkboxPadding.bottom);
			}

			int n = listData.getLength();
			itemBoundaries = new ArrayList<Integer>(n);

			int itemY = 0;
			for (int i = 0; i < n; i++) {
				Object item = listData.get(i);

				int itemWidth = width;
				int itemX = 0;

				boolean checked = false;
				if (listView.getCheckmarksEnabled()) {
					throw new UnsupportedOperationException();
					// checked = listView.isItemChecked(i);
					// itemX = CHECKBOX.getWidth()
					// + (checkboxPadding.left + checkboxPadding.right);
					// itemWidth -= itemX;
				}

				itemRenderer.render(item, i, listView, false, checked, false,
						false);
				int itemHeight = itemRenderer.getPreferredHeight(itemWidth);

				if (listView.getCheckmarksEnabled()) {
					itemHeight = Math.max(itemHeight, checkboxHeight);
				}

				itemY += itemHeight;
				itemBoundaries.add(itemY);
			}
		} else {
			itemRenderer.render(null, -1, listView, false, false, false, false);
			fixedItemHeight = itemRenderer.getPreferredHeight(-1);

			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// fixedItemHeight = Math.max(CHECKBOX.getHeight()
				// + (checkboxPadding.top + checkboxPadding.bottom),
				// fixedItemHeight);
			}
		}

		if (validateSelection) {
			// Ensure that the selection is visible
			Sequence<Span> selectedRanges = listView.getSelectedRanges();

			if (selectedRanges.getLength() > 0) {
				int rangeStart = selectedRanges.get(0).start;
				int rangeEnd = selectedRanges
						.get(selectedRanges.getLength() - 1).end;

				Bounds selectionBounds = getItemBounds(rangeStart);
				selectionBounds = selectionBounds
						.union(getItemBounds(rangeEnd));

				Bounds visibleSelectionBounds = listView
						.getVisibleArea(selectionBounds);
				if (visibleSelectionBounds != null
						&& visibleSelectionBounds.height < selectionBounds.height) {
					listView.scrollAreaToVisible(selectionBounds);
				}
			}
		}

		validateSelection = false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void paint() {
		ListView listView = (ListView) getComponent();
		List<Object> listData = (List<Object>) listView.getListData();
		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();

		int width = getWidth();
		int height = getHeight();

		// // Paint the background
		// if (backgroundColor != null) {
		// graphics.setPaint(backgroundColor);
		// graphics.fillRect(0, 0, width, height);
		// }

		// Paint the list contents
		int itemStart = 0;
		int itemEnd = listData.getLength() - 1;

		// Ensure that we only paint items that are visible
		// Rectangle clipBounds = graphics.getClipBounds();
		// if (clipBounds != null) {
		// if (variableItemHeight) {
		// itemStart = getItemAt(clipBounds.y);
		// if (itemStart == -1) {
		// itemStart = listData.getLength();
		// }
		//
		// if (itemEnd != -1) {
		// int clipBottom = clipBounds.y + clipBounds.height - 1;
		// clipBottom = Math.min(clipBottom,
		// itemBoundaries.get(itemEnd) - 1);
		// itemEnd = getItemAt(clipBottom);
		// }
		// } else {
		// itemStart = Math.max(
		// itemStart,
		// (int) Math.floor(clipBounds.y
		// / (double) fixedItemHeight));
		// itemEnd = Math.min(
		// itemEnd,
		// (int) Math.ceil((clipBounds.y + clipBounds.height)
		// / (double) fixedItemHeight) - 1);
		// }
		// }
		//
		// // Paint the item background
		// if (alternateItemBackgroundColor != null) {
		// for (int itemIndex = itemStart; itemIndex <= itemEnd; itemIndex++) {
		// int itemY = getItemY(itemIndex);
		// int rowHeight = getItemHeight(itemIndex);
		// if (itemIndex % 2 > 0) {
		// graphics.setPaint(alternateItemBackgroundColor);
		// graphics.fillRect(0, itemY, width, rowHeight + 1);
		// }
		// }
		// }
		
		StringBuilder sb = new StringBuilder();

		// Paint the item content
		for (int itemIndex = itemStart; itemIndex <= itemEnd; itemIndex++) {
			Object item = listData.get(itemIndex);
			boolean highlighted = (itemIndex == highlightIndex && listView
					.getSelectMode() != ListView.SelectMode.NONE);
			boolean selected = listView.isItemSelected(itemIndex);
			boolean disabled = listView.isItemDisabled(itemIndex);
			int itemY = getItemY(itemIndex);
			int itemHeight = getItemHeight(itemIndex);

			Color itemBackgroundColor = null;
			if (selected) {
				itemBackgroundColor = (listView.isFocused()) ? this.selectionBackgroundColor
						: inactiveSelectionBackgroundColor;
			} else {
				if (highlighted && showHighlight && !disabled) {
					itemBackgroundColor = highlightBackgroundColor;
				}
			}

			// if (itemBackgroundColor != null) {
			// graphics.setPaint(itemBackgroundColor);
			// graphics.fillRect(0, itemY, width, itemHeight);
			// }

			int itemX = 0;
			int itemWidth = width;

			boolean checked = false;
			if (listView.getCheckmarksEnabled()) {
				throw new UnsupportedOperationException();
				// checked = listView.isItemChecked(itemIndex);
				//
				// int checkboxY = (itemHeight - CHECKBOX.getHeight()) / 2;
				// Graphics2D checkboxGraphics = (Graphics2D) graphics.create(
				// checkboxPadding.left, itemY + checkboxY,
				// CHECKBOX.getWidth(), CHECKBOX.getHeight());
				//
				// CHECKBOX.setSelected(checked);
				// CHECKBOX.setEnabled(!disabled
				// && !listView.isCheckmarkDisabled(itemIndex));
				// CHECKBOX.paint(checkboxGraphics);
				// checkboxGraphics.dispose();
				//
				// itemX = CHECKBOX.getWidth()
				// + (checkboxPadding.left + checkboxPadding.right);
				//
				// itemWidth -= itemX;
			}

			// Paint the data
			System.out.println(item);
			
			itemRenderer.render(item, itemIndex, listView, selected, checked,
					highlighted, disabled);
			itemRenderer.setSize(itemWidth, itemHeight);
			sb.append(itemRenderer.toString());
			
			itemY += itemHeight;
		}
		
		ui.setInnerHTML(sb.toString());

		super.paint();
	}

	// List view skin methods
	@Override
	@SuppressWarnings("unchecked")
	public int getItemAt(int y) {
		if (y < 0) {
			throw new IllegalArgumentException("y is negative");
		}

		ListView listView = (ListView) getComponent();

		int index;
		if (variableItemHeight) {
			if (y == 0) {
				index = 0;
			} else {
				index = ArrayList.binarySearch(itemBoundaries, y);
				if (index < 0) {
					index = -(index + 1);
				}
			}
		} else {
			index = (y / fixedItemHeight);

			List<Object> listData = (List<Object>) listView.getListData();
			if (index >= listData.getLength()) {
				index = -1;
			}
		}

		return index;
	}

	@Override
	public Bounds getItemBounds(int index) {
		return new Bounds(0, getItemY(index), getWidth(), getItemHeight(index));
	}

	@Override
	public int getItemIndent() {
		int itemIndent = 0;

		ListView listView = (ListView) getComponent();
		if (listView.getCheckmarksEnabled()) {
			throw new UnsupportedOperationException();
			// itemIndent = CHECKBOX.getWidth() + checkboxPadding.left
			// + checkboxPadding.right;
		}

		return itemIndent;
	}

	private int getItemY(int index) {
		int itemY;

		if (variableItemHeight) {
			if (index == 0) {
				itemY = 0;
			} else {
				itemY = itemBoundaries.get(index - 1);
			}
		} else {
			itemY = index * fixedItemHeight;
		}

		return itemY;
	}

	private int getItemHeight(int index) {
		int itemHeight;

		if (variableItemHeight) {
			itemHeight = itemBoundaries.get(index);

			if (index > 0) {
				itemHeight -= itemBoundaries.get(index - 1);
			}
		} else {
			itemHeight = fixedItemHeight;
		}

		return itemHeight;
	}

	@Override
	public boolean isFocusable() {
		ListView listView = (ListView) getComponent();
		return (listView.getSelectMode() != ListView.SelectMode.NONE);
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		this.font = font;
		invalidateComponent();
	}

	public final void setFont(String font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		setFont(Font.decode(font));
	}

	public final void setFont(Dictionary<String, ?> font) {
		if (font == null) {
			throw new IllegalArgumentException("font is null.");
		}

		// setFont(Theme.deriveFont(font));
		throw new UnsupportedOperationException();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}

		this.color = color;
		repaintComponent();
	}

	public final void setColor(String color) {
		if (color == null) {
			throw new IllegalArgumentException("color is null.");
		}

		setColor(Color.decode(color));
	}

	public final void setColor(int color) {
		// TerraTheme theme = (TerraTheme) Theme.getTheme();
		setColor(Theme.getTheme().getColor(color));
	}

	public Color getDisabledColor() {
		return disabledColor;
	}

	public void setDisabledColor(Color disabledColor) {
		if (disabledColor == null) {
			throw new IllegalArgumentException("disabledColor is null.");
		}

		this.disabledColor = disabledColor;
		repaintComponent();
	}

	public final void setDisabledColor(String disabledColor) {
		if (disabledColor == null) {
			throw new IllegalArgumentException("disabledColor is null.");
		}

		setDisabledColor(Color.decode(disabledColor));
	}

	public final void setDisabledColor(int disabledColor) {
		// TerraTheme theme = (TerraTheme) Theme.getTheme();
		setDisabledColor(Theme.getTheme().getColor(disabledColor));
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		repaintComponent();
	}

	public final void setBackgroundColor(String backgroundColor) {
		if (backgroundColor == null) {
			throw new IllegalArgumentException("backgroundColor is null.");
		}

		setBackgroundColor(Color.decode(backgroundColor));
	}

	public final void setBackgroundColor(int backgroundColor) {
		setBackgroundColor(Theme.getTheme().getColor(backgroundColor));
	}

	public Color getSelectionColor() {
		return selectionColor;
	}

	public void setSelectionColor(Color selectionColor) {
		if (selectionColor == null) {
			throw new IllegalArgumentException("selectionColor is null.");
		}

		this.selectionColor = selectionColor;
		repaintComponent();
	}

	public final void setSelectionColor(String selectionColor) {
		if (selectionColor == null) {
			throw new IllegalArgumentException("selectionColor is null.");
		}

		setSelectionColor(Color.decode(selectionColor));
	}

	public final void setSelectionColor(int selectionColor) {
		setSelectionColor(Theme.getTheme().getColor(selectionColor));
	}

	public Color getSelectionBackgroundColor() {
		return selectionBackgroundColor;
	}

	public void setSelectionBackgroundColor(Color selectionBackgroundColor) {
		if (selectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"selectionBackgroundColor is null.");
		}

		this.selectionBackgroundColor = selectionBackgroundColor;
		repaintComponent();
	}

	public final void setSelectionBackgroundColor(
			String selectionBackgroundColor) {
		if (selectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"selectionBackgroundColor is null.");
		}

		setSelectionBackgroundColor(Color.decode(selectionBackgroundColor));
	}

	public final void setSelectionBackgroundColor(int selectionBackgroundColor) {
		setSelectionBackgroundColor(Theme.getTheme().getColor(
				selectionBackgroundColor));
	}

	public Color getInactiveSelectionColor() {
		return inactiveSelectionColor;
	}

	public void setInactiveSelectionColor(Color inactiveSelectionColor) {
		if (inactiveSelectionColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionColor is null.");
		}

		this.inactiveSelectionColor = inactiveSelectionColor;
		repaintComponent();
	}

	public final void setInactiveSelectionColor(String inactiveSelectionColor) {
		if (inactiveSelectionColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionColor is null.");
		}

		setInactiveSelectionColor(Color.decode(inactiveSelectionColor));
	}

	public final void setInactiveSelectionColor(int inactiveSelectionColor) {
		setInactiveSelectionColor(Theme.getTheme().getColor(
				inactiveSelectionColor));
	}

	public Color getInactiveSelectionBackgroundColor() {
		return inactiveSelectionBackgroundColor;
	}

	public void setInactiveSelectionBackgroundColor(
			Color inactiveSelectionBackgroundColor) {
		if (inactiveSelectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionBackgroundColor is null.");
		}

		this.inactiveSelectionBackgroundColor = inactiveSelectionBackgroundColor;
		repaintComponent();
	}

	public final void setInactiveSelectionBackgroundColor(
			String inactiveSelectionBackgroundColor) {
		if (inactiveSelectionBackgroundColor == null) {
			throw new IllegalArgumentException(
					"inactiveSelectionBackgroundColor is null.");
		}

		setInactiveSelectionBackgroundColor(Color
				.decode(inactiveSelectionBackgroundColor));
	}

	public final void setInactiveSelectionBackgroundColor(
			int inactiveSelectionBackgroundColor) {
		setInactiveSelectionBackgroundColor(Theme.getTheme().getColor(
				inactiveSelectionBackgroundColor));
	}

	public Color getHighlightBackgroundColor() {
		return highlightBackgroundColor;
	}

	public void setHighlightBackgroundColor(Color highlightBackgroundColor) {
		if (highlightBackgroundColor == null) {
			throw new IllegalArgumentException(
					"highlightBackgroundColor is null.");
		}

		this.highlightBackgroundColor = highlightBackgroundColor;
		repaintComponent();
	}

	public final void setHighlightBackgroundColor(
			String highlightBackgroundColor) {
		if (highlightBackgroundColor == null) {
			throw new IllegalArgumentException(
					"highlightBackgroundColor is null.");
		}

		setHighlightBackgroundColor(Color.decode(highlightBackgroundColor));
	}

	public final void setHighlightBackgroundColor(int highlightBackgroundColor) {
		setHighlightBackgroundColor(Theme.getTheme().getColor(
				highlightBackgroundColor));
	}

	public Color getAlternateItemBackgroundColor() {
		return alternateItemBackgroundColor;
	}

	public void setAlternateItemBackgroundColor(
			Color alternateItemBackgroundColor) {
		this.alternateItemBackgroundColor = alternateItemBackgroundColor;
		repaintComponent();
	}

	public final void setAlternateItemBackgroundColor(
			String alternateItemBackgroundColor) {
		if (alternateItemBackgroundColor == null) {
			throw new IllegalArgumentException(
					"alternateItemBackgroundColor is null.");
		}

		setAlternateItemBackgroundColor(Color
				.decode(alternateItemBackgroundColor));
	}

	public final void setAlternateItemColor(int alternateItemBackgroundColor) {
		setAlternateItemBackgroundColor(Theme.getTheme().getColor(
				alternateItemBackgroundColor));
	}

	public boolean getShowHighlight() {
		return showHighlight;
	}

	public void setShowHighlight(boolean showHighlight) {
		this.showHighlight = showHighlight;
		repaintComponent();
	}

	public Insets getCheckboxPadding() {
		return checkboxPadding;
	}

	public void setCheckboxPadding(Insets checkboxPadding) {
		if (checkboxPadding == null) {
			throw new IllegalArgumentException("checkboxPadding is null.");
		}

		this.checkboxPadding = checkboxPadding;
		invalidateComponent();
	}

	public final void setCheckboxPadding(Dictionary<String, ?> checkboxPadding) {
		if (checkboxPadding == null) {
			throw new IllegalArgumentException("checkboxPadding is null.");
		}

		setCheckboxPadding(new Insets(checkboxPadding));
	}

	public final void setCheckboxPadding(int checkboxPadding) {
		setCheckboxPadding(new Insets(checkboxPadding));
	}

	public final void setCheckboxPadding(String checkboxPadding) {
		if (checkboxPadding == null) {
			throw new IllegalArgumentException("checkboxPadding is null.");
		}

		setCheckboxPadding(Insets.decode(checkboxPadding));
	}

	public boolean isVariableItemHeight() {
		return variableItemHeight;
	}

	public void setVariableItemHeight(boolean variableItemHeight) {
		this.variableItemHeight = variableItemHeight;
		invalidateComponent();
	}

	@Override
	public boolean mouseMove(Component component, int x, int y) {
		boolean consumed = super.mouseMove(component, x, y);

		ListView listView = (ListView) getComponent();

		int previousHighlightIndex = this.highlightIndex;
		highlightIndex = getItemAt(y);

		if (previousHighlightIndex != highlightIndex
				&& listView.getSelectMode() != ListView.SelectMode.NONE
				&& showHighlight) {
			if (previousHighlightIndex != -1) {
				// XXX repaintComponent(getItemBounds(previousHighlightIndex));
				repaintComponent();
			}

			if (highlightIndex != -1) {
				// repaintComponent(getItemBounds(highlightIndex));
				repaintComponent();
			}
		}

		return consumed;
	}

	@Override
	public void mouseOut(Component component) {
		super.mouseOut(component);

		ListView listView = (ListView) getComponent();

		if (highlightIndex != -1
				&& listView.getSelectMode() != ListView.SelectMode.NONE
				&& showHighlight) {
			Bounds itemBounds = getItemBounds(highlightIndex);
			// XXX repaintComponent(itemBounds.x, itemBounds.y,
			// itemBounds.width,
			// itemBounds.height);
			repaintComponent();
		}

		highlightIndex = -1;
		selectIndex = -1;
	}

	@Override
	public boolean mouseDown(Component component, Mouse.Button button, int x,
			int y) {
		boolean consumed = super.mouseDown(component, button, x, y);

		ListView listView = (ListView) getComponent();
		int itemIndex = getItemAt(y);

		if (itemIndex != -1 && !listView.isItemDisabled(itemIndex)) {
			if (!listView.getCheckmarksEnabled()
					|| listView.isCheckmarkDisabled(itemIndex)
					|| !getCheckboxBounds(itemIndex).contains(x, y)) {
				ListView.SelectMode selectMode = listView.getSelectMode();

				if (button == Mouse.Button.RIGHT) {
					if (selectMode != ListView.SelectMode.NONE
							&& !listView.isItemSelected(itemIndex)) {
						listView.setSelectedIndex(itemIndex);
					}
				} else {
					// XXX Keyboard.Modifier commandModifier = Platform
					// .getCommandModifier();

					if (Keyboard.isPressed(Keyboard.Modifier.SHIFT)
							&& selectMode == ListView.SelectMode.MULTI) {
						Filter<?> disabledItemFilter = listView
								.getDisabledItemFilter();

						if (disabledItemFilter == null) {
							// Select the range
							ArrayList<Span> selectedRanges = new ArrayList<Span>();
							int startIndex = listView.getFirstSelectedIndex();
							int endIndex = listView.getLastSelectedIndex();

							Span selectedRange = (itemIndex > startIndex) ? new Span(
									startIndex, itemIndex) : new Span(
									itemIndex, endIndex);
							selectedRanges.add(selectedRange);

							listView.setSelectedRanges(selectedRanges);
						}
					} else if (/*
								 * Keyboard.isPressed(commandModifier) &&
								 */selectMode == ListView.SelectMode.MULTI) {
						// Toggle the item's selection state
						if (listView.isItemSelected(itemIndex)) {
							listView.removeSelectedIndex(itemIndex);
						} else {
							listView.addSelectedIndex(itemIndex);
						}
					} else if (/*
								 * Keyboard.isPressed(commandModifier) &&
								 */selectMode == ListView.SelectMode.SINGLE) {
						// Toggle the item's selection state
						if (listView.isItemSelected(itemIndex)) {
							listView.setSelectedIndex(-1);
						} else {
							listView.setSelectedIndex(itemIndex);
						}
					} else {
						if (selectMode != ListView.SelectMode.NONE) {
							if (listView.isItemSelected(itemIndex)) {
								selectIndex = itemIndex;
							} else {
								listView.setSelectedIndex(itemIndex);
							}
						}
					}
				}
			}
		}

		listView.requestFocus();

		return consumed;
	}

	@Override
	public boolean mouseUp(Component component, Mouse.Button button, int x,
			int y) {
		boolean consumed = super.mouseUp(component, button, x, y);

		ListView listView = (ListView) getComponent();
		if (selectIndex != -1
				&& listView.getFirstSelectedIndex() != listView
						.getLastSelectedIndex()) {
			listView.setSelectedIndex(selectIndex);
			selectIndex = -1;
		}

		return consumed;
	}

	@Override
	public boolean mouseClick(Component component, Mouse.Button button, int x,
			int y, int count) {
		boolean consumed = super.mouseClick(component, button, x, y, count);

		ListView listView = (ListView) getComponent();
		int itemIndex = getItemAt(y);

		if (itemIndex != -1 && !listView.isItemDisabled(itemIndex)) {
			if (listView.getCheckmarksEnabled()
					&& !listView.isCheckmarkDisabled(itemIndex)
					&& getCheckboxBounds(itemIndex).contains(x, y)) {
				listView.setItemChecked(itemIndex,
						!listView.isItemChecked(itemIndex));
			} else {
				if (selectIndex != -1 && count == 1) {
					ListView.ItemEditor itemEditor = listView.getItemEditor();

					if (itemEditor != null) {
						if (itemEditor.isEditing()) {
							itemEditor.endEdit(true);
						}

						itemEditor.beginEdit(listView, selectIndex);
					}
				}
			}
		}

		selectIndex = -1;

		return consumed;
	}

	private Bounds getCheckboxBounds(int itemIndex) {
		throw new UnsupportedOperationException();
		// Bounds itemBounds = getItemBounds(itemIndex);
		//
		// int checkboxHeight = CHECKBOX.getHeight();
		// return new Bounds(checkboxPadding.left, itemBounds.y
		// + (itemBounds.height - checkboxHeight) / 2,
		// CHECKBOX.getWidth(), checkboxHeight);
	}

	@Override
	public boolean mouseWheel(Component component, Mouse.ScrollType scrollType,
			int scrollAmount, int wheelRotation, int x, int y) {
		ListView listView = (ListView) getComponent();

		if (highlightIndex != -1) {
			Bounds itemBounds = getItemBounds(highlightIndex);

			highlightIndex = -1;

			if (listView.getSelectMode() != ListView.SelectMode.NONE
					&& showHighlight) {
				// XXX repaintComponent(itemBounds.x, itemBounds.y,
				// itemBounds.width,
				// itemBounds.height, true);
				repaintComponent(true);
			}
		}

		return super.mouseWheel(component, scrollType, scrollAmount,
				wheelRotation, x, y);
	}

	/**
	 * {@link KeyCode#UP UP} Selects the previous enabled list item when select
	 * mode is not {@link SelectMode#NONE}<br> {@link KeyCode#DOWN DOWN} Selects the
	 * next enabled list item when select mode is not {@link SelectMode#NONE}
	 * <p>
	 * {@link Modifier#SHIFT SHIFT} + {@link KeyCode#UP UP} Increases the
	 * selection size by including the previous enabled list item when select
	 * mode is {@link SelectMode#MULTI}<br>
	 * {@link Modifier#SHIFT SHIFT} + {@link KeyCode#DOWN DOWN} Increases the
	 * selection size by including the next enabled list item when select mode
	 * is {@link SelectMode#MULTI}
	 */
	@Override
	public boolean keyPressed(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		boolean consumed = super.keyPressed(component, keyCode, keyLocation);

		ListView listView = (ListView) getComponent();
		ListView.SelectMode selectMode = listView.getSelectMode();

		switch (keyCode) {
		case KeyCodes.KEY_UP: {
			if (selectMode != ListView.SelectMode.NONE) {
				int index = listView.getFirstSelectedIndex();

				do {
					index--;
				} while (index >= 0 && listView.isItemDisabled(index));

				if (index >= 0) {
					if (Keyboard.isPressed(Keyboard.Modifier.SHIFT)
							&& listView.getSelectMode() == ListView.SelectMode.MULTI) {
						listView.addSelectedIndex(index);
					} else {
						listView.setSelectedIndex(index);
					}
				}

				consumed = true;
			}

			break;
		}

		case KeyCodes.KEY_DOWN: {
			if (selectMode != ListView.SelectMode.NONE) {
				int index = listView.getLastSelectedIndex();
				int count = listView.getListData().getLength();

				do {
					index++;
				} while (index < count && listView.isItemDisabled(index));

				if (index < count) {
					if (Keyboard.isPressed(Keyboard.Modifier.SHIFT)
							&& listView.getSelectMode() == ListView.SelectMode.MULTI) {
						listView.addSelectedIndex(index);
					} else {
						listView.setSelectedIndex(index);
					}
				}

				consumed = true;
			}

			break;
		}
		}

		// Clear the highlight
		if (highlightIndex != -1
				&& listView.getSelectMode() != ListView.SelectMode.NONE
				&& showHighlight) {
			// XXX repaintComponent(getItemBounds(highlightIndex));
			repaintComponent();
		}

		highlightIndex = -1;

		return consumed;
	}

	/**
	 * {@link KeyCode#SPACE SPACE} Toggles check mark selection when select mode
	 * is {@link SelectMode#SINGLE}
	 */
	@Override
	public boolean keyReleased(Component component, int keyCode,
			Keyboard.KeyLocation keyLocation) {
		boolean consumed = super.keyReleased(component, keyCode, keyLocation);

		ListView listView = (ListView) getComponent();

		switch (keyCode) {
		case KeyCodes.KEY_ENTER: {
			if (listView.getCheckmarksEnabled()
					&& listView.getSelectMode() == ListView.SelectMode.SINGLE) {
				int selectedIndex = listView.getSelectedIndex();

				if (!listView.isCheckmarkDisabled(selectedIndex)) {
					listView.setItemChecked(selectedIndex,
							!listView.isItemChecked(selectedIndex));
					consumed = true;
				}
			}

			break;
		}
		}

		return consumed;
	}

	/**
	 * Select the next enabled list item where the first character of the
	 * rendered text matches the typed key (case insensitive).
	 */
	@Override
	public boolean keyTyped(Component component, char character) {
		boolean consumed = super.keyTyped(component, character);

		ListView listView = (ListView) getComponent();
		List<?> listData = listView.getListData();
		ListView.ItemRenderer itemRenderer = listView.getItemRenderer();

		character = Character.toUpperCase(character);

		for (int i = listView.getLastSelectedIndex() + 1, n = listData
				.getLength(); i < n; i++) {
			if (!listView.isItemDisabled(i)) {
				String string = itemRenderer.toString(listData.get(i));

				if (string != null && string.length() > 0) {
					char first = Character.toUpperCase(string.charAt(0));

					if (first == character) {
						listView.setSelectedIndex(i);
						consumed = true;
						break;
					}
				}
			}
		}

		return consumed;
	}

	// Component state events
	@Override
	public void enabledChanged(Component component) {
		super.enabledChanged(component);

		repaintComponent();
	}

	@Override
	public void focusedChanged(Component component, Component obverseComponent) {
		super.focusedChanged(component, obverseComponent);

		repaintComponent();
	}

	// List view events
	@Override
	public void listDataChanged(ListView listView, List<?> previousListData) {
		highlightIndex = -1;
		invalidateComponent();
	}

	@Override
	public void itemRendererChanged(ListView listView,
			ListView.ItemRenderer previousItemRenderer) {
		invalidateComponent();
	}

	@Override
	public void itemEditorChanged(ListView listView,
			ListView.ItemEditor previousItemEditor) {
		// No-op
	}

	@Override
	public void selectModeChanged(ListView listView,
			ListView.SelectMode previousSelectMode) {
		repaintComponent();
	}

	@Override
	public void checkmarksEnabledChanged(ListView listView) {
		invalidateComponent();
	}

	@Override
	public void disabledItemFilterChanged(ListView listView,
			Filter<?> previousDisabledItemFilter) {
		repaintComponent();
	}

	@Override
	public void disabledCheckmarkFilterChanged(ListView listView,
			Filter<?> previousDisabledCheckmarkFilter) {
		repaintComponent();
	}

	// List view item events
	@Override
	public void itemInserted(ListView listView, int index) {
		invalidateComponent();
	}

	@Override
	public void itemsRemoved(ListView listView, int index, int count) {
		invalidateComponent();
	}

	@Override
	public void itemUpdated(ListView listView, int index) {
		invalidateComponent();
	}

	@Override
	public void itemsCleared(ListView listView) {
		invalidateComponent();
	}

	@Override
	public void itemsSorted(ListView listView) {
		if (variableItemHeight) {
			invalidateComponent();
		} else {
			repaintComponent();
		}
	}

	// List view item state events
	@Override
	public void itemCheckedChanged(ListView listView, int index) {
		// XXX repaintComponent(getItemBounds(index));
		repaintComponent();
	}

	// List view selection detail events
	@Override
	public void selectedRangeAdded(ListView listView, int rangeStart,
			int rangeEnd) {
		if (listView.isValid()) {
			Bounds selectionBounds = getItemBounds(rangeStart);
			selectionBounds = selectionBounds.union(getItemBounds(rangeEnd));
			// XXX repaintComponent(selectionBounds);
			repaintComponent();

			// Ensure that the selection is visible
			Bounds visibleSelectionBounds = listView
					.getVisibleArea(selectionBounds);
			if (visibleSelectionBounds.height < selectionBounds.height) {
				listView.scrollAreaToVisible(selectionBounds);
			}
		} else {
			validateSelection = true;
		}
	}

	@Override
	public void selectedRangeRemoved(ListView listView, int rangeStart,
			int rangeEnd) {
		// Repaint the area containing the removed selection
		if (listView.isValid()) {
			Bounds selectionBounds = getItemBounds(rangeStart);
			selectionBounds = selectionBounds.union(getItemBounds(rangeEnd));
			// XXX repaintComponent(selectionBounds);
			repaintComponent();
		}
	}

	@Override
	public void selectedRangesChanged(ListView listView,
			Sequence<Span> previousSelectedRanges) {
		if (previousSelectedRanges != null
				&& previousSelectedRanges != listView.getSelectedRanges()) {
			if (listView.isValid()) {
				// Repaint the area occupied by the previous selection
				if (previousSelectedRanges != null
						&& previousSelectedRanges.getLength() > 0) {
					int rangeStart = previousSelectedRanges.get(0).start;
					int rangeEnd = previousSelectedRanges
							.get(previousSelectedRanges.getLength() - 1).end;

					Bounds previousSelectionBounds = getItemBounds(rangeStart);
					previousSelectionBounds = previousSelectionBounds
							.union(getItemBounds(rangeEnd));
					// XXX repaintComponent(previousSelectionBounds);
					repaintComponent();
				}

				// Repaint the area occupied by the current selection
				Sequence<Span> selectedRanges = listView.getSelectedRanges();
				if (selectedRanges.getLength() > 0) {
					int rangeStart = selectedRanges.get(0).start;
					int rangeEnd = selectedRanges.get(selectedRanges
							.getLength() - 1).end;

					Bounds selectionBounds = getItemBounds(rangeStart);
					selectionBounds = selectionBounds
							.union(getItemBounds(rangeEnd));
					// XXX repaintComponent(selectionBounds);
					repaintComponent();

					// Ensure that the selection is visible
					Bounds visibleSelectionBounds = listView
							.getVisibleArea(selectionBounds);
					if (visibleSelectionBounds != null
							&& visibleSelectionBounds.height < selectionBounds.height) {
						// TODO Repainting the entire component is a workaround
						// for PIVOT-490
						repaintComponent();

						listView.scrollAreaToVisible(selectionBounds);
					}
				}
			} else {
				validateSelection = true;
			}
		}
	}

	@Override
	public void selectedItemChanged(ListView listView,
			Object previousSelectedItem) {
		// No-op
	}
}
