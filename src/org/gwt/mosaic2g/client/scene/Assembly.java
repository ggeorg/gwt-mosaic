package org.gwt.mosaic2g.client.scene;

/**
 * An {@code Assembly} is a feature composed of other features. It is a bit like
 * a switch statement: only one child of an assembly can be active at a time.
 * This can be used to compose e.g. a menu that can be in one of several visual
 * states. Often the parts of an assembly are groups.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Assembly extends Group implements HasFeatures {

	private Feature currentPart = null;

	public Assembly(Show show) {
		super(show);
	}

	public Feature getCurrentPart() {
		return currentPart;
	}

	public void setCurrentPart(Feature part) {
		if (currentPart == part) {
			return;
		}
		if (isActivated()&& getParts().contains(part)) {
			part.activate();
			currentPart.deactivate();
		}
		currentPart = part;
	}

	@Override
	public int getX() {
		return currentPart.getX();
	}

	@Override
	public int getY() {
		return currentPart.getY();
	}

	@Override
	public int getWidth() {
		return currentPart.getWidth();
	}

	@Override
	public int getHeight() {
		return currentPart.getHeight();
	}
	
	@Override
	public void resize(int width, int height) {
		currentPart.resize(width, height);
	}

	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			currentPart = parts.get(0);
		} else {
			currentPart = null;
		}
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			currentPart.activate();
		} else {
			currentPart.deactivate();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		return (changed = currentPart.nextFrame(scene));
	}

	@Override
	public void paintFrame(Scene scne) {
		if (!changed) {
			return;
		}
		currentPart.paintFrame(scne);
	}

}
