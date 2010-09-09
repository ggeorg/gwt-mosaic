package org.gwt.mosaic2g.client.scene;

/**
 * Abstract base class for features that modify a single child feature.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public abstract class Modifier extends Feature {

	private Feature part;
	
	protected Modifier(Show show) {
		super(show);
	}

	public Feature getPart() {
		return part;
	}

	public void setPart(Feature part) {
		this.part = part;
	}

	@Override
	public int getX() {
		return part.getX();
	}

	@Override
	public int getY() {
		return part.getY();
	}

	@Override
	public int getWidth() {
		return part.getWidth();
	}

	@Override
	public int getHeight() {
		return part.getHeight();
	}
	
	@Override
	public void resize(int width, int height) {
		part.resize(width, height);
	}

	@Override
	protected void setSetupMode(boolean mode) {
		if (mode) {
			part.setup();
		} else {
			part.unsetup();
		}
	}

	@Override
	public boolean needsMoreSetup() {
		if (part.needsMoreSetup()) {
			return true;
		}
		return false;
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			part.activate();
		} else {
			part.deactivate();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		return part.nextFrame(scene);
	}

	@Override
	public void paintFrame(Scene scene) {
		part.paintFrame(scene);
	}

	@Override
	public void markAsChanged() {
		super.markAsChanged();
		part.markAsChanged();
	}
}
