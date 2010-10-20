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

import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.client.scene.Control;
import org.gwt.mosaic2g.client.scene.Show;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Bill Foote (http://jovial.com)
 * @author ggeorg
 */
public class Image extends Control implements LoadHandler, ErrorHandler {

	private String url;
	private boolean urlChanged = false;

	private boolean imageSetup;

	private com.google.gwt.user.client.ui.Image cachedWidget;

	public Image(Show show) {
		super(show, 0, 0);
	}

	public Image(Show show, int x, int y) {
		super(show, x, y);
	}

	public Image(Show show, int x, int y, int width, int height) {
		super(show, x, y, width, height);
	}

	public Image(Show show, int x, int y, Property<Integer> width,
			Property<Integer> height) {
		super(show, x, y, width, height);
	}

	public Image(Show show, Property<Integer> x, Property<Integer> y,
			Property<Integer> width, Property<Integer> height) {
		super(show, x, y, width, height);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		assert url != null;
		if (url.equals(this.url)) {
			return;
		}
		if (isSetup()) {
			final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(
					url);
			img.addLoadHandler(new LoadHandler() {
				public void onLoad(LoadEvent event) {
					RootPanel.get().remove(img);
					Image.this.url = url;
					Image.this.urlChanged = true;
					markAsChanged();
				}
			});
			Style imgStyle = img.getElement().getStyle();
			imgStyle.setVisibility(Visibility.HIDDEN);
			RootPanel.get().add(img, Short.MIN_VALUE, Short.MIN_VALUE);
		} else {
			this.imageSetup = false;
			this.url = url;
			// this.urlChanged = true;
		}
		markAsChanged();
	}

	@Override
	protected Widget createWidget() {
		updateWidget(cachedWidget = new com.google.gwt.user.client.ui.Image(),
				true);
		cachedWidget.setUrl(url);
		cachedWidget.addLoadHandler(this);
		cachedWidget.addErrorHandler(this);
		updateWidget(cachedWidget, true);
		return cachedWidget;
	}

	@Override
	protected void updateWidget(Widget w, boolean init) {
		super.updateWidget(w, init);
		if (urlChanged) {
			cachedWidget.setUrl(url);
		}
	}

	@Override
	protected void setSetupMode(boolean mode) {
		super.setSetupMode(mode);
		if (mode) {
			if (!cachedWidget.isAttached()) {
				Style widgetStyle = cachedWidget.getElement().getStyle();
				widgetStyle.setVisibility(Visibility.HIDDEN);
				RootPanel.get().add(cachedWidget, Short.MIN_VALUE,
						Short.MIN_VALUE);
			}
		} else {
			imageSetup = false;
		}
	}

	@Override
	public boolean needsMoreSetup() {
		return !imageSetup;
	}

	public void onError(ErrorEvent event) {
		imageSetup = true;
		sendFeatureSetup();
	}

	public void onLoad(LoadEvent event) {
		imageSetup = true;
		sendFeatureSetup();
	}

	@Override
	protected void sendFeatureSetup() {
		RootPanel.get().remove(cachedWidget);
		Style widgetStyle = cachedWidget.getElement().getStyle();
		widgetStyle.setVisibility(Visibility.VISIBLE);
		super.sendFeatureSetup();
	}

	// XXX
	public void preload() {
		if (url != null) {
			final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(
					url);
			img.addLoadHandler(new LoadHandler() {
				public void onLoad(LoadEvent event) {
					RootPanel.get().remove(img);
				}
			});
			Style imgStyle = img.getElement().getStyle();
			imgStyle.setVisibility(Visibility.HIDDEN);
			RootPanel.get().add(img, Short.MIN_VALUE, Short.MIN_VALUE);
		}
	}

}
