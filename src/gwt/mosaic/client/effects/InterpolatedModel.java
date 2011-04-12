/*
 * Copyright 2010 ArkaSoft LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*  
 * Copyright (c) 2007, Sun Microsystems, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  Note:  In order to comply with the binary form redistribution
 *         requirement in the above license, the licensee may include
 *         a URL reference to a copy of the required copyright notice,
 *         the list of conditions and the disclaimer in a human readable
 *         file with the binary form of the code that is subject to the
 *         above license.  For example, such file could be put on a
 *         Blu-ray disc containing the binary form of the code or could
 *         be put in a JAR file that is broadcast via a digital television
 *         broadcast medium.  In any event, you must include in any end
 *         user licenses governing any code that includes the code subject
 *         to the above license (in source and/or binary form) a disclaimer
 *         that is at least as protective of Sun as the disclaimers in the
 *         above license.
 *
 *         A copy of the required copyright notice, the list of conditions and
 *         the disclaimer will be maintained at
 *         https://hdcookbook.dev.java.net/misc/license.html .
 *         Thus, licensees may comply with the binary form redistribution
 *         requirement with a text file that contains the following text:
 *
 *             A copy of the license(s) governing this code is located
 *             at https://hdcookbook.dev.java.net/misc/license.html
 */
package gwt.mosaic.client.effects;

import com.google.gwt.user.client.Command;

