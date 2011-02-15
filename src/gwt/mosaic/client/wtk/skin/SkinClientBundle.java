package gwt.mosaic.client.wtk.skin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface SkinClientBundle extends ClientBundle {
	public static final SkinClientBundle INSTANCE = GWT.create(SkinClientBundle.class);
	
	@Source("skin.css")
	public SkinCssResource css();

}
