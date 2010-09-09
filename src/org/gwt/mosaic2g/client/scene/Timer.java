package org.gwt.mosaic2g.client.scene;

import com.google.gwt.user.client.Command;

public class Timer extends Feature {

	private final InterpolatedModel model;

	public Timer(Show show, int numFrames, Command[] commands) {
		this(show, numFrames, true, commands);
	}

	public Timer(Show show, int numFrames, boolean repeat, Command[] commands) {
		super(show);
		model = InterpolatedModel.makeTimer(numFrames, repeat, commands);
	}

	public Command[] getCommands() {
		return model.getEndCommands();
	}

	public void setCommands(Command[] endCommands) {
		model.setEndCommands(endCommands);
	}

	@Override
	public boolean nextFrame(Scene scene) {
		model.nextFrame(scene);
		return changed;
	}

	@Override
	protected void setSetupMode(boolean mode) {
		// do nothing
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			model.activate();
		}
	}

	@Override
	public void markAsChanged() {
		// do nothing
	}

}
