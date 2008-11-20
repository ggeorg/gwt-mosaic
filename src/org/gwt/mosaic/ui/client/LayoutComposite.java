/*
 * Copyright 2008 Google Inc.
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

import org.gwt.mosaic.ui.client.layout.HasLayoutManager;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class LayoutComposite extends Composite implements
    HasLayoutManager {

  /**
   * Default constructor.
   */
  protected LayoutComposite() {
    initWidget(new LayoutPanel());
  }

  /**
   * Creates a LayoutComposite with the given element. This is protected so that
   * it can be used by a subclass that wants to substitute another element. The
   * element is presumed to be a &lt;div&gt;.
   * 
   * @param elem the element to be used for this panel.
   */
  protected LayoutComposite(Element elem) {
    initWidget(new LayoutPanel(elem) {
    });
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
   * @see org.mosaic.ui.client.layout.HasLayoutManager#getPreferredSize()
   */
  public int[] getPreferredSize() {
    return getWidget().getPreferredSize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.mosaic.ui.client.layout.HasLayoutManager#layout()
   */
  public void layout() {
    getWidget().layout();
  }
}
