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

import com.google.gwt.user.client.ui.HasAlignment;

/**
 * {@code GridLayoutData} is the layout data object associated with
 * {@link GridLayout}. To set a {@code GridLayoutData} object into a widget, use
 * the {@link LayoutPanel#add(com.google.gwt.user.client.ui.Widget, LayoutData)}
 * method.
 * <p>
 * NOTE: Do not reuse {@code GridLayouData} objects. Every widget in a {@code
 * LayoutPanel} that is managed by a {@link GridLayout} must have a unique
 * {@code GridLayoutData} object. If the layout data for a widget in a
 * {@link GridLayout} is {@code null} at layout time, a unique {@code
 * GridLayoutData} object is created for it.
 * 
 * @see GridLayout
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class GridLayoutData extends LayoutData implements HasAlignment {

  public int rowspan;
  public int colspan;

  private HorizontalAlignmentConstant horizontalAlignment;
  private VerticalAlignmentConstant verticalAlignment;

  public GridLayoutData() {
    this(1, 1);
  }

  public GridLayoutData(boolean decorate) {
    this(1, 1, decorate);
  }

  public GridLayoutData(HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment) {
    this(1, 1, horizontalAlignment, verticalAlignment);
  }

  public GridLayoutData(HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment, boolean decorate) {
    this(1, 1, horizontalAlignment, verticalAlignment, decorate);
  }

  public GridLayoutData(int colSpan, int rowSpan) {
    super(false);
    setColspan(colSpan);
    setRowspan(rowSpan);
  }

  /**
   * 
   * @param colSpan
   * @param rowSpan
   * @param decorate
   */
  public GridLayoutData(int colSpan, int rowSpan, boolean decorate) {
    super(decorate);
    setColspan(colSpan);
    setRowspan(rowSpan);
  }

  public GridLayoutData(int colSpan, int rowSpan,
      HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment) {
    super(false);
    setColspan(colSpan);
    setRowspan(rowSpan);
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  public GridLayoutData(int colSpan, int rowSpan,
      HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment, boolean decorate) {
    super(decorate);
    setColspan(colSpan);
    setRowspan(rowSpan);
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  protected int getColspan() {
    return colspan;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasHorizontalAlignment#getHorizontalAlignment
   * ()
   */
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  protected int getRowspan() {
    return rowspan;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasVerticalAlignment#getVerticalAlignment()
   */
  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  protected void setColspan(int colspan) {
    this.colspan = Math.max(1, colspan);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasHorizontalAlignment#setHorizontalAlignment
   * (com.google.gwt.user.client.ui.HasHorizontalAlignment.
   * HorizontalAlignmentConstant)
   */
  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }

  protected void setRowspan(int rowspan) {
    this.rowspan = Math.max(1, rowspan);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.user.client.ui.HasVerticalAlignment#setVerticalAlignment
   * (com.
   * google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant)
   */
  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }

}
