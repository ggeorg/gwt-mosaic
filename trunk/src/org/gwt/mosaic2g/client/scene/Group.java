package org.gwt.mosaic2g.client.scene;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Represents a group of features that are all activated at the same time. It's
 * useful to group features together so that e.g. they can be turned on and off
 * as a unit within an assembly.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Group extends Feature implements HasFeatures {

	protected FeatureCollection parts = new FeatureCollection(this);

	private int numSetupChecked;
	
	public Group(Show show) {
		super(show);
	}

	@Override
	public int getX() {
		int x = Integer.MAX_VALUE;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getX();
			if (val < x) {
				x = val;
			}
		}
		return x;
	}

	@Override
	public int getY() {
		int y = Integer.MAX_VALUE;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getY();
			if (val < y) {
				y = val;
			}
		}
		return y;
	}
	
	@Override
	public int getWidth() {
		int width = Integer.MIN_VALUE;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getWidth();
			if (val > width) {
				width = val;
			}
		}
		return width;
	}
	
	@Override
	public int getHeight() {
		int height = Integer.MIN_VALUE;
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			int val = it.next().getHeight();
			if (val > height) {
				height = val;
			}
		}
		return height;
	}
	
	@Override
	protected void setSetupMode(boolean mode) {
		if (mode) {
			numSetupChecked = 0;
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				it.next().setup();
			}
		} else {
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				it.next().unsetup();
			}
		}
	}
	
	@Override
	public boolean needsMoreSetup() {
		while (numSetupChecked < parts.size()) {
			if (parts.get(numSetupChecked).needsMoreSetup()) {
				return true;
			}
			/*
			 * Once a part doesn't need more setup, it will never go back to
			 * needing setup until we call unsetup() then setup(). The variable
			 * numSetupChecked is re-set to 0 just before calling setup() on our
			 * part, so this is safe. Note that the contract of Feature requires
			 * that setup() be called before needsMoreSetup() is consulted.
			 * 
			 * This optimization helps speed the calculation of needsMoreSetup()
			 * in the case where a group or an assembly is the child of multiple
			 * parts of an assembly. With this optimization, a potential O(n^2)
			 * is turned into O(n) (albeit typically with a small n).
			 */
			numSetupChecked++;
		}
		return false;
	}

	@Override
	protected void setActivateMode(boolean mode) {
		if (mode) {
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				it.next().activate();
			}
		} else {
			Iterator<Feature> it = iterator();
			while (it.hasNext()) {
				it.next().deactivate();
			}
		}
	}

	@Override
	public void markAsChanged() {
		super.markAsChanged();
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next().markAsChanged();
		}
	}

	@Override
	public boolean nextFrame(Scene scene) {
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			if(it.next().nextFrame(scene)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void paintFrame(Scene scene) {
		if (!isActivated() || !changed) {
			return;
		}
		
		// TODO consult children changes events
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next().paintFrame(scene);
		}
	}

	public void add(Feature f) {
		parts.add(f);
	}

	public void clear() {
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	public Iterator<Feature> iterator() {
		return getParts().iterator();
	}

	protected FeatureCollection getParts() {
		return parts;
	}

	public boolean remove(Feature f) {
		try {
			parts.remove(f);
		} catch (NoSuchElementException ex) {
			return false;
		}
		return true;
	}
}
