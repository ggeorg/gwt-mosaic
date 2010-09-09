package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

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
