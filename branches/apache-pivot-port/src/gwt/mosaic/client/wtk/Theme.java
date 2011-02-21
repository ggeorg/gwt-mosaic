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
package gwt.mosaic.client.wtk;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.collections.ArrayList;
import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.wtk.skin.BoxPaneSkin;
import gwt.mosaic.client.wtk.skin.FlowPaneSkin;
import gwt.mosaic.client.wtk.skin.GridPaneFillerSkin;
import gwt.mosaic.client.wtk.skin.GridPaneSkin;
import gwt.mosaic.client.wtk.skin.ImageViewSkin;
import gwt.mosaic.client.wtk.skin.LabelSkin;
import gwt.mosaic.client.wtk.skin.PanelSkin;
import gwt.mosaic.client.wtk.skin.StackPaneSkin;
import gwt.mosaic.client.wtk.skin.TablePaneFillerSkin;
import gwt.mosaic.client.wtk.skin.TablePaneSkin;
import gwt.mosaic.client.wtk.skin.WindowSkin;
import gwt.mosaic.client.wtk.style.Color;

import com.google.gwt.core.client.GWT;

/**
 * Base class for Pivot themes. A theme defines a complete "look and feel" for a
 * Pivot application.
 * <p>
 * Note that concrete Theme implementations should be declared as final. If
 * multiple third-party libraries attempted to extend a theme, it would cause a
 * conflict, as only one could be used in any given application.
 * <p>
 * IMPORTANT All skin mappings must be added to the map, even non-static inner
 * classes. Otherwise, the component's base class will attempt to install its
 * own skin, which will result in the addition of duplicate listeners.
 */
public abstract class Theme {

	public static final String NAME_KEY = "name";
	public static final String SIZE_KEY = "size";
	public static final String BOLD_KEY = "bold";
	public static final String ITALIC_KEY = "italic";

	/** Active window border. */
	public static final int ACTIVE_BORDER_COLOR = 0;

	/** Active window caption. */
	public static final int ACTIVE_CAPTION_COLOR = 1;

	/** Background color of multiple document interface. */
	public static final int ACTIVE_WORKSPACE_COLOR = 2;

	/** Desktop background. */
	public static final int BACKGROUND_COLOR = 3;

	/** Face color for three-dimensional display elements. */
	public static final int BUTTON_FACE_COLOR = 4;

	/**
	 * Dark shadow for three-dimensional display elements (for edges facing away
	 * from the light source).
	 */
	public static final int BUTTON_HIGHLIGHT_COLOR = 5;

	/** Shadow color for three-dimensional display elements. */
	public static final int BUTTON_SHADOW_COLOR = 6;

	/** Text on push buttons. */
	public static final int BUTTON_TEXT_COLOR = 7;

	/** Text in caption, size box, and scrollbar arrow box. */
	public static final int CAPTION_TEXT_COLOR = 8;

	/**
	 * Grayed (disabled) text. This color is set to #000 if the current display
	 * driver does not support a solid gray color.
	 */
	public static final int GRAY_TEXT_COLOR = 9;

	/** Item(s) selected in a control. */
	public static final int HIGHLIGHT_COLOR = 10;

	/** Text of item(s) selected in a control. */
	public static final int HIGHLIGHT_TEXT_COLOR = 11;

	/** Inactive window border. */
	public static final int INACTIVE_BORDER_COLOR = 12;

	/** Inactive window caption. */
	public static final int INACTIVE_CAPTION_COLOR = 13;

	/** Color of text in an inactive caption. */
	public static final int INACTIVE_CAPTION_TEXT_COLOR = 14;

	/** Background color for tooltip controls. */
	public static final int INFO_BACKGROUND_COLOR = 15;

	/** Text color for tooltip controls. */
	public static final int INFO_TEXT_COLOR = 16;

	/** Menu background. */
	public static final int MENU_COLOR = 17;

	/** Text in menus. */
	public static final int MENU_TEXT_COLOR = 18;

	/** Scroll bar gray area. */
	public static final int SCROLLBAR_COLOR = 19;

	/** Dark shadow for three-dimensional display elements. */
	public static final int THREE_D_DARK_SHADOW_COLOR = 20;

	/** Face color for three-dimensional display elements. */
	public static final int THREE_D_FACE_COLOR = 21;

	/** Highlight color for three-dimensional display elements. */
	public static final int THREE_D_HIGHLIGHT_COLOR = 22;

	/**
	 * Light color for three-dimensional display elements (for edges facing the
	 * light source).
	 */
	public static final int THREE_D_LIGHT_SHADOW_COLOR = 23;

	/** Dark shadow for three-dimensional display elements. */
	public static final int THREE_D_SHADOW_COLOR = 24;

	/** Window background. */
	public static final int WINDOW_COLOR = 25;

