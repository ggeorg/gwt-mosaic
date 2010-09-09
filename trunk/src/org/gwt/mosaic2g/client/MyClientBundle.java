package org.gwt.mosaic2g.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface MyClientBundle extends ClientBundle {
	public static final MyClientBundle INSTANCE = GWT
			.create(MyClientBundle.class);

	@Source("my.css")
	public MyCssResource css();

}
