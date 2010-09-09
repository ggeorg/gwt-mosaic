package org.gwt.mosaic2g.client.animator;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 * An {@code AnimationEngine} runs the main animation loop, and manages display
 * of a set of {@link AnimationClient AnimationClients}.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class AnimationEngine {

	private final Timer animationTimer;
	private final AnimationClient[] clients;

	private int frameMillis;

	private boolean running = false;
	private boolean paused = false;

	private int frame;
	private Duration duration;
	private double lastFrameElapsedMillis;

	public AnimationEngine(AnimationClient[] clients) {
		super();
		this.clients = clients;
		this.animationTimer = new Timer() {
			@Override
			public void run() {
				onUpdate();
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

	public Duration getDuration() {
		return duration;
	}

	public final void start() {
		// Cancel the animation engine if it is running.
		cancel();

		onStart();

		running = true;
		frame = 0;
		lastFrameElapsedMillis = 0.0;
		duration = new Duration();

		onUpdate();
		animationTimer.scheduleRepeating(frameMillis);
	}

	public final void cancel() {
		if (!isRunning()) {
			return;
		}

		animationTimer.cancel();

		onComplete();

		running = false;
	}

	protected void onStart() {
		// TODO initialize all animation clients
	}

	protected void onUpdate() {
		if (paused) {
			return;
		}

		advanceModel();

		double currFrameElapsedMillis = getDuration().elapsedMillis();
		double dt = currFrameElapsedMillis
				- (lastFrameElapsedMillis + frameMillis);

		if (dt <= frameMillis) {
			if (dt > 0) {
				currFrameElapsedMillis -= dt;
			}
			// if we're on time, update the display and return
			showFrame();
		} else {
			if (GWT.isClient()) {
				GWT.log("Frame " + frame + " skipped.");
			}
		}

		lastFrameElapsedMillis = currFrameElapsedMillis;
		frame++;
	}

	protected void advanceModel() {
		// advance model to the next frame
		for (int i = 0; i < clients.length; i++) {
			try {
				clients[i].nextFrame();
			} catch (Exception ex) {
				GWT.log("Exception in advance model to the next frame, "
						+ "animation client: " + i, ex);
			}
		}
	}

	protected void showFrame() {
		// tell the model we're caught up, and show the current frame
		for (int i = 0; i < clients.length; i++) {
			try {
				clients[i].setCaughtUp();
				// clients[i].addDisplayAreas(renderContext);
			} catch (Exception ex) {
				GWT.log("Exception in caught up, animation client: " + i, ex);
			}
		}
		for (int i = 0; i < clients.length; i++) {
			try {
				clients[i].paintFrame();
			} catch (Exception ex) {
				GWT.log("Exception in paint frame, animation client: " + i, ex);
			}
		}
	}

	protected void onComplete() {
		// TODO deinitialize animation clients
	}

}
