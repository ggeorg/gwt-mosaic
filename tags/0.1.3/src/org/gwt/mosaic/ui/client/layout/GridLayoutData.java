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

import com.google.gwt.user.client.ui.DecoratorPanel;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public class GridLayoutData extends LayoutData {

  public int rowspan;
  public int colspan;

  public GridLayoutData() {
    this(1, 1);
  }
  
  public GridLayoutData(boolean decorate) {
    this(1, 1, decorate);
  }

  public GridLayoutData(int colSpan, int rowSpan) {
    super();
    setColspan(colSpan);
    setRowspan(rowSpan);
  }
  
  public GridLayoutData(int colSpan, int rowSpan, boolean decorate) {
    super();
    setColspan(colSpan);
    setRowspan(rowSpan);
    this.decoratorPanel = decorate ? new DecoratorPanel() : null;
  }

  protected int getRowspan() {
    return rowspan;
  }

  protected void setRowspan(int rowspan) {
    this.rowspan = Math.max(1, rowspan);
  }

  protected int getColspan() {
    return colspan;
  }

  protected void setColspan(int colspan) {
    this.colspan = Math.max(1, colspan);
  }

}
