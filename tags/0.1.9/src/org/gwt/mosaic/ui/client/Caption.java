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

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that is used as a header in e.g. <code>WindowPanel</code>.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Caption extends LayoutComposite implements HasHTML,
    SourcesMouseEvents {
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

  private MouseListenerCollection mouseListeners;

  private ClickListenerCollection clickListeners;

  private DoubleClickListenerCollection dblClickListeners;

  private final HTML caption = new HTML();

  private HorizontalPanel leftIconBox, rightIconBox;

  public Caption(String text) {
    this(text, false);
  }

  public Caption(String text, boolean asHTML) {
    final LayoutPanel layoutPanel = getWidget();
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
        getWidget().insert(leftIconBox, new BoxLayoutData(FillStyle.VERTICAL),
            0);
      }
      leftIconBox.add(w);
    } else {
      if (rightIconBox == null) {
        rightIconBox = new HorizontalPanel();
        rightIconBox.setStyleName(DEFAULT_STYLENAME + "-iconBoxRight");
        rightIconBox.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        getWidget().add(rightIconBox, new BoxLayoutData(FillStyle.VERTICAL));
      }
      if (rightIconBox.getWidgetCount() > 0) {
        rightIconBox.insert(w, 0);
      } else {
        rightIconBox.add(w);
      }
    }
  }

  public void addClickListener(ClickListener listener) {
    if (clickListeners == null) {
      clickListeners = new ClickListenerCollection();
      sinkEvents(Event.ONCLICK);
    }
    clickListeners.add(listener);
  }

  public void addDoubleClickListener(DoubleClickListener listener) {
    if (dblClickListeners == null) {
      dblClickListeners = new DoubleClickListenerCollection();
      sinkEvents(Event.ONDBLCLICK);
    }
    dblClickListeners.add(listener);
  }

  public void addMouseListener(MouseListener listener) {
    if (mouseListeners == null) {
      mouseListeners = new MouseListenerCollection();
      sinkEvents(Event.MOUSEEVENTS);
    }
    mouseListeners.add(listener);
  }

  public void clear() {
    if (leftIconBox != null) {
      leftIconBox.clear();
    }
    if (rightIconBox != null) {
      rightIconBox.clear();
    }
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

  @Override
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT:
        if (mouseListeners != null) {
          mouseListeners.fireMouseEvent(this, event);
        }
        break;
      case Event.ONCLICK:
        if (clickListeners != null) {
          clickListeners.fireClick(this);
        }
        break;
      case Event.ONDBLCLICK:
        if (dblClickListeners != null) {
          dblClickListeners.fireDblClick(this);
        }
        break;
    }
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

  public void removeClickListener(ClickListener listener) {
    if (clickListeners != null) {
      clickListeners.remove(listener);
    }
  }

  public void removeDoubleClickListener(DoubleClickListener listener) {
    if (dblClickListeners != null) {
      dblClickListeners.remove(listener);
    }
  }

  public void removeMouseListener(MouseListener listener) {
    if (mouseListeners != null) {
      mouseListeners.remove(listener);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    caption.setHTML(html);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
   */
  public void setText(String text) {
    caption.setText(text);
  }

}
