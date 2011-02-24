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

import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.util.ListenerList;
import gwt.mosaic.shared.beans.Bean;

/**
 * Container that provides a scrollable view of a component, with optional fixed
 * row and column headers.
 */
@Bean
@SuppressWarnings("serial")
public class ScrollPane extends Viewport {
	/**
	 * Enumeration defining scroll pane scroll bar policies.
	 */
	public enum ScrollBarPolicy {
		AUTO, NEVER, ALWAYS, FILL, FILL_TO_CAPACITY
	}

	private static class ScrollPaneListenerList extends
			ListenerList<ScrollPaneListener> implements ScrollPaneListener {

		@Override
		public void horizontalScrollBarPolicyChanged(ScrollPane scrollPane,
				ScrollBarPolicy previousHorizontalScrollBarPolicy) {
			for (ScrollPaneListener listener : this) {
				listener.horizontalScrollBarPolicyChanged(scrollPane,
						previousHorizontalScrollBarPolicy);
			}
		}

		@Override
		public void verticalScrollBarPolicyChanged(ScrollPane scrollPane,
				ScrollBarPolicy previousVerticalScrollBarPolicy) {
			for (ScrollPaneListener listener : this) {
				listener.verticalScrollBarPolicyChanged(scrollPane,
						previousVerticalScrollBarPolicy);
			}
		}

		@Override
		public void rowHeaderChanged(ScrollPane scrollPane,
				Component previousRowHeader) {
			for (ScrollPaneListener listener : this) {
				listener.rowHeaderChanged(scrollPane, previousRowHeader);
			}
		}

		@Override
		public void columnHeaderChanged(ScrollPane scrollPane,
				Component previousColumnHeader) {
			for (ScrollPaneListener listener : this) {
				listener.columnHeaderChanged(scrollPane, previousColumnHeader);
			}
		}

		@Override
		public void cornerChanged(ScrollPane scrollPane,
				Component previousCorner) {
			for (ScrollPaneListener listener : this) {
				listener.cornerChanged(scrollPane, previousCorner);
			}
		}
	}

	/**
	 * Component class representing the components that will get placed in the
	 * corners of a <tt>ScrollPane</tt>. Skins will instantiate these components
	 * as needed when unfilled corners are introduced by a row header or column
	 * header.
	 */
	public static class Corner extends Component {
		/**
		 * Enumeration defining placement values for scroll pane corners.
		 */
		public enum Placement {
			TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_RIGHT;
		}

		private Placement placement;

		public Corner(Placement placement) {
			if (placement == null) {
				throw new IllegalArgumentException("Placement is null.");
			}

			this.placement = placement;

			installSkin(Corner.class);
		}

		public Placement getPlacement() {
			return placement;
		}
	}

	private ScrollBarPolicy horizontalScrollBarPolicy;
	private ScrollBarPolicy verticalScrollBarPolicy;
	private Component rowHeader;
	private Component columnHeader;
	private Component corner;
	private ScrollPaneListenerList scrollPaneListeners = new ScrollPaneListenerList();

	public ScrollPane() {
		this(ScrollBarPolicy.AUTO, ScrollBarPolicy.AUTO);
	}

	public ScrollPane(ScrollBarPolicy horizontalScrollBarPolicy,
			ScrollBarPolicy verticalScrollBarPolicy) {
		super();

		if (horizontalScrollBarPolicy == null) {
			throw new IllegalArgumentException(
					"horizontalScrollBarPolicy is null");
		}

		if (verticalScrollBarPolicy == null) {
			throw new IllegalArgumentException(
					"verticalScrollBarPolicy is null");
		}

		this.horizontalScrollBarPolicy = horizontalScrollBarPolicy;
		this.verticalScrollBarPolicy = verticalScrollBarPolicy;

		installSkin(ScrollPane.class);
	}

	public ScrollBarPolicy getHorizontalScrollBarPolicy() {
		return horizontalScrollBarPolicy;
	}

