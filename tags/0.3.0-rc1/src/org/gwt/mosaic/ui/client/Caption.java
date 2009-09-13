/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that is used as a header in e.g. <code>WindowPanel</code>.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Caption extends LayoutComposite implements HasHTML,
    SourcesMouseEvents, HasClickHandlers, HasDoubleClickHandlers,
    HasAllMouseHandlers {
  public enum CaptionRegion {
    LEFT, RIGHT
  }

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-Caption";

  /**
   * The caption images to use.
   */
  public static final CaptionImages IMAGES = (CaptionImages) GWT.create(CaptionImages.class);

  private final HTMLLabel caption = new HTMLLabel();

  private HorizontalPanel leftIconBox, rightIconBox;

  public Caption(String text) {
    this(text, false);
  }

  public Caption(String text, boolean asHTML) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout());
    layoutPanel.setPadding(0);
    layoutPanel.setWidgetSpacing(0);

    layoutPanel.add(caption, new BoxLayoutData(FillStyle.BOTH));

    caption.setStyleName(DEFAULT_STYLENAME + "-text");

    if (asHTML) {
      setHTML(text);
    } else {
      setText(text);
    }

    setStyleName(DEFAULT_STYLENAME);
  }

  public void add(Widget w) {
    add(w, CaptionRegion.LEFT);
  }

  public void add(Widget w, CaptionRegion region) {
    if (CaptionRegion.LEFT == region) {
      if (leftIconBox == null) {
        leftIconBox = new HorizontalPanel();
        leftIconBox.setStyleName(DEFAULT_STYLENAME + "-iconBoxLeft");
        leftIconBox.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        getLayoutPanel().insert(leftIconBox,
            new BoxLayoutData(FillStyle.VERTICAL), 0);
      }
      leftIconBox.add(w);
    } else {
      if (rightIconBox == null) {
        rightIconBox = new HorizontalPanel();
        rightIconBox.setStyleName(DEFAULT_STYLENAME + "-iconBoxRight");
        rightIconBox.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        getLayoutPanel().add(rightIconBox,
            new BoxLayoutData(FillStyle.VERTICAL));
      }
      if (rightIconBox.getWidgetCount() > 0) {
        rightIconBox.insert(w, 0);
      } else {
        rightIconBox.add(w);
      }
    }
    invalidate();
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  /**
   * @deprecated Use {@link #addClickHandler} instead
   */
  @Deprecated
  public void addClickListener(ClickListener listener) {
    ListenerWrapper.WrappedClickListener.add(this, listener);
  }

  public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
    return addDomHandler(handler, DoubleClickEvent.getType());
  }

  /**
   * @deprecated Use {@link #addClickHandler} instead
   */
  @Deprecated
  public void addDoubleClickListener(DoubleClickListener listener) {
    ListenerWrapper.WrappedDoubleClickListener.add(this, listener);
  }

  /**
   * @deprecated Use {@link #addMouseOverHandler} {@link #addMouseMoveHandler},
   *             {@link #addMouseDownHandler}, {@link #addMouseUpHandler} and
   *             {@link #addMouseOutHandler} instead
   */
  @Deprecated
  public void addMouseListener(MouseListener listener) {
    ListenerWrapper.WrappedMouseListener.add(this, listener);
  }

  public void clear() {
    if (leftIconBox != null) {
      leftIconBox.clear();
    }
    if (rightIconBox != null) {
      rightIconBox.clear();
    }
    invalidate();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#getHTML()
   */
  public String getHTML() {
    return caption.getHTML();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return caption.getText();
  }

  public Widget getWidget(int index) {
    return getWidget(index, CaptionRegion.LEFT);
  }

  public Widget getWidget(int index, CaptionRegion region) {
    if (region == CaptionRegion.LEFT) {
      if (leftIconBox != null) {
        return leftIconBox.getWidget(index);
      }
    } else {
      if (rightIconBox != null) {
        return rightIconBox.getWidget(index);
      }
    }
    return null;
  }

  public boolean remove(Widget widget) {
    if (leftIconBox != null) {
      int index = leftIconBox.getWidgetIndex(widget);
      if (index != -1) {
        return leftIconBox.remove(index);
      }
    }
    if (rightIconBox != null) {
      return rightIconBox.remove(widget);
    }
    return false;
  }

  /**
   * @deprecated Use the {@link HandlerRegistration#removeHandler} method on the
   *             object returned by {@link #addClickHandler} instead
   */
  @Deprecated
  public void removeClickListener(ClickListener listener) {
    ListenerWrapper.WrappedClickListener.remove(this, listener);
  }

  /**
   * @deprecated Use the {@link HandlerRegistration#removeHandler} method on the
   *             object returned by an add*Handler method instead
   */
  @Deprecated
  public void removeDoubleClickListener(DoubleClickListener listener) {
    ListenerWrapper.WrappedDoubleClickListener.remove(this, listener);
  }

  /**
   * @deprecated Use the {@link HandlerRegistration#removeHandler} method on the
   *             object returned by an add*Handler method instead
   */
  @Deprecated
  public void removeMouseListener(MouseListener listener) {
    ListenerWrapper.WrappedMouseListener.remove(this, listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    caption.setHTML(html);
    invalidate();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    caption.setText(text);
    invalidate();
  }

  public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
    return addDomHandler(handler, MouseDownEvent.getType());
  }

  public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
    return addDomHandler(handler, MouseUpEvent.getType());
  }

  public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
    return addDomHandler(handler, MouseOutEvent.getType());
  }

  public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
    return addDomHandler(handler, MouseOverEvent.getType());
  }

  public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
    return addDomHandler(handler, MouseMoveEvent.getType());
  }

  public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
    return addDomHandler(handler, MouseWheelEvent.getType());
  }

}
