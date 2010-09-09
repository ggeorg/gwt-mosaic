package org.gwt.mosaic2g.client.scene;

import org.gwt.mosaic2g.client.util.Queue;

import com.google.gwt.user.client.Command;

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
