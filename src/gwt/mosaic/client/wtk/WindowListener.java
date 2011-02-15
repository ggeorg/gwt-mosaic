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

/**
 * Window listener interface.
 */
public interface WindowListener {
	/**
	 * Window listener adapter.
	 */
	public static class Adapter implements WindowListener {
		@Override
		public void titleChanged(Window window, String previousTitle) {
		}

//		@Override
//		public void iconAdded(Window window, Image addedIcon) {
//		}
//
//		@Override
//		public void iconInserted(Window window, Image addedIcon, int index) {
//		}
//
//		@Override
//		public void iconsRemoved(Window window, int index,
//				Sequence<Image> removed) {
//		}

		@Override
		public void contentChanged(Window window, Component previousContent) {
		}

		@Override
		public void activeChanged(Window window, Window obverseWindow) {
		}

		@Override
		public void maximizedChanged(Window window) {
		}
	}

	/**
	 * Called when a window's title has changed.
	 * 
	 * @param window
	 * @param previousTitle
	 */
	public void titleChanged(Window window, String previousTitle);

	/**
	 * Called when a window's icon has changed.
	 * 
	 * @param window
	 * @param addedIcon
	 */
	// TODO public void iconAdded(Window window, Image addedIcon);

	/**
	 * Called when a window's icon has changed.
	 * 
	 * @param window
	 * @param addedIcon
	 */
	// TODO public void iconInserted(Window window, Image addedIcon, int index);

	/**
	 * Called when a window's icon has changed.
	 * 
	 * @param window
	 * @param index
	 * @param removed
	 */
	// TODO public void iconsRemoved(Window window, int index, Sequence<Image> removed);

	/**
	 * Called when a window's content component has changed.
	 * 
	 * @param window
	 * @param previousContent
	 */
	public void contentChanged(Window window, Component previousContent);

	/**
	 * Called when a window's active state has changed.
	 * 
	 * @param window
	 * @param obverseWindow
	 */
	public void activeChanged(Window window, Window obverseWindow);

	/**
	 * Called when a window's maximized state has changed.
	 * 
	 * @param window
	 */
	public void maximizedChanged(Window window);
}