/**
 * An {@code InterpolatedModel} controls one or mode integer factors, and
 * interpolates their values according to key frames. At the end of the
 * sequence, the {@code InterpolatedModel} can kick off a command, it can repeat
 * the animation, or it can stick at the last frame.
 * <p>
 * An {@code InterpolatedModel} with no values can function as a timer. A timer
 * simply has a number of key frames, and triggers a set of commands after those
 * key frames.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class InterpolatedModel {

	/**
	 * For a scale_model, the field for the X value
	 **/
	public final static int SCALE_X_FIELD = 0;

	/**
	 * For a scale_model, the field for the Y value
	 **/
	public final static int SCALE_Y_FIELD = 1;

	/**
	 * For a scale_model, the field for the horizontal scale factor in mills
	 * (1/1000)
	 **/
	public final static int SCALE_X_FACTOR_FIELD = 2;

	/**
	 * For a scale_model, the field for the vertical scale factor in mills
	 * (1/1000)
	 **/
	public final static int SCALE_Y_FACTOR_FIELD = 3;

	/**
	 * Frame number of key frames, frames[0] is always 0.
	 */
	/* private */final int[] frames;

	/**
	 * Current value of each field.
	 */
	private final int[] currValues;

	/**
	 * Values at keyframe, indexed by field. The array for a field can be
	 * {@code null}, in which case the initial value of currValues will be
	 * maintained.
	 */
	private final int[][] values;

	/**
	 * Frame to go after the end. {@code Integer.MAX_VALUE} means
	 * "stick at end", {@code 0} will case a cycle.
	 */
	private final int repeatFrame;

	/**
	 * # of times to repeat images before sending end commands,
	 * {@code Integer.MAX_VALUE} means "infinite".
	 */
	private final int loopCount;

	private int currFrame; // current frame in cycle
	private int currIndex; // frames[index] <= currFrame <= frames[index+1]
	private int repeatIndex; // index when currFrame is repeatFrame 1
	private int loopsRemaining; // see loopCount

	protected Command endCommand;

	public InterpolatedModel(int[] frames, int[] currValues, int[][] values) {
		this(frames, currValues, values, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public InterpolatedModel(int[] frames, int[] currValues, int[][] values,
			int repeatFrame, int loopCount) {
		this.frames = frames;
		this.currValues = currValues;
		this.values = values;
		this.repeatFrame = repeatFrame;
		this.loopCount = loopCount;

		// initialize model
		if (repeatFrame == Integer.MAX_VALUE) {
			repeatIndex = Integer.MAX_VALUE;
		} else {
			repeatIndex = 0;
			// This is tricky. We must calculate the index such
			// that frames[i] <= (repeatFrame - 1) < frames[i+1]
			while (repeatFrame - 1 >= frames[repeatIndex + 1]) {
				repeatIndex++;
			}
			// now repeatFrame-1 < frames[repeatIndex + 1]
			// and repeatFrame-1 >= frames[repeatIndex]
		}
	}

	/**
	 * Give the current value for the given field.
	 * 
	 * @param fieldNum
	 *            the field number, counting from {@code 0}
	 * @return the field value
	 */
	public final int getField(int fieldNum) {
		/*
		 * If this is an automatically generated value, then it only has a
		 * meaningful value if we're activated.
		 */
		// TODO assert

		return currValues[fieldNum];
	}

	/**
	 * Sets the value of a field to {@code value}. This method may be called by
	 * the application code, so long as it's called within a command body. It is
	 * an error to call this method for any field that is changed by the normal
	 * interpolation scheme, and trying to do so may result in an assertion
	 * failure. In other words, if you want to programmatically control a value,
	 * make sure that every keyframe has the same (initial) value for that
	 * field.
	 * 
	 * @param fieldNum
	 *            the field number, counting from {@code 0}
	 * @param value
	 *            the field value
	 */
	public final void setField(int fieldNum, int value) {
		// this is a value that is interpolated
		assert (values[fieldNum] == null);
		// GWT.log("XXX assert (values[fieldNum] == null)");
		currValues[fieldNum] = value;
	}

	public Command getEndCommand() {
		return endCommand;
	}

	public void setEndCommand(Command endCommand) {
		this.endCommand = endCommand;
	}

	protected void activate() {
		loopsRemaining = loopCount;
		if (frames.length <= 1) {
			currFrame = Integer.MAX_VALUE;
			currIndex = Integer.MAX_VALUE;
		} else {
			currFrame = 0;
			currIndex = 0;
			for (int i = 0; i < currValues.length; i++) {
				if (values[i] != null) {
					currValues[i] = values[i][0];
				}
			}
		}
	}

	protected boolean nextFrame() {
		if (currFrame == Integer.MAX_VALUE) {
			return false;
		}

		currFrame++;
		int nextIndex = currIndex + 1;
		int dist = frames[nextIndex] - frames[currIndex];
		int distNext = frames[nextIndex] - currFrame;
		int distLast = currFrame - frames[currIndex];

		/*
		 * distNext, the distance to the next value, should never be less than
		 * zero, except for one special case. That special case is a one-frame
		 * timer, because the frames array for a one-frame timer contains { 0 ,
		 * 0 }.
		 */
		assert !(distLast < 0 || (distNext < 0 && frames[nextIndex] != 0));

		for (int i = 0; i < currValues.length; i++) {
			int[] vs = values[i];

			if (vs != null) {
				currValues[i] = (vs[nextIndex] * distLast + vs[currIndex]
						* distNext)
						/ dist;
			}
		}

		if (distNext <= 0) {
			// It will be -1 in the case of a one-frame timer
			currIndex = nextIndex;
			if (currIndex + 1 >= frames.length) {
				if (loopCount != Integer.MAX_VALUE) {
					loopsRemaining--;
				}
				if (loopsRemaining > 0) {
					if (repeatFrame == Integer.MAX_VALUE) {
						currFrame = 0;
						currIndex = 0;
					} else {
						currFrame = repeatFrame;
						if (currFrame != Integer.MAX_VALUE) {
							currFrame--;
						}
						currIndex = repeatIndex;
					}
				} else {
					loopsRemaining = loopCount;
					currFrame = repeatFrame;
					if (currFrame != Integer.MAX_VALUE) {
						currFrame--;
					}
					currIndex = repeatIndex;
					if (endCommand != null) {
						endCommand.execute();
					}
				}
			}
		}

		return true;
	}

	/**
	 * Scale the x, y, width and heigh values according to the current values of
	 * SCALE_X_FIELD, SCALE_Y_FIELD, SCALE_X_FACTOR_FIELD and
	 * SCALE_Y_FACTOR_FIELD, storing them in scaledBounds. This, of course,
	 * depends on this InterpolatedModel having these four values in it, which
	 * means that this is a scaling_model.
	 * 
	 * @return true if the value of scaledBounds have changed, false otherwise.
	 **/
	// public boolean scaleBounds(int x, int y, int width, int height,
	// Rectangle scaledBounds) {
	// int dx = getField(SCALE_X_FIELD);
	// int dy = getField(SCALE_Y_FIELD);
	// int xf = getField(SCALE_X_FACTOR_FIELD);
	// int yf = getField(SCALE_Y_FACTOR_FIELD);
	//
	// x = (x - dx) * xf;
	// if (x < 0) {
	// x -= 500;
	// } else {
	// x += 500;
	// }
	// x = (x / 1000) + dx;
	//
	// y = (y - dy) * yf;
	// if (y < 0) {
	// y -= 500;
	// } else {
	// y += 500;
	// }
	// y = (y / 1000) + dy;
	//
	// width *= xf;
	// if (width < 0) {
	// width -= 500;
	// } else {
	// width += 500;
	// }
	// width /= 1000;
	//
	// height *= yf;
	// if (height < 0) {
	// height -= 500;
	// } else {
	// height += 500;
	// }
	// height /= 1000;
	//
	// if (x != scaledBounds.x || y != scaledBounds.y
	// || width != scaledBounds.width || height != scaledBounds.height) {
	// scaledBounds.reshape(x, y, width, height);
	// return true;
	// } else {
	// return false;
	// }
	// }

	// -----------------

	public static InterpolatedModel makeDefaultFadeModel() {
		return makeFadeModel(new int[] { 0 }, new int[] { 255 },
				Integer.MAX_VALUE, 0, null);
	}

	public static InterpolatedModel makeFadeModel(int[] frames,
			int[] fadeValues, int repeatFrame, int loopCount, Command endCommand) {
		int[][] values = new int[][] { fadeValues };
		return makeInterpolatedModel(frames, values, repeatFrame, loopCount,
				endCommand);
	}

	public static InterpolatedModel makeTimer(int numFrames, boolean repeat,
			Command command) {
		int[] frames = new int[] { 0, numFrames - 1 };
		// That means keyframes from 0 through numFrames-1, which is a total of
		// numFrames frames. For example, a timer that's one frame long runs
		// from frame 0 through frame 0.

		int[][] values = new int[0][];
		int repeatFrame = repeat ? 0 : Integer.MAX_VALUE;
		int loopCount = 1;

		/*
		 * Timer can be implemented as a degenerate case of InterpolateModel.
		 * It's just a model that interpolates zero data values between frame 0
		 * and frame numFrames.
		 */
		return makeInterpolatedModel(frames, values, repeatFrame, loopCount,
				command);
	}

	public static InterpolatedModel makeDefaultTranslatorModel() {
		return InterpolatedModel.makeTranslatorModel(new int[] { 0 },
				new int[][] { { 0 }, { 0 } }, Integer.MAX_VALUE, 0, null);
	}

	public static InterpolatedModel makeTranslatorModel(int[] frames,
			int[][] values, int repeatFrame, int loopCount, Command endCommand) {
		return makeInterpolatedModel(frames, values, repeatFrame, loopCount,
				endCommand);
	}

	public static InterpolatedModel makeDefaultScalingModel() {
		return makeScalingModel(new int[] { 0 }, new int[][] { { 1000 },
				{ 1000 } }, Integer.MAX_VALUE, 0, null);
	}

	public static InterpolatedModel makeScalingModel(int[] frames,
			int[][] values, int repeatFrame, int loopCount, Command command) {
		return makeInterpolatedModel(frames, values, repeatFrame, loopCount,
				command);
	}

	public static InterpolatedModel makeInterpolatedModel(int[] frames,
			int[][] values, int repeatFrame, int loopCount, Command endCommand) {
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
		InterpolatedModel result = new InterpolatedModel(frames, currValues,
				values, repeatFrame, loopCount);
		result.setEndCommand(endCommand);
		return result;
	}

}
