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
import org.gwt.mosaic2g.client.binding.Getter;
import org.gwt.mosaic2g.client.binding.Property;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Segment;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.control.Box;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CopyOfMain implements EntryPoint {
	private Scene scene;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootLayoutPanel.get().add(scene = new Scene());
		scene.setShow(createShow());

		final AnimationEngine animationEngine = new AnimationEngine(
				new AnimationClient[] { scene });
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				animationEngine.start();
			}
		});
	}

	private Show createShow() {
		Show show = new Show();

		Box b1 = new Box(show, Property.valueOf(1), Property.valueOf(1),
				Property.valueOf(146), scene.getClientHeight().createBinding(
						new Getter<Integer>() {
							public Integer get(Integer value) {
								return value - 154;
							}
						}));
		b1.setBorder("1px solid blue");
		b1.setBackground("cyan");

		Box b2 = new Box(show, Property.valueOf(1), b1.getHeight()
				.createBinding(new Getter<Integer>() {
					public Integer get(Integer value) {
						return value + 4;
					}
				}), Property.valueOf(146), Property.valueOf(148));
		b2.setBorder("1px solid red");
		b2.setBackground("orange");

		Box b3 = new Box(show, Property.valueOf(150), Property.valueOf(1),
				scene.getClientWidth().createBinding(new Getter<Integer>() {
					public Integer get(Integer value) {
						return value - 152;
					}
				}), scene.getClientHeight().createBinding(
						new Getter<Integer>() {
							public Integer get(Integer value) {
								return value - 2;
							}
						}));
		b3.setBorder("1px solid green");
		b3.setBackground("yellow");

		Segment s = new Segment(show);
		s.add(b1);
		s.add(b2);
		s.add(b3);

		show.activateSegment(s);

		return show;
	}

}
