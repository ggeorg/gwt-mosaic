package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

public class FeatureSetupCommand implements Command {
	private final Show show;

	public FeatureSetupCommand(Show show) {
		this.show = show;
	}

	public void execute() {
		final Segment s = show.getCurrentSegment();
		if (s != null) {
            s.runFeatureSetup();
        }
	}

}
