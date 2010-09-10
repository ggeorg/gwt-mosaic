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
package org.gwt.mosaic2g.rebind;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.gwt.mosaic2g.client.scene.InterpolatedModel;
import org.gwt.mosaic2g.client.scene.Translator;

import com.google.gwt.uibinder.rebind.MortalLogger;

public class ShowParser {

	private final Lexer lexer;

	public ShowParser(Reader reader, String grinFile, MortalLogger logger) {
		lexer = new Lexer(reader, grinFile, this, logger);
	}

	public AbstractInterpolatedModel parse() throws IOException {
		String tok = lexer.getString();

		// Current token is in tok
		int lineStart = lexer.getLineNumber();
		if (tok == null) {
			lexer.reportError("EOF unexpected");
		} else if ("translator_model".equals(tok)) {
			return parseTranslatorModel(lineStart);
		} else if ("scaling_model".equals(tok)) {
			return parseScalingModel(lineStart);
		} else if ("fade_model".equals(tok)) {
			return parseFadeModel(lineStart);
		}

		return null;
	}

	private AbstractInterpolatedModel parseFadeModel(int lineStart) throws IOException {
		parseExpected("{");
		ArrayList<int[]> keyFrames = new ArrayList<int[]>();
		String tok = lexer.getString();
		for (;;) {
			if ("}".equals(tok)) {
				break;
			}
			int frameNum = 0;
			try {
				frameNum = Integer.decode(tok).intValue();
			} catch (NumberFormatException ex) {
				lexer.reportError(ex.toString());
			}
			int alpha = lexer.getInt();
			EasingEquation[] easing = new EasingEquation[1];
			tok = lexer.getString();
			tok = parseEasing(tok, easing);
			if (keyFrames.size() == 0 || easing[0] == null) {
				keyFrames.add(new int[] { frameNum, alpha });
			} else {
				int[] destination = { frameNum, alpha };
				try {
					easing[0].addKeyFrames(keyFrames, destination);
				} catch (Exception ex) {
					lexer.reportError("Easing:  " + ex.getMessage());
				}
			}
		}
		tok = lexer.getString();
		int repeatFrame;
		if ("repeat".equals(tok)) {
			repeatFrame = lexer.getInt();
			tok = lexer.getString();
		} else {
			repeatFrame = Integer.MAX_VALUE; // Off the end
		}
		int loopCount = 1;
		if ("loop_count".equals(tok)) {
			loopCount = lexer.getIntOrInfinite();
			tok = lexer.getString();
		}
		if (!(";".equals(tok))) {
			lexer.reportError("\";\" expected, \"" + tok + "\" seen");
		}
		int[] fs = new int[keyFrames.size()];
		int[] alphas = new int[keyFrames.size()];
		for (int i = 0; i < keyFrames.size(); i++) {
			int[] el = keyFrames.get(i);
			fs[i] = el[0];
			alphas[i] = el[1];
			if (i > 0 && fs[i] <= fs[i - 1]) {
				lexer.reportError("Frame number must be increasing");
			}
			if (alphas[i] < 0 || alphas[i] > 255) {
				lexer.reportError("Illegal alpha value:  " + alphas[i]);
			}
		}
		if (fs.length < 1) {
			lexer.reportError("Need at least one keyframe");
		}
		if (fs[0] != 0) {
			lexer.reportError("Keyframes must start at frame 0");
		}
		return new FadeModel(fs, alphas, repeatFrame, loopCount);
	}

	private AbstractInterpolatedModel parseScalingModel(int lineStart) throws IOException {
		parseExpected("{");
		ArrayList<int[]> keyframes = new ArrayList<int[]>();
		for (;;) {
			String tok = lexer.getString();
			if ("}".equals(tok)) {
				break;
			}
			int frameNum = 0;
			try {
				frameNum = Integer.decode(tok).intValue();
			} catch (NumberFormatException ex) {
				lexer.reportError(ex.toString());
			}
			int x = lexer.getInt();
			int y = lexer.getInt();
			int scaleX = lexer.getInt();
			int scaleY = lexer.getInt();
			tok = lexer.getString();
			EasingEquation[] easing = new EasingEquation[1];
			if (!("mills".equals(tok))) {
				tok = parseEasing(tok, easing);
			}
			if (keyframes.size() == 0 || easing[0] == null) {
				keyframes.add(new int[] { frameNum, x, y, scaleX, scaleY });
			} else {
				int[] destination = { frameNum, x, y, scaleX, scaleY };
				try {
					easing[0].addKeyFrames(keyframes, destination);
				} catch (Exception ex) {
					lexer.reportError("Easing:  " + ex.getMessage());
				}
			}
			lexer.expectString("mills", tok);
		}
		String tok = lexer.getString();
		int repeatFrame = -1;
		if ("repeat".equals(tok)) {
			repeatFrame = lexer.getInt();
			tok = lexer.getString();
		}
		int loopCount = 1;
		if ("loop_count".equals(tok)) {
			loopCount = lexer.getIntOrInfinite();
			tok = lexer.getString();
		}

		if (!(";".equals(tok))) {
			lexer.reportError("\";\" expected, \"" + tok + "\" seen");
		}
		int[] fs = new int[keyframes.size()];
		int[][] values = new int[4][];
		values[0] = new int[keyframes.size()];
		values[1] = new int[keyframes.size()];
		values[2] = new int[keyframes.size()];
		values[3] = new int[keyframes.size()];
		for (int i = 0; i < keyframes.size(); i++) {
			int[] el = keyframes.get(i);
			fs[i] = el[0];
			values[InterpolatedModel.SCALE_X_FIELD][i] = el[1];
			values[InterpolatedModel.SCALE_Y_FIELD][i] = el[2];
			values[InterpolatedModel.SCALE_X_FACTOR_FIELD][i] = el[3];
			values[InterpolatedModel.SCALE_Y_FACTOR_FIELD][i] = el[4];
			if (i > 0 && fs[i] <= fs[i - 1]) {
				lexer.reportError("Frame number must be increasing");
			}
		}
		if (fs.length < 1) {
			lexer.reportError("Need at least one keyframe");
		}
		if (fs[0] != 0) {
			lexer.reportError("Keyframes must start at frame 0");
		}
		if (repeatFrame == -1) {
			repeatFrame = Integer.MAX_VALUE; // Make it stick at end
		} else if (repeatFrame > fs[fs.length - 1]) {
			lexer.reportError("repeat > max frame");
		}
		return new ScalingModel(fs, values, repeatFrame, loopCount);
	}

