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
import gwt.mosaic.client.wtk.ApplicationContext.DisplayHost;
import gwt.mosaic.client.wtk.skin.DisplaySkin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 * Container that serves as the root of a component hierarchy.
 */
public final class Display extends Container {
	interface SkinBeanAdapter extends BeanAdapter<DisplaySkin>{}
	
	private ApplicationContext.DisplayHost displayHost;

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

	@Override
	public void invalidate() {
		super.invalidate();

		validateTimer.schedule(33);
	}

	@Override
	public void repaint(boolean immediate) {
		super.repaint(immediate);

		if (immediate) {
			repaintTimer.schedule(0);
		} else {
			repaintTimer.schedule(33);
		}
	}

	private final Timer validateTimer = new Timer() {
		@Override
		public void run() {
			Display.this.validate();
		}
	};

	private final Timer repaintTimer = new Timer() {
		@Override
		public void run() {
			DisplayHost displayHost = Display.this.getDisplayHost();
			Display.this.paint(displayHost);
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
