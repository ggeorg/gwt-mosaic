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
package org.gwt.mosaic2g.client.scene;

import org.gwt.mosaic2g.client.style.Opacity;

/**
 * Modifies a child feature by applying an alpha value when drawing in it. This
 * lets you animate a fade-in and fade-out effect. It works be specifying alpha
 * values at a few key frames, and doing linear interpolation between those key
 * frames.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Fade extends Modifier {

	public final static int ALPHA_FIELD = 0;

	private final InterpolatedModel model;

	private final Opacity lastAlpha = new Opacity(Integer.MIN_VALUE);

	public Fade(Show show) {
		this(show, InterpolatedModel.makeDefaultFadeModel());
	}

	public Fade(Show show, InterpolatedModel model) {
		super(show);
		this.model = model;
	}

	public InterpolatedModel getModel() {
		return model;
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			// Setup initial values
			lastAlpha.setOpacity(Integer.MIN_VALUE);
			model.activate();
		}
		super.setActivateMode(mode);
	}

	@Override
	public boolean nextFrame(Scene scene) {
		model.nextFrame(scene);

		int currAlpha = model.getField(ALPHA_FIELD);
		if (currAlpha > 255) {
			currAlpha = 255;
		}
		if (currAlpha < 0) {
			currAlpha = 0;
		}
		if (currAlpha != lastAlpha.getOpacity()) {
			lastAlpha.setOpacity(currAlpha);
			markAsChanged();
		}

		return (changed = super.nextFrame(scene));
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		Opacity old = scene.getOpacity();
		scene.setOpacity(lastAlpha);
		getPart().paintFrame(scene);
		scene.setOpacity(old);
		
		changed = false;
	}
}
