/*
 * Copyright (c) 2008-2010 GWT Mosaic Georgios J. Georgopoulos.
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
 * A {@code DecoratedPopupPanel} that contains a {@link LayoutPanel}.
 * <p>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <dt>.gwt-DecoratedPopupPanel</dt>
 * <dd>the outside of the popup</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupContent</dt>
 * <dd>the wrapper around the content</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopLeft</dt>
 * <dd>the top left cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopCenter</dt>
 * <dd>the top center cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopRight</dt>
 * <dd>the top right cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupTopRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleLeft</dt>
 * <dd>the middle left cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleCenter</dt>
 * <dd>the middle center cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleRight</dt>
 * <dd>the middle right cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupMiddleRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomLeft</dt>
 * <dd>the bottom left cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomCenter</dt>
 * <dd>the bottom center cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomRight</dt>
 * <dd>the bottom right cell</dd>
 * <dt>.gwt-DecoratedPopupPanel .popupBottomRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.mosaic-popupLayoutPanel</dt>
 * <dd>the LayoutPanel used</dd>
 * </ul>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class DecoratedLayoutPopupPanel extends AbstractDecoratedPopupPanel
    implements HasLayoutManager {

  // Used to cache requested size is popup panel is not attached
  private String desiredHeight = null, desiredWidth = null;

  private Command layoutCommand = new Command() {
    public void execute() {
      DecoratedLayoutPopupPanel.this.layout();
    }
  };

  /**
   * Creates an empty {@code DecoratedPopupPanel}.
   */
  public DecoratedLayoutPopupPanel() {
    this(false, false);
  }

  /**
   * Creates an empty {@code DecoratedPopupPanel}, specifying its "auto-hide"
   * property.
   * 
   * @param autoHide <code>true</code> if the popup should be automatically
   *          hidden when the user clicks outside of it
   */
  public DecoratedLayoutPopupPanel(boolean autoHide) {
    this(autoHide, false);
  }

  /**
   * Creates an empty {@code DecoratedPopupPanel}, specifying its "auto-hide"
   * and "modal" properties.
   * 
   * @param autoHide {@code true} if the popup should be automatically hidden
   *          when the user clicks outside of it
   * @param modal {@code true} if keyboard or mouse events that do not target
   *          the {@code DecoratedPopupPanel} or its children should be ignored
   */
  public DecoratedLayoutPopupPanel(boolean autoHide, boolean modal) {
    this(autoHide, modal, "popup");
  }

  /**
   * Creates an empty {@code DecoratedPopupPanel} using the specified style
   * names.
   * 
   * @param autoHide {@code true} if the popup should be automatically hidden
   *          when the user clicks outside of it
   * @param modal {@code true} if keyboard or mouse events that do not target
   *          the {@code DecoratedPopupPanel} or its children should be ignored
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
    layoutPanel.setStyleName("mosaic-popupLayoutPanel");
    super.setWidget(layoutPanel);
  }

  // HasWidgets implementation ---------------------------------------------

  /**
   * Adds a widget to this {@code DecoratedLayoutPopupPanel}.
   * 
   * @param w the child widget to be added
   * @see com.google.gwt.user.client.ui.SimplePanel#add(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public void add(Widget w) {
    // Can't add() more than one widget to a LayoutPoupPanel.
    if (getWidget() != null) {
      throw new IllegalStateException(
          "DecoratedLayoutPopupPanel can only contain one child widget");
    }
    setWidget(w);
  }

  /**
   * Removes all child widgets.
   * 
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#clear()
   */
  @Override
  public void clear() {
    getLayoutPanel().clear();
  }

  /**
   * Gets an iterator for the contained widgets. This iterator has to implement
   * {@link Iterator#remove()}.
   * 
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#iterator()
   */
  @Override
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  /**
   * Removes a child widget.
   * 
   * @param w the widget to be removed
   * @return <code>true</code> if the widget was present
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#remove(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public boolean remove(Widget w) {
    return getLayoutPanel().remove(w);
  }

  // SimplePanel overrides -------------------------------------------------

  /**
   * Sets this {@code DecoratedLayoutPopupPanel}'s widget. Any existing child
   * widget will be removed.
   * 
   * @param w the panel's new widget, or {@code null} to clear the panel
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#setWidget(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public void setWidget(Widget w) {

    /*
     * NOTE: super.setWidget() calls maybeUpdateSize()
     */

    // Validate
    if (w == getWidget()) {
      return;
    }

    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.clear();
    layoutPanel.add(w);
  }

  /**
   * Gets the {@code LayoutPopupPanel}'s child widget.
   * 
   * @return the child widget, or {@code null} if none is present
   * @see com.google.gwt.user.client.ui.DecoratedPopupPanel#getWidget()
   */
  @Override
  public Widget getWidget() {
    final LayoutPanel layoutPanel = getLayoutPanel();
    if (layoutPanel.getWidgetCount() != 0) {
      return layoutPanel.getWidget(0);
    } else {
      return null;
    }
  }

  // SimplePanel overrides -------------------------------------------------

  /**
   * Sets the height of the {@code LayoutPopupPanel}.
   * 
   * @param height the object's new height, in CSS units (e.g. "10px", "1em")
   * @see com.google.gwt.user.client.ui.PopupPanel#setHeight(java.lang.String)
   */
  @Override
  public void setHeight(String height) {
    if (isAttached()) {
      final int[] decoratorBorder = getDecoratorBorder();
      setContentSize(-1, DOM.toPixelSize(height, false) - decoratorBorder[1]);
    } else {
      desiredHeight = height;
    }
  }

  /**
   * Sets the width of the {@code LayoutPopupPanel}.
   * 
   * @param width the object's new width, in CSS units (e.g. "10px", "1em")
   * @see com.google.gwt.user.client.ui.PopupPanel#setWidth(java.lang.String)
   */
  @Override
  public void setWidth(String width) {
    if (isAttached()) {
      final int[] decoratorBorder = getDecoratorBorder();
      setContentSize(DOM.toPixelSize(width, true) - decoratorBorder[0], -1);
    } else {
      desiredWidth = width;
    }
  }

  /**
   * A panel's onLoad method will be called after all of its children are
   * attached.
   */
  @Override
  protected void onLoad() {
    if (desiredWidth != null && desiredHeight != null) {
      setSize(desiredWidth, desiredHeight);
      desiredWidth = desiredHeight = null;
    } else if (desiredWidth != null) {
      setWidth(desiredWidth);
      desiredWidth = null;
    } else if (desiredHeight != null) {
      setHeight(desiredHeight);
      desiredHeight = null;
    }

    // Wait for the popup to be attacted
    DeferredCommand.addCommand(layoutCommand);
  }

  // HasLayoutManager implementation ---------------------------------------

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

  // -----------------------------------------------------------------------

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

  /**
   * TODO make it private
   * 
   * @return the initial {@link LayoutPanel} used
   */
  protected LayoutPanel getLayoutPanel() {
    return (LayoutPanel) super.getWidget();
  }

  /**
   * Sets the content size (the size of the internal {@link LayoutPanel}).
   * 
   * @param d the content size to set
   */
  protected void setContentSize(final Dimension d) {
    WidgetHelper.setSize(getLayoutPanel(), d);
  }

  /**
   * Sets the content size (the size of the internal {@link LayoutPanel}).
   * 
   * @param width the content width to set
   * @param height the content height to set
   */
  protected void setContentSize(int width, int height) {
    setContentSize(new Dimension(width, height));
  }

  private int[] getDecoratorBorder() {
    final Dimension size2 = WidgetHelper.getOffsetSize(this);
    final Dimension size3 = WidgetHelper.getOffsetSize(getLayoutPanel());

    return new int[] {
        (size2.width - size3.width), (size2.height - size3.height)};
  }
}
