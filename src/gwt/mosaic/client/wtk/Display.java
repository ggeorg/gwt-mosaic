/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gwt.mosaic.client.wtk;

import gwt.mosaic.client.beans.BeanAdapter;
import gwt.mosaic.client.collections.HashSet;
import gwt.mosaic.client.wtk.skin.DisplaySkin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 * Container that serves as the root of a component hierarchy.
 */
@SuppressWarnings("serial")
public final class Display extends Container {
	interface SkinBeanAdapter extends BeanAdapter<DisplaySkin> {
	}

	private transient ApplicationContext.DisplayHost displayHost;

	protected Display() {
		// No-op
	}

	public Display(ApplicationContext.DisplayHost displayHost) {
		this.displayHost = displayHost;

		SkinBeanAdapter adapter = GWT.create(SkinBeanAdapter.class);
		adapter.setBean(new DisplaySkin());
		super.setSkin(adapter);
	}

	public ApplicationContext.DisplayHost getDisplayHost() {
		return displayHost;
	}

	@Override
	protected void setSkin(BeanAdapter<? extends Skin> styles) {
		throw new UnsupportedOperationException("Can't replace Display skin.");
	}

	@Override
	protected void setParent(Container parent) {
		throw new UnsupportedOperationException("Display can't have a parent.");
	}

	@Override
	public void setLocation(int x, int y) {
		throw new UnsupportedOperationException(
				"Can't change the location of the display.");
	}

	@Override
	public void setVisible(boolean visible) {
		throw new UnsupportedOperationException(
				"Can't change the visibility of the display.");
	}

	// --------------------

	private static final int DEFAULT_DELAY_MILLIS = 33;

	private int invalidateTimerDelayMillis = DEFAULT_DELAY_MILLIS;
	private int repaintTimerDelayMillis = DEFAULT_DELAY_MILLIS;

	@Override
	public void invalidate() {
		super.invalidate();

		validateTimer.schedule(invalidateTimerDelayMillis--);

		if (invalidateTimerDelayMillis <= 0) {
			invalidateTimerDelayMillis = DEFAULT_DELAY_MILLIS;
		}
	}

	@Override
	public void repaint(Component component, boolean immediate) {
		componentsToPaint.add(component);
		//immediate = true;
		if (immediate) {
			repaintTimer.cancel();
			repaintTimer.run();
		} else {
			repaintTimer.schedule(repaintTimerDelayMillis--);
		}

		if (repaintTimerDelayMillis <= 0) {
			repaintTimerDelayMillis = DEFAULT_DELAY_MILLIS;
		}
	}

	private final transient Timer validateTimer = new Timer() {
		@Override
		public void run() {
			Display.this.setup(true);
			Display.this.validate();
		}
	};

	private transient HashSet<Component> componentsToPaint = new HashSet<Component>();
	private final transient Timer repaintTimer = new Timer() {
		@Override
		public void run() {
			if (componentsToPaint.isEmpty()) {
				return;
			}
			HashSet<Component> oldComponentsToPaint = componentsToPaint;
			componentsToPaint = new HashSet<Component>();
			for (Component component : oldComponentsToPaint) {
				component.paint();
			}
			oldComponentsToPaint.clear();
		}
	};

	// -------------

	@Override
	public void insert(Component component, int index) {
		if (!(component instanceof Window)) {
			throw new IllegalArgumentException("component must be an instance "
					+ "of " + Window.class);
		}

		super.insert(component, index);
	}

	@Override
	protected void descendantAdded(Component descendant) {
		super.descendantAdded(descendant);

		String automationID = descendant.getAutomationID();

		if (automationID != null) {
			Automation.add(automationID, descendant);
		}
	}

	@Override
	protected void descendantRemoved(Component descendant) {
		super.descendantRemoved(descendant);

		String automationID = descendant.getAutomationID();

		if (automationID != null) {
			Automation.remove(automationID);
		}
	}

	@Override
	public FocusTraversalPolicy getFocusTraversalPolicy() {
		return null;
	}

}
