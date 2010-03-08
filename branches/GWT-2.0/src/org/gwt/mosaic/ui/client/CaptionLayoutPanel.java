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

import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.uibinder.client.ElementParserToUse;
import com.google.gwt.user.client.ui.AttachDetachHelper;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
@ElementParserToUse(className = "org.gwt.mosaic.ui.elementparsers.CaptionLayoutPanelParser")
public class CaptionLayoutPanel extends LayoutComposite implements HasWidgets,
    IndexedPanel {

  /**
   * The default style name.
   */
  private static final String DEFAULT_STYLENAME = "mosaic-CaptionLayoutPanel";

  private final Caption caption;
  private final LayoutPanel body;
  private Widget footer;

  private CollapsedListenerCollection collapsedListeners;

  private boolean collapsed;

  public CaptionLayoutPanel() {
    this(null, false);
  }

  public CaptionLayoutPanel(final String text) {
    this(text, false);
  }

  public CaptionLayoutPanel(final String text, boolean asHTML) {
    final LayoutPanel layoutPanel = getLayoutPanel();
    layoutPanel.setLayout(new BoxLayout(Orientation.VERTICAL));
    layoutPanel.setWidgetSpacing(0);

    caption = new Caption(text, asHTML);
    layoutPanel.add(caption, new BoxLayoutData(FillStyle.HORIZONTAL));

    body = new LayoutPanel();
    body.addStyleName("Body");
    layoutPanel.add(body, new BoxLayoutData(FillStyle.BOTH));

    setStyleName(DEFAULT_STYLENAME);
  }

  // HasWidgets implementation ---------------------------------------------

  /**
   * Adds a widget to this panel.
   * 
   * @param w the child widget to be added
   * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
   */
  public void add(Widget w) {
    body.add(w);
  }

  /**
   * Adds a widget to this panel.
   * 
   * @param w the child widget to be added
   * @param layoutData TODO
   */
  public void add(Widget w, LayoutData layoutData) {
    body.add(w, layoutData);
  }

  /**
   * Removes all child widgets.
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#clear()
   */
  public void clear() {
    body.clear();
  }

  /**
   * Gets an iterator for the contained widgets. This iterator has to implement
   * {@link Iterator#remove()}.
   * 
   * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
   */
  public Iterator<Widget> iterator() {
    return body.iterator();
  }

  /**
   * Removes a child widget.
   * 
   * @param w the widget to be removed
   * @return <code>true</code> if the widget was present
   * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
   */
  public boolean remove(Widget w) {
    return body.remove(w);
  }

  // IndexedPanel implementation -------------------------------------------

  /**
   * Gets the child widget at the specified index.
   * 
   * @param index the child widget's index
   * @return the child widget
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
   */
  public Widget getWidget(int index) {
    return body.getWidget(index);
  }

  /**
   * Gets the number of child widgets in this panel.
   * 
   * @return the number of children
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
   */
  public int getWidgetCount() {
    return body.getWidgetCount();
  }

  /**
   * Gets the index of the specified child widget.
   * 
   * @param child the widget to be found
   * @return the widget's index, or {@code -1} if it is not a child of this
   *         panel
   * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
   */
  public int getWidgetIndex(Widget child) {
    return body.getWidgetIndex(child);
  }

  /**
   * Removes the widget at the specified index.
   * 
   * @param index the index of the widget to be removed
   * @return <code>false</code> if the widget is not present
   * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
   */
  public boolean remove(int index) {
    return body.remove(index);
  }

  // -----------------------------------------------------------------------

  @Override
  protected void doAttachChildren() {
    // See comment in doDetachChildren for an explanation of this call
    AttachDetachHelper.onAttach(getLayoutPanel());
  }

  @Override
  protected void doDetachChildren() {
    // We need to detach the decPanel (which is layoutPanel's parent) because it
    // is not part of the iterator of Widgets that this class returns (see the
    // iterator() method override).
    // Detaching the decPanel detaches both itself and its children. We do not
    // call super.onDetachChildren() because that would detach the decPanel's
    // children (redundantly) without detaching the decPanel itself.
    // This is similar to a {@link ComplexPanel}, but we do not want to expose
    // the decPanel widget, as its just an internal implementation.
    AttachDetachHelper.onDetach(getLayoutPanel());
  }

  // -----------------------------------------------------------------------

  public void addCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners == null) {
      collapsedListeners = new CollapsedListenerCollection();
    }
    collapsedListeners.add(listener);
  }

  protected void fireCollapsedChange(Widget sender) {
    if (collapsedListeners != null) {
      collapsedListeners.fireCollapsedChange(sender);
    }
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

  public int getWidgetSpacing() {
    return body.getWidgetSpacing();
  }

  void hideContent(boolean hideContent) {
    body.setVisible(!hideContent);
    invalidate(body);
    if (footer != null) {
      footer.setVisible(!hideContent);
      invalidate(footer);
    }
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void removeCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners != null) {
      collapsedListeners.remove(listener);
    }
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
    hideContent(collapsed);
    fireCollapsedChange(this);
  }

  protected void setFooter(Widget footer) {
    if (this.footer != null) {
      getLayoutPanel().remove(this.footer);
    }
    this.footer = footer;
    if (this.footer != null) {
      getLayoutPanel().add(this.footer, new BoxLayoutData(FillStyle.HORIZONTAL));
    }
  }

  public void setLayout(LayoutManager layout) {
    body.setLayout(layout);
  }

  public void setPadding(int padding) {
    body.setPadding(padding);
  }

  public void setWidgetSpacing(int spacing) {
    body.setWidgetSpacing(spacing);
  }

}
