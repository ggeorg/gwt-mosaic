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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class WidgetWrapper extends Composite implements HasAlignment {

  private static final String DEFAULT_STYLE_NAME = "mosaic-WidgetWrapper";

  private HorizontalAlignmentConstant alignLeft;
  private VerticalAlignmentConstant alignTop;

  public WidgetWrapper(Widget widget) {
    this(widget, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
  }

  public WidgetWrapper(Widget widget, HorizontalAlignmentConstant alignLeft,
      VerticalAlignmentConstant alignTop) {
    Grid grid = new Grid(1, 1);
    grid.setBorderWidth(0);
    grid.setCellPadding(0);
    grid.setCellSpacing(0);
    this.alignLeft = alignLeft;
    this.alignTop = alignTop;
    setAlignment(grid);
    grid.setWidget(0, 0, widget);
    initWidget(grid);
    addStyleName(DEFAULT_STYLE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.google.gwt.user.client.ui.Composite#getWidget()
   */
  public Grid getWidget() {
    return (Grid) super.getWidget();
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return alignLeft;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    alignLeft = align;
    setAlignment(getWidget());
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return alignTop;
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    alignTop = align;
    setAlignment(getWidget());
  }

  private void setAlignment(Grid grid) {
    grid.getCellFormatter().setAlignment(0, 0, alignLeft, alignTop);
  }

}
