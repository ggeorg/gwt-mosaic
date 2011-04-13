package gwt.mosaic.client.effects;

import gwt.mosaic.client.effects.easing.Easing;
import gwt.mosaic.client.effects.easing.Easing.Type;

import com.google.gwt.animation.client.Animation;

public abstract class Transition extends Animation {

	private final Easing easing;
	private final Easing.Type type;

	public Transition(Easing easing, Easing.Type type) {
		this.easing = easing;
		this.type = type;
	}

	public Easing getEasing() {
		return easing;
	}

	public Easing.Type getType() {
		return type;
	}

	@Override
	protected final double interpolate(double progress) {
		// return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
		if (type == Type.EASE_IN) {
			return easing.easeIn(progress, 0, progress, 1.0);
		} else if (type == Type.EASE_OUT) {
			return easing.easeOut(progress, 0, progress, 1.0);
		} else if (type == Type.EASE_IN_OUT) {
			return easing.easeInOut(progress, 0, progress, 1.0);
		} else {
			return progress;
		}
	}

	@Override
	protected abstract void onUpdate(double progress);

}