	private AbstractInterpolatedModel parseTranslatorModel(int lineStart) throws IOException {
		parseExpected("{");
		ArrayList<int[]> keyFrames = new ArrayList<int[]>();
		String tok = lexer.getString();
		for (;;) {
			if ("}".equals(tok)) {
				tok = lexer.getString();
				break;
			}
			int frameNum = 0;
			try {
				frameNum = Integer.decode(tok).intValue();
			} catch (NumberFormatException ex) {
				lexer.reportError(ex.toString());
			}
			int x = lexer.getIntOrOffscreen();
			int y = lexer.getIntOrOffscreen();
			String tweenType = lexer.getString();
			EasingEquation[] easing = new EasingEquation[1];
			if ("linear-relative".equals(tweenType)) {
				tweenType = "linear";
			}
			tok = parseEasing(tweenType, easing);
			if (keyFrames.size() == 0 || easing[0] == null) {
				keyFrames.add(new int[] { frameNum, x, y });
			} else {
				int[] destination = { frameNum, x, y };
				try {
					easing[0].addKeyFrames(keyFrames, destination);
				} catch (Exception ex) {
					lexer.reportError("Easing:  " + ex.getMessage());
				}
			}
		}
		int repeatFrame = -1;
		if ("repeat".equals(tok)) {
			repeatFrame = lexer.getInt();
			tok = lexer.getString();
		}
		int loopCount = 1;
		if ("loop_count".equals(tok)) {
			loopCount = lexer.getIntOrInfinite();
			tok = lexer.getString();
		}
		if (!(";".equals(tok))) {
			lexer.reportError("\";\" expected, \"" + tok + "\" seen");
		}
		int[] fs = new int[keyFrames.size()];
		int[][] values = new int[2][];
		values[0] = new int[keyFrames.size()];
		values[1] = new int[keyFrames.size()];
		for (int i = 0; i < keyFrames.size(); i++) {
			int[] el = keyFrames.get(i);
			fs[i] = el[0];
			values[Translator.X_FIELD][i] = el[1];
			values[Translator.Y_FIELD][i] = el[2];
			if (i > 0 && fs[i] <= fs[i - 1]) {
				lexer.reportError("Frame number must be increasing");
			}
		}
		if (fs.length < 1) {
			lexer.reportError("Need at least one keyframe");
		}
		if (fs[0] != 0) {
			lexer.reportError("Keyframes must start at frame 0");
		}
		if (repeatFrame == -1) {
			repeatFrame = Integer.MAX_VALUE; // Make it stick at end
		} else if (repeatFrame > fs[fs.length - 1]) {
			lexer.reportError("repeat > max frame");
		}
		return new TranslatorModel(fs, values, repeatFrame, loopCount);
	}

