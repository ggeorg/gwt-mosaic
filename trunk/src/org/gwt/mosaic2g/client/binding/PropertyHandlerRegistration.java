package org.gwt.mosaic2g.client.binding;

import com.google.gwt.event.shared.HandlerRegistration;

public class PropertyHandlerRegistration implements HandlerRegistration {

	private final HandlerRegistration hr1, hr2;

	public PropertyHandlerRegistration(HandlerRegistration hr1,
			HandlerRegistration hr2) {
		this.hr1 = hr1;
		this.hr2 = hr2;
	}

	public void removeHandler() {
		hr1.removeHandler();
		hr2.removeHandler();
	}

}
