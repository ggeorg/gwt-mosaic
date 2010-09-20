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

import org.gwt.mosaic2g.client.util.Queue;

import com.google.gwt.user.client.Command;

/**
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public final class Show implements Node {

	private Queue pendingCommands = new Queue(32);
	private boolean deferringPendingCommands = false;

	// For push/pop segment commands
	private Segment[] segmentStack = new Segment[0];
	private int segmentStackPos = 0;
	private ActivateSegmentCommand popSegmentCommand = null;

	private Segment currentSegment = null;

	public Segment getCurrentSegment() {
		return currentSegment;
	}

	public void setCurrentSegment(Segment currentSegment) {
		this.currentSegment = currentSegment;
	}

	/**
	 * The animation engine calls this method just before calling
	 * {@link #nextFrame()} if the animation loop is caught up. From time to
	 * time pending commands will be deferred until animation caught up.
	 */
	void setCaughtUp() {
		deferringPendingCommands = false;
	}

	void nextFrame(Scene scene) {
		// if (destroyed) {
		// return;
		// }
		
		while (!deferringPendingCommands && !pendingCommands.isEmpty()) {
			Command c = (Command) pendingCommands.remove();
			if (c != null) {
				c.execute(); // Can call deferNextCommands()
			}
		}
		
		if (currentSegment != null) {
			currentSegment.nextFrameForActiveFeatures(scene);
		}
	}

	void paintFrame(Scene scene) {
		// if (destroyed) {
		// return;
		// }
		
		if (currentSegment != null) {
			currentSegment.paintFrame(scene);
		}
		
		assert !deferringPendingCommands;
	}

	public void deferNextCommands() {
		deferringPendingCommands = true;
	}

	/**
	 * Run the given command when we advance to the next frame. If the show has
	 * been destroyed, this has no effect.
	 * 
	 * @param cmd
	 *            the command to run
	 */
	public void runCommand(Command cmd) {
		pendingCommands.add(cmd);
	}

	/**
	 * Run the given commands when we advance to the next frame. If the show has
	 * been destroyed, this has no effect.
	 * 
	 * @param cmds
	 *            the commands to run
	 */
	public void runCommands(Command[] cmds) {
		if (cmds == null || cmds.length == 0) {
			return;
		}
		for (int i = 0; i < cmds.length; i++) {
			pendingCommands.add(cmds[i]);
		}
	}

	/**
	 * Set the current segment. This is the main way an application controls
	 * what is being displayed on the screen. The new segment will become
	 * current when we advance to the next frame.
	 * <p>
	 * The current segment is not pushed onto the segment activation stack.
	 * 
	 * @param s
	 *            the segment to activate, or {@code null} to pop the segment
	 *            activation stack
	 */
	public void activateSegment(Segment s) {
		activateSegment(s, false);
	}

	public void activateSegment(Segment s, boolean push) {
		if (s == null) {
			runCommand(popSegmentCommand);
		} else {
			runCommand(s.getCommandToActivate(push));
		}
	}

	/**
	 * Used by the {@link ActiveSegmentCommand}, never call this method
	 * directly.
	 */
	void pushCurrentSegment() {
		segmentStack[segmentStackPos] = currentSegment;
		segmentStackPos = (segmentStackPos + 1) % segmentStack.length;
	}

	/**
	 * Used by the {@link ActiveSegmentCommand}, never call this method
	 * directly.
	 */
	Segment popSegmentStack() {
		if (--segmentStackPos < 0) {
			segmentStackPos = segmentStack.length - 1;
		}
		Segment result = segmentStack[segmentStackPos];
		segmentStack[segmentStackPos] = null;
		return result;
	}

	/**
	 * Used by the {@link ActiveSegmentCommand}, never call this method
	 * directly.
	 * 
	 * @param newSegment
	 *            the new segment to activate
	 */
	void doActivateSegment(Segment newSegment) {
		Segment oldSegment = currentSegment;
		currentSegment = newSegment;
		currentSegment.activate(oldSegment);
	}

}
