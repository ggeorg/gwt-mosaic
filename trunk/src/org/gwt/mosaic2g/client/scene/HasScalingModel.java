package org.gwt.mosaic2g.client.scene;

import org.gwt.mosaic2g.client.util.Rectangle;

public interface HasScalingModel {
	InterpolatedModel getScalingModel();

	void setScalingModel(InterpolatedModel scalingModel);

	void setScalingModel(InterpolatedModel scalingModel, boolean managed);

	Rectangle getScaledBounds();
}
