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
import com.google.gwt.user.client.ui.AbstractDecoratedPopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DecoratedLayoutPopupPanel extends AbstractDecoratedPopupPanel
    implements HasLayoutManager {

  private String onLoadHeight = null;

  private String onLoadWidth = null;

  /**
   * Creates an empty decorated popup panel. A child widget must be added to it
   * before it is shown.
   */
  public DecoratedLayoutPopupPanel() {
    this(false, false);
  }

  /**
   * Creates an empty decorated popup panel, specifying its "auto-hide"
   * property.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   */
  public DecoratedLayoutPopupPanel(boolean autoHide) {
    this(autoHide, false);
  }

  /**
   * Creates an empty decorated popup panel, specifying its "auto-hide" and
   * "modal" properties.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   * @param modal <code>true</code> if keyboard or mouse events that do not
   *          target the PopupPanel or its children should be ignored
   */
  public DecoratedLayoutPopupPanel(boolean autoHide, boolean modal) {
    this(autoHide, modal, "popup");
  }

  /**
   * Creates an empty decorated popup panel using the specified style names.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   * @param modal <code>true</code> if keyboard or mouse events that do not
   *          target the PopupPanel or its children should be ignored
   * @param prefix the prefix applied to child style names
   */
  protected DecoratedLayoutPopupPanel(boolean autoHide, boolean modal,
      String prefix) {
    this(autoHide, modal, prefix, AnimationType.CENTER);
  }

  /**
   * Creates an empty decorated popup panel using the specified style names.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   * @param modal <code>true</code> if keyboard or mouse events that do not
   *          target the PopupPanel or its children should be ignored
   * @param prefix the prefix applied to child style names
   * @param type type the type of animation to use
   */
  protected DecoratedLayoutPopupPanel(boolean autoHide, boolean modal,
      String prefix, AnimationType type) {
    super(autoHide, modal, prefix, type);

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
   * Causes this popup to be sized to fit the preferred size and layouts of its
   * subcomponents. The {@link #layout()} method is called after the preferred
   * size is calculated.
   * 
   * @see #show()
   * @see #showModal()
   */
  public void pack() {
    if (!isAttached()) {
      show();
      pack();
    } else {
      // setSize("auto", "auto");
      setContentSize(getLayoutPanel().getPreferredSize());
    }
  }

  @Override
  public void setHeight(String height) {
    if (isAttached()) {
      final int[] decoratorBorder = getDecoratorBorder();
      setContentSize(-1, DOM.toPixelSize(height, false) - decoratorBorder[1]);
    } else {
      onLoadHeight = height;
    }
  }

  @Override
  public void setWidget(Widget w) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.clear();
    layoutPanel.add(w);
  }

  @Override
  public void setWidth(String width) {
    if (isAttached()) {
      final int[] decoratorBorder = getDecoratorBorder();
      setContentSize(DOM.toPixelSize(width, true) - decoratorBorder[0], -1);
    } else {
      onLoadWidth = width;
    }
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
    if (onLoadWidth != null && onLoadHeight != null) {
      setSize(onLoadWidth, onLoadHeight);
      onLoadWidth = onLoadHeight = null;
    } else if (onLoadWidth != null) {
      setWidth(onLoadWidth);
      onLoadWidth = null;
    } else if (onLoadHeight != null) {
      setHeight(onLoadHeight);
      onLoadHeight = null;
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        layout();
      }
    });
  }

  protected void setContentSize(final Dimension d) {
    WidgetHelper.setSize(getLayoutPanel(), d);
  }

  protected void setContentSize(int width, int height) {
    setContentSize(new Dimension(width, height));
  }

}
