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
import org.gwt.mosaic2g.client.scene.Assembly;
import org.gwt.mosaic2g.client.scene.AutoSize;
import org.gwt.mosaic2g.client.scene.Container;
import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Fade;
import org.gwt.mosaic2g.client.scene.GrinFile;
import org.gwt.mosaic2g.client.scene.InterpolatedModel;
import org.gwt.mosaic2g.client.scene.InterpolatedModelParser;
import org.gwt.mosaic2g.client.scene.Scene;
import org.gwt.mosaic2g.client.scene.Segment;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.Timer;
import org.gwt.mosaic2g.client.scene.Translator;
import org.gwt.mosaic2g.client.scene.control.Box;
import org.gwt.mosaic2g.client.scene.control.Image;
import org.gwt.mosaic2g.client.scene.control.Text;
import org.gwt.mosaic2g.client.scene.layout.HBox;
import org.gwt.mosaic2g.client.scene.layout.Stack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {
	private Scene scene;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootLayoutPanel.get().add(scene = new Scene());
		scene.setShow(buildShow07(new Show()));
		AnimationEngine animationEngine = new AnimationEngine(
				new AnimationClient[] { scene });
		animationEngine.start();
	}

	/*
	 * Box + ScalingModel
	 */
	Show buildShow00(Show show) {
		Segment segment = new Segment(show);

		Control box = new Box(show, 100, 100, 140, 90);
		box.setBorder("2px solid #4d4d4d");

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 50, 100 },
				new int[][] { { 100, 100, 100 }, { 100, 100, 100 },
						{ 1000, 5000, 1000 }, { 1000, 5000, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		box.setScalingModel(sm);

		segment.add(box);

		show.activateSegment(segment);

		return show;
	}

	/*
	 * Dynamic Stack+Box+Text+Translator
	 */
	Show buildShow01(Show show) {
		Segment segment0 = new Segment(show);

		InterpolatedModel scalingModel = InterpolatedModel.makeScalingModel(
				new int[] { 0, 50, 100 }, new int[][] { { 10, 10, 10 },
						{ 10, 10, 10 }, { 1000, 5000, 1000 },
						{ 1000, 5000, 1000 } }, Integer.MAX_VALUE,
				Integer.MAX_VALUE, null);

		Box box = new Box(show, 10, 10, 140, 90);
		box.setBorder("2px solid #4d4d4d");
		box.setScalingModel(scalingModel);

		final Text label = new Text(show, 10, 10, 140, 90);
		label.setText(
				"Hello, World!<br>hgajshgjdahgjs hdgjahgsj hdgjahgsjdh gjahsgjd hgajshgdj hagjshgdj ahgsjhdgj ahgsjhdga jhsgjdhagjs hgdjhagjs hdgjahgsj dhajhg jagsjgjahsg jdhagjshgd jahgs jdhgajhsgd jhagjshgdj ahgjshdgj ahgsjhdgj ahgsjd hajhsgdjh agjshgdjah gsjdhga j",
				true);
		label.setBorder("2px solid #ff4d4d");
		label.setScalingModel(scalingModel, true);

		Stack stack = new Stack(show, 100, 100);
		stack.add(box);
		stack.add(label);

		InterpolatedModel model = InterpolatedModel.makeTranslatorModel(
				new int[] { 0, 100, 150, 180 }, new int[][] {
						{ 0, 200, 300, 0 }, { 0, 200, 100, 0 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		Translator translator = new Translator(show, model);
		translator.setPart(stack);

		Timer timer = new Timer(show, 10, new Command[] { new Command() {
			public void execute() {
				// label.setText("Hello, World!".substring(0,
				// 1 + (int) (Math.random() * 13)));
			}
		} });

		segment0.add(translator);
		segment0.add(timer);

		show.activateSegment(segment0);

		return show;
	}

	Show buildShow02(Show show) {
		Segment segment = new Segment(show);

		Box box1 = new Box(show, 100, 100, 140, 90);
		box1.setBorder("1px solid black");

		Container c = new Container(show, 10, 10, 512, 384);
		c.add(box1);

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 50, 100 }, new int[][] { { 100, 100, 100 },
				{ 100, 100, 100 }, { 1000, 500, 1000 }, { 1000, 500, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		c.setScalingModel(sm);

		Box box2 = new Box(show, 9, 9, 514, 386);
		box2.setBorder("1px solid red");
		box2.setScalingModel(sm, true);

		InterpolatedModel model = InterpolatedModel.makeTranslatorModel(
				new int[] { 0, 100, 150, 180 }, new int[][] {
						{ 0, 200, 300, 0 }, { 0, 200, 100, 0 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		Translator translator = new Translator(show, model);
		translator.setPart(c);

		segment.add(translator);
		segment.add(box2);

		show.activateSegment(segment);

		return show;
	}

	Show buildShow03(Show show) {
		Segment segment = new Segment(show);

		Box box1 = new Box(show, 10, 10, 50, 90);
		box1.setBorder("1px solid black");

		Box box2 = new Box(show, 10, 10, 50, 90);
		box2.setBorder("1px solid red");

		Box box3 = new Box(show, 10, 10, 50, 90);
		box3.setBorder("1px solid green");

		Box box4 = new Box(show, 10, 10, 50, 90);
		box4.setBorder("1px solid blue");

		HBox hbox = new HBox(show, 100, 100, 500, 200);
		hbox.add(box1);
		hbox.add(box2);
		hbox.add(box3);
		hbox.add(box4);

		Box box = new Box(show, 100, 100, 500, 200);
		box.setBorder("1px solid red");

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 50, 100 }, new int[][] { { 25, 25, 25 }, { 45, 45, 45 },
				{ 1000, 2000, 1000 }, { 1000, 2000, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		hbox.setScalingModel(sm);
		// box.setScalingModel(sm);

		segment.add(hbox);
		segment.add(box);

		show.activateSegment(segment);

		return show;
	}

	Show buildShow04(Show show) {
		Segment segment = new Segment(show);

		Box box1 = new Box(show, 0, 0, 50, 90);
		box1.setBorder("1px solid black");

		Box box2 = new Box(show, 0, 0, 50, 90);
		box2.setBorder("1px solid red");

		Box box3 = new Box(show, 0, 0, 50, 90);
		box3.setBorder("1px solid green");

		Box box4 = new Box(show, 0, 0, 50, 90);
		box4.setBorder("1px solid blue");

		HBox hbox1 = new HBox(show, 100, 100, 500, 100);
		hbox1.add(box1);
		hbox1.add(box2);
		hbox1.add(box3);
		hbox1.add(box4);

		Box box5 = new Box(show, 0, 0, 100, 100);
		box5.setBorder("1px solid #456789");

		HBox hbox2 = new HBox(show, 100, 100, 500, 200);
		hbox2.add(box5);
		hbox2.add(hbox1);

		segment.add(hbox2);

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 50, 100 }, new int[][] { { 25, 25, 25 }, { 45, 45, 45 },
				{ 1000, 2000, 1000 }, { 1000, 2000, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		hbox2.setScalingModel(sm);

		show.activateSegment(segment);

		return show;
	}

	Show buildShow05(Show show) {
		Segment segment = new Segment(show);

		Box box1 = new Box(show, 10, 10, 50, 90);
		box1.setBorder("8px solid black");

		Box box2 = new Box(show, 10, 10, 50, 90);
		box2.setBorder("6px solid red");

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 25, 50 }, new int[][] { { 30, 30, 30 }, { 50, 50, 50 },
				{ 1000, 2000, 1000 }, { 1000, 2000, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		box2.setScalingModel(sm);

		Box box3 = new Box(show, 10, 10, 50, 90);
		box3.setBorder("4px solid green");

		Box box4 = new Box(show, 10, 10, 50, 90);
		box4.setBorder("2px solid blue");

		HBox hbox = new HBox(show, 100, 100, 500, 500);
		hbox.setSpacing(16);

		hbox.add(box1);
		hbox.add(box2);
		hbox.add(box3);
		hbox.add(box4);

		segment.add(hbox);

		show.activateSegment(segment);

		return show;
	}

	Show buildShow06(Show show) {
		Segment segment = new Segment(show);

		Box box1 = new Box(show, 2, 2, 128, 296);
		box1.setBorder("2px solid black");
		AutoSize autoSizing1 = new AutoSize(show, 512, 384,
				AutoSize.MAX_X_MARGIN | AutoSize.HEIGHT_SIZABLE);
		autoSizing1.setPart(box1);

		Box box2 = new Box(show, 2, 304, 128, 78);
		box2.setBorder("2px solid red");
		AutoSize autoSizing2 = new AutoSize(show, 512, 384,
				AutoSize.MAX_X_MARGIN | AutoSize.MIN_Y_MARGIN);
		autoSizing2.setPart(box2);

		Box box3 = new Box(show, 136, 2, 374, 380);
		box3.setBorder("2px solid red");
		AutoSize autoSizing3 = new AutoSize(show, 512, 384,
				AutoSize.WIDTH_SIZABLE | AutoSize.HEIGHT_SIZABLE);
		autoSizing3.setPart(box3);

		InterpolatedModel model = InterpolatedModel.makeTranslatorModel(
				new int[] { 0, 50, 100, 150 }, new int[][] {
						{ 0, 200, 300, 0 }, { 0, 200, 100, 0 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		Translator translator = new Translator(show, model);
		translator.setPart(autoSizing1);

		segment.add(translator);
		segment.add(autoSizing2);
		segment.add(autoSizing3);

		show.activateSegment(segment);

		return show;
	}

	@GrinFile(value = "translator_model01.txt")
	interface TranslatorModel01Parser extends InterpolatedModelParser {
	};

	Show buildShow07(Show show) {
		Segment segment = new Segment(show);

		final Box box1 = new Box(show, 2, 2, 128, 296);
		box1.setBorder("2px solid black");
		final AutoSize autoSizing1 = new AutoSize(show, 512, 384,
				AutoSize.MAX_X_MARGIN | AutoSize.HEIGHT_SIZABLE);
		autoSizing1.setPart(box1);

		final Box box2 = new Box(show, 2, 304, 128, 78);
		box2.setBorder("2px solid red");
		final AutoSize autoSizing2 = new AutoSize(show, 512, 384,
				AutoSize.MAX_X_MARGIN | AutoSize.MIN_Y_MARGIN);
		autoSizing2.setPart(box2);

		InterpolatedModel fadeModel = InterpolatedModel.makeFadeModel(
				new int[] { 0, 50, 100 }, new int[] { 0, 128, 255 }, 0,
				Integer.MAX_VALUE, null);

		final Assembly assembly = new Assembly(show);
		assembly.add(autoSizing1);
		assembly.add(autoSizing2);

		final Fade fade = new Fade(show, fadeModel);
		fade.setPart(assembly);

		TranslatorModel01Parser _model = GWT
				.create(TranslatorModel01Parser.class);
		InterpolatedModel model = InterpolatedModel.makeTranslatorModel(
				new int[] { 0, 50, 100, 150 }, new int[][] {
						{ 0, 200, 300, 0 }, { 0, 200, 100, 0 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);
		Translator translator = new Translator(show, model);
		translator.setPart(assembly);

		Timer timer = new Timer(show, 10, new Command[] { new Command() {
			public void execute() {
				if (assembly.getCurrentPart() == autoSizing1) {
					assembly.setCurrentPart(autoSizing2);
				} else {
					assembly.setCurrentPart(autoSizing1);
				}
			}
		} });

		// Clipped clipped = new Clipped(show, new Rectangle(10,10, 300,100));
		// clipped.setPart(fade);

		segment.add(translator);
		segment.add(timer);

		show.activateSegment(segment);

		return show;
	}

	Show buildShow08(Show show) {
		Segment segment = new Segment(show);

		InterpolatedModel sm = InterpolatedModel.makeScalingModel(new int[] {
				0, 50, 100 }, new int[][] { { 25, 25, 25 }, { 45, 45, 45 },
				{ 1000, 2000, 1000 }, { 1000, 2000, 1000 } },
				Integer.MAX_VALUE, Integer.MAX_VALUE, null);

		Image img = new Image(show, 10, 10, 300, 200);
		img.setUrl("http://www.google.gr/intl/en_com/images/srpr/logo1w.png");
		img.setScalingModel(sm);

		segment.add(img);

		show.activateSegment(segment);

		return show;
	}
}
