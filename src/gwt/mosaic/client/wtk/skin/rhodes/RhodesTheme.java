package gwt.mosaic.client.wtk.skin.rhodes;

import com.google.gwt.core.client.GWT;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.TextInput;
import gwt.mosaic.client.wtk.Theme;
import gwt.mosaic.client.wtk.style.Color;

public class RhodesTheme extends Theme {

	interface PushButtonSkinBeanAdapter extends
			BeanAdapter<RhodesPushButtonSkin> {
	}

	interface TextInputSkinBeanAdapter extends BeanAdapter<RhodesTextInputSkin> {
	}

	public RhodesTheme() {

		componentSkinMap.put(PushButton.class,
				new BeanAdapterFactory<RhodesPushButtonSkin>() {
					@Override
					public BeanAdapter<RhodesPushButtonSkin> create() {
						PushButtonSkinBeanAdapter adapter = GWT
								.create(PushButtonSkinBeanAdapter.class);
						adapter.setBean(new RhodesPushButtonSkin());
						return adapter;
					}
				});

		componentSkinMap.put(TextInput.class,
				new BeanAdapterFactory<RhodesTextInputSkin>() {
					@Override
					public BeanAdapter<RhodesTextInputSkin> create() {
						TextInputSkinBeanAdapter adapter = GWT
								.create(TextInputSkinBeanAdapter.class);
						adapter.setBean(new RhodesTextInputSkin());
						return adapter;
					}
				});

		//
		// Setup theme colors.
		//

		colors.insert(Color.WHITE, ACTIVE_BORDER_COLOR);
		colors.insert(new Color(0x99, 0xB4, 0xD1), ACTIVE_CAPTION_COLOR);
		colors.insert(new Color(0xAB, 0xAB, 0xAB), ACTIVE_WORKSPACE_COLOR);
		colors.insert(Color.BLACK, BACKGROUND_COLOR);
		colors.insert(new Color(0xF0, 0xF0, 0xF0), BUTTON_FACE_COLOR);
		colors.insert(Color.WHITE, BUTTON_HIGHLIGHT_COLOR);
		colors.insert(new Color(0xA0, 0xA0, 0xA0), BUTTON_SHADOW_COLOR);
		colors.insert(Color.BLACK, BUTTON_TEXT_COLOR);
		colors.insert(Color.BLACK, CAPTION_TEXT_COLOR);
		colors.insert(new Color(0x6D, 0x6D, 0x6D), GRAY_TEXT_COLOR);
		colors.insert(new Color(0x33, 0x99, 0xFF), HIGHLIGHT_COLOR);
		colors.insert(Color.WHITE, HIGHLIGHT_TEXT_COLOR);
		colors.insert(new Color(0xF4, 0xF7, 0xFC), INACTIVE_BORDER_COLOR);
		colors.insert(new Color(0xBF, 0xCD, 0xDB), INACTIVE_CAPTION_COLOR);
		colors.insert(new Color(0x43, 0x4E, 0x54), INACTIVE_CAPTION_TEXT_COLOR);
		colors.insert(new Color(0xFF, 0xFF, 0xE1), INFO_BACKGROUND_COLOR);
		colors.insert(Color.BLACK, INFO_TEXT_COLOR);
		colors.insert(colors.get(BUTTON_FACE_COLOR), MENU_COLOR);
		colors.insert(Color.BLACK, MENU_TEXT_COLOR);
		colors.insert(new Color(0xC8, 0xC8, 0xC8), SCROLLBAR_COLOR);
		colors.insert(new Color(0x69, 0x69, 0x69), THREE_D_DARK_SHADOW_COLOR);
		colors.insert(colors.get(BUTTON_FACE_COLOR), THREE_D_FACE_COLOR);
		colors.insert(Color.WHITE, THREE_D_HIGHLIGHT_COLOR);
		colors.insert(new Color(0xE3, 0xE3, 0xE3), THREE_D_LIGHT_SHADOW_COLOR);
		colors.insert(colors.get(BUTTON_SHADOW_COLOR), THREE_D_SHADOW_COLOR);
		colors.insert(Color.WHITE, WINDOW_COLOR);
		colors.insert(new Color(0x64, 0x64, 0x64), WINDOW_FRAME_COLOR);
		colors.insert(Color.BLACK, WINDOW_TEXT_COLOR);
	}

}
