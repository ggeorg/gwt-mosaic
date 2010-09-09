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

	int getPrefWidth();

	int getPrefHeight();

	//int getMaxWidth();

	//int getMaxHeight();

	// functions to express its natural grow, shrink, and fill preferences
	
	//boolean getHGrow();
	//boolean getVGrow();
	//boolean getHFill();
	//boolean getVFill();
}
