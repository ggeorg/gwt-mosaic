package org.gwt.mosaic2g.rebind;

public class AbstractInterpolatedModel {

	/**
	 * Frame number of key frames, frames[0] is always 0.
	 */
	protected int[] frames;

	/**
	 * Current value of each field.
	 */
	protected int[] currValues;

	/**
	 * Values at keyframe, indexed by field. The array for a field can be
	 * {@code null}, in which case the initial value of currValues will be
	 * maintained.
	 */
	protected int[][] values;

	/**
	 * Frame to go after the end. {@code Integer.MAX_VALUE} means
	 * "stick at end", {@code 0} will case a cycle.
	 */
	protected int repeatFrame;

	/**
	 * # of times to repeat images before sending end commands,
	 * {@code Integer.MAX_VALUE} means "infinite".
	 */
	protected int loopCount;

	protected void init(int[] frames, int[][] values, int repeatFrame,
			int loopCount) {
		int[] currValues = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			int[] valueList = values[i];
			boolean allSame = true;
			currValues[i] = valueList[0];
			for (int j = 1; j < valueList.length; j++) {
				allSame = allSame && currValues[i] == valueList[j];
			}
			if (allSame) {
				values[i] = null;
				// if all of the values are the same, we don't need the list of
				// values for this parameter.
			}
		}
		this.frames = frames;
		this.currValues = currValues;
		this.values = values;
		this.repeatFrame = repeatFrame;
		this.loopCount = loopCount;
	}

	public int getRepeatFrame() {
		return repeatFrame;
	}

	public void setRepeatFrame(int repeatFrame) {
		this.repeatFrame = repeatFrame;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public int[] getFrames() {
		return frames;
	}

	public int[] getCurrValues() {
		return currValues;
	}

	public int[][] getValues() {
		return values;
	}

}
