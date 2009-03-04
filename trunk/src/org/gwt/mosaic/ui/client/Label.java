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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.UserAgent;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWordWrap;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

/**
 * A widget that contains arbitrary text, <i>not</i> interpreted as HTML.
 * 
 * This widget uses a &lt;div&gt; element, causing it to be displayed with table
 * layout (GWT's Label widget is using block layout by default).
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <pre>
 * &lt;ul class='css'&gt;
 * &lt;li&gt;.gwt-Label { }&lt;/li&gt;
 * &lt;/ul&gt;
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Label extends Composite implements SourcesClickEvents,
    SourcesMouseEvents, SourcesMouseWheelEvents, HasHorizontalAlignment,
    HasText, HasWordWrap, HasDirection {

  private final com.google.gwt.user.client.ui.Label label;

  public static Label wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    Label label = new Label(element);

    // Mark it attached and remember it for cleanup.
    label.onAttach();
    RootPanel.detachOnWindowClose(label);

    return label;
  }

  /**
   * Creates an empty label.
   */
  public Label() {
    label = new com.google.gwt.user.client.ui.Label();
    initWidget(label);
  }

  /**
   * Creates a label with the specified text.
   * 
   * @param text the new label's text
   */
  public Label(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a label with the specified text.
   * 
   * @param text the new label's text
   * @param wordWrap <code>false</code> to disable word wrapping
   */
  public Label(String text, boolean wordWrap) {
    this(text);
    setWordWrap(wordWrap);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be either a &lt;div&gt; or &lt;span&gt; element.
   * 
   * @param element the element to be used
   */
  protected Label(Element element) {
    assert element.getTagName().equalsIgnoreCase("div")
        || element.getTagName().equalsIgnoreCase("span");

    // Use protected Element constructor by sub classing.
    label = new com.google.gwt.user.client.ui.Label(element) {
    };

    initWidget(label);
  }

  /**
   * Set display CSS attribute to table layout.
   * 
   * @param widget the widget to be wrapped
   */
  @Override
  protected void initWidget(Widget widget) {
    // Check that the widget is assigned to label.
    assert (widget == label);

    if (UserAgent.isIE6()) {
      final WidgetWrapper wrapper = new WidgetWrapper(label,
          HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP);
      super.initWidget(wrapper);
    } else {
      DOM.setStyleAttribute(label.getElement(), "display", "table");
      super.initWidget(label);
    }
  }

  private ResizableWidget resizableWidget = null;

  public void addToResizableWidgetCollection() {
    if (resizableWidget == null) {
      resizableWidget = new ResizableWidget() {
        public com.google.gwt.user.client.Element getElement() {
          return label.getElement();
        }

        public boolean isAttached() {
          return Label.this.isAttached();
        }

        public void onResize(int width, int height) {
          Widget parent = Label.this.getParent();
          if (parent != null) {
            if (parent instanceof DecoratorPanel) {
              parent = parent.getParent();
            } else {
              if (parent instanceof LayoutComposite) {
                parent = parent.getParent();
                if (parent instanceof DecoratorPanel) {
                  parent = parent.getParent();
                }
              }
              if (parent instanceof FormPanel) {
                parent = parent.getParent();
                if (parent instanceof DecoratorPanel) {
                  parent = parent.getParent();
                }
              }
            }
          }

          if (parent instanceof HasLayoutManager) {
            ((HasLayoutManager) parent).layout(true);
          }
        }
      };
    }
    ResizableWidgetCollection.get().add(resizableWidget);
  }

  public void removeFromResizableWidgetCollection() {
    if (resizableWidget != null) {
      ResizableWidgetCollection.get().remove(resizableWidget);
    }
  }

  public void addClickListener(ClickListener listener) {
    label.addClickListener(listener);
  }

  public void removeClickListener(ClickListener listener) {
    label.removeClickListener(listener);
  }

  public void addMouseListener(MouseListener listener) {
    label.addMouseListener(listener);
  }

  public void removeMouseListener(MouseListener listener) {
    label.removeMouseListener(listener);
  }

  public void addMouseWheelListener(MouseWheelListener listener) {
    label.addMouseWheelListener(listener);
  }

  public void removeMouseWheelListener(MouseWheelListener listener) {
    label.removeMouseWheelListener(listener);
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return label.getHorizontalAlignment();
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    if (UserAgent.isIE6()) {
      WidgetWrapper wrapper = (WidgetWrapper) getWidget();
      wrapper.setHorizontalAlignment(align);
    }
    label.setHorizontalAlignment(align);
  }

  public String getText() {
    return label.getText();
  }

  public void setText(String text) {
    label.setText(text);
  }

  public boolean getWordWrap() {
    return label.getWordWrap();
  }

  public void setWordWrap(boolean wrap) {
    label.setWordWrap(wrap);
  }

  public Direction getDirection() {
    return label.getDirection();
  }

  public void setDirection(Direction direction) {
    label.setDirection(direction);
  }

}
