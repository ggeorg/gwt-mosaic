/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of JGoodies Karsten Lentzsch nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.gwt.mosaic.forms.client.debug;

import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.layout.LayoutData;

import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that paints grid bounds if and only if the panel's layout manager is
 * a {@link ScrollFormLayout}. You can tweak the debug paint process by setting
 * a custom grid color.
 * <p>
 * This class is not intended to be extended. However, it is not marked as
 * {@code final} to allow users to subclass it for debugging purposes.
 * 
 * @author Karsten Lentzsch
 * @author georgopoulos.georgios(at)gmail.com
 * 
 * @see FormDebugUtils
 * 
 */
public class FormDebugPanel extends ScrollLayoutPanel {

  /**
   * The default color used to paint the form's debug grid.
   */
  private static final String DEFAULT_GRID_COLOR = "#F00";

  /**
   * Holds the color used to paint the debug grid.
   */
  private String gridColor = DEFAULT_GRID_COLOR;

  // Instance Creation ****************************************************

  /**
   * Constructs a FormDebugPanel with all options turned off.
   */
  public FormDebugPanel() {
    super();
  }

  /**
   * Constructs a FormDebugPanel on the given {@link FormLayout} instance.
   * 
   * @param layout the panel's {@link FormLayout} instance
   */
  public FormDebugPanel(FormLayout layout) {
    super(layout);
    setGridColor(DEFAULT_GRID_COLOR);
  }

  // Accessors ************************************************************

  /**
   * Sets the debug grid's color.
   * 
   * @param color the color used to paint the debug grid
   */
  public void setGridColor(String color) {
    gridColor = color;
  }

  // Painting *************************************************************

  @Override
  public void add(Widget widget, LayoutData layoutData) {
    super.add(widget, layoutData);
    final Widget child = getWidget(getWidgetIndex(widget));
    DOM.setStyleAttribute(child.getElement(), "border", "1px dotted "
        + gridColor);
  }
}
