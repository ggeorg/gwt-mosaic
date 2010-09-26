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
package org.gwt.mosaic2g.client.scene.layout;

/**
 * Encapsulates the state required for a node to be dynamically resized by its
 * parent during the layout pass of the scene graph.
 * 
 * @author ggeorg
 */
public interface Resizable {

	// functions to express its acceptable size range to its parent.

	//int getMinWidth();

	//int getMinHeight();

	//int getMaxWidth();

	//int getMaxHeight();

	// functions to express its natural grow, shrink, and fill preferences
	
	//boolean getHGrow();
	//boolean getVGrow();
	//boolean getHFill();
	//boolean getVFill();
}
