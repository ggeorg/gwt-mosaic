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
package org.gwt.mosaic.ui.client.layout;

import org.gwt.mosaic.core.client.DOM;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractDecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
class InternalDecoratorPanel extends AbstractDecoratorPanel {
  private int[] borderSizes;

  InternalDecoratorPanel() {
    super();
  }

  InternalDecoratorPanel(String[] rowStyles, int containerIndex) {
    super(rowStyles, containerIndex);
  }

  @Override
  public boolean remove(Widget w) {
    if (isAttached()) {
      removeFromParent();
    }
    return super.remove(w);
  }

  int[] getBorderSizes() {
    if (borderSizes == null) {
      borderSizes = new int[4];

      final Element topLeft = this.getCellElement(0, 0);
      final Element bottomRight = this.getCellElement(2, 2);

      final int[] m = DOM.getMarginSizes(getElement());
      final int[] b = DOM.getBorderSizes(getElement());
      
      borderSizes[0] = topLeft.getOffsetHeight() + b[0] + m[0];
      borderSizes[1] = bottomRight.getOffsetWidth() + b[1] + m[1];
      borderSizes[2] = bottomRight.getOffsetHeight() + b[2] + m[2];
      borderSizes[3] = topLeft.getOffsetWidth() + b[3] + m[3];
    }
    return borderSizes;
  }
}
