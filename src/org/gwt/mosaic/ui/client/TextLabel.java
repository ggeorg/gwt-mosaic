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

import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.i18n.client.BidiUtils;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that contains arbitrary text, <i>or</i> interpreted as HTML.
 * <p>
 * This widget uses a &lt;label&gt; element.
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <pre>
 * &lt;ul class='css'&gt;
 * &lt;li&gt;.mosaic-Label { }&lt;/li&gt;
 * &lt;/ul&gt;
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class TextLabel extends Widget implements SourcesClickEvents,
    SourcesMouseEvents, SourcesMouseWheelEvents, HasHorizontalAlignment,
    HasText, HasWordWrap, HasDirection, HasLayoutManager {

  /**
   * Creates a Label widget that wraps an existing &lt;div&gt; or &lt;span&gt;
   * element.
   * 
   * This element must already be attached to the document. If the element is
   * removed from the document, you must call
   * {@link RootPanel#detachNow(Widget)}.
   * 
   * @param element the element to be wrapped
   */
  public static TextLabel wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    TextLabel label = new TextLabel(element);

    // Mark it attached and remember it for cleanup.
    label.onAttach();
    RootPanel.detachOnWindowClose(label);

    return label;
  }

  private ClickListenerCollection clickListeners;
  private HorizontalAlignmentConstant horzAlign;
  private MouseListenerCollection mouseListeners;
  private MouseWheelListenerCollection mouseWheelListeners;

  /**
   * Creates an empty label.
   */
  public TextLabel() {
    setElement(Document.get().createLabelElement());
    setStyleName("mosaic-Label");
  }

  /**
   * Creates a label with the specified text.
   * 
   * @param text the new label's text
   */
  public TextLabel(String text) {
    this(text, false);

  }

  /**
   * Creates a label with the specified text.
   * 
   * @param text the new label's text
   * @param wordWrap <code>false</code> to disable word wrapping
   */
  public TextLabel(String text, boolean wordWrap) {
    this();
    setText(text);
    setWordWrap(wordWrap);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be a &lt;label&gt; element.
   * 
   * @param element the element to be used
   */
  protected TextLabel(Element element) {
    setElement(element);
    assert element.getTagName().equalsIgnoreCase("label");
  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
      sinkEvents(Event.ONCLICK);
    }
    clickListeners.add(listener);
  }

  public void addMouseListener(MouseListener listener) {
    if (mouseListeners == null) {
      mouseListeners = new MouseListenerCollection();
      sinkEvents(Event.MOUSEEVENTS);
    }
    mouseListeners.add(listener);
  }

  public void addMouseWheelListener(MouseWheelListener listener) {
    if (mouseWheelListeners == null) {
      mouseWheelListeners = new MouseWheelListenerCollection();
      sinkEvents(Event.ONMOUSEWHEEL);
    }
    mouseWheelListeners.add(listener);
  }

  public Direction getDirection() {
    return BidiUtils.getDirectionOnElement(getElement());
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horzAlign;
  }

  public String getText() {
    return getElement().getInnerText();
  }

  public boolean getWordWrap() {
    return !getElement().getStyle().getProperty("whiteSpace").equals("nowrap");
  }

  @Override
  public void onBrowserEvent(Event event) {
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        if (clickListeners != null) {
          clickListeners.fireClick(this);
        }
        break;

      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT:
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        break;

      case Event.ONMOUSEWHEEL:
        if (mouseWheelListeners != null) {
          mouseWheelListeners.fireMouseWheelEvent(this, event);
        }
        break;
    }
  }

  public void removeClickListener(ClickListener listener) {
    if (clickListeners != null) {
      clickListeners.remove(listener);
    }
  }

  public void removeMouseListener(MouseListener listener) {
    if (mouseListeners != null) {
      mouseListeners.remove(listener);
    }
  }

  public void removeMouseWheelListener(MouseWheelListener listener) {
    if (mouseWheelListeners != null) {
      mouseWheelListeners.remove(listener);
    }
  }

  public void setDirection(Direction direction) {
    BidiUtils.setDirectionOnElement(getElement(), direction);
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    horzAlign = align;
    getElement().getStyle().setProperty("textAlign", align.getTextAlignString());
  }

  public void setText(String text) {
    getElement().setInnerText(text);
  }

  /**
   * Link this label with another form control by id attribute.
   * 
   * @param htmlFor the id attribute of the other form control
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-for">W3C
   *      HTML Specification</a>
   */
  public void setLabelFor(String htmlFor) {
    final LabelElement labelElem = getElement().cast();
    labelElem.setHtmlFor(htmlFor);
  }

  /**
   * This attribute links this label with another form control by id attribute.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/1999/REC-html401-19991224/interact/forms.html#adef-for">W3C
   *      HTML Specification</a>
   */
  public String getLabelFor() {
    return ((LabelElement) getElement().cast()).getHtmlFor();
  };

  public void setWordWrap(boolean wrap) {
    getElement().getStyle().setProperty("whiteSpace",
        wrap ? "normal" : "nowrap");
  }

  public Dimension getPreferredSize() {
    invalidate();
    return new Dimension(getOffsetWidth(), getOffsetHeight());
  }

  public void invalidate() {
    setSize("auto", "auto");
  }

  public void layout() {
    // Nothing to do here!
  }
}
