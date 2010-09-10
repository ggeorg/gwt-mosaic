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
package org.gwt.mosaic2g.client.scene;

import org.gwt.mosaic2g.client.MyClientBundle;
import org.gwt.mosaic2g.client.animator.AnimationClient;
import org.gwt.mosaic2g.client.style.ComputedStyle;
import org.gwt.mosaic2g.client.util.Rectangle;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@code Scene} is the root for all content in a scene graph.
 * 
 * @author ggeorg
 */
public class Scene extends Composite implements AnimationClient,
		RequiresResize, ProvidesResize {

	private final AbsolutePanel target;

	private Show show;

	private int originX = 0;
	private int originY = 0;

	private int opacity = -1;

	private Rectangle clipRegion;

	private boolean sizeChanged = false;

	public Scene() {
		this(new AbsolutePanel());
	}

	protected Scene(AbsolutePanel p) {
		initWidget(target = p);

		// Setting the panel's position style to 'relative' causes it to be
		// treated
		// as a new positioning context for its children.
		DOM.setStyleAttribute(getElement(), "position", "relative");
		DOM.setStyleAttribute(getElement(), "overflow", "hidden");

		MyClientBundle.INSTANCE.css().ensureInjected();
		setStyleName(MyClientBundle.INSTANCE.css().scene());
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		// TODO do show initialization/destroy
		this.show = show;
	}

	public boolean isSizeChanged() {
		return sizeChanged;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public void translate(int x, int y) {
		originX += x;
		originY += y;
	}

	public void getClipBounds(Rectangle r) {
		if (clipRegion != null) {
			r.x = clipRegion.x;
			r.y = clipRegion.y;
			r.width = clipRegion.width;
			r.height = clipRegion.height;
		}
	}

	public void setClipBounds(Rectangle r) {
		this.clipRegion = (r == null) ? null : new Rectangle(originX + r.x,
				originY + r.y, r.width, r.height);
	}

	public void onResize() {
		if (!sizeChanged) {
			sizeChanged = true;
		}
	}

	public void nextFrame() {
		if (show != null) {
			show.nextFrame(this);
		}
		sizeChanged = false;
	}

	public void setCaughtUp() {
		if (show != null) {
			show.setCaughtUp();
		}
	}

	public void paintFrame() {
		if (show != null) {
			show.paintFrame(this);
		}
	}

	public void renderWidget(Widget w, int x, int y) {
		renderWidget(w, x, y, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}

	public void renderWidget(Widget w, int x, int y, int width, int height) {
		final Element elem = w.getElement();
		final Style widgetStyle = elem.getStyle();

		if (width != Integer.MIN_VALUE) {
			w.setWidth(width + Unit.PX.toString());
			// widgetStyle.setWidth(width, Unit.PX);
		}
		if (height != Integer.MIN_VALUE) {
			w.setHeight(height + Unit.PX.toString());
			// widgetStyle.setHeight(height, Unit.PX);
		}

		if (clipRegion != null) {
			clipWidget(elem, w.getAbsoluteLeft(), w.getAbsoluteTop(),
					w.getOffsetWidth(), w.getOffsetHeight());
		}

		if (opacity >= 0 && opacity <= 255) {
			setOpacity(widgetStyle, opacity);
		}

		x -= ComputedStyle.getMarginLeft(elem);
		x -= ComputedStyle.getBorderLeftWidth(elem);
		x -= ComputedStyle.getPaddingLeft(elem);

		y -= ComputedStyle.getMarginTop(elem);
		y -= ComputedStyle.getBorderTopWidth(elem);
		y -= ComputedStyle.getPaddingTop(elem);

		if (!w.isAttached()) {
			target.add(w, originX + x, originY + y);
		} else {
			target.setWidgetPosition(w, originX + x, originY + y);
		}
	}

	private void clipWidget(Element elem, int absoluteLeft, int absoluteTop,
			int offsetWidth, int offsetHeight) {
		int x1 = absoluteLeft - target.getAbsoluteLeft();
		int y1 = absoluteTop - target.getAbsoluteTop();
		int x2 = x1 + offsetWidth;
		int y2 = y1 + offsetHeight;
		int X1 = clipRegion.x;
		int Y1 = clipRegion.y;
		int X2 = X1 + clipRegion.width;
		int Y2 = Y1 + clipRegion.height;

		x1 = (x1 < X1) ? (X1 - x1) : 0;
		y1 = (y1 < Y1) ? (Y1 - y1) : 0;

		x2 = (x2 > X2) ? offsetWidth - (x2 - X2) : offsetWidth;
		y2 = (y2 > Y2) ? offsetHeight - (y2 - Y2) : offsetHeight;

		setClip(elem, y1, x2, y2, x1);
	}

	private void setClip(Element elem, int top, int right, int bottom, int left) {
		StringBuilder sb = new StringBuilder();
		sb.append("rect(").append(top).append("px, ").append(right)
				.append("px, ").append(bottom).append("px, ").append(left)
				.append("px)");
		elem.getStyle().setProperty("clip", sb.toString());
	}

	private void setOpacity(Style style, double opacity) {
		style.setOpacity(opacity / 255.0);

		// XXX IE
		//style.setProperty("filter", "alpha(opacity=" + opacity + ")");
	}

}
