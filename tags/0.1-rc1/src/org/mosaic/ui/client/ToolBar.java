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

import org.mosaic.ui.client.layout.BoxLayout;
import org.mosaic.ui.client.layout.BoxLayoutData;
import org.mosaic.ui.client.layout.HasLayoutManager;
import org.mosaic.ui.client.layout.LayoutPanel;
import org.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar extends Composite implements HasLayoutManager, HasName, IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolBar";

  public ToolBar() {
    final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout());

    initWidget(layoutPanel);

    setStyleName(DEFAULT_STYLENAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  protected LayoutPanel getWidget() {
    return (LayoutPanel) super.getWidget();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#layout()
   */
  public void layout() {
    getWidget().layout();
  }

  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setName(String name) {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return getWidget().getWidget(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return getWidget().getWidgetCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return getWidget().getWidgetIndex(child);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return getWidget().remove(index);
  }

  public void add(Widget widget) {
    final LayoutPanel layoutPanel = getWidget();
    layoutPanel.add(widget);
    if (((BoxLayout) layoutPanel.getLayout()).getOrient() == Orientation.HORIZONTAL) {
      getWidget().add(widget, new BoxLayoutData(FillStyle.VERTICAL));
    } else {
      getWidget().add(widget, new BoxLayoutData(FillStyle.HORIZONTAL));
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
    final BoxLayout boxLayout = (BoxLayout) getWidget().getLayout();
    if (boxLayout.getOrient() != orient) {
      boxLayout.setOrient(orient);
    }
  }

  public BoxLayout.Orientation getOrient() {
    return ((BoxLayout) getWidget().getLayout()).getOrient();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayout#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

}
