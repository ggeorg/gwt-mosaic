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

import java.util.Iterator;

import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.LayoutData;
import org.mosaic.ui.client.layout.LayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

public class CaptionLayoutPanel extends LayoutComposite implements HasWidgets,
    IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-CaptionLayoutPanel";

  private final Caption caption;
  private final LayoutPanel body;
  private Widget footer;

  public CaptionLayoutPanel() {
    this(null, false);
  }
  
  public CaptionLayoutPanel(final String text) {
    this(text, false);
  }

  public CaptionLayoutPanel(final String text, boolean asHTML) {
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(0);

    caption = new Caption(text, asHTML);
    layoutPanel.add(caption, new BoxLayoutData(FillStyle.HORIZONTAL));

    body = new LayoutPanel();
    body.addStyleName("Body");
    layoutPanel.add(body, new BoxLayoutData(FillStyle.BOTH));

    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Adds a widget to this panel.
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    body.add(w);
  }

  /**
   * Appends the specified widget to the end of this container.
   * 
   * @param widget
   * @param layoutData
   */
  public void add(Widget widget, LayoutData layoutData) {
    body.add(widget, layoutData);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    body.clear();
  }

  protected Widget getFooter() {
    return footer;
  }

  public Caption getHeader() {
    return caption;
  }

  public LayoutManager getLayout() {
    return body.getLayout();
  }

  public int getPadding() {
    return body.getPadding();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return body.getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return body.getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return body.getWidgetIndex(child);
  }

  public int getWidgetSpacing() {
    return body.getWidgetSpacing();
  }

  void hideContents(boolean flag) {
    body.setVisible(!flag);
    if (footer != null) {
      footer.setVisible(!flag);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return body.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return body.remove(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget w) {
    return body.remove(w);
  }

  protected void setFooter(Widget footer) {
    if (this.footer != null) {
      getWidget().remove(this.footer);
    }
    this.footer = footer;
    if (this.footer != null) {
      getWidget().add(this.footer, new BoxLayoutData(FillStyle.HORIZONTAL));
    }
  }

  public void setLayout(LayoutManager layout) {
    body.setLayout(layout);
  }

  public void setPadding(int padding) {
    body.setPadding(padding);
  }

  public void setCollapsed(boolean collapsed) {
    body.setVisible(!collapsed);
  }
  
  public boolean isCollapsed() {
    return !body.isVisible();
  }

}
