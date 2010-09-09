package org.gwt.mosaic2g.rebind;

import java.util.ArrayList;

/**
 * This abstract class represents an easing function that can be used by the
 * GRIN framework/compiler to generate a linear interpolation.
 * 
 * @author ggeorg
 */
public abstract class EasingEquation {

	/**
	 * Total number of keyframes added to approximate interpolations.
	 */
	public static int framesAdded = 0;

	private int maxError = 0;

	/**
	 * Evaluate the eqsing equasion.
	 * 
	 * @param t
	 *            current time in frames, seconds, or any other unit
	 * @param b
	 *            beginning value
	 * @param c
	 *            change in value
	 * @param d
	 *            duration in frames, seconds, or any other unit
	 * @return
	 */
	public abstract double evaluate(double t, double b, double c, double d);

	/**
	 * Set the maximum allowed error when generating linear interpolations from
	 * this equation. Defaults to zero.
	 * 
	 * @param maxError
	 *            themaximum allowed error
	 */
	public void setMaxError(int maxError) {
		this.maxError = maxError;
	}

	/**
	 * Approximate this easing function with linear interpolation segments,
	 * making sure that the error doesn't exceed maxError units. We start the
	 * easing from keyFrames[size - 1], and add to keyFrames.
	 * 
	 * @param keyFrames
	 *            a list of { frame#, value, ... } int arrays
	 * @param end
	 *            where to ease to, { frame#, value, ... }
	 */
	public void addKeyFrames(ArrayList<int[]> keyFrames, int[] end) {
		int[] start = keyFrames.get(keyFrames.size() - 1);
		int startFrame = start[0];
		int endFrame = end[0];
		int duration = endFrame - startFrame;
		int[][] allFrames = new int[duration + 1][];
		for (int f = 0; f <= duration; f++) {
			allFrames[f] = new int[end.length];
			allFrames[f][0] = f + startFrame;
			for (int i = 1; i < end.length; i++) {
				double val = evaluate(f, start[i], end[i] - start[i], duration);
				allFrames[f][i] = (int) Math.round(val);
			}
		}
		trimUnneededKeyFrames(startFrame, keyFrames, end, allFrames);
	}

	/**
	 * Approximate the list of keyFrames with a reduced list, within the error
	 * band of this easing equation.
	 * 
	 * @param startFrame
	 * @param keyFrames
	 * @param end
	 * @param allFrames
	 */
	protected void trimUnneededKeyFrames(int startFrame,
			ArrayList<int[]> keyFrames, int[] end, int[][] allFrames) {
		for (;;) {
			int[] current = keyFrames.get(keyFrames.size() - 1);
			if (current[0] >= end[0]) {
				return; // all done!
			}
			int frame = current[0] + 1;
			// go until at end of function, or error is too big
			for (;;) {
				frame++;
				if (frame > end[0]) {
					break;
				}
				int[] candidate = allFrames[frame - startFrame];
				boolean tooMuchError = false;
				// check interpolation algorithm from InterpolationModel
				for (int f = current[0] + 1; f < candidate[0]; f++) {
					int dist = candidate[0] - current[0];
					int distNext = candidate[0] - f;
					int distLast = f - current[0];
					for (int i = 1; i < current.length; i++) {
						int v = (candidate[i] * distLast + current[i]
								* distNext)
								/ dist;
						int err = v - allFrames[f - startFrame][i];
						if (Math.abs(err) > maxError) {
							tooMuchError = true;
						}
					}
				}
				if (tooMuchError) {
					break;
				}
			}
			frame--;
			keyFrames.add(allFrames[frame - startFrame]);
			framesAdded++;
		}
	}

}