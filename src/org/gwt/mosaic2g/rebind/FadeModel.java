package org.gwt.mosaic2g.rebind;

public class FadeModel extends AbstractInterpolatedModel {
	public FadeModel(int[] fs, int[] fadeValues, int repeatFrame, int loopCount) {
		int[][] values = new int[][] { fadeValues };
		init(fs, values, repeatFrame, loopCount);
	}
}
