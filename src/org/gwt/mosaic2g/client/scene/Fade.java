package org.gwt.mosaic2g.client.scene;

/**
 * Modifies a child feature by applying an alpha value when drawing in it. This
 * lets you animate a fade-in and fade-out effect. It works be specifying alpha
 * values at a few key frames, and doing linear interpolation between those key
 * frames.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Fade extends Modifier {

	public final static int ALPHA_FIELD = 0;

	private final InterpolatedModel model;

	private int lastAlpha;

	public Fade(Show show) {
		this(show, InterpolatedModel.makeDefaultFadeModel());
	}

	public Fade(Show show, InterpolatedModel model) {
		super(show);
		this.model = model;
	}

	public InterpolatedModel getModel() {
		return model;
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			// Setup initial values
			lastAlpha = Short.MIN_VALUE;
			model.activate();
		}
		super.setActivateMode(mode);
	}

	@Override
	public boolean nextFrame(Scene scene) {
		model.nextFrame(scene);

		int currAlpha = model.getField(ALPHA_FIELD);
		if (currAlpha > 255) {
			currAlpha = 255;
		}
		if (currAlpha < 0) {
			currAlpha = 0;
		}
		if (currAlpha != lastAlpha) {
			lastAlpha = currAlpha;
			markAsChanged();
		}

		return (changed = super.nextFrame(scene));
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		int old = scene.getOpacity();
		scene.setOpacity(lastAlpha);
		getPart().paintFrame(scene);
		scene.setOpacity(old);
		
		changed = false;
	}
}
