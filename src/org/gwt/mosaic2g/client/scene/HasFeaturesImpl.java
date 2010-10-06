/*
 * Copyright 2010 ArkaSoft LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic2g.client.scene;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An abstract implementation of {@link HasFeatures}.
 * 
 * @author ggeorg
 */
public abstract class HasFeaturesImpl extends Feature implements HasFeatures {

	protected FeatureCollection parts = new FeatureCollection(this);

	private int numSetupChecked;

	public HasFeaturesImpl(Show show) {
		super(show);
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

	public void add(Feature f) {
		parts.add(f);
		changed = true;
	}

	public void clear() {
		Iterator<Feature> it = iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
		changed = true;
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
			changed = true;
		} catch (NoSuchElementException ex) {
			return false;
		}
		return true;
	}

}
