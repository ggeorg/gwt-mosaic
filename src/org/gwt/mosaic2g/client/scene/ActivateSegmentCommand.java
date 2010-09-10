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
package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

/**
 * 
 * @author ggeorg
 */
public class ActivateSegmentCommand implements Command {

	protected final Segment segment;
	protected final boolean push;
	protected final boolean pop;

	public ActivateSegmentCommand(Segment s) {
		this(s, false, false);
	}

	public ActivateSegmentCommand(Segment s, boolean push, boolean pop) {
		this.segment = s;
		this.push = push;
		this.pop = pop;
	}

	public boolean isPush() {
		return push;
	}

	public boolean isPop() {
		return pop;
	}

	public Segment getSegment() {
		return segment;
	}

	public void execute() {
		final Show show = segment.getShow();
		if (push) {
			segment.getShow().pushCurrentSegment();
		}
		Segment seg = segment;
		if (pop) {
			seg = segment.getShow().popSegmentStack();
		}
		if (seg != null) {
			show.doActivateSegment(seg);
		}
	}

}