	public void setHorizontalScrollBarPolicy(
			ScrollBarPolicy horizontalScrollBarPolicy) {
		if (horizontalScrollBarPolicy == null) {
			throw new IllegalArgumentException(
					"horizontalScrollBarPolicy is null");
		}

		ScrollBarPolicy previousHorizontalScrollBarPolicy = this.horizontalScrollBarPolicy;

		if (horizontalScrollBarPolicy != previousHorizontalScrollBarPolicy) {
			this.horizontalScrollBarPolicy = horizontalScrollBarPolicy;
			scrollPaneListeners.horizontalScrollBarPolicyChanged(this,
					previousHorizontalScrollBarPolicy);
		}
	}

	public ScrollBarPolicy getVerticalScrollBarPolicy() {
		return verticalScrollBarPolicy;
	}

	public void setVerticalScrollBarPolicy(
			ScrollBarPolicy verticalScrollBarPolicy) {
		if (verticalScrollBarPolicy == null) {
			throw new IllegalArgumentException(
					"verticalScrollBarPolicy is null");
		}

		ScrollBarPolicy previousVerticalScrollBarPolicy = this.verticalScrollBarPolicy;

		if (verticalScrollBarPolicy != previousVerticalScrollBarPolicy) {
			this.verticalScrollBarPolicy = verticalScrollBarPolicy;
			scrollPaneListeners.verticalScrollBarPolicyChanged(this,
					previousVerticalScrollBarPolicy);
		}
	}

	public Component getRowHeader() {
		return rowHeader;
	}

	public void setRowHeader(Component rowHeader) {
		Component previousRowHeader = this.rowHeader;

		if (rowHeader != previousRowHeader) {
			// Remove any previous rowHeader component
			if (previousRowHeader != null) {
				remove(previousRowHeader);
			}

			this.rowHeader = null;

			if (rowHeader != null) {
				if (rowHeader.getParent() != null) {
					throw new IllegalArgumentException(
							"Component already has a parent.");
				}

				int insertionIndex = 0;

				if (getView() != null) {
					insertionIndex++;
				}

				// Add the component
				insert(rowHeader, insertionIndex);
			}

			this.rowHeader = rowHeader;

			scrollPaneListeners.rowHeaderChanged(this, previousRowHeader);
		}
	}

	public Component getColumnHeader() {
		return columnHeader;
	}

	public void setColumnHeader(Component columnHeader) {
		Component previousColumnHeader = this.columnHeader;

		if (columnHeader != previousColumnHeader) {
			// Remove any previous columnHeader component
			if (previousColumnHeader != null) {
				remove(previousColumnHeader);
			}

			this.columnHeader = null;

			if (columnHeader != null) {
				int insertionIndex = 0;

				if (getView() != null) {
					insertionIndex++;
				}

				// Add the component
				insert(columnHeader, insertionIndex);
			}

			this.columnHeader = columnHeader;

			scrollPaneListeners.columnHeaderChanged(this, previousColumnHeader);
		}
	}

	public Component getCorner() {
		return corner;
	}

	public void setCorner(Component corner) {
		Component previousCorner = this.corner;

		if (corner != this.corner) {
			// Remove any previous corner component
			if (previousCorner != null) {
				remove(previousCorner);
			}

			this.corner = null;

			if (corner != null) {
				int insertionIndex = 0;

				if (getView() != null) {
					insertionIndex++;
				}

				if (rowHeader != null) {
					insertionIndex++;
				}

				if (columnHeader != null) {
					insertionIndex++;
				}

				// Add the component
				insert(corner, insertionIndex);
			}

			this.corner = corner;

			scrollPaneListeners.cornerChanged(this, previousCorner);
		}
	}

	@Override
	public Sequence<Component> remove(int index, int count) {
		for (int i = index, n = index + count; i < n; i++) {
			Component component = get(i);
			if (component == rowHeader || component == columnHeader
					|| component == corner) {
				throw new UnsupportedOperationException();
			}
		}

		// Call the base method to remove the components
		return super.remove(index, count);
	}

	public ListenerList<ScrollPaneListener> getScrollPaneListeners() {
		return scrollPaneListeners;
	}
}
