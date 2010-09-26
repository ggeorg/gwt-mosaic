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

import java.util.Iterator;

import org.gwt.mosaic2g.binding.client.Property;

/**
 * Represents a group of features that are all activated at the same time. It's
 * useful to group features together so that e.g. they can be turned on and off
 * as a unit within an assembly.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Group extends HasFeaturesImpl {

	private InterpolatedModel scalingModel;
	private boolean managedSM;

	public Group(Show show) {
		super(show);
	}

	public InterpolatedModel getScalingModel() {
		return scalingModel;
	}

	public void setScalingModel(InterpolatedModel scalingModel) {
		setScalingModel(scalingModel, false);
	}

	public void setScalingModel(InterpolatedModel scalingModel, boolean managed) {
		this.scalingModel = scalingModel;
		this.managedSM = managed;
	}

	@Override
	public Property<Integer> getX() {
		int x = OFFSCREEN;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getX().$();
			if (val < x) {
				x = val;
			}
		}
		return super.getX().$(x);
	}

	@Override
	public Property<Integer> getY() {
		int y = OFFSCREEN;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getY().$();
			if (val < y) {
				y = val;
			}
		}
		return super.getY().$(y);
	}

	@Override
	public Property<Integer> getWidth() {
		int width = Integer.MIN_VALUE;
		final int left = getX().$();
		final Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			int w = f.getWidth().$();
			if (w == Integer.MIN_VALUE) {
				if (f instanceof Control) {
					if (w == Integer.MIN_VALUE) {
						w = ((Control) f).getPrefWidth();
					}
				} else {
					continue;
				}
			}
			final int fw = w + (f.getX().$() - left); // TODO check offscreen
			if (fw > width) {
				width = fw;
			}
		}
		return super.getWidth().$(width);
	}

	@Override
	public Property<Integer> getHeight() {
		int height = Integer.MIN_VALUE;
		final int top = getY().$();
		final Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			int h = f.getHeight().$();
			if (h == Integer.MIN_VALUE) {
				if (f instanceof Control) {
					if (h == Integer.MIN_VALUE) {
						h = ((Control) f).getPrefHeight();
					}
				} else {
					continue;
				}
			}
			final int fh = h + (f.getY().$() - top); // TODO check offscreen
			if (fh > height) {
				height = fh;
			}
		}
		return super.getHeight().$(height);
	}

	@Override
	protected void setSetupMode(boolean mode) {
		if (scalingModel != null) {
			if (mode) {
				Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					if (f instanceof HasScalingModel) {
						((HasScalingModel) f).setScalingModel(scalingModel,
								true);
					}
				}
			} else {
				Iterator<Feature> it = iterator();
				while (it.hasNext()) {
					final Feature f = it.next();
					if (f instanceof HasScalingModel) {
						((HasScalingModel) f).setScalingModel(null);
					}
				}
			}
		}
		super.setSetupMode(mode);
	}

	@Override
	protected void setActivateMode(boolean mode) {
		super.setActivateMode(mode);
		if (mode) {
			if (scalingModel != null && !managedSM) {
				scalingModel.activate();
			}
			markAsChanged();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		if (scalingModel != null && !managedSM) {
			scalingModel.nextFrame(scene);
		}
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			if (it.next().nextFrame(scene)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		// TODO consult children changes events
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next().paintFrame(scene);
		}
	}

}
