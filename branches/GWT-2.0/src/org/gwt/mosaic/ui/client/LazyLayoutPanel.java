/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos
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

import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience class to help lazy loading. The bulk of a {@code LazyLayoutPanel}
 * is not instantiated until {@link #setVisible}(true) or {@link #ensureWidget}
 * is called.
 * <p>
 * Similar to {@code com.google.gwt.user.client.ui.LazyPanel}, but with layout
 * manager support.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class LazyLayoutPanel extends LayoutComposite implements
    HasWidgets {

  private Widget widget = null;

  public LazyLayoutPanel() {
    super();
  }

  /**
   * Create the widget contained within the {@code LazyLayoutPanel}.
   * 
   * @return the lazy widget
   */
  protected abstract Widget createWidget();

  /**
   * Ensures that the widget has been created by calling {@link #createWidget()}
   * if {@link #getWidget()} returns {@code null}. Typically it is not necessary
   * to call this directly, as it is called as a side effect of a {@code
   * #setVisible(true)} call.
   */
  public void ensureWidget() {
    if (widget == null) {
      final LayoutPanel layoutPanel = getLayoutPanel();
      layoutPanel.add(widget = createWidget());
      layoutPanel.invalidate(widget);
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          layoutPanel.layout();
        }
      });
    }
  }

  /**
   * Gets the panel's child widget.
   * 
   * @return the child widget, or {@code null} if none is present
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  @Override
  public Widget getWidget() {
    return widget;
  }

  /**
   * Sets whether this object is visible. If {@code visible} is {@code true},
   * creates the sole child widget if necessary by calling {@link #ensureWidget}
   * 
   * @param visible {@code true} to show the object, {@code false} to hide it
   * @see com.google.gwt.user.client.ui.UIObject#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      ensureWidget();
    }
    super.setVisible(visible);
  }

  /**
   * Adds a widget to this panel.
   * 
   * @param w the child widget to be added
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    if (widget != null) {
      layoutPanel.clear();
    }
    widget = w;
    getLayoutPanel().add(widget);
  }

  /**
   * Return a simple iterator that enumerates the 0 or 1 elements in this panel.
   * 
   * @return the iterator
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return getLayoutPanel().iterator();
  }

  /**
   * Removes a child widget.
   * 
   * @param w the widget to be removed
   * @return {@code true} if the child was present
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget w) {
    // Validate.
    if (widget != w) {
      return false;
    }

    // Physical detach.
    getLayoutPanel().remove(w);

    // Logical detach.
    widget = null;

    return true;
  }

  /**
   * Removes all child widgets.
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    remove(widget);
  }
}