	/** Window frame. */
	public static final int WINDOW_FRAME_COLOR = 26;

	/** Text in windows. */
	public static final int WINDOW_TEXT_COLOR = 27;

	// ---------------------------------------------------------------------
	private static Theme theme = GWT.create(Theme.class);

	/**
	 * Gets the current theme.
	 * 
	 * @throws IllegalStateException
	 *             If a theme has not been installed.
	 */
	public static Theme getTheme() {
		if (theme == null) {
			throw new IllegalStateException("No installed theme.");
		}

		return theme;
	}

	// ---------------------------------------------------------------------
	interface BoxPaneSkinBeanAdapter extends BeanAdapter<BoxPaneSkin> {
	}

	interface FlowPaneSkinBeanAdapter extends BeanAdapter<FlowPaneSkin> {
	}

	interface GridPaneSkinBeanAdapter extends BeanAdapter<GridPaneSkin> {
	}

	interface GridPaneFillerSkinBeanAdapter extends
			BeanAdapter<GridPaneFillerSkin> {
	}

	interface ImageViewSkinBeanAdapter extends BeanAdapter<ImageViewSkin> {
	}

	interface LabelSkinBeanAdapter extends BeanAdapter<LabelSkin> {
	}

	interface PanelSkinBeanAdapter extends BeanAdapter<PanelSkin> {
	}

	interface StackPaneSkinBeanAdapter extends BeanAdapter<StackPaneSkin> {
	}

	interface TablePaneSkinBeanAdapter extends BeanAdapter<TablePaneSkin> {
	}

	interface TablePaneFillerSkinBeanAdapter extends
			BeanAdapter<TablePaneFillerSkin> {
	}

	interface WindowSkinBeanAdapter extends BeanAdapter<WindowSkin> {
	}

	// ---------------------------------------------------------------------
	protected interface BeanAdapterFactory<T extends Skin> {
		BeanAdapter<T> create();
	}

	protected final HashMap<Class<? extends Component>, BeanAdapterFactory<? extends Skin>> componentSkinMap = new HashMap<Class<? extends Component>, BeanAdapterFactory<? extends Skin>>();

	protected final ArrayList<Color> colors = new ArrayList<Color>();

	public Theme() {
		// componentSkinMap.put(Border.class, BorderSkin.class);
		componentSkinMap.put(BoxPane.class,
				new BeanAdapterFactory<BoxPaneSkin>() {
					@Override
					public BeanAdapter<BoxPaneSkin> create() {
						BoxPaneSkinBeanAdapter adapter = GWT
								.create(BoxPaneSkinBeanAdapter.class);
						adapter.setBean(new BoxPaneSkin());
						return adapter;
					}
				});
		// componentSkinMap.put(CardPane.class, CardPaneSkin.class);
		// componentSkinMap.put(ColorChooserButtonSkin.ColorChooserPopup.class,
		// ColorChooserButtonSkin.ColorChooserPopupSkin.class);
		componentSkinMap.put(FlowPane.class,
				new BeanAdapterFactory<FlowPaneSkin>() {
					@Override
					public BeanAdapter<FlowPaneSkin> create() {
						FlowPaneSkinBeanAdapter adapter = GWT
								.create(FlowPaneSkinBeanAdapter.class);
						adapter.setBean(new FlowPaneSkin());
						return adapter;
					}
				});
		componentSkinMap.put(GridPane.class,
				new BeanAdapterFactory<GridPaneSkin>() {
					@Override
					public BeanAdapter<GridPaneSkin> create() {
						GridPaneSkinBeanAdapter adapter = GWT
								.create(GridPaneSkinBeanAdapter.class);
						adapter.setBean(new GridPaneSkin());
						return adapter;
					}
				});
		componentSkinMap.put(GridPane.Filler.class,
				new BeanAdapterFactory<GridPaneFillerSkin>() {
					@Override
					public BeanAdapter<GridPaneFillerSkin> create() {
						GridPaneFillerSkinBeanAdapter adapter = GWT
								.create(GridPaneFillerSkinBeanAdapter.class);
						adapter.setBean(new GridPaneFillerSkin());
						return adapter;
					}
				});
		componentSkinMap.put(ImageView.class,
				new BeanAdapterFactory<ImageViewSkin>() {
					@Override
					public BeanAdapter<ImageViewSkin> create() {
						ImageViewSkinBeanAdapter adapter = GWT
								.create(ImageViewSkinBeanAdapter.class);
						adapter.setBean(new ImageViewSkin());
						return adapter;
					}
				});
		componentSkinMap.put(Label.class, new BeanAdapterFactory<LabelSkin>() {
			@Override
			public BeanAdapter<LabelSkin> create() {
				LabelSkinBeanAdapter adapter = GWT
						.create(LabelSkinBeanAdapter.class);
				adapter.setBean(new LabelSkin());
				return adapter;
			}
		});
		// componentSkinMap.put(MovieView.class, MovieViewSkin.class);
		componentSkinMap.put(Panel.class, new BeanAdapterFactory<PanelSkin>() {
			@Override
			public BeanAdapter<PanelSkin> create() {
				PanelSkinBeanAdapter adapter = GWT
						.create(PanelSkinBeanAdapter.class);
				adapter.setBean(new PanelSkin());
				return adapter;
			}
		});
		// componentSkinMap.put(ScrollPane.class, ScrollPaneSkin.class);
		// componentSkinMap.put(Separator.class, SeparatorSkin.class);
		componentSkinMap.put(StackPane.class,
				new BeanAdapterFactory<StackPaneSkin>() {
					@Override
					public BeanAdapter<StackPaneSkin> create() {
						StackPaneSkinBeanAdapter adapter = GWT
								.create(StackPaneSkinBeanAdapter.class);
						adapter.setBean(new StackPaneSkin());
						return adapter;
					}
				});
		componentSkinMap.put(TableInput.class,
				new BeanAdapterFactory<TablePaneSkin>() {
					@Override
					public BeanAdapter<TablePaneSkin> create() {
						TablePaneSkinBeanAdapter adapter = GWT
								.create(TablePaneSkinBeanAdapter.class);
						adapter.setBean(new TablePaneSkin());
						return adapter;
					}
				});
		componentSkinMap.put(TableInput.Filler.class,
				new BeanAdapterFactory<TablePaneFillerSkin>() {
					@Override
					public BeanAdapter<TablePaneFillerSkin> create() {
						TablePaneFillerSkinBeanAdapter adapter = GWT
								.create(TablePaneFillerSkinBeanAdapter.class);
						adapter.setBean(new TablePaneFillerSkin());
						return adapter;
					}
				});
		// componentSkinMap.put(TextArea.class, TextAreaSkin.class);
		// componentSkinMap.put(TextPane.class, TextPaneSkin.class);
		componentSkinMap.put(Window.class,
				new BeanAdapterFactory<WindowSkin>() {
					@Override
					public BeanAdapter<WindowSkin> create() {
						WindowSkinBeanAdapter adapter = GWT
								.create(WindowSkinBeanAdapter.class);
						adapter.setBean(new WindowSkin());
						return adapter;
					}
				});
	}

