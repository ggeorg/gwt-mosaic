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

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.layout.HasPrefSize;
import org.gwt.mosaic2g.client.scene.layout.Resizable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;

/**
 * A {@code Feature} is a thing that presents some sort of UI. A {@link Segment}
 * presents some number of {@code Feature}s, and {@code Feature}s can be shared
 * between {@code Segment}s.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public abstract class Feature implements Node {
	public static final int OFFSCREEN = Integer.MAX_VALUE;

	private final Show show;

	private int activateCount = 0;
	private int setupCount = 0;

	protected boolean changed;
	protected boolean moved;
	protected boolean resized;

	private Property<Integer> xP;
	private Property<Integer> yP;
	private Property<Integer> widthP;
	private Property<Integer> heightP;

	private int flex;

	private final ValueChangeHandler<Integer> locationChanged = new ValueChangeHandler<Integer>() {
		public void onValueChange(ValueChangeEvent<Integer> event) {
			moved = true;
			markAsChanged();
		}
	};

	private final ValueChangeHandler<Integer> sizeChanged = new ValueChangeHandler<Integer>() {
		public void onValueChange(ValueChangeEvent<Integer> event) {
			resized = true;
			markAsChanged();
		}
	};

	protected Feature(Show show) {
		assert show != null;
		this.show = show;
	}

	public Show getShow() {
		return show;
	}

	/**
	 * Get the upper-left hand corner of this feature as presently displayed.
	 * Return {@code OFFSCREEN} if this feature has no visible representation.
	 * 
	 * @return the x coordinate
	 */
	public Property<Integer> getX() {
		if (xP == null) {
			setX(Property.valueOf(OFFSCREEN));
		}
		return xP;
	}

	protected void setX(Property<Integer> x) {
		if (this.xP == null) {
			this.xP = x;
			this.xP.addValueChangeHandler(locationChanged);
		} else {
			throw new IllegalStateException("Feature.setX() may only be "
					+ "called once.");
		}
	}

	/**
	 * Get the upper-left hand corner of this feature as presently displayed
	 * Return {@code OFFSCREEN} if this feature has no visible representation.
	 * 
	 * @return the y coordinate
	 **/
	public Property<Integer> getY() {
		if (yP == null) {
			setY(Property.valueOf(OFFSCREEN));
		}
		return yP;
	}

	protected void setY(Property<Integer> y) {
		if (this.yP == null) {
			this.yP = y;
			this.yP.addValueChangeHandler(locationChanged);
		} else {
			throw new IllegalStateException("Feature.setY() may only be "
					+ "called once.");
		}
	}

	/**
	 * Get the width of this feature as presently displayed. Return
	 * Integer.MIN_VALUE if this feature has no visible representation.
	 * 
	 * @return the width
	 */
	public Property<Integer> getWidth() {
		if (widthP == null) {
			setWidth(Property.valueOf(Integer.MIN_VALUE));
		}
		return widthP;
	}

	protected void setWidth(Property<Integer> width) {
		if (this.widthP == null) {
			this.widthP = width;
			this.widthP.addValueChangeHandler(sizeChanged);
		} else {
			throw new IllegalStateException("Feature.setWidth() may only be "
					+ "called once.");
		}
	}

	/**
	 * Get the height of this feature as presently displayed. Return
	 * Integer.MIN_VALUE if this feature has no visible representation.
	 * 
	 * @return the height
	 */
	public Property<Integer> getHeight() {
		if (heightP == null) {
			setHeight(Property.valueOf(Integer.MIN_VALUE));
		}
		return heightP;
	}

	protected void setHeight(Property<Integer> height) {
		if (this.heightP == null) {
			this.heightP = height;
			this.heightP.addValueChangeHandler(sizeChanged);
		} else {
			throw new IllegalStateException("Feature.setHeight() may only be "
					+ "called once.");
		}
	}

	/**
	 * Called from {@link Segment} to advance us to the state we should be in
	 * for the next frame. Never call this method directly.
	 */
	public abstract boolean nextFrame(Scene scene);

	/**
	 * Paint the current state of this feature to the {@link scene}. Never call
	 * this method directly.
	 */
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}
		paintDone();
	}

	protected void paintDone() {
		changed = moved = resized = false;
	}

	/**
	 * Mark this feature as modified for the next call to
	 * {@link #paintFrame(Scene)}. This can be called by a parent node on its
	 * children, e.g. by setting a new alpha value.
	 */
	public void markAsChanged() {
		changed = true;
	}

	/**
	 * Called by the show when it's time to begin setting up this feature. This
	 * might be called from the show multiple times; each call will eventually
	 * be matched by a call to {@link #unsetup()}. Never call this method
	 * directly.
	 * 
	 * @see #unsetup()
	 */
	void setup() {
		if (++setupCount == 1) {
			setSetupMode(true);
		}
	}

	/**
	 * Change the setup mode of this feature. The new mode will be always be
	 * different than the old. Custom feature extensions must implements this
	 * method.
	 * 
	 * @param b
	 *            the setup mode of this feature
	 */
	protected abstract void setSetupMode(boolean mode);

	/**
	 * Called by the show when this feature is no longer needed by whatever
	 * contains it. When the last call to {@link #setup()} has been matched by a
	 * call to {@link #unsetup()}, it's time to unload this feature's assets.
	 * 
	 * @see #setup()
	 */
	void unsetup() {
		if (--setupCount == 0) {
			setSetupMode(false);
		}
	}

	/**
	 * Check to see if this feature has been set up, or has a pending request to
	 * be set up.
	 * 
	 * @return {@code true} if this feature has been set up or has a pending
	 *         request to be set up; {@code false} otherwise
	 */
	public final boolean isSetup() {
		return setupCount > 0;
	}

	/**
	 * This is where the feature says whether or not it needs more setup. The
	 * implementation of this method must not call any outside code or call any
	 * show/animation methods. For a given setup cycle, this method is called
	 * only after setup(). Clients should never call this method directly.
	 * 
	 * @return whether or not the feature needs more setup
	 */
	protected boolean needsMoreSetup() {
		return false;
	}

	/**
	 * Called by the show when this feature becomes activated, that is, it
	 * starts presenting. That nest, so this can be called multiple times. When
	 * the last call to {@link #activate()} is undone by a call to
	 * {@link #deactivate()}, that means this feature is no longer being shown.
	 * Never call this method directly.
	 * 
	 * @see #deactivate()
	 */
	void activate() {
		if (++activateCount == 1) {
			setActivateMode(true);
		}
	}

	/**
	 * Change the activated mode of this feature. The new mode will always be
	 * different than the old. Never call this method directly. Custom feature
	 * extensions must implement this method.
	 * 
	 * @param mode
	 *            the activated mode of this feature
	 */
	protected abstract void setActivateMode(boolean mode);

	/**
	 * Called by the show when this feature is no longer being presented by
	 * whatever contains it.
	 * 
	 * @see #activate()
	 */
	void deactivate() {
		if (--activateCount == 0) {
			setActivateMode(false);
		}
	}

	/**
	 * Check to see if this feature has been activated.
	 * 
	 * @return {@code true} if this feature has been activated; {@code false}
	 *         otherwise
	 */
	public final boolean isActivated() {
		return activateCount > 0;
	}

	private Command featureSetupCommand = null;

	/**
	 * When a feature finishes its setup, it should call this to tell the show
	 * about it.
	 */
	protected void sendFeatureSetup() {
		if (featureSetupCommand == null) {
			featureSetupCommand = new FeatureSetupCommand(show);
		}
		show.runCommand(featureSetupCommand);
	}

	/**
	 * Called from the {@link ResetFeatureCommand}, this should reset the
	 * internal state of the feature to what it was when first activated.
	 */
	public final void resetFeature() {
		if (activateCount > 0) {
			setActivateMode(false);
			setActivateMode(true);
		}
	}

	// ---------------------------------------------------------------------

	public boolean instanceOfResizable() {
		return (this instanceof Resizable);
	}

	public boolean instanceOfHasPrefSize() {
		return (this instanceof HasPrefSize);
	}

	public int getPrefWidth() {
		return 0;
	}

	public int getPrefHeight() {
		return 0;
	}

	public int getFlex() {
		return flex;
	}

	public void setFlex(int flex) {
		flex = Math.max(flex, 0);
		if (this.flex == flex) {
			return;
		}
		this.flex = flex;
		this.changed = true;
	}

}
