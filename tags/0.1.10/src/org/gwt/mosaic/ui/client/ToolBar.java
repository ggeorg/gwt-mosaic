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
package org.gwt.mosaic.ui.client;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar extends LayoutComposite implements IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolBar";

  public ToolBar() {
    super();
    getLayoutPanel().setLayout(new BoxLayout());
    setStyleName(DEFAULT_STYLENAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return getLayoutPanel().getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return getLayoutPanel().getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return getLayoutPanel().getWidgetIndex(child);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return getLayoutPanel().remove(index);
  }

  public void add(Widget widget) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.add(widget);
    if (((BoxLayout) layoutPanel.getLayout()).getOrient() == Orientation.HORIZONTAL) {
      getLayoutPanel().add(widget, new BoxLayoutData(FillStyle.VERTICAL));
    } else {
      getLayoutPanel().add(widget, new BoxLayoutData(FillStyle.HORIZONTAL));
    }
  }

  /**
   * Adds a thin line to the {@link ToolBar} to separate sections of toolbar
   * items.
   * 
   * @return the {@link ToolBarSeparator} object created
   */
  public ToolBarSeparator addSeparator() {
    final ToolBarSeparator sep = new ToolBarSeparator();
    add(sep);
    return sep;
  }

  public void setOrient(BoxLayout.Orientation orient) {
    final BoxLayout boxLayout = (BoxLayout) getLayoutPanel().getLayout();
    if (boxLayout.getOrient() != orient) {
      boxLayout.setOrient(orient);
    }
  }

  public BoxLayout.Orientation getOrient() {
    return ((BoxLayout) getLayoutPanel().getLayout()).getOrient();
  }

}
