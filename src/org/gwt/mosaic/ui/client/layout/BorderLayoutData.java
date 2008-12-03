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
package org.gwt.mosaic.ui.client.layout;

import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.CollapsedListenerCollection;
import org.gwt.mosaic.ui.client.layout.BorderLayout.BorderLayoutRegion;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class BorderLayoutData extends LayoutData {

  double preferredSize = -1.0;

  BorderLayoutRegion region;

  boolean resizable, collapse;

  int minSize = 0, maxSize = -1;

  public BorderLayoutData() {
    this(BorderLayoutRegion.CENTER, false);
  }

  public BorderLayoutData(boolean decorate) {
    this(BorderLayoutRegion.CENTER, decorate);
  }

  public BorderLayoutData(BorderLayoutRegion region) {
    this(region, false);
  }

  public BorderLayoutData(BorderLayoutRegion region, boolean decorate) {
    super(decorate);
    this.region = region;
  }

  public BorderLayoutData(BorderLayoutRegion region, double preferredSize) {
    this(region, preferredSize, false);
  }

  public BorderLayoutData(BorderLayoutRegion region, double preferredSize,
      boolean decorate) {
    super(decorate);
    this.region = region;
    this.preferredSize = preferredSize;
  }

  public BorderLayoutData(BorderLayoutRegion region, double preferredSize,
      int minSize, int maxSize) {
    this(region, preferredSize, minSize, maxSize, false);
  }

  public BorderLayoutData(BorderLayoutRegion region, double preferredSize,
      int minSize, int maxSize, boolean decorate) {
    super(decorate);
    this.region = region;
    this.preferredSize = preferredSize;
    this.minSize = Math.max(0, minSize);
    this.maxSize = Math.max(0, maxSize);
    this.resizable = minSize < maxSize;
    // TODO
    // if (this.resizable) {
    // this.preferredSize = (this.preferredSize > this.minSize) ? Math.min(
    // this.preferredSize, this.maxSize) : this.minSize;
    // }
  }

  public BorderLayoutData(BorderLayoutRegion region, int minSize, int maxSize) {
    this(region, -1.0, minSize, maxSize, false);
  }

  public BorderLayoutData(BorderLayoutRegion region, int minSize, int maxSize,
      boolean decorate) {
    this(region, -1.0, minSize, maxSize, decorate);
  }

  public int getMaxSize() {
    return maxSize;
  }

  public int getMinSize() {
    return minSize;
  }

  public BorderLayoutRegion getRegion() {
    return region;
  }

  public boolean isResizable() {
    return resizable;
  }

  protected void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  protected void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  private CollapsedListenerCollection collapsedListeners;

  protected void addCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners == null) {
      collapsedListeners = new CollapsedListenerCollection();
    }
    collapsedListeners.add(listener);
  }

  protected void removeCollapsedListener(CollapsedListener listener) {
    if (collapsedListeners != null) {
      collapsedListeners.remove(listener);
    }
  }

  protected void fireCollapsedChange(Widget sender) {
    if (collapsedListeners != null) {
      collapsedListeners.fireCollapsedChange(sender);
    }
  }

  /**
   * Returns the user specified preferred size of a child widget, either width
   * or height in pixels or ratio depending on the {@link LayoutManager}.
   * Default is -1 which means that the widget's calculated preferred size
   * should be used instead. Values > 0 and <= 1 are ratios, 0 and values > 1
   * are pixels.
   * 
   * @return the preferred size or -1 indicating that the widget's calculated
   *         preferred size should be used instead of this value.
   * 
   * @see BaseLayout#getFlowWidth(com.google.gwt.user.client.ui.Widget)
   * @see BaseLayout#getFlowHeight(com.google.gwt.user.client.ui.Widget)
   */
  public double getPreferredSize() {
    return preferredSize;
  }

  /**
   * Sets the child widget's preferred size, either width or height in pixels or
   * ratio depending on the {@link LayoutManager}. Values > 0 and <= 1 are
   * ratios, 0 and values > 1 are pixels, and -1 means that the widget's
   * calculated preferred size should be used.
   * 
   * @param preferredSize the preferred size or -1 indicating that the widget's
   *          calculated preferred size should be used instead of this value.
   */
  public void setPreferredSize(double preferredSize) {
    this.preferredSize = preferredSize;
  }
}
