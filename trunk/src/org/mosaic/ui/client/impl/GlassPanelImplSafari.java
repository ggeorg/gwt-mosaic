/*
 * Copyright 2007 Google Inc.
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
package org.mosaic.ui.client.impl;

import org.mosaic.ui.client.GlassPanel;

import com.google.gwt.user.client.Element;

/**
 * Safari implementation of {@link GlassPanelImpl}.
 */
public class GlassPanelImplSafari extends GlassPanelImplStandard {
  private static native void implMatchDocumentSize(Element elem)
  /*-{
    var scrollWidth = @org.mosaic.ui.client.impl.GlassPanelImpl::getWindowScrollWidth()();
    var scrollHeight = @org.mosaic.ui.client.impl.GlassPanelImpl::getWindowScrollHeight()();

    var width = Math.max($wnd.innerWidth, scrollWidth);
    var height = Math.max($wnd.innerHeight, scrollHeight);

    var computedStyle = $doc.defaultView.getComputedStyle($doc.documentElement, null);
    var marginLeft = parseInt(computedStyle.getPropertyValue("margin-left"));
    var marginTop = parseInt(computedStyle.getPropertyValue("margin-top"));
    
    // TODO Verify 'negative HTML element margins' case
    elem.style.marginLeft = -marginLeft + "px";
    elem.style.marginTop = -marginTop + "px";
    
    elem.style.width = (width + marginLeft) + "px";
    elem.style.height = (height + marginTop) + "px";
  }-*/;

  public void matchDocumentSize(GlassPanel glassPanel, boolean dueToResize) {
    implMatchDocumentSize(glassPanel.getElement());
  }
}
