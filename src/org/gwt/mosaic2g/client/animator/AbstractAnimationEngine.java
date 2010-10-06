package org.gwt.mosaic2g.client.animator;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public abstract class AbstractAnimationEngine {

	private final AnimationClient[] clients;
	private final RepeatingCommand cmd;

	private int frameMillis;

	private boolean running = false;
	private boolean paused = false;

	public AbstractAnimationEngine(AnimationClient[] clients) {
		this.clients = clients;

		cmd = new RepeatingCommand() {
			public boolean execute() {
				return onUpdate();
			}
		};
		setFrameMillis(40);
	}

	public int getFrameMillis() {
		return frameMillis;
	}

	public void setFrameMillis(int frameMillis) {
		this.frameMillis = frameMillis;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = running ? paused : false;
	}

	public final void start() {
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				cancel();

				onStart();

				running = true;

				onUpdate();
				
				sceduleRepeating(frameMillis);
			}
		});
	}
	
	protected abstract void sceduleRepeating(int frameMillis2);

	protected abstract void onStart();

	protected abstract boolean onUpdate();

	protected abstract void onComplete();
	
	public final void cancel() {
		if (!isRunning()) {
			return;
		}

		doCancel();

		onComplete();

		running = false;
	}

	protected abstract void doCancel();
}
