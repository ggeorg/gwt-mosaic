package gwt.mosaic.client.effects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public final class AnimationEngine implements RepeatingCommand {

	private static final int DEFAULT_FRAME_DELAY = 40; // 25fps

	private static AnimationEngine instance;

	public static AnimationEngine get() {
		if (instance == null) {
			instance = new AnimationEngine();
		}
		return instance;
	}

	private final List<AnimationClient> animationClients = new ArrayList<AnimationClient>();

	private int delayMs;

	private boolean scheduled, canceled = false;

	private transient double nextFrameTime;

	private AnimationEngine() {
		delayMs = DEFAULT_FRAME_DELAY;
	}

	public int getDelayMs() {
		return delayMs;
	}

	public void setDelayMs(int delayMs) {
		this.delayMs = delayMs;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void stop() {
		// There's no way to "unschedule" a command, so we use a stopped flag.
		canceled = true;
	}

	@Override
	public boolean execute() {
		if (canceled) {
			return false;
		}

		// Duplicate the animations list in case it changes as we iterate over
		// it
		AnimationClient[] curAnimationClients = new AnimationClient[animationClients
				.size()];
		curAnimationClients = animationClients.toArray(curAnimationClients);

		double curTime = Duration.currentTimeMillis();
		double oldNextFrameTime = nextFrameTime;
		nextFrameTime = Duration.currentTimeMillis() + 2.0 * delayMs;

		// Iterator through the animation clients
		for (AnimationClient animationClient : curAnimationClients) {
			animationClient.nextFrame();
			if (curTime < oldNextFrameTime) {
				animationClient.update();
			} else {
				animationClient.nextFrame();
				animationClient.update();
			}
		}

		return (scheduled = animationClients.size() > 0);
	}

	public void start() {
		canceled = false;
		if (!scheduled) {
			scheduled = true;
			nextFrameTime = Duration.currentTimeMillis() + 2.0 * delayMs;
			Scheduler.get().scheduleFixedPeriod(this, delayMs);
		}
	}

	public void add(AnimationClient animationClient) {
		if (animationClient == null) {
			throw new IllegalArgumentException("animationClient is null");
		}
		animationClients.add(animationClient);

		// Start animation if not scheduled
		start();
	}

	public void remove(AnimationClient animationClient) {
		if (animationClient == null) {
			throw new IllegalArgumentException("animationClient is null");
		}
		animationClients.remove(animationClient);
	}

}
