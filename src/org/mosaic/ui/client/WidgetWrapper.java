/*
 * Copyright 2008 Georgios J. Georgopoulos.
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
package org.mosaic.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

public class WidgetWrapper extends Composite {

  private static final String DEFAULT_STYLE_NAME = "mosaic-WidgetWrapper";

  public WidgetWrapper(Widget widget) {
    Grid grid = new Grid(1, 1);
    grid.setBorderWidth(0);
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    grid.getCellFormatter().setAlignment(0, 0, HasAlignment.ALIGN_CENTER,
        HasAlignment.ALIGN_MIDDLE);
    
    grid.setWidget(0, 0, widget);

    initWidget(grid);

    addStyleName(DEFAULT_STYLE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  protected Grid getWidget() {
    return (Grid) super.getWidget();
  }

}
