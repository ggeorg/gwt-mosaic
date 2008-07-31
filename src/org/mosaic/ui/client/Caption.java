/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that is used as a header in e.g. <code>WindowPanel</code>.
 */
public class Caption extends FocusPanel implements HasHTML {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-Caption";

  private final HorizontalPanel hpanel = new HorizontalPanel();

  private final HorizontalPanel leftIconBox = new HorizontalPanel();
  private final HorizontalPanel rightIconBox = new HorizontalPanel();

  private final HTML caption = new HTML();

  public enum CaptionRegion {
    LEFT, RIGHT
  };

  public Caption(String text) {
    caption.setStyleName(DEFAULT_STYLENAME + "-text");
    leftIconBox.setStyleName(DEFAULT_STYLENAME + "-iconBoxLeft");
    rightIconBox.setStyleName(DEFAULT_STYLENAME + "-iconBoxRight");

    setText(text);

    hpanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

    hpanel.add(leftIconBox);
    hpanel.add(caption);
    hpanel.add(rightIconBox);

    hpanel.setCellHorizontalAlignment(leftIconBox, HorizontalPanel.ALIGN_LEFT);
    hpanel.setCellHorizontalAlignment(caption, HorizontalPanel.ALIGN_CENTER);
    hpanel.setCellHorizontalAlignment(rightIconBox, HorizontalPanel.ALIGN_RIGHT);

    hpanel.setWidth("100%");
    hpanel.setCellWidth(caption, "100%");

    super.add(hpanel);

    setStyleName(DEFAULT_STYLENAME);
  }

  @Override
  public void add(Widget w) {
    leftIconBox.add(w);
  }

  public void add(Widget w, CaptionRegion region) {
    if (CaptionRegion.LEFT == region) {
      leftIconBox.add(w);
    } else {
      if (rightIconBox.getWidgetCount() > 0) {
        rightIconBox.insert(w, 0);
      } else {
        rightIconBox.add(w);
      }
    }
  }

  public boolean remove(Widget widget) {
    int index = leftIconBox.getWidgetIndex(widget);
    if (index != -1) {
      return leftIconBox.remove(index);
    } else {
      return rightIconBox.remove(widget);
    }
  }

  public void clear() {
    leftIconBox.clear();
    rightIconBox.clear();
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
   * @see com.google.gwt.user.client.ui.HasHTML#setHTML(java.lang.String)
   */
  public void setHTML(String html) {
    caption.setHTML(html);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasText#getText()
   */
  public String getText() {
    return caption.getText();
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
