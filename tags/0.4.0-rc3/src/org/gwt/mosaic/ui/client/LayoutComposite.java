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

import org.gwt.mosaic.core.client.Dimension;
import org.gwt.mosaic.ui.client.layout.FillLayout;
import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A type of widget that can wrap an aggregate of multiple other widgets
 * contained in a single {@link LayoutPanel}, hiding the wrapped
 * {@link LayoutPanel}'s methods.
 * 
 * @see LayoutPanel
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class LayoutComposite extends Composite implements
    HasLayoutManager {

  /**
   * Creates a new {@code LayoutComposite} with {@link FillLayout}.
   */
  protected LayoutComposite() {
    initWidget(new LayoutPanel());
  }

  /**
   * Creates a {@code LayoutComposite} with the given element. This is protected
   * so that it can be used by a subclass that wants to substitute another
   * element. The element is presumed to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   */
  protected LayoutComposite(Element elem) {
    initWidget(new LayoutPanel(elem) {
    });
  }

  /**
   * Creates a new {@code LayoutCombosite} with the specified layout manager.
   * 
   * @param layout the {@link LayoutManager} to use
   */
  protected LayoutComposite(LayoutManager layout) {
    initWidget(new LayoutPanel(layout));
  }

  /**
   * Creates a new {@code LayoutCombosite} with the specified layout manager and
   * with the given element. This is protected so that it can be used by a
   * subclass that wants to substitute another element. The element is presumed
   * to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   * @param layout the {@link LayoutManager} to use
   */
  protected LayoutComposite(Element elem, LayoutManager layout) {
    initWidget(new LayoutPanel(elem, layout) {
    });
  }

  /**
   * Returns the internal {@link LayoutPanel} for this {@code LayoutComposite}.
   * 
   * @return the internal {@link LayoutPanel}
   */
  protected LayoutPanel getLayoutPanel() {
    return (LayoutPanel) super.getWidget();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public Dimension getPreferredSize() {
    return getLayoutPanel().getPreferredSize();
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
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
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
  
  /**
   * @return the autoLayout
   */
  public boolean isAutoLayout() {
    return getLayoutPanel().isAutoLayout();
  }

  /**
   * @param autoLayout the autoLayout to set
   */
  public void setAutoLayout(boolean autoLayout) {
    getLayoutPanel().setAutoLayout(autoLayout);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
   */
  @Override
  public void setWidth(String width) {
    getLayoutPanel().setWidth(width);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.gwt.user.client.ui.UIObject#setHeight(java.lang.String)
   */
  @Override
  public void setHeight(String height) {
    getLayoutPanel().setHeight(height);
  }
}