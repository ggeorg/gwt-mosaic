package org.gwt.mosaic2g.rebind;

import java.util.ArrayList;

/**
 * This class represents an degenerate case of an easing function that consists
 * of just listing the in between points. It can be used by the GRIN compiler to
 * generate a linear interpolation that approximates that list of points within
 * a certail error.
 * 
 * @author ggeorg
 */
public class PointsEasingEquation extends EasingEquation {

	private int[][] points;

	public PointsEasingEquation(int[][] points) {
		this.points = points;
	}

	public void addKeyFrames(ArrayList<int[]> keyFrames, int[] end)
			 {
		int[] start = keyFrames.get(keyFrames.size() - 1);
		int startFrame = start[0];
		int endFrame = end[0];
		int duration = endFrame - startFrame;
		int[][] allFrames = new int[duration + 1][];
		if (points.length != duration) {
			throw new RuntimeException("Expected " + duration + " points, got "
					+ points.length);
		}
		if (start.length != end.length) {
			throw new RuntimeException(
					"Start and end frames have different number of values");
		}
		allFrames[0] = new int[start.length];
		for (int i = 0; i < start.length; i++) {
			allFrames[0][i] = start[i];
		}
		for (int f = 1; f <= duration; f++) {
			int[] pts = points[f - 1];
			if (pts.length != end.length - 1) {
				throw new RuntimeException("In point set " + f + " expected "
						+ (end.length - 1) + " values but got " + pts.length);
			}
			allFrames[f] = new int[end.length];
			allFrames[f][0] = f + startFrame;
			for (int i = 1; i < end.length; i++) {
				allFrames[f][i] = pts[i - 1];
			}
		}
		for (int i = 1; i < end.length; i++) {
			if (allFrames[duration][i] != end[i]) {
				throw new RuntimeException("End point value " + i + " mismatch:  "
						+ end[i] + " != " + allFrames[duration][i]);
			}
		}
		trimUnneededKeyFrames(startFrame, keyFrames, end, allFrames);
	}

	public double evaluate(double t, double b, double c, double d) {
		throw new RuntimeException("not implemented, shouldn't be called");
	}

}
