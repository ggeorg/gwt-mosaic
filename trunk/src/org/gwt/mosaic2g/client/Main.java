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
package org.gwt.mosaic2g.client;

import org.gwt.mosaic2g.client.animator.AnimationClient;
import org.gwt.mosaic2g.client.animator.AnimationEngine;
import org.gwt.mosaic2g.client.scene.Group;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Segment;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.control.Box;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {
	private final Scene scene = new Scene();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Specify a GWT panel (here RootLayoutPanel) to render our scene graph.
		// Within the panel we add the scene. A scene is a top level area where
		// you can set the root node of a scene graph, which is always a show
		// node.
		RootLayoutPanel.get().add(scene);
		scene.setShow(createShow());

		// Create an AnimationEngine instance with all AnimationClient(s) it has
		// to manage (Scene implements the AnimationClient interface).
		final AnimationEngine animationEngine = new AnimationEngine(
				new AnimationClient[] { scene });
		// Start the animation engine.
		animationEngine.start();
	}

	private Show createShow() {
		Show show = new Show();

		Box box1 = new Box(show, 50, 50, 100, 100);
		box1.setBackground("orange");

		Box box2 = new Box(show, 150, 150, 50, 50);
		box2.setBackground("green");
		
		Box box3 = new Box(show, 250, 100, 50, 150);
		box3.setBackground("magenta");

		Group boxGroup = new Group(show);
		boxGroup.add(box1);
		boxGroup.add(box2);
		boxGroup.add(box3);

		Box bgBox = new Box(show, boxGroup.getX().createBinding(), boxGroup
				.getY().createBinding(), boxGroup.getWidth().createBinding(),
				boxGroup.getHeight().createBinding());
		bgBox.setBorder("1px solid yellow");
		bgBox.setBackground("yellow");

		Segment s = new Segment(show);
		s.add(bgBox);
		s.add(boxGroup);

		show.activateSegment(s);

		return show;
	}

}
