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
import com.google.gwt.user.client.ui.AttachDetachHelper;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@code PopupPanel} that contains a {@link LayoutPanel}.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-PopupPanel</dt>
 * <dd>the outside of the popup</dd>
 * <dt>.gwt-PopupPanel .popupContent</dt>
 * <dd>the wrapper around the content</dd>
 * <dt>.gwt-PopupPanelGlass</dt>
 * <dd>the glass background behind the popup</dd>
 * <dt>.mosaic-popupLayoutPanel</dt>
 * <dd>the LayoutPanel used</dd>
 * </dl>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class LayoutPopupPanel extends PopupPanel implements HasLayoutManager {

  // Used to cache requested size is popup panel is not attached
  private String desiredHeight = null, desiredWidth = null;

  private Command layoutCommand = new Command() {
    public void execute() {
      LayoutPopupPanel.this.layout();
    }
  };

  /**
   * Creates an empty {@code LayoutPopupPanel}.
   */
  public LayoutPopupPanel() {
    this(false, false);
  }

  /**
   * Creates an empty {@code LayoutPopupPanel}, specifying its "auto-hide"
   * property.
   * 
   * @param autoHide {@code true} if the popup should be automatically hidden
   *          when the user clicks outside of it or the history token changes
   */
  public LayoutPopupPanel(boolean autoHide) {
    this(autoHide, false);
  }

  /**
   * Creates an empty {@code LayoutPopupPanel}, specifying its "auto-hide" and
   * "modal" properties.
   * 
   * @param autoHide {@code true} if the popup should be automatically hidden
   *          when the user clicks outside of it or the history token changes
   * @param modal {@code true} if keyboard or mouse events that do not target
   *          the {@code LayoutPopupPanel} or its children should be ignored
   */
  public LayoutPopupPanel(boolean autoHide, boolean modal) {
    super(autoHide, modal);
    final LayoutPanel layoutPanel = new LayoutPanel();
    layoutPanel.setStyleName("mosaic-popupLayoutPanel");
    super.setWidget(layoutPanel);
  }

  // HasWidgets implementation ---------------------------------------------

  /**
   * Adds a widget to this {@code LayoutPopupPanel}.
   * 
   * @param w the child widget to be added
   * @see com.google.gwt.user.client.ui.SimplePanel#add(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public void add(Widget w) {
    // Can't add() more than one widget to a LayoutPoupPanel.
    if (getWidget() != null) {
      throw new IllegalStateException(
          "LayoutPopupPanel can only contain one child widget");
    }
    setWidget(w);
  }

  /**
   * Removes all child widgets.
   * 
   * @see com.google.gwt.user.client.ui.Panel#clear()
   */
  @Override
  public void clear() {
    getLayoutPanel().clear();
  }

  /**
   * Gets an iterator for the contained widgets. This iterator has to implement
   * {@link Iterator#remove()}.
   * 
   * @see com.google.gwt.user.client.ui.SimplePanel#iterator()
   */
  @Override
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  /**
   * Removes a child widget.
   * 
   * @param w the widget to be removed
   * @return {@code true} if the widget was present
   * @see com.google.gwt.user.client.ui.SimplePanel#remove(com.google.gwt.user.client.ui.Widget)
   */
  @Override
  public boolean remove(Widget w) {
    return getLayoutPanel().remove(w);
  }

  // SimplePanel overrides -------------------------------------------------

  /**
   * Sets this {@code LayoutPopupPanel}'s widget. Any existing child widget will
   * be removed.
   * 
   * @param w the panel's new widget, or {@code null} to clear the panel
   * @see com.google.gwt.user.client.ui.PopupPanel#setWidget(com.google.gwt.user.client.ui.Widget)
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
   * @see com.google.gwt.user.client.ui.SimplePanel#getWidget()
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

  @Override
  protected void doAttachChildren() {
    // See comment in doDetachChildren for an explanation of this call
    AttachDetachHelper.onAttach(getLayoutPanel());
  }

  @Override
  protected void doDetachChildren() {
    // We need to detach the layoutPanel because it is not part of the iterator
    // of Widgets that this class returns (see the iterator() method override).
    // Detaching the layoutPanel detaches both itself and its children. We do
    // not call super.onDetachChildren() because that would detach the
    // layoutPanel's children (redundantly) without detaching the layoutPanel
    // itself. This is similar to a {@link ComplexPanel}, but we do not want to
    // expose the layoutPanel widget, as its just an internal implementation.
    AttachDetachHelper.onDetach(getLayoutPanel());
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
   * Causes this {@code LayoutPopupPanel} to be sized to fit the preferred size
   * and layouts of its sub-widgets. The {@link #layout()} method is called
   * after the preferred size is calculated.
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
   * @return the internal {@link LayoutPanel} used
   */
  protected LayoutPanel getLayoutPanel() {
    return (LayoutPanel) super.getWidget();
  }

  /**
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
