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
import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.util.WidgetHelper;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author johan.rydberg(at)gmail.com
 * 
 */
public class SheetLayoutPanel extends SheetPanel implements HasLayoutManager {

  /**
   * Creates an empty sheet panel. A child widget must be added to it before it
   * is shown.
   */
  public SheetLayoutPanel(Resources resources) {
    super(resources);
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setPadding(0);
    super.setWidget(layoutPanel);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public Dimension getPreferredSize() {
    final Dimension result = getLayoutPanel().getPreferredSize();

    final int[] decoratorBorder = getDecoratorBorder();

    result.width += decoratorBorder[0];
    result.height += decoratorBorder[1];

    return result;
  }

  @Override
  public Widget getWidget() {
    final LayoutPanel layoutPanel = getLayoutPanel();
    if (layoutPanel.getWidgetCount() > 0) {
      return layoutPanel.getWidget(0);
    } else {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate()
   */
  public void invalidate() {
    invalidate(null);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate(com.google.gwt.user.client.ui.Widget)
   */
  public void invalidate(Widget widget) {
    getLayoutPanel().invalidate(widget);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    getLayoutPanel().layout();
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.RequiresResize#onResize()
   */
  public void onResize() {
    layout();
  }
  
  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#needsLayout()
   */
  public boolean needsLayout() {
    return getLayoutPanel().needsLayout();
  }

  @Override
  public void setWidget(Widget w) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.clear();
    layoutPanel.add(w);
  }

  private int[] getDecoratorBorder() {
    final Dimension size2 = WidgetHelper.getOffsetSize(this);
    final Dimension size3 = WidgetHelper.getOffsetSize(getLayoutPanel());

    return new int[] {
        (size2.width - size3.width), (size2.height - size3.height)};
  }

  protected LayoutPanel getLayoutPanel() {
    return (LayoutPanel) super.getWidget();
  }

  @Override
  protected void onLoad() {

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        layout();
      }
    });
  }

}
