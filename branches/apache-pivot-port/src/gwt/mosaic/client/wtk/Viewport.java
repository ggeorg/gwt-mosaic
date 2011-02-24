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
import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.util.ListenerList;
import gwt.mosaic.shared.beans.DefaultProperty;

/**
 * Abstract base class for viewport components. Viewports provide a windowed
 * view on a component (called the "view") that is too large to fit within a
 * given area. They are generally scrollable.
 */
@DefaultProperty("view")
@SuppressWarnings("serial")
public abstract class Viewport extends Container {
	/**
	 * Viewport skin interface. Viewport skins must implement this.
	 */
	public interface Skin {
		public Bounds getViewportBounds();
	}

	private static class ViewportListenerList extends
			ListenerList<ViewportListener> implements ViewportListener {

		@Override
		public void scrollTopChanged(Viewport viewport, int previousScrollTop) {
			for (ViewportListener listener : this) {
				listener.scrollTopChanged(viewport, previousScrollTop);
			}
		}

		@Override
		public void scrollLeftChanged(Viewport viewport, int previousScrollLeft) {
			for (ViewportListener listener : this) {
				listener.scrollLeftChanged(viewport, previousScrollLeft);
			}
		}

		@Override
		public void viewChanged(Viewport viewport, Component previousView) {
			for (ViewportListener listener : this) {
				listener.viewChanged(viewport, previousView);
			}
		}
	}

	private int scrollTop = 0;
	private int scrollLeft = 0;
	private Component view;

	private boolean consumeRepaint = false;

	private ViewportListenerList viewportListeners = new ViewportListenerList();

	@Override
	protected void setSkin(
			BeanAdapter<? extends gwt.mosaic.client.wtk.Skin> styles) {
		if (!(styles.getBean() instanceof Viewport.Skin)) {
			throw new IllegalArgumentException("Skin class must implement "
					+ Viewport.Skin.class.getName());
		}

		super.setSkin(styles);
	}

	public int getScrollTop() {
		return scrollTop;
	}

	public void setScrollTop(int scrollTop) {
		int previousScrollTop = this.scrollTop;

		if (scrollTop != previousScrollTop) {
			this.scrollTop = scrollTop;
			viewportListeners.scrollTopChanged(this, previousScrollTop);
		}
	}

	public int getScrollLeft() {
		return scrollLeft;
	}

	public void setScrollLeft(int scrollLeft) {
		int previousScrollLeft = this.scrollLeft;

		if (scrollLeft != previousScrollLeft) {
			this.scrollLeft = scrollLeft;
			viewportListeners.scrollLeftChanged(this, previousScrollLeft);
		}
	}

	public Component getView() {
		return view;
	}

	public void setView(Component view) {
		Component previousView = this.view;

		if (view != previousView) {
			// Remove any previous view component
			this.view = null;

			if (previousView != null) {
				remove(previousView);
			}

			// Set the new view component
			if (view != null) {
				insert(view, 0);
			}

			this.view = view;

			viewportListeners.viewChanged(this, previousView);
		}
	}

	/**
	 * Returns the <tt>consumeRepaint</tt> flag, which controls whether the
	 * viewport will propagate repaints to its parent or consume them. This flag
	 * enables skins to optimize viewport scrolling by blitting the display to
	 * reduce the required repaint area.
	 * 
	 * @return <tt>true</tt> if this viewport will consume repaints that bubble
	 *         up through it; <tt>false</tt> if it will propagate them up like
	 *         normal.
	 */
	public boolean isConsumeRepaint() {
		return consumeRepaint;
	}

	/**
	 * Sets the <tt>consumeRepaint</tt> flag, which controls whether the
	 * viewport will propagate repaints to its parent or consume them. This flag
	 * enables skins to optimize viewport scrolling by blitting the display to
	 * reduce the required repaint area.
	 * 
	 * @param consumeRepaint
	 *            <tt>true</tt> to consume repaints that bubble up through this
	 *            viewport; <tt>false</tt> to propagate them up like normal.
	 */
	public void setConsumeRepaint(boolean consumeRepaint) {
		this.consumeRepaint = consumeRepaint;
	}

	public Bounds getViewportBounds() {
		Viewport.Skin viewportSkin = (Viewport.Skin) getSkin();
		return viewportSkin.getViewportBounds();
	}

	@Override
	public void repaint(boolean immediate) {
		if (!consumeRepaint) {
			super.repaint(immediate);
		}
	}

	@Override
	public Sequence<Component> remove(int index, int count) {
		for (int i = index, n = index + count; i < n; i++) {
			Component component = get(i);
			if (component == view) {
				throw new UnsupportedOperationException();
			}
		}

		// Call the base method to remove the components
		return super.remove(index, count);
	}

	public ListenerList<ViewportListener> getViewportListeners() {
		return viewportListeners;
	}
}
