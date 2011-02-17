package gwt.mosaic.client.wtk.skin.rhodes;

import com.google.gwt.core.client.GWT;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.wtk.PushButton;
import gwt.mosaic.client.wtk.Theme;

public class RhodesTheme extends Theme {

	interface PushButtonSkinBeanAdapter extends
			BeanAdapter<RhodesPushButtonSkin> {
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
	}
}
