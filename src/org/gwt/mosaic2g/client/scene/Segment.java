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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.gwt.user.client.Command;

/**
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public final class Segment implements Node, HasFeatures {

	private final FeatureCollection activeFeatures = new FeatureCollection(this);
	private final Map<Feature, Boolean> featureWasActiveted = new HashMap<Feature, Boolean>();
	// XXX do we really need this map?

	private Command onEntryCommand;
	private Command onExitCommand;

	private boolean active = false;

	// # of features in setup clause and activate clause that have
	// been checked so far for setup
	private int setupCheckedInActive;

	private ActivateSegmentCommand cmdToActivate;
	private ActivateSegmentCommand cmdToActivatePush;

	private final Show show;

	public Segment(Show show) {
		this(show, null, null);
	}

	public Segment(Show show, Command onEntryCommand, Command onExitCommand) {
		this.show = show;
		this.onEntryCommand = onEntryCommand;
		this.onExitCommand = onExitCommand;
	}

	public Show getShow() {
		return show;
	}

	public Command getOnEntryCommand() {
		return onEntryCommand;
	}

	public void setOnEntryCommand(Command onEntryCommand) {
		this.onEntryCommand = onEntryCommand;
	}

	public Command getOnExitCommand() {
		return onExitCommand;
	}

	public void setOnExitCommand(Command onExitCommand) {
		this.onExitCommand = onExitCommand;
	}

	public boolean isActive() {
		return active;
	}

	/**
	 * Used by {@link Show}, never call this method directly.
	 */
	Command getCommandToActivate(boolean push) {
		if (push) {
			if (cmdToActivatePush == null) {
				cmdToActivatePush = new ActivateSegmentCommand(this, true,
						false);
			}
			return cmdToActivatePush;
		} else {
			if (cmdToActivate == null) {
				cmdToActivate = new ActivateSegmentCommand(this);
			}
			return cmdToActivate;
		}
	}

	/**
	 * Used by {@link Show}, never call this method directly.
	 * 
	 * @param lastSegment
	 *            the last segment we're coming from
	 */
	void activate(Segment lastSegment) {
		if (lastSegment == this) {
			return;
		}

		active = true;

		setupCheckedInActive = 0;

		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			f.setup();
			if (!f.needsMoreSetup()) {
				f.activate();
				f.markAsChanged();
				featureWasActiveted.put(f, Boolean.TRUE);
			}
		}

		if (lastSegment != null) {
			lastSegment.deactivate();
		}
		if (onEntryCommand != null) {
			show.runCommand(onEntryCommand);
		}

		runFeatureSetup();
	}

	private boolean getFeatureWasActiveted(Feature f) {
		if (featureWasActiveted.containsKey(f)) {
			return featureWasActiveted.get(f);
		}
		return false;
	}

	/**
	 * When a feature is setup, we get this call. We have to be a little
	 * conservative: it's possible that a feature from a previous, stale segment
	 * could finish its setup after we become the current segment, so this call
	 * really means: one of our features probably finished setup, but we'd
	 * better check to be sure .
	 */
	void runFeatureSetup() {
		// check to see if all active features are set up
		final int length = activeFeatures.size();
		if (setupCheckedInActive < length) {
			while (setupCheckedInActive < length) {
				final Feature f = activeFeatures.get(setupCheckedInActive);
				if (!getFeatureWasActiveted(f) && f.needsMoreSetup()) {
					return;
				}
				++setupCheckedInActive;
			}
			Iterator<Feature> it = activeFeatures.iterator();
			while (it.hasNext()) {
				final Feature f = it.next();
				if (!getFeatureWasActiveted(f)) {
					f.activate();
					f.markAsChanged();
					featureWasActiveted.put(f, Boolean.TRUE);
				}
			}
		}
	}

	/**
	 * Called when another segment is activated, and called on the active
	 * segment when the show is destroyed.
	 */
	void deactivate() {
		active = false;
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			final Feature f = it.next();
			if (getFeatureWasActiveted(f)) {
				f.deactivate();
				featureWasActiveted.put(f, Boolean.FALSE);
			}
			f.unsetup();
		}

		if (onExitCommand != null) {
			show.runCommand(onExitCommand);
		}
	}

	public void nextFrameForActiveFeatures(Scene scene) {
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			it.next().nextFrame(scene);
		}
	}

	public void paintFrame(Scene scene) {
		Iterator<Feature> it = activeFeatures.iterator();
		while (it.hasNext()) {
			it.next().paintFrame(scene);
		}
	}

	public void add(Feature f) {
		activeFeatures.add(f);
	}

	public void clear() {
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	public Iterator<Feature> iterator() {
		return activeFeatures.iterator();
	}

	public boolean remove(Feature f) {
		try {
			activeFeatures.remove(f);
		} catch (NoSuchElementException ex) {
			return false;
		}
		return true;
	}

}
