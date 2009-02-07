/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Separator extends Composite implements HasHorizontalAlignment {

  private static final String DEFAULT_STYLENAME = "mosaic-FormSeparator";

  private final HorizontalAlignmentConstant align;

  public Separator(String text) {
    this(text, Separator.ALIGN_LEFT);
  }

  public Separator(String text, HorizontalAlignmentConstant align) {
    this.align = checkHorizontalAlignment(align);
    final HorizontalPanel panel = new HorizontalPanel();
    panel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    // panel.setWidth("100%");
    final Label l = new Label(text);
    l.setWordWrap(false);
    final HTML hr = new HTML("<hr></hr>");
    if (this.align == Separator.ALIGN_LEFT) {
      panel.add(l);
      panel.add(new HTML("&nbsp;"));
      panel.add(hr);
    } else {
      panel.add(hr);
      panel.add(new HTML("&nbsp;"));
      panel.add(l);
    }
    panel.setCellWidth(hr, "99%");
    initWidget(panel);
    setStyleName(DEFAULT_STYLENAME);
  }

  protected HorizontalAlignmentConstant checkHorizontalAlignment(
      HorizontalAlignmentConstant align) {
    if (align == Separator.ALIGN_CENTER) {
      throw new IllegalArgumentException("ALIGN_CENTER is not supported.");
    } else if (align == Separator.ALIGN_DEFAULT) {
      align = Separator.ALIGN_LEFT;
    }
    return align;
  }

  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return align;
  }

  public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
    throw new UnsupportedOperationException();
  }
}
