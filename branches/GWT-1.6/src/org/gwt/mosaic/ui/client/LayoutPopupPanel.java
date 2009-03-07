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
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class LayoutPopupPanel extends PopupPanel implements HasLayoutManager {

  private final LayoutPanel layoutPanel;

  /**
   * Creates an empty popup panel. A child widget must be added to it before it
   * is shown.
   */
  public LayoutPopupPanel() {
    this(false, false);
  }

  /**
   * Creates an empty popup panel, specifying its "auto-hide" property.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   */
  public LayoutPopupPanel(boolean autoHide) {
    this(autoHide, false);
  }

  /**
   * Creates an empty popup panel, specifying its "auto-hide" and "modal"
   * properties.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   * @param modal <code>true</code> if keyboard or mouse events that do not
   *          target the PopupPanel or its children should be ignored
   */
  public LayoutPopupPanel(boolean autoHide, boolean modal) {
    super(autoHide, modal);

    layoutPanel = new LayoutPanel();
    layoutPanel.setPadding(0);
    super.setWidget(layoutPanel);
  }

  @Override
  public Widget getWidget() {
    if (layoutPanel.getWidgetCount() > 0) {
      return layoutPanel.getWidget(0);
    } else {
      return null;
    }
  }

  @Override
  public void setWidget(Widget w) {
    layoutPanel.clear();
    layoutPanel.add(w);
  }

  @Override
  protected void onLoad() {
    // afterLoad();

    calculateDecorationSize();

    if (desiredWidth != null && desiredHeight != null) {
      setSize(desiredWidth, desiredHeight);
    } else if (desiredWidth != null) {
      setWidth(desiredWidth);
    } else if (desiredHeight != null) {
      setHeight(desiredHeight);
    }

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        layout();
      }
    });
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
      // setSize("0px", "0px");
      // layoutPanel.setSize("0px", "0px");
      // layoutPanel.setSize("auto", "auto");
      int[] size = getLayoutPanel().getPreferredSize();
      setContentSize(size[0], size[1]);
      // delayedLayout(MIN_DELAY_MILLIS);
      layout();
    }
  }

  protected LayoutPanel getLayoutPanel() {
    return layoutPanel;
  }

  protected void setContentSize(int width, int height) {
    DOM.setContentAreaWidth(layoutPanel.getElement(), width);
    DOM.setContentAreaHeight(layoutPanel.getElement(), height);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    int[] result = layoutPanel.getPreferredSize();
    result[0] += decorationWidthCache;
    result[1] += decorationHeightCache;
    return result;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    layoutPanel.layout();

//    DeferredCommand.addCommand(new Command() {
//      public void execute() {
//        System.out.println(getOffsetWidth() + "x" + getOffsetHeight());
//      }
//    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#layout(boolean)
   */
  public void layout(boolean invalidate) {
    layoutPanel.layout(invalidate);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.gwt.mosaic.ui.client.layout.HasLayoutManager#invalidate(boolean)
   */
  public void invalidate(boolean layout) {
    layoutPanel.invalidate(layout);
  }

  private int decorationWidthCache = 0;
  private int decorationHeightCache = 0;

  private void calculateDecorationSize() {
    final int[] size = DOM.getBoxSize(layoutPanel.getElement());
    final int[] box = DOM.getBoxSize(getElement());
    decorationWidthCache = (box[0] - size[0]);
    decorationHeightCache = (box[1] - size[1]);
  }

  private String desiredHeight = null;

  @Override
  public void setHeight(String height) {
    // super.setHeight(height);
    desiredHeight = height;
    if (isAttached()) {
      layoutPanel.setHeight(height);
      final int[] size = DOM.getBoxSize(layoutPanel.getElement());
      setContentSize(size[0], size[1] - decorationHeightCache);
    }
  }

  private String desiredWidth = null;

  @Override
  public void setWidth(String width) {
    // super.setWidth(width);
    desiredWidth = width;
    if (isAttached()) {
      layoutPanel.setWidth(width);
      final int[] size = DOM.getBoxSize(layoutPanel.getElement());
      setContentSize(size[0] - decorationWidthCache, size[1]);
    }
  }

}
