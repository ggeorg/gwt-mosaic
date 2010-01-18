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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractDecoratorPanel;

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

  int[] getBorderSizes() {
    if (borderSizes == null) {
      borderSizes = new int[4];

      final Element topLeft = this.getCellElement(0, 0);
      final Element bottomRight = this.getCellElement(2, 2);

      borderSizes[0] = topLeft.getOffsetHeight();
      borderSizes[1] = bottomRight.getOffsetWidth();
      borderSizes[2] = bottomRight.getOffsetHeight();
      borderSizes[3] = topLeft.getOffsetWidth();
    }
    return borderSizes;
  }
}
