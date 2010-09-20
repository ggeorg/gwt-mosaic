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
/*  
 * Copyright (c) 2007, Sun Microsystems, Inc.
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *  Note:  In order to comply with the binary form redistribution 
 *         requirement in the above license, the licensee may include 
 *         a URL reference to a copy of the required copyright notice, 
 *         the list of conditions and the disclaimer in a human readable 
 *         file with the binary form of the code that is subject to the
 *         above license.  For example, such file could be put on a 
 *         Blu-ray disc containing the binary form of the code or could 
 *         be put in a JAR file that is broadcast via a digital television 
 *         broadcast medium.  In any event, you must include in any end 
 *         user licenses governing any code that includes the code subject 
 *         to the above license (in source and/or binary form) a disclaimer 
 *         that is at least as protective of Sun as the disclaimers in the 
 *         above license.
 * 
 *         A copy of the required copyright notice, the list of conditions and
 *         the disclaimer will be maintained at 
 *         https://hdcookbook.dev.java.net/misc/license.html .
 *         Thus, licensees may comply with the binary form redistribution
 *         requirement with a text file that contains the following text:
 * 
 *             A copy of the license(s) governing this code is located
 *             at https://hdcookbook.dev.java.net/misc/license.html
 */
package org.gwt.mosaic2g.client.scene.control;

import org.gwt.mosaic2g.client.MyClientBundle;
import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Show;
import org.gwt.mosaic2g.client.scene.layout.Resizable;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@code Text} is useful for displaying text that is required to fit within a
 * specific space, and thus may need to use truncation to size the string to
 * fit.
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Text extends Control implements HasAlignment, Resizable {

	private String text;
	private boolean asHTML;
	private boolean textChanged;

	private HorizontalAlignmentConstant horzAlign = HasHorizontalAlignment.ALIGN_CENTER;
	private boolean horzAlignChanged;

	private VerticalAlignmentConstant vertAlign = HasVerticalAlignment.ALIGN_MIDDLE;
	private boolean vertAlignChanged;

	private LabelWidget cachedWidget;

	public Text(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		setText(text, false);
	}

	public void setText(String text, boolean asHTML) {
		if (text != null && text.equals(this.text) && asHTML == this.asHTML) {
			return;
		}
		this.text = text;
		this.asHTML = asHTML;
		this.textChanged = true;
		markAsChanged();
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		return horzAlign;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		if (horzAlign == align) {
			return;
		}
		horzAlign = align;
		horzAlignChanged = true;
	}

	public VerticalAlignmentConstant getVerticalAlignment() {
		return vertAlign;
	}

	public void setVerticalAlignment(VerticalAlignmentConstant align) {
		if (vertAlign == align) {
			return;
		}
		vertAlign = align;
		vertAlignChanged = true;
	}

	public int getPrefWidth() {
		if (cachedWidget == null) {
			return super.getWidth();
		} else {
			return cachedWidget.getOffsetWidth();
		}
	}

	public int getPrefHeight() {
		if (cachedWidget == null) {
			return super.getHeight();
		} else {
			return cachedWidget.getOffsetHeight();
		}
	}

	@Override
	protected Widget createWidget() {
		updateWidget(cachedWidget = new LabelWidget(), true);
		return cachedWidget;
	}

	@Override
	protected void updateWidget(Widget w, boolean init) {
		super.updateWidget(w, init);
		if (w instanceof HasHTML && (init || textChanged)) {
			final HasHTML hasHTML = (HasHTML) w;
			if (asHTML) {
				hasHTML.setHTML(text);
			} else {
				hasHTML.setText(text);
			}
			textChanged = false;
		}
		if (w instanceof HasHorizontalAlignment && (init || horzAlignChanged)) {
			((HasHorizontalAlignment) w).setHorizontalAlignment(horzAlign);
			horzAlignChanged = false;
		}
		if (w instanceof HasVerticalAlignment && (init || vertAlignChanged)) {
			((HasVerticalAlignment) w).setVerticalAlignment(vertAlign);
			vertAlignChanged = false;
		}
	}

	// -------------------------
	private class LabelWidget extends Composite implements HasAlignment,
			HasHTML, HasWordWrap {
		private final SimplePanel div;
		private final HTML htmlDiv;

		public LabelWidget() {
			initWidget(div = new SimplePanel());
			div.add(htmlDiv = new HTML());
			// setWordWrap(false);
			MyClientBundle.INSTANCE.css().ensureInjected();
			setStyleName(MyClientBundle.INSTANCE.css().labelWidget());
			htmlDiv.setStyleName(MyClientBundle.INSTANCE.css()
					.labelWidgetText());
		}

		public String getText() {
			return htmlDiv.getText();
		}

		public void setText(String text) {
			htmlDiv.setText(text);
		}

		public boolean getWordWrap() {
			return htmlDiv.getWordWrap();
		}

		public void setWordWrap(boolean wrap) {
			htmlDiv.setWordWrap(wrap);
		}

		public String getHTML() {
			return htmlDiv.getHTML();
		}

		public void setHTML(String html) {
			htmlDiv.setHTML(html);
		}

		public HorizontalAlignmentConstant getHorizontalAlignment() {
			return horzAlign;
		}

		public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
			horzAlign = align;
			htmlDiv.setHorizontalAlignment(align);
		}

		public VerticalAlignmentConstant getVerticalAlignment() {
			return vertAlign;
		}

		public void setVerticalAlignment(VerticalAlignmentConstant align) {
			vertAlign = align;
			htmlDiv.getElement()
					.getStyle()
					.setProperty(
							"verticalAlign",
							vertAlign == null ? "" : vertAlign
									.getVerticalAlignString());
		}
		
		@Override
		public void setWidth(String width) {
			super.setWidth(width);
			htmlDiv.setWidth(width);
		}

		@Override
		public void setHeight(String height) {
			super.setHeight(height);
			htmlDiv.setHeight(height);
		}

	}

}
