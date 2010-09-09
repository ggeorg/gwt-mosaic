package org.gwt.mosaic2g.client.scene;

/**
 * A {@code Translator} wraps other features, and adds movement taken from a
 * translator model (represented at runtime by an {@link InterpolatedModel} to
 * it.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Translator extends Modifier {
	public static final int OFFSCREEN = Integer.MAX_VALUE;

	public final static int X_FIELD = 0;
	public final static int Y_FIELD = 1;

	private final InterpolatedModel model;

	private int lastDx;
	private int lastDy;

	private final int refWidth;
	private final int refHeight;

	public Translator(Show show) {
		this(show, InterpolatedModel.makeDefaultTranslatorModel());
	}

	public Translator(Show show, InterpolatedModel model) {
		this(show, model, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	public Translator(Show show, InterpolatedModel model, int refWidth,
			int refHeight) {
		super(show);
		this.model = model;
		this.refWidth = refWidth;
		this.refHeight = refHeight;
	}

	public InterpolatedModel getModel() {
		return model;
	}

	@Override
	public int getX() {
		int x = model.getField(X_FIELD);
		x += getPart().getX();
		return x;
	}

	@Override
	public int getY() {
		int y = model.getField(Y_FIELD);
		y += getPart().getY();
		return y;
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			model.activate();
			lastDx = OFFSCREEN;
			lastDy = OFFSCREEN;
			markAsChanged();
		}
		super.setActivateMode(mode);
	}

	@Override
	public boolean nextFrame(Scene scene) {
		model.nextFrame(scene);
		int dx = model.getField(X_FIELD);
		if (refWidth > 0) {
			dx = (int) (dx * (double) scene.getElement().getClientWidth() / refWidth);
		}
		int dy = model.getField(Y_FIELD);
		if (refHeight > 0) {
			dy = (int) (dy * (double) scene.getElement().getClientHeight() / refHeight);
		}
		if (dx != OFFSCREEN && dy != OFFSCREEN && dx != lastDx || dy != lastDy) {
			markAsChanged();
			lastDx = dx;
			lastDy = dy;
		}
		return (changed = super.nextFrame(scene));
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}

		scene.translate(lastDx, lastDy);
		getPart().paintFrame(scene);
		scene.translate(-lastDx, -lastDy);

		changed = false;
	}
}