	/*
	 * Parsethe easing, and set easing[0] to that value. Return the next token
	 * to be parsed.
	 */
	private String parseEasing(String tweenType, EasingEquation[] easing)
			throws IOException {
		String tok = lexer.getString();

		if ("linear".equals(tweenType)) {
			// Nothing special needed
		} else if ("start".equals(tweenType)) {
			// start is a synonym for linear-relative
		} else if ("ease-in-quad".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInQuad(t, b, c, d);
				}
			};
		} else if ("ease-out-quad".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutQuad(t, b, c, d);
				}
			};
		} else if ("ease-in-out-quad".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutQuad(t, b, c, d);
				}
			};
		} else if ("ease-in-cubic".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInCubic(t, b, c, d);
				}
			};
		} else if ("ease-out-cubic".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutCubic(t, b, c, d);
				}
			};
		} else if ("ease-in-out-cubic".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutCubic(t, b, c, d);
				}
			};
		} else if ("ease-in-quart".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInQuart(t, b, c, d);
				}
			};
		} else if ("ease-out-quart".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutQuart(t, b, c, d);
				}
			};
		} else if ("ease-in-out-quart".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutQuart(t, b, c, d);
				}
			};
		} else if ("ease-in-quint".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInQuint(t, b, c, d);
				}
			};
		} else if ("ease-out-quint".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutQuint(t, b, c, d);
				}
			};
		} else if ("ease-in-out-quint".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutQuint(t, b, c, d);
				}
			};
		} else if ("ease-in-sine".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInSine(t, b, c, d);
				}
			};
		} else if ("ease-out-sine".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutSine(t, b, c, d);
				}
			};
		} else if ("ease-in-out-sine".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutSine(t, b, c, d);
				}
			};
		} else if ("ease-in-expo".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInExpo(t, b, c, d);
				}
			};
		} else if ("ease-out-expo".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutExpo(t, b, c, d);
				}
			};
		} else if ("ease-in-out-expo".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutExpo(t, b, c, d);
				}
			};
		} else if ("ease-in-circ".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInCirc(t, b, c, d);
				}
			};
		} else if ("ease-out-circ".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutCirc(t, b, c, d);
				}
			};
		} else if ("ease-in-out-circ".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutCirc(t, b, c, d);
				}
			};
		} else if ("ease-in-elastic".equals(tweenType)) {
			final double[] a = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "amplitude", a);
			final double[] p = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "period", p);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInElastic(t, b, c, d, a[0], p[0]);
				}
			};
		} else if ("ease-out-elastic".equals(tweenType)) {
			final double[] a = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "amplitude", a);
			final double[] p = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "period", p);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutElastic(t, b, c, d, a[0], p[0]);
				}
			};
		} else if ("ease-in-out-elastic".equals(tweenType)) {
			final double[] a = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "amplitude", a);
			final double[] p = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "period", p);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing
							.easeInOutElastic(t, b, c, d, a[0], p[0]);
				}
			};
		} else if ("ease-in-back".equals(tweenType)) {
			final double[] s = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "overshoot", s);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInBack(t, b, c, d, s[0]);
				}
			};
		} else if ("ease-out-back".equals(tweenType)) {
			final double[] s = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "overshoot", s);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutBack(t, b, c, d, s[0]);
				}
			};
		} else if ("ease-in-out-back".equals(tweenType)) {
			final double[] s = { PennerEasing.DEFAULT };
			tok = getOptionalParameter(tok, "overshoot", s);
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutBack(t, b, c, d, s[0]);
				}
			};
		} else if ("ease-in-bounce".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInBounce(t, b, c, d);
				}
			};
		} else if ("ease-out-bounce".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeOutBounce(t, b, c, d);
				}
			};
		} else if ("ease-in-out-bounce".equals(tweenType)) {
			easing[0] = new EasingEquation() {
				public double evaluate(double t, double b, double c, double d) {
					return PennerEasing.easeInOutBounce(t, b, c, d);
				}
			};
		} else if ("ease-points".equals(tweenType)) {
			lexer.expectString("{", tok);
			easing[0] = parseEasePoints();
			tok = lexer.getString();
		} else {
			lexer.reportError("unknown tween type \"" + tweenType + "\"");
		}
		if ("max-error".equals(tok)) {
			int maxError = lexer.getInt();
			tok = lexer.getString();
			if (easing[0] == null) {
				// It's OK to specify a max-error for linear interpolation,
				// but it doesn't mean anything, since there's no
				// approximation with linear interpolation.
			} else {
				easing[0].setMaxError(maxError);
			}
		}
		return tok;
	}

	private EasingEquation parseEasePoints() throws IOException {
		ArrayList<int[]> frames = new ArrayList<int[]>();
		for (;;) {
			String tok = lexer.getString();
			if ("}".equals(tok)) {
				break;
			} else if (!("(".equals(tok))) {
				lexer.reportError("\"(\" expected");
			}
			ArrayList<Integer> values = new ArrayList<Integer>();
			for (;;) {
				tok = lexer.getString();
				if (")".equals(tok)) {
					break;
				}
				values.add(new Integer(lexer.convertToInt(tok)));
			}
			int[] arr = new int[values.size()];
			for (int i = 0; i < values.size(); i++) {
				arr[i] = values.get(i).intValue();
			}
			frames.add(arr);
		}
		int[][] points = frames.toArray(new int[frames.size()][]);
		return new PointsEasingEquation(points);
	}

	//
	// Get an optional parameter named name. Returns the next token after
	// the optional parameter. Delivers result to value[0].
	//
	private String getOptionalParameter(String tok, String name, double[] value)
			throws IOException {
		if (name.equals(tok)) {
			value[0] = lexer.getDouble();
			return lexer.getString();
		} else {
			return tok;
		}
	}

	/**
	 * Parses a token that we expect to see. A token is read, and if it's not
	 * the expected token, an IOException is generated. This can be useful for
	 * things like parsing the ";" at the end of various constructs.
	 **/
	private void parseExpected(String expected) throws IOException {
		lexer.parseExpected(expected);
	}

}
