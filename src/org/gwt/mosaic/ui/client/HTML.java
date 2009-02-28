/*
 * Copyright 2006 Google Inc.
 * 
 * Copyright (c) 2009 GWT Mosaic Georgios J. Georgopoulos
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
package org.gwt.mosaic.ui.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that can contain arbitrary HTML.
 * 
 * This widget uses a &lt;div&gt; element, causing it to be displayed with table
 * layout (GWT's HTML widget is using block layout by default).
 * <p>
 * If you only need a simple label (text, but not HTML), then the {@link Label}
 * widget is more appropriate, as it disallows the use of HTML, which can lead
 * to potential security issues if not used properly.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <pre>
 * &lt;ul class='css'&gt;
 * &lt;li&gt;.gwt-HTML { }&lt;/li&gt;
 * &lt;/ul&gt;
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class HTML extends Label implements HasHTML {

  /**
   * Creates an HTML widget that wraps an existing &lt;div&gt; or &lt;span&gt;
   * element.
   * 
   * This element must already be attached to the document. If the element is
   * removed from the document, you must call
   * {@link RootPanel#detachNow(Widget)}.
   * 
   * @param element the element to be wrapped
   */
  public static HTML wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    HTML html = new HTML(element);

    // Mark it attached and remember it for cleanup.
    html.onAttach();
    RootPanel.detachOnWindowClose(html);

    return html;
  }

  /**
   * Creates an empty HTML widget.
   */
  public HTML() {
    super();
    setStyleName("gwt-HTML");
  }

  /**
   * Creates an HTML widget with the specified HTML contents.
   * 
   * @param html the new widget's HTML contents
   */
  public HTML(String html) {
    this();
    setHTML(html);
  }

  /**
   * Creates an HTML widget with the specified contents, optionally treating it
   * as HTML, and optionally disabling word wrapping.
   * 
   * @param html the widget's contents
   * @param wordWrap <code>false</code> to disable word wrapping
   */
  public HTML(String html, boolean wordWrap) {
    this(html);
    setWordWrap(wordWrap);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be either a &lt;div&gt; or &lt;span&gt; element.
   * 
   * @param element the element to be used
   */
  protected HTML(Element element) {
    super(element);
  }

  public String getHTML() {
    return getElement().getInnerHTML();
  }

  public void setHTML(String html) {
    getElement().setInnerHTML(html);
  }
}
