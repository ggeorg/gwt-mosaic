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

import com.google.gwt.user.client.ui.HasAlignment;

/**
 * Layout data object for {@link FillLayout}.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * @see FillLayout
 */
public class FillLayoutData extends LayoutData implements HasAlignment {

  private HorizontalAlignmentConstant horizontalAlignment;
  private VerticalAlignmentConstant verticalAlignment;

  /**
   * Creates a new instance of {@code FillLayoutData}. The associated widget
   * should be undecorated.
   */
  public FillLayoutData() {
    super(false);
  }

  /**
   * Creates a new instance of {@code FillLayoutData} by specifying that the
   * associated widget should be decorated if parameter {@code decorate} is
   * {@code true}, and undecorated if {@code false}.
   * 
   * @param decorate specifies whether the associated widget will be decorated
   *          or not.
   */
  public FillLayoutData(final boolean decorate) {
    super(decorate);
  }

  @Deprecated
  public FillLayoutData(HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment) {
    this(horizontalAlignment, verticalAlignment, false);
  }

  @Deprecated
  public FillLayoutData(HorizontalAlignmentConstant horizontalAlignment,
      VerticalAlignmentConstant verticalAlignment, final boolean decorate) {
    super(decorate);
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return horizontalAlignment;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    this.horizontalAlignment = align;
  }
  
  public void setHorizontalAlignment(String align) {
    align = align.trim().toLowerCase();
    if (align.equals("left".intern())) {
      setHorizontalAlignment(ALIGN_LEFT);
    } else if (align.equals("center".intern())) {
      setHorizontalAlignment(ALIGN_CENTER);
    } else if (align.equals("right".intern())) {
      setHorizontalAlignment(ALIGN_RIGHT);
    } else if (align.equals("default".intern())) {
      setHorizontalAlignment(ALIGN_DEFAULT);
    }
  }

  public VerticalAlignmentConstant getVerticalAlignment() {
    return verticalAlignment;
  }

  public void setVerticalAlignment(VerticalAlignmentConstant align) {
    this.verticalAlignment = align;
  }
  
  public void setVerticalAlignment(String align) {
    align = align.trim().toLowerCase();
    if (align.equals("top".intern())) {
      setVerticalAlignment(ALIGN_TOP);
    } else if (align.equals("middle".intern())) {
      setVerticalAlignment(ALIGN_MIDDLE);
    } else if (align.equals("bottom".intern())) {
      setVerticalAlignment(ALIGN_BOTTOM);
    }
  }

}