	/**
	 * Returns the skin adapter responsible for skinning the specified component
	 * class.
	 * 
	 * @param componentClass
	 *            The component class.
	 * 
	 * @return The skin adapter, or <tt>null</tt> if no skin mapping exists for
	 *         the component class.
	 */
	public final BeanAdapter<? extends Skin> get(
			Class<? extends Component> componentClass) {
		BeanAdapterFactory<? extends Skin> factory = componentSkinMap
				.get(componentClass);
		if (factory != null) {
			return factory.create();
		} else {
			return null;
		}
	}

	public Font getFont() {
		return new Font();
	}

	public void setFont(Font font) {
		//
	}

	// public static Font deriveFont(Dictionary<String, ?> dictionary) {
	// Font font = theme.getFont();
	//
	// String name = font.getName();
	// if (dictionary.containsKey(NAME_KEY)) {
	// name = (String)dictionary.get(NAME_KEY);
	// }
	//
	// int size = font.getSize();
	// if (dictionary.containsKey(SIZE_KEY)) {
	// Object value = dictionary.get(SIZE_KEY);
	//
	// if (value instanceof String) {
	// String string = (String)value;
	//
	// if (string.endsWith("%")) {
	// float percentage = Float.parseFloat(string.substring(0, string.length() -
	// 1)) / 100f;
	// size = Math.round(font.getSize() * percentage);
	// } else {
	// throw new IllegalArgumentException(value + " is not a valid font size.");
	// }
	// } else {
	// size = (Integer)value;
	// }
	// }
	//
	// int style = font.getStyle();
	// if (dictionary.containsKey(BOLD_KEY)) {
	// boolean bold = (Boolean)dictionary.get(BOLD_KEY);
	//
	// if (bold) {
	// style |= Font.BOLD;
	// } else {
	// style &= ~Font.BOLD;
	// }
	// }
	//
	// if (dictionary.containsKey(ITALIC_KEY)) {
	// boolean italic = (Boolean)dictionary.get(ITALIC_KEY);
	//
	// if (italic) {
	// style |= Font.ITALIC;
	// } else {
	// style &= ~Font.ITALIC;
	// }
	// }
	//
	// return new Font(name, style, size);
	// }
	
	/**
     * Gets a value from the theme's color palette.
     *
     * @param index
     */
    public Color getColor(int index) {
        return colors.get(index);
    }
}
