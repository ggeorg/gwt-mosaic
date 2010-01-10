/*
 * Copyright 2008-2010 Georgios J. Georgopoulos.
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

import java.util.Iterator;

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel which typically contains a row of tool buttons.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class ToolBar extends LayoutComposite implements IndexedPanel,
    HasWidgets {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-ToolBar";

  /**
   * Creates a {@code ToolBar} instance.
   */
  public ToolBar() {
    super(new BoxLayout());
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return getLayoutPanel().getWidget(index);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return getLayoutPanel().getWidgetCount();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return getLayoutPanel().getWidgetIndex(child);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return getLayoutPanel().remove(index);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    final BoxLayout boxLayoutMgr = (BoxLayout) layoutPanel.getLayout();
    if (w instanceof ToolBarSpring) {
      layoutPanel.add(w, new BoxLayoutData(FillStyle.BOTH));
    } else if (boxLayoutMgr.getOrientation() == Orientation.HORIZONTAL) {
      layoutPanel.add(w, new BoxLayoutData(FillStyle.VERTICAL));
    } else {
      getLayoutPanel().add(w, new BoxLayoutData(FillStyle.BOTH));
    }
  }

  /**
   * Adds a thin line to the {@link ToolBar} to separate sections of {@code
   * ToolBar} items.
   * 
   * @return the {@link ToolBarSeparator} instance added
   */
  public ToolBarSeparator addSeparator() {
    final ToolBarSeparator separator = new ToolBarSeparator();
    add(separator);
    return separator;
  }

  /**
   * Adds a flexible space between {@code ToolBar} items.
   * 
   * @return the {@link ToolBarSpring} instance added
   */
  public ToolBarSpring addSpring() {
    final ToolBarSpring spring = new ToolBarSpring();
    add(spring);
    return spring;
  }

  /**
   * Returns the {@code ToolBar} orientation. Whether the {@code ToolBar}
   * children are oriented horizontally or vertically.
   * 
   * @return the {@code ToolBar} orientation
   * @deprecated use {@link #getOrientation()} instead
   */
  @Deprecated
  public BoxLayout.Orientation getOrient() {
    return ((BoxLayout) getLayoutPanel().getLayout()).getOrientation();
  }

  /**
   * Returns the {@code ToolBar} orientation. Whether the {@code ToolBar}
   * children are oriented horizontally or vertically.
   * 
   * @return the {@code ToolBar} orientation
   * @see #setOrientation(Orientation)
   */
  public BoxLayout.Orientation getOrientation() {
    return getOrient();
  }

  /**
   * Used to specify whether the {@code ToolBar} children are oriented
   * horizontally or vertically. The default value is horizontal.
   * 
   * @param orient specifies if child elements are placed next to each other in
   *          a row (horizontal) or in a column (vertical)
   * @deprecated use {{@link #setOrientation(Orientation)} instead
   */
  @Deprecated
  public void setOrient(BoxLayout.Orientation orient) {
    final BoxLayout boxLayout = (BoxLayout) getLayoutPanel().getLayout();
    if (boxLayout.getOrientation() != orient) {
      boxLayout.setOrientation(orient);
    }
  }

  /**
   * Used to specify whether the {@code ToolBar} children are oriented
   * horizontally or vertically. The default value is horizontal.
   * 
   * @param orient specifies if child elements are placed next to each other in
   *          a row (horizontal) or in a column (vertical)
   */
  public void setOrientation(BoxLayout.Orientation orient) {
    setOrient(orient);
  }

  /**
   * Sets the orientation of child widgets (used by UiBinder).
   * 
   * @param orient the orientation of child widgets, can be {@code horizontal}
   *          or {@code vertical}
   * @see #setOrientation(Orientation)
   * @see #getOrientation()
   */
  public void setOrientation(String orient) {
    orient = orient.trim().toLowerCase();
    if (orient.equals("horizontal".intern())) {
      setOrientation(Orientation.HORIZONTAL);
    } else if (orient.equals("vertical".intern())) {
      setOrientation(Orientation.VERTICAL);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    getLayoutPanel().clear();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget w) {
    return getLayoutPanel().remove(w);
  }

}
