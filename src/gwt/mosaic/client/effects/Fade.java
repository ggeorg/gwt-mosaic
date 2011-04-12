package gwt.mosaic.client.effects;

import gwt.mosaic.client.style.Opacity;

public abstract class Fade implements AnimationClient {

	public final static int ALPHA_FIELD = 0;

	private final InterpolatedModel model;

	private transient final Opacity lastAlpha = new Opacity(Integer.MIN_VALUE);
	private transient boolean changed = false;

	public Fade() {
		this(InterpolatedModel.makeDefaultFadeModel());
	}

	public Fade(InterpolatedModel model) {
		this.model = model;
	}

	public InterpolatedModel getModel() {
		return model;
	}

	@Override
	public final void nextFrame() {
		if (!model.nextFrame()) {
			AnimationEngine.get().remove(this);
			return;
		}

		int currAlpha = model.getField(ALPHA_FIELD);
		if (currAlpha > 255) {
			currAlpha = 255;
		}
		if (currAlpha < 0) {
			currAlpha = 0;
		}
		if (currAlpha != lastAlpha.getOpacity()) {
			lastAlpha.setOpacity(currAlpha);
			changed = true;
		}
	}

	@Override
	public void update() {
		if (changed) {
			update(lastAlpha);
			changed = false;
		}
	}

	protected abstract void update(Opacity lastAlpha);
}
