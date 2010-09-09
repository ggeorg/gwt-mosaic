package org.gwt.mosaic2g.client.scene;

import java.util.Iterator;

public interface HasFeatures {

	void add(Feature f);
	
	void clear();
	
	Iterator<Feature> iterator();
	
	boolean remove(Feature f);
}
